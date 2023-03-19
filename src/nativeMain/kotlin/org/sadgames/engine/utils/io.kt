package org.sadgames.engine.utils

import com.kgl.stb.STBIOCallbacks
import io.ktor.utils.io.bits.*
import kotlinx.cinterop.convert
import okio.*
import okio.Path.Companion.toPath
import org.sadgames.engine.IO
import platform.posix.*

/**
 * Created by Slava Mamchur on 27.02.2023.
 */

fun readTextFromFile(path: String) = IO.source(path.toPath()).buffer().use { it.readUtf8() }

class STBIOFile(path: String): STBIOCallbacks, Closeable {
    val file = fopen(path, "rb")!!

    fun reset() = fseek(file, 0L, SEEK_SET)

    override val eof get() = feof(file) != 0
    override fun read(data: Memory) = fread(data.pointer, 1, data.size.convert(), file).toInt()
    override fun skip(n: Int) { fseek(file, n.convert(), SEEK_CUR) }
    override fun close() { fclose(file) }
}