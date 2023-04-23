package org.sadgames.engine.render.gl.material.shaders.params

import com.kgl.opengl.GL_ARRAY_BUFFER
import kotlinx.cinterop.CValuesRef
import kotlinx.cinterop.refTo
import org.sadgames.engine.utils.memSize

object V2D: VBOElement<FloatArray>(GL_ARRAY_BUFFER, 2)  {
    override fun getDataRef(data: FloatArray): Pair<Long, CValuesRef<*>> = Pair(data.memSize, data.refTo(0))
}