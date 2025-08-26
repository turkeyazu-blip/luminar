package ru.luminar.events.system;

import com.google.common.base.Preconditions;
import com.google.common.collect.Queues;
import java.util.Iterator;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@ElementTypesAreNonnullByDefault
abstract class Dispatcher {
   static Dispatcher perThreadDispatchQueue() {
      return new Dispatcher.PerThreadQueuedDispatcher();
   }

   static Dispatcher legacyAsync() {
      return new Dispatcher.LegacyAsyncDispatcher();
   }

   static Dispatcher immediate() {
      return Dispatcher.ImmediateDispatcher.INSTANCE;
   }

   abstract void dispatch(Object var1, Iterator<Subscriber> var2);

   private static final class PerThreadQueuedDispatcher extends Dispatcher {
      private final ThreadLocal<Queue<Dispatcher.PerThreadQueuedDispatcher.Event>> queue = new ThreadLocal<Queue<Dispatcher.PerThreadQueuedDispatcher.Event>>() {
         // $FF: synthetic field
         final Dispatcher.PerThreadQueuedDispatcher this$0;

         {
            this.this$0 = this$0;
         }

         protected Queue<Dispatcher.PerThreadQueuedDispatcher.Event> initialValue() {
            return Queues.newArrayDeque();
         }
      };
      private final ThreadLocal<Boolean> dispatching = new ThreadLocal<Boolean>() {
         // $FF: synthetic field
         final Dispatcher.PerThreadQueuedDispatcher this$0;

         {
            this.this$0 = this$0;
         }

         protected Boolean initialValue() {
            return false;
         }
      };

      void dispatch(Object event, Iterator<Subscriber> subscribers) {
         Preconditions.checkNotNull(event);
         Preconditions.checkNotNull(subscribers);
         Queue<Dispatcher.PerThreadQueuedDispatcher.Event> queueForThread = (Queue)Objects.requireNonNull((Queue)this.queue.get());
         queueForThread.offer(new Dispatcher.PerThreadQueuedDispatcher.Event(event, subscribers));
         if (!(Boolean)this.dispatching.get()) {
            this.dispatching.set(true);

            Dispatcher.PerThreadQueuedDispatcher.Event nextEvent;
            try {
               while((nextEvent = (Dispatcher.PerThreadQueuedDispatcher.Event)queueForThread.poll()) != null) {
                  while(nextEvent.subscribers.hasNext()) {
                     ((Subscriber)nextEvent.subscribers.next()).dispatchEvent(nextEvent.event);
                  }
               }
            } finally {
               this.dispatching.remove();
               this.queue.remove();
            }
         }

      }

      private static final class Event {
         private final Object event;
         private final Iterator<Subscriber> subscribers;

         private Event(Object event, Iterator<Subscriber> subscribers) {
            this.event = event;
            this.subscribers = subscribers;
         }
      }
   }

   private static final class LegacyAsyncDispatcher extends Dispatcher {
      private final ConcurrentLinkedQueue<Dispatcher.LegacyAsyncDispatcher.EventWithSubscriber> queue = Queues.newConcurrentLinkedQueue();

      void dispatch(Object event, Iterator<Subscriber> subscribers) {
         Preconditions.checkNotNull(event);

         while(subscribers.hasNext()) {
            this.queue.add(new Dispatcher.LegacyAsyncDispatcher.EventWithSubscriber(event, (Subscriber)subscribers.next()));
         }

         Dispatcher.LegacyAsyncDispatcher.EventWithSubscriber e;
         while((e = (Dispatcher.LegacyAsyncDispatcher.EventWithSubscriber)this.queue.poll()) != null) {
            e.subscriber.dispatchEvent(e.event);
         }

      }

      private static final class EventWithSubscriber {
         private final Object event;
         private final Subscriber subscriber;

         private EventWithSubscriber(Object event, Subscriber subscriber) {
            this.event = event;
            this.subscriber = subscriber;
         }
      }
   }

   private static final class ImmediateDispatcher extends Dispatcher {
      private static final Dispatcher.ImmediateDispatcher INSTANCE = new Dispatcher.ImmediateDispatcher();

      void dispatch(Object event, Iterator<Subscriber> subscribers) {
         Preconditions.checkNotNull(event);

         while(subscribers.hasNext()) {
            ((Subscriber)subscribers.next()).dispatchEvent(event);
         }

      }
   }
}
