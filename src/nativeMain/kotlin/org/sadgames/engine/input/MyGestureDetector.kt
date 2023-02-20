package org.sadgames.engine.input

import org.sadgames.engine.render.IRenderer

/**
 * Created by Slava Mamchur on 19.02.2023.
 */

class MyGestureDetector(private val renderer: IRenderer): IGestureDetectorListener {

    companion object {
        const val MOUSE_WHEEL_SENSITIVITY = 0.1f
        const val TOUCH_SCALE_FACTOR = 22.5f / 320

        val lockObject = Any()
    }

    private var distance        = 1f
    private val mPreviousX      = 0f
    private val mPreviousY      = 0f
    private var mScaleFactor    = 1.0f
            var oldScaleFactor  = 0f

    override fun onTap() {
        println("Tap -> Not yet implemented")
    }

    override fun onSwipe(dX: Float, dY: Float) {
        println("Swipe -> Not yet implemented")
    }

    override fun onZoom(amount: Float) {
        distance += amount * MOUSE_WHEEL_SENSITIVITY
        distance = if (distance <= 0) 0.1f else distance
        oldScaleFactor = mScaleFactor;
        mScaleFactor = distance;

        //if (oldScaleFactor != mScaleFactor) {
            //renderer.camera?.vFov = (DEFAULT_CAMERA_VERTICAL_FOV / mScaleFactor);
            //requestRender();
        //}

        println("Zoom ($mScaleFactor) -> Not yet implemented")
    }
}