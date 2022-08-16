package day20.task2;

import org.junit.Test;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Day20Task2Test {
    @Test public void test1() {
        String[] in = new String[] {"             Z L X W       C                 "
                , "             Z P Q B       K                 "
                , "  ###########.#.#.#.#######.###############  "
                , "  #...#.......#.#.......#.#.......#.#.#...#  "
                , "  ###.#.#.#.#.#.#.#.###.#.#.#######.#.#.###  "
                , "  #.#...#.#.#...#.#.#...#...#...#.#.......#  "
                , "  #.###.#######.###.###.#.###.###.#.#######  "
                , "  #...#.......#.#...#...#.............#...#  "
                , "  #.#########.#######.#.#######.#######.###  "
                , "  #...#.#    F       R I       Z    #.#.#.#  "
                , "  #.###.#    D       E C       H    #.#.#.#  "
                , "  #.#...#                           #...#.#  "
                , "  #.###.#                           #.###.#  "
                , "  #.#....OA                       WB..#.#..ZH"
                , "  #.###.#                           #.#.#.#  "
                , "CJ......#                           #.....#  "
                , "  #######                           #######  "
                , "  #.#....CK                         #......IC"
                , "  #.###.#                           #.###.#  "
                , "  #.....#                           #...#.#  "
                , "  ###.###                           #.#.#.#  "
                , "XF....#.#                         RF..#.#.#  "
                , "  #####.#                           #######  "
                , "  #......CJ                       NM..#...#  "
                , "  ###.#.#                           #.###.#  "
                , "RE....#.#                           #......RF"
                , "  ###.###        X   X       L      #.#.#.#  "
                , "  #.....#        F   Q       P      #.#.#.#  "
                , "  ###.###########.###.#######.#########.###  "
                , "  #.....#...#.....#.......#...#.....#.#...#  "
                , "  #####.#.###.#######.#######.###.###.#.#.#  "
                , "  #.......#.......#.#.#.#.#...#...#...#.#.#  "
                , "  #####.###.#####.#.#.#.#.###.###.#.###.###  "
                , "  #.......#.....#.#...#...............#...#  "
                , "  #############.#.#.###.###################  "
                , "               A O F   N                     "
                , "               A A D   M                     "};

        Day20Task2 t = new Day20Task2(Arrays.stream(in).collect(Collectors.toList()));
    }
}