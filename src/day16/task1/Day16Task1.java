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
            String line  = reader.readLine();

            Day16Task1 test = new Day16Task1(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public Day16Task1(String s) {
        this.inputs = Arrays.stream(s.split("")).map(Integer::parseInt).collect(
                Collectors.toList());

        pattern.add(0);
        pattern.add(1);
        pattern.add(0);
        pattern.add(-1);

        int count = 0;

        while (count <= 100) {
            inputs = fft();
            count++;
        }

        System.out.println(inputs);
    }

    public List<Integer> fft() {
        List<Integer> outputs = new ArrayList<>();
        for (int i = 0; i < inputs.size(); i++) {
            outputs.add(applyPattern(i));
        }

        return reduce(outputs);
    }

    public List<Integer> reduce(List<Integer> integerList){
        List<Integer> l = new ArrayList<>();
        for(int i = 0; i < integerList.size(); i++){
            String s = String.valueOf(integerList.get(i));
            char c = s.charAt(s.length()-1);
            l.add(Integer.parseInt(c + ""));
        }

        return l;
    }

    public int applyPattern(int number) {
        int currPatternPos = 1;
        int sum = 0;
        int counter = 0;

        for (int i = 0; i < inputs.size(); i++, currPatternPos++) {
            sum += pattern.get(currPatternPos) * inputs.get(i);

            if (counter < number) {
                counter++;
            }

            if (counter == number) {
                counter = 0;
                currPatternPos++;
            }

            if (currPatternPos + 1 >= pattern.size()) {
                currPatternPos = 0;
            }
        }

        return sum;
    }
}
