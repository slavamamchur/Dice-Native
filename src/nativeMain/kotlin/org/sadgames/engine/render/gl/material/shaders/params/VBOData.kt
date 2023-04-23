package org.sadgames.engine.render.gl.material.shaders.params

import com.kgl.opengl.*
import kotlinx.cinterop.CValuesRef
import org.sadgames.engine.utils.toPtr

class VBOData(val element: VBOElement<*>, val stride: Int = 0, pos: Int = 0) {
    var handle = glGenBuffer(); private set
    var data: Any? = null; private set
    val pointer: CValuesRef<*>? = pos.toPtr()

    inline fun bind() = glBindBuffer(element.type, handle)
    inline fun unBind() = glBindBuffer(element.type, 0u)

    fun put(data: Any) {
        bind()
        element.put(data)
        unBind()

        this.data = data
    }

    fun clear() {
        if (handle > 0u) {
            unBind()
            glDeleteBuffer(handle)
            handle = 0u
        }
    }

    fun reload() {
        if (data != null) {
            clear()
            handle = glGenBuffer()
            put(data!!)
        }
    }

    fun release() {
        clear()
        data = null
    }
}