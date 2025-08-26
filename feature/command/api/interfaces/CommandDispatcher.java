package ru.luminar.feature.command.api.interfaces;

import ru.luminar.feature.command.api.impl.DispatchResult;

public interface CommandDispatcher {
   DispatchResult dispatch(String var1);
}
