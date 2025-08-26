package ru.luminar.utils.shader.shaders;

import ru.luminar.utils.shader.IShader;

public class VertexGlsl implements IShader {
   public String glsl() {
      return "#version 120\n void main() {\n     gl_TexCoord[0] = gl_MultiTexCoord0;\n     gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;\n }\n";
   }
}
