package day6.task2;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class Day6Task2Test {

    @Test
    public void testing() {
        String[] in =
                new String[] { "COM)B", "B)C", "C)D", "D)E", "E)F", "B)G", "G)H", "D)I", "E)J",
                        "J)K", "K)L", "K)YOU", "I)SAN" };
        Day6Task2 t = new Day6Task2(Arrays.asList(in));
        t.showDependencies();

        assertEquals(4, t.findCrossPoint());
    }
}
