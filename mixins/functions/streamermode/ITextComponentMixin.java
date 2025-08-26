package ru.luminar.mixins.functions.streamermode;

import java.util.Optional;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.ITextProperties.IStyledTextAcceptor;
import net.minecraft.util.text.ITextProperties.ITextAcceptor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import ru.luminar.feature.functions.impl.utils.StreamerMode;

@Mixin({ITextComponent.class})
public interface ITextComponentMixin {
   @Shadow
   String func_150261_e();

   @Overwrite
   default <T> Optional<T> func_230534_b_(IStyledTextAcceptor<T> acceptor, Style style) {
      return acceptor.accept(style, StreamerMode.replace(this.func_150261_e()));
   }

   @Overwrite
   default <T> Optional<T> func_230533_b_(ITextAcceptor<T> acceptor) {
      return acceptor.accept(StreamerMode.replace(this.func_150261_e()));
   }
}
