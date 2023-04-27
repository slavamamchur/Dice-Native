package org.sadgames.engine.render.models

data class RawModel(
    var vertexes: FloatArray,
    var uvs: FloatArray?,
    var normals: FloatArray?,
    var indices: ShortArray?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RawModel) return false

        return vertexes.contentEquals(other.vertexes)
    }

    override fun hashCode() = vertexes.contentHashCode()
}
