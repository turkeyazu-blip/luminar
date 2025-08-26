package ru.luminar.feature.command.api.interfaces;

import java.util.List;
import java.util.Optional;
import net.minecraft.client.gui.screen.Screen;

public interface Parameters {
   Optional<Integer> asInt(int var1);

   Optional<Float> asFloat(int var1);

   Optional<Double> asDouble(int var1);

   Optional<String> asString(int var1);

   String collectMessage(int var1);

   List<String> getArguments();

   Screen getSender();
}
