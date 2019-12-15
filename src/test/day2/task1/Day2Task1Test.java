package day2.task1;

import org.junit.Test;
import util.IntCodeComputer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertArrayEquals;

public class Day2Task1Test {

    @Test
    public void processCode() {
        Integer[] in = new Integer[] { 1, 0, 0, 0, 99 };
        Integer[] exp = new Integer[] { 2, 0, 0, 0, 99 };
        Day2Task1 t = new Day2Task1(in);
        assertArrayEquals(exp, t.processCode().toArray());
    }

    @Test
    public void processCode2() {
        Integer[] in = new Integer[] { 2, 3, 0, 3, 99 };
        Integer[] exp = new Integer[] { 2, 3, 0, 6, 99 };
        Day2Task1 t = new Day2Task1(in);
        assertArrayEquals(exp, t.processCode().toArray());
    }

    @Test
    public void processCode3() {
        Integer[] in = new Integer[] { 2, 4, 4, 5, 99, 0 };
        Integer[] exp = new Integer[] { 2, 4, 4, 5, 99, 9801 };
        Day2Task1 t = new Day2Task1(in);
        assertArrayEquals(exp, t.processCode().toArray());
    }

    @Test
    public void processCode4() {
        Integer[] in = new Integer[] { 1, 1, 1, 4, 99, 5, 6, 0, 99 };
        Integer[] exp = new Integer[] { 30, 1, 1, 4, 2, 5, 6, 0, 99 };
        Day2Task1 t = new Day2Task1(in);
        assertArrayEquals(exp, t.processCode().toArray());
    }

    @Test
    public void testWithIntCodeComputer1(){
        Integer[] in = new Integer[] { 1, 0, 0, 0, 99 };
        Map<Integer, Long> input = new HashMap<>();

        for(int i = 0; i < in.length; i++){
            input.put(i, (long)in[i]);
        }

        IntCodeComputer c = new IntCodeComputer(input, new ArrayList<>());
        c.processCode();
        System.out.println(c.getMemory());
    }

    @Test
    public void testWithIntCodeComputer2(){
        Integer[] in = new Integer[] { 2, 3, 0, 3, 99 };
        Map<Integer, Long> input = new HashMap<>();

        for(int i = 0; i < in.length; i++){
            input.put(i, (long)in[i]);
        }

        IntCodeComputer c = new IntCodeComputer(input, new ArrayList<>());
        c.processCode();
        System.out.println(c.getMemory());
    }

    @Test
    public void testWithIntCodeComputer3(){
        Integer[] in = new Integer[] { 2, 4, 4, 5, 99, 0 };
        Map<Integer, Long> input = new HashMap<>();

        for(int i = 0; i < in.length; i++){
            input.put(i, (long)in[i]);
        }

        IntCodeComputer c = new IntCodeComputer(input, new ArrayList<>());
        c.processCode();
        System.out.println(c.getMemory());
    }

    @Test
    public void testWithIntCodeComputer4(){
        Integer[] in = new Integer[] { 1, 1, 1, 4, 99, 5, 6, 0, 99 };
        Map<Integer, Long> input = new HashMap<>();

        for(int i = 0; i < in.length; i++){
            input.put(i, (long)in[i]);
        }

        IntCodeComputer c = new IntCodeComputer(input, new ArrayList<>());
        c.processCode();
        System.out.println(c.getMemory());
    }
}
