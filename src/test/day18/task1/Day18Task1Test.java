package day18.task1;

import org.junit.Test;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Day18Task1Test {

    @Test
    public void test1() {
        String[] in = new String[]{"#########", "#b.A.@.a#", "#########"};

        Day18Task1 t = new Day18Task1(Arrays.stream(in).collect(Collectors.toList()));
    }

    @Test
    public void test2() {
        String in[] = new String[]{"########################", "#f.D.E.e.C.b.A.@.a.B.c.#",
                "######################.#", "#d.....................#",
                "########################"};

        Day18Task1 t = new Day18Task1(Arrays.stream(in).collect(Collectors.toList()));

    }

    @Test
    public void test3() {
        String in[] = new String[]{"########################",
                "#...............b.C.D.f#",
                "#.######################",
                "#.....@.a.B.c.d.A.e.F.g#",
                "########################"};

        Day18Task1 t = new Day18Task1(Arrays.stream(in).collect(Collectors.toList()));
    }

    @Test
    public void test4() {
        String in[] = new String[]{"#################",
                "#i.G..c...e..H.p#",
                "########.########",
                "#j.A..b...f..D.o#",
                "########@########",
                "#k.E..a...g..B.n#",
                "########.########",
                "#l.F..d...h..C.m#",
                "#################"};

        Day18Task1 t = new Day18Task1(Arrays.stream(in).collect(Collectors.toList()));
    }

    @Test
    public void test5() {
        String in[] = new String[]{"########################",
                "#@..............ac.GI.b#",
                "###d#e#f################",
                "###A#B#C################",
                "###g#h#i################",
                "########################"};

        Day18Task1 t = new Day18Task1(Arrays.stream(in).collect(Collectors.toList()));
    }
}
