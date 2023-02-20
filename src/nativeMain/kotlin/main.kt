import cglfw.*
import com.kgl.glfw.*
import okio.*
import okio.Path.Companion.toPath
import org.sadgames.engine.*
import org.sadgames.engine.render.gl.GLRenderer

private object Game {
	private val engine: GameEngine
	private val window: Window

	init {
		//val file = fopen("/home/slava/CACHED_IMAGES_DB.sq3", "rb")
		//println("Hello, Kotlin/Native!${file != null}")
		//fclose(file)

		println(readFromFile("/home/slava/test.txt".toPath(true)))

		//val buffer = PlatformBufferAllocator.allocate(8) // allocates a buffer of 8 bytes
		//buffer.storeLongAt(0, 123451234567890L) // stores a long value at offset 0

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

	private fun readFromFile(path: Path) = FileSystem.SYSTEM.source(path).use {
		val buffer = Buffer()
		it.read(buffer, FileSystem.SYSTEM.metadata(path).size ?: 0)

		val result = buffer.readUtf8()
		buffer.close()

		result
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

		//PlatformBufferAllocator.free(buffer)
	}
}

fun main() = Game.run()
