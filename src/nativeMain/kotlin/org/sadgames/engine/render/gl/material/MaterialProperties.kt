package org.sadgames.engine.render.gl.material

data class MaterialProperties(
    var ambientRate: Float = 0.4f,
    var diffuseRate: Float = 1.0f,
    var specularRate: Float = 0.9f,
    var diffuseMapName: String? = null,
    var normalMapName: String? = null,
    var dUDVMapName: String? = null,
    var specularMapName: String? = null) {
}
