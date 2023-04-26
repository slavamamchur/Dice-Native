package org.sadgames.engine.render.gl.material.shaders

import org.sadgames.ROAD_TEXTURE_NAME
import org.sadgames.TERRAIN_ATLAS_TEXTURE_NAME
import org.sadgames.engine.CacheItemType.TEXTURE
import org.sadgames.engine.GameEngine
import org.sadgames.engine.GameEngine.Companion.gameCache
import org.sadgames.engine.SettingsManager
import org.sadgames.engine.cache.TextureCache
import org.sadgames.engine.render.*
import org.sadgames.engine.render.gl.material.textures.AbstractTexture
import org.sadgames.engine.render.gl.models.Abstract3DMesh
import org.sadgames.engine.scene.GameScene
import org.sadgames.engine.scene.items.IDrawableItem
import org.sadgames.engine.utils.Matrix4f.Companion.createRotationFromQuaternion
import org.sadgames.engine.utils.Vector4f
import org.sadgames.engine.utils.toArray
import org.sadgames.engine.utils.toFloatArray

open class SimpleRenderer: AbstractRenderer() {

    var skyBoxRotationAngle = 0f

    override fun getVertexShaderResId() = MAIN_RENDERER_VERTEX_SHADER
    override fun getFragmentShaderResId() = MAIN_RENDERER_FRAGMENT_SHADER

    override fun bindGlobalParams(engine: GameEngine) {
        val lightSource = engine.scene.globalIllumination
        val is2D = SettingsManager.isIn_2D_Mode
        val (shadowW, shadowH) = engine.renderer.shadowMapSize

        params[IS_2D_MODE_PARAM_NAME]?.value = if (is2D) 1 else 0
        params[IS_2D_MODEF_PARAM_NAME]?.value = if (is2D) 1 else 0
        params[ACTIVE_SHADOWMAP_SLOT_PARAM_NAME]?.value = engine.renderer.bindShadowMap(FBO_TEXTURE_SLOT)

        params[CAMERA_POSITION_PARAM_NAME]?.value = engine.scene.activeCamera!!.cameraPosition.toArray()
        params[LIGHT_POSITION_PARAM_NAME]?.value = lightSource?.lightPosInEyeSpace
        params[LIGHT_POSITIONF_PARAM_NAME]?.value = lightSource?.lightPosInEyeSpace
        params[LIGHT_COLOUR_PARAM_NAME]?.value = lightSource?.lightColour?.toArray()

        //todo: move to Skybox
        //params[SKY_BOX_MV_MATRIXF_PARAM_NAME]?.value = createRotationFromQuaternion(Vector4f(0f, skyBoxRotationAngle, 0f, 1f)).toFloatArray()

        params[UX_PIXEL_OFFSET_PARAM_NAME]?.value = 1f / shadowW
        params[UY_PIXEL_OFFSET_PARAM_NAME]?.value = 1f / shadowH
    }

    override fun getAdditionalParams(scene: GameScene, renderable: IDrawableItem) =
        mutableMapOf<String, Any>().also {
            val textures = gameCache[TEXTURE]!! as TextureCache
            val material = (renderable as? Abstract3DMesh)?.material

            if (material?.diffuseMapName != null)
                it[ACTIVE_TEXTURE_SLOT_PARAM_NAME] = textures[material.diffuseMapName!!].bind(0u)
        }
}
