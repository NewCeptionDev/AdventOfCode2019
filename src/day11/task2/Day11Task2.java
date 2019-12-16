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
            if (inputDir == 0) {
                return left;
            } else {
                return right;
            }
        }

        public Pair<Integer, Integer> calculateNewPosition(Pair<Integer, Integer> currentPosition) {
            switch (this) {
                case UP:
                    return new Pair<>(currentPosition.getKey(), currentPosition.getValue() + 1);
                case DOWN:
                    return new Pair<>(currentPosition.getKey(), currentPosition.getValue() - 1);
                case RIGHT:
                    return new Pair<>(currentPosition.getKey() + 1, currentPosition.getValue());
                case LEFT:
                    return new Pair<>(currentPosition.getKey() - 1, currentPosition.getValue());
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
        Pair<Integer, Integer> currentPosition = new Pair<>(0, 0);
        List<Pair<Integer, Integer>> alreadyPainted = new ArrayList<>();
        Direction direction = Direction.UP;

        while (!i.isRealDone()) {
            inputs.set(0, (long) getField(currentPosition.getKey(), currentPosition.getValue()));
            i.updateInputs(inputs);
            i.processCode();
            int outOne = 0;
            if (!i.isRealDone()) {
                outOne = Math.toIntExact(i.getOutputs().get(0));
                i.updateInputs(inputs);
                i.processCode();
            }
            if (!i.isRealDone()) {
                int outTwo = Math.toIntExact(i.getOutputs().get(0));
                area.put(currentPosition, outOne);

                boolean alreadyPaintedBefore = false;

                for (Pair<Integer, Integer> p : alreadyPainted) {
                    if (p.getKey().equals(currentPosition.getKey()) && p.getValue()
                            .equals(currentPosition.getValue())) {
                        alreadyPaintedBefore = true;
                    }
                }

                if (!alreadyPaintedBefore) {
                    alreadyPainted.add(currentPosition);
                }

                direction = direction.turn(outTwo);

                currentPosition = direction.calculateNewPosition(currentPosition);
            }
        }
        print();
    }

    private long getField(int x, int y) {
        return area.getOrDefault(new Pair<>(x, y), 0);
    }

    private void print() {

        int minY = Integer.MAX_VALUE;
        int maxY = Integer.MIN_VALUE;
        int minX = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;

        for (Pair<Integer, Integer> p : area.keySet()) {
            if (p.getKey() < minX) {
                minX = p.getKey();
            } else if (p.getKey() > maxX) {
                maxX = p.getKey();
            }

            if (p.getValue() < minY) {
                minY = p.getValue();
            } else if (p.getValue() > maxY) {
                maxY = p.getValue();
            }
        }

        for (int y = minY; y < maxY; y++) {
            for (int x = minX; x < maxX; x++) {
                long field = getField(x, y);
                if (field == 1L) {
                    System.out.print("#");
                } else {
                    System.out.print(".");
                }
            }
            System.out.println();
        }
    }
}
