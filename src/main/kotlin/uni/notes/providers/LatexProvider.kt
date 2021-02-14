package uni.notes.providers

import org.apache.commons.io.FilenameUtils
import org.apache.commons.io.IOUtils
import org.apache.commons.io.filefilter.DirectoryFileFilter
import org.fife.ui.autocomplete.BasicCompletion
import org.fife.ui.autocomplete.CompletionProvider
import org.fife.ui.autocomplete.DefaultCompletionProvider
import uni.notes.util.Logger
import uni.notes.util.ThreadPool
import uni.notes.util.doWhen
import java.io.File
import java.io.FileFilter
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.regex.Pattern

val commandQueue = ConcurrentLinkedQueue<String>()
val environmentQueue = ConcurrentLinkedQueue<String>()

class LatexProvider : Provider {

    @Suppress("SpellCheckingInspection")
    private val defaultKeywords = listOf(
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
        Logger.info("Starting index")
        val prov = DefaultCompletionProvider()
        defaultKeywords.forEach { word -> prov.addCompletion(BasicCompletion(prov, word)) }
        @Suppress("SpellCheckingInspection")
        val folders = File("/usr/share/texmf-dist/tex/latex").listFiles(DirectoryFileFilter.INSTANCE as FileFilter)
        val pool = ThreadPool()
        folders!!.forEach { folder -> pool.add(FileSearcher(folder.toPath())) }
        pool.start()
        doWhen({ pool.finished() }) {
            commandQueue.forEach { word -> prov.addCompletion(BasicCompletion(prov, word).also { it.summary = "FIXME" }) }
            environmentQueue.forEach { word -> prov.addCompletion(BasicCompletion(prov, word)) }
            Logger.info("Indexing finished")
        }
        return prov
    }

    private class FileSearcher(private val folder: Path) : Thread() {

        override fun run() {
            Files.walk(folder)
                .filter(Files::isRegularFile)
                .filter(Files::isReadable)
                .filter { file -> FilenameUtils.isExtension(file.toFile().name, "tex") }
                .forEach { file ->
                    run {
                        val text = file.toFile().readText().replace("\n", "")

                        @Suppress("SpellCheckingInspection")
                        val commandMatcher = Pattern.compile("\\\\newcommand\\{\\\\(\\w+)}").matcher(text)

                        @Suppress("SpellCheckingInspection")
                        val environmentMatcher = Pattern.compile("\\\\newenvironment\\{\\\\(\\w+)}").matcher(text)
                        while (commandMatcher.find()) {
                            commandQueue.add(commandMatcher.group(1))
                            //println("===========================Doc for ${commandMatcher.group(1)}===========================")
                            //getDocFile(commandMatcher.group(1))?.let { println(readPdf(it)) }
                            //println("===============================================================================================")

                        }
                        while (environmentMatcher.find()) {
                            environmentQueue.add(environmentMatcher.group(1))
                        }
                    }
                }
        }

        //private fun getDocFile(query: String): File? {
        //    val builder = ProcessBuilder()
        //        .command("texdoc", "-lI", query)
        //    val proc = builder.start()
        //    while (proc.isAlive) sleep(10)
        //    return try {
        //        File(IOUtils.toString(proc.inputStream, StandardCharsets.UTF_8).split("\n")[0].split(" ")[2])
        //    } catch (e: Exception) {
        //        null
        //    }
        //}

        //private fun readPdf(file: File): String {
        //    val builder = ProcessBuilder()
        //        .command("pdftotext", file.absolutePath, "-")
        //    val proc = builder.start()
        //    while (proc.isAlive) sleep(10)
        //    return IOUtils.toString(proc.inputStream, StandardCharsets.UTF_8)
        //}
    }
}