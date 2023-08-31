package ir.syrent.playground

import cloud.commandframework.Command
import cloud.commandframework.Completion
import cloud.commandframework.DescriptiveCompletion
import cloud.commandframework.arguments.standard.StringArgument
import cloud.commandframework.bukkit.BukkitCommandManager
import cloud.commandframework.execution.CommandExecutionCoordinator
import cloud.commandframework.paper.PaperCommandManager
import net.kyori.adventure.audience.Audience
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin

class Playground : JavaPlugin() {
    override fun onEnable() {
        // Plugin startup logic
        Command(this)
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}

class Command(
    plugin: Playground
) {

    var manager: PaperCommandManager<Audience>
    var builder: Command.Builder<Audience>

    init {
        val senderMapper = { commandSender: CommandSender -> Audience.audience(commandSender) }
        val backwardsMapper = { sayanSender: Audience -> sayanSender as CommandSender }

        manager = PaperCommandManager(
            plugin,
            CommandExecutionCoordinator.simpleCoordinator(),
            senderMapper,
            backwardsMapper
        )

        manager.registerBrigadier()

        builder = manager.commandBuilder("foo", "foo1", "foo2")
            .argument(
                StringArgument.builder<Audience?>("bar")
                    .withCompletionsProvider { _, _ ->
                        listOf(
                            DescriptiveCompletion.of("DescriptiveCompletion#ofSuggestion", "DescriptiveCompletion#ofDescription"),
                            Completion.of("Completion#ofCompletion").withSuggestion("Completion#ofSuggestion"),
                        )
                    }
            )
        manager.command(builder)
        manager.commandRegistrationHandler().registerCommand(builder.build())
    }
}