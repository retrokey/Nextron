package net.pandadev.nextron.utils.commandapi;

import com.google.common.reflect.ClassPath;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import net.pandadev.nextron.utils.commandapi.help.Help;
import net.pandadev.nextron.utils.commandapi.help.HelpNode;
import net.pandadev.nextron.utils.commandapi.node.CommandNode;
import net.pandadev.nextron.utils.commandapi.paramter.ParamProcessor;
import net.pandadev.nextron.utils.commandapi.paramter.Processor;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Modifier;
import java.util.Arrays;

public class CommandHandler {
    @Getter
    @Setter
    private static Plugin plugin;

    /**
     * Registers commands based off a file path
     *
     * @param path Path
     */
    @SneakyThrows
    public static void registerCommands(String path, Plugin plugin) {
        ClassPath.from(plugin.getClass().getClassLoader()).getAllClasses().stream().filter(info -> info.getPackageName().startsWith(path)).forEach(info -> {
            Class<?> cls = info.load();
            if (!Modifier.isAbstract(cls.getModifiers())) { // Skip abstract classes
                registerCommands(cls, plugin);
            }
        });
    }

    /**
     * Registers the commands in the class
     *
     * @param commandClass Class
     */
    @SneakyThrows
    public static void registerCommands(Class<?> commandClass, Plugin plugin) {
        CommandHandler.setPlugin(plugin);
        registerCommands(commandClass.newInstance());
    }

    /**
     * Registers the commands in the class
     *
     * @param commandClasses Classes
     */
    @SneakyThrows
    public static void registerCommands(Plugin plugin, Class<?>... commandClasses) {
        CommandHandler.setPlugin(plugin);
        for (Class<?> commandClass : commandClasses) {
            registerCommands(commandClass.newInstance());
        }
    }

    /**
     * Registers the commands in the class
     *
     * @param commandClass Class
     */
    public static void registerCommands(Object commandClass) {
        Arrays.stream(commandClass.getClass().getDeclaredMethods()).forEach(method -> {
            Command command = method.getAnnotation(Command.class);
            if (command == null) return;

            new CommandNode(commandClass, method, command);
        });

        Arrays.stream(commandClass.getClass().getDeclaredMethods()).forEach(method -> {
            Help help = method.getAnnotation(Help.class);
            if (help == null) return;

            HelpNode helpNode = new HelpNode(commandClass, help.names(), help.permission(), method);
            CommandNode.getNodes().forEach(node -> node.getNames().forEach(name -> Arrays.stream(help.names()).map(String::toLowerCase).filter(helpName -> name.toLowerCase().startsWith(helpName)).forEach(helpName -> node.getHelpNodes().add(helpNode))));
        });
    }

    /**
     * Registers processors based off a file path
     *
     * @param path Path
     */
    @SneakyThrows
    public static void registerProcessors(String path, Plugin plugin) {
        ClassPath.from(plugin.getClass().getClassLoader()).getAllClasses().stream().filter(info -> info.getPackageName().startsWith(path)).filter(info -> info.load().getSuperclass().equals(Processor.class)).forEach(info -> {
            try {
                ParamProcessor.createProcessor((Processor<?>) info.load().newInstance());
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
    }

    /**
     * Register processor
     *
     * @param processor Processor
     */
    public static void registerProcessor(Processor<?> processor) {
        ParamProcessor.createProcessor(processor);
    }

    /**
     * Register processors
     *
     * @param processors Processors
     */
    public static void registerProcessors(Processor<?>... processors) {
        Arrays.stream(processors).forEach(CommandHandler::registerProcessor);
    }
}
