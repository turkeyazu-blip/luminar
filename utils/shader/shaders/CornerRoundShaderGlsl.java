package ru.luminar.utils.shader.shaders;

import ru.luminar.utils.shader.IShader;

public class CornerRoundShaderGlsl implements IShader {
   public String glsl() {
      return "#version 120\n\nuniform vec2 location;\nuniform vec2 rectSize;\nuniform vec4 color;\nuniform float radius;\nuniform bool blur;\n\nfloat roundSDF(vec2 p, vec2 b, float r) {\n    vec2 q = abs(p) - b;\n    return length(max(q, 0.0)) - r;\n}\n\nvoid main() {\n    vec2 st = gl_TexCoord[0].st;n    vec2 rectHalf = rectSize * 0.5;\n\n    float dist = roundSDF(st * rectSize - rectHalf, rectHalf - vec2(radius), radius);\n    float smoothedAlpha = (1.0 - smoothstep(0.0, 1.0, dist)) * color.a;\n\n    gl_FragColor = vec4(color.rgb, smoothedAlpha);\n}\n";
   }
}
