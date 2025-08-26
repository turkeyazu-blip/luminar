package ru.luminar.feature.functions.impl.utils;

import java.util.Iterator;
import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PotionItem;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.potion.PotionUtils;
import ru.luminar.feature.functions.Category;
import ru.luminar.feature.functions.Function;
import ru.luminar.feature.functions.settings.Setting;
import ru.luminar.feature.functions.settings.impl.BindSetting;
import ru.luminar.utils.player.InventoryUtil;

public class FastSwap extends Function {
   public BindSetting trap = new BindSetting("Кнопка трапки", -1);
   public BindSetting plast = new BindSetting("Кнопка пласта", -1);
   public BindSetting disor = new BindSetting("Кнопка дезор", -1);
   public BindSetting pil = new BindSetting("Кнопка явной пыли", -1);
   public BindSetting chorus = new BindSetting("Кнопка хорус", -1);
   public BindSetting pearl = new BindSetting("Кнопка перка", -1);
   public BindSetting iscel = new BindSetting("Кнопка исцел", -1);

   public FastSwap() {
      super("FastSwap", Category.Utils);
      this.addSettings(new Setting[]{this.trap, this.plast, this.disor, this.pil, this.chorus, this.pearl, this.iscel});
   }

   public void onKey(int key) {
      if (mc.field_71439_g != null) {
         int potionSlot;
         if (key == (Integer)this.trap.get()) {
            potionSlot = InventoryUtil.getSlotInHotbar(Items.field_234760_kn_);
            if (potionSlot != -1) {
               mc.field_71439_g.field_71071_by.field_70461_c = potionSlot;
            } else {
               this.print("Трапка не найдена в хотбаре");
            }
         }

         if (key == (Integer)this.plast.get()) {
            potionSlot = InventoryUtil.getSlotInHotbar(Items.field_203180_bP);
            if (potionSlot != -1) {
               mc.field_71439_g.field_71071_by.field_70461_c = potionSlot;
            } else {
               this.print("Пласт не найден в хотбаре");
            }
         }

         if (key == (Integer)this.disor.get()) {
            potionSlot = InventoryUtil.getSlotInHotbar(Items.field_151061_bv);
            if (potionSlot != -1) {
               mc.field_71439_g.field_71071_by.field_70461_c = potionSlot;
            } else {
               this.print("Дезориентация не найдена в хотбаре");
            }
         }

         if (key == (Integer)this.pil.get()) {
            potionSlot = InventoryUtil.getSlotInHotbar(Items.field_151102_aT);
            if (potionSlot != -1) {
               mc.field_71439_g.field_71071_by.field_70461_c = potionSlot;
            } else {
               this.print("Явная пыль не найдена в хотбаре");
            }
         }

         if (key == (Integer)this.chorus.get()) {
            potionSlot = InventoryUtil.getSlotInHotbar(Items.field_185161_cS);
            if (potionSlot != -1) {
               mc.field_71439_g.field_71071_by.field_70461_c = potionSlot;
            } else {
               this.print("Хорус не найден в хотбаре");
            }
         }

         if (key == (Integer)this.pearl.get()) {
            potionSlot = InventoryUtil.getSlotInHotbar(Items.field_151079_bi);
            if (potionSlot != -1) {
               mc.field_71439_g.field_71071_by.field_70461_c = potionSlot;
            } else {
               this.print("Перка не найдена в хотбаре");
            }
         }

         if (key == (Integer)this.iscel.get()) {
            potionSlot = InventoryUtil.getSlotInHotbar(Items.field_151068_bn);
            if (potionSlot != -1) {
               ItemStack stack = mc.field_71439_g.field_71071_by.func_70301_a(potionSlot);
               if (stack.func_77973_b() instanceof PotionItem) {
                  List<EffectInstance> effects = PotionUtils.func_185190_b(stack);
                  boolean hasHealing = false;
                  Iterator var6 = effects.iterator();

                  while(var6.hasNext()) {
                     EffectInstance effect = (EffectInstance)var6.next();
                     if (effect.func_188419_a() == Effects.field_76432_h) {
                        mc.field_71439_g.field_71071_by.field_70461_c = potionSlot;
                        hasHealing = true;
                        break;
                     }
                  }

                  if (!hasHealing) {
                     List<EffectInstance> effects2 = PotionUtils.func_185185_a(stack.func_77978_p());
                     Iterator var10 = effects2.iterator();

                     while(var10.hasNext()) {
                        EffectInstance effect1 = (EffectInstance)var10.next();
                        if (effect1.func_188419_a() == Effects.field_76432_h) {
                           mc.field_71439_g.field_71071_by.field_70461_c = potionSlot;
                           hasHealing = true;
                        }
                     }
                  }

                  if (!hasHealing) {
                     this.print("Зелье в хотбаре не имеет исцеления");
                  }
               }
            } else {
               this.print("Исцелка не найдена в хотбаре");
            }
         }

      }
   }
}
