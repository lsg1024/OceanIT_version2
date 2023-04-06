package com.example.oceanit.View

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.oceanit.DTO.SensorDTO
import com.example.oceanit.DTO.companyDTO
import com.example.oceanit.R
import com.example.oceanit.Retrofit.Loginkey
import com.example.oceanit.Retrofit.Retrofit2
import com.example.oceanit.Socket_File.Join_Data
import com.example.oceanit.Socket_File.Sensor_data
import com.example.oceanit.ViewModel.SensorViewModel
import com.example.oceanit.ViewModel.SocketViewModel
import com.example.oceanit.databinding.FragmentMainBinding
import com.github.anastr.speedviewlib.SpeedView
import com.github.anastr.speedviewlib.components.Section
import com.google.gson.Gson
import com.ramotion.foldingcell.FoldingCell
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URISyntaxException
import java.text.DecimalFormat


class MainFragment : Fragment() {

    private var user_key : Int = 0
    val call by lazy { Retrofit2.getInstance() }
    lateinit var mainActivity: MainActivity


    val df = DecimalFormat("#.##")

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var socketViewModel: SocketViewModel
    private lateinit var sensorViewModel: SensorViewModel

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMainBinding.inflate(inflater, container, false)


        mainActivity = context as MainActivity

        user_key = Loginkey.getUserKey(mainActivity).toInt()

        socketViewModel = ViewModelProvider(this)[SocketViewModel::class.java]
        sensorViewModel = ViewModelProvider(this)[SensorViewModel::class.java]

        val sensorViews = arrayOf(
            binding.speedView1,
            binding.speedView2,
            binding.speedView3,
            binding.speedView4,
            binding.speedView5,
            binding.speedView6
        )

        socketViewModel.sensorBeforeData.observe(viewLifecycleOwner, Observer {data ->
            data?.let {
                beforeData(data)
            }
        })

        socketViewModel.sensorData.observe(viewLifecycleOwner, Observer { data ->
            data?.let {
                lifecycleScope.launch {
                    binding.topText1.text = it.Tc.toString()
                    binding.topText2.text = it.DO.toString()
                    binding.topText3.text = it.pH.toString()
                    binding.topText4.text = it.Sa.toString()
                    binding.topText5.text = it.ORP.toString()
                    binding.topText6.text = it.TUR.toString()

                    updateBackground(it.Tc.toInt(), binding.speedView1, binding.blinkText1)
                    updateBackground(it.DO.toInt(), binding.speedView2, binding.blinkText2)
                    updateBackground(it.pH.toInt(), binding.speedView3, binding.blinkText3)
                    updateBackground(it.Sa.toInt(), binding.speedView4, binding.blinkText4)
                    updateBackground(it.ORP.toInt(), binding.speedView5, binding.blinkText5)
                    updateBackground(it.TUR.toInt(), binding.speedView6, binding.blinkText6)

                    binding.speedView1.speedTextListener = { df.format(data.Tc)}
                    binding.speedView2.speedTextListener = { df.format(data.DO)}
                    binding.speedView3.speedTextListener = { df.format(data.pH)}
                    binding.speedView4.speedTextListener = { df.format(data.Sa)}
                    binding.speedView5.speedTextListener = { df.format(data.ORP)}
                    binding.speedView6.speedTextListener = { df.format(data.TUR)}

                    binding.speedView1.realSpeedTo(df.format(data.Tc).toFloat())
                    binding.speedView2.realSpeedTo(df.format(data.DO).toFloat())
                    binding.speedView3.realSpeedTo(df.format(data.pH).toFloat())
                    binding.speedView4.realSpeedTo(df.format(data.Sa).toFloat())
                    binding.speedView5.realSpeedTo(df.format(data.ORP).toFloat())
                    binding.speedView6.realSpeedTo(df.format(data.TUR).toFloat())
                }
            }

        })

        sensorViewModel.getSensorOG()

