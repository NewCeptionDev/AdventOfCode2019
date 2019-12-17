package day13.task1;

import javafx.util.Pair;
import util.InputReader;
import util.IntCodeComputer;

import java.util.*;
import java.util.stream.Collectors;

public class Day13Task1 {

    public static void main(String[] args) {
        List<String> input = InputReader.read("src/day13/task1/input.txt");

        new Day13Task1(Arrays.stream(input.get(0).split(",")).map(Integer::parseInt).collect(
                Collectors.toList()));
    }

    public Day13Task1(List<Integer> input){
        Map<Integer, Long> in = new HashMap<>();
        for(int i = 0; i < input.size(); i++){
            in.put(i, (long) input.get(i));
        }

        IntCodeComputer c = new IntCodeComputer(in, new ArrayList<>());

        Set<Pair<Integer, Integer>> blockPositions = new HashSet<>();

        while(!c.isRealDone()){
            c.continueProcess();
            if (!c.isRealDone()) {
                int x = Math.toIntExact(c.getOutputs().get(0));
                c.continueProcess();
                int y = Math.toIntExact(c.getOutputs().get(0));
                c.continueProcess();
                int type = Math.toIntExact(c.getOutputs().get(0));

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
