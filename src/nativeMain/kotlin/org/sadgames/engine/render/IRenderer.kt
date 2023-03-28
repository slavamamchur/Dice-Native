package org.sadgames.engine.render

import org.sadgames.engine.scene.GameScene

interface IRenderer {
    val shadowMapSize: Pair<Int, Int>

    fun onResize(width: Int, height: Int)
    fun onDraw(scene: GameScene)
    fun onExit()

    fun bindShadowMap(slot: UInt): Int?
    fun bindWaterDepthMap(slot: UInt): Int?
}