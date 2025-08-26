package ru.luminar.events.impl.player;

import ru.luminar.events.Event;

public class EventMovement extends Event {
   public double x;
   public double y;
   public double z;
   public double xPrev;
   public double yPrev;
   public double zPrev;

   public EventMovement(double x, double y, double z, double xPrev, double yPrev, double zPrev) {
      this.x = x;
      this.y = y;
      this.z = z;
      this.xPrev = xPrev;
      this.yPrev = yPrev;
      this.zPrev = zPrev;
   }

   public void setX(double x) {
      this.x = x;
   }

   public void setY(double y) {
      this.y = y;
   }

   public void setZ(double z) {
      this.z = z;
   }

   public void setXPrev(double x) {
      this.xPrev = x;
   }

   public void setYPrev(double y) {
      this.yPrev = y;
   }

   public void setZPrev(double z) {
      this.zPrev = z;
   }
}
