package com.example.oceanit

import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ViewPortHandler
import java.text.DecimalFormat

class MyValueFormatter : ValueFormatter() {
    private val mFormat = DecimalFormat("0.##")

    override fun getFormattedValue(
        value: Float,
        entry: Entry,
        dataSetIndex: Int,
        viewPortHandler: ViewPortHandler
    ): String {

        return mFormat.format(value)
    }
}