package day3.task2;

import day3.task1.Day3Task1;
import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Day3Task2 {

    private Pair<Integer, Integer> centralPoint = new Pair<>(0, 0);

    public static void main(String[] args) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/day3/task2/input.txt"));
            String l1 = reader.readLine();
            String l2 = reader.readLine();

            String[] l1Array = l1.split(",");
            String[] l2Array = l2.split(",");

            new Day3Task2(l1Array, l2Array);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Day3Task2(String[] wire1, String[] wire2) {
        int distance = fewestSteps(findCrossPoints(processWire(wire1), processWire(wire2)));
        System.out.println("Closest Cross Point: " + distance);
    }

    public int testing(String[] wire1, String[] wire2){
        return fewestSteps(findCrossPoints(processWire(wire1), processWire(wire2)));
    }

    private List<Pair<Integer, Integer>> processWire(String[] strings) {
        List<Pair<Integer, Integer>> crossedPoints = new LinkedList<>();
        crossedPoints.add(centralPoint);

        for (String s : strings) {
            char direction = s.charAt(0);
            int movement = Integer.parseInt(s.substring(1));

            Pair<Integer, Integer> last = crossedPoints.get(crossedPoints.size() - 1);

            switch (direction) {
                case 'R':
                    for (int i = 1; i <= movement; i++) {
                        Pair<Integer, Integer> p = new Pair<>(last.getKey() + i, last.getValue());
                        crossedPoints.add(p);
                    }
                    break;
                case 'L':
                    for (int i = 1; i <= movement; i++) {
                        Pair<Integer, Integer> p = new Pair<>(last.getKey() - i, last.getValue());
                        crossedPoints.add(p);
                    }
                    break;
                case 'U':
                    for (int i = 1; i <= movement; i++) {
                        Pair<Integer, Integer> p = new Pair<>(last.getKey(), last.getValue() - i);
                        crossedPoints.add(p);
                    }
                    break;
                case 'D':
                    for (int i = 1; i <= movement; i++) {
                        Pair<Integer, Integer> p = new Pair<>(last.getKey(), last.getValue() + i);
                        crossedPoints.add(p);
                    }
                    break;
            }
        }

        return crossedPoints;
    }

    private List<Pair<Integer, Integer>> findCrossPoints(List<Pair<Integer, Integer>> l1,
            List<Pair<Integer, Integer>> l2) {
        List<Pair<Integer, Integer>> results = new ArrayList<>();

        for (Pair<Integer, Integer> p : l1) {
            if (!p.getValue().equals(centralPoint.getValue()) || !p.getKey()
                    .equals(centralPoint.getKey())) {
                for (Pair<Integer, Integer> p2 : l2) {
                    if (p.getKey().equals(p2.getKey()) && p.getValue().equals(p2.getValue())) {
                        results.add(new Pair<>(l1.indexOf(p), l2.indexOf(p2)));
                    }
                }
            }
        }

        return results;
    }

    private int fewestSteps(List<Pair<Integer, Integer>> l) {
        if (l.size() == 1) {
            return calculateSteps(l.get(0));
        } else {
            int closest = Integer.MAX_VALUE;

            for (Pair<Integer, Integer> p : l) {
                int distance = calculateSteps(p);

                if (distance < closest) {
                    closest = distance;
                }
            }

            return closest;
        }
    }

    private int calculateSteps(Pair<Integer, Integer> p) {
        int distance = p.getKey() + p.getValue();
        return Math.abs(distance);
    }
}
