package org.sadgames.engine.input

/**
 * Created by Slava Mamchur on 15.05.21.
 */

interface IGestureDetectorListener {
    fun onTap()
    fun onSwipe(dX: Float, dY: Float)
    fun onZoom(amount: Float)
}