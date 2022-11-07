package com.example.oceanit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.WorkerThread
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.oceanit.DTO.companyDTO
import com.example.oceanit.Retrofit.Loginkey
import com.example.oceanit.Retrofit.Retrofit2
import com.example.oceanit.Socket_File.Join_Data
import com.example.oceanit.Socket_File.Sensor_data
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.google.gson.Gson
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.coroutines.delay
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URISyntaxException
import java.util.*
import kotlin.concurrent.thread


class GraphFragment : Fragment() {

    lateinit var chart: LineChart
    lateinit var chart2: LineChart
    lateinit var chart3: LineChart
    lateinit var chart4: LineChart
    lateinit var chart5: LineChart
    lateinit var chart6: LineChart

    var user_key: Int = 0
    lateinit var socket_data: Array<Sensor_data>
    lateinit var company_name: TextView

    private val gson = Gson()
    lateinit var mSocket: Socket
    lateinit var mainActivity: MainActivity
    val call by lazy { Retrofit2.getInstance() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        Log.d("recycle life", "onCreateView")
        val view = inflater.inflate(R.layout.fragment_graph_framgment, container, false)

        mainActivity = context as MainActivity

        user_key = Loginkey.getUserKey(mainActivity).toInt()

        chart = view.findViewById(R.id.LineChart)
        chart2 = view.findViewById(R.id.LineChart2)
        chart3 = view.findViewById(R.id.LineChart3)
        chart4 = view.findViewById(R.id.LineChart4)
        chart5 = view.findViewById(R.id.LineChart5)
        chart6 = view.findViewById(R.id.LineChart6)
        company_name = view.findViewById(R.id.grap_name)



        call?.company(user_key)?.enqueue(object : Callback<companyDTO> {

            override fun onResponse(call: Call<companyDTO>, response: Response<companyDTO>) {
                if (response.isSuccessful) {
                    val result: companyDTO? = response.body()

                    Log.d("company_name", "$result")

                    // 회사 이름 textViewd에 넣어주기
                    company_name.text = result!!.result.fishery

                }
            }


            override fun onFailure(call: Call<companyDTO>, t: Throwable) {

                Log.d("company_name", "${t.message}")

            }

        })

        // 데이터 넣는 데이터 셋
        val dataSets1 = ArrayList<ILineDataSet>()
        val dataSets2 = ArrayList<ILineDataSet>()
        val dataSets3 = ArrayList<ILineDataSet>()
        val dataSets4 = ArrayList<ILineDataSet>()
        val dataSets5 = ArrayList<ILineDataSet>()
        val dataSets6 = ArrayList<ILineDataSet>()

        // 어디선가 가져온 데이터를 리스트에 넣는데
        val dataList1 = ArrayList<Entry>()
        val dataList2 = ArrayList<Entry>()
        val dataList3 = ArrayList<Entry>()
        val dataList4 = ArrayList<Entry>()
        val dataList5 = ArrayList<Entry>()
        val dataList6 = ArrayList<Entry>()

        try {
            mSocket = IO.socket("http://211.184.227.81:8500")
            Log.d("SOCKET", "Connection success : $mSocket")

        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }

        mSocket.connect()

        mSocket.on(Socket.EVENT_CONNECT) { arg: Array<Any?>? ->
            mSocket.emit("join", gson.toJson(Join_Data(room = user_key)))
            Log.d("Socket_join", "입장")
        }

        mSocket.on("sensor_before", Emitter.Listener { args ->

            Log.d("Socket_on", "arg before data $args")

            // 형식없는 [{ }] json 형식을 array 형태로 반환해준다
            socket_data = gson.fromJson(args[0].toString(), Array<Sensor_data>::class.java)

            Log.d("Socket_on", "before data ${socket_data.size}")

            for (i in 0 until socket_data.size - 1) {

                fun data1(): ArrayList<Entry> {
                    dataList1.add(Entry(i.toFloat(), socket_data[i].Tc))
                    Log.d("Socket_on2", "before data ${socket_data[i].Tc}")
                    return dataList1
                }

                fun data2(): ArrayList<Entry> {
                    dataList2.add(Entry(i.toFloat(), socket_data[i].DO))
                    return dataList2
                }

                fun data3(): ArrayList<Entry> {
                    dataList3.add(Entry(i.toFloat(), socket_data[i].pH))
                    return dataList3
                }

                fun data4(): ArrayList<Entry> {
                    dataList4.add(Entry(i.toFloat(), socket_data[i].Sa))
                    return dataList4
                }

                fun data5(): ArrayList<Entry> {
                    dataList5.add(Entry(i.toFloat(), socket_data[i].ORP))
                    return dataList5
                }

                fun data6(): ArrayList<Entry> {
                    dataList6.add(Entry(i.toFloat(), socket_data[i].TUR))
                    return dataList6
                }
                data1()
                data2()
                data3()
                data4()
                data5()
                data6()
            }


            val lineDataSet1 = LineDataSet(dataList1, "수온")
            val lineDataSet2 = LineDataSet(dataList2, "산소(mg/L)")
            val lineDataSet3 = LineDataSet(dataList3, "pH(pH)")
            val lineDataSet4 = LineDataSet(dataList4, "염도(ppt)")
            val lineDataSet5 = LineDataSet(dataList5, "OPR(mV)")
            val lineDataSet6 = LineDataSet(dataList6, "탁도(TUR)")

            mainActivity.runOnUiThread {
                kotlin.run {
                    chart_shape(chart)
                    chart_shape(chart2)
                    chart_shape(chart3)
                    chart_shape(chart4)
                    chart_shape(chart5)
                    chart_shape(chart6)
                }
            }

            //1. 데이터 셋에 데이터 넣기
            createSet(lineDataSet1)
            createSet(lineDataSet2)
            createSet(lineDataSet3)
            createSet(lineDataSet4)
            createSet(lineDataSet5)
            createSet(lineDataSet6)

            //2. 리스트에 데이터셋 추가
            dataSets1.add(lineDataSet1)
            dataSets2.add(lineDataSet2)
            dataSets3.add(lineDataSet3)
            dataSets4.add(lineDataSet4)
            dataSets5.add(lineDataSet5)
            dataSets6.add(lineDataSet6)
//
            //3. 라인 데이터에 리스트 추가
            val line_data1 = LineData(dataSets1)
            val line_data2 = LineData(dataSets2)
            val line_data3 = LineData(dataSets3)
            val line_data4 = LineData(dataSets4)
            val line_data5 = LineData(dataSets5)
            val line_data6 = LineData(dataSets6)
//
            Custom_Legend(chart)
            Custom_Legend(chart2)
            Custom_Legend(chart3)
            Custom_Legend(chart4)
            Custom_Legend(chart5)
            Custom_Legend(chart6)


            lineDataSet1.color = ContextCompat.getColor(mainActivity, R.color.red)
            lineDataSet1.setCircleColor(ContextCompat.getColor(mainActivity, R.color.red))
            line_data1.setValueFormatter(MyValueFormatter())

            lineDataSet2.color = ContextCompat.getColor(mainActivity, R.color.primary)
            lineDataSet2.setCircleColor(ContextCompat.getColor(mainActivity, R.color.primary))
            line_data2.setValueFormatter(MyValueFormatter())

            lineDataSet3.color = ContextCompat.getColor(mainActivity, R.color.brawn)
            lineDataSet3.setCircleColor(ContextCompat.getColor(mainActivity, R.color.brawn))
            line_data3.setValueFormatter(MyValueFormatter())

            lineDataSet4.color = ContextCompat.getColor(mainActivity, R.color.light_grean)
            lineDataSet4.setCircleColor(ContextCompat.getColor(mainActivity, R.color.light_grean))
            line_data4.setValueFormatter(MyValueFormatter())

            lineDataSet5.color = ContextCompat.getColor(mainActivity, R.color.colorAccent)
            lineDataSet5.setCircleColor(ContextCompat.getColor(mainActivity, R.color.colorAccent))
            line_data5.setValueFormatter(MyValueFormatter())

            lineDataSet6.color = ContextCompat.getColor(mainActivity, R.color.purple_200)
            lineDataSet6.setCircleColor(ContextCompat.getColor(mainActivity, R.color.purple_200))
            line_data6.setValueFormatter(MyValueFormatter())

            chart_YAxis(chart)
            chart_YAxis(chart2)
            chart_YAxis(chart3)
            chart_YAxis(chart4)
            val yAxis: YAxis = chart5.axisLeft
            chart5.axisRight.isEnabled = false
            yAxis.enableGridDashedLine(10f, 10f, 0f)
            // axis range
            yAxis.axisMaximum = 400f
            yAxis.axisMinimum = 0f
            val yAxis2: YAxis = chart6.axisLeft
            chart5.axisRight.isEnabled = false
            yAxis2.enableGridDashedLine(10f, 10f, 0f)
            // axis range
            yAxis2.axisMaximum = 20f
            yAxis2.axisMinimum = 0f
//
//            // set data
            chart.data = line_data1
            chart2.data = line_data2
            chart3.data = line_data3
            chart4.data = line_data4
            chart5.data = line_data5
            chart6.data = line_data6
//
            chart.invalidate()
            chart2.invalidate()
            chart3.invalidate()
            chart4.invalidate()
            chart5.invalidate()
            chart6.invalidate()

        })

        return view
    }

