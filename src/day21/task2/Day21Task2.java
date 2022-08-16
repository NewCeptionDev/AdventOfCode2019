package day21.task2;

import util.InputReader;
import util.IntCode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day21Task2 {

    public static void main(String[] args) {
            List<String> in = InputReader.read("src/day21/task1/input.txt");

            new Day21Task2(Arrays.stream(in.get(0).split(",")).map(Long::parseLong).collect(
                    Collectors.toList()));
        }

        public Day21Task2(List<Long> input){
            List<String> commands = new ArrayList<>();
            commands.add("NOT C J");
            commands.add("AND D J");
            commands.add("AND H J");
            commands.add("NOT B T");
            commands.add("AND D T");
            commands.add("OR T J");
            commands.add("NOT A T");
            commands.add("OR T J");
            commands.add("RUN");

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
                    if(result > 10000) {
                        System.out.println(result);
                    }
                }
            }
            System.out.println(line);
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
