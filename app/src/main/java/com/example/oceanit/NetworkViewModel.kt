package com.example.oceanit

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

@SuppressLint("NewApi")
class NetworkViewModel(application: Application) : AndroidViewModel(application) {

    private val _isConnected = MutableLiveData<Boolean>()

    val isConnected: LiveData<Boolean>
        get() = _isConnected

    //    connectivityManager 는 시스템의 ConnectivityManager 객체 생성 변수
    private val connectivityManager = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    //    init 은 NetworkCallback 객체를 생성해 Network 상태를 수신해 _isConnected 객체 값들을 변경한다
    init {
        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                _isConnected.postValue(true)
            }

            override fun onLost(network: Network) {
                _isConnected.postValue(false)
            }
        }
        connectivityManager.registerDefaultNetworkCallback(networkCallback)
    }

    //    디바이스 Network 상태를 확인하는 함수
    fun checkNetworkConnection() {
//    네트워크 상태를 체크한다 -> 연결 여부
        val network = connectivityManager.activeNetwork
//    네트워크 기능을 체크한다
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        val isInternet = capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
        _isConnected.postValue(isInternet)
    }
}