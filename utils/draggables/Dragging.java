package ru.luminar.utils.draggables;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import ru.luminar.Luminar;
import ru.luminar.feature.functions.Function;
import ru.luminar.utils.math.MathUtil;

public class Dragging {
   @Expose
   @SerializedName("x")
   private float xPos;
   @Expose
   @SerializedName("y")
   private float yPos;
   public float initialXVal;
   public float initialYVal;
   private float startX;
   private float startY;
   private boolean dragging;
   private float width;
   private float height;
   @Expose
   @SerializedName("name")
   private String name;
   private final Function module;

   public Function getModule() {
      return this.module;
   }

   public String getName() {
      return this.name;
   }

   public float getWidth() {
      return this.width;
   }

   public void setWidth(float w) {
      this.width = w;
   }

   public float getHeight() {
      return this.height;
   }

   public void setHeight(float h) {
      this.height = h;
   }

   public Dragging(Function module, String name, float initialXVal, float initialYVal) {
      this.module = module;
      this.name = name;
      this.xPos = initialXVal;
      this.yPos = initialYVal;
      this.initialXVal = initialXVal;
      this.initialYVal = initialYVal;
   }

   public float getX() {
      return this.xPos;
   }

   public void setX(float x) {
      this.xPos = x;
   }

   public float getY() {
      return this.yPos;
   }

   public void setY(float y) {
      this.yPos = y;
   }

   public final void onDraw(int mouseX, int mouseY, MainWindow res) {
      MathUtil.getMouseScale(mouseX, mouseY);
      float guiScale = (float)(Minecraft.func_71410_x().func_228018_at_().func_198100_s() / 2.0D);
      if (this.dragging) {
         this.xPos = (float)mouseX / guiScale - this.startX;
         this.yPos = (float)mouseY / guiScale - this.startY;
         if (this.xPos + this.width > (float)res.func_198107_o()) {
            this.xPos = (float)res.func_198107_o() - this.width;
         }

         if (this.yPos + this.height > (float)res.func_198087_p()) {
            this.yPos = (float)res.func_198087_p() - this.height;
         }

         if (this.xPos < 0.0F) {
            this.xPos = 0.0F;
         }

         if (this.yPos < 0.0F) {
            this.yPos = 0.0F;
         }
      }

   }

   public void onReset() {
      this.xPos = this.initialXVal;
      this.yPos = this.initialYVal;
   }

   public final boolean onClick(double mouseX, double mouseY, int button) {
      MathUtil.getMouseScale(mouseX, mouseY);
      if (button == 0 && MathUtil.isHovered((float)mouseX, (float)mouseY, this.xPos, this.yPos, this.width, this.height)) {
         this.dragging = true;
         double guiScale = Minecraft.func_71410_x().func_228018_at_().func_198100_s() / 2.0D;
         this.startX = (float)(mouseX / guiScale) - this.xPos;
         this.startY = (float)(mouseY / guiScale) - this.yPos;
         return true;
      } else {
         if (button == 1 && MathUtil.isHovered((float)mouseX, (float)mouseY, this.xPos, this.yPos, this.width, this.height) && this.name.equals("Armor")) {
            Luminar.instance.functions.anInterface.isArmorRotated = !Luminar.instance.functions.anInterface.isArmorRotated;
         }

         return false;
      }
   }

   public final void onRelease(int button, MainWindow res) {
      if (button == 0) {
         this.dragging = false;
         if (this.xPos + this.width > (float)res.func_198107_o()) {
            this.xPos = (float)res.func_198107_o() - this.width;
         }

         if (this.yPos + this.height > (float)res.func_198087_p()) {
            this.yPos = (float)res.func_198087_p() - this.height;
         }

         if (this.xPos < 0.0F) {
            this.xPos = 0.0F;
         }

         if (this.yPos < 0.0F) {
            this.yPos = 0.0F;
         }
      }

   }
}
