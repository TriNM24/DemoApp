package android.com.demo.utils

import java.text.SimpleDateFormat
import java.util.*

object Utils {
    fun getStringFromDate(format: String, date: Date): String {
        val outputDateFormat = SimpleDateFormat(format, Locale.getDefault())
        return outputDateFormat.format(date)
    }
}