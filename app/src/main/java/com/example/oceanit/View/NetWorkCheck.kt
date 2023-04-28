package com.example.oceanit.View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.activity.viewModels
import com.example.oceanit.NetworkViewModel
import com.example.oceanit.R

class NetWorkCheck : AppCompatActivity() {

    private val viewModel: NetworkViewModel by viewModels()
    private lateinit var layoutDisconnected: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.networkcheck)

        layoutDisconnected = findViewById(R.id.layoutDisconnected)
        layoutDisconnected.visibility = View.GONE

        viewModel.checkNetworkConnection()

        viewModel.isConnected.observe(this) { isConnected ->
            // 인터넷 연결 상태에 따른 동작 수행
            if (isConnected) {
                // 인터넷이 연결된 상태
                layoutDisconnected.visibility = View.GONE
                startActivity(Intent(this@NetWorkCheck, LoginActivity::class.java))
                finish()
            } else {
                layoutDisconnected.visibility = View.VISIBLE
            }
        }
    }
}