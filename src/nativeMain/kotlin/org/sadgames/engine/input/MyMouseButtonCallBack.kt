package org.sadgames.engine.input

import com.kgl.core.Flag
import com.kgl.glfw.*

/**
 * Created by Slava Mamchur on 19.02.2023.
 */

class MyMouseButtonCallBack(private val listener: IGestureDetectorListener?): MouseButtonCallback {
    override fun invoke(window: Window, button: MouseButton, action: Action, mods: Flag<Mod>) {
        if (button ==  MouseButton.LEFT && action == Action.Release)
            listener?.onTap()
    }
}
