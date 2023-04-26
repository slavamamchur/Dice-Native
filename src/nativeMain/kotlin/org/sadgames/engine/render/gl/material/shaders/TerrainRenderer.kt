package org.sadgames.engine.render.gl.material.shaders

import org.sadgames.ROAD_TEXTURE_NAME
import org.sadgames.TERRAIN_ATLAS_TEXTURE_NAME
import org.sadgames.engine.CacheItemType
import org.sadgames.engine.GameEngine
import org.sadgames.engine.SettingsManager
import org.sadgames.engine.cache.TextureCache
import org.sadgames.engine.render.*
import org.sadgames.engine.render.gl.models.Abstract3DMesh
import org.sadgames.engine.scene.GameScene
import org.sadgames.engine.scene.items.IDrawableItem

open class TerrainRenderer: SimpleRenderer() {
    override fun getVertexShaderResId() = TERRAIN_RENDERER_VERTEX_SHADER
    override fun getFragmentShaderResId() = TERRAIN_RENDERER_FRAGMENT_SHADER
    override fun getGeometryShaderResId() = TERRAIN_RENDERER_GEOMETRY_SHADER

    override fun bindGlobalParams(engine: GameEngine) {
        super.bindGlobalParams(engine)

        val graphicsQualityLevel = SettingsManager.graphicsQualityLevel
        val is2D = SettingsManager.isIn_2D_Mode
        val textures = GameEngine.gameCache[CacheItemType.TEXTURE]!! as TextureCache

        params[RND_SEED__PARAM_NAME]?.value = if (GraphicsQuality.LOW == graphicsQualityLevel || is2D) -1f else engine.scene.moveFactor

        params[ACTIVE_BACKGROUND_SLOT_PARAM_NAME]?.value = textures[engine.backgroundTextureName ?: ""].bind(BACKGROUND_TEXTURE_SLOT)
        params[ACTIVE_ROAD_TEXTURE_SLOT_PARAM_NAME]?.value = textures[ROAD_TEXTURE_NAME].bind(ROAD_TILE_TEXTURE_SLOT)
        params[ACTIVE_TERRAIN_TEXTURE_SLOT_PARAM_NAME]?.value = textures[TERRAIN_ATLAS_TEXTURE_NAME].bind(ROAD_TILE_TEXTURE_SLOT + 1u)
        params[ACTIVE_DEPTHMAP_SLOT_PARAM_NAME]?.value = engine.renderer.bindWaterDepthMap(ROAD_TILE_TEXTURE_SLOT + 2u)
    }

    override fun getAdditionalParams(scene: GameScene, renderable: IDrawableItem): MutableMap<String, Any> {
        return super.getAdditionalParams(scene, renderable).also {
            val textures = GameEngine.gameCache[CacheItemType.TEXTURE]!! as TextureCache
            val material = (renderable as? Abstract3DMesh)?.material

            //todo: set Material Prop
        }
    }
}
