package day19.task2;

import util.InputReader;
import util.IntCode;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day19Task2 {

    public static void main(String[] args) {
        List<String> in = InputReader.read("src/day19/task1/input.txt");

        new Day19Task2(Arrays.stream(in.get(0).split(",")).map(Long::parseLong).collect(Collectors.toList()));
    }

    public Day19Task2(List<Long> input){
        int testingY = 5;

        while(true) {
            int firstAffectedX = -1;
            int testingX = 2;

            while(firstAffectedX == -1) {
                IntCode intCode = new IntCode(input);
                intCode.addToInput(testingX);
                intCode.addToInput(testingY);
                Long result = intCode.runCode(true);

                if(result == 1){
                    firstAffectedX = testingX;
                } else {
                    testingX++;
                }
            }

            boolean xPlusHundredStillAffected = true;

            while (xPlusHundredStillAffected) {
                IntCode intCode = new IntCode(input);
                intCode.addToInput(testingX + 99);
                intCode.addToInput(testingY);
                Long result = intCode.runCode(true);

                if(result == 1){
                    intCode = new IntCode(input);
                    intCode.addToInput(testingX);
                    intCode.addToInput(testingY + 99);
                    result = intCode.runCode(true);

                    if(result == 1) {
                        System.out.println(testingX * 10000 + testingY);
                        return;
                    } else {
                        testingX++;
                    }
                } else {
                   xPlusHundredStillAffected = false;
                   testingY++;
                }
            }
        }
    }
}