    override fun onResume() {
        super.onResume()

        // 데이터 넣는 데이터 셋
        val dataSets1 = ArrayList<ILineDataSet>()
        val dataSets2 = ArrayList<ILineDataSet>()
        val dataSets3 = ArrayList<ILineDataSet>()
        val dataSets4 = ArrayList<ILineDataSet>()
        val dataSets5 = ArrayList<ILineDataSet>()
        val dataSets6 = ArrayList<ILineDataSet>()

        // 어디선가 가져온 데이터를 리스트에 넣는데
        val dataList1 = ArrayList<Entry>()
        val dataList2 = ArrayList<Entry>()
        val dataList3 = ArrayList<Entry>()
        val dataList4 = ArrayList<Entry>()
        val dataList5 = ArrayList<Entry>()
        val dataList6 = ArrayList<Entry>()

        call?.company(user_key)?.enqueue(object : Callback<companyDTO> {

            override fun onResponse(call: Call<companyDTO>, response: Response<companyDTO>) {
                if (response.isSuccessful) {
                    val result: companyDTO? = response.body()

                    Log.d("company_name", "$result")

                    // 회사 이름 textViewd에 넣어주기
                    company_name.text = result!!.result.fishery

                }
            }


            override fun onFailure(call: Call<companyDTO>, t: Throwable) {

                Log.d("company_name", "${t.message}")

            }

        })

        try {
            mSocket = IO.socket("http://211.184.227.81:8500")
            Log.d("SOCKET", "Connection success : $mSocket")

        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }

        mSocket.connect()


        mSocket.on(Socket.EVENT_CONNECT) { arg: Array<Any?>? ->
            mSocket.emit("join", gson.toJson(Join_Data(room = user_key)))
            Log.d("Socket_join", "입장")
        }

        mSocket.on("sensor_before", Emitter.Listener { args ->

            Log.d("Socket_on", "arg before data $args")

            // 형식없는 [{ }] json 형식을 array 형태로 반환해준다
            socket_data = gson.fromJson(args[0].toString(), Array<Sensor_data>::class.java)

            Log.d("Socket_on", "before data ${socket_data.size}")


            fun data1(i: Int): ArrayList<Entry> {
                dataList1.add(Entry(i.toFloat(), socket_data[i].Tc))
                Log.d("Socket_on2", "before data ${socket_data[i].Tc}")
                return dataList1
            }

            fun data2(i: Int): ArrayList<Entry> {
                dataList2.add(Entry(i.toFloat(), socket_data[i].DO))
                return dataList2
            }

            fun data3(i: Int): ArrayList<Entry> {
                dataList3.add(Entry(i.toFloat(), socket_data[i].pH))
                return dataList3
            }

            fun data4(i: Int): ArrayList<Entry> {
                dataList4.add(Entry(i.toFloat(), socket_data[i].Sa))
                return dataList4
            }

            fun data5(i: Int): ArrayList<Entry> {
                dataList5.add(Entry(i.toFloat(), socket_data[i].ORP))
                return dataList5
            }

            fun data6(i: Int): ArrayList<Entry> {
                dataList6.add(Entry(i.toFloat(), socket_data[i].TUR))
                return dataList6
            }

            for (i in 0 until socket_data.size - 1) {

                data1(i)
                data2(i)
                data3(i)
                data4(i)
                data5(i)
                data6(i)
            }

            val lineDataSet1 = LineDataSet(dataList1, "수온")
            val lineDataSet2 = LineDataSet(dataList2, "산소(mg/L)")
            val lineDataSet3 = LineDataSet(dataList3, "pH(pH)")
            val lineDataSet4 = LineDataSet(dataList4, "염도(ppt)")
            val lineDataSet5 = LineDataSet(dataList5, "OPR(mV)")
            val lineDataSet6 = LineDataSet(dataList6, "탁도(TUR)")

            //1. 데이터 셋 만들기
            createSet(lineDataSet1)
            createSet(lineDataSet2)
            createSet(lineDataSet3)
            createSet(lineDataSet4)
            createSet(lineDataSet5)
            createSet(lineDataSet6)

            //2. 리스트에 데이터셋 추가
            dataSets1.add(lineDataSet1)
            dataSets2.add(lineDataSet2)
            dataSets3.add(lineDataSet3)
            dataSets4.add(lineDataSet4)
            dataSets5.add(lineDataSet5)
            dataSets6.add(lineDataSet6)

            //3. 라인 데이터에 리스트 추가
            val line_data1 = LineData(dataSets1)
            val line_data2 = LineData(dataSets2)
            val line_data3 = LineData(dataSets3)
            val line_data4 = LineData(dataSets4)
            val line_data5 = LineData(dataSets5)
            val line_data6 = LineData(dataSets6)

            chart_shape(chart)
            chart_shape(chart2)
            chart_shape(chart3)
            chart_shape(chart4)
            chart_shape(chart5)
            chart_shape(chart6)

            Custom_Legend(chart)
            Custom_Legend(chart2)
            Custom_Legend(chart3)
            Custom_Legend(chart4)
            Custom_Legend(chart5)
            Custom_Legend(chart6)

            lineDataSet1.color = ContextCompat.getColor(mainActivity, R.color.red)
            lineDataSet1.setCircleColor(ContextCompat.getColor(mainActivity, R.color.red))
            line_data1.setValueFormatter(MyValueFormatter())

            lineDataSet2.color = ContextCompat.getColor(mainActivity, R.color.primary)
            lineDataSet2.setCircleColor(ContextCompat.getColor(mainActivity, R.color.primary))
            line_data2.setValueFormatter(MyValueFormatter())

            lineDataSet3.color = ContextCompat.getColor(mainActivity, R.color.brawn)
            lineDataSet3.setCircleColor(ContextCompat.getColor(mainActivity, R.color.brawn))
            line_data3.setValueFormatter(MyValueFormatter())

            lineDataSet4.color = ContextCompat.getColor(mainActivity, R.color.light_grean)
            lineDataSet4.setCircleColor(ContextCompat.getColor(mainActivity, R.color.light_grean))
            line_data4.setValueFormatter(MyValueFormatter())

            lineDataSet5.color = ContextCompat.getColor(mainActivity, R.color.colorAccent)
            lineDataSet5.setCircleColor(ContextCompat.getColor(mainActivity, R.color.colorAccent))
            line_data5.setValueFormatter(MyValueFormatter())

            lineDataSet6.color = ContextCompat.getColor(mainActivity, R.color.purple_200)
            lineDataSet6.setCircleColor(ContextCompat.getColor(mainActivity, R.color.purple_200))
            line_data6.setValueFormatter(MyValueFormatter())

            chart_YAxis(chart)
            chart_YAxis(chart2)
            chart_YAxis(chart3)
            chart_YAxis(chart4)
            val yAxis: YAxis = chart5.axisLeft
            chart5.axisRight.isEnabled = false
            yAxis.enableGridDashedLine(10f, 10f, 0f)
            // axis range
            yAxis.axisMaximum = 400f
            yAxis.axisMinimum = 0f
            val yAxis2: YAxis = chart6.axisLeft
            chart5.axisRight.isEnabled = false
            yAxis2.enableGridDashedLine(10f, 10f, 0f)
            // axis range
            yAxis2.axisMaximum = 20f
            yAxis2.axisMinimum = 0f


            chart.data = line_data1
            chart2.data = line_data2
            chart3.data = line_data3
            chart4.data = line_data4
            chart5.data = line_data5
            chart6.data = line_data6


            chart.invalidate()
            chart2.invalidate()
            chart3.invalidate()
            chart4.invalidate()
            chart5.invalidate()
            chart6.invalidate()
        })
    }

