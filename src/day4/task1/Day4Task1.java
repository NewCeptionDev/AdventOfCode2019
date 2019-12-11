package day4.task1;

import java.util.ArrayList;
import java.util.List;

public class Day4Task1 {

    public static void main(String[] args) {
        List<Integer> allNumbers = new ArrayList<>();

        for(int i =  273025; i <= 767253; i++){
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
