package ru.ifmo.se.server;

import ru.ifmo.se.commands.*;
import ru.ifmo.se.manager.App;
import ru.ifmo.se.manager.Collection;
import ru.ifmo.se.server.message.MessageReader;
import ru.ifmo.se.server.message.MessageWriter;

import java.io.*;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Invoker
 *
 * @author Gurin Minu
 * @version 0
 * @since 0
 */
public class Controller {
    private final HashMap<Pattern, ClassCommand> commandMap = new HashMap<>();
    private MessageReader reader;
    private MessageWriter writer;
    private App app;
    private Collection collection;
    private ArrayList<CommandName> hist = new ArrayList<>();


    /**
     * Constructor Controller, который принимает команды
     */
    public Controller(Collection collection, App app, MessageReader reader, MessageWriter writer) {
        this.collection = collection;
        this.app = app;
        this.reader = reader;
        this.writer = writer;
    }

    /**
     * Начинает принимать команды пользователя
     */
    public void executeCommand() {
        Object object = reader.readCommand();
        ClassCommand command;
        if (object instanceof ClassCommand) {
            command = (ClassCommand) object;
        }else if(object instanceof String && object.equals("connect")){
            writer.writeAnswer(object);
            return;
        }
        else{
            return;
        }
        Object resultat;
        if (command.getCommandName() == CommandName.HISTORY) {
            writer.writeAnswer(history());
        } else if (command.getCommandName() == CommandName.EXECUTE_SCRIPT) {
            ArrayList<Object> resultArrayList = new ArrayList<>();
            ExecuteScriptCommand executeScriptCommand = (ExecuteScriptCommand) command;
            ((List<ClassCommand>) executeScriptCommand.getArgument()).forEach(classCommand -> {
                if (classCommand.getCommandName() == CommandName.HISTORY) {
                    resultArrayList.add(history());
                } else {
                    resultArrayList.add(classCommand.execute(new Context() {
                        @Override
                        public App app() {
                            return app;
                        }

                        @Override
                        public Collection collection() {
                            return collection;
                        }
                    }));
                }
                if (classCommand.getCommandName() != CommandName.ERROR) {
                    hist.add(classCommand.getCommandName());
                }
            });
            writer.writeAnswer(resultArrayList);
        } else {
            System.out.println(command.getCommandName());
            resultat = command.execute(new Context() {
                @Override
                public App app() {
                    return app;
                }

                @Override
                public Collection collection() {
                    return collection;
                }
            });
            writer.writeAnswer(resultat);
        }
        hist.add(command.getCommandName());
    }

    private String history() {
        StringBuilder result = new StringBuilder();
        if (hist.size() == 0) {
            result = new StringBuilder("Команд не найдено");
        } else if (hist.size() < 5) {
            for (CommandName command1 : hist) {
                result.append(command1.toString().toLowerCase()).append("\n");
            }
        } else {
            for (int i = hist.size() - 5; i < hist.size(); i++) {
                result.append(hist.get(i).toString().toLowerCase()).append("\n");
            }
        }
        return result.toString();
    }
}
