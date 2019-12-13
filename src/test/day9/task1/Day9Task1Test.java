package day9.task1;

import org.junit.Test;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Day9Task1Test {


    @Test
    public void testing1(){
        String in = "104,1125899906842624,99";
        String[] l = in.split(",");

        Day9Task1 t = new Day9Task1(Arrays.stream(l).map(Long::parseLong).collect(Collectors.toList()));
    }
}
