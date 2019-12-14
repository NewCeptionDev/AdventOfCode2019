package day12.task1;

import org.junit.Test;

public class Day12Task1Test {

    @Test
    public void test1() {
        String[] arr = new String[] { "<x=-1, y=0, z=2>", "<x=2, y=-10, z=-7>", "<x=4, y=-8, z=8>",
                "<x=3, y=5, z=-1>" };

        Day12Task1 t = new Day12Task1(arr, 10);
    }
}
