package day18.task1;

import util.InputReader;
import util.Pair;

import java.util.*;

public class Day18Task1 {

    public static void main(String[] args) {
        List<String> in = InputReader.read("src/day18/task1/input.txt");

        new Day18Task1(in);
    }

    private Map<Integer, Map<Integer, Character>> map;

    public interface Link {
        boolean allClear(List<Character> openDoors);

        int getDistance();
    }

    private Map<Character, Key> keys = new HashMap<>();

    private int entranceCount = 0;

    public static boolean isKey(Character c) {
        return Character.isLetter(c) && Character.isLowerCase(c);
    }

    public static boolean isDoor(Character c) {
        return Character.isLetter(c) && Character.isUpperCase(c);
    }

    public static boolean notWall(Map<Integer, Map<Integer, Character>> map,
            Pair<Integer, Integer> position) {
        return map.containsKey(position.getValue()) && map.get(position.getValue())
                .containsKey(position.getKey())
                && map.get(position.getValue()).get(position.getKey()) != '#';
    }

    public static String positionAsString(Pair<Integer, Integer> position) {
        return "K" + position.getKey() + "_" + position.getValue();
    }

    public static Link getLink(Map<Integer, Map<Integer, Character>> map,
            Pair<Integer, Integer> currentPos, Pair<Integer, Integer> targetPos) {
        List<String> visited = new ArrayList<>();
        Queue<QueueElement> queue = new LinkedList<>();
        queue.add(new QueueElement(currentPos, 0, new ArrayList<>()));

        while (!queue.isEmpty()) {
            QueueElement element = queue.poll();
            visited.add(positionAsString(element.getPosition()));
            List<Pair<Integer, Integer>> nextPositions =
                    calculatePossibleNextPositions(element.getPosition(), map.get(0).size(),
                            map.size()).stream()
                            .filter(pos -> !visited.contains(positionAsString(pos)) && notWall(map,
                                    pos)).toList();

            for (Pair<Integer, Integer> nextPos : nextPositions) {
                if (nextPos.getKey().equals(targetPos.getKey()) && nextPos.getValue()
                        .equals(targetPos.getValue())) {
                    return new Reachable(element.getDistance() + 1, element.getDoors());
                }
                Character mapElement = map.get(nextPos.getValue()).get(nextPos.getKey());
                List<Character> newDoors = new ArrayList<>(element.getDoors());
                if (isDoor(mapElement)) {
                    newDoors.add(mapElement);
                }
                queue.add(new QueueElement(nextPos, element.getDistance() + 1, newDoors));
            }
        }

        return new Unreachable();
    }

    public static int findShortestPath(List<Character> places, Map<Character, Key> keys, List<Character> visited,
            List<Character> robotPositions, int distance, Map<String, Integer> memory) {
        if (places.size() == visited.size()) {
            return distance;
        }
        List<Character> remaining = places.stream().filter(key -> !visited.contains(key)).toList();
        String mk = robotPositions.stream().map(c -> c + "").reduce("", String::concat) + ":"
                + remaining.stream().map(c -> c + "").reduce("", String::concat);

        if (memory.containsKey(mk)) {
            return memory.get(mk) + distance;
        }

        int min = Integer.MAX_VALUE;
        for (int i = 0; i < robotPositions.size(); i++) {
            Character curr = robotPositions.get(i);
            Map<Character, Link> links = keys.get(curr).getLinks();
            for (Character key : remaining.stream().filter(r -> links.get(r).allClear(visited))
                    .toList()) {
                List<Character> newPositions = new ArrayList<>(robotPositions);
                newPositions.set(i, key);
                List<Character> newVisited = new ArrayList<>(visited);
                newVisited.add(key);
                min = Math.min(min, findShortestPath(places, keys, newVisited, newPositions,
                        distance + links.get(key).getDistance(), memory));
            }
        }
        memory.put(mk, min - distance);
        return min;
    }

