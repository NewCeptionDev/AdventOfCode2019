package day18.task1;

import util.Pair;
import util.InputReader;

import java.util.*;

public class Day18Task1 {

    public static void main(String[] args) {
        List<String> in = InputReader.read("src/day18/task1/input.txt");

        new Day18Task1(in);
    }

    private char[][] field;
    private Map<Character, Pair<Integer, Integer>> keys;
    private Map<Character, List<Pair<Integer, Integer>>> currentBestWay = new HashMap<>();
    private Map<Pair<Integer, Integer>, Pair<Map<Character, Set<Character>>, Map<Character, List<Pair<Integer, Integer>>>>> savedBestWays = new HashMap<>();

    public Day18Task1(List<String> in) {
        field = new char[in.size()][];
        for (int i = 0; i < in.size(); i++) {
            field[i] = in.get(i).toCharArray();
        }

        keys = getAllKeys();

        print();

        for(Character c : keys.keySet()){
            System.out.println("Char " + c + ": " + keys.get(c));
        }

        System.out.println(simulateRun(keys.keySet(), new ArrayList<>(), getStartPos(), 0));
    }

    private int simulateRun(Set<Character> toDo, List<Character> collected,
            Pair<Integer, Integer> pos, int posMoved) {
        //System.out.println("New Simulation Step. TODO Size: " + toDo.size());
        //System.out.println("Current Steps: " + posMoved);

        //TODO Save old Calculations to reuse
        int minSteps = Integer.MAX_VALUE;
        if (toDo.size() > 0) {
            Map<Character, Set<Character>> possible;
            Map<Character, List<Pair<Integer, Integer>>> bestWaysForMethod;
            Pair<Map<Character, Set<Character>>, Map<Character, List<Pair<Integer, Integer>>>> oldMoves = getValuesFromPair(pos);
            if (oldMoves == null) {
                possible = calculateLocksInFrontofKeys(pos, toDo);
                bestWaysForMethod = new HashMap<>(currentBestWay);
                savedBestWays.put(new Pair<>(pos.getKey(), pos.getValue()), new Pair<>(possible, bestWaysForMethod));
                System.out.println("explored new");
            } else {
                System.out.println("Found old State");
                possible = oldMoves.getKey();
                for(Character c : collected){
                   possible.remove((char)(c+32));
                }
                bestWaysForMethod = oldMoves.getValue();
            }

            List<Character> toRemove = new ArrayList<>();

            for (Character c : possible.keySet()) {
                for(Character col : collected){
                    possible.get(c).remove((char)(col-32));
                }

                if (possible.get(c).size() > 0) {
                    //System.out.println("Lock found, Removing");
                    //System.out.println(possible.get(c));
                    toRemove.add(c);
                }
            }

            for(Character c : toRemove){
                possible.remove(c);
            }

            if(possible.size() == 0){
                //System.out.println("No possible Steps found.");
            }

            for (Character c : possible.keySet()) {
                Set<Character> newToDo = new HashSet<>(toDo);
                newToDo.remove(c);
                List<Character> newCollected = new ArrayList<>(collected);
                newCollected.addAll(getAllKeysOnWay(c, bestWaysForMethod.get(c)));

                int steps = simulateRun(newToDo, newCollected, keys.get(c),
                        posMoved + bestWaysForMethod.get(c).size());

                if (minSteps > steps) {
                    minSteps = steps;
                }
            }
        } else {
            return posMoved;
        }

        return minSteps;
    }

    private Pair<Map<Character, Set<Character>>, Map<Character, List<Pair<Integer, Integer>>>> getValuesFromPair(Pair<Integer, Integer> pair){
        for(Pair<Integer, Integer> inList : savedBestWays.keySet()){
            if(inList.getKey().equals(pair.getKey()) && inList.getValue().equals(pair.getValue())){
                return savedBestWays.get(inList);
            }
        }

        return null;
    }

    private List<Character> getAllKeysOnWay(char key, List<Pair<Integer, Integer>> bestWays) {
        List<Character> keys = new ArrayList<>();

        for (Pair<Integer, Integer> p : bestWays) {
            if (isKey(p.getKey(), p.getValue())) {
                keys.add(getField(p.getKey(), p.getValue()));
            }
        }

        return keys;
    }

