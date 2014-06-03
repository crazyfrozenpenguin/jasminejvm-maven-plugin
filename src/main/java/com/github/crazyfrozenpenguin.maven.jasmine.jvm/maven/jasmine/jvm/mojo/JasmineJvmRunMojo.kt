/**
 * Created with IntelliJ IDEA.
 * Date: 5/30/14
 * Time: 11:12 PM
 */
import org.apache.maven.plugin.AbstractMojo
import org.apache.maven.plugins.annotations.Component
import org.apache.maven.project.MavenProject
import org.apache.maven.plugins.annotations.Mojo
import org.apache.maven.plugins.annotations.ResolutionScope.TEST
import org.apache.maven.plugins.annotations.LifecyclePhase.INTEGRATION_TEST
import org.apache.maven.plugin.MojoExecutionException
import org.jasmine.cli.Main
import java.lang.Thread.currentThread
import java.net.URLClassLoader
import java.net.URL
import java.util.HashSet
import java.io.File
import org.apache.maven.plugins.annotations.Parameter

Mojo(name = "run", defaultPhase = INTEGRATION_TEST, requiresProject = true, threadSafe = false, requiresDependencyResolution = TEST)
class JasmineJvmRunMojo() : AbstractMojo() {

    Component
    private var project: MavenProject? = null

    Parameter(property = "classpathScope", defaultValue = "test")
    protected var classpathScope: String = ""

    Parameter(property = "pattern", defaultValue = "target/**/*Spec.js")
    protected var pattern: String = ""

    /**
     * @param format:String The format for the output file.
     *    Options are: DOC, PROGRESS
     *    Defaults to DOC.
     */
    Parameter(property = "format", defaultValue = "DOC")
    protected var format: String = ""

    /**
     *  @param compileMode:String The Compilation Mode to be used.
     *    Options are: OFF, FORCE, JIT
     *    Defaults to OFF.
     */
    Parameter(property = "compileMode", defaultValue = "OFF")
    protected var compileMode: String = ""

    override fun execute() {
        println("Executing Jasmine JVM...")
        val oldClassLoader: ClassLoader? = currentThread().getContextClassLoader()
        try {
            currentThread().setContextClassLoader(createClassLoader())
            callJasmineJvm("--format", format, "--pattern", pattern, "--compile-mode", compileMode)
        } catch (e: Exception) {
            getLog()?.error("ERROR: " + e.getMessage(), e)
        } finally {
            currentThread().setContextClassLoader(oldClassLoader);
        }
    }

    private fun callJasmineJvm(vararg parameters: String?) {
        Main.main(*parameters)
    }

    private fun createClassLoader(): ClassLoader {
        var list = hashSetOf<URL>();
        when (classpathScope) {
            "system" -> convertStringToUrl(list, project?.getSystemClasspathElements())
            "compile" -> convertStringToUrl(list, project?.getCompileClasspathElements())
            "runtime" -> convertStringToUrl(list, project?.getRuntimeClasspathElements())
            "test" -> convertStringToUrl(list, project?.getTestClasspathElements())
        }
        return URLClassLoader(list.toArray(array<URL>()), currentThread().getContextClassLoader())
    }

    private fun convertStringToUrl(dest: MutableSet<URL>, orig: MutableList<out Any?>?) {
        for (index in 0..orig?.size() as Int - 1) {
            val path = orig?.get(index) as String
            dest.add(File(path).toURI().toURL());
        }
    }
}
