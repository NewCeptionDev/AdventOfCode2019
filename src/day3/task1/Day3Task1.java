package day3.task1;

import day1.task1.Day1Task1;
import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class Day3Task1 {

    private Pair<Integer, Integer> centralPoint = new Pair<>(0, 0);

    public static void main(String[] args) {
        try {
            ClassLoader loader = Day3Task1.class.getClassLoader();
            URL realPath = loader.getResource("day3/task1/input.txt");
            BufferedReader reader = new BufferedReader(new FileReader(realPath.getFile()));
            String l1 = reader.readLine();
            String l2 = reader.readLine();

            String[] l1Array = l1.split(",");
            String[] l2Array = l2.split(",");

            new Day3Task1(l1Array, l2Array);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Day3Task1(String[] wire1, String[] wire2) {
        int distance = closestCrossPoint(findCrossPoints(processWire(wire1), processWire(wire2)));
        System.out.println("Closest Cross Point: " + distance);
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
                        if (!crossedPoints.contains(p)) {
                            crossedPoints.add(p);
                        }
                    }
                    break;
                case 'L':
                    for (int i = 1; i <= movement; i++) {
                        Pair<Integer, Integer> p = new Pair<>(last.getKey() - i, last.getValue());
                        if (!crossedPoints.contains(p)) {
                            crossedPoints.add(p);
                        }
                    }
                    break;
                case 'U':
                    for (int i = 1; i <= movement; i++) {
                        Pair<Integer, Integer> p = new Pair<>(last.getKey(), last.getValue() - i);
                        if (!crossedPoints.contains(p)) {
                            crossedPoints.add(p);
                        }
                    }
                    break;
                case 'D':
                    for (int i = 1; i <= movement; i++) {
                        Pair<Integer, Integer> p = new Pair<>(last.getKey(), last.getValue() + i);
                        if (!crossedPoints.contains(p)) {
                            crossedPoints.add(p);
                        }
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
                        results.add(p);
                    }
                }
            }
        }

        System.out.println("Result Size: " + results.size());

        return results;
    }

    private int closestCrossPoint(List<Pair<Integer, Integer>> l) {
        if (l.size() == 1) {
            return calculateDistance(l.get(0));
        } else {
            int closest = Integer.MAX_VALUE;

            for (Pair<Integer, Integer> p : l) {
                int distance = calculateDistance(p);

                if (distance < closest) {
                    closest = distance;
                }
            }

            return closest;
        }
    }

    private int calculateDistance(Pair<Integer, Integer> p) {
        int distance =
                (p.getKey() - centralPoint.getKey()) + (p.getValue() - centralPoint.getValue());
        System.out.println("Calculated Distance: " + distance);
        return Math.abs(distance);
    }
}
