package uni.notes.util

import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

object Logger {

    fun info(text: String) {
        val frame = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE).walk { it.skip(2).findFirst() }.get()
        println("${timestamp()}===${frame.methodName}@${frame.className}===$text")
    }

    private fun timestamp() = SimpleDateFormat("HH:mm:ss:SSSS").format(Timestamp(Date().time))

    fun error(text: String) {
        val frame = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE).walk { it.skip(2).findFirst() }.get()
        System.err.println("${timestamp()}===${frame.methodName}@${frame.className}===$text")
    }

    fun error(e: Exception) {
        val frame = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE).walk { it.skip(2).findFirst() }.get()
        System.err.println("${timestamp()}===${frame.methodName}@${frame.className}===${e.stackTrace}")
    }
}