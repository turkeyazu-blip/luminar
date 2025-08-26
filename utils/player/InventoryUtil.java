package ru.luminar.utils.player;

import java.util.List;
import java.util.stream.IntStream;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.Item;
import ru.luminar.utils.client.IMinecraft;

public class InventoryUtil implements IMinecraft {
   public static int findEmptySlot(boolean inHotBar) {
      int start = inHotBar ? 0 : 9;
      int end = inHotBar ? 9 : 36;

      for(int i = start; i < end; ++i) {
         if (mc.field_71439_g.field_71071_by.func_70301_a(i).func_190926_b()) {
            return i;
         }
      }

      return -1;
   }

   public static void moveItem(int from, int to, boolean air) {
      if (from != to) {
         pickupItem(from, 0);
         pickupItem(to, 0);
         if (air) {
            pickupItem(from, 0);
         }

      }
   }

   public static void moveItem(int from, int to) {
      if (from != to) {
         pickupItem(from, 0);
         pickupItem(to, 0);
         pickupItem(from, 0);
      }
   }

   public static void pickupItem(int slot, int button) {
      mc.field_71442_b.func_187098_a(0, slot, button, ClickType.PICKUP, mc.field_71439_g);
   }

   public static Slot getInventorySlot(Item item) {
      return (Slot)mc.field_71439_g.field_71070_bA.field_75151_b.stream().filter((s) -> {
         return s.func_75211_c().func_77973_b().equals(item) && s.field_75222_d >= mc.field_71439_g.field_71070_bA.field_75151_b.size() - 36;
      }).findFirst().orElse((Object)null);
   }

   public static Slot getInventorySlot(List<Item> item) {
      return (Slot)mc.field_71439_g.field_71070_bA.field_75151_b.stream().filter((s) -> {
         return item.contains(s.func_75211_c().func_77973_b()) && s.field_75222_d >= mc.field_71439_g.field_71070_bA.field_75151_b.size() - 36;
      }).findFirst().orElse((Object)null);
   }

   public static int getInventoryCount(Item item) {
      return IntStream.range(0, 45).filter((i) -> {
         return mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b().equals(item);
      }).map((i) -> {
         return mc.field_71439_g.field_71071_by.func_70301_a(i).func_190916_E();
      }).sum();
   }

   public static boolean doesHotbarHaveItem(Item item) {
      for(int i = 0; i < 9; ++i) {
         mc.field_71439_g.field_71071_by.func_70301_a(i);
         if (mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b() == item) {
            return true;
         }
      }

      return false;
   }

   public int getSlotInInventory(Item item) {
      int finalSlot = -1;

      for(int i = 0; i < 36; ++i) {
         if (mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b() == item) {
            finalSlot = i;
         }
      }

      return finalSlot;
   }

   public static int getSlotInHotbar(Item item) {
      int firstSlot = 0;
      int lastSlot = 9;
      int finalSlot = -1;

      for(int i = firstSlot; i < lastSlot; ++i) {
         if (mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b() == item) {
            finalSlot = i;
         }
      }

      return finalSlot;
   }
}
