package ru.luminar.utils.math.vectors;

public class Vector2d {
   double x;
   double y;

   public Vector2d(double x, double y) {
      this.x = x;
      this.y = y;
   }

   public double getX() {
      return this.x;
   }

   public double getY() {
      return this.y;
   }

   public void setX(double x) {
      this.x = x;
   }

   public void setY(double y) {
      this.y = y;
   }
}
