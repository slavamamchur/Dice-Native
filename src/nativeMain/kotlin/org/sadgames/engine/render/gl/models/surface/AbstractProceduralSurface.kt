package org.sadgames.engine.render.gl.models.surface

import org.sadgames.GLObjectType
import org.sadgames.engine.render.gl.material.MaterialProperties
import org.sadgames.engine.render.gl.material.shaders.AbstractRenderer
import org.sadgames.engine.render.gl.models.Abstract3DMesh

abstract class AbstractProceduralSurface(
    type: GLObjectType,
    material: MaterialProperties,
    val landSize: Float,
    program: AbstractRenderer): Abstract3DMesh(program) {
}