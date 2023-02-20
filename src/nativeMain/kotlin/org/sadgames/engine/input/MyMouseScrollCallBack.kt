package org.sadgames.engine.input

import com.kgl.glfw.ScrollCallback
import com.kgl.glfw.Window

/**
 * Created by Slava Mamchur on 19.02.2023.
 */

class MyMouseScrollCallBack(private val listener: IGestureDetectorListener?): ScrollCallback {

    private var xScroll = 0f
    private var yScroll = 0f

    override fun invoke(window: Window, x: Double, y: Double) {
        xScroll = x.toFloat()
        yScroll = y.toFloat()

        listener?.onZoom(yScroll)
    }
}