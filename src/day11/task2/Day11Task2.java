package day11.task2;

import util.IntCode;
import util.Pair;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Day11Task2 {

    Map<Integer, Map<Integer, Integer>> area = new HashMap<>();

    public static void main(String[] args) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/day11/task1/input.txt"));
            String line = reader.readLine();

            String[] splitted = line.split(",");
            List<Long> in =
                    Arrays.stream(splitted).map(Long::parseLong).collect(Collectors.toList());

            new Day11Task2(in);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    enum Direction {
        UP, DOWN, LEFT, RIGHT;

        static {
            UP.left = LEFT;
            UP.right = RIGHT;
            LEFT.left = DOWN;
            LEFT.right = UP;
            DOWN.left = RIGHT;
            DOWN.right = LEFT;
            RIGHT.left = UP;
            RIGHT.right = DOWN;
        }

        Direction left;
        Direction right;

        public Direction turn(int inputDir) {
            if (inputDir == 0L) {
                return left;
            } else {
                return right;
            }
        }

        public Pair<Integer, Integer> calculateNewPosition(Pair<Integer, Integer> currentPosition) {
            switch (this) {
                case UP:
                    return new Pair<>(currentPosition.getKey() - 1, currentPosition.getValue());
                case DOWN:
                    return new Pair<>(currentPosition.getKey() + 1, currentPosition.getValue());
                case RIGHT:
                    return new Pair<>(currentPosition.getKey(), currentPosition.getValue() + 1);
                case LEFT:
                    return new Pair<>(currentPosition.getKey(), currentPosition.getValue() - 1);
                default:
                    throw new RuntimeException("error");
            }
        }
    }

    public Day11Task2(List<Long> in) {
        List<Long> inputs = new ArrayList<>();
        inputs.add(1L);

        IntCode intCode = new IntCode(in);
        //        i.changeStepsTillStop(2);
        Pair<Integer, Integer> currentPosition = new Pair<>(50, 50);

        if (!area.containsKey(currentPosition.getValue())) {
            area.put(currentPosition.getValue(), new HashMap<>());
        }
        area.get(currentPosition.getValue()).put(currentPosition.getKey(), 1);
        Direction direction = Direction.UP;

        Long result = 0L;

        while (result != null) {
            intCode.addToInput(getField(currentPosition.getKey(), currentPosition.getValue()));
            int outOne = -1;
            result = intCode.runCode(true);
            if (result != null) {
                outOne = Math.toIntExact(result);
                result = intCode.runCode(true);
                int outTwo = Math.toIntExact(result);
                if (!area.containsKey(currentPosition.getValue())) {
                    area.put(currentPosition.getValue(), new HashMap<>());
                }
                area.get(currentPosition.getValue()).put(currentPosition.getKey(), outOne);

                direction = direction.turn(outTwo);

                currentPosition = direction.calculateNewPosition(currentPosition);
            }
        }
        print();
    }

    private int getField(int x, int y) {
        return area.getOrDefault(y, new HashMap<>()).getOrDefault(x, 0);
    }

    private void print() {

        int minY = Integer.MAX_VALUE;
        int maxY = Integer.MIN_VALUE;
        int minX = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;

        for (int y : area.keySet()) {
            if (y < minY) {
                minY = y;
            } else if (y > maxY) {
                maxY = y;
            }
            for (int x : area.get(y).keySet()) {
                if (x < minX) {
                    minX = x;
                } else if (x > maxX) {
                    maxX = x;
                }
            }
        }

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                long field = getField(x, y);
                if (field == 1L) {
                    System.out.print("#");
                } else {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
    }
}
