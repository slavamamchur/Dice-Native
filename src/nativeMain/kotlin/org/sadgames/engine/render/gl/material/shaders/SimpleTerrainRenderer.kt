package org.sadgames.engine.render.gl.material.shaders

import org.sadgames.ROAD_TEXTURE_NAME
import org.sadgames.TERRAIN_ATLAS_TEXTURE_NAME
import org.sadgames.engine.CacheItemType.TEXTURE
import org.sadgames.engine.SettingsManager
import org.sadgames.engine.render.*
import org.sadgames.engine.render.gl.material.textures.AbstractTexture
import org.sadgames.engine.scene.GameScene
import org.sadgames.engine.scene.items.IDrawableItem
import org.sadgames.engine.utils.Matrix4f.Companion.createRotationFromQuaternion
import org.sadgames.engine.utils.Vector4f
import org.sadgames.engine.utils.toArray
import org.sadgames.engine.utils.toFloatArray

open class SimpleTerrainRenderer: VBOShaderProgram() {

    var skyBoxRotationAngle = 0f

    override fun getVertexShaderResId() = MAIN_RENDERER_VERTEX_SHADER
    override fun getFragmentShaderResId() = MAIN_RENDERER_FRAGMENT_SHADER

    override fun bindGlobalParams(scene: GameScene) {
        val lightSource = scene.globalIllumination
        val graphicsQualityLevel = SettingsManager.graphicsQualityLevel
        val is2D = SettingsManager.isIn_2D_Mode

        params[ACTIVE_BACKGROUND_SLOT_PARAM_NAME]?.value = (scene.engine.gameCache[TEXTURE]?.get(scene.engine.backgroundTextureName ?: "")
                as AbstractTexture).bind(BACKGROUND_TEXTURE_SLOT)
        params[IS_2D_MODE_PARAM_NAME]?.value = if (is2D) 1 else 0
        params[IS_2D_MODEF_PARAM_NAME]?.value = if (is2D) 1 else 0
        params[ACTIVE_SHADOWMAP_SLOT_PARAM_NAME]?.value = scene.engine.renderer.bindShadowMap(FBO_TEXTURE_SLOT)
        params[ACTIVE_ROAD_TEXTURE_SLOT_PARAM_NAME]?.value = (scene.engine.gameCache[TEXTURE]?.get(ROAD_TEXTURE_NAME) as AbstractTexture).bind(ROAD_TILE_TEXTURE_SLOT)
        params[ACTIVE_TERRAIN_TEXTURE_SLOT_PARAM_NAME]?.value = (scene.engine.gameCache[TEXTURE]?.get(TERRAIN_ATLAS_TEXTURE_NAME) as AbstractTexture).bind(ROAD_TILE_TEXTURE_SLOT + 1u)
        params[ACTIVE_DEPTHMAP_SLOT_PARAM_NAME]?.value = scene.engine.renderer.bindWaterDepthMap(ROAD_TILE_TEXTURE_SLOT + 2u)

        params[CAMERA_POSITION_PARAM_NAME]?.value = scene.activeCamera!!.cameraPosition.toArray()
        params[LIGHT_POSITION_PARAM_NAME]?.value = lightSource.lightPosInEyeSpace
        params[LIGHT_POSITIONF_PARAM_NAME]?.value = lightSource.lightPosInEyeSpace
        params[LIGHT_COLOUR_PARAM_NAME]?.value = lightSource.lightColour.toArray()

        params[RND_SEED__PARAM_NAME]?.value = if (GraphicsQuality.LOW == graphicsQualityLevel || is2D) -1f else scene.moveFactor
        params[SKY_BOX_MV_MATRIXF_PARAM_NAME]?.value = createRotationFromQuaternion(Vector4f(0f, skyBoxRotationAngle, 0f, 1f)).toFloatArray()

        val (shadowW, shadowH) = scene.engine.renderer.shadowMapSize
        params[UX_PIXEL_OFFSET_PARAM_NAME]?.value = 1f / shadowW
        params[UY_PIXEL_OFFSET_PARAM_NAME]?.value = 1f / shadowH
    }

    override fun bindAdditionalParams(scene: GameScene, renderable: IDrawableItem) =
        bindLightSourceMVP(renderable, scene.globalIllumination, true)
}
