package ru.luminar.events.impl.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import ru.luminar.events.Event;

public class Render2DEvent extends Event {
   private MatrixStack stack;
   private float partialTicks;
   Render2DEvent.Type type;

   public Render2DEvent(MatrixStack stack, float partialTicks) {
      this.stack = stack;
      this.partialTicks = partialTicks;
   }

   public Render2DEvent(MatrixStack stack, float partialTicks, Render2DEvent.Type type) {
      this.stack = stack;
      this.partialTicks = partialTicks;
      this.type = type;
   }

   public void setStack(MatrixStack matrixStack) {
      this.stack = matrixStack;
   }

   public void setPartialTicks(float pticks) {
      this.partialTicks = pticks;
   }

   public void setType(Render2DEvent.Type type) {
      this.type = type;
   }

   public float getPartialTicks() {
      return this.partialTicks;
   }

   public MatrixStack getMatrixStack() {
      return this.stack;
   }

   public Render2DEvent.Type getType() {
      return this.type;
   }

   public static enum Type {
      PRE,
      POST,
      HIGH;

      // $FF: synthetic method
      private static Render2DEvent.Type[] $values() {
         return new Render2DEvent.Type[]{PRE, POST, HIGH};
      }
   }
}
