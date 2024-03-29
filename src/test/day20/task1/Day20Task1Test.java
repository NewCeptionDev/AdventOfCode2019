package day20.task1;

import org.junit.Test;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Day20Task1Test {

    @Test public void test1() {
        String[] in = new String[] { "         A           ", "         A           ",
                "  #######.#########  ", "  #######.........#  ", "  #######.#######.#  ",
                "  #######.#######.#  ", "  #######.#######.#  ", "  #####  B    ###.#  ",
                "BC...##  C    ###.#  ", "  ##.##       ###.#  ", "  ##...DE  F  ###.#  ",
                "  #####    G  ###.#  ", "  #########.#####.#  ", "DE..#######...###.#  ",
                "  #.#########.###.#  ", "FG..#########.....#  ", "  ###########.#####  ",
                "             Z       ", "             Z       " };

        Day20Task1 t = new Day20Task1(Arrays.stream(in).collect(Collectors.toList()));
    }
    
    @Test public void test2() {
        String[] in = new String[] {"                   A               "
                , "                   A               " , "  #################.#############  "
                , "  #.#...#...................#.#.#  " , "  #.#.#.###.###.###.#########.#.#  "
                , "  #.#.#.......#...#.....#.#.#...#  " , "  #.#########.###.#####.#.#.###.#  "
                , "  #.............#.#.....#.......#  " , "  ###.###########.###.#####.#.#.#  "
                , "  #.....#        A   C    #.#.#.#  " , "  #######        S   P    #####.#  "
                , "  #.#...#                 #......VT" , "  #.#.#.#                 #.#####  "
                , "  #...#.#               YN....#.#  " , "  #.###.#                 #####.#  "
                , "DI....#.#                 #.....#  " , "  #####.#                 #.###.#  "
                , "ZZ......#               QG....#..AS" , "  ###.###                 #######  "
                , "JO..#.#.#                 #.....#  " , "  #.#.#.#                 ###.#.#  "
                , "  #...#..DI             BU....#..LF" , "  #####.#                 #.#####  "
                , "YN......#               VT..#....QG" , "  #.###.#                 #.###.#  "
                , "  #.#...#                 #.....#  " , "  ###.###    J L     J    #.#.###  "
                , "  #.....#    O F     P    #.#...#  " , "  #.###.#####.#.#####.#####.###.#  "
                , "  #...#.#.#...#.....#.....#.#...#  " , "  #.#####.###.###.#.#.#########.#  "
                , "  #...#.#.....#...#.#.#.#.....#.#  " , "  #.###.#####.###.###.#.#.#######  "
                , "  #.#.........#...#.............#  " , "  #########.###.###.#############  "
                , "           B   J   C               " , "           U   P   P               "};

        Day20Task1 t = new Day20Task1(Arrays.stream(in).collect(Collectors.toList()));
    }

}
