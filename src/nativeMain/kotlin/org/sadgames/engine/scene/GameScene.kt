package org.sadgames.engine.scene

import org.sadgames.engine.CacheItemType
import org.sadgames.engine.CacheItemType.*
import org.sadgames.engine.GameEngine
import org.sadgames.engine.cache.AbstractEntityCacheManager
import org.sadgames.engine.cache.TextureCache
import org.sadgames.engine.scene.items.AbstractNode
import org.sadgames.engine.scene.items.camera.AbstractCamera
import org.sadgames.engine.scene.items.lights.DirectionalLight

/**
 * Created by Slava Mamchur on 03.03.2023.
 */

class GameScene(val engine: GameEngine): AbstractNode(0, "ROOT") {
    var activeCamera: AbstractCamera? = null
    var moveFactor = 0f
    val globalIllumination: DirectionalLight; get() = TODO("Not implemented yet!")

}