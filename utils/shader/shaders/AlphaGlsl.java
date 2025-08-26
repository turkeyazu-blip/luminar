package ru.luminar.utils.shader.shaders;

import ru.luminar.utils.shader.IShader;

public class AlphaGlsl implements IShader {
   public String glsl() {
      return "#version 120\n\nuniform sampler2D texture;\nuniform float state;\n\nvoid main() {\n    vec3 sum = texture2D(texture, gl_TexCoord[0].st).rgb;\n\n\n    gl_FragColor = vec4(sum, state);\n}\n";
   }
}
