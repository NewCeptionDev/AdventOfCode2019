package day10.task1;

import util.Pair;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Day10Task1 {

    private Field[][] fields;
    private List<Field> asteroids = new ArrayList<>();

    public static void main(String[] args) {
        List<String> lines = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/day10/task1/input.txt"));
            String line = reader.readLine();

            while (line != null) {
                lines.add(line);
                line = reader.readLine();
            }

            String[] input = new String[lines.size()];
            for (int i = 0; i < input.length; i++) {
                input[i] = lines.get(i);
            }

            new Day10Task1(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Day10Task1(String[] field) {
        fields = new Field[field.length][field[0].length()];
        for (int y = 0; y < field.length; y++) {

            String[] splitted = field[y].split("");
            for (int x = 0; x < splitted.length; x++) {
                Field n = new Field(x, y, splitted[x].equals("#"));
                fields[y][x] = n;

                if (n.isAsteroid()) {
                    asteroids.add(n);
                }
            }
        }

        int max = Integer.MIN_VALUE;
        Field best = null;

        for (Field a : asteroids) {
            int found = detectAsteroids(a.x, a.y);

            if (found > max) {
                max = found;
                best = a;
            }
        }

        System.out.println(max);
        System.out.println(best.x + ":" + best.y);
    }

    private int detectAsteroids(int x, int y) {
        List<Pair<Integer, Integer>> list = new ArrayList<>();
        List<Pair<Integer, Integer>> realList = new ArrayList<>();

        for (Field f : asteroids) {
            if (f.x != x || f.y != y) {
                Pair<Integer, Integer> p = new Pair<>(x - f.x, y - f.y);
                list.add(p);
            }
        }

        for (Pair<Integer, Integer> p : list) {
            boolean singular = true;

            if (!p.getValue().equals(p.getKey())  && p.getValue() != 0 && p.getKey() != 0) {
                int min = Math.min(Math.abs(p.getValue()), Math.abs(p.getKey()));

                for (int i = 2; i <= min && singular; i++) {
                    if (p.getKey() % i == 0 && p.getValue() % i == 0) {

                        List<Pair<Integer, Integer>> toTest = new ArrayList<>();
                        for (int j = 1; j < i; j++) {
                            Pair<Integer, Integer> p3 =
                                    new Pair<>((p.getKey() / i) * j, (p.getValue() / i) * j);
                            toTest.add(p3);
                        }

                        for (Pair<Integer, Integer> p3 : toTest) {
                            for (Pair<Integer, Integer> p2 : list) {
                                if (p2.getKey().equals(p3.getKey()) && p2.getValue()
                                        .equals(p3.getValue())) {
                                    singular = false;
                                }
                            }
                        }
                    }
                }
            } else if(p.getValue().equals(p.getKey())){
                if(p.getKey() < 0){
                    for (int i = -1; i > p.getKey(); i--) {
                        for (Pair<Integer, Integer> p2 : list) {
                            if (p2.getKey() == i && p2.getValue() == i) {
                                singular = false;
                            }
                        }
                    }
                } else if(p.getKey() > 0){
                    for (int i = 1; i < p.getKey(); i++) {
                        for (Pair<Integer, Integer> p2 : list) {
                            if (p2.getKey() == i && p2.getValue() == i) {
                                singular = false;
                            }
                        }
                    }
                }
            }
            else {
                if (p.getKey() < 0) {
                    for (int i = -1; i > p.getKey(); i--) {
                        for (Pair<Integer, Integer> p2 : list) {
                            if (p2.getKey() == i && p2.getValue().equals(p.getValue())) {
                                singular = false;
                            }
                        }
                    }
                } else if(p.getKey() > 0){
                    for (int i = 1; i < p.getKey(); i++) {
                        for (Pair<Integer, Integer> p2 : list) {
                            if (p2.getKey() == i && p2.getValue().equals(p.getValue())) {
                                singular = false;
                            }
                        }
                    }
                } else if (p.getValue() < 0) {
                    for (int i = -1; i > p.getValue(); i--) {
                        for (Pair<Integer, Integer> p2 : list) {
                            if (p2.getKey().equals(p.getKey()) && p2.getValue() == i) {
                                singular = false;
                            }
                        }
                    }
                } else if(p.getValue() > 0){
                    for (int i = 1; i < p.getValue(); i++) {
                        for (Pair<Integer, Integer> p2 : list) {
                            if (p2.getKey().equals(p.getKey()) && p2.getValue() == i) {
                                singular = false;
                            }
                        }
                    }
                }
            }

            if (singular) {
                realList.add(p);
            }
        }

        return realList.size();
    }

    private class Field {
        private int x;
        private int y;
        private boolean asteroid;

        public Field(int x, int y, boolean asteroid) {
            this.x = x;
            this.y = y;
            this.asteroid = asteroid;
        }

        public boolean isAsteroid() {
            return asteroid;
        }
    }

}
