package ru.luminar.feature.functions.impl.render;

import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SUpdateTimePacket;
import ru.luminar.events.Event;
import ru.luminar.events.impl.packet.PacketEvent;
import ru.luminar.events.impl.player.EventUpdate;
import ru.luminar.events.system.Subscribe;
import ru.luminar.feature.functions.Category;
import ru.luminar.feature.functions.Function;
import ru.luminar.feature.functions.settings.Setting;
import ru.luminar.feature.functions.settings.impl.BooleanSetting;
import ru.luminar.feature.functions.settings.impl.ColorSetting;
import ru.luminar.feature.functions.settings.impl.ModeSetting;
import ru.luminar.feature.functions.settings.impl.SliderSetting;
import ru.luminar.mixins.functions.worldtweaks.WorldTweaksPacketMixin;

public class WorldTweaks extends Function {
   public BooleanSetting fog = new BooleanSetting("Менять туман", true);
   public BooleanSetting sky = new BooleanSetting("Менять небо", true);
   public BooleanSetting weather = new BooleanSetting("Менять погоду", false);
   public BooleanSetting time = new BooleanSetting("Менять время", false);
   public BooleanSetting clientColor = (new BooleanSetting("Клиентский цвет", true)).setVisible(() -> {
      return (Boolean)this.fog.get() || (Boolean)this.sky.get();
   });
   public ColorSetting color = (new ColorSetting("Цвет", -1)).setVisible(() -> {
      return ((Boolean)this.fog.get() || (Boolean)this.sky.get()) && !(Boolean)this.clientColor.get();
   });
   public ModeSetting typeWeather = (new ModeSetting("Погода", "Ясно", new String[]{"Ясно", "Дождь", "Гроза"})).setVisible(() -> {
      return (Boolean)this.weather.get();
   });
   public ModeSetting typeTime = (new ModeSetting("Время", "День", new String[]{"День", "Ночь", "Кастом"})).setVisible(() -> {
      return (Boolean)this.time.get();
   });
   public SliderSetting customTime = (new SliderSetting("Кастом время", 10000.0F, 1000.0F, 24000.0F, 1000.0F)).setVisible(() -> {
      return (Boolean)this.time.get() && this.typeTime.is("Кастом");
   });

   public WorldTweaks() {
      super("WorldTweaks", Category.Render);
      this.addSettings(new Setting[]{this.fog, this.sky, this.weather, this.time, this.clientColor, this.color, this.typeWeather, this.typeTime, this.customTime});
   }

   @Subscribe
   public void onEvent(Event event) {
      if (event instanceof EventUpdate && (Boolean)this.weather.get()) {
         if (this.typeWeather.is("Ясно")) {
            mc.field_71441_e.func_72894_k(0.0F);
            mc.field_71441_e.func_147442_i(0.0F);
         } else if (this.typeWeather.is("Дождь")) {
            mc.field_71441_e.func_72894_k(1.0F);
            mc.field_71441_e.func_147442_i(0.0F);
         } else if (this.typeWeather.is("Гроза")) {
            mc.field_71441_e.func_72894_k(1.0F);
            mc.field_71441_e.func_147442_i(1.0F);
         }
      }

      if (event instanceof PacketEvent) {
         PacketEvent e = (PacketEvent)event;
         IPacket var4 = e.getPacket();
         if (var4 instanceof SUpdateTimePacket) {
            SUpdateTimePacket p = (SUpdateTimePacket)var4;
            if ((Boolean)this.time.get()) {
               if (this.typeTime.is("День")) {
                  ((WorldTweaksPacketMixin)p).setGameTime(1000L);
                  ((WorldTweaksPacketMixin)p).setDayTime(1000L);
               } else if (this.typeTime.is("Ночь")) {
                  ((WorldTweaksPacketMixin)p).setGameTime(18000L);
                  ((WorldTweaksPacketMixin)p).setDayTime(18000L);
               } else if (this.typeTime.is("Кастом")) {
                  ((WorldTweaksPacketMixin)p).setGameTime(((Float)this.customTime.get()).longValue());
                  ((WorldTweaksPacketMixin)p).setDayTime(((Float)this.customTime.get()).longValue());
               }
            }
         }
      }

   }
}
