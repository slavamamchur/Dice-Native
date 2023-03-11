package org.sadgames.engine.render.gl.material.shaders

import org.sadgames.engine.render.TERRAIN_RENDERER_FRAGMENT_SHADER
import org.sadgames.engine.render.TERRAIN_RENDERER_GEOMETRY_SHADER
import org.sadgames.engine.render.TERRAIN_RENDERER_VERTEX_SHADER

open class ImprovedTerrainRenderer: SimpleTerrainRenderer() {
    override fun getVertexShaderResId() = TERRAIN_RENDERER_VERTEX_SHADER
    override fun getFragmentShaderResId() = TERRAIN_RENDERER_FRAGMENT_SHADER
    override fun getGeometryShaderResId() = TERRAIN_RENDERER_GEOMETRY_SHADER

}
