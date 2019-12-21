package day21.task1;

import util.InputReader;
import util.IntCodeComputer;

import java.util.*;
import java.util.stream.Collectors;

public class Day21Task1 {

    public static void main(String[] args) {
        List<String> in = InputReader.read("src/day21/task1/input.txt");

        new Day21Task1(Arrays.stream(in.get(0).split(",")).map(Long::parseLong).collect(Collectors.toList()));
    }

    public Day21Task1(List<Long> input){
        Map<Integer, Long> in = new HashMap<>();
        for(int i = 0; i < input.size(); i++){
            in.put(i, input.get(i));
        }

        List<String> commands = new ArrayList<>();
        commands.add("NOT A J");
        commands.add("NOT B T");
        commands.add("OR T J");
        commands.add("NOT C T");
        commands.add("OR T J");
        commands.add("AND D J");
        commands.add("WALK");

        IntCodeComputer c = new IntCodeComputer(in,parseCommand(commands));
        c.changeStepsTillStop(600);

        c.processCode();

        for(Long l : c.getOutputs()){
            System.out.print((char) Math.toIntExact(l));
            System.out.println(l);
        }

    }

    private List<Long> parseCommand(List<String> commands){
        List<Long> coded = new ArrayList<>();

        for(String s : commands){
            char[] chars = s.toCharArray();

            for(char c : chars){
                coded.add(((long) (int) c));
            }
            coded.add(10L);
        }

        return coded;
    }

}
