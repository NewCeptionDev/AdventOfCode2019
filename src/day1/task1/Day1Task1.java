package day1.task1;

import java.io.*;
import java.net.URL;

/**
 * First Task of Day 1
 */
public class Day1Task1 {

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
                sum += getFuelRequirement(mass);
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
     * @return Fuel Requirement of the Component
     */
    protected static int getFuelRequirement(int mass) {
        int temp = mass / 3;
        return temp - 2;
    }

}
