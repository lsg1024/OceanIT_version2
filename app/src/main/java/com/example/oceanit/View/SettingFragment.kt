package com.example.oceanit.View

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.oceanit.DB.AppDatabase
import com.example.oceanit.DTO.*
import com.example.oceanit.R
import com.example.oceanit.Retrofit.Loginkey
import com.example.oceanit.Retrofit.Retrofit2
import com.example.oceanit.ViewModel.CompanyViewModel
import com.example.oceanit.ViewModel.LoginServiceViewModel
import com.example.oceanit.ViewModel.SensorChangeViewModel
import com.google.android.material.slider.RangeSlider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat

class SettingFragment : Fragment() {

    private lateinit var rangeSlider: RangeSlider
    private lateinit var rangeSlider2: RangeSlider
    private lateinit var rangeSlider3: RangeSlider
    private lateinit var rangeSlider4: RangeSlider
    private lateinit var rangeSlider5: RangeSlider
    private lateinit var rangeSlider6: RangeSlider

    private lateinit var watertank_name: TextView
    private lateinit var watertank_name2: TextView
    private lateinit var watertank_name3: TextView
    private lateinit var watertank_name4: TextView
    private lateinit var watertank_name5: TextView
    private lateinit var watertank_name6: TextView

    private lateinit var num1_1: EditText
    private lateinit var num2_1: EditText
    private lateinit var num3_1: EditText
    private lateinit var num4_1: EditText
    private lateinit var num5_1: EditText
    private lateinit var num6_1: EditText

    private lateinit var num1_2: EditText
    private lateinit var num2_2: EditText
    private lateinit var num3_2: EditText
    private lateinit var num4_2: EditText
    private lateinit var num5_2: EditText
    private lateinit var num6_2: EditText

    private lateinit var name : TextView
    private lateinit var addr : TextView
    private lateinit var tel : TextView
    private lateinit var ceo : TextView
    private lateinit var button: Button

    private lateinit var constraint : ConstraintLayout

    private var number1: String = ""
    private lateinit var logout_btn : Button

    private val call by lazy { Retrofit2.getInstance() }

    private var user_key : Int = 0
    private var user_token : String = ""

    private lateinit var mainActivity: MainActivity

    private lateinit var companyViewModel : CompanyViewModel
    private lateinit var  sensorChangeViewModel : SensorChangeViewModel
    private lateinit var loginServiceViewModel: LoginServiceViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_setting, container, false)

        // 회사 이미지 받아오기 서버에서
        name = view.findViewById(R.id.name)
        addr = view.findViewById(R.id.addr)
        tel = view.findViewById(R.id.tel)
        ceo = view.findViewById(R.id.ceo)
        // 설정 완료 후 확인 버튼
        button = view.findViewById(R.id.setb)

        rangeSlider = view.findViewById(R.id.rangeSlider)
        rangeSlider2 = view.findViewById(R.id.rangeSlider2)
        rangeSlider3 = view.findViewById(R.id.rangeSlider3)
        rangeSlider4 = view.findViewById(R.id.rangeSlider4)
        rangeSlider5 = view.findViewById(R.id.rangeSlider5)
        rangeSlider6 = view.findViewById(R.id.rangeSlider6)

        watertank_name = view.findViewById(R.id.watertank_name)
        watertank_name2 = view.findViewById(R.id.watertank_name02)
        watertank_name3 = view.findViewById(R.id.watertank_name03)
        watertank_name4 = view.findViewById(R.id.watertank_name04)
        watertank_name5 = view.findViewById(R.id.watertank_name05)
        watertank_name6 = view.findViewById(R.id.watertank_name06)

        num1_1 = view.findViewById(R.id.num1_1)
        num2_1 = view.findViewById(R.id.num2_1)
        num3_1 = view.findViewById(R.id.num3_1)
        num4_1 = view.findViewById(R.id.num4_1)
        num5_1 = view.findViewById(R.id.num5_1)
        num6_1 = view.findViewById(R.id.num6_1)

        num1_2 = view.findViewById(R.id.num1_2)
        num2_2 = view.findViewById(R.id.num2_2)
        num3_2 = view.findViewById(R.id.num3_2)
        num4_2 = view.findViewById(R.id.num4_2)
        num5_2 = view.findViewById(R.id.num5_2)
        num6_2 = view.findViewById(R.id.num6_2)
        logout_btn = view.findViewById(R.id.logout_btn)

        constraint = view.findViewById(R.id.constraint)

        mainActivity = context as MainActivity

        companyViewModel = ViewModelProvider(this)[CompanyViewModel::class.java]
        sensorChangeViewModel = ViewModelProvider(this)[SensorChangeViewModel::class.java]
        loginServiceViewModel = ViewModelProvider(this)[LoginServiceViewModel::class.java]

        companyViewModel.companyData.observe(viewLifecycleOwner, Observer {data ->
            name.text = data!!.result.company
            addr.text = data.result.addr
            tel.text = data.result.tel
            ceo.text = data.result.ceo
        })

        button.setOnClickListener {
            sensorChangeViewModel.sendSensorData(user_key, getSensorData())
        }

