package ru.luminar.feature.functions.impl.render;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.item.ItemStack;
import ru.luminar.Luminar;
import ru.luminar.events.Event;
import ru.luminar.events.impl.render.Render2DEvent;
import ru.luminar.events.system.Subscribe;
import ru.luminar.feature.functions.Category;
import ru.luminar.feature.functions.Function;
import ru.luminar.feature.functions.impl.render.hud.Keybinds;
import ru.luminar.feature.functions.impl.render.hud.Potions;
import ru.luminar.feature.functions.impl.render.hud.TargetHUD;
import ru.luminar.feature.functions.settings.Setting;
import ru.luminar.feature.functions.settings.impl.BooleanSetting;
import ru.luminar.mixins.accessors.MinecraftAccessor;
import ru.luminar.ui.GuiScreen;
import ru.luminar.utils.draggables.Dragging;
import ru.luminar.utils.draw.ColorUtils;
import ru.luminar.utils.draw.DisplayUtils;
import ru.luminar.utils.draw.font.Fonts;
import ru.luminar.utils.math.vectors.Vector4i;

public class Interface extends Function {
   public BooleanSetting keyBinds = new BooleanSetting("Рендерить бинды", false);
   public BooleanSetting potions = new BooleanSetting("Рендерить зельки", true);
   public BooleanSetting targetHud = new BooleanSetting("Рендерить таргетхуд", true);
   public BooleanSetting armor = new BooleanSetting("Рендерить броню", true);
   public BooleanSetting inform = new BooleanSetting("Рендерить информацию", true);
   final Keybinds binds;
   final Potions potionss;
   final TargetHUD targetHUD;
   public boolean isArmorRotated;
   Dragging armHud;
   Dragging infHud;

   public Interface() {
      super("Interface", Category.Render);
      this.armHud = Luminar.instance.createDrag(this, "Armor", 580.0F, 496.0F);
      this.infHud = Luminar.instance.createDrag(this, "Information", 50.0F, 300.0F);
      Dragging dragTh = Luminar.instance.createDrag(this, "TargetHUD", 50.0F, 200.0F);
      Dragging dragPot = Luminar.instance.createDrag(this, "Potions", 50.0F, 250.0F);
      this.binds = new Keybinds();
      this.potionss = new Potions(dragPot);
      this.targetHUD = new TargetHUD(dragTh);
      this.addSettings(new Setting[]{this.potions, this.targetHud, this.armor, this.inform});
   }

   @Subscribe
   public void onEvent(Event event) {
      if (event instanceof Render2DEvent) {
         Render2DEvent e = (Render2DEvent)event;
         if ((Boolean)this.keyBinds.get()) {
         }

         if ((Boolean)this.targetHud.get()) {
            this.targetHUD.draw(e);
         }

         if ((Boolean)this.potions.get()) {
            this.potionss.draw(e.getMatrixStack());
         }

         float w;
         float h;
         if ((Boolean)this.armor.get()) {
            int posX = (int)this.armHud.getX();
            int posY = (int)this.armHud.getY();
            w = !this.isArmorRotated ? 68.0F : 14.0F;
            h = !this.isArmorRotated ? 16.0F : 64.0F;
            List<ItemStack> armorItems = new ArrayList();
            Iterator var8 = mc.field_71439_g.func_184193_aE().iterator();

            ItemStack itemStack;
            while(var8.hasNext()) {
               itemStack = (ItemStack)var8.next();
               if (!itemStack.func_190926_b()) {
                  armorItems.add(itemStack);
               }
            }

            if (!this.isArmorRotated) {
               for(var8 = armorItems.iterator(); var8.hasNext(); posX += 18) {
                  itemStack = (ItemStack)var8.next();
                  mc.func_175599_af().func_180450_b(itemStack, posX, posY);
                  mc.func_175599_af().func_180453_a(mc.field_71466_p, itemStack, posX, posY, (String)null);
               }
            } else {
               for(int i = armorItems.size() - 1; i >= 0; --i) {
                  itemStack = (ItemStack)armorItems.get(i);
                  mc.func_175599_af().func_180450_b(itemStack, posX, posY);
                  mc.func_175599_af().func_180453_a(mc.field_71466_p, itemStack, posX, posY, (String)null);
                  posY += 16;
               }
            }

            this.armHud.setWidth(w);
            this.armHud.setHeight(h);
         }

         if ((Boolean)this.inform.get()) {
            float x = this.infHud.getX();
            float y = this.infHud.getY();
            w = 60.0F;
            h = 17.5F;
            Vector4i darkVec = new Vector4i(Color.darkGray.darker().darker().brighter().darker().getRGB(), Color.darkGray.darker().darker().brighter().darker().getRGB(), Color.darkGray.darker().darker().brighter().darker().getRGB(), Color.darkGray.darker().darker().brighter().darker().getRGB());
            Vector4i whiteVec = new Vector4i((new Color(247, 247, 247)).getRGB(), (new Color(247, 247, 247)).getRGB(), (new Color(247, 247, 247)).getRGB(), (new Color(247, 247, 247)).getRGB());
            Vector4i vec = ColorUtils.interpolate(darkVec, whiteVec, GuiScreen.whiteAnim);
            int textColor = ColorUtils.interpolate(-1, (new Color(106, 106, 106)).getRGB(), GuiScreen.whiteAnim);
            if (!Luminar.instance.functions.streamerMode.isEnabled()) {
               h += 22.5F;
            }

            DisplayUtils.drawRoundedRect(x, y, w, h, 5.0F, vec);
            Fonts.sfbold.drawText(e.getMatrixStack(), "FPS: " + ((MinecraftAccessor)mc).getDebugFPS(), x + 5.0F, y + 5.0F, textColor, 8.0F);
            if (!Luminar.instance.functions.streamerMode.isEnabled()) {
               Fonts.sfbold.drawText(e.getMatrixStack(), "X: " + mc.field_71439_g.func_233580_cy_().func_177958_n(), x + 5.0F, y + 5.0F + 8.0F, textColor, 8.0F);
               Fonts.sfbold.drawText(e.getMatrixStack(), "Y: " + mc.field_71439_g.func_233580_cy_().func_177956_o(), x + 5.0F, y + 5.0F + 16.0F, textColor, 8.0F);
               Fonts.sfbold.drawText(e.getMatrixStack(), "Z: " + mc.field_71439_g.func_233580_cy_().func_177952_p(), x + 5.0F, y + 5.0F + 24.0F, textColor, 8.0F);
            }

            this.infHud.setWidth(w);
            this.infHud.setHeight(h);
         }
      }

   }

   public void onKey(int key) {
   }
}
