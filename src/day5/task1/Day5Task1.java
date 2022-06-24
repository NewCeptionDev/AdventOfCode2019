package day5.task1;

import util.IntCode;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Day5Task1 {

    private List<Long> code;

    public static void main(String[] args) {
        new Day5Task1();
    }

    public Day5Task1() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/day5/task1/input.txt"));
            String in = reader.readLine();
            String[] strings = in.split(",");
            code = Arrays.stream(strings).map(Long::parseLong).collect(Collectors.toList());

            IntCode intCode = new IntCode(code);
            intCode.addToInput(1);
            intCode.runCode(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
