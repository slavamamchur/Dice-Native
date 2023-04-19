package org.sadgames.engine

import com.kgl.glfw.Window
import kotlinx.coroutines.*
import org.sadgames.engine.CacheItemType.TEXTURE
import org.sadgames.engine.cache.AbstractEntityCacheManager
import org.sadgames.engine.cache.TextureCache
import org.sadgames.engine.input.MyGestureDetector
import org.sadgames.engine.input.MyMouseButtonCallBack
import org.sadgames.engine.input.MyMouseMoveCallBack
import org.sadgames.engine.input.MyMouseScrollCallBack
import org.sadgames.engine.render.IRenderer
import org.sadgames.engine.render.gl.models.Box2D
import org.sadgames.engine.scene.GameScene
import org.sadgames.engine.scene.items.IDrawableItem
import org.sadgames.engine.utils.Vector4f

/**
 * Created by Slava Mamchur on 19.02.2023.
 */

class GameEngine(window: Window, val renderer: IRenderer) {
    private val gestureDetector = MyGestureDetector(renderer)
    val scene = GameScene()

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

    //todo: show splash, load, hide splash
    private suspend fun loadScene() = withContext(Dispatchers.Default) {
        scene.putChild(Box2D(Vector4f(-1f, 1f, 1f, -1f),
                             "/home/slava/blm.jpg").also { it.loadObject() })
    }

    fun drawFrame() { renderer.onDraw(scene) }
    fun startGame() = runBlocking {
        loadScene()
    }

    fun stopGame() {
        renderer.onExit()

        scene.processTreeItems({ (it as? IDrawableItem)?.release() }) { true }
        scene.childs.clear()

        gameCache.values.forEach { it.clearCache() }
    }
}
