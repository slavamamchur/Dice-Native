package org.sadgames.engine.render

interface IRenderer {
    fun onResize(width: Int, height: Int)
    fun onDraw()
    fun onExit()
}