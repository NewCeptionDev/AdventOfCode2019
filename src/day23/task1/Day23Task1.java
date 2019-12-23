package day23.task1;

import util.InputReader;
import util.IntCodeComputer;

import java.util.*;
import java.util.stream.Collectors;

public class Day23Task1 {

    public static void main(String[] args) {
        List<String> in = InputReader.read("src/day23/task1/input.txt");

        new Day23Task1(Arrays.stream(in.get(0).split(",")).map(Long::parseLong).collect(Collectors.toList()));
    }

    private Map<Integer, Long> code;
    private List<Computer> computers = new ArrayList<>();
    private boolean allUp;
    private int currActive = 0;

    public Day23Task1(List<Long> intcode) {
        this.code = new HashMap<>();

        for (int i = 0; i < intcode.size(); i++) {
            code.put(i, intcode.get(i));
        }

        for (int i = 0; i < 50; i++) {
            Computer c = new Computer(i);
            computers.add(c);
        }

        nextComputer();
    }

    public void nextComputer() {
        computers.get(currActive).run();
        currActive = currActive == 49 ? 0 : currActive + 1;
    }

    private class Computer {
        private IntCodeComputer nic;
        public Queue<Package> input;

        public Computer(long id) {
            List<Long> inputs = new ArrayList<>();
            inputs.add(id);
            System.out.println("init computer with id " + id);

            this.nic = new IntCodeComputer(new HashMap<>(code), inputs);
            this.nic.changeStepsTillStop(3);
            this.input = new LinkedList<>();
        }

        public void run() {
            nic.processCode();
            List<Long> out = nic.getOutputs();

            if (out.size() > 0) {
                int destination = Math.toIntExact(out.get(0));
                long x = out.get(1);
                long y = out.get(2);

                Package p = new Package(x, y);

                if (destination == 255) {
                    System.out.println("GOT Package to 255: " + x + ", " + y);
                    return;
                }

                nic.refreshOutput();
                computers.get(destination).input.add(p);
                System.out.println("Send Package to " + destination + " with " + x + ", " + y);
            }

            List<Long> newInput = new ArrayList<>();
            if (input.size() > 0) {
                System.out.println("Got package");
                Package p = input.remove();
                newInput.add(p.getX());
                newInput.add(p.getY());
            } else {
                newInput.add(-1L);
            }

            nic.updateInputs(newInput);
            nextComputer();
        }
    }

    private static class Package {
        private long x;
        private long y;

        public Package(long x, long y) {
            this.x = x;
            this.y = y;
        }

        public long getX() {
            return x;
        }

        public long getY() {
            return y;
        }
    }

}
