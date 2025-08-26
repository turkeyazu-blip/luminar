package ru.luminar.feature.functions.settings;

import java.util.function.Supplier;

public interface ISetting {
   Setting<?> setVisible(Supplier<Boolean> var1);
}
