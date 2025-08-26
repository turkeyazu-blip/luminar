package ru.luminar.feature.managers.waypoint;

import java.awt.Color;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;
import ru.luminar.events.impl.render.Render2DEvent;
import ru.luminar.events.system.Subscribe;
import ru.luminar.feature.command.impl.WaypointCommand;
import ru.luminar.utils.client.IMinecraft;
import ru.luminar.utils.draw.ColorUtils;
import ru.luminar.utils.draw.DisplayUtils;
import ru.luminar.utils.draw.font.Fonts;
import ru.luminar.utils.math.ProjectionUtil;
import ru.luminar.utils.math.vectors.Vector4i;

public class WaypointManager implements IMinecraft {
   @Subscribe
   public void onEvent(Render2DEvent e) {
      if (!WaypointCommand.waypoints.isEmpty()) {
         Iterator var2 = WaypointCommand.waypoints.keySet().iterator();

         while(var2.hasNext()) {
            String name = (String)var2.next();
            Vector3i vec3i = (Vector3i)WaypointCommand.waypoints.get(name);
            Vector3d vec3d = new Vector3d((double)vec3i.func_177958_n() + 0.5D, (double)vec3i.func_177956_o() + 0.5D, (double)vec3i.func_177952_p() + 0.5D);
            Vector2f vec2f = ProjectionUtil.project(vec3d.field_72450_a, vec3d.field_72448_b, vec3d.field_72449_c);
            int distance = (int)Minecraft.func_71410_x().field_71439_g.func_213303_ch().func_72438_d(vec3d);
            String dist = distance + "m";
            float textWidth = Math.max(Fonts.montserrat.getWidth(name, 8.0F, 0.05F), Fonts.montserrat.getWidth(dist, 6.0F, 0.05F));
            float fontHeight = Fonts.montserrat.getHeight(8.0F);
            float posX = vec2f.field_189982_i - textWidth / 2.0F;
            float posY = vec2f.field_189983_j - fontHeight / 2.0F;
            String icon = "B";
            if (name.equals("Вулкан")) {
               icon = "A";
            } else if (name.equals("Маяк убийца")) {
               icon = "E";
            } else if (name.equals("Мистический Алтарь")) {
               icon = "F";
            } else if (name.equals("Метеоритный дождь")) {
               icon = "C";
            }

            Vector4i vec = new Vector4i(ColorUtils.reAlphaInt(Color.darkGray.darker().darker().brighter().darker().getRGB(), 204), ColorUtils.reAlphaInt(Color.darkGray.darker().darker().brighter().darker().getRGB(), 204), ColorUtils.reAlphaInt(Color.darkGray.darker().darker().brighter().darker().getRGB(), 204), ColorUtils.reAlphaInt(Color.darkGray.darker().darker().brighter().darker().getRGB(), 170));
            DisplayUtils.drawRoundedRect(posX - 4.0F, posY - 2.0F, textWidth + 7.0F, fontHeight * 2.0F + 4.0F, 4.0F, vec);
            Fonts.montserrat.drawText(e.getMatrixStack(), name, posX, posY, -1, 8.0F, 0.05F);
            Fonts.montserrat.drawText(e.getMatrixStack(), dist, posX, posY + fontHeight, Color.gray.getRGB(), 6.0F, 0.05F);
         }

      }
   }
}
