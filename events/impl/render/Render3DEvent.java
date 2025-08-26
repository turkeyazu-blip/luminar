package ru.luminar.events.impl.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import ru.luminar.events.Event;

public class Render3DEvent extends Event {
   private MatrixStack stack;
   private float partialTicks;

   public Render3DEvent(MatrixStack stack, float partialTicks) {
      this.stack = stack;
      this.partialTicks = partialTicks;
   }

   public void setStack(MatrixStack matrixStack) {
      this.stack = matrixStack;
   }

   public void setPartialTicks(float pticks) {
      this.partialTicks = pticks;
   }

   public float getPartialTicks() {
      return this.partialTicks;
   }

   public MatrixStack getMatrixStack() {
      return this.stack;
   }
}
