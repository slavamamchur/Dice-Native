package org.sadgames.engine.utils

import com.kgl.stb.STBIOCallbacks
import io.ktor.utils.io.bits.*
import okio.*
import okio.Path.Companion.toPath
import org.sadgames.engine.IO

/**
 * Created by Slava Mamchur on 27.02.2023.
 */

fun readTextFromFile(path: String) = IO.source(path.toPath()).buffer().use { it.readUtf8() }

class STBIOFile(path: String): STBIOCallbacks, Closeable {
    val source = IO.source(path.toPath()).buffer()

    override val eof: Boolean get() = !source.request(1)

    override fun read(data: Memory): Int {
        val buffer = source.readByteArray()
        data.storeByteArray(0, buffer)

        return buffer.size
    }

    override fun skip(n: Int) {
        source.skip(n.toLong())
    }

    override fun close() {
        source.close()
    }
}