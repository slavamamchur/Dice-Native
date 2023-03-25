package org.sadgames.engine.render.gl.material.shaders

import com.kgl.opengl.*
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
    private val shaderProgram = getShaderProgram()

    init {
        if (shaderProgram.isCompiled) {
            createParams()
        } else {
            throw RuntimeException(shaderProgram.log)
        }
    }

    protected abstract fun getVertexShaderResId(): String?
    protected abstract fun getFragmentShaderResId(): String?

    abstract fun bindGlobalParams(scene: GameScene)
    abstract fun bindAdditionalParams(scene: GameScene, renderable: IDrawableItem)

    protected open fun getGeometryShaderResId(): String? = null

    protected open fun getShaderProgram() = MyShaderProgram(
        if (getGeometryShaderResId()?.isNotEmpty() == true)
                        hashMapOf(
                        GL_VERTEX_SHADER to  getVertexShaderResId()!!,
                        GL_GEOMETRY_SHADER to getGeometryShaderResId()!!,
                        GL_FRAGMENT_SHADER to getFragmentShaderResId()!!)
        else
            hashMapOf(
                GL_VERTEX_SHADER to  getVertexShaderResId()!!,
                GL_FRAGMENT_SHADER to getFragmentShaderResId()!!)
    )

    fun useProgram() = shaderProgram.begin()
    fun deleteProgram() = shaderProgram.dispose()

    private fun createParams() {
        params.clear()
        createAttributes()
        createUniforms()
    }

    protected open fun createAttributes() {
        shaderProgram.attributeNames.forEach {
            params[it] = if(it == OFFSETS_PARAM_NAME)
                                GLInstancedShaderParam(it, shaderProgram.programId)
                            else
                                GLShaderParam(FLOAT_ATTRIB_ARRAY_PARAM, it, shaderProgram.programId)
        }
    }

    protected open fun createUniforms() {
        shaderProgram.uniformTypes.forEach {
            params[it.key] = GLShaderParam(GL_PARAM_TYPES[it.value]!!, it.key, shaderProgram.programId)
        }
    }

    protected open fun bindLightSourceMVP(renderable: IDrawableItem, ls: DirectionalLight?, hasDepthTextureExtension: Boolean) {
        params[LIGHT_MVP_MATRIX_PARAM_NAME]?.value =
            (Matrix4f(BIAS) * (Matrix4f(ls!!.projectionMatrix) * (Matrix4f(ls.viewMatrix) * renderable.transform))).toFloatArray()
    }

    fun setMVPMatrixData(data: FloatArray) {
        params[MVP_MATRIX_PARAM_NAME]?.value = data
    }

    fun setMVMatrixData(data: FloatArray) {
        params[MV_MATRIX_PARAM_NAME]?.value = data
        params[MV_MATRIXF_PARAM_NAME]?.value = data
    }

    open fun bindMVPMatrix(renderable: IDrawableItem, viewMatrix: Matrix4f, projectionMatrix: Matrix4f) {
        val mMVMatrix = viewMatrix * renderable.transform
        setMVMatrixData(mMVMatrix.toFloatArray())
        setMVPMatrixData((projectionMatrix * mMVMatrix).toFloatArray())
    }

    fun setAdditionalParams(paramMap: Map<String, Any>) {
        for (entry in paramMap.entries) {
            val param = params[entry.key]

            if (param != null && param.paramReference >= 0)
                param.value = entry.value
        }
    }
}