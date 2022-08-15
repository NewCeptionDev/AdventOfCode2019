package day18.task2;

import day18.task1.Day18Task1;
import util.InputReader;
import util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static day18.task1.Day18Task1.*;

public class Day18Task2 {

    public static void main(String[] args) {
        List<String> in = InputReader.read("src/day18/task1/input.txt");

        new Day18Task2(in);
    }

    private Map<Integer, Map<Integer, Character>> map;

    private Map<Character, Day18Task1.Key> keys = new HashMap<>();

    private int entranceCount = 0;

    public Day18Task2(List<String> lines) {
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

        Pair<Integer, Integer> middle = new Pair<>((lines.get(0).length() + 1) / 2 -1, (lines.size() + 1) / 2 - 1);

        lines.set(middle.getValue() - 1, lines.get(middle.getValue() - 1).substring(0, middle.getKey() - 1) + "@#@" + lines.get(middle.getValue() - 1).substring(middle.getKey() + 2));
        lines.set(middle.getValue(), lines.get(middle.getValue()).substring(0, middle.getKey() - 1) + "###" + lines.get(middle.getValue()).substring(middle.getKey() + 2));
        lines.set(middle.getValue() + 1, lines.get(middle.getValue() + 1).substring(0, middle.getKey() - 1) + "@#@" + lines.get(middle.getValue() + 1).substring(middle.getKey() + 2));


        for (int y = 0; y < lines.size(); y++) {
            map.put(y, new HashMap<>());

            for (int x = 0; x < lines.get(y).length(); x++) {
                Character curr = lines.get(y).charAt(x);
                map.get(y).put(x, curr);

                if (isKey(curr)) {
                    keys.put(curr, new Day18Task1.Key(new Pair<>(x, y)));
                } else if (curr == '@') {
                    keys.put((char) (entranceCount + '0'), new Day18Task1.Key(new Pair<>(x, y)));
                    entranceCount++;
                }
            }
        }

        return map;
    }

    public void collectKeys(List<Character> places) {
        for (int i = 0; i < places.size(); i++) {
            Day18Task1.Key a = keys.get(places.get(i));
            Character aName = places.get(i);
            for (int j = i + 1; j < places.size(); j++) {
                Day18Task1.Key b = keys.get(places.get(j));
                Character bName = places.get(j);
                Day18Task1.Link link = getLink(map, a.getPosition(), b.getPosition());
                a.getLinks().put(bName, link);
                b.getLinks().put(aName, link);
            }
        }
    }
}
