package ru.luminar.feature.functions.impl.render;

import net.minecraft.client.settings.PointOfView;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.glfw.GLFW;
import ru.luminar.events.impl.render.Render3DEvent;
import ru.luminar.events.system.Subscribe;
import ru.luminar.feature.functions.Category;
import ru.luminar.feature.functions.Function;
import ru.luminar.feature.functions.settings.Setting;
import ru.luminar.feature.functions.settings.impl.BindSetting;

public class ThirdPerson extends Function {
   public static BindSetting bind = new BindSetting("Кнопка", -1);
   public static float prevX = 0.0F;
   public static float prevY = 0.0F;
   public static float x = 0.0F;
   public static float y = 0.0F;
   public static boolean active = false;
   public PointOfView pointOfView;

   public ThirdPerson() {
      super("ThirdPerson", Category.Render);
      this.addSettings(new Setting[]{bind});
   }

   public static float getYaw(float pt) {
      return pt == 1.0F ? x : MathHelper.func_219799_g(pt, prevX, x);
   }

   public static float getPitch(float pt) {
      return pt == 1.0F ? y : MathHelper.func_219799_g(pt, prevY, y);
   }

   @Subscribe
   public void onEvent(Render3DEvent e) {
      if ((Integer)bind.get() != -1) {
         boolean keyPressed;
         if ((Integer)bind.get() > 5) {
            keyPressed = GLFW.glfwGetKey(mc.func_228018_at_().func_198092_i(), (Integer)bind.get()) == 1;
         } else {
            keyPressed = GLFW.glfwGetMouseButton(mc.func_228018_at_().func_198092_i(), (Integer)bind.get()) == 1;
         }

         if (keyPressed) {
            if (mc.field_71462_r != null) {
               return;
            }

            if (!active) {
               this.pointOfView = mc.field_71474_y.func_243230_g();
               x = mc.field_71439_g.field_70177_z;
               y = mc.field_71439_g.field_70125_A;
               prevX = mc.field_71439_g.field_70126_B;
               prevY = mc.field_71439_g.field_70127_C;
            }

            mc.field_71474_y.func_243229_a(PointOfView.THIRD_PERSON_BACK);
            active = true;
         } else if (active) {
            mc.field_71474_y.func_243229_a(this.pointOfView);
            active = false;
         }

      }
   }
}
