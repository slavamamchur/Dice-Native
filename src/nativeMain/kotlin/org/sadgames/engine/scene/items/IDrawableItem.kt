package org.sadgames.engine.scene.items

import org.sadgames.engine.utils.Matrix4f
import org.sadgames.engine.utils.Vector3f
import org.sadgames.engine.utils.Vector4f

/**
 * Created by Slava Mamchur on 03.03.2023.
 */

interface IDrawableItem {
    val id: UInt
    val transform: Matrix4f

    val castShadow: Boolean; get() = true
    val reflected: Boolean; get() = true
    val drawInRaysBuffer: Boolean; get() = true

    var position: Vector3f
    //var place: Vector2f //todo: moveto -> GameItem
    var rotationX: Float
    var rotationY: Float
    var rotationZ: Float
    var scaleFactor: Float

    //var effects: Int //todo: moveto -> GL2DBox

    fun updateTransform() {
        transform.reAssign(Matrix4f.IDENTITY)
        transform *= Vector4f(rotationX, rotationY, rotationZ, 1f)
        transform *= position
        transform *= scaleFactor
    }

    //fun animationStart() //todo: moveto -> Animated3DObject
    //fun animationStop() //todo: moveto -> Animated3DObject
    //fun setMaterialProperties(material: MaterialPropertiesObject?) //todo: moveto -> 3DObject
    fun loadObject()
    fun loadFromObject(src: IDrawableItem)
    fun bindObject()
    fun render()
    fun clearData()
}