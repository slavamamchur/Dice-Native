package org.sadgames.engine.render.gl.models

import com.kgl.opengl.*
import org.sadgames.engine.SceneItemType
import org.sadgames.engine.render.NORMALS_PARAM_NAME
import org.sadgames.engine.render.TEXELS_PARAM_NAME
import org.sadgames.engine.render.VERTEXES_PARAM_NAME
import org.sadgames.engine.render.gl.material.shaders.VBOShaderProgram
import org.sadgames.engine.render.gl.material.shaders.params.VBOData
import org.sadgames.engine.scene.items.AbstractNode
import org.sadgames.engine.scene.items.IDrawableItem
import org.sadgames.engine.utils.Matrix4f
import org.sadgames.engine.utils.Vector3f
import kotlin.properties.Delegates.observable

/**
 * Created by Slava Mamchur on 20.03.2023.
 */

abstract class AbstractGlMesh(var program: VBOShaderProgram): AbstractNode(), IDrawableItem {
    override var id = glGenVertexArray()
    override val type = SceneItemType.DRAWABLE_3D_ITEM
    override val transform = Matrix4f()

    override var rotationX: Float by observable(0f) {_, oldVal, newVal -> if (oldVal != newVal) updateTransform()}
    override var rotationY: Float by observable(0f) {_, oldVal, newVal -> if (oldVal != newVal) updateTransform()}
    override var rotationZ: Float by observable(0f) {_, oldVal, newVal -> if (oldVal != newVal) updateTransform()}
    override var scaleFactor: Float by observable(0f) {_, oldVal, newVal -> if (oldVal != newVal) updateTransform()}

    override var position: Vector3f by observable(Vector3f(0f)) {_, oldVal, newVal -> if (oldVal != newVal) updateTransform()}

    override fun loadObject() {
        glBindVertexArray(id)

        createVertexesVBO()
        createTexelsVBO()
        createNormalsVBO()
        createFacesIBO()

        program.params[VERTEXES_PARAM_NAME]?.value = vertexesVBO
        if (texelsVBO != null)
            program.params[TEXELS_PARAM_NAME]?.value = texelsVBO
        if (normalsVBO != null)
            program.params[NORMALS_PARAM_NAME]?.value = normalsVBO
        if (facesIBO != null) {
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, facesIBO!!.vboPtr)
            glBindVertexArray(0u)
        }
    }

    override fun loadFromObject(src: IDrawableItem) {
        if (src is AbstractGlMesh) {
            id = src.id
            clearVBOPtr(vertexesVBO)
            vertexesVBO = src.vertexesVBO
            clearVBOPtr(texelsVBO)
            texelsVBO = src.texelsVBO
            clearVBOPtr(normalsVBO)
            normalsVBO = src.normalsVBO
            clearVBOPtr(facesIBO)
            facesIBO = src.facesIBO
        }
        else
            throw IllegalArgumentException("Invalid AbstractGlMesh")
    }

    override fun bindObject() = glBindVertexArray(id)

    override fun render() {
        if (facesIBO == null)
            glDrawArrays(GL_TRIANGLES, 0, facesCount)
        else
            glDrawElements(GL_TRIANGLE_STRIP, facesCount, GL_UNSIGNED_SHORT, null)
    }

    override fun release() {
        clearVBOPtr(vertexesVBO)
        vertexesVBO = null
        clearVBOPtr(texelsVBO)
        texelsVBO = null
        clearVBOPtr(normalsVBO)
        normalsVBO = null
        clearVBOPtr(facesIBO)
        facesIBO = null

        glDeleteVertexArray(id)
    }

    var vertexesVBO: VBOData? = null; protected set
    var texelsVBO: VBOData? = null; protected set
    var normalsVBO: VBOData? = null; protected set
    var facesIBO: VBOData? = null; protected set

    protected abstract fun createVertexesVBO()
    protected abstract fun createTexelsVBO()
    protected abstract fun createNormalsVBO()
    protected abstract fun createFacesIBO()
    protected open fun clearVBOPtr(param: VBOData?) = param?.clear()

    protected  abstract val facesCount: Int
}