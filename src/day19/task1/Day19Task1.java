package day19.task1;

import util.InputReader;
import util.IntCode;

import java.util.*;
import java.util.stream.Collectors;

public class Day19Task1 {

    public static List<Long> input;

    public static void main(String[] args) {
        List<String> in = InputReader.read("src/day19/task1/input.txt");

        input = Arrays.stream(in.get(0).split(",")).map(Long::parseLong).collect(Collectors.toList());
        new Day19Task1(Arrays.stream(in.get(0).split(",")).map(Long::parseLong).collect(Collectors.toList()));
    }

    public Day19Task1(List<Long> input){
        int sum = 0;
        for(int y = 0; y < 50; y++){
            for(int x = 0; x < 50; x++){
                IntCode intCode = new IntCode(input);
                intCode.addToInput(x);
                intCode.addToInput(y);
                Long result = intCode.runCode(true);

                if(result == 1){
                    sum++;
                    System.out.print("X");
                } else {
                    System.out.print(".");
                }
            }
            System.out.println();
        }

        System.out.println("Effected by Laser Beam: " + sum);
    }

    public boolean isAffected(List<Long> input, int x, int y) {
        IntCode intCode = new IntCode(input);
        intCode.addToInput(x);
        intCode.addToInput(y);
        Long result = intCode.runCode(true);

        return result == 1;
    }
}
