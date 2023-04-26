package org.sadgames.engine.render.gl.material.shaders

import org.sadgames.engine.GameEngine
import org.sadgames.engine.render.IS_OBJECT_GROUP_PARAM_NAME
import org.sadgames.engine.render.MODEL_MATRIX_PARAM_NAME
import org.sadgames.engine.render.SHADOWMAP_FRAGMENT_SHADER_DEPTH_SUPPORT
import org.sadgames.engine.render.SHADOWMAP_VERTEX_SHADER_DEPTH_SUPPORT
import org.sadgames.engine.scene.GameScene
import org.sadgames.engine.scene.items.IDrawableItem
import org.sadgames.engine.utils.Matrix4f
import org.sadgames.engine.utils.toFloatArray

open class ShadowMapRenderer: AbstractRenderer() {
    override fun getVertexShaderResId() = SHADOWMAP_VERTEX_SHADER_DEPTH_SUPPORT
    override fun getFragmentShaderResId() = SHADOWMAP_FRAGMENT_SHADER_DEPTH_SUPPORT
    override fun bindGlobalParams(engine: GameEngine) {}
    override fun bindLocalParams(scene: GameScene, renderable: IDrawableItem) { //todo: move to additional params
        val isObjectGroup = renderable.instancedItem
        params[IS_OBJECT_GROUP_PARAM_NAME]?.value = if (isObjectGroup) 1 else 0

        if (isObjectGroup) {
            renderable.transform.m42 -= 0.03530573f
            params[MODEL_MATRIX_PARAM_NAME]?.value = renderable.transform.toFloatArray()
            renderable.transform.m42 += 0.03530573f
        }
    }

    override fun bindMVPMatrix(renderable: IDrawableItem, viewMatrix: Matrix4f, projectionMatrix: Matrix4f) {
        val modelMatrix = if (renderable.instancedItem) Matrix4f() else renderable.transform
        setMVPMatrixData((projectionMatrix * viewMatrix * modelMatrix).toFloatArray())
    }
}
