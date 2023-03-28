package org.sadgames.engine.render.gl.models

import org.sadgames.engine.render.gl.material.shaders.VBOShaderProgram

/**
 * Created by Slava Mamchur on 28.03.2023.
 */

abstract class Abstract3DMesh(program: VBOShaderProgram): AbstractGlMesh(program) {
    //todo: mesh, material -> animatedObject as child
}