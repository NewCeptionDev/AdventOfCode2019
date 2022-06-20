package day4.task2;

import util.InputReader;

import java.util.ArrayList;
import java.util.List;

public class Day4Task2 {

    public static void main(String[] args) {
        String input = InputReader.read("src/day4/task1/input.txt").get(0);

        int lowerBound = Integer.parseInt(input.split("-")[0]);
        int upperBound = Integer.parseInt(input.split("-")[1]);
        System.out.println("Possible Solutions: " + calculate(lowerBound, upperBound).size());
    }

    public static List<Integer> calculate(int lower, int upper) {
        List<Integer> allNumbers = new ArrayList<>();

        for (int i = lower; i <= upper; i++) {
            allNumbers.add(i);
        }

        List<Integer> possible = new ArrayList<>();

        for (int number : allNumbers) {
            char[] asChars = Integer.toString(number).toCharArray();

            boolean okay = true;
            boolean doubleNumbers = false;
            boolean lockDouble = false;
            int lastDouble = Integer.MIN_VALUE;

            for (int j = 1; j < asChars.length && okay; j++) {
                int n1 = Integer.parseInt(String.valueOf(asChars[j - 1]));
                int n2 = Integer.parseInt(String.valueOf(asChars[j]));

                if (n1 > n2) {
                    okay = false;
                }

                if(n1 == n2){
                    if(lastDouble == n1){
                        if(!lockDouble){
                            doubleNumbers = false;
                        }
                    } else if(doubleNumbers){
                        lockDouble = true;
                    } else {
                        doubleNumbers = true;
                        lastDouble = n1;
                    }
                }
            }

            if (doubleNumbers && okay) {
                possible.add(number);
            }
        }

        return possible;
    }

}
