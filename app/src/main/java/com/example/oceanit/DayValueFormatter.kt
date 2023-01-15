package com.example.oceanit

import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.DecimalFormat

class DayValueFormatter : ValueFormatter() {

    private val dFormat = DecimalFormat()

    override fun getFormattedValue(value: Float): String {

        return dFormat.format(value)
    }
}