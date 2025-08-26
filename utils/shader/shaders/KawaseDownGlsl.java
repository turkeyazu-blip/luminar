package ru.luminar.utils.shader.shaders;

import ru.luminar.utils.shader.IShader;

public class KawaseDownGlsl implements IShader {
   public String glsl() {
      return "#version 120\n\nuniform sampler2D image;\nuniform float offset;\nuniform vec2 resolution;\n\nvoid main()\n{\n    vec2 uv = gl_TexCoord[0].xy * 2.0;\n    vec2 halfpixel = resolution * 2.0;\n    vec3 sum = texture2D(image, uv).rgb * 4.0;\n    sum += texture2D(image, uv - halfpixel.xy * offset).rgb;\n    sum += texture2D(image, uv + halfpixel.xy * offset).rgb;\n    sum += texture2D(image, uv + vec2(halfpixel.x, -halfpixel.y) * offset).rgb;\n    sum += texture2D(image, uv - vec2(halfpixel.x, -halfpixel.y) * offset).rgb;\n    gl_FragColor = vec4(sum / 8.0, 1);\n}";
   }

   public String getName() {
      return "downkawasi";
   }
}