    private Map<Character, Set<Character>> calculateLocksInFrontofKeys(
            Pair<Integer, Integer> startPos, Set<Character> toDoKeys) {
        Map<Character, Set<Character>> possible = new HashMap<>();
        currentBestWay = new HashMap<>();

        for (Character c : toDoKeys) {
            List<Pair<Integer, Integer>> bestWay = new ArrayList<>();
            List<Pair<List<Pair<Integer, Integer>>, Set<Character>>> possibleWays =
                    new ArrayList<>();
            findLocks(c, startPos, null, new ArrayList<>(), bestWay, possibleWays, new HashSet<>());

            Pair<List<Pair<Integer, Integer>>, Set<Character>> best = null;
            for (Pair<List<Pair<Integer, Integer>>, Set<Character>> p : possibleWays) {
                if (best == null) {
                    best = p;
                } else {
                    if (best.getKey().size() > p.getKey().size()) {
                        best = p;
                    }
                }
            }

            possible.put(c, best.getValue());

            List<Pair<Integer, Integer>> removed = best.getKey().subList(1, best.getKey().size());
            currentBestWay.put(c,  best.getKey());
        }
        return possible;
    }

    private Pair<Integer, Integer> getStartPos() {
        for (int y = 0; y < field.length; y++) {
            for (int x = 0; x < field[y].length; x++) {
                if (getField(y, x) == '@') {
                    return new Pair<>(y, x);
                }
            }
        }

        return null;
    }

    private Map<Character, Pair<Integer, Integer>> getAllKeys() {
        Map<Character, Pair<Integer, Integer>> results = new HashMap<>();

        for (int y = 0; y < field.length; y++) {
            for (int x = 0; x < field[y].length; x++) {
                if (isKey(y, x)) {
                    results.put(getField(y, x), new Pair<>(y, x));
                }
            }
        }

        return results;
    }

    private void findLocks(char key, Pair<Integer, Integer> startPos,
            Pair<Integer, Integer> lastPos, List<Pair<Integer, Integer>> visited,
            List<Pair<Integer, Integer>> way,
            List<Pair<List<Pair<Integer, Integer>>, Set<Character>>> solutions,
            Set<Character> currLocks) {

        List<Pair<Integer, Integer>> possibleMoves = getPossibleMoves(startPos.getKey(), startPos.getValue(), lastPos);

        for (Pair<Integer, Integer> p : possibleMoves) {
            if (!visited.contains(p)) {
                List<Pair<Integer, Integer>> visitedClone = new ArrayList<>(visited);
                visitedClone.add(p);

                if (isKey(p.getKey(), p.getValue())) {
                    if (getField(p.getKey(), p.getValue()) == key) {
                        way.add(p);
                        solutions.add(new Pair<>(way, currLocks));
                    }
                } else if (isLock(p.getKey(), p.getValue())) {
                    currLocks.add(getField(p.getKey(), p.getValue()));
                }

                if (!isWall(p.getKey(), p.getValue())) {
                    Set<Character> currLocksClone = new HashSet<>(currLocks);
                    List<Pair<Integer, Integer>> wayClone = new ArrayList<>(way);
                    wayClone.add(p);

                    findLocks(key, new Pair<>(p.getKey(), p.getValue()), startPos, visitedClone,
                            wayClone, solutions, currLocksClone);
                }
            }
        }
    }

    private List<Pair<Integer, Integer>> getPossibleMoves(int y, int x,
            Pair<Integer, Integer> lastPos) {
        List<Pair<Integer, Integer>> moves = new ArrayList<>();

        if (y > 0) {
            moves.add(new Pair<>(y - 1, x));
        }

        if (y < field.length - 1) {
            moves.add(new Pair<>(y + 1, x));
        }

        if (x > 0) {
            moves.add(new Pair<>(y, x - 1));
        }

        if (x < field[0].length - 1) {
            moves.add(new Pair<>(y, x + 1));
        }

        List<Pair<Integer, Integer>> walls = new ArrayList<>();

        for (Pair<Integer, Integer> move : moves) {
            if (isWall(move.getKey(), move.getValue())) {
                walls.add(move);
            }

            if (lastPos != null) {
                if (move.getKey().equals(lastPos.getKey()) && move.getValue()
                        .equals(lastPos.getValue())) {
                    walls.add(move);
                }
            }
        }

        for (Pair<Integer, Integer> i : walls) {
            moves.remove(i);
        }

        return moves;
    }

    private boolean isWall(int y, int x) {
        return getField(y, x) == '#';
    }

    private boolean isSpace(int y, int x) {
        return getField(y, x) == '.' || getField(y,x) == '@';
    }

    private boolean isLock(int y, int x) {
        char val = getField(y, x);

        return val >= 65 && val <= 90;
    }

    private boolean isKey(int y, int x) {
        char val = getField(y, x);

        return val >= 97 && val <= 122;
    }

    private char getField(int y, int x) {
        return field[y][x];
    }

    public void print() {
        for (int y = 0; y < field.length; y++) {
            for (int x = 0; x < field[y].length; x++) {
                System.out.print(field[y][x]);
            }
            System.out.println();
        }
    }
}
