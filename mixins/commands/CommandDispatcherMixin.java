package ru.luminar.mixins.commands;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.luminar.Luminar;
import ru.luminar.feature.command.api.impl.DispatchResult;
import ru.luminar.feature.command.api.interfaces.CommandDispatcher;
import ru.luminar.feature.functions.impl.utils.FTHelper;
import ru.luminar.mixins.accessors.MinecraftAccessor;

@Mixin({ClientPlayerEntity.class})
public abstract class CommandDispatcherMixin {
   @Shadow
   public abstract void func_71165_d(String var1);

   @Inject(
      method = {"chat"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void check(String msg, CallbackInfo ci) {
      if (Minecraft.func_71410_x().field_71441_e != null) {
         CommandDispatcher commandDispatcher = Luminar.instance.commandDispatcher;
         if (commandDispatcher.dispatch(msg) == DispatchResult.DISPATCHED) {
            ci.cancel();
         }

         if (Luminar.instance.functions.helper.isEnabled() && (Boolean)FTHelper.ahMe.get() && msg.startsWith("/ah me")) {
            ci.cancel();
            this.func_71165_d("/ah " + ((MinecraftAccessor)Minecraft.func_71410_x()).getSession().func_111285_a());
         }

      }
   }

   private double eval(final String str) {
      return ((<undefinedtype>)(new Object() {
         int pos = -1;
         int ch;

         void nextChar() {
            this.ch = ++this.pos < str.length() ? str.charAt(this.pos) : -1;
         }

         boolean eat(int charToEat) {
            while(this.ch == 32) {
               this.nextChar();
            }

            if (this.ch == charToEat) {
               this.nextChar();
               return true;
            } else {
               return false;
            }
         }

         double parse() {
            this.nextChar();
            double x = this.parseExpression();
            if (this.pos < str.length()) {
               throw new RuntimeException("Unexpected: " + (char)this.ch);
            } else {
               return x;
            }
         }

         double parseExpression() {
            double x = this.parseTerm();

            while(true) {
               while(!this.eat(43)) {
                  if (!this.eat(45)) {
                     return x;
                  }

                  x -= this.parseTerm();
               }

               x += this.parseTerm();
            }
         }

         double parseTerm() {
            double x = this.parseFactor();

            while(true) {
               while(!this.eat(42)) {
                  if (!this.eat(47)) {
                     return x;
                  }

                  x /= this.parseFactor();
               }

               x *= this.parseFactor();
            }
         }

         double parseFactor() {
            if (this.eat(43)) {
               return this.parseFactor();
            } else if (this.eat(45)) {
               return -this.parseFactor();
            } else {
               int startPos = this.pos;
               double x;
               if (this.eat(40)) {
                  x = this.parseExpression();
                  this.eat(41);
               } else {
                  if ((this.ch < 48 || this.ch > 57) && this.ch != 46) {
                     throw new RuntimeException("Unexpected: " + (char)this.ch);
                  }

                  while(this.ch >= 48 && this.ch <= 57 || this.ch == 46) {
                     this.nextChar();
                  }

                  x = Double.parseDouble(str.substring(startPos, this.pos));
               }

               return x;
            }
         }
      })).parse();
   }
}
