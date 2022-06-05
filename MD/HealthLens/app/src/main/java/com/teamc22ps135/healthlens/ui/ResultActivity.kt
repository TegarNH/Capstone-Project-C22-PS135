package com.teamc22ps135.healthlens.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL
import com.teamc22ps135.healthlens.R
import com.teamc22ps135.healthlens.adapter.ProductRecomAdapter
import com.teamc22ps135.healthlens.data.remote.response.ProductList
import com.teamc22ps135.healthlens.databinding.ActivityResultBinding
import com.teamc22ps135.healthlens.viewmodel.ResultViewModel

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding
    private lateinit var resultViewModel: ResultViewModel
    private var progressDialog: AlertDialog? = null
    private lateinit var productRecomAdapter: ProductRecomAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.title = getString(R.string.title_actionbar_result)

        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAdapter()
        setupViewmodel()

        binding.btnBackHome.setOnClickListener {
            onBackPressed()
        }

        binding.btnDetectAgain.setOnClickListener {
            showDialogChooseDetection()
        }
    }

    private fun getIdDetection(): String? {
        return intent.getStringExtra(ReviewDetectActivity.KEY_ID_DETECTION)
    }

    private fun saveStateChooseDetection(state: String) {
        val preferences =
            getSharedPreferences(MainActivity.PREFS_CHOOSE_DETECTION, Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString(MainActivity.KEY_SKIN, state)
        editor.apply()
    }

    private fun setupViewmodel() {
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
            binding.resultSkin.text = result.resultDetection
            setRecommendationList(result.recommendationList)
            setProductRecomList(result.productList)
        }
    }

    private fun setupAdapter() {
        productRecomAdapter = ProductRecomAdapter()

        val horizontalLayout = LinearLayoutManager(
            this,
            HORIZONTAL,
            false
        )

        binding.rvProductRecommended.layoutManager = horizontalLayout
        binding.rvProductRecommended.setHasFixedSize(true)
        binding.rvProductRecommended.adapter = productRecomAdapter
    }

    private fun setRecommendationList(listRecommendation: ArrayList<String>) {
        val builder = StringBuilder()
        var number = 0
        listRecommendation.forEach { items ->
            number += 1
            builder.append(
                "$number. $items\n"
            )
        }
        binding.pointRecommendation.text = builder
    }

    private fun setProductRecomList(listProductRecom: List<ProductList>) {
        productRecomAdapter.setDataProductRecom(listProductRecom)
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
            setPositiveButton(getString(R.string.prompt_ok)) { _, _ ->
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

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this@ResultActivity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }

    private fun isLoading(loading: Boolean) {
        if (loading) {
            val dialog = AlertDialog.Builder(this)
            dialog.apply {
                setView(
                    View.inflate(
                        this@ResultActivity,
                        R.layout.layout_dialog_progress_getting_result,
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
                setupViewmodel()
            }
            create()
            show()
        }
    }
}