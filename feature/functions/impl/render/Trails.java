package ru.luminar.feature.functions.impl.render;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.settings.PointOfView;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Vector3d;
import org.lwjgl.opengl.GL11;
import ru.luminar.Luminar;
import ru.luminar.events.impl.render.Render3DEvent;
import ru.luminar.events.system.Subscribe;
import ru.luminar.feature.functions.Category;
import ru.luminar.feature.functions.Function;
import ru.luminar.utils.math.MathUtil;
import ru.luminar.utils.math.TimerUtil;

public class Trails extends Function {
   public ArrayList<Trails.Point> points = new ArrayList();

   public Trails() {
      super("Trails", Category.Render);
   }

   @Subscribe
   public void onRender(Render3DEvent event) {
      Iterator var2 = mc.field_71441_e.func_217369_A().iterator();

      while(true) {
         PlayerEntity entity;
         do {
            do {
               if (!var2.hasNext()) {
                  RenderSystem.pushMatrix();
                  Vector3d projection = mc.func_175598_ae().field_217783_c.func_216785_c();
                  RenderSystem.translated(-projection.field_72450_a, -projection.field_72448_b, -projection.field_72449_c);
                  RenderSystem.enableBlend();
                  RenderSystem.disableCull();
                  RenderSystem.disableTexture();
                  RenderSystem.blendFunc(770, 771);
                  RenderSystem.shadeModel(7425);
                  RenderSystem.disableAlphaTest();
                  RenderSystem.depthMask(false);
                  RenderSystem.lineWidth(3.0F);
                  GL11.glEnable(2848);
                  GL11.glHint(3154, 4354);
                  if (!this.points.isEmpty()) {
                     GL11.glBegin(8);
                     double trailHeight = 1.85D;

                     for(int i = 0; i < this.points.size(); ++i) {
                        Trails.Point point = (Trails.Point)this.points.get(i);
                        float alpha = (float)i / (float)this.points.size();
                        Color color = new Color(Luminar.instance.styleManager.getCurrentStyle().getColor(i));
                        Color colorWithAlpha = new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)(alpha * 255.0F));
                        GL11.glColor4f((float)colorWithAlpha.getRed() / 255.0F, (float)colorWithAlpha.getGreen() / 255.0F, (float)colorWithAlpha.getBlue() / 255.0F, (float)colorWithAlpha.getAlpha() / 255.0F);
                        GL11.glVertex3d(point.position.field_72450_a, point.position.field_72448_b, point.position.field_72449_c);
                        GL11.glVertex3d(point.position.field_72450_a, point.position.field_72448_b + 1.85D, point.position.field_72449_c);
                     }

                     GL11.glEnd();
                  }

                  GL11.glHint(3154, 4352);
                  GL11.glDisable(2848);
                  RenderSystem.enableTexture();
                  RenderSystem.disableBlend();
                  RenderSystem.enableAlphaTest();
                  RenderSystem.enableCull();
                  RenderSystem.shadeModel(7424);
                  RenderSystem.depthMask(true);
                  RenderSystem.popMatrix();
                  return;
               }

               entity = (PlayerEntity)var2.next();
               this.points.removeIf((p) -> {
                  return p.time.passed(500L);
               });
            } while(!(entity instanceof ClientPlayerEntity));
         } while(entity == mc.field_71439_g && mc.field_71474_y.func_243230_g() == PointOfView.FIRST_PERSON);

         Vector3d player = new Vector3d(MathUtil.interpolate(entity.func_226277_ct_(), entity.field_70142_S, (double)event.getPartialTicks()), MathUtil.interpolate(entity.func_226278_cu_() + 0.05D, entity.field_70137_T + 0.05D, (double)event.getPartialTicks()), MathUtil.interpolate(entity.func_226281_cx_(), entity.field_70136_U, (double)event.getPartialTicks()));
         this.points.add(new Trails.Point(player));
      }
   }

   public static class Point {
      private final Vector3d position;
      private final TimerUtil time = new TimerUtil();

      public Point(Vector3d position) {
         this.position = position;
      }
   }
}
