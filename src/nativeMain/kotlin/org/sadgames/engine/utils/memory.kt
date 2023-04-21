package org.sadgames.engine.utils

import io.ktor.utils.io.bits.*
import io.ktor.utils.io.core.*
import io.ktor.utils.io.core.internal.*
import kotlinx.cinterop.*

/**
 * Created by Slava Mamchur on 28.02.2023.
 */

inline val IntArray.ptr; get() = this.refTo(0).getPointer(MemScope())
@OptIn(ExperimentalUnsignedTypes::class)
inline val UIntArray.ptr; get() = this.refTo(0).getPointer(MemScope())
inline val ByteArray.ptr; get() = this.refTo(0).getPointer(MemScope())
inline val FloatArray.ptr; get() = this.refTo(0).getPointer(MemScope())
inline val FloatArray.memSize; get() = (this.size * Float.SIZE_BYTES).toLong()
inline val ShortArray.memSize; get() = (this.size * Short.SIZE_BYTES).toLong()

@OptIn(DangerousInternalIoApi::class)
inline fun Memory.clone() = Memory(nativeHeap.allocArray(this.size), this.size).also {
    this.copyTo(it, 0, this.size, 0)
}

@OptIn(DangerousInternalIoApi::class)
inline fun allocateBuffer(size: Long): Buffer = Buffer(Memory(nativeHeap.allocArray(size), size))
@OptIn(DangerousInternalIoApi::class)
fun allocateData(data: FloatArray) = allocateBuffer(data.memSize).also { it.writeFullyLittleEndian(data) }.memory