package com.example.oceanit.View

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.oceanit.DTO.SensorDTO
import com.example.oceanit.DTO.companyDTO
import com.example.oceanit.DTO.grap_DTO
import com.example.oceanit.MyValueFormatter
import com.example.oceanit.R
import com.example.oceanit.Retrofit.Loginkey
import com.example.oceanit.Retrofit.Retrofit2
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.*
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.ArrayList


class GraphFragment : Fragment() {

    lateinit var chart: LineChart
    lateinit var chart2: LineChart
    lateinit var chart3: LineChart
    lateinit var chart4: LineChart
    lateinit var chart5: LineChart
    lateinit var chart6: LineChart

    var line_data1 : LineData? = null
    var line_data2 : LineData? = null
    var line_data3 : LineData? = null
    var line_data4 : LineData? = null
    var line_data5 : LineData? = null
    var line_data6 : LineData? = null


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

    var user_key: Int = 0
    lateinit var company_name: TextView

    lateinit var mainActivity: MainActivity

    val call by lazy { Retrofit2.getInstance() }

    val List_v : ArrayList<String> = ArrayList()

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

        dataList1.clear()
        dataList2.clear()
        dataList3.clear()
        dataList4.clear()
        dataList5.clear()
        dataList6.clear()

        dataSets1.clear()
        dataSets2.clear()
        dataSets3.clear()
        dataSets4.clear()
        dataSets5.clear()
        dataSets6.clear()

        call?.grap(user_key)?.enqueue(object : Callback<grap_DTO> {
            override fun onResponse(call: Call<grap_DTO>, response: Response<grap_DTO>) {
                if (response.isSuccessful){
                    val grap = response.body()!!.result

                    // 리스트에 시간 정보를 넣는다
                    for (i in 0 until grap.size) {
                        val j = grap[i].date
                        val date = j.chunked(2)
                        List_v.add(date[1] + date[2] + date[3] + date[4] + date[5] + date[6] + date[7])
                    }

                    List_v.reverse()

                    for (i in 0 until grap.size) {
                        val j = grap.size - 1
                        addDataToList(i, j - i, dataList1, grap[j].Tc)
                        addDataToList(i, j - i, dataList2, grap[j].DO)
                        addDataToList(i, j - i, dataList3, grap[j].pH)
                        addDataToList(i, j - i, dataList4, grap[j].Sa)
                        addDataToList(i, j - i, dataList5, grap[j].ORP)
                        addDataToList(i, j - i, dataList6, grap[j].TUR)
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

                    setDataSetProperties(lineDataSet1, R.color.red)
                    setDataSetProperties(lineDataSet2, R.color.primary)
                    setDataSetProperties(lineDataSet3, R.color.brawn)
                    setDataSetProperties(lineDataSet4, R.color.light_grean)
                    setDataSetProperties(lineDataSet5, R.color.colorAccent)
                    setDataSetProperties(lineDataSet6, R.color.purple_200)

                    chart_YAxis(chart, tcmax!!, tcmin!!)
                    chart_YAxis(chart2, domax!!, domin!!)
                    chart_YAxis(chart3, phmax!!, phmin!!)
                    chart_YAxis(chart4, samax!!, samin!!)
                    val yAxis: YAxis = chart5.axisLeft
                    chart5.axisRight.isEnabled = false
                    yAxis.enableGridDashedLine(10f, 10f, 0f)

                    yAxis.axisMaximum = orpmax!! * 2
                    if (orpmin!! <= 250) {
                        yAxis.axisMinimum = -500f
                    } else {
                        yAxis.axisMinimum = orpmin!! * -2
                    }

                    val yAxis2: YAxis = chart6.axisLeft
                    chart6.axisRight.isEnabled = false
                    yAxis2.enableGridDashedLine(10f, 10f, 0f)

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

                    chart.invalidate()
                    chart.setVisibleXRangeMaximum(3f)
                    chart2.invalidate()
                    chart2.setVisibleXRangeMaximum(3f)
                    chart3.invalidate()
                    chart3.setVisibleXRangeMaximum(3f)
                    chart4.invalidate()
                    chart4.setVisibleXRangeMaximum(3f)
                    chart5.invalidate()
                    chart5.setVisibleXRangeMaximum(3f)
                    chart6.invalidate()
                    chart6.setVisibleXRangeMaximum(3f)

                }
            }
            override fun onFailure(call: Call<grap_DTO>, t: Throwable) {
                Log.d("grap_do", "${t.message}")
            }
        })

    }

    private fun chart_shape(set_chart: LineChart) {

        mainActivity.runOnUiThread {
            set_chart.animateX(1000)
            set_chart.setTouchEnabled(true)
            set_chart.setPinchZoom(false)
            set_chart.isDoubleTapToZoomEnabled = false
            set_chart.setExtraOffsets(8f, 16f, 45f, 8f)
            set_chart.description = null
            set_chart.isScaleXEnabled = false
            set_chart.isScaleYEnabled = false
        }

        set_chart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            setDrawGridLines(false)
            valueFormatter = IndexAxisValueFormatter(List_v)
        }

        set_chart.axisRight.apply {
            setDrawGridLines(false)
            setDrawLabels(false)
            setDrawAxisLine(false)
        }
    }

    private fun Custom_Legend(set_chart: LineChart) {
        val legend: Legend = set_chart.legend
        legend.formSize = 10f
        legend.textSize = 15f
    }

    private fun chart_YAxis(set_chart: LineChart, max_data:Float, min_data: Float) {

        set_chart.axisRight.isEnabled = false
        set_chart.axisLeft.apply {
            enableGridDashedLine(10f, 10f, 0f)
            axisMaximum = max_data * 3
            if (min_data <= 20) {
                axisMinimum = -40f
            } else {
                axisMinimum = min_data * -2
            }
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

    private fun setDataSetProperties(lineDataSet: LineDataSet, color: Int){
        lineDataSet.color = ContextCompat.getColor(mainActivity, color)
        lineDataSet.setCircleColor(ContextCompat.getColor(mainActivity, color))
        lineDataSet.valueFormatter = MyValueFormatter()
    }

    fun addDataToList(i: Int, j: Int, data: ArrayList<Entry>, value: Float){
        data.add(Entry(i.toFloat(), value))
    }
}