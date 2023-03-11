layout (triangles) in;
layout (triangle_strip, max_vertices = 3) out;

void main(void){
    for(int i=0; i<3; i++) {
        gl_Layer = 0;
        gl_Position = gl_in[i].gl_Position;
        EmitVertex();
    }

    EndPrimitive();
}