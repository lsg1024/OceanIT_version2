package com.example.oceanit.View

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.oceanit.DB.AppDatabase
import com.example.oceanit.DB.User
import com.example.oceanit.DTO.LoginDTO
import com.example.oceanit.DTO.LoginData
import com.example.oceanit.R
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
    lateinit var E_id : EditText
    lateinit var E_pw : EditText
    lateinit var loginBtn : Button
    lateinit var db : AppDatabase
    var id : String = ""
    var pw : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_activty)

        E_id = findViewById(R.id.login_id)
        E_pw = findViewById(R.id.login_password)
        loginBtn = findViewById(R.id.login_button)
        db = AppDatabase.getDBInstance(this.applicationContext)!!

//        파이어베이스 FCM 을 위한 디바이스 토큰 추출
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            } else {
                Log.d("token_error", "token_error")
            }

            // Get new FCM registration token
            token = task.result.toString()
            // Log
            Log.d("토큰_값", token)

        })

        // 유저키 유무 확인 -> 로그인 유지 위해 없으면 if 문 아니면 else 문 실행
        if (Loginkey.getUserKey(this@LoginActivity).isEmpty()) {
            Log.d("Login_key", "key_data 없음")

            loginBtn.setOnClickListener {loginData()}

        } else {
            Log.d("Login_key", "응답 ${Loginkey.getUserKey(this@LoginActivity)}")
            Log.d("DataBase 저장 완료", "${db.UserDao()!!.getUser(0)}")

            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            Toast.makeText(this@LoginActivity, "로그인 성공", Toast.LENGTH_LONG).show()
            startActivity(intent)
            finish()

        }
    }
    private fun loginData() {
        // 로그인 아이디 패스워드 입력후 버튼 눌러서 전송
        id = E_id.text.toString()
        pw = E_pw.text.toString()

        if (id.isEmpty() || pw.isEmpty()) {
            showErrorMessage("아이디 또는 비밀번호를 입력하세요.")
            return
        }
        login()
    }


    private fun login() {

        val user = User(loginId = id, loginPw = pw)

        call?.login(token, LoginData(E_id.text.toString(), E_pw.text.toString()))?.enqueue(object : Callback<LoginDTO>{

            override fun onResponse(call: Call<LoginDTO>, response: Response<LoginDTO>) {
                if (response.isSuccessful) {
                    val result : LoginDTO? = response.body()

                    Log.d("Login_Log", "$result")

                    if (result?.result?.user_key != null) {
                        Loginkey.setUserKey(this@LoginActivity, result.result.user_key.toInt())
                        Loginkey.setTokenKey(this@LoginActivity, token)

                        Log.d("Login_key", "${result.result.user_key.toInt()}")
                        Log.d("setTokenKey", token)

                        db.UserDao()!!.insert(user)
                        Log.d("DataBase 저장 완료", "${db.UserDao()!!.getUser(0)}")

                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        Toast.makeText(this@LoginActivity, "로그인 성공", Toast.LENGTH_LONG).show()
                        startActivity(intent)
                        finish()

                    } else {
                        Toast.makeText(this@LoginActivity, "아이디 또는 비밀번호가 잘못되었습니다", Toast.LENGTH_SHORT).show()
                        Log.d("Login_Log", "user_key null")
                    }

                }
            }

            override fun onFailure(call: Call<LoginDTO>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "로그인 실패", Toast.LENGTH_LONG).show()
                Log.d("Login_Log", "${t.message}")
            }

        })
    }

    private fun showErrorMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}