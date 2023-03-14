package org.sadgames.engine.utils

/**
 * Created by Slava Mamchur on 14.03.2023.
 */

val String.isNumber get() = this.toIntOrNull() != null