package org.sadgames.engine

import com.kgl.glfw.Window
import org.sadgames.engine.input.MyGestureDetector
import org.sadgames.engine.input.MyMouseButtonCallBack
import org.sadgames.engine.input.MyMouseMoveCallBack
import org.sadgames.engine.input.MyMouseScrollCallBack
import org.sadgames.engine.render.IRenderer

/**
 * Created by Slava Mamchur on 19.02.2023.
 */

class GameEngine(val window: Window, val renderer: IRenderer) {
    private val gestureDetector = MyGestureDetector(renderer)

    init {
        with(window) {
            setFrameBufferCallback { _, w, h -> renderer.onResize(w, h) }
            setMouseButtonCallback(MyMouseButtonCallBack(gestureDetector))
            setCursorPosCallback(MyMouseMoveCallBack(gestureDetector))
            setScrollCallback(MyMouseScrollCallBack(gestureDetector))
        }
    }
}
