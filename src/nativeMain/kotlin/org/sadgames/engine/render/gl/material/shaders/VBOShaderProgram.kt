package org.sadgames.engine.render.gl.material.shaders

import com.kgl.opengl.*
import org.sadgames.engine.GameEngine
import org.sadgames.engine.scene.items.lights.DirectionalLight
import org.sadgames.engine.render.GLParamType.FLOAT_ATTRIB_ARRAY_PARAM
import org.sadgames.engine.render.*
import org.sadgames.engine.render.gl.material.shaders.params.GLInstancedShaderParam
import org.sadgames.engine.render.gl.material.shaders.params.GLShaderParam
import org.sadgames.engine.scene.GameScene
import org.sadgames.engine.scene.items.IDrawableItem
import org.sadgames.engine.utils.Matrix4f
import org.sadgames.engine.utils.toFloatArray

abstract class VBOShaderProgram {
    companion object {
        val BIAS = floatArrayOf(0.5f, 0.0f, 0.0f, 0.0f,
            0.0f, 0.5f, 0.0f, 0.0f,
            0.0f, 0.0f, 0.5f, 0.0f,
            0.5f, 0.5f, 0.5f, 1.0f)
    }

              val params: MutableMap<String, GLShaderParam?> = HashMap()
    private   val shader = createNativeShader()

    init {
        if (shader.isCompiled) {
            createParams()
        } else {
            throw RuntimeException(shader.log)
        }
    }

    protected abstract fun getVertexShaderResId(): String?
    protected abstract fun getFragmentShaderResId(): String?

    open fun bindGlobalParams(engine: GameEngine) {}
    open fun bindLocalParams(scene: GameScene, renderable: IDrawableItem) {
        scene.activeCamera?.let { bindMVPMatrix(renderable, Matrix4f(it.viewMatrix), Matrix4f(it.projectionMatrix)) }
        getAdditionalParams(scene, renderable)?.forEach { params[it.key]?.value = it.value }
    }

    protected open fun getGeometryShaderResId(): String? = null

    private fun createNativeShader() = MyShaderProgram(
                        hashMapOf(
                        GL_VERTEX_SHADER to  getVertexShaderResId()!!,
                        GL_FRAGMENT_SHADER to getFragmentShaderResId()!!).also {
                            if (getGeometryShaderResId()?.isNotEmpty() == true)
                                it[GL_GEOMETRY_SHADER] = getGeometryShaderResId()!!
                        }
    )

    fun bind() = shader.begin()
    fun release() = shader.dispose()

    private fun createParams() {
        params.clear()
        createAttributes()
        createUniforms()
    }

    protected open fun createAttributes() {
        shader.attributeNames.forEach {
            params[it] = if(it == OFFSETS_PARAM_NAME)
                                GLInstancedShaderParam(it, shader.programId)
                            else
                                GLShaderParam(FLOAT_ATTRIB_ARRAY_PARAM, it, shader.programId)
        }
    }

    protected open fun createUniforms() {
        shader.uniformTypes.forEach {
            params[it.key] = GLShaderParam(GL_PARAM_TYPES[it.value]!!, it.key, shader.programId)
        }
    }

    protected open fun bindLightSourceMVP(renderable: IDrawableItem, ls: DirectionalLight?, hasDepthTextureExtension: Boolean) {
        params[LIGHT_MVP_MATRIX_PARAM_NAME]?.value =
            (Matrix4f(BIAS) * (Matrix4f(ls!!.projectionMatrix) * (Matrix4f(ls.viewMatrix) * renderable.transform))).toFloatArray()
    }

    protected open fun setMVPMatrixData(data: FloatArray) {
        params[MVP_MATRIX_PARAM_NAME]?.value = data
    }

    protected open fun setMVMatrixData(data: FloatArray) {
        params[MV_MATRIX_PARAM_NAME]?.value = data
        params[MV_MATRIXF_PARAM_NAME]?.value = data
    }

    protected open fun bindMVPMatrix(renderable: IDrawableItem, viewMatrix: Matrix4f, projectionMatrix: Matrix4f) {
        val mMVMatrix = viewMatrix * renderable.transform
        setMVMatrixData(mMVMatrix.toFloatArray())
        setMVPMatrixData((projectionMatrix * mMVMatrix).toFloatArray())
    }

    protected open fun getAdditionalParams(scene: GameScene, renderable: IDrawableItem): Map<String, Any>? = null

    fun setAdditionalParams(paramMap: Map<String, Any>) {
        paramMap.entries.forEach { params[it.key]?.value = it.value }
    }
}