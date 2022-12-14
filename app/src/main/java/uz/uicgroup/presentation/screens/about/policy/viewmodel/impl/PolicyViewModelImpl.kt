package uz.uicgroup.presentation.screens.about.policy.viewmodel.impl

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import uz.uicgroup.presentation.screens.about.policy.viewmodel.PolicyViewModel
import javax.inject.Inject

@HiltViewModel
class PolicyViewModelImpl @Inject constructor() : ViewModel(), PolicyViewModel {
    override val backLiveData = MutableLiveData<Unit>()

    override fun backScreen() {
        backLiveData.value = Unit
    }
}