package day24.task1;

import util.InputReader;
import util.Pair;

import java.util.*;

public class Day24Task1 {

    public static void main(String[] args) {
        List<String> in = InputReader.read("src/day24/task1/input.txt");

        new Day24Task1(in);
    }

    public Day24Task1(List<String> input) {
        List<String> previousAreas = new ArrayList<>();

        Area currentArea = new Area(input);
        previousAreas.add(currentArea.toAreaString());
        while(true) {
            currentArea = new Area(currentArea.nextGeneration());

            if(previousAreas.contains(currentArea.toAreaString())) {
                System.out.println(currentArea.toAreaString());
                System.out.println(currentArea.calculateBiodiversityRating());
                return;
            }
            previousAreas.add(currentArea.toAreaString());
        }
    }

    public static class Area {
        Map<Integer, Map<Integer, Character>> map = new HashMap<>();

        public Area(String areaString) {
            this(Arrays.asList(areaString.split(",")));
        }

        public Area(List<String> lines) {
            for(int y = 0; y < lines.size(); y++) {
                map.put(y, new HashMap<>());
                for(int x = 0; x < lines.get(y).length(); x++) {
                    map.get(y).put(x, lines.get(y).charAt(x));
                }
            }
        }

        public String toAreaString() {
            String areaString = "";
            for(int y : map.keySet()) {
                areaString += map.get(y).values().stream().map(val -> val + "").reduce("", String::concat);
            }

            return areaString;
        }

        public String nextGeneration() {
            String areaString = "";
            for(int y = 0; y < map.size(); y++) {
                for(int x = 0; x < map.get(y).size(); x++) {
                    List<Pair<Integer, Integer>> surroundingPositions = getSurroundingPositions(map.get(0).size(), map.size(), new Pair<>(x, y));

                    long adjacentBugs = surroundingPositions.stream().filter(pos -> map.get(pos.getValue()).get(pos.getKey()) == '#').count();

                    if(map.get(y).get(x) == '#') {
                        if(adjacentBugs == 1) {
                            areaString += '#';
                        } else {
                            areaString += '.';
                        }
                    } else {
                        if(adjacentBugs == 1 || adjacentBugs == 2) {
                            areaString += '#';
                        } else {
                            areaString += '.';
                        }
                    }
                }
                areaString += ",";
            }

            return areaString;
        }

        public long calculateBiodiversityRating() {
            long bioDiv = 0;

            for(int y = 0; y < map.size(); y++) {
                for(int x = 0; x < map.get(y).size(); x++) {
                    int pos = y * map.get(y).size() + x;

                    if(map.get(y).get(x) == '#') {
                        bioDiv += Math.pow(2, pos);
                    }
                }
            }

            return bioDiv;
        }
    }

    public static List<Pair<Integer, Integer>> getSurroundingPositions(int maxX, int maxY, Pair<Integer, Integer> position) {
        List<Pair<Integer, Integer>> surrounding = new ArrayList<>();

        if(position.getKey() > 0) {
            surrounding.add(new Pair<>(position.getKey() - 1, position.getValue()));
        }

        if(position.getValue() > 0) {
            surrounding.add(new Pair<>(position.getKey(), position.getValue() - 1));
        }

        if(position.getKey() + 1 < maxX) {
            surrounding.add(new Pair<>(position.getKey() + 1, position.getValue()));
        }

        if(position.getValue() + 1 < maxY) {
            surrounding.add(new Pair<>(position.getKey(), position.getValue() + 1));
        }

        return surrounding;
    }
}
