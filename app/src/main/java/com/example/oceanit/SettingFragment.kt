package com.example.oceanit

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.oceanit.DTO.SensorDTO
import com.example.oceanit.DTO.Sensor_Body
import com.example.oceanit.DTO.Sensor_CG_DTO
import com.example.oceanit.DTO.companyDTO
import com.example.oceanit.Retrofit.Loginkey
import com.example.oceanit.Retrofit.Retrofit2
import com.google.android.material.slider.RangeSlider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// 자유롭게 변경하고 버튼을 누르면 edittext 값들을 서버로 전송
// 페이지에 들어왔을 때 onCreateView에서 서버에 저장된 값을 불러온다
//

class SettingFragment : Fragment() {

    lateinit var rangeSlider: RangeSlider
    lateinit var rangeSlider2: RangeSlider
    lateinit var rangeSlider3: RangeSlider
    lateinit var rangeSlider4: RangeSlider
    lateinit var rangeSlider5: RangeSlider
    lateinit var rangeSlider6: RangeSlider

    lateinit var watertank_name: TextView
    lateinit var watertank_name2: TextView
    lateinit var watertank_name3: TextView
    lateinit var watertank_name4: TextView
    lateinit var watertank_name5: TextView
    lateinit var watertank_name6: TextView

    lateinit var num1_1: EditText
    lateinit var num2_1: EditText
    lateinit var num3_1: EditText
    lateinit var num4_1: EditText
    lateinit var num5_1: EditText
    lateinit var num6_1: EditText

    lateinit var num1_2: EditText
    lateinit var num2_2: EditText
    lateinit var num3_2: EditText
    lateinit var num4_2: EditText
    lateinit var num5_2: EditText
    lateinit var num6_2: EditText

    lateinit var name : TextView
    lateinit var button: Button

    var num : Float = 0F
    var number1: String = ""
    var number2: String = ""

    val call by lazy { Retrofit2.getInstance() }

    private var user_key : Int = 0

    lateinit var mainActivity: MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {



        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_setting, container, false)

        // 회사 이미지 받아오기 서버에서
        name = view.findViewById(R.id.name)
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

