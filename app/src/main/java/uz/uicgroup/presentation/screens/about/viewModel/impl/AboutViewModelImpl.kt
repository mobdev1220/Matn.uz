package uz.uicgroup.presentation.screens.about.viewModel.impl

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import uz.uicgroup.data.local.SharedPref
import uz.uicgroup.presentation.screens.about.viewModel.AboutViewModel
import uz.uicgroup.utils.Theme.goInDarkMode
import uz.uicgroup.utils.Theme.goInLightMode
import uz.uicgroup.utils.extension.eventValueFlow
import javax.inject.Inject

@HiltViewModel
class AboutViewModelImpl @Inject constructor(
    private val sharedPref: SharedPref
) : ViewModel(), AboutViewModel {
    override val backScreenLiveData = MutableLiveData<Unit>()
    override val nightModeLiveData = MutableLiveData<Boolean>()
    override val sharedPrefValue = eventValueFlow<Boolean>()

    override fun backScreen() {
        backScreenLiveData.value = Unit
    }

    override fun setTheme(nightMode: Boolean) {
        if (nightMode) {
            goInLightMode()
            sharedPref.theme = false
            nightModeLiveData.value = sharedPref.theme
        } else {
            goInDarkMode()
            sharedPref.theme = true
            nightModeLiveData.value = sharedPref.theme
        }
    }

    override fun getSharedPrefThemeValue() {
        viewModelScope.launch {
            sharedPrefValue.emit(sharedPref.theme)
        }
    }
}