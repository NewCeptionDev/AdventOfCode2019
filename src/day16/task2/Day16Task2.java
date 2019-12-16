package day16.task2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day16Task2 {

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

        for(int i = 0; i < 10000; i++){
            inputs.addAll(sequence);
        }

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
        System.out.println(getOutput(offset));

    }

    private int getOffset(){
        String offsetString = "";

        for(int i = 0; i < 7; i++){
            offsetString += inputs.get(i);
        }

        return Integer.parseInt(offsetString);
    }

    public List<Integer> fft() {
        List<Integer> outputs = new ArrayList<>();
        for (int i = 1; i <= inputs.size(); i++) {
            outputs.add(applyPattern(i));
        }

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
        int currPatternPos = 0;
        int sum = 0;
        int counter = 1;

        for (int i = 0; i < inputs.size(); i++) {
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

            sum += pattern.get(currPatternPos) * inputs.get(i);
        }

        return sum;
    }

    private String getOutput (int offset){
        String result = "";

        for(int i = offset; i < offset+8; i++){
            result += String.valueOf(inputs.get(i));
        }

        return result;
    }
}
