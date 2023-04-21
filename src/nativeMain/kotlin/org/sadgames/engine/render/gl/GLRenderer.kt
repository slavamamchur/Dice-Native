package org.sadgames.engine.render.gl

import com.kgl.opengl.*
import org.sadgames.GLObjectType
import org.sadgames.engine.GameEngine
import org.sadgames.engine.render.IRenderer
import org.sadgames.engine.render.gl.fbo.ColorBufferFBO
import org.sadgames.engine.render.gl.fbo.DepthBufferFBO
import org.sadgames.engine.render.gl.material.shaders.*
import org.sadgames.engine.scene.GameScene
import org.sadgames.engine.scene.items.IDrawableItem
import org.sadgames.engine.utils.Color4f

class GLRenderer: IRenderer {
    companion object {
        private val shaderCache: MutableMap<GLObjectType, VBOShaderProgram> = hashMapOf()
        fun createShader(type: GLObjectType) = shaderCache[type] ?:  (when (type) {
                GLObjectType.TERRAIN_OBJECT_32 -> ImprovedTerrainRenderer()
                //GLObjectType.WATER_OBJECT -> WaterRendererProgram()
                //GLObjectType.GEN_TERRAIN_OBJECT -> GenTerrainProgram()
                GLObjectType.SHADOW_MAP_OBJECT -> ShadowMapProgram()
                GLObjectType.GUI_OBJECT -> GUIRendererProgram()
                //GLObjectType.SKY_BOX_OBJECT -> SkyBoxProgram()
                //GLObjectType.SKY_DOME_OBJECT -> SkyDomeProgram()
                //GLObjectType.SUN_OBJECT -> SunRendererProgram()
                //GLObjectType.FLARE_OBJECT -> SunFlareProgram()
                //GLObjectType.FOREST_OBJECT -> ForestRenderer()
                //GLObjectType.REFLECTION_MAP_OBJECT -> ReflectionMapRenderProgram()
                //GLObjectType.REFRACTION_MAP_OBJECT -> RefractionMapRenderProgram()
                //GLObjectType.RAYS_MAP_OBJECT -> RaysMapProgram()
                else -> SimpleTerrainRenderer()
            }).also {shaderCache[type] = it}
    }

    private var mainFbo: ColorBufferFBO? = null
    private var shadowMap: DepthBufferFBO? = null
    private var refractionMap: ColorBufferFBO? = null

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

        refractionMap?.cleanUp()
        refractionMap = ColorBufferFBO(width / 2, height / 2, Color4f(0.0f, 0.0f, 0.0f, 0.0f), true, 2)

        mainFbo?.cleanUp()
        mainFbo = ColorBufferFBO(width, height, Color4f(0.1f, 0.2f, 0.3f, 1f), isMultiSampled = true)
    }

    private fun drawItem(item: IDrawableItem) {
        item.bind()
        item.render()
    }

    override fun onDraw(scene: GameScene) {
        mainFbo?.bind()

        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
        glCullFace(GL_BACK)

        glDisable(GL_DEPTH_TEST)

        scene.processTreeItems({ drawItem((it as IDrawableItem)) }) { it is IDrawableItem }

        mainFbo?.unbind()
        mainFbo?.blit(null)
    }

    override fun onExit() {
        mainFbo?.cleanUp()
        shadowMap?.cleanUp()
        refractionMap?.cleanUp()

        shaderCache.forEach { it.value.deleteProgram() }
        shaderCache.clear()
    }

    override fun bindShadowMap(slot: UInt) = shadowMap?.fboTexture?.bind(slot)
    override fun bindWaterDepthMap(slot: UInt) = refractionMap?.depthTexture?.bind(slot)
    override val shadowMapSize: Pair<Int, Int>; get() = Pair(shadowMap?.width ?: 0, shadowMap?.height ?: 0)
}
