package net.draycia.carbon.velocity.users;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import java.util.Locale;
import java.util.UUID;
import net.draycia.carbon.api.users.CarbonPlayer;
import net.draycia.carbon.common.users.CarbonPlayerCommon;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

@DefaultQualifier(NonNull.class)
public final class CarbonPlayerVelocity extends CarbonPlayerCommon {

    private final ProxyServer server;
    private final CarbonPlayer carbonPlayer;

    public CarbonPlayerVelocity(final ProxyServer server, final CarbonPlayer carbonPlayer) {
        this.server = server;
        this.carbonPlayer = carbonPlayer;
    }

    public CarbonPlayerVelocity(
        final ProxyServer server,
        final Identity identity,
        final String username,
        final UUID uuid
    ) {
        super(identity, username, uuid);
        this.server = server;
        this.carbonPlayer = this;
    }

    @Override
    public Audience audience() {
        final @Nullable Player player = this.player();

        if (player == null) {
            return Audience.empty();
        }

        return player;
    }

    @Override
    protected CarbonPlayer carbonPlayer() {
        return this.carbonPlayer;
    }

    private @Nullable Player player() {
        return this.server.getPlayer(this.uuid).orElse(null);
    }

    @Override
    public @Nullable Locale locale() {
        final @Nullable Player player = this.player();

        if (player != null) {
            return player.getPlayerSettings().getLocale();
        } else {
            return null;
        }
    }

    @Override
    public Component createItemHoverComponent() {
        return Component.empty();
    }

    @Override
    public boolean hasPermission(final String permission) {
        final @Nullable Player player = this.player();

        if (player != null) {
            return player.hasPermission(permission);
        }

        return false;
    }

}