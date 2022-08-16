package day24.task2;

import util.InputReader;
import util.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class Day24Task2 {

    public static void main(String[] args) {
        List<String> in = InputReader.read("src/day24/task1/input.txt");

        new Day24Task2(in, 200);
    }

    public Day24Task2(List<String> input, int runtime) {
        Area currentArea = new Area(input);

        for (int i = 0; i < runtime; i++) {
            currentArea.nextGeneration();
        }

        System.out.println(currentArea.countBugs());
    }

    public static class Area {
        Map<Integer, Map<Integer, Map<Integer, Character>>> map = new HashMap<>();

        public Area(List<String> lines) {
            map.put(0, new HashMap<>());
            for (int y = 0; y < lines.size(); y++) {
                map.get(0).put(y, new HashMap<>());
                for (int x = 0; x < lines.get(y).length(); x++) {
                    map.get(0).get(y).put(x, lines.get(y).charAt(x));
                }
            }
        }

        public void nextGeneration() {
            Map<Integer, Map<Integer, Map<Integer, Character>>> newMap = new HashMap<>();

            List<Integer> sortedLevel = map.keySet().stream().sorted().toList();

            int lowestLevel = sortedLevel.get(0);
            int highestLevel = sortedLevel.get(sortedLevel.size() - 1);

            for (int level = lowestLevel - 1; level <= highestLevel + 1; level++) {
                newMap.put(level, new HashMap<>());
                for (int y = 0; y < 5; y++) {
                    newMap.get(level).put(y, new HashMap<>());
                    for (int x = 0; x < 5; x++) {
                        if (x == 2 && y == 2) {
                            newMap.get(level).get(y).put(2, '?');
                        } else {

                            Set<Pair<Integer, Integer>> surroundingPositions =
                                    getSurroundingPositions(new Pair<>(x, y));

                            Set<Pair<Integer, Integer>> normalNeighbours =
                                    surroundingPositions.stream()
                                            .filter(pos -> pos.getKey() >= 0 && pos.getValue() >= 0
                                                    && pos.getKey() < 5 && pos.getValue() < 5 && (
                                                    pos.getKey() != 2 || pos.getValue() != 2))
                                            .collect(Collectors.toSet());
                            Set<Pair<Integer, Integer>> specialNeighbours =
                                    new HashSet<>(surroundingPositions);
                            specialNeighbours.removeAll(normalNeighbours);

                            long adjacentBugs = 0L;

                            final int currLevel = level;

                            adjacentBugs += normalNeighbours.stream()
                                    .filter(pos -> map.containsKey(currLevel) && map.get(currLevel)
                                            .containsKey(pos.getValue()) && map.get(currLevel)
                                            .get(pos.getValue()).containsKey(pos.getKey()) &&
                                            map.get(currLevel).get(pos.getValue()).get(pos.getKey())
                                                    == '#').count();

                            // Inner Recursion
                            if (specialNeighbours.stream()
                                    .anyMatch(pos -> pos.getKey() == 2 && pos.getValue() == 2)) {
                                List<Pair<Integer, Integer>> specialToCheck = new ArrayList<>();

                                if (y == 1) {
                                    specialToCheck.addAll(
                                            List.of(new Pair<>(0, 0), new Pair<>(1, 0),
                                                    new Pair<>(2, 0), new Pair<>(3, 0),
                                                    new Pair<>(4, 0)));
                                } else if (y == 3) {
                                    specialToCheck.addAll(
                                            List.of(new Pair<>(0, 4), new Pair<>(1, 4),
                                                    new Pair<>(2, 4), new Pair<>(3, 4),
                                                    new Pair<>(4, 4)));
                                } else {
                                    if (x == 1) {
                                        specialToCheck.addAll(
                                                List.of(new Pair<>(0, 0), new Pair<>(0, 1),
                                                        new Pair<>(0, 2), new Pair<>(0, 3),
                                                        new Pair<>(0, 4)));
                                    } else {
                                        specialToCheck.addAll(
                                                List.of(new Pair<>(4, 0), new Pair<>(4, 1),
                                                        new Pair<>(4, 2), new Pair<>(4, 3),
                                                        new Pair<>(4, 4)));
                                    }
                                }

                                adjacentBugs += specialToCheck.stream()
                                        .filter(pos -> map.containsKey(currLevel + 1) && map.get(
                                                currLevel + 1).containsKey(pos.getValue())
                                                && map.get(currLevel + 1).get(pos.getValue())
                                                .containsKey(pos.getKey()) &&
                                                map.get(currLevel + 1).get(pos.getValue())
                                                        .get(pos.getKey()) == '#').count();
                            }

                            // Outer Recursion
                            if (specialNeighbours.stream().anyMatch(pos -> pos.getKey() < 0)) {
                                if (map.containsKey(currLevel - 1) && map.get(currLevel - 1)
                                        .containsKey(2) && map.get(currLevel - 1).get(2)
                                        .containsKey(1)
                                        && map.get(currLevel - 1).get(2).get(1) == '#') {
                                    adjacentBugs++;
                                }
                            }

                            if (specialNeighbours.stream().anyMatch(pos -> pos.getKey() > 4)) {
                                if (map.containsKey(currLevel - 1) && map.get(currLevel - 1)
                                        .containsKey(2) && map.get(currLevel - 1).get(2)
                                        .containsKey(3)
                                        && map.get(currLevel - 1).get(2).get(3) == '#') {
                                    adjacentBugs++;
                                }
                            }

                            if (specialNeighbours.stream().anyMatch(pos -> pos.getValue() < 0)) {
                                if (map.containsKey(currLevel - 1) && map.get(currLevel - 1)
                                        .containsKey(1) && map.get(currLevel - 1).get(1)
                                        .containsKey(2)
                                        && map.get(currLevel - 1).get(1).get(2) == '#') {
                                    adjacentBugs++;
                                }
                            }

                            if (specialNeighbours.stream().anyMatch(pos -> pos.getValue() > 4)) {
                                if (map.containsKey(currLevel - 1) && map.get(currLevel - 1)
                                        .containsKey(3) && map.get(currLevel - 1).get(3)
                                        .containsKey(2)
                                        && map.get(currLevel - 1).get(3).get(2) == '#') {
                                    adjacentBugs++;
                                }
                            }

                            boolean isBug = false;

                            if (map.containsKey(currLevel) && map.get(currLevel).containsKey(y)
                                    && map.get(currLevel).get(y).containsKey(x)
                                    && map.get(currLevel).get(y).get(x) == '#') {
                                if (adjacentBugs == 1) {
                                    isBug = true;
                                }
                            } else {
                                if (adjacentBugs == 1 || adjacentBugs == 2) {
                                    isBug = true;
                                }
                            }

                            newMap.get(currLevel).get(y).put(x, isBug ? '#' : '.');
                        }
                    }
                }
            }

            map = newMap;
        }

        public long countBugs() {
            long bugCount = 0;

            for (int level : map.keySet()) {
                for (int y : map.get(level).keySet()) {
                    bugCount += map.get(level).get(y).values().stream().filter(val -> val == '#')
                            .count();
                }
            }

            return bugCount;
        }
    }

    public static Set<Pair<Integer, Integer>> getSurroundingPositions(
            Pair<Integer, Integer> position) {
        Set<Pair<Integer, Integer>> surrounding = new HashSet<>();

        surrounding.add(new Pair<>(position.getKey() - 1, position.getValue()));

        surrounding.add(new Pair<>(position.getKey(), position.getValue() - 1));

        surrounding.add(new Pair<>(position.getKey() + 1, position.getValue()));

        surrounding.add(new Pair<>(position.getKey(), position.getValue() + 1));

        return surrounding;
    }

}
