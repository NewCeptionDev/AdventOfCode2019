package day13.task1;

import util.InputReader;
import util.IntCode;
import util.Pair;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Day13Task1 {

    public static void main(String[] args) {
        List<String> input = InputReader.read("src/day13/task1/input.txt");

        new Day13Task1(Arrays.stream(input.get(0).split(",")).map(Long::parseLong).collect(
                Collectors.toList()));
    }

    public Day13Task1(List<Long> input){
        IntCode intCode = new IntCode(input);

        Set<Pair<Integer, Integer>> blockPositions = new HashSet<>();

        Long result = 0L;

        while(result != null){
            result = intCode.runCode(true);
            if (result != null) {
                int x = Math.toIntExact(result);
                result = intCode.runCode(true);
                int y = Math.toIntExact(result);
                result = intCode.runCode(true);
                int type = Math.toIntExact(result);

                if(type == 2){
                    blockPositions.add(new Pair<>(x,y));
                } else {
                    blockPositions.remove(new Pair<>(x,y));
                }
            }
        }

        System.out.println("Blocks on Screen: " + blockPositions.size());
    }

}
