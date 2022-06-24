package day15.task2;

import util.InputReader;
import util.IntCode;
import util.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class Day15Task2 {

    public static void main(String[] args) {
        List<String> in = InputReader.read("src/day15/task1/input.txt");
        new Day15Task2(Arrays.stream(in.get(0).split(",")).map(Long::parseLong)
                .collect(Collectors.toList()));
    }

    private Map<Integer, Map<Integer, String>> map = new HashMap<>();

    public Day15Task2(List<Long> input) {
        IntCode intCode = new IntCode(input);

        Pair<Integer, Integer> position = new Pair<>(0, 0);
        map.put(0, new HashMap<>());
        map.get(0).put(0, ".");
        int direction = 1;
        int moves = 0;

        Long result = 0L;

        Pair<Integer, Integer> oxygenPosition = null;

        // Let robot run for a while to make sure whole map is discovered
        // Always sticking to right wall to make sure we do not get stuck
        while (result != null && moves < 50000) {
            moves++;
            intCode.addToInput(direction);
            result = intCode.runCode(true);

            if(result == null) {
                break;
            }

            if(result == 0L) {
                Pair<Integer, Integer> wallPosition = getPositionInDirection(position, direction);
                if (!map.containsKey(wallPosition.getValue())) {
                    map.put(wallPosition.getValue(), new HashMap<>());
                }
                map.get(wallPosition.getValue()).put(wallPosition.getKey(), "#");

                Pair<Integer, Integer> positionToTheRight = getPositionInDirection(position, turnRight(direction));
                if (map.containsKey(positionToTheRight.getValue()) && Objects.equals(
                        map.get(positionToTheRight.getValue()).get(positionToTheRight.getKey()),
                        "#")) {
                    direction = turnLeft(direction);
                } else {
                    direction = turnRight(direction);
                }

            } else if(result == 1L) {
                Pair<Integer, Integer> newPosition = getPositionInDirection(position, direction);
                if (!map.containsKey(newPosition.getValue())) {
                    map.put(newPosition.getValue(), new HashMap<>());
                }
                map.get(newPosition.getValue()).put(newPosition.getKey(), ".");

                position = newPosition;
                Pair<Integer, Integer> positionToTheRight = getPositionInDirection(position, turnRight(direction));
                if (!map.containsKey(positionToTheRight.getValue()) || !Objects.equals(
                        map.get(positionToTheRight.getValue()).get(positionToTheRight.getKey()),
                        "#")) {
                    direction = turnRight(direction);
                }

            } else if(result == 2L) {
                oxygenPosition = getPositionInDirection(position, direction);
                if (!map.containsKey(oxygenPosition.getValue())) {
                    map.put(oxygenPosition.getValue(), new HashMap<>());
                }
                map.get(oxygenPosition.getValue()).put(oxygenPosition.getKey(), "O");

                position = oxygenPosition;
                Pair<Integer, Integer> positionToTheRight = getPositionInDirection(position, turnRight(direction));
                if (!map.containsKey(positionToTheRight.getValue()) || !Objects.equals(
                        map.get(positionToTheRight.getValue()).get(positionToTheRight.getKey()),
                        "#")) {
                    direction = turnRight(direction);
                }
            }
        }

        List<Pair<Integer, Integer>> possiblePositions = new ArrayList<>();
        List<Pair<Integer, Integer>> visited = new ArrayList<>();
        if(oxygenPosition != null) {
            possiblePositions.add(oxygenPosition);
        } else {
            System.err.println("This should not happen");
        }
        int steps = 0;

        while (possiblePositions.size() > 0) {
            List<Pair<Integer, Integer>> newPossiblePositions = new ArrayList<>();
            visited.addAll(possiblePositions);
            steps++;

            for(Pair<Integer, Integer> currentlyConsidered : possiblePositions) {
                List<Pair<Integer, Integer>> neighbourPositions =
                        getNeighbourPositions(currentlyConsidered).stream()
                                .filter(neighbour -> visited.stream().allMatch(visitedPosition ->
                                        !visitedPosition.getKey().equals(neighbour.getKey())
                                                || !visitedPosition.getValue()
                                                .equals(neighbour.getValue()))).toList();
                for(Pair<Integer, Integer> neighbour : neighbourPositions) {
                    if (map.getOrDefault(neighbour.getValue(), new HashMap<>()).getOrDefault(neighbour.getKey(), "?").equals(".")) {
                        if(newPossiblePositions.stream().allMatch(added ->
                                !added.getValue().equals(neighbour.getValue()) || !added.getKey()
                                        .equals(neighbour.getKey()))) {
                            newPossiblePositions.add(neighbour);
                        }
                    }
                }
            }
            possiblePositions = newPossiblePositions;
        }

        System.out.println("Minutes to fill complete area: " + (steps - 1));
    }

    private Pair<Integer, Integer> getPositionInDirection(Pair<Integer, Integer> currentPosition, int direction) {
        switch (direction) {
            case 1:
                return new Pair<>(currentPosition.getKey(), currentPosition.getValue() - 1);
            case 2:
                return new Pair<>(currentPosition.getKey(), currentPosition.getValue() + 1);
            case 3:
                return new Pair<>(currentPosition.getKey() - 1, currentPosition.getValue());
            case 4:
                return new Pair<>(currentPosition.getKey() + 1, currentPosition.getValue());
        }

        return null;
    }

    private int turnLeft(int direction) {
        switch (direction) {
            case 1:
                return 3;
            case 2:
                return 4;
            case 3:
                return 2;
            case 4:
                return 1;
        }

        return 1;
    }

    private List<Pair<Integer, Integer>> getNeighbourPositions(Pair<Integer, Integer> current) {
        return List.of(new Pair<>(current.getKey() + 1, current.getValue()), new Pair<>(current.getKey() - 1, current.getValue()), new Pair<>(current.getKey(), current.getValue() - 1), new Pair<>(current.getKey(), current.getValue() + 1));
    }

    private int turnRight(int direction) {
        switch (direction) {
            case 1:
                return 4;
            case 2:
                return 3;
            case 3:
                return 1;
            case 4:
                return 2;
        }

        return 1;
    }

    private void printMap(Map<Integer, Map<Integer, String>> map) {
        int lowestY = map.keySet().stream().sorted().toList().get(0);
        int highestY = map.keySet().stream().sorted().toList().get(map.keySet().size() - 1);
        for(int y = lowestY - 1; y <= highestY + 1; y++) {
            for(int i = -30; i < 30; i++) {
                System.out.print(map.getOrDefault(y, new HashMap<>()).getOrDefault(i, "?"));
            }
            System.out.println();
        }
    }
}
