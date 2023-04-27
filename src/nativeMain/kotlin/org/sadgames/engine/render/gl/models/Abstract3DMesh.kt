package org.sadgames.engine.render.gl.models

import org.sadgames.engine.render.gl.material.MaterialProperties
import org.sadgames.engine.render.gl.material.shaders.AbstractRenderer
import org.sadgames.engine.render.gl.material.shaders.params.IDX
import org.sadgames.engine.render.gl.material.shaders.params.V2D
import org.sadgames.engine.render.gl.material.shaders.params.V3D
import org.sadgames.engine.render.gl.material.shaders.params.VBOData
import org.sadgames.engine.render.models.RawModel

abstract class Abstract3DMesh(program: AbstractRenderer): AbstractGlMesh(program) { //todo: use animation and physics as interfaces
    protected abstract val model: RawModel
              abstract val material: MaterialProperties

    override fun createVertexesVBO() {
        vertexesVBO = VBOData(V3D).also { it.put(model.vertexes) }
    }

    override fun createTexelsVBO() {
        if (model.uvs != null) texelsVBO = VBOData(V2D).also { it.put(model.uvs!!) }
    }

    override fun createNormalsVBO() {
        if (model.normals != null) normalsVBO = VBOData(V3D).also { it.put(model.normals!!) }
    }

    override fun createFacesIBO() {
        if (model.indices != null) facesIBO = VBOData(IDX).also { it.put(model.indices!!) }
    }

    override val facesCount: Int; get() = model.indices?.size ?: 0
}