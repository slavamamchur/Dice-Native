package org.sadgames.engine.render.gl.material.shaders.params

import com.kgl.opengl.GL_ARRAY_BUFFER
import com.kgl.opengl.GL_ELEMENT_ARRAY_BUFFER
import kotlinx.cinterop.CValuesRef
import kotlinx.cinterop.refTo
import org.sadgames.engine.utils.memSize

/**
 * Created by Slava Mamchur on 17.04.2023.
 */

enum class VBOElement(val type: UInt, val size: Int) {
    V2D(GL_ARRAY_BUFFER, 2),
    V3D(GL_ARRAY_BUFFER, 3),
    IDX(GL_ELEMENT_ARRAY_BUFFER, 1);

    fun getDataRef(data: Any): Pair<Long, CValuesRef<*>> { //todo: add index support
        val value = data as FloatArray

        return Pair(value.memSize, value.refTo(0))
    }
}