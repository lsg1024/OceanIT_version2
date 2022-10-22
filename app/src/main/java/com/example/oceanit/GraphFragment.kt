package com.example.oceanit

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import kotlin.concurrent.thread


class GraphFragment : Fragment() {

    lateinit var chart : LineChart
    lateinit var des : Description
    lateinit var legend: Legend
    lateinit var leftAxis : YAxis
    lateinit var rightAxis : YAxis
    lateinit var data :LineData
    lateinit var mainActivity: MainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)

        mainActivity = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        val view = inflater.inflate(R.layout.fragment_graph_framgment, container, false)

        chart = view.findViewById(R.id.LineChart)

        setChart()

        return view
    }

    override fun onStart() {
        super.onStart()

//        make_chart()
//
//        mainActivity.runOnUiThread{
//
//            addEntry()
//        }

    }

//    override fun onResume() {
//        super.onResume()
//
//        make_chart()
//        mainActivity.runOnUiThread{
//            addEntry(10.1)
//        }
//
//    }

    private fun setChart() {
        val xAxis = chart.xAxis

        xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            textSize = 10f
            setDrawGridLines(false)
            granularity = 2f
            axisMinimum = 3f
            isGranularityEnabled = true //
        }

        chart.apply {
            axisRight.isEnabled = false
            axisLeft.axisMaximum = 50f
            legend.apply {
                textSize = 15f
                verticalAlignment = Legend.LegendVerticalAlignment.TOP
                horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
                orientation = Legend.LegendOrientation.HORIZONTAL
                setDrawInside(false)
            }
        }
        val lineData = LineData()
        chart.data = lineData
        feedMultiple()
    }

    private fun feedMultiple(){

        var thread : Thread? = null

        thread?.interrupt()

        val runnable = Runnable{
            addEntry()
        }

        thread = Thread(Runnable {
            while (true) {
                mainActivity.runOnUiThread(runnable)
                try {
                    // 지연시간 그래프 나오기까지
                    Thread.sleep(10000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        })
        thread.start()
    }

    private fun addEntry(){
        val data : LineData = chart.data

        val TempList = mutableListOf<Float>(35.6f)

        data.let {

            var set : ILineDataSet? = data.getDataSetByIndex(0)

            if (set == null) {
                set = createSet()
                data.addDataSet(set)
            }

            data.addEntry(Entry(set.entryCount.toFloat(), TempList[0]), 0)

            data.notifyDataChanged()
            chart.apply {
                notifyDataSetChanged()
                moveViewToX(data.entryCount.toFloat())
                setVisibleXRangeMaximum(4f)
                setPinchZoom(false)
                isDoubleTapToZoomEnabled = false
                description.text = "시간"
                description.textSize = 30f
                setExtraOffsets(8f, 16f, 8f, 16f)
            }
        }
    }


    }

    private fun createSet(): LineDataSet {

        val set = LineDataSet(null, "체온")
        set.apply {
            axisDependency = YAxis.AxisDependency.LEFT
            color = getColor(R.color.black)
            setCircleColor(getColor(R.color.black))
            valueTextSize = 10f
            lineWidth = 2f
            circleRadius = 3f
            fillAlpha = 0
            fillColor = getColor(R.color.white)
            setDrawValues(true)

        }
        return set
    }


//    fun make_chart(){
//        chart.setDrawGridBackground(true)
//        chart.setBackgroundColor(Color.BLACK)
//        chart.setGridBackgroundColor(Color.BLACK)
//
//        chart.description.isEnabled = true
//        des = chart.description
//        des.isEnabled = true
//        des.text = "test"
//        des.textSize = 15f
//        des.textColor = Color.WHITE
//
//        chart.setTouchEnabled(false)
//
//        // 차트 클릭 gestures  비활성화
//        chart.setTouchEnabled(false)
//
//        chart.isDragEnabled = false
//        chart.setScaleEnabled(false)
//
//        chart.isAutoScaleMinMaxEnabled = true
//
//        chart.setPinchZoom(false)
//
//        chart.xAxis.setDrawGridLines(true)
//        chart.xAxis.setDrawAxisLine(false)
//
//        chart.xAxis.isEnabled = true
//        chart.xAxis.setDrawGridLines(false)
//
//        legend = chart.legend
//        legend.isEnabled = true
//        legend.formSize = 10f
//        legend.textSize = 12f
//        legend.textColor = Color.WHITE
//
//        leftAxis = chart.axisLeft
//        leftAxis.isEnabled = true
//        leftAxis.textColor = resources.getColor(R.color.primary)
//        leftAxis.setDrawGridLines(true)
//        leftAxis.gridColor = resources.getColor(R.color.primary)
//
//        rightAxis = chart.axisRight
//        rightAxis.isEnabled = false
//
//        chart.invalidate()
//
//    }
//
//
//    private fun addEntry(num: Double) {
//
//        data.let {
//            var set = data.getDataSetByIndex(0)
//            data = chart.data
//            // set.addEntry(...); // can be called as well
//
//            // set.addEntry(...); // can be called as well
//            if (set == null) {
//                set = createSet()
//                data.addDataSet(set)
//            }
//
//            data.addEntry(Entry(set.entryCount.toFloat(), num.toFloat()), 0)
//            data.notifyDataChanged()
//
//            // let the chart know it's data has changed
//            chart.notifyDataSetChanged()
//            chart.setVisibleXRangeMaximum(150f)
//            // this automatically refreshes the chart (calls invalidate())
//            chart.moveViewTo(data.entryCount.toFloat(), 50f, YAxis.AxisDependency.LEFT)
//        }
//    }
//
//    private fun createSet(): LineDataSet {
//
//        val set = LineDataSet(null, "Real-time Line Data")
//        set.lineWidth = 1f
//        set.setDrawValues(false)
//        set.valueTextColor = resources.getColor(R.color.white)
//        set.color = resources.getColor(R.color.white)
//        set.mode = LineDataSet.Mode.LINEAR
//        set.setDrawCircles(false)
//        set.highLightColor = Color.rgb(190, 190, 190)
//        return set
//
//    }
//
//
//
//
//    companion object {
//
//        @JvmStatic
//        fun newInstance() =
//            GraphFragment().apply {
//                arguments = Bundle().apply {
//
//
//                }
//            }
//    }
//}


