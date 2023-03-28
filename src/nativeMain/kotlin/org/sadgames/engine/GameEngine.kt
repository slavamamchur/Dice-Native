package org.sadgames.engine

import com.kgl.glfw.Window
import org.sadgames.engine.CacheItemType.TEXTURE
import org.sadgames.engine.cache.AbstractEntityCacheManager
import org.sadgames.engine.cache.TextureCache
import org.sadgames.engine.input.MyGestureDetector
import org.sadgames.engine.input.MyMouseButtonCallBack
import org.sadgames.engine.input.MyMouseMoveCallBack
import org.sadgames.engine.input.MyMouseScrollCallBack
import org.sadgames.engine.render.IRenderer
import org.sadgames.engine.scene.GameScene

/**
 * Created by Slava Mamchur on 19.02.2023.
 */

class GameEngine(window: Window, val renderer: IRenderer) {
    private val gestureDetector = MyGestureDetector(renderer)
    private val scene = GameScene(this)

    @ThreadLocal companion object {
        var screenWidth = 0
        var screenHeight = 0

        val gameCache: MutableMap<CacheItemType, AbstractEntityCacheManager<*>> = hashMapOf(
            TEXTURE to TextureCache
        )
    }

    var backgroundTextureName: String? = null

    init {
        with(window) {
            setFrameBufferCallback { _, w, h -> renderer.onResize(w, h) }
            setMouseButtonCallback(MyMouseButtonCallBack(gestureDetector))
            setCursorPosCallback(MyMouseMoveCallBack(gestureDetector))
            setScrollCallback(MyMouseScrollCallBack(gestureDetector))

            screenWidth = frameBufferSize.first
            screenHeight = frameBufferSize.second
        }
    }

    fun drawScene() { renderer.onDraw(scene) }
    fun startGame() {}
    fun stopGame() { renderer.onExit() }
}
