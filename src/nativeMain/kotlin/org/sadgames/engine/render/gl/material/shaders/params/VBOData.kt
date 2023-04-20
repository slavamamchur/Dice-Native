package org.sadgames.engine.render.gl.material.shaders.params

import com.kgl.opengl.*
import kotlinx.cinterop.CValuesRef
import org.sadgames.engine.utils.toPtr

class VBOData(val element: VBOElement, val stride: Int = 0, pos: Int = 0) {
    var handle = glGenBuffer(); private set
    var data: Any? = null; protected set
    val pointer: CValuesRef<*>? = pos.toPtr()

    inline fun bind() = glBindBuffer(element.type, handle)
    inline fun unBind() = glBindBuffer(element.type, 0u)

    fun put(data: Any) {
        bind()
        val (size, reference) = element.getDataRef(data)
        glBufferData(element.type, size, reference, GL_STATIC_DRAW)
        unBind()

        this.data = data
    }

    fun release() {
        unBind()
        data = null

        if (handle > 0u) {
            glDeleteBuffer(handle)
            handle = 0u
        }
    }
}