package day11.task1;

import util.IntCode;
import util.Pair;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Day11Task1 {

    Map<Integer, Map<Integer, Integer>> area = new HashMap<>();

    public static void main(String[] args) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/day11/task1/input.txt"));
            String line = reader.readLine();

            String[] splitted = line.split(",");
            List<Long> in =
                    Arrays.stream(splitted).map(Long::parseLong).collect(Collectors.toList());

            new Day11Task1(in);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Day11Task1(List<Long> in) {
        IntCode intCode = new IntCode(in);

        Pair<Integer, Integer> currentPosition = new Pair<>(0,0);
        List<Pair<Integer, Integer>> alreadyPainted = new ArrayList<>();
        int currDirection = 0; // 0 == UP - 1 == RIGHT - 2 == DOWN - 3 == LEFT

        Long result = 0L;

        while (result != null) {
            intCode.addToInput(getField(currentPosition.getKey(), currentPosition.getValue()));
            result = intCode.runCode(true);
            int outOne = 0;
            if (result != null) {
                outOne = Math.toIntExact(result);
                result = intCode.runCode(true);
            }
            if (result != null) {
                int outTwo = Math.toIntExact(result);
                if(!area.containsKey(currentPosition.getValue())) {
                    area.put(currentPosition.getValue(), new HashMap<>());
                }
                area.get(currentPosition.getValue()).put(currentPosition.getKey(), outOne);

                boolean alreadyPaintedBefore = false;

                for(Pair<Integer, Integer> p : alreadyPainted){
                    if (p.getKey().equals(currentPosition.getKey()) && p.getValue()
                            .equals(currentPosition.getValue())) {
                        alreadyPaintedBefore = true;
                        break;
                    }
                }

                if(!alreadyPaintedBefore){
                    alreadyPainted.add(currentPosition);
                }

                if(outTwo == 0L){
                    currDirection = currDirection > 0 ? currDirection-1 : 3;
                } else if(outTwo == 1L){
                    currDirection = currDirection < 3 ? currDirection+1 : 0;
                } else {
                    System.err.println("Should never happen");
                }

                switch (currDirection){
                    case 0:
                        currentPosition = new Pair<>(currentPosition.getKey(), currentPosition.getValue()+1);
                        break;
                    case 1:
                        currentPosition = new Pair<>(currentPosition.getKey()+1, currentPosition.getValue());
                        break;
                    case 2:
                        currentPosition = new Pair<>(currentPosition.getKey(), currentPosition.getValue()-1);
                        break;
                    case 3:
                        currentPosition = new Pair<>(currentPosition.getKey()-1, currentPosition.getValue());
                        break;
                }
            }

        }
        System.out.println("Panels painted at least once: " + alreadyPainted.size());
    }

    private int getField(int x, int y) {
        return area.getOrDefault(y, new HashMap<>()).getOrDefault(x, 0);
    }
}
