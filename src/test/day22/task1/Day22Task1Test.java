package day22.task1;

import org.junit.Test;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Day22Task1Test {

    @Test
    public void test1(){
        String[] in = new String[]{
                "deal with increment 7",
                "deal into new stack",
                "deal into new stack"
        };

        new Day22Task1(Arrays.stream(in).collect(Collectors.toList()), 10);
    }

    @Test
    public void test0(){
        String[] in = new String[]{
                "deal with increment 3",
        };

        new Day22Task1(Arrays.stream(in).collect(Collectors.toList()), 10);
    }

    @Test
    public void test2(){
        String[] in = new String[]{
                "cut 6",
                "deal with increment 7",
                "deal into new stack"
        };

        new Day22Task1(Arrays.stream(in).collect(Collectors.toList()), 10);
    }

    @Test
    public void test3(){
        String[] in = new String[]{
                "deal with increment 7",
                "deal with increment 9",
                "cut -2"
        };

        new Day22Task1(Arrays.stream(in).collect(Collectors.toList()), 10);
    }

    @Test
    public void test4(){
        String[] in = new String[]{
                "deal into new stack",
                "cut -2",
                "deal with increment 7",
                "cut 8",
                "cut -4",
                "deal with increment 7",
                "cut 3",
                "deal with increment 9",
                "deal with increment 3",
                "cut -1"
        };

        new Day22Task1(Arrays.stream(in).collect(Collectors.toList()), 10);
    }

}
