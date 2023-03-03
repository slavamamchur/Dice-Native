package org.sadgames.engine.scene.items.camera

import org.sadgames.engine.utils.Matrix4f.Companion.createOrthographic
import org.sadgames.engine.utils.Vector3f
import org.sadgames.engine.utils.toFloatArray

class Orthogonal2DCamera(private val landSize: Float):
        AbstractCamera(0f, -landSize / 2f, 0f, 90f, 0f, 0f) {

    private var scaleFactor: Float = 1.0f

    init {
        vFov = 90f
        zoomedVFov = 90f
    }

    override fun setVFovInternal() {
        scaleFactor = vFov / 90f
        updateProjectionMatrix()
    }

    override fun updateProjectionMatrix() {
        val landHalfSize = landSize / 2f
        val left = -landHalfSize * aspectRatio * scaleFactor
        val right = -left
        val bottom = -landHalfSize * scaleFactor
        val top = -bottom

        projectionMatrix = createOrthographic(left, right, bottom, top, -FAR_PLANE, FAR_PLANE).toFloatArray()
    }

    override fun rotateX(angle: Float) {}
    override fun rotateY(angle: Float) {}
    override fun rotateZ(angle: Float) {}

    override fun setPosition(position: Vector3f) {
        cameraPosition = position
    }
}