package uni.notes.providers

import org.apache.commons.io.FilenameUtils
import org.apache.commons.io.filefilter.DirectoryFileFilter
import org.fife.ui.autocomplete.BasicCompletion
import org.fife.ui.autocomplete.CompletionProvider
import org.fife.ui.autocomplete.DefaultCompletionProvider
import uni.notes.util.ThreadPool
import uni.notes.util.doWhen
import java.io.File
import java.io.FileFilter
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.regex.Pattern


class LatexProvider : Provider {

    private val keywords = listOf(
        "cleanPatterns",
        "enableSynctex",
        "enableExtendedBuildMode",
        "enableShellEscape",
        "engine",
        "moveResultToSourceDirectory",
        "outputFormat",
        "jobNames",
        "outputDirectory",
        "producer",
        "root",
        "syntax",
        "bib",
        "textbf",
        "textit",
        "emph",
        "underline",
        "textstyle",
        "displaystyle",
        "limits",
        "left",
        "right",
        "frac",
        "big",
        "Big",
        "bigg",
        "Bigg",
        "pm",
        "mp",
        "neq",
        "leq",
        "req",
        "sum",
        "prod",
        "int",
        "oint",
        "log",
        "exp",
        "sin",
        "cos",
        "tan",
        "infty",
        "to",
        "det",
        "theta",
        "bmod",
        "pmod",
        "equiv",
        "sqrt",
        "mathrm",
        "langle",
        "rangle",
        "lfloor",
        "rfloor",
        "lceil",
        "rceil",
        "tikzpicture",
        "path",
        "node",
        "coordinate",
        "pic",
        "graph",
        "matrix",
        "fill",
        "filldraw",
        "draw",
        "pattern",
        "shade",
        "shadedraw",
        "clip",
        "useasboundingbox",
        "include",
        "input",
        "usepackage",
        "documentclass",
        "begin",
        "document",
        "figure",
        "table",
        "itemize",
        "enumerate",
        "description",
        "part",
        "chapter",
        "section",
        "subsection",
        "subsubsection",
        "paragraph",
        "subparagraph",
        "clearpage",
        "newpage",
        "tracingmacros",
        "tracingcommands",
        "tracinglostchars",
        "tracingonline",
        "tracingoutput",
        "tracingpages",
        "tracingparagraphs",
        "tracingrestores",
        "tracingstats",
        "tracingall",
        "tracingassigns",
        "tracinggroups",
        "tracingifs",
        "tracingscantokens",
        "tracingcolors",
        "autocite",
        "parencite",
        "cite",
        "textcite",
        "citetitle",
        "footnote",
        "footnotemark",
        "ang",
        "num",
        "si",
        "SI",
        "numlist",
        "numrange",
        "SIlist",
        "SIrange",
        "sisetup",
        "tablenum",
        "definecolor",
        "colorlet",
        "color",
        "textcolor",
        "colorbox",
        "pagecolor",
        "nopagecolor",
        "hypersetup",
        "url",
        "nolinkurl",
        "hyperbaseurl",
        "hyperimage",
        "hyperref",
        "hyperlink",
        "hypertarget",
        "autoref",
        "autopageref",
        "align",
        "gather",
        "equation",
        "aligned",
        "text",
        "binom",
        "tbinom",
        "dbinom",
        "tfrac",
        "dfrac",
        "cfrac",
        "iint",
        "iiint",
        "iiiint",
        "idotsint",
        "substack",
        "minted",
        "inputminted",
    )

    override fun getCompletion(): CompletionProvider {
        println("Starting index")
        val prov = DefaultCompletionProvider()
        keywords.forEach { word -> prov.addCompletion(BasicCompletion(prov, word)) }
        val folders = File("/usr/share/texmf-dist/tex/latex").listFiles(DirectoryFileFilter.INSTANCE as FileFilter)
        val globalQueue: Queue<String> = ConcurrentLinkedQueue()
        val pool = ThreadPool()
        folders!!.forEach { folder ->
            pool.add(object : Thread() {
                override fun run() {
                    Files.walk(Paths.get(folder.absolutePath))
                        .filter(Files::isRegularFile).filter(Files::isReadable).filter { file -> FilenameUtils.isExtension(file.toFile().name, "tex") }
                        .forEach {
                            val content = it.toFile().readText()
                            val matcher = Pattern.compile("\\\\newcommand\\{\\\\(\\w+)}").matcher(content)
                            while (matcher.find()) {
                                globalQueue.add(matcher.group(1))
                            }
                        }
                }
            })
        }
        pool.start()
        doWhen({ pool.finished() }) {
            globalQueue.forEach { word -> prov.addCompletion(BasicCompletion(prov, word)) }
            println("Indexing finished")
        }
        return prov
    }
}