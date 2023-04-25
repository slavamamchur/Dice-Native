package org.sadgames.engine.render.gl.models

data class Raw3DModel(
    var vertexes: FloatArray,
    var uvs: FloatArray?,
    var normals: FloatArray?,
    var indices: ShortArray?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Raw3DModel) return false

        return vertexes.contentEquals(other.vertexes)
    }

    override fun hashCode() = vertexes.contentHashCode()
}
