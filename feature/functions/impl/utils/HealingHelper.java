package ru.luminar.feature.functions.impl.utils;

import java.util.Iterator;
import java.util.List;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PotionItem;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.potion.PotionUtils;
import ru.luminar.feature.functions.Category;
import ru.luminar.feature.functions.Function;
import ru.luminar.feature.functions.settings.Setting;
import ru.luminar.feature.functions.settings.impl.BooleanSetting;
import ru.luminar.feature.functions.settings.impl.SliderSetting;

public class HealingHelper extends Function {
   private final BooleanSetting gapple = new BooleanSetting("Золотое Яблоко", true);
   private final BooleanSetting charka = new BooleanSetting("Чарка", true);
   private final BooleanSetting heal = new BooleanSetting("Исцел", true);
   private final BooleanSetting morkva = new BooleanSetting("Золотая морковь", true);
   SliderSetting gapplePorog = (new SliderSetting("Порог для гэппла", 14.0F, 2.0F, 20.0F, 0.5F)).setVisible(() -> {
      return (Boolean)this.gapple.get();
   });
   SliderSetting charkaPorog = (new SliderSetting("Порог для чарки", 12.0F, 2.0F, 20.0F, 0.5F)).setVisible(() -> {
      return (Boolean)this.charka.get();
   });
   SliderSetting healPorog = (new SliderSetting("Порог для хилки", 10.0F, 2.0F, 20.0F, 0.5F)).setVisible(() -> {
      return (Boolean)this.heal.get();
   });
   SliderSetting morkvaPorog = (new SliderSetting("Сытость для морковки", 18.0F, 2.0F, 20.0F, 0.5F)).setVisible(() -> {
      return (Boolean)this.morkva.get();
   });

   public HealingHelper() {
      super("HealingHelper", Category.Utils);
      this.addSettings(new Setting[]{this.charka, this.charkaPorog, this.gapple, this.gapplePorog, this.heal, this.healPorog, this.morkva, this.morkvaPorog});
   }

   public boolean shouldHightLight(ItemStack stack) {
      Item item = stack.func_77973_b();
      float hp = mc.field_71439_g.func_110143_aJ() + mc.field_71439_g.func_110139_bj();
      if (stack.func_77973_b() instanceof PotionItem && (Boolean)this.heal.get() && hp < (Float)this.healPorog.get()) {
         List<EffectInstance> effects = PotionUtils.func_185190_b(stack);
         boolean isHeal = false;
         Iterator var6 = effects.iterator();

         while(var6.hasNext()) {
            EffectInstance effect = (EffectInstance)var6.next();
            if (effect.func_188419_a() == Effects.field_76432_h) {
               isHeal = true;
               return true;
            }
         }

         if (!isHeal) {
            List<EffectInstance> effects2 = PotionUtils.func_185185_a(stack.func_77978_p());
            Iterator var10 = effects2.iterator();

            while(var10.hasNext()) {
               EffectInstance effect1 = (EffectInstance)var10.next();
               if (effect1.func_188419_a() == Effects.field_76432_h) {
                  isHeal = true;
                  return true;
               }
            }
         }
      }

      if ((Boolean)this.gapple.get() && item == Items.field_151153_ao && hp < (Float)this.gapplePorog.get()) {
         return true;
      } else if ((Boolean)this.charka.get() && item == Items.field_196100_at && hp < (Float)this.charkaPorog.get()) {
         return true;
      } else {
         return (Boolean)this.morkva.get() && item == Items.field_151150_bK && (float)mc.field_71439_g.func_71024_bL().func_75116_a() < (Float)this.morkvaPorog.get();
      }
   }
}
