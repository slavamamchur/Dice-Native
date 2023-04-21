package org.sadgames.engine.render.gl.models

import com.kgl.opengl.*
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
import kotlin.reflect.KProperty

/**
 * Created by Slava Mamchur on 20.03.2023.
 */

abstract class AbstractGlMesh(var program: VBOShaderProgram): AbstractNode(), IDrawableItem {
    override var id = glGenVertexArray()
    override val transform = Matrix4f()
    override var rotationX: Float by observable(0f, ::onChange)
    override var rotationY: Float by observable(0f, ::onChange)
    override var rotationZ: Float by observable(0f, ::onChange)
    override var scaleFactor: Float by observable(0f, ::onChange)
    override var position: Vector3f by observable(Vector3f(0f), ::onChange)

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
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, facesIBO!!.handle)
            glBindVertexArray(0u)
        }
    }

    override fun loadFromObject(src: IDrawableItem) {
        if (src is AbstractGlMesh) {
            id = src.id
            clearVBO(vertexesVBO)
            vertexesVBO = src.vertexesVBO
            clearVBO(texelsVBO)
            texelsVBO = src.texelsVBO
            clearVBO(normalsVBO)
            normalsVBO = src.normalsVBO
            clearVBO(facesIBO)
            facesIBO = src.facesIBO
        }
        else
            throw IllegalArgumentException("Invalid AbstractGlMesh")
    }

    override fun bind() {
        program.useProgram()
        glBindVertexArray(id)
    }

    override fun render() {
        if (facesIBO == null)
            glDrawArrays(GL_TRIANGLES, 0, facesCount)
        else
            glDrawElements(GL_TRIANGLE_STRIP, facesCount, GL_UNSIGNED_SHORT, null)
    }

    override fun release() {
        clearVBO(vertexesVBO)
        vertexesVBO = null
        clearVBO(texelsVBO)
        texelsVBO = null
        clearVBO(normalsVBO)
        normalsVBO = null
        clearVBO(facesIBO)
        facesIBO = null

        glDeleteVertexArray(id)
    }

    var vertexesVBO: VBOData? = null; protected set
    var texelsVBO: VBOData? = null; protected set
    var normalsVBO: VBOData? = null; protected set
    var facesIBO: VBOData? = null; protected set

    protected abstract fun createVertexesVBO()
    protected open fun createTexelsVBO() {}
    protected open fun createNormalsVBO() {}
    protected open fun createFacesIBO() {}
    protected inline fun clearVBO(param: VBOData?) = param?.release()

    protected abstract val facesCount: Int

    private fun onChange(@Suppress("UNUSED_PARAMETER") prop: KProperty<*>, oldVal: Float, newVal: Float) {
        if (oldVal != newVal)
            updateTransform()
    }

    private fun onChange(@Suppress("UNUSED_PARAMETER") prop: KProperty<*>, oldVal: Vector3f, newVal: Vector3f) {
        if (oldVal != newVal)
            updateTransform()
    }
}