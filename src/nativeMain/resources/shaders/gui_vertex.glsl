#extension GL_EXT_clip_cull_distance : enable

layout(location = 1) in vec2 a_Position;
//layout(location = 2) in vec2 a_Texture;

varying vec2 v_Texture;

void main()
{
	v_Texture = a_Position.xy * 0.5 + 0.5; //a_Texture;
	gl_Position = vec4(a_Position, 0.0, 1.0);
}
