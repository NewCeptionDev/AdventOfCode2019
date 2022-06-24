package day9.task2;

import util.IntCode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day9Task2 {

    public static void main(String[] args) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/day9/task1/input.txt"));
            String line = reader.readLine();

            String[] splitted = line.split(",");
            List<Long> in =
                    Arrays.stream(splitted).map(Long::parseLong).collect(Collectors.toList());

            new Day9Task2(in);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Day9Task2(List<Long> in) {
        IntCode intCode = new IntCode(in);
        intCode.addToInput(2);
        intCode.runCode(false);
    }


}
