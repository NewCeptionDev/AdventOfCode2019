package day10.task1;

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
            int found = detectableAsteroids(a);

            if (found > max) {
                max = found;
                best = a;
            }
        }

        System.out.println(max);
        System.out.println(best.x + ":" + best.y);
    }

    private int detectableAsteroids(Field a) {
        int detectable = 0;
        int sum = 0;

        Field nextField = a.getUpperField();

        while (nextField != null) {
            if (nextField.isAsteroid()) {
                detectable++;
            }

            nextField = nextField.getUpperField();
        }

        detectable = detectable > 0 ? detectable - 1 : detectable;
        sum += detectable;
        detectable = 0;

        nextField = a.getLowerField();

        while (nextField != null) {
            if (nextField.isAsteroid()) {
                detectable++;
            }

            nextField = nextField.getLowerField();
        }

        detectable = detectable > 0 ? detectable - 1 : detectable;
        sum += detectable;
        detectable = 0;

        nextField = a.getLeftField();

        while (nextField != null) {
            if (nextField.isAsteroid()) {
                detectable++;
            }

            nextField = nextField.getLeftField();
        }

        detectable = detectable > 0 ? detectable - 1 : detectable;
        sum += detectable;
        detectable = 0;

        nextField = a.getRightField();

        while (nextField != null) {
            if (nextField.isAsteroid()) {
                detectable++;
            }

            nextField = nextField.getRightField();
        }

        detectable = detectable > 0 ? detectable - 1 : detectable;
        sum += detectable;
        detectable = 0;

        nextField = a.getLeftLowerField();

        while (nextField != null) {
            if (nextField.isAsteroid()) {
                detectable++;
            }

            nextField = nextField.getLeftLowerField();
        }

        detectable = detectable > 0 ? detectable - 1 : detectable;
        sum += detectable;
        detectable = 0;

        nextField = a.getLeftUpperField();

        while (nextField != null) {
            if (nextField.isAsteroid()) {
                detectable++;
            }

            nextField = nextField.getLeftUpperField();
        }

        detectable = detectable > 0 ? detectable - 1 : detectable;
        sum += detectable;
        detectable = 0;

        nextField = a.getRightLowerField();

        while (nextField != null) {
            if (nextField.isAsteroid()) {
                detectable++;
            }

            nextField = nextField.getRightLowerField();
        }

        detectable = detectable > 0 ? detectable - 1 : detectable;
        sum += detectable;
        detectable = 0;

        nextField = a.getRightUpperField();

        while (nextField != null) {
            if (nextField.isAsteroid()) {
                detectable++;
            }

            nextField = nextField.getUpperField();
        }

        detectable = detectable > 0 ? detectable - 1 : detectable;
        sum += detectable;
        detectable = 0;

        return asteroids.size() - sum;
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

        private Field getUpperField() {
            if (y - 1 >= 0) {
                return fields[y - 1][x];
            }
            return null;
        }

        private Field getLowerField() {
            if (y + 1 < fields.length) {
                return fields[y + 1][x];
            }
            return null;
        }

        private Field getRightField() {
            if (x + 1 < fields[0].length) {
                return fields[y][x + 1];
            }
            return null;
        }

        private Field getLeftField() {
            if (x - 1 >= 0) {
                return fields[y][x - 1];
            }
            return null;
        }

        private Field getRightUpperField() {
            if (x + 1 < fields[0].length && y - 1 >= 0) {
                return fields[y - 1][x + 1];
            }
            return null;
        }

        private Field getRightLowerField() {
            if (x + 1 < fields[0].length && y + 1 < fields.length) {
                return fields[y + 1][x + 1];
            }
            return null;
        }

        private Field getLeftUpperField() {
            if (x - 1 >= 0 && y - 1 >= 0) {
                return fields[y - 1][x - 1];
            }
            return null;
        }

        private Field getLeftLowerField() {
            if (x - 1 >= 0 && y + 1 < fields.length) {
                return fields[y + 1][x - 1];
            }
            return null;
        }
    }

}
