package ru.luminar.feature.functions.impl.utils;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
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
import ru.luminar.feature.functions.settings.impl.ModeSetting;
import ru.luminar.feature.functions.settings.impl.SliderSetting;
import ru.luminar.utils.client.IMinecraft;
import ru.luminar.utils.math.TimerUtil;
import ru.luminar.utils.player.InventoryUtil;

public class AutoSwap extends Function {
   public ModeSetting from = new ModeSetting("Свап с", "Тотем", new String[]{"Тотем", "Сфера"});
   public ModeSetting to = new ModeSetting("Свап на", "Тотем", new String[]{"Тотем", "Сфера"});
   public BindSetting swapKey = new BindSetting("Кнопка свапа", -1);
   public SliderSetting delay = new SliderSetting("Задержка", 150.0F, 100.0F, 250.0F, 25.0F);
   boolean swap;
   boolean opened;
   TimerUtil timer = new TimerUtil();
   TimerUtil timer2 = new TimerUtil();

   public AutoSwap() {
      super("AutoSwap", Category.Utils);
      this.addSettings(new Setting[]{this.from, this.to, this.swapKey, this.delay});
   }

   public void onKey(int key) {
      if ((Integer)this.swapKey.get() == key && this.timer.passed(250L)) {
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
         ItemStack offhandItemStack = mc.field_71439_g.func_184592_cb();
         Item currentItem = offhandItemStack.func_77973_b();
         boolean isHoldingSwapItem = currentItem == this.getSwapItem();
         boolean isHoldingSelectedItem = currentItem == this.getSelectedItem();
         int selectedItemSlot = this.getBestSlotForItem(this.getSelectedItem());
         int swapItemSlot = this.getBestSlotForItem(this.getSwapItem());
         boolean isSameItem = this.getSwapItem() == this.getSelectedItem();
         KeyBinding[] pressedKeys = new KeyBinding[]{mc.field_71474_y.field_74351_w, mc.field_71474_y.field_74368_y, mc.field_71474_y.field_74370_x, mc.field_71474_y.field_74366_z, mc.field_71474_y.field_74314_A, mc.field_71474_y.field_151444_V};
         if (!this.opened) {
            this.updateKeyBindingState(pressedKeys);
         }

         if (this.swap) {
            KeyBinding[] var10;
            int var11;
            int var12;
            KeyBinding keyBinding;
            if (isSameItem) {
               if (selectedItemSlot >= 0) {
                  if (!isHoldingSelectedItem || isSameItem && isHoldingSwapItem) {
                     if (!this.opened) {
                        if (selectedItemSlot <= 35) {
                           mc.func_147108_a(new InventoryScreen(mc.field_71439_g));
                        } else {
                           var10 = pressedKeys;
                           var11 = pressedKeys.length;

                           for(var12 = 0; var12 < var11; ++var12) {
                              keyBinding = var10[var12];
                              keyBinding.func_225593_a_(false);
                           }
                        }

                        this.opened = true;
                     }

                     if (this.timer2.passed(((Float)this.delay.get()).longValue())) {
                        InventoryUtil.moveItem(selectedItemSlot, 45);
                        if (selectedItemSlot <= 35) {
                           mc.func_147108_a((Screen)null);
                        }

                        this.swap = false;
                        this.opened = false;
                     }
                  }
               } else if (swapItemSlot >= 0) {
                  if (!isHoldingSwapItem || isSameItem && isHoldingSelectedItem) {
                     if (!this.opened) {
                        if (swapItemSlot <= 35) {
                           mc.func_147108_a(new InventoryScreen(mc.field_71439_g));
                        } else {
                           var10 = pressedKeys;
                           var11 = pressedKeys.length;

                           for(var12 = 0; var12 < var11; ++var12) {
                              keyBinding = var10[var12];
                              keyBinding.func_225593_a_(false);
                           }
                        }

                        this.opened = true;
                     }

                     if (this.timer2.passed(((Float)this.delay.get()).longValue())) {
                        InventoryUtil.moveItem(swapItemSlot, 45);
                        if (swapItemSlot <= 35) {
                           mc.func_147108_a((Screen)null);
                        }

                        this.swap = false;
                        this.opened = false;
                     }
                  }
               } else {
                  this.swap = false;
               }
            } else {
               if (selectedItemSlot >= 0 && (!isHoldingSelectedItem || isSameItem && isHoldingSwapItem)) {
                  if (!this.opened) {
                     if (selectedItemSlot <= 35) {
                        mc.func_147108_a(new InventoryScreen(mc.field_71439_g));
                     } else {
                        var10 = pressedKeys;
                        var11 = pressedKeys.length;

                        for(var12 = 0; var12 < var11; ++var12) {
                           keyBinding = var10[var12];
                           keyBinding.func_225593_a_(false);
                        }
                     }

                     this.opened = true;
                  }

                  if (this.timer2.passed(((Float)this.delay.get()).longValue())) {
                     InventoryUtil.moveItem(selectedItemSlot, 45);
                     if (selectedItemSlot <= 35) {
                        mc.func_147108_a((Screen)null);
                     }

                     this.swap = false;
                     this.opened = false;
                  }
               }

               if (swapItemSlot >= 0 && (!isHoldingSwapItem || isSameItem && isHoldingSelectedItem)) {
                  if (!this.opened) {
                     if (swapItemSlot <= 35) {
                        mc.func_147108_a(new InventoryScreen(mc.field_71439_g));
                     } else {
                        var10 = pressedKeys;
                        var11 = pressedKeys.length;

                        for(var12 = 0; var12 < var11; ++var12) {
                           keyBinding = var10[var12];
                           keyBinding.func_225593_a_(false);
                        }
                     }

                     this.opened = true;
                  }

                  if (this.timer2.passed(((Float)this.delay.get()).longValue())) {
                     InventoryUtil.moveItem(swapItemSlot, 45);
                     if (swapItemSlot <= 35) {
                        mc.func_147108_a((Screen)null);
                     }

                     this.swap = false;
                     this.opened = false;
                  }
               }
            }

         }
      }
   }

