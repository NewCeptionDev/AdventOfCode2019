package day11.task2;

import javafx.util.Pair;
import util.IntCodeComputer;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class Day11Task2 {

    Map<Pair<Integer, Integer>, Integer> area = new HashMap<>();

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
        Map<Integer, Long> input = new HashMap<>();
        for (int i = 0; i < in.size(); i++) {
            input.put(i, in.get(i));
        }

        List<Long> inputs = new ArrayList<>();
        inputs.add(1L);

        IntCodeComputer i = new IntCodeComputer(input, inputs);
        i.changeStepsTillStop(2);
        Pair<Integer, Integer> currentPosition = new Pair<>(50, 50);
        area.put(currentPosition, 1);
        Direction direction = Direction.UP;

        while (!i.isRealDone()) {
            inputs.set(0, getField(currentPosition.getKey(), currentPosition.getValue()));
            i.updateInputs(inputs);
            int outOne = -1;
            if (!i.isRealDone()) {
                outOne = Math.toIntExact(i.getOutputs().get(0));
                int outTwo = Math.toIntExact(i.getOutputs().get(1));
                area.put(currentPosition, outOne);

                direction = direction.turn(outTwo);

                currentPosition = direction.calculateNewPosition(currentPosition);
            }
        }
        print();
    }

    private long getField(int y, int x) {
        return area.getOrDefault(new Pair<>(y, x), 0);
    }

    private void print() {

        int minY = Integer.MAX_VALUE;
        int maxY = Integer.MIN_VALUE;
        int minX = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;

        for (Pair<Integer, Integer> p : area.keySet()) {
            if (p.getKey() < minY) {
                minY = p.getKey();
            } else if (p.getKey() > maxY) {
                maxY = p.getKey();
            }

            if (p.getValue() < minX) {
                minX = p.getValue();
            } else if (p.getValue() > maxX) {
                maxX = p.getValue();
            }
        }

        for (int y = minY; y <= maxY; y++) {
            for (int x = minX; x <= maxX; x++) {
                long field = getField(y, x);
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