//        logout_btn.setOnClickListener {
//            loginServiceViewModel.
//        }

        val db = AppDatabase.getDBInstance(mainActivity)

        user_key = Loginkey.getUserKey(mainActivity).toInt()
        user_token = Loginkey.getTokenKey(mainActivity)
        Log.d("user_token_get", user_token)

//      임계치 최대치를 입력받아 슬라이더 최대 값을 설정해준다.
        call?.Sensor_OG(user_key)?.enqueue(object : Callback<SensorDTO> {

            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<SensorDTO>, response: Response<SensorDTO>) {
                if (response.isSuccessful) {

                    val result : SensorDTO? = response.body()
                    val mFormat = DecimalFormat("#.##")

                    Log.d("Sensor_value", "$result")

                    // 데이터 EditText에 저장
                    num1_1.setText(num1_1.text.toString() + result!!.result.Tc_low.toString())
                    num1_2.setText(num1_2.text.toString() + result.result.Tc_high.toString())
//                     키보드 입력 값을 받아오고 슬라이더 모양 변화
                    keyborad(num1_1)
                    keyborad(num1_2)
                    // 최대 측량 범위
                    rangeSlider.valueFrom = mFormat.format(result.result.Tc_low).toFloat() - 10
                    // 서버에서 보내준 max 값 + 10f 방식 이용도 가능 가변적인 변경
                    rangeSlider.valueTo = mFormat.format(result.result.Tc_high).toFloat() + 10
                    // 슬라이더 움직일때 조절되는 단위
                    rangeSlider.stepSize = 0f
                    // 서버에서 지정된 사용자의 min max 값 받아오기
                    rangeSlider.setValues(mFormat.format(result.result.Tc_low).toFloat(), mFormat.format(result.result.Tc_high).toFloat())

// ---------------------------------------------------------------------------------------------------- //

                    // 데이터 EditText에 저장
                    num2_1.setText(num2_1.text.toString() + result.result.DO_low.toString())
                    num2_2.setText(num2_2.text.toString() + result.result.DO_high.toString())

                    keyborad(num2_1)
                    keyborad(num2_2)

                    rangeSlider2.valueFrom = mFormat.format(result.result.DO_low - 10).toFloat()
                    rangeSlider2.valueTo = mFormat.format(result.result.DO_high + 10).toFloat()
                    rangeSlider2.stepSize = 0f
                    rangeSlider2.setValues(mFormat.format(result.result.DO_low).toFloat(), mFormat.format(result.result.DO_high).toFloat())

// ---------------------------------------------------------------------------------------------------- //

                    // 데이터 EditText에 저장
                    num3_1.setText(num3_1.text.toString() + result.result.pH_low.toString())
                    num3_2.setText(num3_2.text.toString() + result.result.pH_high.toString())

                    keyborad(num3_1)
                    keyborad(num3_2)

                    rangeSlider3.valueFrom = mFormat.format(result.result.pH_low - 10).toFloat()
                    rangeSlider3.valueTo = mFormat.format(result.result.pH_high + 10).toFloat()
                    rangeSlider3.stepSize = 0f
                    rangeSlider3.setValues(mFormat.format(result.result.pH_low).toFloat(), mFormat.format(result.result.pH_high).toFloat())

// ---------------------------------------------------------------------------------------------------- //

                    // 데이터 EditText에 저장
                    num4_1.setText(num4_1.text.toString() + result.result.Sa_low.toString())
                    num4_2.setText(num4_2.text.toString() + result.result.Sa_high.toString())

                    keyborad(num4_1)
                    keyborad(num4_2)

                    rangeSlider4.valueFrom = mFormat.format(result.result.Sa_low - 10).toFloat()
                    rangeSlider4.valueTo = mFormat.format(result.result.Sa_high + 10).toFloat()
                    rangeSlider4.stepSize = 0f
                    rangeSlider4.setValues(mFormat.format(result.result.Sa_low).toFloat(), mFormat.format(result.result.Sa_high).toFloat())

// ---------------------------------------------------------------------------------------------------- //

                    num5_1.setText(num5_1.text.toString() + result.result.ORP_low.toString())
                    num5_2.setText(num5_2.text.toString() + result.result.ORP_high.toString())

                    keyborad(num5_1)
                    keyborad(num5_2)

                    rangeSlider5.valueFrom = mFormat.format(result.result.ORP_low - 100).toFloat()
                    rangeSlider5.valueTo = mFormat.format(result.result.ORP_high + 100).toFloat()
                    rangeSlider5.stepSize = 0f
                    rangeSlider5.setValues(mFormat.format(result.result.ORP_low).toFloat(), mFormat.format(result.result.ORP_high).toFloat())

// ---------------------------------------------------------------------------------------------------- //

                    num6_1.setText(num6_1.text.toString() + result.result.TUR_low.toString())
                    num6_2.setText(num6_2.text.toString() + result.result.TUR_high.toString())

                    keyborad(num6_1)
                    keyborad(num6_2)

                    rangeSlider6.valueFrom = mFormat.format(result.result.TUR_low - 500).toFloat()
                    rangeSlider6.valueTo = mFormat.format(result.result.TUR_high + 500).toFloat()
                    rangeSlider6.stepSize = 0f
                    rangeSlider6.setValues(mFormat.format(result.result.TUR_low).toFloat(), mFormat.format(result.result.TUR_high).toFloat())

                }
            }

            override fun onFailure(call: Call<SensorDTO>, t: Throwable) {
                Log.d("Sensor_value", "${t.message}")
            }

        })

        logout_btn.setOnClickListener {
            Log.d("logout", "$user_token")
            call?.logout(user_key, user_token)!!.enqueue(object : Callback<logoutDTO>{

                override fun onResponse(call: Call<logoutDTO>, response: Response<logoutDTO>) {
                    if (response.isSuccessful){
                        val result : logoutDTO? = response.body()

                        result!!.result.user_token
                        Log.d("logout_retrofit2", "${result.result.user_token}")
                        result.result.user_key
                        Log.d("logout_retrofit2", "${result.result.user_key}")
                    }

                }

                override fun onFailure(call: Call<logoutDTO>, t: Throwable) {
                    Log.d("logout", "${t.message}")
                }

            })

            Loginkey.removeKey(mainActivity)
            Loginkey.removeTokenKey(mainActivity)
            db!!.UserDao()!!.deleteAllUsers()
            Log.d("romoveAll Data","${ db.UserDao()!!.getAll() }")

            Log.d("remove_date", Loginkey.getUserKey(mainActivity) + "\n${Loginkey.getTokenKey(mainActivity)}")
            Toast.makeText(mainActivity, "로그아웃 성공", Toast.LENGTH_LONG).show()

            // 앱 종료 후 재시작
            val packageManager = (context as MainActivity).packageManager
            val intent = packageManager.getLaunchIntentForPackage((context as MainActivity).packageName)
            val componentName = intent!!.component
            val mainIntent = Intent.makeRestartActivityTask(componentName)
            (context as MainActivity).startActivity(mainIntent)
            Runtime.getRuntime().exit(0)
        }

        constraint.setOnClickListener {
            val intent = Intent(context, WebcctvView::class.java)
            startActivity(intent)
        }

        return view
    }

    override fun onResume() {

        super.onResume()


        // 데이터 입력하는 값 수조 / 서버에서 수조 이름 받아오기 / 데이터 스크롤 + EditText 값 변경
//        button.setOnClickListener{
//
//
//            val formattedValue = inputNum.map {
//                val floatValue = it.text.toString().toFloat()
//                mFormat.format(floatValue).toFloat()
//            }
//
//            call?.Sensor_CG(user_key, Sensor_Body(
//                Tc_low = formattedValue[0],
//                Tc_high = formattedValue[1],
//                DO_low = formattedValue[2],
//                DO_high = formattedValue[3],
//                pH_low = formattedValue[4],
//                pH_high = formattedValue[5],
//                Sa_low =  formattedValue[6],
//                Sa_high = formattedValue[7],
//                ORP_low = formattedValue[8],
//                ORP_high = formattedValue[9],
//                TUR_low = formattedValue[10],
//                TUR_high = formattedValue[11]
//            ))?.enqueue(object : Callback<Sensor_CG_DTO>{
//
//                override fun onResponse(call: Call<Sensor_CG_DTO>, response: Response<Sensor_CG_DTO>) {
//                    if (response.isSuccessful) {
//                        val result : Sensor_CG_DTO? = response.body()
//
//                        num1_1.setText("")
//                        num1_2.setText("")
//                        num2_1.setText("")
//                        num2_2.setText("")
//                        num3_1.setText("")
//                        num3_2.setText("")
//                        num4_1.setText("")
//                        num4_2.setText("")
//                        num5_1.setText("")
//                        num5_2.setText("")
//                        num6_1.setText("")
//                        num6_2.setText("")
//
//                        Log.d("retrofit_sensor_cg", "${num1_1.text}")
//                        Log.d("change_sensor", "$result")
//
//                        Toast.makeText(context, "서버에 변경된 데이터를 전송했습니다", Toast.LENGTH_SHORT).show()
//
//                        change_value()
//                    }
//                }
//
//
//                override fun onFailure(call: Call<Sensor_CG_DTO>, t: Throwable) {
//
//                    Log.d("change_sensor", "${t.message}")
//
//                }
//
//            })
//
//        }

        val rangeSliders = listOf(rangeSlider, rangeSlider2, rangeSlider3, rangeSlider4, rangeSlider5, rangeSlider6)

        val numPackage = listOf(num1_1 to num1_2, num2_1 to num2_2, num3_1 to num3_2, num4_1 to num4_2, num5_1 to num5_2, num6_1 to num6_2)
        // 바 움직이면서 min max 값
        rangeSliders.forEachIndexed { index, rangeSlider ->
            rangeSlider.addOnSliderTouchListener(range_Listner(numPackage[index].first, numPackage[index].second, rangeSlider))
        }

    }

    fun change_value(){

        call?.Sensor_OG(user_key)?.enqueue(object : Callback<SensorDTO> {

            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<SensorDTO>, response: Response<SensorDTO>) {
                if (response.isSuccessful) {

                    val result : SensorDTO? = response.body()

                    // 이유는 알 수 없지만 String format을 이용하면 seekbar 소수점 표시 가능
                    val mFormat = DecimalFormat("#.##")

                    Log.d("Sensor_value", "$result")

                    // 데이터 EditText에 저장
                    num1_1.setText(num1_1.text.toString() + result!!.result.Tc_low.toString())
                    num1_2.setText(num1_2.text.toString() + result.result.Tc_high.toString())
//                     키보드 입력 값을 받아오고 슬라이더 모양 변화
                    keyborad(num1_1)
                    keyborad(num1_2)
                    // 최대 측량 범위
                    rangeSlider.valueFrom = mFormat.format(result.result.Tc_low - 10).toFloat()
                    // 서버에서 보내준 max 값 + 10f 방식 이용도 가능 가변적인 변경
                    rangeSlider.valueTo = mFormat.format(result.result.Tc_high + 10).toFloat()
                    // 슬라이더 움직일때 조절되는 단위
                    rangeSlider.stepSize = 0f
                    // 서버에서 지정된 사용자의 min max 값 받아오기
                    rangeSlider.setValues(mFormat.format(result.result.Tc_low).toFloat(), mFormat.format(result.result.Tc_high).toFloat())

// ---------------------------------------------------------------------------------------------------- //

                    // 데이터 EditText에 저장
                    num2_1.setText(num2_1.text.toString() + result.result.DO_low.toString())
                    num2_2.setText(num2_2.text.toString() + result.result.DO_high.toString())

                    keyborad(num2_1)
                    keyborad(num2_2)

                    rangeSlider2.valueFrom = mFormat.format(result.result.DO_low - 10).toFloat()
                    rangeSlider2.valueTo = mFormat.format(result.result.DO_high + 10).toFloat()
                    rangeSlider2.stepSize = 0f
                    rangeSlider2.setValues(mFormat.format(result.result.DO_low).toFloat(), mFormat.format(result.result.DO_high).toFloat())

// ---------------------------------------------------------------------------------------------------- //

                    // 데이터 EditText에 저장
                    num3_1.setText(num3_1.text.toString() + result.result.pH_low.toString())
                    num3_2.setText(num3_2.text.toString() + result.result.pH_high.toString())

                    keyborad(num3_1)
                    keyborad(num3_2)

                    rangeSlider3.valueFrom = mFormat.format(result.result.pH_low - 10).toFloat()
                    rangeSlider3.valueTo = mFormat.format(result.result.pH_high + 10).toFloat()
                    rangeSlider3.stepSize = 0f
                    rangeSlider3.setValues(mFormat.format(result.result.pH_low).toFloat(), mFormat.format(result.result.pH_high).toFloat())

// ---------------------------------------------------------------------------------------------------- //

                    // 데이터 EditText에 저장
                    num4_1.setText(num4_1.text.toString() + result.result.Sa_low.toString())
                    num4_2.setText(num4_2.text.toString() + result.result.Sa_high.toString())

                    keyborad(num4_1)
                    keyborad(num4_2)

                    rangeSlider4.valueFrom = mFormat.format(result.result.Sa_low - 10).toFloat()
                    rangeSlider4.valueTo = mFormat.format(result.result.Sa_high + 10).toFloat()
                    rangeSlider4.stepSize = 0f
                    rangeSlider4.setValues(mFormat.format(result.result.Sa_low).toFloat(), mFormat.format(result.result.Sa_high).toFloat())

// ---------------------------------------------------------------------------------------------------- //

                    num5_1.setText(num5_1.text.toString() + result.result.ORP_low.toString())
                    num5_2.setText(num5_2.text.toString() + result.result.ORP_high.toString())

                    keyborad(num5_1)
                    keyborad(num5_2)

                    rangeSlider5.valueFrom = mFormat.format(result.result.ORP_low - 100).toFloat()
                    rangeSlider5.valueTo = mFormat.format(result.result.ORP_high + 100).toFloat()
                    rangeSlider5.stepSize = 0f
                    rangeSlider5.setValues(mFormat.format(result.result.ORP_low).toFloat(), mFormat.format(result.result.ORP_high).toFloat())

// ---------------------------------------------------------------------------------------------------- //

                    num6_1.setText(num6_1.text.toString() + result.result.TUR_low.toString())
                    num6_2.setText(num6_2.text.toString() + result.result.TUR_high.toString())

                    keyborad(num6_1)
                    keyborad(num6_2)

                    rangeSlider6.valueFrom = mFormat.format(result.result.TUR_low - 500).toFloat()
                    rangeSlider6.valueTo = mFormat.format(result.result.TUR_high + 500).toFloat()
                    rangeSlider6.stepSize = 0f
                    rangeSlider6.setValues(mFormat.format(result.result.TUR_low).toFloat(), mFormat.format(result.result.TUR_high).toFloat())

                }
            }

            override fun onFailure(call: Call<SensorDTO>, t: Throwable) {
                Log.d("Sensor_value", "${t.message}")
            }

        })
    }

    // 센서 데이터 가져오기
    private fun getSensorData(): Sensor_Body {

        val mFormat = DecimalFormat("#.##")

        // 임계치 수치 EditText 리스트
        val inputNum = listOf(
            num1_1, num1_2,
            num2_1, num2_2,
            num3_1, num3_2,
            num4_1, num4_2,
            num5_1, num5_2,
            num6_1, num6_2)

        // 데이터 수치 0.00 자리로 포멧
        val formattedValue = inputNum.map {
            val floatValue = it.text.toString().toFloat()
            mFormat.format(floatValue).toFloat()
        }

//      포멧 데이터 반환
        return Sensor_Body(
            Tc_low = formattedValue[0],
            Tc_high = formattedValue[1],
            DO_low = formattedValue[2],
            DO_high = formattedValue[3],
            pH_low = formattedValue[4],
            pH_high = formattedValue[5],
            Sa_low = formattedValue[6],
            Sa_high = formattedValue[7],
            ORP_low = formattedValue[8],
            ORP_high = formattedValue[9],
            TUR_low = formattedValue[10],
            TUR_high = formattedValue[11]
        )
    }


    private fun range_Listner(num1 : EditText, num2 : EditText, frangeSlider : RangeSlider): RangeSlider.OnSliderTouchListener {

        // 소수점 잘르는 변수
        val mFormat = DecimalFormat("#.##")

        val rangeSliderTouchListener: RangeSlider.OnSliderTouchListener =
            object : RangeSlider.OnSliderTouchListener {

                override fun onStartTrackingTouch(slider: RangeSlider) {
                }

                override fun onStopTrackingTouch(slider: RangeSlider) {
                    // slider 움직이면 셋팅되는 value 값
                    frangeSlider.setValues(slider.values[0], slider.values[1])

                    // num1_1값을 넣어줘야되는데 Edittext는 string 형태로 받아줘야되므로 포맵을 통해 값을 변경해준다
                    // format을 이용해 슬라이더에서 예외적으로 발생하는 3자리 이상의 소수점을 반올림 처리한다
                    num1.setText(mFormat.format(slider.values[0]).toString())
                    Log.d("num1_1", "${num1}")
                    num2.setText(mFormat.format(slider.values[1]).toString())
                }
            }

        // fragment 초기화
        fragmentManager?.let { refreshFragment(this, it) }
        //      rangeSlider 사용할떄 사용되는 리스너 return 초기화
        return rangeSliderTouchListener
    }

    // edittext 입력 후 키보드 조절 Tlqk
    fun keyborad(knum_1 : EditText) {

        knum_1.setOnEditorActionListener { _, i, _ ->
            var handled = false

            Log.d("numberLog", number1)

            if (i == EditorInfo.IME_ACTION_DONE) {
                Log.d("numberLog", "Done")
                number1 = knum_1.text.toString()
                // 값을 입력했을 때 조건별로 분류
                if (number1.isNotBlank()){
                    Log.d("numberLog", "$number1")
                    Toast.makeText(context, "입력완료", Toast.LENGTH_SHORT).show()

                } else {
                    Toast.makeText(context, "입력 값이 잘못됐습니다", Toast.LENGTH_SHORT).show()
                    handled = true

                }

            }
            handled

        }
    }

    // Fragment 새로고침
    @SuppressLint("DetachAndAttachSameFragment")
    fun refreshFragment(fragment: Fragment, fragmentManager: FragmentManager) {
        // 갱신되는거 확인
        Log.d("refresh", "refresh")
        val ft: FragmentTransaction = fragmentManager.beginTransaction()
        ft.detach(fragment).attach(fragment).commit()
    }

}
