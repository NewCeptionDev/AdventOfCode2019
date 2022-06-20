package day2.task1;

import util.InputReader;

import java.util.Arrays;
import java.util.List;

public class Day2Task1 {

    private List<Integer> code;
    private int currPos = 0;
    private boolean done = false;
    private boolean error = false;

    public static void main(String[] args) {
        Integer[] input = Arrays.stream(InputReader.read("src/day2/task1/input.txt").get(0).trim().split(",")).map(
                Integer::parseInt).toArray(Integer[]::new);
        input[1] = 12;
        input[2] = 2;

        Day2Task1 t = new Day2Task1(input);
        System.out.println(t.processCode().toArray()[0]);
    }

    public Day2Task1(Integer[] code){
        this.code = Arrays.asList(code);
    }

    public List<Integer> processCode(){
        while (!done && !error) {
            switch (code.get(currPos)){
                case 1:
                    add(currPos+1, currPos+2, currPos+3);
                    currPos += 4;
                    break;
                case 2:
                    multiply(currPos+1, currPos+2, currPos+3);
                    currPos += 4;
                    break;
                case 3:
                    done = true;
                    break;
                default:
                    error = true;
            }
        }

        return code;
    }

    private void add(int pos1, int pos2, int pos3){
        int val1 = code.get(pos1);
        int val2 = code.get(pos2);
        code.set(code.get(pos3), code.get(val1) + code.get(val2));
    }

    private void multiply (int pos1, int pos2, int pos3){
        int val1 = code.get(pos1);
        int val2 = code.get(pos2);
        code.set(code.get(pos3), code.get(val1) * code.get(val2));
    }


}
