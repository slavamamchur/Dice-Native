import cglfw.glfwDefaultWindowHints
import cglfw.glfwInit
import cglfw.glfwShowWindow
import cglfw.glfwSwapInterval
import com.kgl.glfw.*
import okio.Buffer
import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath
import okio.use
import org.sadgames.engine.GameEngine
import org.sadgames.engine.render.gl.GLRenderer

private object Game {
	private val engine: GameEngine
	private val window: Window

	init {
		Glfw.setErrorCallback { error, description -> println("Glfw Error -> $error:$description") }
		glfwInit()

		val monitor = Glfw.primaryMonitor!!
		window = Window(monitor.videoMode.width, monitor.videoMode.height, "Sample!", monitor, null)
			.apply {
				isResizable = false
				isVisible = false //true
				cursorMode = CursorMode.Disabled

				//val (width, height) = size
				//val mode = Glfw.primaryMonitor!!.videoMode
				//position = ((mode.width - width) / 2) to ((mode.height - height) / 2)
			}

		glfwDefaultWindowHints()
		with(Glfw.windowHints) {
			samples = 4
			contextVersionMajor = 4
			contextVersionMinor = 2
			openGLProfile = OpenGLProfile.Core
			if (Platform.osFamily == OsFamily.MACOSX)
				openGLForwardCompat = true
		}

		Glfw.currentContext = window
		glfwSwapInterval(0) //
		glfwShowWindow(window.ptr) //

		engine = GameEngine(window, GLRenderer())
	}

	fun run() {
		do {
			if (window.getKey(KeyboardKey.ESCAPE) == Action.Press) {
				window.shouldClose = true
			}

			engine.renderer.onDraw()

			window.swapBuffers()
			Glfw.pollEvents()
		} while (!window.shouldClose)

		engine.renderer.onExit()
		window.cursorMode = CursorMode.Normal
		window.close()
		Glfw.terminate()
	}
}

fun main() = Game.run()
