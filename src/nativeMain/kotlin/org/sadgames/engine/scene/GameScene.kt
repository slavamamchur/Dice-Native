package org.sadgames.engine.scene

import org.sadgames.engine.SceneItemType
import org.sadgames.engine.scene.items.AbstractNode
import org.sadgames.engine.scene.items.camera.AbstractCamera
import org.sadgames.engine.scene.items.lights.DirectionalLight
import org.sadgames.engine.scene.items.lights.DirectionalLight.Companion.GLOBAL_LIGHT

/**
 * Created by Slava Mamchur on 03.03.2023.
 */

class GameScene: AbstractNode(0, "ROOT") {
    override val type; get() = SceneItemType.ROOT_ITEM

    var activeCamera: AbstractCamera? = null
    var moveFactor = 0f

    val globalIllumination; get() = childs[GLOBAL_LIGHT] as? DirectionalLight

}