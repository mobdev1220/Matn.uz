package uz.uicgroup.presentation.screens.about.policy.viewmodel

import androidx.lifecycle.LiveData

interface PolicyViewModel {
    val backLiveData:LiveData<Unit>
    fun backScreen()
}