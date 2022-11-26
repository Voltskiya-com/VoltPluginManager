package com.voltskiya.lib.configs.command;

import com.voltskiya.lib.configs.data.AppleConfigsDatabase;
import com.voltskiya.lib.configs.data.config.AppleConfig;
import com.voltskiya.lib.pmc.PmcPlugin;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.BukkitCommandCompletionContext;
import co.aikar.commands.CommandCompletions;
import co.aikar.commands.InvalidCommandArgument;
import co.aikar.commands.PaperCommandManager;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Name;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@CommandAlias("config")
@CommandPermission("volt.config.all")
public class AppleConfigsCommand extends BaseCommand {

    public AppleConfigsCommand() {
        PaperCommandManager commandManager = PmcPlugin.get().getCommandManager();
        CommandCompletions<BukkitCommandCompletionContext> completions = commandManager.getCommandCompletions();
        completions.registerCompletion("@config_path", this::configPathCompletions);
        commandManager.registerCommand(this);
    }

    @Default
    @CommandCompletion("@config_path")
    public void set(Player player, @Name("config_path") String[] pathAndNameStrings) {
        List<String> pathAndName = trimPathname(List.of(pathAndNameStrings));
        @Nullable AppleConfig<?> config = AppleConfigsDatabase.get().findConfig(pathAndName);
        String[] pathInConfig = trimConfigPath(config, pathAndName);
        if (pathInConfig == null || pathInConfig.length <= 1)
            this.commandError(pathAndNameStrings);
        String valueToSet = pathInConfig[pathInConfig.length - 1];
        String[] pathToSet = new String[pathInConfig.length - 1];
        System.arraycopy(pathInConfig, 0, pathToSet, 0, pathToSet.length);
        String pathToSetJoined = String.join(".", pathToSet);
        if (config.setValue(pathToSet, valueToSet)) {
            player.sendMessage(
                ChatColor.GREEN + String.format("Successfully set set %s to %s", pathToSetJoined,
                    valueToSet));
        } else {
            player.sendMessage(
                ChatColor.RED + String.format("Failed to set %s to %s", pathToSetJoined,
                    valueToSet));
        }
    }

    @NotNull
    public List<String> trimPathname(List<String> path) {
        path = new ArrayList<>(path);
        path.removeIf(String::isBlank);
        return path;
    }

    private void commandError(String[] args) {
        throw new InvalidCommandArgument(
            String.format("There is no config that matches '%s'", String.join(", ", args)));
    }

    private Collection<String> configPathCompletions(BukkitCommandCompletionContext context) {
        List<String> pathAndName = List.of(
            context.getContextValueByName(String[].class, "config_path"));
        pathAndName = trimPathname(pathAndName);

        @Nullable AppleConfig<?> config = AppleConfigsDatabase.get().findConfig(pathAndName);

        if (config == null)
            return AppleConfigsDatabase.get().autoComplete(pathAndName);

        @Nullable List<String> autoComplete = tabCompleteConfigFields(config, pathAndName);
        return autoComplete == null ? tabError() : autoComplete;
    }

    @Nullable
    private List<String> tabCompleteConfigFields(AppleConfig<?> config, List<String> pathAndName) {
        String[] pathInConfig = trimConfigPath(config, pathAndName);
        if (pathInConfig == null)
            return null;
        return config.autoCompleteFields(pathInConfig);
    }

    @Nullable
    private String[] trimConfigPath(AppleConfig<?> config, List<String> pathAndName) {
        if (config == null)
            return null;
        int pathSize = config.iteratePath().size();
        if (pathSize > pathAndName.size())
            return null;
        return pathAndName.subList(pathSize, pathAndName.size()).toArray(String[]::new);
    }

    @NotNull
    private List<String> tabError() {
        return Collections.singletonList("error!");
    }

}