        sensorViewModel.sensorOG.observe(viewLifecycleOwner, Observer {data ->
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
            }
        })


        setupSpeedView(binding.speedView1)
        setupSpeedView(binding.speedView2)
        setupSpeedView(binding.speedView3)
        setupSpeedView(binding.speedView4)
        setupSpeedView(binding.speedView5)
        setupSpeedView(binding.speedView6)

        toggleFoldable(binding.foldingCell, binding.foldingCell2, binding.foldingCell3, binding.foldingCell4, binding.foldingCell5,binding.foldingCell6)

        return binding.root


    }

    override fun onResume() {
        super.onResume()

        call?.company(user_key)?.enqueue(object : Callback<companyDTO>{

            override fun onResponse(call: Call<companyDTO>, response: Response<companyDTO>) {
                if (response.isSuccessful) {
                    val result : companyDTO? = response.body()

                    Log.d("company_name", "$result")

                    // 회사 이름 textViewd에 넣어주기
                    binding.mainName.text = result!!.result.company

                }
            }

            override fun onFailure(call: Call<companyDTO>, t: Throwable) {

                Log.d("company_name", "${t.message}")

            }

        })

    }

    private fun beforeData(sensorDataArray : Array<Sensor_data>) {
        binding.topText1.text = sensorDataArray[0].Tc.toString()
        binding.topText2.text = sensorDataArray[0].DO.toString()
        binding.topText3.text = sensorDataArray[0].pH.toString()
        binding.topText4.text = sensorDataArray[0].Sa.toString()
        binding.topText5.text = sensorDataArray[0].ORP.toString()
        binding.topText6.text = sensorDataArray[0].TUR.toString()

        updateBackground(sensorDataArray[0].Tc.toInt(), binding.speedView1, binding.blinkText1)
        updateBackground(sensorDataArray[0].DO.toInt(), binding.speedView2, binding.blinkText2)
        updateBackground(sensorDataArray[0].pH.toInt(), binding.speedView3, binding.blinkText3)
        updateBackground(sensorDataArray[0].Sa.toInt(), binding.speedView4, binding.blinkText4)
        updateBackground(sensorDataArray[0].ORP.toInt(), binding.speedView5, binding.blinkText5)
        updateBackground(sensorDataArray[0].TUR.toInt(), binding.speedView6, binding.blinkText6)

        binding.speedView1.speedTextListener = { df.format(sensorDataArray[0].Tc)}
        binding.speedView2.speedTextListener = { df.format(sensorDataArray[0].DO)}
        binding.speedView3.speedTextListener = { df.format(sensorDataArray[0].pH)}
        binding.speedView4.speedTextListener = { df.format(sensorDataArray[0].Sa)}
        binding.speedView5.speedTextListener = { df.format(sensorDataArray[0].ORP)}
        binding.speedView6.speedTextListener = { df.format(sensorDataArray[0].TUR)}

        binding.speedView1.realSpeedTo(df.format(sensorDataArray[0].Tc).toFloat())
        binding.speedView2.realSpeedTo(df.format(sensorDataArray[0].DO).toFloat())
        binding.speedView3.realSpeedTo(df.format(sensorDataArray[0].pH).toFloat())
        binding.speedView4.realSpeedTo(df.format(sensorDataArray[0].Sa).toFloat())
        binding.speedView5.realSpeedTo(df.format(sensorDataArray[0].ORP).toFloat())
        binding.speedView6.realSpeedTo(df.format(sensorDataArray[0].TUR).toFloat())
    }

    private fun toggleFoldable(vararg fold_ables: FoldingCell) {
        fold_ables.forEach { foldable ->
            foldable.setOnClickListener {
                foldable.toggle(false)
            }
        }
    }

    private fun setupSpeedView(speedView: SpeedView) {
        speedView.clearSections()
        speedView.addSections(
            Section(0f, .2f, Color.BLUE, speedView.dpTOpx(30f)),
            Section(.2f, .8f, Color.GREEN, speedView.dpTOpx(30f)),
            Section(.8f, 1f, Color.RED, speedView.dpTOpx(30f)),
        )
    }

    private fun updateBackground(value: Int, speedView: SpeedView, blinkText: TextView) {
        when {
            value > speedView.maxSpeed.toInt() -> blinkText.setBackgroundResource(R.drawable.red_edge)
            value < speedView.minSpeed.toInt() -> blinkText.setBackgroundResource(R.drawable.blue_edge)
            else -> blinkText.setBackgroundResource(R.drawable.lime_edge)
        }
    }

}