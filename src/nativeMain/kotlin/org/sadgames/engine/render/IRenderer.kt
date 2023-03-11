package org.sadgames.engine.render

interface IRenderer {
    val shadowMapSize: Pair<Int, Int>

    fun onResize(width: Int, height: Int)
    fun onDraw()
    fun onExit()

    fun bindShadowMap(slot: UInt): Int?
    fun bindWaterDepthMap(slot: UInt): Int?
}