    private fun Custom_Legend(set_chart: LineChart) {

        val legend: Legend = set_chart.legend

        legend.formSize = 10f
        legend.textSize = 15f
    }

    private fun chart_YAxis(set_chart: LineChart) {

        val yAxis: YAxis = set_chart.axisLeft

        // disable dual axis (only use LEFT axis)
        set_chart.axisRight.isEnabled = false

        // horizontal grid lines
        yAxis.enableGridDashedLine(10f, 10f, 0f)
        // axis range
        yAxis.axisMaximum = 50f
        yAxis.axisMinimum = 0f

    }

    private fun chart_shape(set_chart: LineChart) {

        mainActivity.runOnUiThread {
            set_chart.animateX(2000)
            set_chart.setTouchEnabled(true)
            set_chart.setVisibleXRangeMaximum(4f)
            set_chart.setPinchZoom(false)
            set_chart.isDoubleTapToZoomEnabled = false
            set_chart.setExtraOffsets(8f, 16f, 8f, 16f)
            set_chart.description = null
            set_chart.isScaleXEnabled = false
            set_chart.isScaleYEnabled = false
        }

        set_chart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            setDrawGridLines(false)

        }

        set_chart.axisRight.apply {
            setDrawGridLines(false)
            setDrawLabels(false)
            setDrawAxisLine(false)
        }

    }

    private fun createSet(set: LineDataSet): LineDataSet {


        set.apply {
            axisDependency = YAxis.AxisDependency.LEFT
            color = getColor(R.color.red)
            setCircleColor(color)
            valueTextSize = 15f
            lineWidth = 1f
            circleRadius = 3f
            fillAlpha = 0
            fillColor = getColor(R.color.white)
            setDrawValues(true)

        }
        return set
    }

    // Fragment 새로고침
    fun refreshFragment(fragment: Fragment, fragmentManager: FragmentManager) {
        // 갱신되는거 확인
        Log.d("refresh", "refresh")
        val ft: FragmentTransaction = fragmentManager.beginTransaction()
        ft.detach(fragment).attach(fragment).commit()
    }
}


