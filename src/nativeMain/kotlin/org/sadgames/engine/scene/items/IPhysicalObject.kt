package org.sadgames.engine.scene.items

import bullet.collision.collisionShapes.CollisionShape
import bullet.dynamics.dynamics.RigidBody
import bullet.linearMath.Transform
import org.sadgames.engine.utils.Matrix4f
import org.sadgames.engine.utils.toFloatArray

interface IPhysicalObject {

    companion object {
        const val COLLISION_OBJECT = 1
        const val MOVING_OBJECT = 2
    }

    val mass: Float
    val _shape: CollisionShape?
    val tag: Int
    val worldTransformActual; get() = _body?.getWorldTransform()

    var _body: RigidBody?
    var old_body: RigidBody?
    var worldTransformOld: Transform?

    fun hideBody() { old_body = _body;_body = null }
    fun showBody() { _body = old_body; }
    fun setWorldTransformMatrix(transform: Transform?)
    fun createRigidBody()
    fun setPWorldTransform(transformMatrix: Matrix4f?) {
        _body?.setWorldTransform(Transform().apply {
            if (transformMatrix != null)
                setFromOpenGLMatrix(transformMatrix.toFloatArray())
        })
    }

}