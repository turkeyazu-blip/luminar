package ru.luminar.utils;

import java.util.Objects;

public class SlotPos {
   final int x;
   final int y;

   public SlotPos(int x, int y) {
      this.x = x;
      this.y = y;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         SlotPos slotPos = (SlotPos)o;
         return this.x == slotPos.x && this.y == slotPos.y;
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.x, this.y});
   }
}
