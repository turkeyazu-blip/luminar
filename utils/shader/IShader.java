package ru.luminar.utils.shader;

public interface IShader {
   String glsl();

   default String getName() {
      return "SHADERNONAME";
   }
}
