package ru.luminar.events.system;

@ElementTypesAreNonnullByDefault
public interface SubscriberExceptionHandler {
   void handleException(Throwable var1, SubscriberExceptionContext var2);
}
