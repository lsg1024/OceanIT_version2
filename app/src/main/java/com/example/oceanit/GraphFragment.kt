package com.example.oceanit

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.oceanit.Retrofit.Loginkey
import com.example.oceanit.Socket_File.Join_Data
import com.example.oceanit.Socket_File.Sensor_data
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.gson.Gson
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import java.net.URISyntaxException


class GraphFragment : Fragment() {

    lateinit var chart : LineChart
    lateinit var chart2 : LineChart
    lateinit var chart3 : LineChart
    lateinit var chart4 : LineChart
    lateinit var chart5 : LineChart
    lateinit var chart6 : LineChart

    lateinit var des : Description
    lateinit var legend: Legend
    lateinit var leftAxis : YAxis
    lateinit var rightAxis : YAxis
    lateinit var data :LineData
    var user_key : Int = 0
    lateinit var socket_data : Array<Sensor_data>
    var data_list: Float? = null
    var count : Int = 0

    private val gson = Gson()
    lateinit var mSocket: Socket

    lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        val view = inflater.inflate(R.layout.fragment_graph_framgment, container, false)

        mainActivity = context as MainActivity

        user_key = Loginkey.getUserKey(mainActivity).toInt()

        chart = view.findViewById(R.id.LineChart)
        chart2 = view.findViewById(R.id.LineChart2)
        chart3 = view.findViewById(R.id.LineChart3)
        chart4 = view.findViewById(R.id.LineChart4)
        chart5 = view.findViewById(R.id.LineChart5)
        chart6 = view.findViewById(R.id.LineChart6)


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

            for (i in 0..(socket_data.size-1)){
                print(i)
                Log.d("print_log", "$i")
                //                Log.d("print_log", "${socket_data[i].DO}")


                mainActivity.runOnUiThread(Runnable {
                    setChart(chart, "수온", socket_data[i].DO)

                })

                Log.d("print_log", "${socket_data[i].DO}")



////        setChart(chart2, "수")
////        setChart(chart3, "test")
////        setChart(chart4, "탁도")
////        setChart(chart5, "스타일")
////        setChart(chart6, "라벨")

//                    Log.d("count_data1", "${socket_data[i].DO}")

            }

//            mainActivity.runOnUiThread(Runnable {
//
//                setChart(chart, "수온", socket_data[i].DO)
//        setChart(chart2, "수")
//        setChart(chart3, "test")
//        setChart(chart4, "탁도")
//        setChart(chart5, "스타일")
//        setChart(chart6, "라벨")
//            })

//            Log.d("data_list", "before data ${data_list}")

//            count++
//            Log.d("count_data", "$count")

        })

        return view
    }



    @JvmName("setChart1")
    private fun setChart(set_chart : LineChart, label: String, input_data: Float?) {



        val xAxis = set_chart.xAxis

        xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            textSize = 10f
            setDrawGridLines(false)
            granularity = 2f
            axisMinimum = 3f
            isGranularityEnabled = true //
        }

        set_chart.apply {
            axisRight.isEnabled = false
            axisLeft.axisMaximum = 30f
            axisLeft.axisMinimum - 20f
            legend.apply {
                textSize = 15f
                verticalAlignment = Legend.LegendVerticalAlignment.TOP
                horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
                orientation = Legend.LegendOrientation.HORIZONTAL
                setDrawInside(false)
            }
        }
        val lineData = LineData()
        set_chart.data = lineData
        feedMultiple(set_chart, label, input_data)



        count++

    }

    private fun feedMultiple(set_chart : LineChart, label: String, input_data : Float?){

        var thread : Thread? = null
        var thread_count : Int = 0

        // thread?.interrupt()


        thread = Thread(Runnable {
            while (thread_count < 1) {
                try {
                    // 지연시간 그래프 나오기까지

                    Thread.sleep(1000)
                    addEntry(set_chart, label, input_data)

                    Log.d("thread_count", "$thread_count")
                    Log.d("thread_count2", "$input_data")

                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

                thread_count++
            }
        })
        thread.start()
    }
//
private fun addEntry(set_chart : LineChart, label : String, input_data : Float?){
    val data : LineData = chart.data

    if (data != null) {
        var set = data.getDataSetByIndex(0)
        if (set == null) {
            set = createSet(label)
            data.addDataSet(set)
        }
        Log.d("set_addEntry", "$set")
        Log.d("input_data_addEntry", "$input_data")
        data.addEntry(Entry(set.entryCount.toFloat(), input_data!!), 0)

        data.notifyDataChanged()

        set_chart.apply {
            notifyDataSetChanged()
            moveViewToX(data.entryCount.toFloat())
            setVisibleXRangeMaximum(5f)
            setPinchZoom(false)
            isDoubleTapToZoomEnabled = false
            setExtraOffsets(8f, 16f, 8f, 16f)

        }

    }




        // 인덱스 size는 0부터 시작하므로 전체 사이즈에서 -1을 해줘야 된다 -> 반복문??? 사용

//    if(input_data != null){
//        val data : LineData = set_chart.data
//
//        Log.d("input_data", "add_set ${input_data}")
//
//        var set : ILineDataSet? = data.getDataSetByIndex(0)
//
//        Log.d("set_data2", "add_set ${set}")
//
//        Log.d("input_data6", "add_set ${input_data}")
//
//        if (set == null) {
//            set = createSet(label)
//            data.addDataSet(set)
//            Log.d("set_data4", "add_set ${set}")
//            Log.d("set_data6", "add_set ${data}")
//        }
//
//        Log.d("set_data3", "add_set ${set}")
//        Log.d("input_data5", "add_set ${input_data}")
//
//        data.addEntry(Entry(set.entryCount.toFloat(), input_data), 0)
//
//        count++
//        Log.d("count_data3", "$count")
//
////    Log.d("input_data", "add_set ${input_data}")
//
//        //            Log.d("Socket_list_size", "before data ${data_list}")
//
//        data.notifyDataChanged()
////
//        set_chart.notifyDataSetChanged()
//        set_chart.moveViewToX(data.entryCount.toFloat())
//        set_chart.setVisibleXRangeMaximum(3f)
//        set_chart.setPinchZoom(false)
//        set_chart.isDoubleTapToZoomEnabled = false
//        set_chart.setExtraOffsets(8f, 16f, 8f, 16f)
//    }


//    count++
//    Log.d("count_data3", "$count")
//
////    Log.d("input_data", "add_set ${input_data}")
//
//    //            Log.d("Socket_list_size", "before data ${data_list}")
//
//        data.notifyDataChanged()
////
//        set_chart.notifyDataSetChanged()
//        set_chart.moveViewToX(data.entryCount.toFloat())
//        set_chart.setVisibleXRangeMaximum(3f)
//        set_chart.setPinchZoom(false)
//        set_chart.isDoubleTapToZoomEnabled = false
//        set_chart.setExtraOffsets(8f, 16f, 8f, 16f)



//        Log.d("input_data", "$input_data")

//        data.let {
//
//        }
    }
}

    private fun createSet(label : String): LineDataSet {

        val set = LineDataSet(null, label)
        set.apply {
            axisDependency = YAxis.AxisDependency.LEFT
            color = getColor(R.color.black)
            setCircleColor(getColor(R.color.black))
            valueTextSize = 15f
            lineWidth = 4f
            circleRadius = 3f
            fillAlpha = 0
            fillColor = getColor(R.color.white)
            setDrawValues(true)

        }
        return set
    }


