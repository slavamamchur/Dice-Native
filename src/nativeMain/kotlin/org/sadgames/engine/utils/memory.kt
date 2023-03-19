package org.sadgames.engine.utils

import io.ktor.utils.io.bits.*
import io.ktor.utils.io.core.internal.*
import kotlinx.cinterop.*

/**
 * Created by Slava Mamchur on 28.02.2023.
 */

inline val IntArray.ptr; get() = this.refTo(0).getPointer(MemScope())
@OptIn(ExperimentalUnsignedTypes::class)
inline val UIntArray.ptr; get() = this.refTo(0).getPointer(MemScope())
inline val ByteArray.ptr; get() = this.refTo(0).getPointer(MemScope())

@OptIn(DangerousInternalIoApi::class)
fun Memory.clone(): Memory {
    val len = this.size
    val dst = Memory(nativeHeap.allocArray(len), len)

    this.copyTo(dst, 0, len, 0)

    return dst
}