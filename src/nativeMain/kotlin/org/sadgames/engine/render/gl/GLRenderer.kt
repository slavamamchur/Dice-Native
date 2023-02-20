package org.sadgames.engine.render.gl

import com.kgl.opengl.*
import org.sadgames.engine.render.IRenderer

class GLRenderer: IRenderer {
    init {
        glClearColor(0.1f, 0.2f, 0.3f, 1.0f)
        glEnable(GL_MULTISAMPLE)
        glEnable(GL_DEPTH_TEST)
        glEnable(GL_CULL_FACE)
    }

    override fun onResize(width: Int, height: Int) {
        glViewport(0, 0, width, height)
        //TODO("(Re)Create FBos)
    }

    override fun onDraw() {
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
        glCullFace(GL_BACK)
        //TODO("Implement")
    }

    override fun onExit() {
        //TODO("Release Buffers, Textures, FBos and other resources")
    }
}