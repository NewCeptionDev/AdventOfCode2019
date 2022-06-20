package day2.task2;

import day2.task1.Day2Task1;
import javafx.util.Pair;
import util.InputReader;

import java.util.Arrays;

public class Day2Task2 {

    private final Integer[] input =
            Arrays.stream(InputReader.read("src/day2/task1/input.txt").get(0).trim().split(","))
                    .map(Integer::parseInt).toArray(Integer[]::new);

    public static void main(String[] args) {
        Integer searching = 19690720;

        new Day2Task2(searching);
    }

    public Day2Task2(Integer searching) {

        boolean found = false;
        for (int i = 0; i <= 99 && !found; i++) {
            for (int j = 0; j <= 99 && !found; j++) {
                if (testPair(new Pair<>(i, j)).equals(searching)) {
                    System.out.println("Correct Pair found: (" + i + ", " + j + ")");
                    found = true;
                }
            }
        }
    }

    private Integer testPair(Pair<Integer, Integer> p) {
        Integer[] test = input.clone();
        test[1] = p.getKey();
        test[2] = p.getValue();
        Day2Task1 t = new Day2Task1(test);

        return t.processCode().get(0);
    }

}
