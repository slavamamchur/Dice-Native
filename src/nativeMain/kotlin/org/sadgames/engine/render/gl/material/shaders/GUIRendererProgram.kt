package org.sadgames.engine.render.gl.material.shaders

import org.sadgames.engine.render.*
import org.sadgames.engine.render.gl.models.Box2D
import org.sadgames.engine.scene.GameScene
import org.sadgames.engine.scene.items.IDrawableItem
import org.sadgames.engine.utils.Matrix4f

open class GUIRendererProgram: VBOShaderProgram() {
    override fun getVertexShaderResId() = GUI_VERTEX_SHADER
    override fun getFragmentShaderResId() = GUI_FRAGMENT_SHADER
    override fun getGeometryShaderResId(): String? = null

    override fun getAdditionalParams(scene: GameScene, renderable: IDrawableItem): Map<String, Any>? {
        val result: MutableMap<String, Any> = mutableMapOf(
            TARGET_WIDTH_PARAM_NAME to 0,
            TARGET_HEIGHT_PARAM_NAME to 0
        )

        (renderable as? Box2D)?.background?.let {result[ACTIVE_TEXTURE_SLOT_PARAM_NAME] = it.bind(0u)}
        (renderable as? Box2D)?.effects?.let {result[EFFECTS_PARAM_NAME] = it}

        return result
    }

    override fun bindMVPMatrix(renderable: IDrawableItem, viewMatrix: Matrix4f, projectionMatrix: Matrix4f) {}
}
