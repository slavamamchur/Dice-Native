package org.sadgames.engine.render.gl.material.shaders

import org.sadgames.engine.render.EFFECTS_PARAM_NAME
import org.sadgames.engine.render.GUI_FRAGMENT_SHADER
import org.sadgames.engine.render.GUI_VERTEX_SHADER
import org.sadgames.engine.render.TARGET_WIDTH_PARAM_NAME
import org.sadgames.engine.render.TARGET_HEIGHT_PARAM_NAME
import org.sadgames.engine.render.gl.models.Box2D
import org.sadgames.engine.scene.GameScene
import org.sadgames.engine.scene.items.IDrawableItem
import org.sadgames.engine.utils.Matrix4f

open class GUIRendererProgram: ShadowMapProgram() {

    override fun getVertexShaderResId() = GUI_VERTEX_SHADER
    override fun getFragmentShaderResId() = GUI_FRAGMENT_SHADER
    override fun getGeometryShaderResId(): String? = null

    override fun bindAdditionalParams(scene: GameScene, renderable: IDrawableItem) {
        super.bindAdditionalParams(scene, renderable)

        params[EFFECTS_PARAM_NAME]?.value = (renderable as? Box2D)?.effects
        params[TARGET_WIDTH_PARAM_NAME]?.value = 0
        params[TARGET_HEIGHT_PARAM_NAME]?.value = 0
    }

    override fun bindMVPMatrix(renderable: IDrawableItem, viewMatrix: Matrix4f, projectionMatrix: Matrix4f) {}
}
