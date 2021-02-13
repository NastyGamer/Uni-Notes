package uni.notes.util

import org.apache.commons.io.FilenameUtils
import org.fife.ui.rsyntaxtextarea.SyntaxConstants

object SyntaxHighlighter {

    operator fun get(fileName: String) = when(FilenameUtils.getExtension(fileName)) {
        "java" -> SyntaxConstants.SYNTAX_STYLE_JAVA
        "tex", "bib" -> SyntaxConstants.SYNTAX_STYLE_LATEX
        else -> SyntaxConstants.SYNTAX_STYLE_NONE
    }

}