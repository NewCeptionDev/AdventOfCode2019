package day11.task2;

import javafx.util.Pair;
import util.IntCodeComputer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
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
        int currDirection = 0; // 0 == UP - 1 == RIGHT - 2 == DOWN - 3 == LEFT

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

                if (outTwo == 0L) {
                    currDirection = currDirection > 0 ? currDirection - 1 : 3;
                } else if (outTwo == 1L) {
                    currDirection = currDirection < 3 ? currDirection + 1 : 0;
                } else {
                    System.err.println("Should never happen");
                }

                switch (currDirection) {
                    case 0:
                        currentPosition = new Pair<>(currentPosition.getKey(),
                                currentPosition.getValue() + 1);
                        break;
                    case 1:
                        currentPosition = new Pair<>(currentPosition.getKey() + 1,
                                currentPosition.getValue());
                        break;
                    case 2:
                        currentPosition = new Pair<>(currentPosition.getKey(),
                                currentPosition.getValue() - 1);
                        break;
                    case 3:
                        currentPosition = new Pair<>(currentPosition.getKey() - 1,
                                currentPosition.getValue());
                        break;
                }
            }
        }
        print();
    }

    private int getField(int x, int y) {
        return area.getOrDefault(new Pair<>(x, y), 0);
    }

    private void print() {

        int minY = Integer.MAX_VALUE;
        int maxY = Integer.MIN_VALUE;
        int minX = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;

        for(Pair<Integer, Integer> p : area.keySet()){
            if(p.getKey() < minX){
                minX = p.getKey();
            } else if(p.getKey() > maxX){
                maxX = p.getKey();
            }

            if(p.getValue() < minY){
                minY = p.getValue();
            } else if(p.getValue() > maxY){
                maxY = p.getValue();
            }
        }

        for(int y = minY; y < maxY; y++){
            for(int x = minX; x < maxX; x++){
                int field = getField(x,y);
                if(field == 1){
                    System.out.print("#");
                } else {
                    System.out.print(".");
                }
            }
            System.out.println();
        }
    }
}
