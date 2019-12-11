package day1.task1;

import static org.junit.jupiter.api.Assertions.*;

class Day1Task1Test {

    @org.junit.jupiter.api.Test void getFuelRequirementTest() {

        Day1Task1 m = new Day1Task1();

        assertEquals(2, m.getFuelRequirement(12));
        assertEquals(2, m.getFuelRequirement(14));
        assertEquals(654, m.getFuelRequirement(1969));
        assertEquals(33583, m.getFuelRequirement(100756));

    }
}
