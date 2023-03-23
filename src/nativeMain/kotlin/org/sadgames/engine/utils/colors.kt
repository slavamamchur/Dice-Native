package org.sadgames.engine.utils
    fun argb(alpha: Int, red: Int, green: Int, blue: Int) = alpha shl 24 or (red shl 16) or (green shl 8) or blue
    fun argb2libGDX(alpha: Int, red: Int, green: Int, blue: Int) = convert2libGDX(argb(alpha, red, green, blue))
    fun alpha(color: Int) = color ushr 24
    fun red(color: Int) = color shr 16 and 0xFF
    fun green(color: Int) = color shr 8 and 0xFF
    fun blue(color: Int) = color and 0xFF
    fun convert2libGDX(color: Int) = color shl 8 and -0x100 or (color shr 24 and 0xFF)
