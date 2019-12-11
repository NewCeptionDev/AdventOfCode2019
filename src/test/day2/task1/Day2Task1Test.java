package day2.task1;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class Day2Task1Test {

    @Test void processCode() {
        Integer[] in = new Integer[] { 1, 0, 0, 0, 99 };
        Integer[] exp = new Integer[] { 2, 0, 0, 0, 99 };
        Day2Task1 t = new Day2Task1(in);
        assertArrayEquals(exp, t.processCode().toArray());
    }

    @Test void processCode2() {
        Integer[] in = new Integer[] { 2, 3, 0, 3, 99 };
        Integer[] exp = new Integer[] { 2, 3, 0, 6, 99 };
        Day2Task1 t = new Day2Task1(in);
        assertArrayEquals(exp, t.processCode().toArray());
    }

    @Test void processCode3() {
        Integer[] in = new Integer[] { 2, 4, 4, 5, 99, 0 };
        Integer[] exp = new Integer[] { 2, 4, 4, 5, 99, 9801 };
        Day2Task1 t = new Day2Task1(in);
        assertArrayEquals(exp, t.processCode().toArray());
    }

    @Test void processCode4() {
        Integer[] in = new Integer[] { 1, 1, 1, 4, 99, 5, 6, 0, 99 };
        Integer[] exp = new Integer[] { 30, 1, 1, 4, 2, 5, 6, 0, 99 };
        Day2Task1 t = new Day2Task1(in);
        assertArrayEquals(exp, t.processCode().toArray());
    }
}
