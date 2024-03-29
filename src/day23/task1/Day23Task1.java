package day23.task1;

import util.InputReader;
import util.IntCode;
import util.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class Day23Task1 {

    public static void main(String[] args) {
        List<String> in = InputReader.read("src/day23/task1/input.txt");

        new Day23Task1(Arrays.stream(in.get(0).split(",")).map(Long::parseLong).collect(Collectors.toList()));
    }

    public Day23Task1(List<Long> intcode) {
        Map<Integer, IntCode> machines = new HashMap<>();
        Map<Integer, Queue<Pair<Long, Long>>> packages = new HashMap<>();
        Map<Integer, List<Long>> partialPackages = new HashMap<>();

        for (int i = 0; i < 50; i++) {
            IntCode intCode = new IntCode(intcode);
            intCode.addToInput(i);
            intCode.setDefaultInput(-1);
            machines.put(i, intCode);
            packages.put(i, new LinkedList<>());
            partialPackages.put(i, new ArrayList<>());
        }

        while (true) {
            for (int i : machines.keySet()) {
                if (!packages.get(i).isEmpty()) {
                    Pair<Long, Long> inPackage = packages.get(i).poll();
                    machines.get(i).addToInput(inPackage.getKey());
                    machines.get(i).addToInput(inPackage.getValue());
                }

                Pair<Boolean, Long> output = machines.get(i).doStep(true, false);

                if (output != null && output.getValue() != null) {
                    List<Long> currentList = partialPackages.get(i);
                    currentList.add(output.getValue());

                    if (currentList.size() == 3) {
                        if (currentList.get(0) != null && currentList.get(0) == 255L) {
                            System.out.println(currentList.get(2));
                            return;
                        }

                        if (packages.containsKey(Math.toIntExact(currentList.get(0)))) {
                            packages.get(Math.toIntExact(currentList.get(0)))
                                    .add(new Pair<>(currentList.get(1), currentList.get(2)));
                        }
                        currentList = new ArrayList<>();
                    }
                    partialPackages.put(i, currentList);
                }
            }
        }
    }
}
