package com.example.oceanit.Firebase

import android.annotation.SuppressLint
import com.google.firebase.messaging.FirebaseMessagingService
import android.util.Log

class MyFirebaseMessagingService : FirebaseMessagingService() {

    @SuppressLint("LongLogTag")
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // FCM 이 아닌 서버간 토큰 연동 작업 실행
        Log.d("MyFirebaseMessagingService", "Refreshed token:$token")
    }
}