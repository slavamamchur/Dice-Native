package org.sadgames.engine.render

import com.kgl.opengl.*
import org.sadgames.engine.utils.Color4f

/**
 * Created by Slava Mamchur on 22.02.2023.
 */

val DEPTH_BUFFER_CLEAR_COLOR = Color4f(1.0f, 1.0f, 1.0f, 1.0f)

enum class GLParamType {
    FLOAT_ATTRIB_ARRAY_PARAM,
    FLOAT_UNIFORM_VECTOR_PARAM,
    FLOAT_UNIFORM_VECTOR4_PARAM,
    FLOAT_UNIFORM_MATRIX_PARAM,
    FLOAT_UNIFORM_PARAM,
    INTEGER_UNIFORM_PARAM
}

const val VERTEXES_PARAM_NAME = "a_Position"
const val TEXELS_PARAM_NAME = "a_Texture"
const val NORMALS_PARAM_NAME = "a_Normal"
const val OFFSETS_PARAM_NAME = "aOffset"
const val ACTIVE_TEXTURE_SLOT_PARAM_NAME = "u_TextureUnit"
const val ACTIVE_REFRACTION_MAP_SLOT_PARAM_NAME = "u_RefractionMapUnit"
const val ACTIVE_SKYBOX_MAP_SLOT_PARAM_NAME = "u_ReflectionMapUnit"
const val ACTIVE_NORMALMAP_SLOT_PARAM_NAME = "u_NormalMapUnit"
const val ACTIVE_DUDVMAP_SLOT_PARAM_NAME = "u_DUDVMapUnit"
const val ACTIVE_BLENDING_MAP_SLOT_PARAM_NAME = "u_BlendingMapUnit"
const val ACTIVE_SHADOWMAP_SLOT_PARAM_NAME = "uShadowTexture"
const val ACTIVE_DEPTHMAP_SLOT_PARAM_NAME = "depthMap"
const val ACTIVE_BACKGROUND_SLOT_PARAM_NAME = "u_BackgroundUnit"
const val ACTIVE_TERRAIN_TEXTURE_SLOT_PARAM_NAME = "u_TerrainAtlas"
const val ACTIVE_ROAD_TEXTURE_SLOT_PARAM_NAME = "u_RoadUnit"
const val ACTIVE_DIRT_TEXTURE_SLOT_PARAM_NAME = "u_DirtUnit"
const val ACTIVE_GRASS_TEXTURE_SLOT_PARAM_NAME = "u_GrassUnit"
const val ACTIVE_SAND_TEXTURE_SLOT_PARAM_NAME = "u_SandUnit"
const val IS_CUBEMAP_PARAM_NAME = "u_isCubeMap"
const val IS_CUBEMAPF_PARAM_NAME = "u_isCubeMapF"
const val IS_2D_MODE_PARAM_NAME = "u_is2DMode"
const val IS_LIGHT_SOURCE_PARAM_NAME = "u_isLightSource"
const val IS_2D_MODEF_PARAM_NAME = "u_is2DModeF"
const val HAS_REFLECT_MAP_PARAM_NAME = "u_hasReflectMap"
const val IS_NORMALMAP_PARAM_NAME = "u_isNormalMap"
const val MVP_MATRIX_PARAM_NAME = "u_MVP_Matrix"
const val LIGHT_MVP_MATRIX_PARAM_NAME = "uShadowProjMatrix"
const val MV_MATRIX_PARAM_NAME = "u_MV_Matrix"
const val MV_MATRIXF_PARAM_NAME = "u_MV_MatrixF"
const val MODEL_MATRIX_PARAM_NAME = "uMMatrix"
const val SKY_BOX_MV_MATRIXF_PARAM_NAME = "u_SkyboxMV_MatrixF"
const val LIGHT_POSITION_PARAM_NAME = "u_lightPosition"
const val LIGHT_POSITIONF_PARAM_NAME = "u_lightPositionF"
const val LIGHT_COLOUR_PARAM_NAME = "u_lightColour"
const val CAMERA_POSITION_PARAM_NAME = "u_camera"
const val RND_SEED__PARAM_NAME = "u_RndSeed"
const val IS_OBJECT_GROUP_PARAM_NAME = "u_isObjectGroup"
const val IS_OBJECT_GROUPF_PARAM_NAME = "u_isObjectGroupF"
const val AMBIENT_RATE_PARAM_NAME = "u_AmbientRate"
const val DIFFUSE_RATE_PARAM_NAME = "u_DiffuseRate"
const val SPECULAR_RATE_PARAM_NAME = "u_SpecularRate"
const val UX_PIXEL_OFFSET_PARAM_NAME = "uxPixelOffset"
const val UY_PIXEL_OFFSET_PARAM_NAME = "uyPixelOffset"
const val TIME_PARAM_NAME = "uTime"
const val ALPHA_SCALE_PARAM_NAME = "uAlphaScale"
const val EFFECTS_PARAM_NAME = "uEffects"
const val CONTRAST_LEVEL_PARAM_NAME = "uContrastLevel"
const val TARGET_WIDTH_PARAM_NAME = "targetWidth"
const val TARGET_HEIGHT_PARAM_NAME = "targetHeight"
const val TESSELLATION_PARAMS_PARAM_NAME = "tess_params"

