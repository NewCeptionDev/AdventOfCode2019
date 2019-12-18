package day15.task1;

import javafx.util.Pair;
import util.InputReader;
import util.IntCodeComputer;

import java.util.*;
import java.util.stream.Collectors;

public class Day15Task1 {

    //TODO

    public static void main(String[] args) {
        List<String> in = InputReader.read("src/day15/task1/input.txt");
        new Day15Task1(Arrays.stream(in.get(0).split(",")).map(Integer::parseInt)
                .collect(Collectors.toList()));
    }

    private List<Pair<Pair<Integer, Integer>, String>> foundTiles = new ArrayList<>();

    public Day15Task1(List<Integer> input) {
        boolean done = false;
        Map<Integer, Long> in = new HashMap<>();
        for (int i = 0; i < input.size(); i++) {
            in.put(i, (long) input.get(i));
        }

        Pair<Integer, Integer> position = new Pair<>(0, 0);
        List<Long> inputs = new ArrayList<>();
        inputs.add(1L);

        IntCodeComputer c = new IntCodeComputer(in, inputs);
        int counter = 0;

        while (!c.isRealDone() && !done && counter < 10000) {
            c.updateInputs(inputs);

            int out = Math.toIntExact(c.getOutputs().get(0));

            int nextMove = 0;
            switch (out) {
                case 0:
                    nextMove = insertWall(Math.toIntExact(inputs.get(0)), position);
                    break;
                case 1:
                    System.out.println("can move");
                    if (getField(position.getKey(), position.getValue()).equals("")) {
                        foundTiles.add(new Pair<>(new Pair<>(position.getKey(), position.getValue()),
                                "."));
                    }
                    nextMove = Math.toIntExact(inputs.get(0));
                    break;
                case 2:
                    System.out.println("found system");
                    if (getField(position.getKey(), position.getValue()).equals("")) {
                        foundTiles.add(new Pair<>(new Pair<>(position.getKey(), position.getValue()),
                                "0"));
                    }
                    nextMove = Math.toIntExact(inputs.get(0));
                    done = true;
                    break;
            }

            inputs = new ArrayList<>();
            inputs.add((long) nextMove);
            counter++;
        }

        System.out.println("Found: " + foundTiles.size());
        print();
    }

    private int insertWall(int input, Pair<Integer, Integer> pos) {
        System.out.println("hit wall");
        switch (input) {
            case 1:
                if (getField(pos.getKey(), pos.getValue()).equals("")) {
                    foundTiles.add(new Pair<>(new Pair<>(pos.getKey(), pos.getValue() - 1), "#"));
                }
                return 4;
            case 2:
                if (getField(pos.getKey(), pos.getValue()).equals("")) {
                    foundTiles.add(new Pair<>(new Pair<>(pos.getKey(), pos.getValue() + 1), "#"));
                }
                return 3;
            case 3:
                if (getField(pos.getKey(), pos.getValue()).equals("")) {
                    foundTiles.add(new Pair<>(new Pair<>(pos.getKey() - 1, pos.getValue()), "#"));
                }
                return 1;
            case 4:
                if (getField(pos.getKey(), pos.getValue()).equals("")) {
                    foundTiles.add(new Pair<>(new Pair<>(pos.getKey() + 1, pos.getValue()), "#"));
                }
                return 2;
        }
        return 0;
    }

    private void print() {
        int minY = Integer.MAX_VALUE;
        int maxY = Integer.MIN_VALUE;
        int maxX = Integer.MIN_VALUE;
        int minX = Integer.MAX_VALUE;

        for (int i = 0; i < foundTiles.size(); i++) {
            Pair<Integer, Integer> p = foundTiles.get(i).getKey();
            if(p.getKey() < minX){
                minX = p.getKey();
            }

            if(p.getKey() > maxX){
                maxX = p.getKey();
            }

            if(p.getValue() > maxY){
                maxY = p.getValue();
            }

            if(p.getValue() < minY){
                minY = p.getValue();
            }
        }

        for (int y = maxY; y > minY; y--){
            for(int x = minY; x < maxX; x++){
                System.out.print(getField(x, y));
            }
            System.out.println();
        }
    }

    private String getField(int x, int y){
        for(Pair<Pair<Integer, Integer>, String> p : foundTiles){
            if(p.getKey().getValue().equals(y) && p.getKey().getKey().equals(x)){
                return p.getValue();
            }
        }
        return "?";
    }
}
