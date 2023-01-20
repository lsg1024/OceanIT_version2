package com.example.oceanit

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.oceanit.DTO.SensorDTO
import com.example.oceanit.DTO.companyDTO
import com.example.oceanit.Retrofit.Loginkey
import com.example.oceanit.Retrofit.Retrofit2
import com.example.oceanit.Socket_File.Join_Data
import com.example.oceanit.Socket_File.Sensor_data
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.*
import com.github.mikephil.charting.data.CandleEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.MPPointF
import com.google.gson.Gson
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URISyntaxException
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList


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

    val List_v : ArrayList<String> = ArrayList()

    var ll1 : LimitLine? = null
    var ll2 : LimitLine? = null

    var tcmax : Float? = null
    var tcmin : Float? = null

    var domax : Float? = null
    var domin : Float? = null

    var phmax : Float? = null
    var phmin : Float? = null

    var samax : Float? = null
    var samin : Float? = null

    var orpmax : Float? = null
    var orpmin : Float? = null

    var turmax : Float? = null
    var turmin : Float? = null

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
                    company_name.text = result!!.result.company
                }
            }
            override fun onFailure(call: Call<companyDTO>, t: Throwable) {
                Log.d("company_name", "${t.message}")
            }

        })

        call?.Sensor_OG(user_key)?.enqueue(object : Callback<SensorDTO>{
            override fun onResponse(call: Call<SensorDTO>, response: Response<SensorDTO>) {
                if (response.isSuccessful){
                    val re_data : SensorDTO = response.body()!!

                    val data = re_data.result

                    tcmax = data.Tc_high
                    tcmin = data.Tc_low

                    domax = data.DO_high
                    domin = data.DO_low

                    phmax = data.pH_high
                    phmin = data.pH_low

                    samax = data.Sa_high
                    samin = data.Sa_low

                    orpmax = data.ORP_high
                    orpmin = data.ORP_low

                    turmax = data.TUR_high
                    turmin = data.TUR_low

                }
            }

            override fun onFailure(call: Call<SensorDTO>, t: Throwable) {
                Log.d("grap_value", "${t.message}")
            }

        })

        return view
    }

    override fun onResume() {
        super.onResume()

        var line_data1 : LineData
        var line_data2 : LineData
        var line_data3 : LineData
        var line_data4 : LineData
        var line_data5 : LineData
        var line_data6 : LineData


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

        // 차트에서 데이터 처리하는 시간과 그려주는 시간을 주기위해 지연시간을 줌
        val mHandler = Handler(Looper.getMainLooper())

        // 차트 데이터 클릭시 나타날 아이콘
//        val marker = MyMarkerView(mainActivity, R.layout.custom_marker_view)

        call?.company(user_key)?.enqueue(object : Callback<companyDTO> {

            override fun onResponse(call: Call<companyDTO>, response: Response<companyDTO>) {
                if (response.isSuccessful) {
                    val result: companyDTO? = response.body()

                    Log.d("company_name", "$result")

                    // 회사 이름 textViewd에 넣어주기
                    company_name.text = result!!.result.company

                    mHandler.postDelayed({
                        mainActivity.runOnUiThread(Runnable {

                            try {
                                mSocket = IO.socket("http://211.184.227.81:8500")
                                Log.d("SOCKET", "Connection success : $mSocket")

                            } catch (e: URISyntaxException) {
                                e.printStackTrace()
                            }
//
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
                                Log.d("Socket_on_date", "before data ${socket_data[0].date}")

                                // 리스트에 시간 정보를 넣는다
                                for (i in 0 until socket_data.size-1) {
                                    val j = socket_data[i].date
                                    val date = j.chunked(2)
                                    List_v.add(date[1]+date[2]+date[3]+date[4]+date[5]+date[6]+date[7])
                                }

                                List_v.reverse()

                                Log.d("List_v", "before data ${List_v +  List_v.size}")

                                fun data1(i: Int, j : Int): ArrayList<Entry> {
                                    dataList1.add(Entry(i.toFloat(), socket_data[j].Tc))
                                    Log.d("Socket_on2", "before data ${socket_data[j].Tc}")
                                    return dataList1
                                }

                                fun data2(i: Int, j : Int): ArrayList<Entry> {
                                    dataList2.add(Entry(i.toFloat(), socket_data[j].DO))
                                    return dataList2
                                }

                                fun data3(i: Int, j : Int): ArrayList<Entry> {
                                    dataList3.add(Entry(i.toFloat(), socket_data[j].pH))
                                    return dataList3
                                }

                                fun data4(i: Int, j : Int): ArrayList<Entry> {
                                    dataList4.add(Entry(i.toFloat(), socket_data[j].Sa))
                                    return dataList4
                                }

                                fun data5(i: Int, j : Int): ArrayList<Entry> {
                                    dataList5.add(Entry(i.toFloat(), socket_data[j].ORP))
                                    return dataList5
                                }

                                fun data6(i: Int, j : Int): ArrayList<Entry> {
                                    dataList6.add(Entry(i.toFloat(), socket_data[j].TUR))
                                    return dataList6
                                }

                                // 차트 클릭 이벤트 ui에 추가
//                                marker.chartView = chart
//                                chart.marker = marker
//
//                                marker.chartView = chart2
//                                chart2.marker = marker
//
//                                marker.chartView = chart3
//                                chart3.marker = marker
//
//                                marker.chartView = chart4
//                                chart4.marker = marker
//
//                                marker.chartView = chart5
//                                chart5.marker = marker
//
//                                marker.chartView = chart6
//                                chart6.marker = marker

                                for (i in 0 until socket_data.size-1) {
                                    // 그래프 추출시 값이 역순으로 출력되야되므로 리벌스 시켜준다
                                    val j = socket_data.size-1
                                    data1(i, j-i)
                                    data2(i,j-i)
                                    data3(i,j-i)
                                    data4(i,j-i)
                                    data5(i,j-i)
                                    data6(i,j-i)

                                }

                                val lineDataSet1 = LineDataSet(dataList1, "수온(℃)")
                                val lineDataSet2 = LineDataSet(dataList2, "산소(mg/L)")
                                val lineDataSet3 = LineDataSet(dataList3, "pH(pH)")
                                val lineDataSet4 = LineDataSet(dataList4, "염도(ppt)")
                                val lineDataSet5 = LineDataSet(dataList5, "OPR(mV)")
                                val lineDataSet6 = LineDataSet(dataList6, "탁도(NTU)")

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
                                line_data1 = LineData(dataSets1)
                                line_data2 = LineData(dataSets2)
                                line_data3 = LineData(dataSets3)
                                line_data4 = LineData(dataSets4)
                                line_data5 = LineData(dataSets5)
                                line_data6 = LineData(dataSets6)


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
                                // MaValueFormatter 클래스에서 포맷 형식을 불러와서 소수점을 잘라줬다
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

                                chart_YAxis(chart, domax!!, domin!!)
                                chart_YAxis(chart2, domax!!, domin!!)
                                chart_YAxis(chart3, phmax!!, phmin!!)
                                chart_YAxis(chart4, samax!!, samin!!)
                                val yAxis: YAxis = chart5.axisLeft
                                chart5.axisRight.isEnabled = false
                                yAxis.enableGridDashedLine(10f, 10f, 0f)
                                // axis range
                                val ll3 = LimitLine(orpmax!!, "최대 임계치 $orpmax")
                                val ll4  = LimitLine(orpmin!!, "최소 임계치 $orpmin")


                                ll3.apply {
                                    lineWidth = 2f
                                    enableDashedLine(20f, 0f, 0f)
                                    labelPosition = LimitLine.LimitLabelPosition.RIGHT_TOP
                                    textSize = 10f
                                }
                                ll4.apply {
                                    lineWidth = 2f
                                    lineColor = Color.BLUE
                                    enableDashedLine(40f, 0f, 0f)
                                    labelPosition = LimitLine.LimitLabelPosition.RIGHT_BOTTOM
                                    textSize = 10f
                                }
                                yAxis.setDrawLimitLinesBehindData(true)
                                yAxis.addLimitLine(ll3)
                                yAxis.addLimitLine(ll4)
                                yAxis.axisMaximum = orpmax!! *3
                                if (orpmin!! <= 250) {
                                    yAxis.axisMinimum = -500f
                                } else {
                                    yAxis.axisMinimum = orpmin!! *-3
                                }
                                val yAxis2: YAxis = chart6.axisLeft
                                chart5.axisRight.isEnabled = false
                                yAxis2.enableGridDashedLine(10f, 10f, 0f)
                                // axis range
                                val ll5 = LimitLine(turmax!!, "최대 임계치 $turmax")
                                val ll6  = LimitLine(turmin!!, "최소 임계치 $turmin")
                                ll5.apply {
                                    lineWidth = 2f
                                    enableDashedLine(20f, 0f, 0f)
                                    labelPosition = LimitLine.LimitLabelPosition.RIGHT_TOP
                                    textSize = 10f
                                }
                                ll6.apply {
                                    lineWidth = 2f
                                    lineColor = Color.BLUE
                                    enableDashedLine(40f, 0f, 0f)
                                    labelPosition = LimitLine.LimitLabelPosition.RIGHT_BOTTOM
                                    textSize = 10f
                                }
                                yAxis2.setDrawLimitLinesBehindData(true)
                                yAxis2.addLimitLine(ll5)
                                yAxis2.addLimitLine(ll6)
                                yAxis2.axisMaximum = turmax!! * 2
                                if (turmin!! <= 250) {
                                    yAxis2.axisMinimum = -500f
                                } else {
                                    yAxis2.axisMinimum = turmin!! * -2
                                }
                                chart.data = line_data1
                                chart2.data = line_data2
                                chart3.data = line_data3
                                chart4.data = line_data4
                                chart5.data = line_data5
                                chart6.data = line_data6
                            })
                        })

                        chart.invalidate()
                        chart2.invalidate()
                        chart3.invalidate()
                        chart4.invalidate()
                        chart5.invalidate()
                        chart6.invalidate()

                    }, 1500)
                }
            }


            override fun onFailure(call: Call<companyDTO>, t: Throwable) {

                Log.d("company_name", "${t.message}")

            }

        })

    }

    private fun chart_shape(set_chart: LineChart) {

        mainActivity.runOnUiThread {
            set_chart.animateX(1000)
            set_chart.setTouchEnabled(true)
            set_chart.setVisibleXRangeMaximum(3f)
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
            axisMaximum = 18.5f
            chart.xAxis.valueFormatter = IndexAxisValueFormatter(List_v)
            chart2.xAxis.valueFormatter = IndexAxisValueFormatter(List_v)
            chart3.xAxis.valueFormatter = IndexAxisValueFormatter(List_v)
            chart4.xAxis.valueFormatter = IndexAxisValueFormatter(List_v)
            chart5.xAxis.valueFormatter = IndexAxisValueFormatter(List_v)
            chart6.xAxis.valueFormatter = IndexAxisValueFormatter(List_v)


        }

        set_chart.axisRight.apply {
            setDrawGridLines(false)
            setDrawLabels(false)
            setDrawAxisLine(false)
        }

    }

    // 차트 클릭 이벤트에서 클릭했을 때 나오는 아이콘 format 형식 지정해줌
    @SuppressLint("ViewConstructor")
