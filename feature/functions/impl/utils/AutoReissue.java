package ru.luminar.feature.functions.impl.utils;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.client.CClickWindowPacket;
import ru.luminar.events.Event;
import ru.luminar.events.impl.packet.PacketEvent;
import ru.luminar.events.impl.player.EventUpdate;
import ru.luminar.events.system.Subscribe;
import ru.luminar.feature.functions.Category;
import ru.luminar.feature.functions.Function;
import ru.luminar.utils.math.TimerUtil;

public class AutoReissue extends Function {
   boolean waitForPacket;
   TimerUtil timer = new TimerUtil();
   boolean locked;
   boolean send;
   boolean click1;
   boolean click2;
   TimerUtil timer2 = new TimerUtil();

   public AutoReissue() {
      super("AutoReissue", Category.Utils);
   }

   @Subscribe
   public void onEvent(Event event) {
      if (event instanceof EventUpdate) {
         Screen var3 = mc.field_71462_r;
         ContainerScreen screen;
         String title;
         if (var3 instanceof ContainerScreen) {
            screen = (ContainerScreen)var3;
            title = screen.func_231171_q_().getString();
            Slot slot = screen.getSlotUnderMouse();
            if (slot != null) {
               ItemStack stack = slot.func_75211_c();
               int number = slot.getSlotIndex();
               if (title.contains("Хранилище [1/1]") && stack.func_77973_b() == Items.field_151113_aN && number == 52) {
                  this.waitForPacket = true;
               }
            }
         }

         if (this.locked && this.timer.passed(60000L)) {
            if (!this.send) {
               mc.field_71439_g.func_71165_d("/ah");
               this.timer2.reset();
               this.send = true;
            }

            var3 = mc.field_71462_r;
            if (var3 instanceof ContainerScreen) {
               screen = (ContainerScreen)var3;
               title = screen.func_231171_q_().getString();
               if (this.timer2.passed(50L) && title.contains("Аукционы") && !this.click1) {
                  mc.func_147114_u().func_147297_a(new CClickWindowPacket(screen.func_212873_a_().field_75152_c, 46, 0, ClickType.PICKUP, ItemStack.field_190927_a, (short)0));
                  this.click1 = true;
               }

               if (this.timer2.passed(350L) && title.contains("Хранилище") && !this.click2) {
                  mc.func_147114_u().func_147297_a(new CClickWindowPacket(screen.func_212873_a_().field_75152_c, 52, 0, ClickType.PICKUP, ItemStack.field_190927_a, (short)0));
                  this.click2 = true;
               }

               if (this.timer2.passed(500L)) {
                  mc.func_147108_a((Screen)null);
               }
            }

            if (this.timer2.passed(550L)) {
               this.click1 = false;
               this.click2 = false;
               this.send = false;
               this.timer.reset();
            }
         }
      }

      if (event instanceof PacketEvent) {
         PacketEvent e = (PacketEvent)event;
         IPacket var11 = e.getPacket();
         if (var11 instanceof CClickWindowPacket) {
            CClickWindowPacket wp = (CClickWindowPacket)var11;
            Screen var12 = mc.field_71462_r;
            if (var12 instanceof ContainerScreen) {
               ContainerScreen screen = (ContainerScreen)var12;
               String title = screen.func_231171_q_().getString();
               if (!this.waitForPacket) {
                  return;
               }

               if (title.contains("Хранилище [1/1]") && !this.locked) {
                  this.waitForPacket = false;
                  this.locked = true;
                  this.timer.reset();
                  this.timer2.reset();
               }
            }
         }
      }

   }
}
