package org.sadgames.engine.render.gl.material.shaders.params

import com.kgl.opengl.GL_STATIC_DRAW
import com.kgl.opengl.glBufferData
import kotlinx.cinterop.CValuesRef

abstract class VBOElement<T>(val type: UInt, val size: Int) {
    abstract fun getDataRef(data: T): Pair<Long, CValuesRef<*>>

    @Suppress("UNCHECKED_CAST")
    fun put(data: Any) {
        val (size, reference) = getDataRef(data as T)
        glBufferData(type, size, reference, GL_STATIC_DRAW)
    }
}