package org.sadgames.engine.render.gl.material.shaders.params

import com.kgl.opengl.GL_ELEMENT_ARRAY_BUFFER
import kotlinx.cinterop.CValuesRef
import kotlinx.cinterop.refTo
import org.sadgames.engine.utils.memSize

object IDX: VBOElement<ShortArray>(GL_ELEMENT_ARRAY_BUFFER, 1) {
    override fun getDataRef(data: ShortArray): Pair<Long, CValuesRef<*>>  = Pair(data.memSize, data.refTo(0))
}