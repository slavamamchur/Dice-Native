package org.sadgames.engine.scene.items.camera

import org.sadgames.engine.render.animation.IAnimatedObject
import org.sadgames.engine.scene.items.AbstractNode
import org.sadgames.engine.scene.items.lights.DirectionalLight
import org.sadgames.engine.utils.*
import org.sadgames.engine.utils.Matrix4f.Companion.createLookTowards
import org.sadgames.engine.utils.Matrix4f.Companion.createPerspective
import kotlin.experimental.and
import kotlin.math.*
import kotlin.properties.Delegates.observable

abstract class AbstractCamera(cameraPosition: Vector3f, var pitch: Float, var yaw: Float, var roll: Float):
    IAnimatedObject, AbstractNode() {

    constructor(eyeX: Float, eyeY: Float, eyeZ: Float, pitch: Float, yaw: Float, roll: Float): this(Vector3f(eyeX, eyeY, eyeZ), pitch, yaw, roll)

    override fun generateName() = "Camera_$itemNumber"

    companion object {
        const val NEAR_PLANE = 0.01f
        const val FAR_PLANE = 1000f
        const val ROTATE_BY_X: Short = 1
        const val ROTATE_BY_Y: Short = 2
        const val ROTATE_BY_Z: Short = 4

        const val DEFAULT_CAMERA_VERTICAL_FOV = 45.0f
    }

    protected var transformMatrix: FloatArray = Matrix4f().toFloatArray()
    var viewMatrix by observable(FloatArray(16)) { _, _, _ -> lightSourceObserver?.onCameraViewMatrixChanged() }
    var projectionMatrix: FloatArray by observable(Matrix4f().toFloatArray()) { _, _, _ -> lightSourceObserver?.onCameraProjectionMatrixChanged() }
    var cameraPosition by observable(cameraPosition) { _, _, _ -> updateViewMatrix() }
    var vFov by observable(DEFAULT_CAMERA_VERTICAL_FOV) { _, _, _ -> setVFovInternal() }
    var zoomedVFov = DEFAULT_CAMERA_VERTICAL_FOV
    var aspectRatio by observable(-1f) { _, _, _ -> updateProjectionMatrix() }
    val cameraDirection get() = calcDirectionByPos(cameraPosition)
    var lightSourceObserver: DirectionalLight? = null

    init { this.cameraPosition = cameraPosition }

    protected open fun setVFovInternal() = updateProjectionMatrix()

    fun updateViewMatrix() {
        val forward = Vector3f(cos(pitch) * sin(yaw), -sin(pitch), cos(pitch) * cos(yaw))
        val up = Vector3f(sin(pitch) * sin(yaw), cos(pitch), sin(pitch) * cos(yaw))

        viewMatrix = createLookTowards(cameraPosition, forward, up).toFloatArray()
    }

    protected open fun updateProjectionMatrix() {
        projectionMatrix = createPerspective(vFov, aspectRatio, NEAR_PLANE, FAR_PLANE).toFloatArray()
    }

    fun updatePitch(pitch: Float) {
        this.pitch = pitch
        updateViewMatrix()
    }

    fun updateYaw(yaw: Float) {
        this.yaw = yaw
        updateViewMatrix()
    }

    fun updateRoll(roll: Float) {
        this.roll = roll
        updateViewMatrix()
    }

    fun directSetPitchByDirection(direction: Vector3f) { pitch = toDegrees(acos(Vector2f(direction.x, direction.z).length())) }
    fun directSetYawByDirection(direction: Vector3f) { yaw = -(toDegrees(atan(direction.x / direction.z)) - if (direction.z > 0) 180f else 0f) }
    fun calcDirectionByPos(cameraPosition: Vector3f) = -cameraPosition.normalized()

    fun calcDirectionByAngles(): Vector3f {
        return Vector3f( (sin(toRadians(pitch)) * cos(toRadians(yaw))),
                                  (sin(toRadians(pitch)) * sin(toRadians(yaw))),
                                   cos(toRadians(pitch)) )
        /** method #2
         * direction.x = cos(glm::radians(pitch)) * cos(glm::radians(yaw));
         * direction.y = sin(glm::radians(pitch));
         * direction.z = cos(glm::radians(pitch)) * sin(glm::radians(yaw));  */

        /** method #3
         * #apply yaw (around y)
         * x = x * cos(yaw) - z * sin(yaw)
         * z = z * cos(yaw) + x * sin(yaw)
         *
         * #apply pitch (around x)
         * y = y * cos(roll) - z * sin(roll)
         * z = z * cos(roll) + y * sin(roll)
         *
         * #apply roll (around z)
         * x = x * cos(pitch) - y * sin(pitch)
         * y = y * cos(pitch) + x * sin(pitch)  */
    }

    fun flipVertical() {
        pitch = -pitch
        cameraPosition.y = -cameraPosition.y

        updateViewMatrix()
    }

    abstract fun rotateX(angle: Float)
    abstract fun rotateY(angle: Float)
    abstract fun rotateZ(angle: Float)

    /** IAnimatedObject implementation ----------------------------------------------------------- */
    override fun getTransformationMatrix(): FloatArray = transformMatrix

    override fun setRotation(angle: Float, rotationAxesMask: Short) {
        if ((rotationAxesMask and ROTATE_BY_X) != 0.toShort()) pitch += angle
        if (rotationAxesMask and ROTATE_BY_Y != 0.toShort()) yaw += angle
        if (rotationAxesMask and ROTATE_BY_Z != 0.toShort()) roll += angle

        updateViewMatrix()
    }

    override fun setZoomLevel(zoomLevel: Float) { vFov = zoomedVFov / zoomLevel }
    override fun onAnimationEnd() { zoomedVFov = vFov }
}
