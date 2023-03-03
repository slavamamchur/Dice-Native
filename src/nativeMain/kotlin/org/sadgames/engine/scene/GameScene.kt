package org.sadgames.engine.scene

import org.sadgames.engine.scene.items.AbstractNode
import org.sadgames.engine.scene.items.camera.AbstractCamera
import org.sadgames.engine.scene.items.lights.DirectionalLight

/**
 * Created by Slava Mamchur on 03.03.2023.
 */

class GameScene: AbstractNode(0, "ROOT") {
    var activeCamera: AbstractCamera? =null
    val globalIllumination: DirectionalLight; get() = TODO("Not implemented yet!")

    //todo: cache...
}