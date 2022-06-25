package day17.task2;

import util.InputReader;
import util.IntCode;
import util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day17Task2 {

    public static void main(String[] args) {
        List<String> inputString = InputReader.read("src/day17/task1/input.txt");

        String[] splitted = inputString.get(0).split(",");

        new Day17Task2(Arrays.stream(splitted).map(Long::parseLong).collect(Collectors.toList()));
    }

    List<String> stringMap = new ArrayList<>();
    char[][] map;
    StringBuilder currLine = new StringBuilder();

    public Day17Task2(List<Long> in) {
        IntCode intCode = new IntCode(in);

        Long result = 0L;

        while (result != null) {
            result = intCode.runCode(true);

            if (result != null) {
                int output = Math.toIntExact(result);

                if (output == 10) {
                    stringMap.add(currLine.toString());
                    currLine = new StringBuilder();
                } else {
                    char ascii = (char) output;
                    currLine.append(ascii);
                }
            }
        }

        transformToArray();

        calculateMovement();

        //Figured out compression by hand
        String input = "A,B,A,C,B,C,A,C,B,C\nL,8,R,10,L,10\nR,10,L,8,L,8,L,10\nL,4,L,6,L,8,L,8\nn\n";

        in.set(0, 2L);
        intCode = new IntCode(in);
        for(char c : input.toCharArray()) {
            intCode.addToInput(c);
        }

        intCode.runCode(false);
    }

    private void transformToArray() {
        map = new char[stringMap.size()][stringMap.get(0).length()];

        for (int i = 0; i < stringMap.size(); i++) {
            char[] chars = stringMap.get(i).toCharArray();

            map[i] = chars;
        }

    }

    private void calculateMovement() {
        boolean calculated = false;
        Pair<Pair<Integer, Integer>, Direction> findRobot = findRobot();

        List<String> instructions = new ArrayList<>();

        Pair<Integer, Integer> currentPosition = findRobot.getKey();
        Direction direction = findRobot.getValue();

        while (!calculated) {
            int steps = 0;
            Pair<Integer, Integer> nextBlock =
                    direction.getNextBlock(currentPosition.getKey(), currentPosition.getValue());

            while (isStructure(nextBlock.getKey(), nextBlock.getValue())) {
                steps++;
                currentPosition = nextBlock;
                nextBlock = direction.getNextBlock(currentPosition.getKey(),
                        currentPosition.getValue());
            }

            if (steps > 0) {
                instructions.add(String.valueOf(steps));
            }

            Direction nextDirection =
                    findNewDirection(currentPosition.getKey(), currentPosition.getValue(),
                            direction);

            if (nextDirection == null) {
                calculated = true;
            } else if (nextDirection == Direction.LEFT) {
                direction = direction.left;
                instructions.add("L");
            } else if (nextDirection == Direction.RIGHT) {
                direction = direction.right;
                instructions.add("R");
            }
        }

        System.out.println("Calculated: " + instructions);
    }

    private Direction findNewDirection(int x, int y, Direction currDirection) {
        Direction testRight = currDirection.right;
        Direction testLeft = currDirection.left;

        Pair<Integer, Integer> nextBlock = testRight.getNextBlock(x, y);

        if (isStructure(nextBlock.getKey(), nextBlock.getValue())) {
            return Direction.RIGHT;
        } else {
            nextBlock = testLeft.getNextBlock(x, y);

            if (isStructure(nextBlock.getKey(), nextBlock.getValue())) {
                return Direction.LEFT;
            }
        }

        return null;
    }

    private Pair<Pair<Integer, Integer>, Direction> findRobot() {
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                char atPos = map[y][x];
                if (atPos != '#' && atPos != '.') {
                    Pair<Integer, Integer> position = new Pair<>(x, y);
                    Direction direction = null;

                    switch (atPos) {
                        case '^':
                            direction = Direction.UP;
                            break;
                        case '<':
                            direction = Direction.LEFT;
                            break;
                        case '>':
                            direction = Direction.RIGHT;
                            break;
                        case 'v':
                            direction = Direction.DOWN;
                            break;
                    }

                    return new Pair<>(position, direction);
                }
            }
        }
        return null;
    }

    enum Direction {
        UP, DOWN, LEFT, RIGHT;

        static {
            UP.left = LEFT;
            UP.right = RIGHT;
            DOWN.right = LEFT;
            DOWN.left = RIGHT;
            LEFT.left = DOWN;
            LEFT.right = UP;
            RIGHT.left = UP;
            RIGHT.right = DOWN;
        }

        Direction left;
        Direction right;

        Pair<Integer, Integer> getNextBlock(int x, int y) {
            switch (this) {
                case UP:
                    return new Pair<>(x, y - 1);
                case DOWN:
                    return new Pair<>(x, y + 1);
                case LEFT:
                    return new Pair<>(x - 1, y);
                case RIGHT:
                    return new Pair<>(x + 1, y);
            }

            return null;
        }
    }

    private boolean isStructure(int x, int y) {
        if (x >= 0 && x < map[0].length && y >= 0 && y < map.length - 1) {
            return map[y][x] == '#';
        } else {
            return false;
        }
    }

}
