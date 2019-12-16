package day16.task2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day16Task2 {

    //TODO

    private List<Integer> inputs;
    private List<Integer> pattern = new ArrayList<>();

    public static void main(String[] args) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/day16/task1/input.txt"));
            String line = reader.readLine();

            Day16Task2 test = new Day16Task2(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Day16Task2(String s) {

        List<Integer> sequence =
                Arrays.stream(s.split("")).map(Integer::parseInt).collect(Collectors.toList());

        this.inputs = new ArrayList<>();

        //for(int i = 0; i < 10000; i++){
        inputs.addAll(sequence);
        //}

        System.out.println(inputs);

        pattern.add(0);
        pattern.add(1);
        pattern.add(0);
        pattern.add(-1);

        int count = 0;

        while (count < 100) {
            inputs = fft();
            count++;
        }

        int offset = getOffset();
        System.out.println(getOutput(0));

    }

    private int getOffset() {
        StringBuilder offsetString = new StringBuilder();

        for (int i = 0; i < 7; i++) {
            offsetString.append(inputs.get(i));
        }

        return Integer.parseInt(offsetString.toString());
    }

    public List<Integer> fft() {
        List<Integer> outputs = new ArrayList<>();
        for (int i = 1; i <= inputs.size(); i++) {
            outputs.add(applyPattern(i));
        }
        System.out.println("Outputs: " + outputs);

        return reduce(outputs);
    }

    public List<Integer> reduce(List<Integer> integerList) {
        List<Integer> l = new ArrayList<>();
        for (int i = 0; i < integerList.size(); i++) {
            String s = String.valueOf(integerList.get(i));
            char c = s.charAt(s.length() - 1);
            l.add(Integer.parseInt(c + ""));
        }

        return l;
    }

    public int applyPattern(int number) {
        List<Integer> workingOn = new ArrayList<>(inputs.subList(number-1, inputs.size()));

        int currPatternPos = 0;
        int sum = 0;
        int counter = 1;

        for (int i = 0; i < workingOn.size(); i++) {
            if (counter == number) {
                counter = 0;
                currPatternPos++;
            }

            if (currPatternPos == pattern.size()) {
                currPatternPos = 0;
            }

            if (counter < number) {
                counter++;
            }

            sum += pattern.get(currPatternPos) * workingOn.get(i);
        }

        return sum;
    }

    private String getOutput(int offset) {
        StringBuilder result = new StringBuilder();

        for (int i = offset; i < offset + 8; i++) {
            result.append(inputs.get(i));
        }

        return result.toString();
    }
}
