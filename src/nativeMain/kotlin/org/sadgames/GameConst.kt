package org.sadgames

import org.sadgames.engine.utils.argb

/**
 * Created by Slava Mamchur on 09.03.2023.
 */

const val SEA_BOTTOM_TEXTURE = "5bae5d26f2675fb54e7cb7dc"
const val NORMALMAP_TEXTURE = "5bae5be1f2675fb54e7cb7da"
const val DUDVMAP_TEXTURE = "5bae5df2f2675fb54e7cb7df"
const val BLENDING_MAP_TEXTURE = "BLENDING_MAP_TEXTURE"
const val SKY_BOX_TEXTURE_NAME = "SKY_BOX_CUBE_MAP_TEXTURE"
const val SKY_DOME_TEXTURE_NAME = "5bae5eb9f2675fb54e7cb7e8"
const val MAP_BACKGROUND_TEXTURE_NAME = "5bae5f0cf2675fb54e7cb7ee"
const val TERRAIN_ATLAS_TEXTURE_NAME = "terrainAtlas4.png"
const val EARTH_DISP_MAP_TEXTURE_NAME = "Earth_Disp_Map.png"
const val NORMALMAP_TERRAIN_ATLAS_TEXTURE_NAME = "normalTerrainAtlas2.png"
const val DISTORTION_TERRAIN_ATLAS_TEXTURE_NAME = "distortionTerrainAtlas2.jpg"
const val ROAD_TEXTURE_NAME = "stone.png"
const val DIRT_TEXTURE_NAME = "dirt.png"
const val GRASS_TEXTURE_NAME = "grass.png"
const val SAND_TEXTURE_NAME = "sand.jpg"
const val SUN_TEXTURE_NAME = "sun2.png"
const val CUBE_TEXTURE_PREFIX = "CUBE_TEXTURE_"

const val ROLLING_DICE_SOUND = "rolling_dice.mp3"

const val TERRAIN_MESH_OBJECT = "TERRAIN_MESH_OBJECT"
const val WATER_MESH_OBJECT = "WATER_MESH_OBJECT"
const val CHIP_MESH_OBJECT = "CHIP_MESH_OBJECT"
const val DICE_MESH_OBJECT_1 = "5bae5fc2f2675fb54e7cb7f5"
const val SKY_BOX_CUBE_MAP_OBJECT = "SKY_BOX_CUBE_MAP_OBJECT"
const val MINI_MAP_OBJECT = "MINI_MAP_OBJECT"
const val SUN_OBJECT = "SUN_OBJECT"

val PATH_COLOR: Int = argb(255, 0, 255, 0)
val WAY_POINT_COLOR: Int = argb(255, 255, 0, 0)

const val NO_POST_EFFECTS: UShort = 0x00u
const val GOD_RAYS_POST_EFFECT: UShort = 0x01u
const val CONTRAST_CHARGE_EFFECT: UShort = 0x02u
const val BLUR_EFFECT: UShort = 0x04u
const val BLOOM_EFFECT: UShort = 0x10u
const val DOF_EFFECT: UShort = 0x20u

const val ON_BEFORE_DRAW_FRAME_EVENT_HANDLER = "beforeDrawFrame"
const val ON_ROLLING_OBJECT_START_EVENT_HANDLER = "onRollingObjectStart"
const val ON_ROLLING_OBJECT_STOP_EVENT_HANDLER = "onRollingObjectStop"
const val ON_MOVING_OBJECT_STOP_EVENT_HANDLER = "onMovingObjectStop"
const val ON_PLAY_TURN_EVENT_HANDLER = "onPlayTurn"
const val ON_PLAYER_MAKE_TURN_EVENT_HANDLER = "onPlayerMakeTurn"
const val ON_PLAYER_CONTINUE_TURN_EVENT_HANDLER = "onPlayerContinueTurn"
const val ON_PLAYER_NEXT_MOVE__EVENT_HANDLER = "playerNextMove"
const val ON_CREATE_DYNAMIC_ITEMS_HANDLER = "onCreateDynamicItems"
const val ON_GAME_RESTARTED_EVENT_HANDLER = "onGameRestarted"
const val ON_PREPARE_MAP_TEXTURE_EVENT_HANDLER = "drawPath"
const val ON_CREATE_REFLECTION_MAP_EVENT_HANDLER = "onCreateReflectionMap"
const val ON_INIT_CAMERA_EVENT_HANDLER = "onCameraInit"
const val ON_INIT_LIGHT_SOURCE_EVENT_HANDLER = "onInitLightSource"
const val ON_INIT_PHYSICS_EVENT_HANDLER = "onInitPhysics"

enum class GameState {
    WAIT, MOVING, FINISHED
}

enum class GLObjectType {
    WATER_OBJECT,
    TERRAIN_OBJECT,
    TERRAIN_OBJECT_32,
    GEN_TERRAIN_OBJECT,
    SKY_BOX_OBJECT,
    SKY_DOME_OBJECT,
    LIGHT_OBJECT,
    GAME_ITEM_OBJECT,
    SHADOW_MAP_OBJECT,
    GUI_OBJECT,
    SUN_OBJECT,
    FLARE_OBJECT,
    FOREST_OBJECT,
    REFLECTION_MAP_OBJECT,
    REFRACTION_MAP_OBJECT,
    RAYS_MAP_OBJECT,
    POST_PROCESS_OBJECT,
    PLANET_OBJECT,
    UNKNOWN_OBJECT
}
