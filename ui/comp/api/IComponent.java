package ru.luminar.ui.comp.api;

import com.mojang.blaze3d.matrix.MatrixStack;

public interface IComponent {
   void draw(MatrixStack var1, int var2, int var3);

   void mouseClicked(int var1, int var2, int var3);

   void mouseReleased(int var1, int var2, int var3);

   void keyTyped(int var1, int var2, int var3);

   void charTyped(char var1, int var2);
}
