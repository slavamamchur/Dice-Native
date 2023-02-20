package org.sadgames.engine.input

import cglfw.GLFW_MOUSE_BUTTON_1
import cglfw.GLFW_PRESS
import cglfw.glfwGetMouseButton
import com.kgl.glfw.CursorPosCallback
import com.kgl.glfw.Glfw
import com.kgl.glfw.Window

/**
 * Created by Slava Mamchur on 19.02.2023.
 */

class MyMouseMoveCallBack(private val listener: IGestureDetectorListener?): CursorPosCallback {
    private var mouseLocked = false
    private var oldX = -1.0
    private var oldY = -1.0

    override fun invoke(window: Window, x: Double, y: Double) {
        mouseLocked = glfwGetMouseButton(window.ptr, GLFW_MOUSE_BUTTON_1) == GLFW_PRESS

        if (mouseLocked) {
            val dX = if(oldX < 0) 0f else (x - oldX).toFloat()
            val dY = if(oldY < 0) 0f else (y - oldY).toFloat()
            oldX = x
            oldY = y
            listener?.onSwipe(dX,dY)
        }
        else {
            oldX = -1.0
            oldY = -1.0
        }
    }
}