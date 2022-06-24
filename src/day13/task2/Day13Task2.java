package day13.task2;

import util.InputReader;
import util.IntCode;
import util.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class Day13Task2 {

    public static void main(String[] args) {
        List<String> input = InputReader.read("src/day13/task1/input.txt");

        new Day13Task2(Arrays.stream(input.get(0).split(",")).map(Long::parseLong)
                .collect(Collectors.toList()));
    }

    private Pair<Integer, Integer> ballPosition = null;
    private Pair<Integer, Integer> paddlePosition = null;

    public Day13Task2(List<Long> input) {
        input.set(0, 2L);
        IntCode intCode = new IntCode(input);

        Set<Pair<Integer, Integer>> blockPositions = new HashSet<>();

        boolean end = false;
        int score = 0;
        boolean parsedBlockBefore = false;

        while (!end) {
            Long result = intCode.runCode(true);
            if (result != null) {
                int x = Math.toIntExact(result);
                result = intCode.runCode(true);
                int y = Math.toIntExact(result);
                result = intCode.runCode(true);
                int type = Math.toIntExact(result);

                if (x == -1 && y == 0) {
                    score = type;
                } else {
                    if (type == 2) {
                        blockPositions.add(new Pair<>(x, y));
                        parsedBlockBefore = true;
                    } else {
                        if (type == 4) {
                            ballPosition = new Pair<>(x, y);
                            intCode.addToInput(onBallPosChange());

                        } else if (type == 3) {
                            paddlePosition = new Pair<>(x, y);
                        } else if(type == 0) {
                            blockPositions.remove(new Pair<>(x, y));
                        }

                        if (blockPositions.size() == 0 && parsedBlockBefore) {
                            end = true;
                        }
                    }
                }
            } else {
                end = true;
            }
        }

        System.out.println("Score at the End: " + score);
    }

    public int onBallPosChange() {
        if (paddlePosition != null) {
            return ballPosition.getKey().compareTo(paddlePosition.getKey());
        } else {
            return 0;
        }
    }

}
