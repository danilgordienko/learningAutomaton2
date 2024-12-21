package ru.testproject.automaton;
import dk.brics.automaton.Automaton;
import dk.brics.automaton.RegExp;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.parse.Parser;
import java.io.File;
import java.io.IOException;
import guru.nidi.graphviz.engine.Format;

import java.util.Scanner;

public class Main {

    //визуализация автомата в файл "automaton.png"
    private static void visualizeAutomaton(Automaton automaton) {
        try {
            String dot = automaton.toDot();
            Parser parser = new Parser();
            MutableGraph graph = parser.read(dot);
            Graphviz.fromGraph(graph).render(Format.PNG).toFile(new File("automaton.png"));
        } catch (IOException e) {
            System.err.println("Ошибка при визуализации автомата: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Automaton automaton = Automaton.makeEmpty();

        System.out.println("Введите строки и их статус (true/false). Введите 'exit' для выхода.");

        while (true) {
            System.out.print("Введите строку и статус: ");
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("exit")) {
                break;
            }

            String[] parts = input.split(" ");
            if (parts.length != 2) {
                System.out.println("Некорректный ввод. Используйте формат: 'строка true/false'.");
                continue;
            }

            String string = parts[0];
            boolean shouldAccept;

            try {
                shouldAccept = Boolean.parseBoolean(parts[1]);
            } catch (Exception e) {
                System.out.println("Некорректный статус. Используйте true или false.");
                continue;
            }

            Automaton stringAutomaton = Automaton.makeString(string);

            if (shouldAccept) {
                // Добавляем строку к автомату, который принимает
                automaton = automaton.union(stringAutomaton);
            } else {
                // Исключаем строку из автомата
                Automaton complement = stringAutomaton.complement();
                automaton = automaton.intersection(complement);
            }

            automaton.minimize(); // Минимизируем автомат

            //визуализируем автомат
            visualizeAutomaton(automaton);
        }
    }
}