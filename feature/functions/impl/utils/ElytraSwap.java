package ru.luminar.feature.functions.impl.utils;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import ru.luminar.Luminar;
import ru.luminar.events.impl.player.EventUpdate;
import ru.luminar.events.system.Subscribe;
import ru.luminar.feature.functions.Category;
import ru.luminar.feature.functions.Function;
import ru.luminar.feature.functions.impl.render.Island;
import ru.luminar.feature.functions.settings.Setting;
import ru.luminar.feature.functions.settings.impl.BindSetting;
import ru.luminar.feature.functions.settings.impl.BooleanSetting;
import ru.luminar.utils.client.IMinecraft;
import ru.luminar.utils.math.TimerUtil;
import ru.luminar.utils.player.InventoryUtil;

public class ElytraSwap extends Function {
   public BindSetting bind = new BindSetting("Бинд свапа", -1);
   public BooleanSetting moreDelay = new BooleanSetting("Увеличеный кд", false);
   TimerUtil timer = new TimerUtil();
   TimerUtil timer2 = new TimerUtil();
   boolean swap;
   boolean opened;

   public ElytraSwap() {
      super("ElytraSwap", Category.Utils);
      this.addSettings(new Setting[]{this.bind, this.moreDelay});
   }

   public void onKey(int key) {
      if (key == (Integer)this.bind.get() && this.timer.passed(250L) && !this.swap) {
         this.swap = true;
         this.timer.reset();
         this.timer2.reset();
      }

   }

   @Subscribe
   public void onUpdate(EventUpdate e) {
      if (this.isPendingRemoval()) {
         if (Luminar.instance.functions.island.isEnabled()) {
            Island.islandManager.add("Эта функция недоступна на этом сервере!", 3);
         } else {
            IMinecraft.print2("Эта функция недоступна на этом сервере!");
         }

         this.toggle();
      } else {
         int slot = this.getItemSlot(Items.field_185160_cR);
         int slotCh = this.getChestPlateSlot();
         if (this.swap) {
            if (mc.field_71439_g.func_184582_a(EquipmentSlotType.CHEST).func_77973_b() != Items.field_185160_cR && slot != -1) {
               if (!this.opened) {
                  mc.func_147108_a(new InventoryScreen(mc.field_71439_g));
                  this.opened = true;
               }

               if (this.timer2.passed((Boolean)this.moreDelay.get() ? 250L : 200L)) {
                  InventoryUtil.moveItem(slot, 6);
                  mc.func_147108_a((Screen)null);
                  this.swap = false;
                  this.opened = false;
               }
            } else if (slotCh != -1) {
               if (!this.opened) {
                  mc.func_147108_a(new InventoryScreen(mc.field_71439_g));
                  this.opened = true;
               }

               if (this.timer2.passed((Boolean)this.moreDelay.get() ? 250L : 200L)) {
                  InventoryUtil.moveItem(slotCh, 6);
                  mc.func_147108_a((Screen)null);
                  this.swap = false;
                  this.opened = false;
               }
            } else {
               this.swap = false;
            }

         }
      }
   }

   private int getItemSlot(Item input) {
      int slot = -1;

      for(int i = 0; i < 36; ++i) {
         ItemStack s = mc.field_71439_g.field_71071_by.func_70301_a(i);
         if (s.func_77973_b() == input) {
            slot = i;
            break;
         }
      }

      if (slot < 9 && slot != -1) {
         slot += 36;
      }

      return slot;
   }

   private int getChestPlateSlot() {
      Item[] items = new Item[]{Items.field_234764_lt_, Items.field_151163_ad};
      Item[] var2 = items;
      int var3 = items.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Item item = var2[var4];

         for(int i = 0; i < 36; ++i) {
            Item stack = mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b();
            if (stack == item) {
               if (i < 9) {
                  i += 36;
               }

               return i;
            }
         }
      }

      return -1;
   }

   public void onEnable() {
      super.onEnable();
   }
}
