package org.sadgames.engine.utils

import kotlinx.cinterop.MemScope
import kotlinx.cinterop.objcPtr
import kotlinx.cinterop.refTo

/**
 * Created by Slava Mamchur on 28.02.2023.
 */

inline val IntArray.ptr; get() = this.refTo(0).getPointer(MemScope())
@OptIn(ExperimentalUnsignedTypes::class)
inline val UIntArray.ptr; get() = this.refTo(0).getPointer(MemScope())
inline val ByteArray.ptr; get() = this.refTo(0).getPointer(MemScope())