    public void collectKeys(List<Character> places) {
        for (int i = 0; i < places.size(); i++) {
            Key a = keys.get(places.get(i));
            Character aName = places.get(i);
            for (int j = i + 1; j < places.size(); j++) {
                Key b = keys.get(places.get(j));
                Character bName = places.get(j);
                Link link = getLink(map, a.getPosition(), b.getPosition());
                a.getLinks().put(bName, link);
                b.getLinks().put(aName, link);
            }
        }
    }

    public Day18Task1(List<String> lines) {
        map = parseMap(lines);
        List<Character> places = keys.keySet().stream().sorted().toList();
        System.out.println(places);
        collectKeys(places);
        List<Character> positions = new ArrayList<>();

        for (int i = 0; i < entranceCount; i++) {
            positions.add((char) (i + '0'));
        }

        System.out.println(findShortestPath(places, keys, positions, positions, 0, new HashMap<>()));
    }

    private Map<Integer, Map<Integer, Character>> parseMap(List<String> lines) {
        Map<Integer, Map<Integer, Character>> map = new HashMap<>();

        for (int y = 0; y < lines.size(); y++) {
            map.put(y, new HashMap<>());

            for (int x = 0; x < lines.get(y).length(); x++) {
                Character curr = lines.get(y).charAt(x);
                map.get(y).put(x, curr);

                if (isKey(curr)) {
                    keys.put(curr, new Key(new Pair<>(x, y)));
                } else if (curr == '@') {
                    keys.put((char) (entranceCount + '0'), new Key(new Pair<>(x, y)));
                    entranceCount++;
                }
            }
        }

        return map;
    }

    private static List<Pair<Integer, Integer>> calculatePossibleNextPositions(
            Pair<Integer, Integer> currentPosition, int maxX, int maxY) {
        List<Pair<Integer, Integer>> possible = new ArrayList<>();

        if (currentPosition.getKey() > 0) {
            possible.add(new Pair<>(currentPosition.getKey() - 1, currentPosition.getValue()));
        }

        if (currentPosition.getValue() > 0) {
            possible.add(new Pair<>(currentPosition.getKey(), currentPosition.getValue() - 1));
        }

        if (currentPosition.getKey() + 1 < maxX) {
            possible.add(new Pair<>(currentPosition.getKey() + 1, currentPosition.getValue()));
        }

        if (currentPosition.getValue() + 1 < maxY) {
            possible.add(new Pair<>(currentPosition.getKey(), currentPosition.getValue() + 1));
        }

        return possible;
    }

    public static class Reachable implements Link {
        private List<Character> doors;
        private int distance;

        public Reachable(int distance, List<Character> doors) {
            this.distance = distance;
            this.doors = doors;
        }

        public boolean allClear(List<Character> openDoors) {
            return doors.stream().allMatch(door -> openDoors.contains(Character.toLowerCase(door)));
        }

        public int getDistance() {
            return this.distance;
        }
    }

    public static class Unreachable implements Link {

        public boolean allClear(List<Character> openDoors) {
            return false;
        }

        public int getDistance() {
            return Integer.MAX_VALUE;
        }
    }

    public static class Key {
        private Pair<Integer, Integer> position;
        private Map<Character, Link> links;

        public Key(Pair<Integer, Integer> position) {
            this.position = position;
            links = new HashMap<>();
        }

        public Pair<Integer, Integer> getPosition() {
            return position;
        }

        public Map<Character, Link> getLinks() {
            return links;
        }
    }

    public static class QueueElement {
        private Pair<Integer, Integer> position;
        private int distance;
        private List<Character> doors;

        public QueueElement(Pair<Integer, Integer> position, int distance, List<Character> doors) {
            this.position = position;
            this.distance = distance;
            this.doors = doors;
        }

        public Pair<Integer, Integer> getPosition() {
            return position;
        }

        public int getDistance() {
            return distance;
        }

        public List<Character> getDoors() {
            return doors;
        }
    }
}
