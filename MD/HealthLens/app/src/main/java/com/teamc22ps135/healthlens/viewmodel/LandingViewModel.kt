package com.teamc22ps135.healthlens.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.teamc22ps135.healthlens.data.model.FirstInstall
import com.teamc22ps135.healthlens.data.pref.FirstInstallPreference
import kotlinx.coroutines.launch

class LandingViewModel(private val pref: FirstInstallPreference) : ViewModel() {
    fun getStateFirstInstall(): LiveData<FirstInstall> {
        return pref.getStateFirstInstall().asLiveData()
    }

    private fun setStateFirstInstall(firstInstall: FirstInstall) {
        viewModelScope.launch {
            pref.setStateFirstInstall(firstInstall)
        }
    }

    fun saveState(state: Boolean) {
        setStateFirstInstall(FirstInstall(isFirstInstall = state))
    }
}