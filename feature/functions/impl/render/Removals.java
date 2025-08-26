package ru.luminar.feature.functions.impl.render;

import ru.luminar.events.Event;
import ru.luminar.events.impl.overlay.EventOverlay;
import ru.luminar.events.impl.player.EventUpdate;
import ru.luminar.events.system.Subscribe;
import ru.luminar.feature.functions.Category;
import ru.luminar.feature.functions.Function;
import ru.luminar.feature.functions.settings.Setting;
import ru.luminar.feature.functions.settings.impl.BooleanSetting;

public class Removals extends Function {
   BooleanSetting fire = new BooleanSetting("Убрать огонь", true);
   BooleanSetting hurteffect = new BooleanSetting("Убрать эффект урона", true);
   BooleanSetting totem = new BooleanSetting("Убрать анимацию тотема", true);
   public BooleanSetting bossbar = new BooleanSetting("Убрать боссбар", true);
   BooleanSetting scoreboard = new BooleanSetting("Убрать скорборд", true);
   BooleanSetting rain = new BooleanSetting("Убрать дождь", true);

   public Removals() {
      super("Removals", Category.Render);
      this.addSettings(new Setting[]{this.fire, this.hurteffect, this.totem, this.bossbar, this.scoreboard, this.rain});
   }

   @Subscribe
   public void onEvent(Event event) {
      if (event instanceof EventUpdate) {
         if (mc.field_71441_e == null) {
            return;
         }

         if ((Boolean)this.rain.get()) {
            mc.field_71441_e.func_72894_k(0.0F);
            mc.field_71441_e.func_147442_i(0.0F);
         }
      }

      if (event instanceof EventOverlay) {
         EventOverlay e = (EventOverlay)event;
         boolean var10000;
         switch(e.overlayType) {
         case FIRE_OVERLAY:
            var10000 = (Boolean)this.fire.get();
            break;
         case BOSS_LINE:
            var10000 = (Boolean)this.bossbar.get();
            break;
         case SCOREBOARD:
            var10000 = (Boolean)this.scoreboard.get();
            break;
         case TOTEM:
            var10000 = (Boolean)this.totem.get();
            break;
         case HURT:
            var10000 = (Boolean)this.hurteffect.get();
            break;
         default:
            throw new IncompatibleClassChangeError();
         }

         boolean cancelOverlay = var10000;
         if (cancelOverlay) {
            e.cancel();
         }
      }

   }
}
