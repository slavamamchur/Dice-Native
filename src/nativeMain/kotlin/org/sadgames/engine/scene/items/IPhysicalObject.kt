package org.sadgames.engine.scene.items

import bullet.collision.collisionShapes.CollisionShape
import bullet.collision.collisionShapes.ConvexHullShape
import bullet.dynamics.dynamics.RigidBody
import bullet.linearMath.DefaultMotionState
import bullet.linearMath.Transform
import bullet.linearMath.Vec3
import org.sadgames.engine.render.gl.models.AbstractGlMesh
import org.sadgames.engine.utils.Matrix4f
import org.sadgames.engine.utils.toFloatArray
import kotlin.random.Random
import kotlin.system.getTimeMillis

interface IPhysicalObject {
    companion object {
        const val COLLISION_OBJECT = 1
        const val MOVING_OBJECT = 2
    }

    val mass: Float
    var _shape: CollisionShape?
    val tag: Int
    val worldTransformActual; get() = _body?.getWorldTransform()
    var _body: RigidBody?
    var old_body: RigidBody?
    var worldTransformOld: Transform?

    fun hideBody() { old_body = _body;_body = null }
    fun showBody() { _body = old_body; }

    fun setWorldTransformMatrix(transform: Transform) {
        worldTransformOld = transform
        (this as AbstractGlMesh).transform.reAssign(
            Matrix4f(FloatArray(16).also { transform.getOpenGLMatrix(it) })
        )
    }

    fun createCollisionShape(vertexes: FloatArray) { _shape = ConvexHullShape(vertexes) }

    fun createRigidBody() {
        old_body = _body

        val rnd = Random(getTimeMillis())
        val bodyInertia = Vec3()
        val initTransform = (this as AbstractGlMesh).transform
        val transform = Transform().apply { setFromOpenGLMatrix(initTransform.toFloatArray()) }

        _shape?.calculateLocalInertia(mass, bodyInertia)

        val bodyCI = RigidBody.RigidBodyConstructionInfo(
            mass,
            DefaultMotionState(transform),
            _shape,
            bodyInertia
        )
        bodyCI.restitution = 0.0125f + rnd.nextInt(125) * 1f / 10000f
        bodyCI.friction = 0.5f + rnd.nextInt(4) * 1f / 10f

        _body = RigidBody(bodyCI)
        _body!!.userObjectPointer = this
    }

    fun setPWorldTransform(transform: Matrix4f?) =
        _body?.setWorldTransform(Transform().apply { transform?.let { setFromOpenGLMatrix(it.toFloatArray())} })
}