package day5.task2;

import org.junit.Test;

import java.util.Arrays;

public class Day5Task2Test {

    public static void main(String[] args) {
        Day5Task2Test t = new Day5Task2Test();
        t.test1();
    }

    public void test1(){
        Integer[] input = new Integer[]{3,12,6,12,15,1,13,14,13,4,13,99,-1,0,1,9};

        Day5Task2 t = new Day5Task2(Arrays.asList(input));
        t.processCode();
    }
}