        return view
    }

    override fun onStart() {
        super.onStart()

        mainActivity = context as MainActivity

        user_key = Loginkey.getUserKey(mainActivity).toInt()

        call?.company(user_key)?.enqueue(object : Callback<companyDTO>{

            override fun onResponse(call: Call<companyDTO>, response: Response<companyDTO>) {
                if (response.isSuccessful) {
                    val result : companyDTO? = response.body()

                    Log.d("company_name", "$result")

                    // 회사 이름 textViewd에 넣어주기
                    name.text = result!!.result.fishery

                }
            }


            override fun onFailure(call: Call<companyDTO>, t: Throwable) {

                Log.d("company_name", "${t.message}")

            }

        })

        call?.Sensor_OG(user_key)?.enqueue(object : Callback<SensorDTO> {

            override fun onResponse(call: Call<SensorDTO>, response: Response<SensorDTO>) {
                if (response.isSuccessful) {

                    val result : SensorDTO? = response.body()

                    Log.d("Sensor_value", "$result")

                    // 데이터 EditText에 저장
                    num1_1.setText(num1_1.text.toString() + result!!.result.DO_low.toString())
                    num1_2.setText(num1_2.text.toString() + result.result.DO_high.toString())
                    // 키보드 입력 값을 받아오고 슬라이더 모양 변화
                    keyborad(num1_1, num1_2)
                    // 최대 측량 범위
                    rangeSlider.valueFrom = 0f
                    // 서버에서 보내준 max 값 + 10f 방식 이용도 가능 가변적인 변경
                    rangeSlider.valueTo = 40f
                    // 슬라이더 움직일때 조절되는 단위
                    rangeSlider.stepSize = 0.01f
                    // 서버에서 지정된 사용자의 min max 값 받아오기
                    rangeSlider.setValues(result.result.DO_low, result.result.DO_high)

// ---------------------------------------------------------------------------------------------------- //

                    // 데이터 EditText에 저장
                    num2_1.setText(num2_1.text.toString() + result.result.pH_low.toString())
                    num2_2.setText(num2_2.text.toString() + result.result.pH_high.toString())

                    keyborad(num2_1, num2_2)

                    rangeSlider2.valueFrom = 0f
                    rangeSlider2.valueTo = 40f
                    rangeSlider2.stepSize = 0.01f
                    rangeSlider2.setValues(result.result.pH_low, result.result.pH_high)

// ---------------------------------------------------------------------------------------------------- //

                    // 데이터 EditText에 저장
                    num3_1.setText(num3_1.text.toString() + result.result.Sa_low.toString())
                    num3_2.setText(num3_2.text.toString() + result.result.Sa_high.toString())

                    keyborad(num3_1, num3_2)

                    rangeSlider3.valueFrom = 0f
                    rangeSlider3.valueTo = 40f
                    rangeSlider3.stepSize = 0.01f
                    rangeSlider3.setValues(result.result.Sa_low, result.result.Sa_high)

// ---------------------------------------------------------------------------------------------------- //

                    // 데이터 EditText에 저장
                    num4_1.setText(num4_1.text.toString() + result.result.ORP_low.toString())
                    num4_2.setText(num4_2.text.toString() + result.result.ORP_high.toString())

                    keyborad(num4_1, num4_2)

                    rangeSlider4.valueFrom = 0f
                    rangeSlider4.valueTo = 300f
                    rangeSlider4.stepSize = 0.01f
                    rangeSlider4.setValues(result.result.ORP_low, result.result.ORP_high)

// ---------------------------------------------------------------------------------------------------- //

                    num5_1.setText(num5_1.text.toString() + result.result.Tc_low.toString())
                    num5_2.setText(num5_2.text.toString() + result.result.Tc_high.toString())

                    keyborad(num5_1, num5_2)

                    rangeSlider5.valueFrom = 00f
                    rangeSlider5.valueTo = 40f
                    rangeSlider5.stepSize = 0.01f
                    rangeSlider5.setValues(result.result.Tc_low, result.result.Tc_high)

// ---------------------------------------------------------------------------------------------------- //

                    num6_1.setText(num6_1.text.toString() + result.result.TUR_low.toString())
                    num6_2.setText(num6_2.text.toString() + result.result.TUR_high.toString())

                    keyborad(num6_1, num6_2)

                    rangeSlider6.valueFrom = 0f
                    rangeSlider6.valueTo = 40f
                    rangeSlider6.stepSize = 0.01f
                    rangeSlider6.setValues(result.result.TUR_low, result.result.TUR_high)

                }
            }

            override fun onFailure(call: Call<SensorDTO>, t: Throwable) {
                Log.d("Sensor_value", "${t.message}")
            }

        })

    }

    override fun onResume() {

        super.onResume()

        mainActivity = context as MainActivity

        // 데이터 입력하는 값 수조 / 서버에서 수조 이름 받아오기 / 데이터 스크롤 + EditText 값 변경

        user_key = Loginkey.getUserKey(mainActivity).toInt()

        button.setOnClickListener {

            num = num1_1.text.toString().toFloat()

            call?.Sensor_CG(user_key, Sensor_Body(
                DO_low = num1_1.text.toString().toFloat(),
                DO_high = num1_2.text.toString().toFloat(),
                pH_low = num2_1.text.toString().toFloat(),
                pH_high =num2_2.text.toString().toFloat(),
                Sa_low = num3_1.text.toString().toFloat(),
                Sa_high = num3_2.text.toString().toFloat(),
                ORP_low =  num4_1.text.toString().toFloat(),
                ORP_high = num4_2.text.toString().toFloat(),
                Tc_low = num5_1.text.toString().toFloat(),
                Tc_high = num5_2.text.toString().toFloat(),
                TUR_low = num6_1.text.toString().toFloat(),
                TUR_high = num6_2.text.toString().toFloat()
            ))?.enqueue(object : Callback<Sensor_CG_DTO>{

                override fun onResponse(call: Call<Sensor_CG_DTO>, response: Response<Sensor_CG_DTO>) {
                    if (response.isSuccessful) {
                        val result : Sensor_CG_DTO? = response.body()

                        Log.d("change_sensor", "$result")

                        rangeSlider.setValues(result!!.result.DO_low, result.result.DO_high)
                        rangeSlider2.setValues(result.result.pH_low, result.result.pH_high)
                        rangeSlider3.setValues(result.result.Sa_low, result.result.Sa_high)
                        rangeSlider4.setValues(result.result.ORP_low, result.result.ORP_high)
                        rangeSlider5.setValues(result.result.Tc_low, result.result.Tc_high)
                        rangeSlider6.setValues(result.result.TUR_low, result.result.TUR_high)

                        Toast.makeText(context, "서버에 변경된 데이터를 전송했습니다", Toast.LENGTH_SHORT).show()

                    }
                }


                override fun onFailure(call: Call<Sensor_CG_DTO>, t: Throwable) {

                    Log.d("change_sensor", "${t.message}")

                }

            })

        }

        // 바 움직이면서 min max 값
        rangeSlider.addOnSliderTouchListener(range_Listner(num1_1, num1_2, rangeSlider))
        rangeSlider2.addOnSliderTouchListener(range_Listner(num2_1, num2_2, rangeSlider2))
        rangeSlider3.addOnSliderTouchListener(range_Listner(num3_1, num3_2, rangeSlider3))
        rangeSlider4.addOnSliderTouchListener(range_Listner(num4_1, num4_2, rangeSlider4))
        rangeSlider5.addOnSliderTouchListener(range_Listner(num5_1, num5_2, rangeSlider5))
        rangeSlider6.addOnSliderTouchListener(range_Listner(num6_1, num6_2, rangeSlider6))

    }


    private fun range_Listner(num1 : EditText, num2 : EditText, frangeSlider : RangeSlider): RangeSlider.OnSliderTouchListener {

        val rangeSliderTouchListener: RangeSlider.OnSliderTouchListener =
            object : RangeSlider.OnSliderTouchListener {

                override fun onStartTrackingTouch(slider: RangeSlider) {
                }

                override fun onStopTrackingTouch(slider: RangeSlider) {
//                    유저가 바에서 손을 떄었을때 동작하는 함수
//                    slider.getValues() 값이 [0.0, 5.0]처럼 배열로 값이 들어있다.
//                    val miniNumber = slider.values[0].toString().indexOf(".")
//                    val maxNumber = slider.values[1].toString().indexOf(".")

                    // slider 움직이면 셋팅되는 value 값
                    frangeSlider.setValues(slider.values[0], slider.values[1])

                    // num1_1값을 넣어줘야되는데 Edittext는 string 형태로 받아줘야되므로 포맵을 통해 값을 변경해준다
                    num1.setText(String.format(slider.values[0].toString()))
                    num2.setText(String.format(slider.values[1].toString()))
                }
            }

        // fragment 초기화
        fragmentManager?.let { refreshFragment(this, it) }
        //      rangeSlider 사용할떄 사용되는 리스너 return 초기화
        return rangeSliderTouchListener
    }

    // edittext 입력 후 키보드 조절 Tlqk
    fun keyborad(knum_1 : EditText, knum_2: EditText) {

        knum_2.setOnEditorActionListener { _, i, _ ->
            var handled = false

            number1 = null.toString()
            number1 = knum_1.text.toString()
            number2 = null.toString()
            number2 = knum_2.text.toString()

            Log.d("numberLog", number1)
            Log.d("numberLog", number2)

            if (i == EditorInfo.IME_ACTION_DONE) {
                Log.d("numberLog", "Done")

                // 값을 입력했을 때 조건별로 분류
                if (number1.isNotBlank() && number2.isNotBlank() && ((0 <= number1.toFloat()) && (number2.toFloat() <= 40))){
                    Log.d("numberLog", "$number1    $number2")
                    handled = false
                    knum_2.isCursorVisible = false
//                    frangeSlider.setValues(number1.toFloat(), number2.toFloat())
                    Toast.makeText(context, "입력완료", Toast.LENGTH_SHORT).show()
                    // 새로고침 가능
                    fragmentManager?.let { refreshFragment(this, it) }


                } else {
                    Toast.makeText(context, "입력 값이 잘못됐습니다", Toast.LENGTH_SHORT).show()
                    handled = true

                }

            }
            handled

        }
    }

    // Fragment 새로고침
    fun refreshFragment(fragment: Fragment, fragmentManager: FragmentManager) {
        // 갱신되는거 확인
        Log.d("refresh", "refresh")
        val ft: FragmentTransaction = fragmentManager.beginTransaction()
        ft.detach(fragment).attach(fragment).commit()
    }

}
