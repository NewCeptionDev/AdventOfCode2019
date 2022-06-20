package day19.task2;

import javafx.util.Pair;
import util.InputReader;
import util.IntCodeComputer;

import java.util.*;
import java.util.stream.Collectors;

public class Day19Task2 {

    //TODO

    public static void main(String[] args) {
        List<String> in = InputReader.read("src/day19/task1/input.txt");

        new Day19Task2(Arrays.stream(in.get(0).split(",")).map(Long::parseLong).collect(Collectors.toList()));
    }

    private Map<Integer, Long> codeInput = new HashMap<>();

    public Day19Task2(List<Long> input) {
        for (int i = 0; i < input.size(); i++) {
            codeInput.put(i, input.get(i));
        }

        int y = 0;
        int x = 0;

        boolean found = false;
        boolean down = true;

        while (!found) {
            if (isAffected(x, y)) {
                if (isAffected(x + 99, y)) {
                    int testX = findlastX(x, y) - 99;
                    if (isAffected(testX, y + 99)) {
                        found = true;
                        findClosestPoint(testX, y);
                    }
                }
                y++;
            } else {
                if (down) {
                    x++;
                    down = false;
                } else {
                    y++;
                    down = true;
                }
            }
        }
    }

    private void findClosestPoint(int x, int y){
        List<Pair<Integer, Integer>> toCheck = new ArrayList<>();
        for(int x2 = 0; x2 < 100; x2++){
            toCheck.add(new Pair<>(x +x2, y));
        }

        for(int y2 = 0; y2 < 100; y2++){
            toCheck.add(new Pair<>(x, y2 + y));
        }

        double min = Double.MAX_VALUE;
        Pair<Integer, Integer> minPair = null;

        for(Pair<Integer, Integer> p : toCheck){
            double calc = calculateDistance(4, 5, p.getKey(), p.getValue());

            if(calc < min){
                min = calc;
                minPair = p;
            }
        }

        System.out.println(minPair);
    }

    private double calculateDistance(int x1, int y1, int x2, int y2){
        return Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2));
    }

    private int findlastX(int startX, int y) {
        int currX = startX;
        while (isAffected(currX, y)) {
            currX++;
        }

        return currX;
    }

    private boolean isAffected(int x, int y) {
        List<Long> inputs = new ArrayList<>();
        inputs.add((long) x);
        inputs.add((long) y);
        IntCodeComputer c = new IntCodeComputer(new HashMap<>(codeInput), inputs);
        c.processCode();

        List<Long> out = c.getOutputs();

        return out.get(0) == 1;
    }
}
