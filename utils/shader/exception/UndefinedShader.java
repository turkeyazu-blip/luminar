package ru.luminar.utils.shader.exception;

public class UndefinedShader extends Throwable {
   private final String shader;

   public String getMessage() {
      return this.shader;
   }

   public UndefinedShader(String shader) {
      this.shader = shader;
   }
}
