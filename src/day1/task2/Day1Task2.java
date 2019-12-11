package day1.task2;

import day1.task1.Day1Task1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

/**
 * Second Task of Day 1
 */
public class Day1Task2 {

    public static void main(String[] args) {
        System.out.println("Required Fuel: " + getFuelRequirementsForFile("input.txt"));
    }

    /**
     * Get the Fuel Requirements for all Components for the File
     *
     * @param path Name of the File
     * @return Sum of all Fuel Requirements
     */
    protected static int getFuelRequirementsForFile(String path) {
        int sum = 0;

        try {
            ClassLoader loader = Day1Task1.class.getClassLoader();
            URL realPath = loader.getResource("day1/task1/" + path);
            BufferedReader reader = new BufferedReader(new FileReader(realPath.getFile()));

            String line = reader.readLine();
            while (line != null) {
                int mass = Integer.parseInt(line);
                int fuel = getFuelRequirement(mass);
                sum += fuel;
                sum += getFuelForFuel(fuel);
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sum;
    }

    /**
     * Get the Fuel Requirement for one Component
     *
     * @param mass Mass of the Component
     * @return Fuel Requirement of the Component or 0 if negative
     */
    protected static int getFuelRequirement(int mass) {
        int temp = mass / 3;
        temp -= 2;
        return Math.max(temp, 0);
    }

    /**
     * Gets the Fuel that is needed for the Fuel
     * @param fuel Fuel
     * @return Fuel that is needed for the given Fuel
     */
    protected static int getFuelForFuel(int fuel) {
        int sum = 0;

        int fuelForFuel = getFuelRequirement(fuel);
        while (fuelForFuel > 0) {
            sum += fuelForFuel;
            fuelForFuel = getFuelRequirement(fuelForFuel);
        }

        return sum;
    }

}
