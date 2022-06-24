package day21.task1;

import util.InputReader;
import util.IntCode;

import java.util.*;
import java.util.stream.Collectors;

public class Day21Task1 {

    public static void main(String[] args) {
        List<String> in = InputReader.read("src/day21/task1/input.txt");

        new Day21Task1(Arrays.stream(in.get(0).split(",")).map(Long::parseLong).collect(Collectors.toList()));
    }

    public Day21Task1(List<Long> input){
        List<String> commands = new ArrayList<>();
        commands.add("NOT A J");
        commands.add("NOT B T");
        commands.add("OR T J");
        commands.add("NOT C T");
        commands.add("OR T J");
        commands.add("AND D J");
        commands.add("WALK");

        IntCode intCode = new IntCode(input);
        for(int command : parseCommand(commands)) {
            intCode.addToInput(command);
        }

        Long result = 0L;
        while (result != null) {
            result = intCode.runCode(true);
            if(result != null) {
                System.out.print((char) Math.toIntExact(result));
                System.out.println(result);
            }
        }
    }

    private List<Integer> parseCommand(List<String> commands){
        List<Integer> coded = new ArrayList<>();

        for(String s : commands){
            char[] chars = s.toCharArray();

            for(char c : chars){
                coded.add(( (int) c));
            }
            coded.add(10);
        }

        return coded;
    }

}
