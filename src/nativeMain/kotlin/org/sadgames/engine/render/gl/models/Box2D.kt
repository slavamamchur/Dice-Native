package org.sadgames.engine.render.gl.models

import com.kgl.opengl.GL_ARRAY_BUFFER
import com.kgl.opengl.GL_ELEMENT_ARRAY_BUFFER
import com.kgl.opengl.GL_TRIANGLE_STRIP
import com.kgl.opengl.glDrawArrays
import org.sadgames.GLObjectType
import org.sadgames.engine.CacheItemType.TEXTURE
import org.sadgames.engine.GameEngine.Companion.gameCache
import org.sadgames.engine.SceneItemType
import org.sadgames.engine.render.ACTIVE_TEXTURE_SLOT_PARAM_NAME
import org.sadgames.engine.render.TEXEL_UV_SIZE
import org.sadgames.engine.render.VERTEX_SIZE
import org.sadgames.engine.render.gl.GLRenderer.Companion.createShader
import org.sadgames.engine.render.gl.material.shaders.params.VBOData
import org.sadgames.engine.render.gl.material.textures.AbstractTexture
import org.sadgames.engine.utils.Vector4f
import org.sadgames.engine.utils.allocateData

/**
 * Created by Slava Mamchur on 23.03.2023.
 */

class Box2D(box: Vector4f,
            textureId: String? = null,
            var effects: Int = 0): AbstractGlMesh(createShader(GLObjectType.GUI_OBJECT)) {

    private val left = box.x
    private val top = box.y
    private val right = box.z
    private val bottom = box.w

    var background = gameCache[TEXTURE]?.get(textureId ?: "") as? AbstractTexture?

    override val type; get() = SceneItemType.DRAWABLE_GUI_ITEM
    override val drawInRaysBuffer: Boolean; get() = false
    override val castShadow: Boolean; get() = false
    override val reflected: Boolean; get() = false
    override val facesCount; get() = 4

    override fun render() = glDrawArrays(GL_TRIANGLE_STRIP, 0, facesCount)

    override fun createVertexesVBO() {
        val vertexes = floatArrayOf(
            left, top, 0.0f,
            left, bottom, 0.0f,
            right, top, 0.0f,
            right, bottom, 0.0f
        )

        vertexesVBO = VBOData(GL_ARRAY_BUFFER, VERTEX_SIZE, 0, 0).also { it.put(vertexes) }
    }

    override fun createTexelsVBO() {
        val uvs = floatArrayOf(
            0.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 1.0f,
            1.0f, 0.0f
        )

        texelsVBO = VBOData(GL_ARRAY_BUFFER, TEXEL_UV_SIZE, 0, 0).also { it.put(uvs) }
    }

    override fun createNormalsVBO() {}
    override fun createFacesIBO() {}

    override fun bind() {
        program.useProgram()
        bindObject()
        bindLocals()
    }

    override fun bindLocals() {
        val param = program.params[ACTIVE_TEXTURE_SLOT_PARAM_NAME] //todo: move reference check into paramByName() code

        if (background != null && param != null && param.paramReference >= 0)
            param.value = background!!.bind(0u)
    }

    override fun release() {
        super.release()

        background?.release()
    }
}