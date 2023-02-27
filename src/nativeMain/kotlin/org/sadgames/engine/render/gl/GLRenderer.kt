package org.sadgames.engine.render.gl

import com.kgl.opengl.*
import org.sadgames.engine.GameEngine
import org.sadgames.engine.render.IRenderer
import org.sadgames.engine.render.gl.fbo.ColorBufferFBO
import org.sadgames.engine.render.gl.fbo.DepthBufferFBO
import org.sadgames.engine.utils.Color4f

class GLRenderer: IRenderer {

    private var mainFbo: ColorBufferFBO? = null
    private var shadowMap: DepthBufferFBO? = null

    init {
        glClearColor(0.1f, 0.2f, 0.3f, 1f)
        glEnable(GL_MULTISAMPLE)
        glEnable(GL_DEPTH_TEST)
        glEnable(GL_CULL_FACE)
    }

    override fun onResize(width: Int, height: Int) {
        glViewport(0, 0, width, height)

        GameEngine.screenWidth = width
        GameEngine.screenHeight = height

        shadowMap?.cleanUp()
        shadowMap = DepthBufferFBO(width / 2, height / 2)

        mainFbo?.cleanUp()
        mainFbo = ColorBufferFBO(width, height, Color4f(0.1f, 0.2f, 0.3f, 1f), isMultiSampled = true)
    }

    override fun onDraw() { //TODO("Implement")
        mainFbo?.bind()

        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
        glCullFace(GL_BACK)

        mainFbo?.unbind()
        mainFbo?.blit(null)
    }

    override fun onExit() {
        mainFbo?.cleanUp()
        shadowMap?.cleanUp()
    }
}