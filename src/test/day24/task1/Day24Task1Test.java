package day24.task1;

import org.junit.Test;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Day24Task1Test {

    @Test
    public void test1() {
        String[] in = new String[]{
                "....#",
                "#..#.",
                "#..##",
                "..#..",
                "#...."
        };

        new Day24Task1(Arrays.stream(in).collect(Collectors.toList()));
    }
}