   private void updateKeyBindingState(KeyBinding[] keyBindings) {
      KeyBinding[] var2 = keyBindings;
      int var3 = keyBindings.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         KeyBinding keyBinding = var2[var4];
         boolean isKeyPressed = InputMappings.func_216506_a(mc.func_228018_at_().func_198092_i(), keyBinding.getKey().func_197937_c());
         keyBinding.func_225593_a_(isKeyPressed);
      }

   }

   private Item getSwapItem() {
      return this.getItemByType((String)this.to.get());
   }

   private Item getSelectedItem() {
      return this.getItemByType((String)this.from.get());
   }

   private Item getItemByType(String itemType) {
      byte var3 = -1;
      switch(itemType.hashCode()) {
      case 1009763266:
         if (itemType.equals("Сфера")) {
            var3 = 1;
         }
         break;
      case 1010520205:
         if (itemType.equals("Тотем")) {
            var3 = 0;
         }
      }

      Item var10000;
      switch(var3) {
      case 0:
         var10000 = Items.field_190929_cY;
         break;
      case 1:
         var10000 = Items.field_196184_dx;
         break;
      default:
         var10000 = Items.field_190931_a;
      }

      return var10000;
   }

   private int getBestSlotForItem(Item item) {
      int i;
      for(i = 0; i < 9; ++i) {
         if (mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b() == item) {
            return i + 36;
         }
      }

      for(i = 9; i < 36; ++i) {
         ItemStack stack = mc.field_71439_g.field_71071_by.func_70301_a(i);
         if (stack.func_77973_b() == item && stack.func_77948_v()) {
            return i;
         }
      }

      for(i = 9; i < 36; ++i) {
         if (mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b() == item) {
            return i;
         }
      }

      return -1;
   }

   public void onEnable() {
      super.onEnable();
   }
}
