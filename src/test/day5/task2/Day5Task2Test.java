package day5.task2;

import org.junit.Test;
import util.IntCodeComputer;

import java.util.*;

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

    @Test
    public void testWithIntCodeComputer1(){
        Integer[] input = new Integer[]{3,12,6,12,15,1,13,14,13,4,13,99,-1,0,1,9};

        Map<Integer, Long> in = new HashMap<>();
        for(int j = 0; j < input.length; j++){
            in.put(j, (long)input[j]);
        }

        List<Long> inputs = new ArrayList<>();
        inputs.add(1L);

        IntCodeComputer c = new IntCodeComputer(in, inputs);
        c.processCode();
        System.out.println(c.getOutputs());
        System.out.println(c.getMemory());
    }
}
