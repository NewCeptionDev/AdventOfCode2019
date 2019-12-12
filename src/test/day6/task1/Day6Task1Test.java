package day6.task1;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class Day6Task1Test {

    @Test
    public void testing() {
        String[] in =
                new String[] { "COM)B", "B)C", "C)D", "D)E", "E)F", "B)G", "G)H", "D)I", "E)J",
                        "J)K", "K)L", "Z)X", "B)Z"};
        Day6Task1 t = new Day6Task1(Arrays.asList(in));
        t.showDependencies();

        assertEquals(47, t.getAllChildren());
    }
}
