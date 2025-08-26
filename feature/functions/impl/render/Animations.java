package ru.luminar.feature.functions.impl.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import ru.luminar.feature.functions.Category;
import ru.luminar.feature.functions.Function;
import ru.luminar.feature.functions.settings.Setting;
import ru.luminar.feature.functions.settings.impl.ModeSetting;
import ru.luminar.feature.functions.settings.impl.SliderSetting;

public class Animations extends Function {
   public final ModeSetting mode = new ModeSetting("Режим", "Никакой", new String[]{"Никакой", "1", "2", "3", "4"});
   public final SliderSetting speed = (new SliderSetting("Скорость", 5.0F, 1.0F, 10.0F, 1.0F)).setVisible(() -> {
      return !this.mode.is("Никакой");
   });
   public final SliderSetting power = (new SliderSetting("Сила", 5.0F, 1.0F, 10.0F, 0.05F)).setVisible(() -> {
      return !this.mode.is("Никакой");
   });
   public final SliderSetting scale = (new SliderSetting("Размер", 1.0F, 0.5F, 1.5F, 0.05F)).setVisible(() -> {
      return !this.mode.is("Никакой");
   });
   public final SliderSetting right_x = new SliderSetting("Правая рука X", 0.0F, -2.0F, 2.0F, 0.1F);
   public final SliderSetting right_y = new SliderSetting("Правая рука Y", 0.0F, -2.0F, 2.0F, 0.1F);
   public final SliderSetting right_z = new SliderSetting("Правая рука Z", 0.0F, -2.0F, 2.0F, 0.1F);
   public final SliderSetting left_x = new SliderSetting("Левая рука X", 0.0F, -2.0F, 2.0F, 0.1F);
   public final SliderSetting left_y = new SliderSetting("Левая рука Y", 0.0F, -2.0F, 2.0F, 0.1F);
   public final SliderSetting left_z = new SliderSetting("Левая рука Z", 0.0F, -2.0F, 2.0F, 0.1F);

   public Animations() {
      super("Animations", Category.Render);
      this.addSettings(new Setting[]{this.mode, this.speed, this.power, this.scale, this.right_x, this.right_y, this.right_z, this.left_x, this.left_y, this.left_z});
   }

   public void animationProcess(MatrixStack stack, float swingProgress, Runnable runnable) {
      float anim = (float)Math.sin((double)swingProgress * 1.5707963267948966D * 2.0D);
      float sin1 = MathHelper.func_76126_a(swingProgress * swingProgress * 3.1415927F);
      float sin2 = MathHelper.func_76126_a(MathHelper.func_76129_c(swingProgress) * 3.1415927F);
      String var7 = (String)this.mode.get();
      byte var8 = -1;
      switch(var7.hashCode()) {
      case 49:
         if (var7.equals("1")) {
            var8 = 0;
         }
         break;
      case 50:
         if (var7.equals("2")) {
            var8 = 1;
         }
         break;
      case 51:
         if (var7.equals("3")) {
            var8 = 2;
         }
         break;
      case 52:
         if (var7.equals("4")) {
            var8 = 3;
         }
      }

      switch(var8) {
      case 0:
         stack.func_227862_a_((Float)this.scale.get(), (Float)this.scale.get(), (Float)this.scale.get());
         stack.func_227861_a_(0.4000000059604645D, 0.10000000149011612D, -0.5D);
         stack.func_227863_a_(Vector3f.field_229181_d_.func_229187_a_(90.0F));
         stack.func_227863_a_(Vector3f.field_229183_f_.func_229187_a_(-60.0F));
         stack.func_227863_a_(Vector3f.field_229179_b_.func_229187_a_(-90.0F - (Float)this.power.get() * 10.0F * anim));
         break;
      case 1:
         stack.func_227862_a_((Float)this.scale.get(), (Float)this.scale.get(), (Float)this.scale.get());
         runnable.run();
         break;
      case 2:
         stack.func_227862_a_((Float)this.scale.get(), (Float)this.scale.get(), (Float)this.scale.get());
         stack.func_227861_a_(0.0D, 0.10000000149011612D, -0.1D);
         stack.func_227863_a_(Vector3f.field_229181_d_.func_229187_a_(60.0F));
         stack.func_227863_a_(Vector3f.field_229183_f_.func_229187_a_(-60.0F));
         stack.func_227863_a_(Vector3f.field_229181_d_.func_229187_a_(sin2 * sin1 * -5.0F));
         stack.func_227863_a_(Vector3f.field_229179_b_.func_229187_a_(-10.0F - (Float)this.power.get() * 10.0F * anim));
         stack.func_227863_a_(Vector3f.field_229179_b_.func_229187_a_(-60.0F));
         break;
      case 3:
         stack.func_227862_a_((Float)this.scale.get(), (Float)this.scale.get(), (Float)this.scale.get());
         stack.func_227861_a_(0.0D, 0.10000000149011612D, -0.1D);
         stack.func_227861_a_(0.5D, -0.1D, 0.0D);
         stack.func_227863_a_(Vector3f.field_229179_b_.func_229187_a_(sin2 * -45.0F));
         stack.func_227861_a_(-0.5D, 0.1D, 0.0D);
         stack.func_227861_a_(0.5D, -0.1D, 0.0D);
         stack.func_227863_a_(Vector3f.field_229181_d_.func_229187_a_(sin2 * -20.0F));
         stack.func_227861_a_(-0.5D, 0.1D, 0.0D);
         stack.func_227863_a_(Vector3f.field_229181_d_.func_229187_a_(50.0F));
         stack.func_227863_a_(Vector3f.field_229179_b_.func_229187_a_(-90.0F));
         stack.func_227863_a_(Vector3f.field_229181_d_.func_229187_a_(20.0F));
      }

   }
}
