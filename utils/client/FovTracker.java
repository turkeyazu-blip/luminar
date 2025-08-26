package ru.luminar.utils.client;

public class FovTracker {
   private static double lastFov;

   public static void setLastFov(double fov) {
      lastFov = fov;
   }

   public static double getLastFov() {
      return lastFov;
   }
}
