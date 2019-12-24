package day24.task1;

import javafx.util.Pair;
import util.InputReader;

import java.util.ArrayList;
import java.util.List;

public class Day24Task1 {

    public static void main(String[] args) {
        List<String> in = InputReader.read("src/day24/task1/input.txt");

        new Day24Task1(in);
    }

    private List<Area> oldStates = new ArrayList<>();

    public Day24Task1(List<String> input) {

        Area first =  parseToArea(input);
        oldStates.add(new Area(first.updateArea()));


        List<Pair<Integer, Integer>> firstTimeSameLayout = null;


        boolean done = false;
        while(!done){
            System.out.println("Step");
            Area nextArea = new Area(oldStates.get(oldStates.size()-1).updateArea());

            for(Area a : oldStates){
                System.out.println("Comparing: ");
                nextArea.print();
                System.out.println("With: ");
                a.print();
                System.out.println("------");

                List<Pair<Integer, Integer>> sameLayout = nextArea.compareToArea(a);

                if(sameLayout != null){
                    done = true;
                    firstTimeSameLayout = sameLayout;
                    System.out.println("Same Layout Done: ");
                    System.out.println(sameLayout);
                    break;
                }
            }

            oldStates.add(nextArea);
        }

        System.out.println("Old States: ");
        for(Area a : oldStates){
            a.print();
            System.out.println("-----");
        }

        long sum = 0;

        for(Pair<Integer, Integer> p : firstTimeSameLayout){
            if(oldStates.get(oldStates.size()-1).isBug(p.getKey(), p.getValue())){
                sum += Math.pow(2, (5 * p.getKey() + p.getValue()));
            }
        }

        System.out.println("Sum: " + sum);

    }

    private Area parseToArea(List<String> l){
        char[][] area = new char[5][5];

        for(int i = 0; i < l.size(); i++){
            area[i] = l.get(i).toCharArray();
        }

        return new Area(area);
    }

    private class Area {
        char[][] fields;

        public Area(char[][] area) {
            this.fields = area;
        }

        public char[][] updateArea() {
            char[][] nextArea = new char[5][5];

            for (int y = 0; y < fields.length; y++) {
                for (int x = 0; x < fields[y].length; x++) {
                    int adjactedBugs = adjactedBugs(y, x);
                    if (isBug(y, x)) {
                        if (adjactedBugs == 1) {
                            nextArea[y][x] = '#';
                        } else {
                            nextArea[y][x] = '.';
                        }
                    } else {
                        if (adjactedBugs == 1 || adjactedBugs == 2) {
                            nextArea[y][x] = '#';
                        } else {
                            nextArea[y][x] = '.';
                        }
                    }
                }
            }

            return nextArea;
        }

        private boolean isBug(int y, int x) {
            if (fields[y][x] == '#') {
                return true;
            }

            return false;
        }

        private int adjactedBugs(int y, int x) {
            int bugCount = 0;

            for (Pair<Integer, Integer> p : getNeighbours(y, x)) {
                if (isBug(p.getKey(), p.getValue())) {
                    bugCount++;
                }
            }

            return bugCount;
        }

        private List<Pair<Integer, Integer>> getNeighbours(int y, int x) {
            List<Pair<Integer, Integer>> neighbours = new ArrayList<>();

            if (y > 0) {
                neighbours.add(new Pair<>(y - 1, x));
            }

            if (y < 4) {
                neighbours.add(new Pair<>(y + 1, x));
            }

            if (x > 0) {
                neighbours.add(new Pair<>(y, x - 1));
            }

            if (x < 4) {
                neighbours.add(new Pair<>(y, x + 1));
            }

            return neighbours;
        }

        private List<Pair<Integer,Integer>> compareToArea(Area a) {
            List<Pair<Integer, Integer>> sameFields = new ArrayList<>();

            for (int y = 0; y < fields.length; y++) {
                for (int x = 0; x < fields[y].length; x++) {
                    if (getField(y,x) == a.getField(y,x)) {
                        sameFields.add(new Pair<>(y, x));
                    }
                }
            }

            List<Pair<Integer, Integer>> overallLayout = null;
            for (Pair<Integer, Integer> i : sameFields) {
                List<Pair<Integer, Integer>> layout;

                layout = checkForAdjectedFields(i.getKey(), i.getValue(), a);

                if (layout != null) {
                    System.out.println("Found Layout");
                    overallLayout = layout;
                }
            }

            return overallLayout;
        }

        private char getField(int y, int x){
            return fields[y][x];
        }

        private List<Pair<Integer, Integer>> checkForAdjectedFields(int y, int x, Area a) {
            List<Pair<Integer, Integer>> neighbours = getNeighbours(y, x);
            List<Pair<Integer, Integer>> sameFields = new ArrayList<>();
            sameFields.add(new Pair<>(y, x));

            for (Pair<Integer, Integer> p : neighbours) {
                if (getField(p.getKey(),p.getValue()) == a.getField(p.getKey(),p.getValue())) {
                    sameFields.add(p);
                }
            }

            if(sameFields.size() == neighbours.size() + 1){
                return sameFields;
            }

            return null;
        }

        private void print(){
            for(int y = 0; y < fields.length; y++){
                for(int x = 0; x < fields[y].length; x++){
                    System.out.print(fields[y][x]);
                }
                System.out.println();
            }
        }
    }
}
