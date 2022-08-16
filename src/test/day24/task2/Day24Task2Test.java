package day24.task2;

import org.junit.Test;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Day24Task2Test {

    @Test
    public void test1() {
        String[] in = new String[]{
                "....#",
                "#..#.",
                "#..##",
                "..#..",
                "#...."
        };

        new Day24Task2(Arrays.stream(in).collect(Collectors.toList()), 10);
    }

}
