package day3.task2;

import org.junit.Test;

import static org.junit.Assert.*;

public class Day3Task2Test {

    @Test
    public void testSteps1(){
        String l1 = "R75,D30,R83,U83,L12,D49,R71,U7,L72";
        String l2 = "U62,R66,U55,R34,D71,R55,D58,R83";

        String[] l1Array = l1.split(",");
        String[] l2Array = l2.split(",");

        Day3Task2 t = new Day3Task2(l1Array, l2Array);

        assertEquals(610, t.testing(l1Array, l2Array));
    }

    @Test
    public void testSteps2(){
        String l1 = "R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51";
        String l2 = "U98,R91,D20,R16,D67,R40,U7,R15,U6,R7";

        String[] l1Array = l1.split(",");
        String[] l2Array = l2.split(",");

        Day3Task2 t = new Day3Task2(l1Array, l2Array);

        assertEquals(410, t.testing(l1Array, l2Array));
    }

}
