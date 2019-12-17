package day17.task2;

import javafx.util.Pair;
import util.InputReader;
import util.IntCodeComputer;

import java.util.*;
import java.util.stream.Collectors;

public class Day17Task2 {

    //TODO VisitedBlocks not used

    public static void main(String[] args) {
        //TODO UPDATE INPUT
        List<String> inputString = InputReader.read("src/day17/task1/input.txt");

        String[] splitted = inputString.get(0).split(",");

        new Day17Task2(Arrays.stream(splitted).map(Integer::parseInt).collect(Collectors.toList()));
    }

    List<String> stringMap = new ArrayList<>();
    char[][] map;
    Map<Pair<Integer, Integer>, Boolean> visitedBlocks = new HashMap<>();
    StringBuilder currLine = new StringBuilder();
    int structureBlocks = 0;

    public Day17Task2(List<Integer> in) {
        Map<Integer, Long> computerInput = new HashMap<>();

        for (int i = 0; i < in.size(); i++) {
            computerInput.put(i, (long) in.get(i));
        }

        IntCodeComputer c = new IntCodeComputer(computerInput, new ArrayList<>());

        while (!c.isRealDone()) {
            c.updateInputs(new ArrayList<>());
            c.processCode();

            if (!c.isRealDone()) {
                int output = Math.toIntExact(c.getOutputs().get(0));

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
        structureBlocks = scanMapForStructure();
        calculateMovement();
    }

    private void calculateMovement() {
        boolean calculated = false;
        Pair<Pair<Integer, Integer>, Direction> findRobot = findRobot();

        List<String> instructions = new ArrayList<>();

        Pair<Integer, Integer> currentPosition = findRobot.getKey();
        Direction direction = findRobot.getValue();

        while (!calculated) {
            int steps = 0;
            Pair<Integer, Integer> nextBlock = direction.getNextBlock(currentPosition.getValue(), currentPosition.getKey());

            while(isStructure(nextBlock.getKey(), nextBlock.getValue())){
                steps++;
                nextBlock = direction.getNextBlock(currentPosition.getValue(), currentPosition.getKey());
            }

            if(steps > 0){
                instructions.add(String.valueOf(steps));
            }

            Direction nextDirection = findNewDirection(currentPosition.getValue(), currentPosition.getKey(), direction);

            if(nextDirection == null){
                if (visitedBlocks.size() != structureBlocks) {
                    System.err.println("Stuck at Endpoint");
                }
                calculated = true;
            } else if(nextDirection == Direction.LEFT){
                direction = direction.left;
                instructions.add("L");
            } else if(nextDirection == Direction.RIGHT){
                direction = direction.right;
                instructions.add("R");
            }
        }

        System.out.println("Calculated: " + instructions);
    }

    private Direction findNewDirection(int x, int y, Direction currDirection){
        Direction testRight = currDirection.right;
        Direction testLeft = currDirection.left;

        Pair<Integer, Integer> nextBlock = testRight.getNextBlock(x, y);

        if(isStructure(nextBlock.getKey(),nextBlock.getValue()) && !visitedBlocks.containsKey(new Pair<>(nextBlock.getValue(),nextBlock.getKey()))){
            return Direction.RIGHT;
        } else {
            nextBlock = testLeft.getNextBlock(x, y);

            if(isStructure(nextBlock.getKey(),nextBlock.getValue()) && !visitedBlocks.containsKey(new Pair<>(nextBlock.getValue(),nextBlock.getKey()))){
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
                    Pair<Integer, Integer> position = new Pair<>(y, x);
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

    private int scanMapForStructure() {
        int structuresFound = 0;
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[0].length; x++) {
                if (isStructure(x, y)) {
                    structuresFound++;
                }
            }
        }

        return structuresFound;
    }

    private void transformToArray() {
        map = new char[stringMap.size()][stringMap.get(0).length()];

        for (int i = 0; i < stringMap.size(); i++) {
            char[] chars = stringMap.get(i).toCharArray();

            map[i] = chars;
        }

    }

    private List<Pair<Integer, Integer>> getAlignments() {
        List<Pair<Integer, Integer>> alignments = new ArrayList<>();

        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                if (isIntersection(x, y)) {
                    alignments.add(new Pair<>(y, x));
                }
            }
        }

        return alignments;
    }

    private boolean isIntersection(int x, int y) {
        if (x > 0 && x < map[0].length - 1) {
            if (y > 0 && y < map.length - 2) {

                boolean isIntersection = true;
                if (!isStructure(x - 1, y)) {
                    isIntersection = false;
                }

                if (isIntersection && !isStructure(x + 1, y)) {
                    isIntersection = false;
                }

                if (isIntersection && !isStructure(x, y + 1)) {
                    isIntersection = false;
                }

                if (isIntersection && !isStructure(x, y - 1)) {
                    isIntersection = false;
                }

                return isIntersection;
            }
        }

        return false;
    }

    private boolean isStructure(int x, int y) {
        if (x >= 0 && x < map[0].length && y >= 0 && y < map.length-1) {
            return map[y][x] == '#';
        } else {
            return false;
        }
    }

}
