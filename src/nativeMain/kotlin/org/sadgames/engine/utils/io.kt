package org.sadgames.engine.utils

import okio.Buffer
import okio.Path.Companion.toPath
import okio.use
import org.sadgames.engine.IO

/**
 * Created by Slava Mamchur on 27.02.2023.
 */

fun readTextFromFile(path: String) = IO.source(path.toPath()).use {
    val buffer = Buffer()
    it.read(buffer, IO.metadata(path.toPath()).size ?: 0)

    val result = buffer.readUtf8()
    buffer.close()

    result
}