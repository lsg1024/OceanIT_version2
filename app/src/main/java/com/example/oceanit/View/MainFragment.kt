package com.example.oceanit.View

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.oceanit.R
import com.example.oceanit.Socket_File.Sensor_data
import com.example.oceanit.ViewModel.CompanyViewModel
import com.example.oceanit.ViewModel.SocketViewModel
import com.example.oceanit.databinding.FragmentMainBinding
import com.github.anastr.speedviewlib.SpeedView
import com.github.anastr.speedviewlib.components.Section
import com.ramotion.foldingcell.FoldingCell
import kotlinx.coroutines.launch
import java.text.DecimalFormat


class MainFragment : Fragment() {

    val df = DecimalFormat("#.##")

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var socketViewModel: SocketViewModel
    private lateinit var companyViewModel : CompanyViewModel

    private var nowValue1 : TextView? = null
    private var nowValue2 : TextView? = null
    private var nowValue3 : TextView? = null
    private var nowValue4 : TextView? = null
    private var nowValue5 : TextView? = null
    private var nowValue6 : TextView? = null
    lateinit var sensorViews : Array<SpeedView>

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMainBinding.inflate(inflater, container, false)

        nowValue1 = binding.topText1
        nowValue2 = binding.topText2
        nowValue3 = binding.topText3
        nowValue4 = binding.topText4
        nowValue5 = binding.topText5
        nowValue6 = binding.topText6

        socketViewModel = ViewModelProvider(this)[SocketViewModel::class.java]
        companyViewModel = ViewModelProvider(this)[CompanyViewModel::class.java]

        sensorViews = arrayOf(
            binding.speedView1,
            binding.speedView2,
            binding.speedView3,
            binding.speedView4,
            binding.speedView5,
            binding.speedView6
        )

//      소켓 실시간 데이터 이전 데이터 로딩까지 시간 걸려 이전 데이터 먼저 호출
        socketViewModel.sensorBeforeData.observe(viewLifecycleOwner, Observer {data ->
            data?.let {
                lifecycleScope.launch {
                    beforeData(data)
                }
            }
        })

        socketViewModel.sensorData.observe(viewLifecycleOwner) { data ->
            data?.let {
                lifecycleScope.launch {
                    binding.topText1.text = it.Tc.toString()
                    binding.topText2.text = it.DO.toString()
                    binding.topText3.text = it.pH.toString()
                    binding.topText4.text = it.Sa.toString()
                    binding.topText5.text = it.ORP.toString()
                    binding.topText6.text = it.TUR.toString()

                    binding.speedView1.speedTextListener = { df.format(data.Tc) }
                    binding.speedView2.speedTextListener = { df.format(data.DO) }
                    binding.speedView3.speedTextListener = { df.format(data.pH) }
                    binding.speedView4.speedTextListener = { df.format(data.Sa) }
                    binding.speedView5.speedTextListener = { df.format(data.ORP) }
                    binding.speedView6.speedTextListener = { df.format(data.TUR) }

                    binding.speedView1.realSpeedTo(df.format(data.Tc).toFloat())
                    binding.speedView2.realSpeedTo(df.format(data.DO).toFloat())
                    binding.speedView3.realSpeedTo(df.format(data.pH).toFloat())
                    binding.speedView4.realSpeedTo(df.format(data.Sa).toFloat())
                    binding.speedView5.realSpeedTo(df.format(data.ORP).toFloat())
                    binding.speedView6.realSpeedTo(df.format(data.TUR).toFloat())

                }
            }
        }

