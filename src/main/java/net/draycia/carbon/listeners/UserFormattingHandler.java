package net.draycia.carbon.listeners;

import net.draycia.carbon.CarbonChat;
import net.draycia.carbon.events.CarbonEvents;
import net.draycia.carbon.events.api.PreChatFormatEvent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.event.EventSubscriber;
import net.kyori.event.PostOrders;

public class UserFormattingHandler {

  public UserFormattingHandler() {
    CarbonEvents.register(PreChatFormatEvent.class, new EventSubscriber<PreChatFormatEvent>() {
      @Override
      public int postOrder() {
        return PostOrders.FIRST;
      }

      @Override
      public boolean consumeCancelledEvents() {
        return false;
      }

      @Override
      public void invoke(final PreChatFormatEvent event) {
        if (!event.user().online()) {
          UserFormattingHandler.this.suppressFormatting(event);
          return;
        }

        if (!event.user().player().hasPermission("carbonchat.formatting") &&
          !event.user().player().hasPermission("carbonchat.channels." + event.channel().key() + ".formatting")) {
          UserFormattingHandler.this.suppressFormatting(event);
        } else {
          // Swap the &-style codes for minimessage-compatible strings
          event.message(MiniMessage.get().serialize(CarbonChat.LEGACY.deserialize(event.message())));
        }
      }
    });
  }

  private void suppressFormatting(final PreChatFormatEvent event) {
    event.format(event.format().replace("<message>", "<pre><message></pre>"));
  }

}
