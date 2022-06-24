package day5.task2;

import util.IntCode;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Day5Task2 {

    public static void main(String[] args) {
        new Day5Task2();
    }

    public Day5Task2() {
        List<Long> c = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/day5/task1/input.txt"));
            String in = reader.readLine();
            String[] strings = in.split(",");
            c = Arrays.stream(strings).map(Long::parseLong).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }

        IntCode intCode = new IntCode(c);
        intCode.addToInput(5);
        intCode.runCode(false);
    }

}
