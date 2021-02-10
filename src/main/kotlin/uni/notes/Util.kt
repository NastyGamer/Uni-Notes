package uni.notes

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.awt.Color

fun String.sanitize() =
    toCharArray().filter { c -> c.toInt() in 65..90 || c.toInt() in 97..122 }.joinToString(String())

fun Color.asPaint(): javafx.scene.paint.Color = javafx.scene.paint.Color.rgb(red, green, blue, 1.0)

fun <E> ArrayList<E>.addIf(condition: () -> Boolean, item: E) {
    if (condition.invoke()) add(item)
}

fun doWhen(condition: () -> Boolean, toDo: () -> Unit) {
    GlobalScope.launch {
        run {
            while (!condition.invoke()) delay(10)
            toDo.invoke()
        }
    }
}