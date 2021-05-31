package net.draycia.carbon.sponge.listeners;

import com.google.inject.Inject;
import java.util.ArrayList;
import java.util.Optional;
import net.draycia.carbon.api.events.CarbonChatEvent;
import net.draycia.carbon.api.util.KeyedRenderer;
import net.draycia.carbon.common.channels.BasicChatChannel;
import net.draycia.carbon.sponge.CarbonChatSponge;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.message.PlayerChatEvent;

import static java.util.Objects.requireNonNullElse;
import static net.draycia.carbon.api.util.KeyedRenderer.keyedRenderer;
import static net.kyori.adventure.key.Key.key;
import static net.kyori.adventure.text.Component.empty;

public final class SpongeChatListener {

    private final CarbonChatSponge carbonChat;
    private final BasicChatChannel basicChat;

    @Inject
    private SpongeChatListener(
        final CarbonChatSponge carbonChat,
        final BasicChatChannel basicChat
    ) {
        this.carbonChat = carbonChat;
        this.basicChat = basicChat;
    }

    @Listener
    public void onPlayerChat(final @NonNull PlayerChatEvent event, final @First Player source) {
        final var sender = this.carbonChat.server().player(source.uniqueId()).join();

        if (sender == null) {
            return;
        }

        final var channel = requireNonNullElse(sender.selectedChannel(), this.basicChat);

        // TODO: option to specify if the channel should invoke ChatChannel#recipients
        //   or ChatChannel#filterRecipients
        //   for now we will just always invoke ChatChannel#recipients
        final var recipients = channel.recipients(sender);

        final var renderers = new ArrayList<KeyedRenderer>();
        renderers.add(keyedRenderer(key("carbon", "default"), channel));

        final var chatEvent = new CarbonChatEvent(sender, event.message(), recipients, renderers);
        final var result = this.carbonChat.eventHandler().emit(chatEvent);

        if (!result.wasSuccessful()) {
            final var message = chatEvent.result().reason();

            if (!message.equals(empty())) {
                sender.sendMessage(message);
            }

            return;
        }

        try {
            event.setAudience(Audience.audience(chatEvent.recipients()));
        } catch (final UnsupportedOperationException ignored) {
            // Do we log something here? Would get spammy fast.
        }

        event.setChatFormatter((player, target, message, originalMessage) -> {
            Component component = message;

            for (final var renderer : chatEvent.renderers()) {
                component = renderer.render(sender, target, component, message);
            }

            if (component == Component.empty()) {
                return Optional.empty();
            }

            return Optional.ofNullable(component);
        });
    }

}
