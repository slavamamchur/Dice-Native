package org.sadgames.engine.render.gl.models

import com.kgl.opengl.GL_TRIANGLE_STRIP
import com.kgl.opengl.glDrawArrays
import org.sadgames.GLObjectType.GUI_OBJECT
import org.sadgames.engine.CacheItemType.TEXTURE
import org.sadgames.engine.GameEngine.Companion.gameCache
import org.sadgames.engine.SceneItemType
import org.sadgames.engine.render.ACTIVE_TEXTURE_SLOT_PARAM_NAME
import org.sadgames.engine.render.gl.GLRenderer.Companion.createShader
import org.sadgames.engine.render.gl.material.shaders.params.VBOData
import org.sadgames.engine.render.gl.material.shaders.params.VBOElement.V2D
import org.sadgames.engine.render.gl.material.textures.AbstractTexture
import org.sadgames.engine.utils.Vector4f

@Suppress("MemberVisibilityCanBePrivate")
class Box2D(val box: Vector4f, textureId: String? = null, var effects: Int = 0): AbstractGlMesh(createShader(GUI_OBJECT)) {
    var background = gameCache[TEXTURE]?.get(textureId ?: "") as? AbstractTexture
    override val type; get() = SceneItemType.DRAWABLE_GUI_ITEM
    override val facesCount; get() = 4

    override fun render() = glDrawArrays(GL_TRIANGLE_STRIP, 0, facesCount)

    override fun createVertexesVBO() {
        val vertexes = floatArrayOf(
            box.x, box.y,
            box.x, box.w,
            box.z, box.y,
            box.z, box.w
        )

        vertexesVBO = VBOData(V2D).also { it.put(vertexes) }
    }

    override fun bind() {
        super.bind()
        program.params[ACTIVE_TEXTURE_SLOT_PARAM_NAME]?.value = background?.bind(0u)
    }

    override fun release() {
        super.release()
        background?.release()
    }
}