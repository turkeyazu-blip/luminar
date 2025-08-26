package ru.luminar.mixins;

import java.util.Map;
import net.minecraft.item.Item;
import net.minecraft.util.CooldownTracker;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import ru.luminar.mixins.accessors.CooldownAccessor;
import ru.luminar.utils.ICooldownTrackerAccessor;

@Mixin({CooldownTracker.class})
public abstract class MixinCooldownTracker implements ICooldownTrackerAccessor {
   @Shadow
   private Map<Item, ?> field_185147_a;
   @Shadow
   private int field_185148_b;

   @Unique
   public float getCooldownSeconds(Item item, float partialTicks) {
      Object cooldownObj = this.field_185147_a.get(item);
      if (cooldownObj == null) {
         return 0.0F;
      } else {
         int startTime = ((CooldownAccessor)cooldownObj).getStartTime();
         int endTime = ((CooldownAccessor)cooldownObj).getEndTime();
         if (cooldownObj != null) {
            float f = 20.0F;
            float f1 = (float)endTime - ((float)this.field_185148_b + partialTicks);
            return MathHelper.func_76131_a(f1 / f, 0.0F, (float)endTime / 20.0F);
         } else {
            return 0.0F;
         }
      }
   }
}
