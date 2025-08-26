package ru.luminar.utils.math;

public class TimerUtil {
   public long lastMS = System.currentTimeMillis();

   public void reset() {
      this.lastMS = System.currentTimeMillis();
   }

   public boolean passed(long time) {
      return System.currentTimeMillis() - this.lastMS > time;
   }

   public void setLastMS(long newValue) {
      this.lastMS = System.currentTimeMillis() + newValue;
   }

   public void setTime(long time) {
      this.lastMS = time;
   }

   public long getTime() {
      return System.currentTimeMillis() - this.lastMS;
   }

   public boolean hasTimeElapsed() {
      return this.lastMS < System.currentTimeMillis();
   }

   public boolean passed(long time, long timeUntil) {
      if (this.passed(time)) {
         if (this.passed(time + timeUntil)) {
            this.reset();
         }

         return true;
      } else {
         return false;
      }
   }
}
