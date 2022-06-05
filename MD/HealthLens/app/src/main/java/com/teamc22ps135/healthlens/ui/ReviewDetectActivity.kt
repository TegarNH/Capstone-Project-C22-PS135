package com.teamc22ps135.healthlens.ui

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.teamc22ps135.healthlens.R
import com.teamc22ps135.healthlens.databinding.ActivityReviewDetectBinding
import com.teamc22ps135.healthlens.util.rotateBitmap
import com.teamc22ps135.healthlens.util.rotateFileImage
import com.teamc22ps135.healthlens.util.uriToFile
import com.teamc22ps135.healthlens.viewmodel.ReviewDetectViewModel
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class ReviewDetectActivity : AppCompatActivity() {

    private lateinit var reviewDetectViewModel: ReviewDetectViewModel
    private lateinit var binding: ActivityReviewDetectBinding
    private var filePhoto: File? = null
    private var progressDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        binding = ActivityReviewDetectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        reviewDetectViewModel = ViewModelProvider(this)[ReviewDetectViewModel::class.java]

        setImagePreview()

        binding.btnTryAgain.setOnClickListener {
            onBackPressed()
        }

        binding.btnProcess.setOnClickListener { showDialogConfirmationUpload() }
    }

    private fun setImagePreview() {
        val resultCode = intent.getIntExtra(KEY_RESULT_CODE, 0)
        if (resultCode == GALLERY_RESULT) {
            setImageFromGallery()
        } else if (resultCode == CAMERA_X_RESULT) {
            setImageFromCamera()
        }
    }

    private fun setImageFromGallery() {
        val selectedImg = intent.getParcelableExtra<Uri>("uri") as Uri

        val myFile = uriToFile(selectedImg, this)
        filePhoto = myFile

        binding.ivImage.setImageURI(selectedImg)
    }

    private fun setImageFromCamera() {
        val myFile = intent.getSerializableExtra(DetectionActivity.KEY_PICTURE) as File
        val isFrontCamera = intent.getBooleanExtra(DetectionActivity.KEY_IS_FRONT_CAMERA, true)

        val result = rotateBitmap(
            BitmapFactory.decodeFile(myFile.path),
            isFrontCamera
        )

        filePhoto = rotateFileImage(myFile, isFrontCamera)

        binding.ivImage.setImageBitmap(result)
    }

    private fun showDialogConfirmationUpload() {
        AlertDialog.Builder(this).apply {
            setTitle(getString(R.string.title_confirmation))
            setMessage(getString(R.string.message_confirmation_upload))
            setPositiveButton(getString(R.string.prompt_yes)) { _, _ ->
                uploadPhoto()
            }
            setNegativeButton(getString(R.string.prompt_cancel)) { dialog, _ -> dialog.cancel() }
            create()
            show()
        }
    }

    private fun getStateChooseDetection(): String? {
        val preferences =
            getSharedPreferences(MainActivity.PREFS_CHOOSE_DETECTION, Context.MODE_PRIVATE)
        return preferences.getString(MainActivity.KEY_SKIN, null)
    }

    private fun uploadPhoto() {
        val file = filePhoto as File
        val typeDetection = getStateChooseDetection() as String

        val partTypeDetection = typeDetection.toRequestBody("text/plain".toMediaType())
        val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            NAME_PICTURE_MULTIPART,
            file.name,
            requestImageFile
        )

        reviewDetectViewModel.uploadPhoto(imageMultipart, partTypeDetection)

        reviewDetectViewModel.isLoading.observe(this@ReviewDetectActivity) {
            it.getContentIfNotHandled()?.let { state ->
                isLoading(state)
            }
        }

        reviewDetectViewModel.isFailed.observe(this@ReviewDetectActivity) {
            it.getContentIfNotHandled()?.let {
                isFailed()
            }
        }

        reviewDetectViewModel.isUploadSuccess.observe(this@ReviewDetectActivity) {
            it.getContentIfNotHandled()?.let {
                reviewDetectViewModel.idDetection.observe(this@ReviewDetectActivity) { id ->
                    isUploadSuccess(idDetection = id)
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun isLoading(loading: Boolean) {
        if (loading) {
            val dialog = AlertDialog.Builder(this)
            dialog.apply {
                setView(
                    View.inflate(
                        this@ReviewDetectActivity,
                        R.layout.layout_dialog_progress_detecting,
                        null
                    )
                )
                setCancelable(false)
            }
            progressDialog = dialog.create()
            progressDialog!!.show()
        } else {
            progressDialog?.dismiss()
        }
    }

    private fun isFailed() {
        AlertDialog.Builder(this).apply {
            setTitle(getString(R.string.title_error))
            setMessage(getString(R.string.message_error))
            setPositiveButton(getString(R.string.prompt_try_again)) { _, _ ->
                uploadPhoto()
            }
            create()
            show()
        }
    }

    private fun isUploadSuccess(idDetection: String?) {
        val intent = Intent(this@ReviewDetectActivity, ResultActivity::class.java)
        intent.putExtra(
            KEY_ID_DETECTION,
            idDetection
        )
        startActivity(intent)
        finish()
    }

    companion object {
        const val CAMERA_X_RESULT = 10
        const val GALLERY_RESULT = 20

        private const val NAME_PICTURE_MULTIPART = "picture_path"
        const val KEY_RESULT_CODE = "resultCode"
        const val KEY_ID_DETECTION = "idDetection"
    }
}
