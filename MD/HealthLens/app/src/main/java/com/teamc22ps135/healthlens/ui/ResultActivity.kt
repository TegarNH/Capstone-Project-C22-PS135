package com.teamc22ps135.healthlens.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.teamc22ps135.healthlens.R
import com.teamc22ps135.healthlens.adapter.RecommendationAdapter
import com.teamc22ps135.healthlens.data.remote.response.Story
import com.teamc22ps135.healthlens.databinding.ActivityResultBinding
import com.teamc22ps135.healthlens.viewmodel.ResultViewModel

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding
    private lateinit var resultViewModel: ResultViewModel
    private var progressDialog: AlertDialog? = null
    private lateinit var recommendationAdapter: RecommendationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.title = "Detection Result"

        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        resultViewModel = ViewModelProvider(this)[ResultViewModel::class.java]

        resultViewModel.uploadPhoto(getIdDetection())

        resultViewModel.isLoading.observe(this@ResultActivity) {
            it.getContentIfNotHandled()?.let { state ->
                isLoading(state)
            }
        }

        resultViewModel.isFailed.observe(this@ResultActivity) {
            it.getContentIfNotHandled()?.let {
                isFailed()
            }
        }

        resultViewModel.resultDetection.observe(this@ResultActivity) { result ->
            binding.resultSkin.text = result.message
            setRecommendationList(result.listStory)
        }

        binding.btnBackHome.setOnClickListener {
            onBackPressed()
        }

        binding.btnDetectAgain.setOnClickListener {
            showDialogChooseDetection()
        }

        recommendationAdapter = RecommendationAdapter()

        binding.rvRecommendation.layoutManager = LinearLayoutManager(this)
        binding.rvRecommendation.setHasFixedSize(true)
        binding.rvRecommendation.adapter = recommendationAdapter
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this@ResultActivity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }

    private fun setRecommendationList(listRecommendation: List<Story>) {
        recommendationAdapter.setDataRecommendation(listRecommendation)
    }

    private fun isLoading(loading: Boolean) {
        if (loading) {
            val dialog = AlertDialog.Builder(this)
            dialog.apply {
                setView(
                    View.inflate(
                        this@ResultActivity,
                        R.layout.layout_dialog_progress,
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
            setTitle("Error")
            setMessage("Oppss")
            setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            create()
            show()
        }
    }

    private fun showDialogChooseDetection() {
        var selectedDetectionIndex = 0
        val detection = arrayOf(
            getString(R.string.title_skin_problems),
            getString(R.string.title_skin_type)
        )
        var selectedDetection = detection[selectedDetectionIndex]

        AlertDialog.Builder(this).apply {
            setTitle(getString(R.string.tv_choose_detection))
            setSingleChoiceItems(detection, selectedDetectionIndex) { _, which ->
                selectedDetectionIndex = which
                selectedDetection = detection[which]
            }
            setPositiveButton("OK") { _, _ ->
                if (selectedDetection == "Skin Problems") {
                    saveStateChooseDetection(MainActivity.VALUE_SKIN_PROBLEMS)
                } else {
                    saveStateChooseDetection(MainActivity.VALUE_SKIN_TYPE)
                }

                val intent = Intent(this@ResultActivity, GuidelineActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
                finish()
            }
            setNegativeButton(getString(R.string.prompt_cancel)) { dialog, _ -> dialog.cancel() }
            create()
            show()
        }
    }

    private fun getIdDetection(): String? {
        return intent.getStringExtra("idDetection")
    }

    private fun saveStateChooseDetection(state: String) {
        val preferences =
            getSharedPreferences(MainActivity.PREFS_CHOOSE_DETECTION, Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString(MainActivity.KEY_SKIN, state)
        editor.apply()
    }
}