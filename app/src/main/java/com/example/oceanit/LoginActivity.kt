package com.example.oceanit

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.oceanit.DTO.LoginDTO
import com.example.oceanit.DTO.LoginData
import com.example.oceanit.Retrofit.Loginkey
import com.example.oceanit.Retrofit.Retrofit2
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    val call by lazy { Retrofit2.getInstance() }
    var token : String =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_activty)

        val E_id : EditText = findViewById(R.id.login_id)
        val E_pw : EditText = findViewById(R.id.login_password)
        val loginB : Button = findViewById(R.id.login_button)

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            token = task.result.toString()
            // Log
            Log.d("토큰", token)

        })

        // 유저키 유무 확인 -> 로그인 유지 위해 없으면 if 문 아니면 else 문 실행
        if (Loginkey.getUserKey(this@LoginActivity).isEmpty()) {
            Log.d("Login_key", "key_data 없음")

            loginB.setOnClickListener {
                // 로그인 아이디 패스워드 입력후 버튼 눌러서 전송
                val id = E_id.text.toString()
                val pw = E_pw.text.toString()
                Log.d("Login_Log", "$id + $pw")
                call?.login(token, LoginData(id, pw))?.enqueue(object : Callback<LoginDTO>{

                    override fun onResponse(call: Call<LoginDTO>, response: Response<LoginDTO>) {
                        if (response.isSuccessful) {
                            val result : LoginDTO? = response.body()

                            Log.d("Login_Log", "$result")

                            Loginkey.setUserKey(this@LoginActivity, result!!.result.user_key!!.toInt())
                            Loginkey.setTokenKey(this@LoginActivity, token)

                            Log.d("Login_key", "${result.result.user_key!!.toInt()}")
                            Log.d("setTokenKey", "${token}")

                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            Toast.makeText(this@LoginActivity, "로그인 성공", Toast.LENGTH_LONG).show()
                            startActivity(intent)
                            finish()
                        }
                    }

                    override fun onFailure(call: Call<LoginDTO>, t: Throwable) {
                        Toast.makeText(this@LoginActivity, "로그인 실패", Toast.LENGTH_LONG).show()
                        Log.d("Login_Log", "${t.message}")
                    }

                })
            }

        } else {
            Log.d("Login_key", "응답 ${Loginkey.getUserKey(this@LoginActivity)}")

            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            Toast.makeText(this@LoginActivity, "로그인 성공", Toast.LENGTH_LONG).show()
            startActivity(intent)
            finish()

        }

    }


}