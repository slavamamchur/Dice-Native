package org.sadgames.engine.render.animation

import org.sadgames.engine.utils.Vector3f

/**
 * Created by Slava Mamchur on 02.03.2023.
 */

interface IAnimatedObject {
    fun getTransformationMatrix(): FloatArray?
    fun setPosition(position: Vector3f)
    fun setRotation(angle: Float, rotationAxesMask: Short)
    fun setZoomLevel(zoomLevel: Float)
    fun onAnimationEnd()
}