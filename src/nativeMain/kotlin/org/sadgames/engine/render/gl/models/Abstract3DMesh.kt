package org.sadgames.engine.render.gl.models

import org.sadgames.engine.render.gl.material.shaders.VBOShaderProgram
import org.sadgames.engine.render.gl.material.shaders.params.IDX
import org.sadgames.engine.render.gl.material.shaders.params.V2D
import org.sadgames.engine.render.gl.material.shaders.params.V3D
import org.sadgames.engine.render.gl.material.shaders.params.VBOData

abstract class Abstract3DMesh(program: VBOShaderProgram): AbstractGlMesh(program) { //todo: use animation and physics as interfaces
    protected abstract val model: Raw3DModel
    //todo: Material property

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

    override fun bind() {
        super.bind()
        //todo: ... shader params -> material (uniform buffer maybe???)
    }
}