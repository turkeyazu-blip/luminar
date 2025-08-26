package ru.luminar.ui.comp.api;

import com.mojang.blaze3d.matrix.MatrixStack;
import ru.luminar.feature.functions.Function;
import ru.luminar.feature.functions.settings.Setting;
import ru.luminar.ui.GuiScreen;

public abstract class Component implements IComponent {
   public float x;
   public float y;
   public float width;
   public float height;
   public Function function;
   public GuiScreen parent;
   public Setting<?> setting;

   public boolean isHovered(int mouseX, int mouseY, float width, float height) {
      return (float)mouseX >= this.x && (float)mouseY >= this.y && (float)mouseX < this.x + width && (float)mouseY < this.y + height;
   }

   public boolean isHovered(int mouseX, int mouseY, float x, float y, float width, float height) {
      return (float)mouseX >= x && (float)mouseY >= y && (float)mouseX < x + width && (float)mouseY < y + height;
   }

   public boolean isHovered(int mouseX, int mouseY) {
      return (float)mouseX > this.x && (float)mouseX < this.x + this.width && (float)mouseY > this.y && (float)mouseY < this.y + this.height;
   }

   public boolean isHovered(int mouseX, int mouseY, float height) {
      return (float)mouseX > this.x && (float)mouseX < this.x + this.width && (float)mouseY > this.y && (float)mouseY < this.y + height;
   }

   public void setPosition(float x, float y, float width, float height) {
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
   }

   public abstract void draw(MatrixStack var1, int var2, int var3);

   public abstract void mouseClicked(int var1, int var2, int var3);

   public abstract void mouseReleased(int var1, int var2, int var3);

   public abstract void keyTyped(int var1, int var2, int var3);

   public abstract void charTyped(char var1, int var2);
}
