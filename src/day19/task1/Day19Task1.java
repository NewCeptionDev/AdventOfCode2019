package day19.task1;

import util.InputReader;
import util.IntCodeComputer;

import java.util.*;
import java.util.stream.Collectors;

public class Day19Task1 {

    public static void main(String[] args) {
        List<String> in = InputReader.read("src/day19/task1/input.txt");

        new Day19Task1(Arrays.stream(in.get(0).split(",")).map(Long::parseLong).collect(Collectors.toList()));
    }

    public Day19Task1(List<Long> input){
        int sum = 0;
        Map<Integer, Long> codeInput = new HashMap<>();
        for(int i = 0; i < input.size(); i++){
            codeInput.put(i, input.get(i));
        }


        for(int y = 0; y < 50; y++){
            for(int x = 0; x < 50; x++){
                List<Long> inputs = new ArrayList<>();
                IntCodeComputer c = new IntCodeComputer(new HashMap<>(codeInput), inputs);
                inputs.add((long)x);
                inputs.add((long)y);
                c.processCode();

                List<Long> out = c.getOutputs();

                if(out.get(0) == 1){
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
}
