package day2.task2;

import util.InputReader;
import util.IntCode;
import util.Pair;

import java.util.Arrays;

public class Day2Task2 {

    private final Long[] input =
            Arrays.stream(InputReader.read("src/day2/task1/input.txt").get(0).trim().split(","))
                    .map(Long::parseLong).toArray(Long[]::new);

    public static void main(String[] args) {
        long searching = 19690720;

        new Day2Task2(searching);
    }

    public Day2Task2(long searching) {

        boolean found = false;
        for (long i = 0; i <= 99 && !found; i++) {
            for (long j = 0; j <= 99 && !found; j++) {
                if (testPair(new Pair<>(i, j)) == searching) {
                    System.out.println("Correct Pair found: (" + i + ", " + j + ")");
                    found = true;
                }
            }
        }
    }

    private long testPair(Pair<Long, Long> p) {
        Long[] test = input.clone();
        test[1] = p.getKey();
        test[2] = p.getValue();
        IntCode intCode = new IntCode(Arrays.asList(test));
        intCode.runCode(false);

        return intCode.getMemory().get(0L);
    }

}