val GL_PARAM_TYPES: MutableMap<UInt, GLParamType> = hashMapOf(
            GL_INT to GLParamType.INTEGER_UNIFORM_PARAM,
            GL_SAMPLER_2D to GLParamType.INTEGER_UNIFORM_PARAM,
            GL_SAMPLER_2D_SHADOW to GLParamType.INTEGER_UNIFORM_PARAM,
            GL_SAMPLER_2D_ARRAY_SHADOW to GLParamType.INTEGER_UNIFORM_PARAM,
            GL_FLOAT to GLParamType.FLOAT_UNIFORM_PARAM,
            GL_FLOAT_MAT4 to GLParamType.FLOAT_UNIFORM_MATRIX_PARAM,
            GL_FLOAT_VEC3 to GLParamType.FLOAT_UNIFORM_VECTOR_PARAM,
            GL_FLOAT_VEC4 to GLParamType.FLOAT_UNIFORM_VECTOR4_PARAM)

enum class GraphicsQuality {
    LOW, MEDIUM, HIGH, ULTRA
}

val SHADOW_MAP_RESOLUTION_SCALE = floatArrayOf(0.5f, 1.0f, 1.5f, 2.0f)
val TEXTURE_RESOLUTION_SCALE = intArrayOf(4, 2, 1, 1)

const val MAIN_RENDERER_VERTEX_SHADER = "shaders/vertex_shader.glsl"
const val MAIN_RENDERER_FRAGMENT_SHADER = "shaders/fragment_shader.glsl"
const val TERRAIN_RENDERER_VERTEX_SHADER = "shaders/vs_terrain32.glsl"
const val TERRAIN_RENDERER_GEOMETRY_SHADER = "shaders/gs_terrain32.glsl"
const val TERRAIN_RENDERER_FRAGMENT_SHADER = "shaders/fs_terrain32.glsl"
const val PLANET_RENDERER_VERTEX_SHADER = "shaders/vs_planet.glsl"
const val PLANET_RENDERER_TC_SHADER = "shaders/tc_planet.glsl"
const val PLANET_RENDERER_TE_SHADER = "shaders/te_planet.glsl"
const val PLANET_RENDERER_GEOMETRY_SHADER = "shaders/gs_planet.glsl"
const val PLANET_RENDERER_FRAGMENT_SHADER = "shaders/fs_planet.glsl"
const val WATER_RENDERER_VERTEX_SHADER = "shaders/vs_water.glsl"
const val WATER_RENDERER_VERTEX_SHADER_ES32 = "shaders/vs_water_es32.glsl"
const val WATER_RENDERER_FRAGMENT_SHADER = "shaders/fs_water.glsl"
const val WATER_RENDERER_GEOMETRY_SHADER = "shaders/gs_water.glsl"
const val GEN_TERRAIN_FRAGMENT_SHADER = "shaders/fs_genTerrain.glsl"
const val SHADOWMAP_VERTEX_SHADER = "shaders/v_depth_map.glsl"
const val SHADOWMAP_VERTEX_SHADER_DEPTH_SUPPORT = "shaders/depth_tex_v_depth_map.glsl"
const val SHADOWMAP_FRAGMENT_SHADER = "shaders/f_depth_map.glsl"
const val SHADOWMAP_FRAGMENT_SHADER_DEPTH_SUPPORT = "shaders/depth_tex_f_depth_map.glsl"
const val SHADOWMAP_GEOMETRY_SHADER_DEPTH_SUPPORT = "shaders/depth_tex_g_depth_map.glsl"
const val GUI_VERTEX_SHADER = "shaders/gui_vertex.glsl"
const val SUN_VERTEX_SHADER = "shaders/sun_vertex.glsl"
const val GUI_FRAGMENT_SHADER = "shaders/gui_fragment.glsl"
const val SUN_FRAGMENT_SHADER = "shaders/sun_fragment.glsl"
const val FLARE_FRAGMENT_SHADER = "shaders/flare_fragment.glsl"
const val FOREST_VERTEX_SHADER = "shaders/forest_vertex.glsl"
const val SKYBOX_VERTEX_SHADER = "shaders/skybox_vertex.glsl"
const val SKYBOX_FRAGMENT_SHADER = "shaders/skybox_fragment.glsl"
const val SKYDOME_VERTEX_SHADER = "shaders/skydome_vertex.glsl"
const val SKYDOME_FRAGMENT_SHADER = "shaders/skydome_fragment.glsl"
const val REFLECTION_MAP_VERTEX_SHADER = "shaders/vs_reflect_map.glsl"
const val REFLECTION_MAP_FRAGMENT_SHADER = "shaders/fs_reflect_map.glsl"
const val REFRACTION_MAP_VERTEX_SHADER = "shaders/vs_refract_map.glsl"
const val REFRACTION_MAP_FRAGMENT_SHADER = "shaders/fs_refract_map.glsl"
const val RAYS_VERTEX_SHADER = "shaders/vs_rays.glsl"
const val RAYS_FRAGMENT_SHADER = "shaders/fs_rays.glsl"

const val FBO_TEXTURE_SLOT = 6u
const val BACKGROUND_TEXTURE_SLOT = 7u
const val ROAD_TILE_TEXTURE_SLOT = 8u