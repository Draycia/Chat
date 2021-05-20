package net.draycia.carbon.bukkit;

import cloud.commandframework.CommandManager;
import cloud.commandframework.execution.AsynchronousCommandExecutionCoordinator;
import cloud.commandframework.paper.PaperCommandManager;
import com.google.inject.Inject;
import net.draycia.carbon.api.CarbonServer;
import net.draycia.carbon.api.users.UserManager;
import net.draycia.carbon.bukkit.command.BukkitCommander;
import net.draycia.carbon.bukkit.command.BukkitPlayerCommander;
import net.draycia.carbon.common.CarbonChatCommon;
import net.draycia.carbon.common.command.Commander;
import org.apache.logging.log4j.Logger;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.nio.file.Path;

@DefaultQualifier(NonNull.class)
public final class CarbonChatBukkit extends CarbonChatCommon {

    private @Inject UserManager userManager;
    private @Inject Logger logger;
    private final CarbonChatBukkitEntry plugin;
    private @Inject CarbonServerBukkit carbonServerBukkit;

    CarbonChatBukkit(final CarbonChatBukkitEntry plugin) {
        this.plugin = plugin;

        super.initialize();
    }

    public UserManager userManager() {
        return this.userManager;
    }

    @Override
    public Logger logger() {
        return this.logger;
    }

    @Override
    public Path dataDirectory() {
        return this.plugin.getDataFolder().toPath();
    }

    @Override
    public CarbonServer server() {
        return this.carbonServerBukkit;
    }

    @Override
    protected CommandManager<Commander> createCommandManager() {
        final PaperCommandManager<Commander> commandManager;
        try {
            commandManager = new PaperCommandManager<>(
                this.plugin,
                AsynchronousCommandExecutionCoordinator.<Commander>newBuilder().build(),
                commandSender -> {
                    if (commandSender instanceof Player player) {
                        return new BukkitPlayerCommander(this, player);
                    }
                    return BukkitCommander.from(commandSender);
                },
                commander -> ((BukkitCommander) commander).commandSender()
            );
        } catch (final Exception ex) {
            throw new RuntimeException("Failed to initialize command manager.", ex);
        }
        commandManager.registerAsynchronousCompletions();
        commandManager.registerBrigadier();
        final var brigadierManager = commandManager.brigadierManager();
        if (brigadierManager != null) {
            brigadierManager.setNativeNumberSuggestions(false);
        }
        return commandManager;
    }

}
