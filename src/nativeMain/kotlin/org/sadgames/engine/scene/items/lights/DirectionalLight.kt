package org.sadgames.engine.scene.items.lights

import org.sadgames.engine.scene.items.AbstractNode
import org.sadgames.engine.scene.items.camera.AbstractCamera
import org.sadgames.engine.scene.items.camera.AbstractCamera.Companion.FAR_PLANE
import org.sadgames.engine.utils.*
import org.sadgames.engine.utils.Matrix4f.Companion.applyViewMatrix
import org.sadgames.engine.utils.Matrix4f.Companion.convertToScreenSpace
import org.sadgames.engine.utils.Matrix4f.Companion.createLookTowards
import org.sadgames.engine.utils.Matrix4f.Companion.createTranslation
import kotlin.math.acos
import kotlin.math.atan
import kotlin.math.cos
import kotlin.math.sin
import kotlin.properties.Delegates.observable

open class DirectionalLight(lightPos: FloatArray, var lightColour: Vector3f, camera: AbstractCamera)
    : AbstractNode(name = "Sun") {
    companion object {
        fun getPosByDirection(direction: Vector3f) = direction * (FAR_PLANE * 100f)
        fun getUpDirection(dir: Vector3f) = Vector3f(dir.x, (-dir.x * dir.x - dir.z * dir.z) / dir.y, dir.z)
        fun getRightDirection(dir: Vector3f, up: Vector3f) = up cross dir
    }

    var lightPosInModelSpace = FloatArray(4)
        set(value) {
            lightDirection = Vector3f(value).normalized()
            up = getUpDirection(lightDirection)
            /** val check = lightDirection dot up -> check true orthogonal view */
            getPosByDirection(lightDirection) to field

            updateLightPosInEyeSpace()
            toPosition2D(mCamera.cameraPosition)
        }

    var mCamera by observable(camera) { _, _, newValue ->
        updateLightPosInEyeSpace()
        toPosition2D(newValue.cameraPosition)
        newValue.lightSourceObserver = this
    }

    val lightPosInEyeSpace: FloatArray = FloatArray(4)
    val position2D: FloatArray = FloatArray(3)
    var viewMatrix: FloatArray = Matrix4f().toFloatArray()
    var projectionMatrix = FloatArray(16)

    private lateinit var lightDirection: Vector3f
    private lateinit var up: Vector3f

    init {
        lightPosInModelSpace = lightPos
        mCamera.lightSourceObserver = this
    }

    fun onCameraViewMatrixChanged() {
        updateLightPosInEyeSpace()
        onCameraProjectionMatrixChanged()
    }

    inline fun onCameraProjectionMatrixChanged() { toPosition2D(mCamera.cameraPosition) }

    fun updateViewProjectionMatrix(width: Int, height: Int) {
        updateViewMatrix(true)
        updateProjectionMatrix(width, height)
    }

    fun getColorByAngle(day: Vector3f, night: Vector3f): Vector3f {
        val angle = 0.0f
        /** for dynamic day-night cycle
         *toDegrees(acos(Vector2f(lightDirection.x, lightDirection.z).length().toDouble())).toFloat() - 90.0f */

        return Vector3f(day.x + (day.x - night.x) / 90f * angle,
                day.y + (day.y - night.y) / 90f * angle,
                day.z + (day.z - night.z) / 90f * angle)
    }

    inline fun updateLightPosInEyeSpace() {
        (Matrix4f(mCamera.viewMatrix) * Vector4f(lightPosInModelSpace)) to lightPosInEyeSpace
    }

    fun toScreenSpace(position: Vector3f, scale: Float) : Vector2f? {
        val model = createTranslation(position)

        val matMV = applyViewMatrix(model, Matrix4f(mCamera.viewMatrix))
        matMV *= scale

        return convertToScreenSpace(Vector3f(0f), Matrix4f(mCamera.projectionMatrix) * matMV)
    }

    fun toPosition2D(cameraPosition: Vector3f) =
        (toScreenSpace(Vector3f(lightPosInModelSpace).normalized() * 4.5f + cameraPosition,1f) ?: Vector2f(-1000f, -1000f)) to position2D

    protected open fun updateViewMatrix(useGL: Boolean) {
        if (useGL) {/** classic openGL view matrix  */
            val right = getRightDirection(lightDirection, up)

            viewMatrix[0] = right.x
            viewMatrix[1] = up.x
            viewMatrix[2] = lightDirection.x
            viewMatrix[3] = 0.0f

            viewMatrix[4] = right.y
            viewMatrix[5] = up.y
            viewMatrix[6] = lightDirection.y
            viewMatrix[7] = 0.0f

            viewMatrix[8] = right.z
            viewMatrix[9] = up.z
            viewMatrix[10] = lightDirection.z
            viewMatrix[11] = 0.0f

            viewMatrix[12] = 0.0f
            viewMatrix[13] = 0.0f
            viewMatrix[14] = 0.0f
            viewMatrix[15] = 1.0f

            /*setLookAtM(viewMatrix, 0, lightPosInModelSpace[0], lightPosInModelSpace[1], lightPosInModelSpace[2],
                       0f, 0f, 0f,
                       up.x, up.y, up.z)*/
        }
        else { /** set view matrix via pitch/roll angles */
            val center = Vector3f(lightDirection.x, lightDirection.y, lightDirection.z)
            val pitch = toDegrees(acos(Vector2f(center.x, center.z).length()))
            val yaw = toDegrees((atan(center.x / center.z))) - if (center.z > 0f) 180f else 0f

            val forward = Vector3f(cos(pitch) * sin(-yaw), -sin(pitch), cos(pitch) * cos(-yaw))
            val up = Vector3f(sin(pitch) * sin(-yaw), cos(pitch), sin(pitch) * cos(-yaw))

            viewMatrix = createLookTowards(center, forward, up).toFloatArray()
        }
    }

    protected open fun updateProjectionMatrix(width: Int, height: Int) {
        val ratio = width.toFloat() / height.toFloat()

        projectionMatrix =
            Matrix4f.createOrthographic(-8f * ratio, 8f * ratio, -10f, 10f, -FAR_PLANE, FAR_PLANE).toFloatArray()
    }

}