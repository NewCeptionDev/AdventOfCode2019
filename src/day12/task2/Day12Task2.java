package day12.task2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Day12Task2 {

    private List<Moon> moons = new ArrayList<>();

    public static void main(String[] args) {
        try {
            List<String> lines = new ArrayList<>();

            BufferedReader reader = new BufferedReader(new FileReader("src/day12/task1/input.txt"));
            String line = reader.readLine();

            while (line != null) {
                lines.add(line);
                line = reader.readLine();
            }

            new Day12Task2(lines.stream().toArray(String[]::new));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private List<Integer> updateVelocities(List<Integer> velocities, List<Integer> positions) {
        List<Integer> updatedVelocities = new ArrayList<>(velocities);

        for(int i = 0; i < velocities.size(); i++) {
            for(int j = i + 1; j < velocities.size(); j++) {
                if(positions.get(i) > positions.get(j)) {
                    updatedVelocities.set(i, updatedVelocities.get(i) - 1);
                    updatedVelocities.set(j,  updatedVelocities.get(j) + 1);
                } else if(positions.get(i) < positions.get(j)){
                    updatedVelocities.set(i, updatedVelocities.get(i) + 1);
                    updatedVelocities.set(j,  updatedVelocities.get(j) - 1);
                }
            }
        }

        return updatedVelocities;
    }

    private List<Integer> updatePositions(List<Integer> positions, List<Integer> velocities) {
        List<Integer> newPositions = new ArrayList<>(positions);

        for(int i = 0; i < positions.size(); i++) {
            newPositions.set(i, newPositions.get(i) + velocities.get(i));
        }

        return newPositions;
    }

    private boolean matchesStartState(List<Moon> moons, List<Integer> moonPositionsForDimension, List<Integer> velocitiesForDimension, int dimension) {
        boolean matches = true;

        for(int i = 0; i < moons.size() && matches; i++) {
            matches = moons.get(i).dimensions[dimension] == moonPositionsForDimension.get(i) && moons.get(i).velocityDimensions[dimension] == velocitiesForDimension.get(i);
        }

        return matches;
    }

    public Day12Task2(String[] moons) {

        for (String s : moons) {
            String withoutBrackets = s.substring(1, s.length() - 1);
            String[] coordinates = withoutBrackets.split(",");
            int x = 0;
            int y = 0;
            int z = 0;
            for (int i = 0; i < coordinates.length; i++) {
                String[] numbers = coordinates[i].split("=");
                switch (i) {
                    case 0:
                        x = Integer.parseInt(numbers[1]);
                        break;
                    case 1:
                        y = Integer.parseInt(numbers[1]);
                        break;
                    case 2:
                        z = Integer.parseInt(numbers[1]);
                        break;
                }
            }

            this.moons.add(new Moon(x, y, z));
        }

        int[] stepsPerDimension = new int[3];

        for(int i = 0; i < stepsPerDimension.length; i++) {
            int finalI = i;
            List<Integer> moonPositionsForDim = this.moons.stream().map(moon -> moon.getDimensions()[finalI]).collect(
                    Collectors.toList());
            List<Integer> velocityForDim = this.moons.stream().map(moon -> moon.velocityDimensions[finalI]).collect(
                    Collectors.toList());

            do {
                velocityForDim = updateVelocities(velocityForDim, moonPositionsForDim);
                moonPositionsForDim = updatePositions(moonPositionsForDim, velocityForDim);

                stepsPerDimension[i] += 1;

            } while (!matchesStartState(this.moons, moonPositionsForDim, velocityForDim, i));
        }

        System.out.println(calculateLeastCommonMultiple(stepsPerDimension));
    }

    private List<Integer> getPrimeFactors(int n) {
        int i = 2;
        List<Integer> factors = new ArrayList<>();

        while(i * i <= n) {
            if(n % i != 0) {
                i++;
            } else {
                n = Math.floorDiv(n, i);
                factors.add(i);
            }
        }

        if(n > 1) {
            factors.add(n);
        }

        return factors;
    }

    public long calculateLeastCommonMultiple(int[] steps) {
        List<List<Integer>> primeFactorsPerDimension = new ArrayList<>();

        for (int step : steps) {
            primeFactorsPerDimension.add(getPrimeFactors(step));
        }

        Set<Integer> allPrimeFactors = new HashSet<>();

        for (List<Integer> integers : primeFactorsPerDimension) {
            allPrimeFactors.addAll(integers);
        }

        long lcm = 1;

        for(int prime : allPrimeFactors) {
            List<Integer> occurrencesOfPrimeSorted = primeFactorsPerDimension.stream().map(dimension -> (int) dimension.stream()
                    .filter(primesOfDimension -> primesOfDimension == prime).count()).sorted(Integer::compareTo).collect(Collectors.toList());
            int amount = occurrencesOfPrimeSorted.get(occurrencesOfPrimeSorted.size() - 1);
            lcm *= Math.pow(prime, amount);
        }

        return lcm;
    }

    private class Moon {
        int[] dimensions = new int[3];

        int[] velocityDimensions = new int[3];

        public Moon(int x, int y, int z) {
            dimensions[0] = x;
            dimensions[1] = y;
            dimensions[2] = z;
        }

        public int[] getDimensions() {
            return dimensions;
        }
    }
}
