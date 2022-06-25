package day16.task1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day16Task1 {

    private List<Integer> inputs;
    private List<Integer> pattern = new ArrayList<>();

    public static void main(String[] args) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/day16/task1/input.txt"));
            String line = reader.readLine();

            Day16Task1 day16 = new Day16Task1(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Day16Task1(String s) {
        this.inputs =
                Arrays.stream(s.split("")).map(Integer::parseInt).collect(Collectors.toList());

        pattern.add(0);
        pattern.add(1);
        pattern.add(0);
        pattern.add(-1);

        int count = 0;

        while (count < 100) {
            inputs = fft();
            count++;
        }

        System.out.println(inputs.subList(0, 8));
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
        List<Integer> multiByOne = new ArrayList<>();
        List<Integer> multiByMinusOne = new ArrayList<>();

        int nextOneArray = number - 1;
        int nextMinusArray = (number * 2) + (number - 1);

        for(int i = 0; i < inputs.size(); i++) {
            if(i >= nextOneArray && i < nextOneArray + number) {
                multiByOne.add(inputs.get(i));
            }
            if(i >= nextMinusArray && i < nextMinusArray + number) {
                multiByMinusOne.add(inputs.get(i));
            }
            if(i == nextOneArray + number - 1) {
                nextOneArray += number * 4;
                i += number;
            }
            if(i == nextMinusArray + number - 1) {
                nextMinusArray += number * 4;
                i += number;
            }
        }

        return multiByOne.stream().reduce(0, Integer::sum) - multiByMinusOne.stream().reduce(0, Integer::sum);
    }
}
