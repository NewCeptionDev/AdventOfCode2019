package day25.task1;

import util.InputReader;
import util.IntCode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day25Task1 {

    public static void main(String[] args) {
        List<String> in = InputReader.read("src/day25/task1/input.txt");

        new Day25Task1(Arrays.stream(in.get(0).split(",")).map(Long::parseLong).collect(Collectors.toList()));
    }

    public Day25Task1(List<Long> input){
        // Figured out Commands by trial and error
        List<String> commands = new ArrayList<>();
        commands.add("south");
        commands.add("west");
        commands.add("south");
        commands.add("take shell");
        commands.add("north");
        commands.add("north");
        commands.add("take weather machine");
        commands.add("north");
        commands.add("west");
        commands.add("east");
        commands.add("south");
        commands.add("west");
        commands.add("south");
        commands.add("east");
        commands.add("take candy cane");
        commands.add("west");
        commands.add("north");
        commands.add("east");
        commands.add("south");
        commands.add("east");
        commands.add("east");
        commands.add("south");
        commands.add("take hypercube");
        commands.add("south");
        commands.add("south");
        commands.add("east");

        IntCode intCode = new IntCode(input);
        for(int command : parseCommand(commands)) {
            intCode.addToInput(command);
        }

        Long result = 0L;

        String line = "";
        while (result != null) {
            result = intCode.runCode(true);
            if(result != null) {
                line += (char) Math.toIntExact(result);

                if(result == 10) {
                    System.out.println(line);
                    line = "";
                }
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
