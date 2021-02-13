package uni.notes.providers

import org.fife.ui.autocomplete.BasicCompletion
import org.fife.ui.autocomplete.CompletionProvider
import org.fife.ui.autocomplete.DefaultCompletionProvider

class JavaProvider : Provider {

    @Suppress("SpellCheckingInspection")
    private val keywords = listOf(
        "abstract",
        "continue",
        "for",
        "new",
        "switch",
        "assert",
        "default",
        "goto",
        "package",
        "synchronized",
        "boolean",
        "do",
        "if",
        "private",
        "this",
        "break",
        "double",
        "implements",
        "protected",
        "throw",
        "byte",
        "else",
        "import",
        "public",
        "throws",
        "case",
        "enum",
        "instanceof",
        "return",
        "transient",
        "catch",
        "extends",
        "int",
        "short",
        "try",
        "char",
        "final",
        "interface",
        "static",
        "void",
        "class",
        "finally",
        "long",
        "strictfp",
        "volatile",
        "const",
        "float",
        "native",
        "super",
        "while"
    )

    override fun getCompletion(): CompletionProvider {
        val prov = DefaultCompletionProvider()
        keywords.forEach { word -> prov.addCompletion(BasicCompletion(prov, word)) }
        return prov
    }
}