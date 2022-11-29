package uz.uicgroup.presentation

import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.r0adkll.slidr.Slidr
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import uz.uicgroup.R
import uz.uicgroup.data.local.SharedPref
import uz.uicgroup.presentation.screen.edit.dialog.BottomDialog
import uz.uicgroup.presentation.screen.edit.dialog.NetWorkDialog
import uz.uicgroup.utils.ConnectivityReceiver
import uz.uicgroup.utils.Open
import uz.uicgroup.utils.Theme
import uz.uicgroup.utils.extension.showToast
import uz.uicgroup.utils.internetConnection.ConnectivityObserver
import uz.uicgroup.utils.internetConnection.NetworkConnectivityObserver

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main),
    ConnectivityReceiver.ConnectivityReceiverListener {
    private var shared: SharedPref? = null
    lateinit var connectivityObserver: ConnectivityObserver

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        if (isConnected) {
            Open.openScreen.value = Unit
        } else
            Open.notInternet.value = Unit
    }

    private fun registerConnectivityReceiver() {
        registerReceiver(
            ConnectivityReceiver(),
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerConnectivityReceiver()
        connectivityObserver = NetworkConnectivityObserver(applicationContext)
        lifecycleScope.launch {
            connectivityObserver.observer().collect {
                when (it) {
                    ConnectivityObserver.Status.UnAvailable -> {
                        showDialog(true)
                    }
                    ConnectivityObserver.Status.Lost -> {
                        showDialog(true)
                    }
                    ConnectivityObserver.Status.Losing -> {
                        showDialog(true)
                    }
                    else -> {

                    }
                }
            }
        }
        shared = SharedPref(this)
        if (shared!!.theme)
            Theme.goInDarkMode()
        else
            Theme.goInLightMode()
    }

    override fun onResume() {
        super.onResume()
        ConnectivityReceiver.connectivityReceiverListener = this
    }

    private fun showDialog(isNetwork: Boolean) {
        val dialog = NetWorkDialog(isNetwork)
        dialog.show(supportFragmentManager, "")
    }
}