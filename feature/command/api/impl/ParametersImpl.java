package ru.luminar.feature.command.api.impl;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import net.minecraft.client.gui.screen.Screen;
import ru.luminar.feature.command.api.interfaces.Parameters;

public class ParametersImpl implements Parameters {
   String[] parameters;

   public ParametersImpl(String[] parameters) {
      this.parameters = parameters;
   }

   public Optional<Integer> asInt(int index) {
      return Optional.ofNullable((Integer)this.getElementFromParametersOrNull(index, Integer::valueOf));
   }

   public Optional<Float> asFloat(int index) {
      return Optional.ofNullable((Float)this.getElementFromParametersOrNull(index, Float::valueOf));
   }

   public Optional<Double> asDouble(int index) {
      return Optional.ofNullable((Double)this.getElementFromParametersOrNull(index, Double::valueOf));
   }

   public Optional<String> asString(int index) {
      return Optional.ofNullable((String)this.getElementFromParametersOrNull(index, String::valueOf));
   }

   public String collectMessage(int startIndex) {
      return ((String)IntStream.range(startIndex, this.parameters.length).mapToObj((i) -> {
         return (String)this.asString(i).orElse("");
      }).collect(Collectors.joining(" "))).trim();
   }

   public List<String> getArguments() {
      return List.of();
   }

   public Screen getSender() {
      return null;
   }

   private <T> T getElementFromParametersOrNull(int index, Function<String, T> mapper) {
      if (index >= this.parameters.length) {
         return null;
      } else {
         try {
            return mapper.apply(this.parameters[index]);
         } catch (Exception var4) {
            return null;
         }
      }
   }
}
