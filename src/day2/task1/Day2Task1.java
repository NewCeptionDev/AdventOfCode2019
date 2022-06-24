package day2.task1;

import util.InputReader;
import util.IntCode;

import java.util.Arrays;

public class Day2Task1 {

    public static void main(String[] args) {
        Long[] input = Arrays.stream(InputReader.read("src/day2/task1/input.txt").get(0).trim().split(",")).map(
                Long::parseLong).toArray(Long[]::new);
        input[1] = 12L;
        input[2] = 2L;

        Day2Task1 t = new Day2Task1(input);
    }

    public Day2Task1(Long[] code){
        IntCode intCode = new IntCode(Arrays.asList(code));
        intCode.runCode(false);
        System.out.println(intCode.getMemory().get(0L));
    }
}
