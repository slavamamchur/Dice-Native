package org.sadgames.engine.scene.items.camera

import org.sadgames.engine.utils.Vector3f
import kotlin.math.*

class FixedIsometricCamera(eyeX: Float, eyeY: Float, eyeZ: Float, pitch: Float, yaw: Float, roll: Float):
        AbstractCamera(eyeX, eyeY, eyeZ, pitch, yaw, roll) {

    override fun rotateX(angle: Float) {
        val rAngle: Float
        val cameraPos = Vector3f(cameraPosition.x, cameraPosition.y, cameraPosition.z)
        val direction = cameraDirection
        val oldPitch = pitch

        if (yaw > -45f && yaw <= 45f || yaw in 135f..225f) {
            rAngle = if (yaw > -45f && yaw <= 45f) angle else -angle
            cameraPos.y = cos(rAngle) * (cameraPos.y - direction.y) - sin(rAngle) * (cameraPos.z - direction.z) + direction.y
            cameraPos.z = sin(rAngle) * (cameraPos.y - direction.y) + cos(rAngle) * (cameraPos.z - direction.z) + direction.z
        } else {
            rAngle = if (yaw <= -45f && yaw >= -89.999f || yaw in 224.999f..270f) -angle else angle
            cameraPos.x = cos(rAngle) * (cameraPos.x - direction.x) - sin(rAngle) * (cameraPos.y - direction.y) + direction.x
            cameraPos.y = sin(rAngle) * (cameraPos.x - direction.x) + cos(rAngle) * (cameraPos.y - direction.y) + direction.y
        }

        directSetPitchByDirection(calcDirectionByPos(cameraPos))

        if (pitch in 1.5f..90.0f)
            cameraPosition = cameraPos
        else
            pitch = oldPitch
    }

    override fun rotateY(angle: Float) {
        val direction = cameraDirection

        cameraPosition.x = cos(angle) * (cameraPosition.x - direction.x) - sin(angle) * (cameraPosition.z - direction.z) + direction.x
        cameraPosition.z = sin(angle) * (cameraPosition.x - direction.x) + cos(angle) * (cameraPosition.z - direction.z) + direction.z

        directSetYawByDirection(cameraDirection)
    }

    override fun rotateZ(angle: Float) {}

    override fun setPosition(position: Vector3f) { cameraPosition = position }
}