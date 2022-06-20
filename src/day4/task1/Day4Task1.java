package day4.task1;

import util.InputReader;

import java.util.ArrayList;
import java.util.List;

public class Day4Task1 {

    public static void main(String[] args) {
        List<Integer> allNumbers = new ArrayList<>();

        String input = InputReader.read("src/day4/task1/input.txt").get(0);

        int lowerBound = Integer.parseInt(input.split("-")[0]);
        int upperBound = Integer.parseInt(input.split("-")[1]);

        for(int i =  lowerBound; i <= upperBound; i++){
            allNumbers.add(i);
        }

        List<Integer> possible = new ArrayList<>();

        for(int i = 0; i < allNumbers.size(); i++){
            int number = allNumbers.get(i);

            char[] asChars = Integer.toString(number).toCharArray();

            boolean okay = true;
            boolean doubleNumbers = false;

            for(int j = 1; j < asChars.length && okay; j++){
                int n1 = Integer.parseInt(String.valueOf(asChars[j-1]));
                int n2 = Integer.parseInt(String.valueOf(asChars[j]));

                if(n1 > n2){
                    okay = false;
                }

                if(n1 == n2){
                    doubleNumbers = true;
                }
            }

            if(doubleNumbers && okay){
                possible.add(number);
            }
        }

        System.out.println("Possible Solutions: " + possible.size());
    }


}
