layout (location = 0) out vec4 refractBuffer;

uniform sampler2D u_TextureUnit;
uniform sampler2DShadow uShadowTexture;

uniform float u_AmbientRate;

varying vec2 v_Texture;
varying float vdiffuse;
varying highp vec4 vShadowCoord;

const float nmapTiling = 4.0;

float shadowPCF(vec3 coord) { //todo: without filtering from underwater area
      const ivec2 offsets[4] = ivec2[4](ivec2(-1, 0), ivec2(0, -1), ivec2(1, 0), ivec2(0, 1));
      return dot(textureGatherOffsets(uShadowTexture, coord.xy, coord.z, offsets), vec4(0.25));
}

vec4 calcLightColor(float shadowRate) {
      vec3 lightColour = vec3(1.0) * (0.4 + 0.6 * vdiffuse);

      return vec4(lightColour * shadowRate, 1.0);
}

vec4 calcPhongLightingMolel(vec4 diffuseColor, float AmbientRate) {
      float shadowRate = (shadowPCF(vec3(vShadowCoord.xy, min(vShadowCoord.z, 1.0))) * (1.0 - AmbientRate)) + AmbientRate;
      return calcLightColor(shadowRate) * diffuseColor;
}

void main() {
      refractBuffer = calcPhongLightingMolel(texture2D(u_TextureUnit, v_Texture * nmapTiling ), 0.1);
}
