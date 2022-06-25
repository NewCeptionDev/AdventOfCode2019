package day16.task2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Day16Task2 {

    private List<Integer> inputs;

    public static void main(String[] args) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/day16/task1/input.txt"));
            String line = reader.readLine();

            Day16Task2 day16 = new Day16Task2(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Day16Task2(String s) {

        List<Integer> sequence =
                Arrays.stream(s.split("")).map(Integer::parseInt).collect(Collectors.toList());

        this.inputs = new ArrayList<>();

        for(int i = 0; i < 10000; i++){
            inputs.addAll(sequence);
        }

        int offset = getOffset();

        inputs = inputs.subList(offset, inputs.size());

        int count = 0;

        while (count < 100) {
            List<Integer> newInputs = new ArrayList<>();
            int sum = 0;
            for(int i = inputs.size() -1; i >= 0; i--) {
                sum = (sum + inputs.get(i)) % 10;
                newInputs.add(sum);
            }
            Collections.reverse(newInputs);
            inputs = newInputs;
            count++;
        }


        System.out.println(inputs.subList(0, 8));
    }

    private int getOffset() {
        StringBuilder offsetString = new StringBuilder();

        for (int i = 0; i < 7; i++) {
            offsetString.append(inputs.get(i));
        }

        return Integer.parseInt(offsetString.toString());
    }
}