//    inner class MyMarkerView(context: Context?, layoutResource: Int) : MarkerView(context, layoutResource) {
//        private val tvContent: TextView = findViewById(R.id.tvContent)
//
//        // runs every time the MarkerView is redrawn, can be used to update the
//        // content (user-interface)
//        override fun refreshContent(e: Entry, highlight: Highlight) {
//
//            val mFormat = DecimalFormat("0")
//
//            if (e is CandleEntry) {
//                tvContent.text = "17"
//
//            } else {
//
//                for (i in (mFormat.format(e.x).toInt()) until socket_data.size){
//
//                    val j = socket_data.size
//                    tvContent.text = (socket_data[j - mFormat.format(e.x).toInt()-1].date)
//                }
//            }
//            super.refreshContent(e, highlight)
//        }
//
//        override fun getOffset(): MPPointF {
//            return MPPointF((-(width / 2)).toFloat(), (-height).toFloat())
//        }
//
//    }

    private fun Custom_Legend(set_chart: LineChart) {

        val legend: Legend = set_chart.legend

        legend.formSize = 10f
        legend.textSize = 15f
    }

    private fun chart_YAxis(set_chart: LineChart, max_data:Float, min_data: Float) {

        val yAxis: YAxis = set_chart.axisLeft

        // disable dual axis (only use LEFT axis)
        set_chart.axisRight.isEnabled = false
        // horizontal grid lines
        yAxis.enableGridDashedLine(10f, 10f, 0f)
        // axis range

        yAxis.axisMaximum = max_data * 3
        if (min_data <= 20) {
            yAxis.axisMinimum = -40f
        } else {
            yAxis.axisMinimum = min_data * -2
        }

        limitLine(max_data, min_data, yAxis)

    }

    private fun limitLine(max_data : Float, min_data : Float, yAxis: YAxis){
        ll1 = LimitLine(max_data, "최대 임계치 $max_data")
        ll2 = LimitLine(min_data, "최소 임계치 $min_data")

        ll1?.apply {
            lineWidth = 2f
            enableDashedLine(20f, 0f, 0f)
            labelPosition = LimitLine.LimitLabelPosition.RIGHT_TOP
            textSize = 10f
        }

        ll2?.apply {
            lineWidth = 2f
            lineColor = Color.BLUE
            enableDashedLine(40f, 0f, 0f)
            labelPosition = LimitLine.LimitLabelPosition.RIGHT_BOTTOM
            textSize = 10f
        }

        yAxis.setDrawLimitLinesBehindData(true)
        yAxis.addLimitLine(ll1)
        yAxis.addLimitLine(ll2)
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

}