        socketViewModel.sensorOG.observe(viewLifecycleOwner, Observer { data ->
            data?.let {
                binding.max1.text = it.result.Tc_high.toString() + "℃"
                binding.min1.text = it.result.Tc_low.toString() + "℃"
                binding.max2.text = it.result.DO_high.toString()
                binding.min2.text = it.result.DO_low.toString()
                binding.max3.text = it.result.pH_high.toString()
                binding.min3.text = it.result.pH_low.toString()
                binding.max4.text = it.result.Sa_high.toString()
                binding.min4.text = it.result.Sa_low.toString()
                binding.max5.text = it.result.ORP_high.toString()
                binding.min5.text = it.result.ORP_low.toString()
                binding.max6.text = it.result.TUR_high.toString()
                binding.min6.text = it.result.TUR_low.toString()

                val sensorData = arrayOf(
                    data.result.Tc_high to data.result.Tc_low,
                    data.result.DO_high to data.result.DO_low,
                    data.result.pH_high to data.result.pH_low,
                    data.result.Sa_high to data.result.Sa_low,
                    data.result.ORP_high to data.result.ORP_low,
                    data.result.TUR_high to data.result.TUR_low
                )

//              최소 최대 임계치 비율 영역을 분리하기 위한 수식
                for (i in sensorViews.indices) {
                    val (max, min) = sensorData[i]
                    val valueNumber = (max - min) / 3f
                    sensorViews[i].maxSpeed = max + valueNumber
                    sensorViews[i].minSpeed = min - valueNumber
                }

                updateBackground(nowValue1!!.text.toString().toFloat(), data.result.Tc_high, data.result.Tc_low, binding.blinkText1)
                updateBackground(nowValue2!!.text.toString().toFloat(), data.result.DO_high, data.result.DO_low, binding.blinkText2)
                updateBackground(nowValue3!!.text.toString().toFloat(), data.result.pH_high, data.result.pH_low, binding.blinkText3)
                updateBackground(nowValue4!!.text.toString().toFloat(), data.result.Sa_high, data.result.Sa_low,  binding.blinkText4)
                updateBackground(nowValue5!!.text.toString().toFloat(), data.result.ORP_high, data.result.ORP_low, binding.blinkText5)
                updateBackground(nowValue6!!.text.toString().toFloat(), data.result.TUR_high, data.result.TUR_low, binding.blinkText6)

            }
        })

        setupSpeedView(binding.speedView1)
        setupSpeedView(binding.speedView2)
        setupSpeedView(binding.speedView3)
        setupSpeedView(binding.speedView4)
        setupSpeedView(binding.speedView5)
        setupSpeedView(binding.speedView6)

        toggleFoldable(binding.foldingCell, binding.foldingCell2, binding.foldingCell3, binding.foldingCell4, binding.foldingCell5,binding.foldingCell6)

        companyViewModel.companyData.observe(viewLifecycleOwner, Observer {data ->
            binding.mainName.text = data!!.result.company
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

//  실시간 소켓 연결 전 이전 데이터 호출 함수 -> 20초 공백 때문
    private fun beforeData(sensorDataArray: Array<Sensor_data>) {
        nowValue1!!.text = sensorDataArray[0].Tc.toString()
        nowValue2!!.text = sensorDataArray[0].DO.toString()
        nowValue3!!.text = sensorDataArray[0].pH.toString()
        nowValue4!!.text = sensorDataArray[0].Sa.toString()
        nowValue5!!.text = sensorDataArray[0].ORP.toString()
        nowValue6!!.text = sensorDataArray[0].TUR.toString()


        updateSensorData(sensorDataArray[0].Tc, binding.speedView1)
        updateSensorData(sensorDataArray[0].DO, binding.speedView2)
        updateSensorData(sensorDataArray[0].pH, binding.speedView3)
        updateSensorData(sensorDataArray[0].Sa, binding.speedView4)
        updateSensorData(sensorDataArray[0].ORP, binding.speedView5)
        updateSensorData(sensorDataArray[0].TUR, binding.speedView6)
    }

//  SpeedView 라이브러리 기존 소수점 형식 포멧을 통해 변경 0.0 -> 0.00 (데이터 상세 표시를 위해) 현재 값 표시
    private fun updateSensorData(value: Float, speedView: SpeedView) {
        speedView.speedTextListener = { df.format(value) }
        speedView.realSpeedTo(df.format(value).toFloat())
    }

    //  경고등 색변경 함수
    private fun updateBackground(value: Float, max : Float, min : Float, blinkText: TextView) {
        when {
            value > max -> blinkText.setBackgroundResource(R.drawable.red_edge)
            value < min -> blinkText.setBackgroundResource(R.drawable.blue_edge)
            else -> blinkText.setBackgroundResource(R.drawable.lime_edge)
        }
    }

//  Fold 라이브러리 클릭 펼치기 함수
    private fun toggleFoldable(vararg fold_ables: FoldingCell) {
        fold_ables.forEach { foldable ->
            foldable.setOnClickListener {
                foldable.toggle(false)
            }
        }
    }

//  SpeedView 비율 및 색 지정 함수
    private fun setupSpeedView(speedView: SpeedView) {
        speedView.clearSections()
        speedView.addSections(
            Section(0f, .2f, Color.BLUE, speedView.dpTOpx(30f)),
            Section(.2f, .8f, Color.GREEN, speedView.dpTOpx(30f)),
            Section(.8f, 1f, Color.RED, speedView.dpTOpx(30f)),
        )
    }

}