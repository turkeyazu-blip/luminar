package ru.luminar.events;

public class Event {
   private boolean isCancel;

   public void cancel() {
      this.isCancel = true;
   }

   public void open() {
      this.isCancel = false;
   }

   public boolean isCancel() {
      return this.isCancel;
   }
}
