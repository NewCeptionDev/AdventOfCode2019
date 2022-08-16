package day23.task2;

import util.InputReader;
import util.IntCode;
import util.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class Day23Task2 {
    public static void main(String[] args) {
        List<String> in = InputReader.read("src/day23/task1/input.txt");

        new Day23Task2(Arrays.stream(in.get(0).split(",")).map(Long::parseLong).collect(Collectors.toList()));
    }

    public Day23Task2(List<Long> intcode) {
        Map<Integer, IntCode> machines = new HashMap<>();
        Map<Integer, Queue<Pair<Long, Long>>> packages = new HashMap<>();
        Map<Integer, List<Long>> partialPackages = new HashMap<>();
        Map<Integer, Integer> idleMachines = new HashMap<>();

        Pair<Long, Long> lastNATPackage = null;
        Long lastYDeliveredToMachine0ByNAT = null;

        for (int i = 0; i < 50; i++) {
            IntCode intCode = new IntCode(intcode);
            intCode.addToInput(i);
            intCode.setDefaultInput(-1);
            machines.put(i, intCode);
            packages.put(i, new LinkedList<>());
            partialPackages.put(i, new ArrayList<>());
            idleMachines.put(i, 0);
        }

        while (true) {
            for (int i : machines.keySet()) {
                if (!packages.get(i).isEmpty()) {
                    Pair<Long, Long> inPackage = packages.get(i).poll();
                    machines.get(i).addToInput(inPackage.getKey());
                    machines.get(i).addToInput(inPackage.getValue());
                }

                Pair<Boolean, Long> output = machines.get(i).doStep(true, true);

                if(output != null && !output.getKey()) {
                    idleMachines.put(i, idleMachines.get(i) + 1);
                } else if (output != null && output.getValue() != null) {
                    idleMachines.put(i, 0);
                    List<Long> currentList = partialPackages.get(i);
                    currentList.add(output.getValue());

                    if (currentList.size() == 3) {
                        if (currentList.get(0) != null && currentList.get(0) == 255L) {
                            lastNATPackage = new Pair<>(currentList.get(1), currentList.get(2));
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

            if(packages.values().stream().map(Collection::size).noneMatch(size -> size > 0) && partialPackages.values().stream().map(Collection::size).noneMatch(size -> size > 0) && idleMachines.values().stream().filter(val -> val > 200).count() == machines.values().size()) {
                machines.get(0).addToInput(lastNATPackage.getKey());
                machines.get(0).addToInput(lastNATPackage.getValue());

                if(lastNATPackage.getValue().equals(lastYDeliveredToMachine0ByNAT)) {
                    System.out.println(lastYDeliveredToMachine0ByNAT);
                    return;
                }

                lastYDeliveredToMachine0ByNAT = lastNATPackage.getValue();
                for(int i = 0; i < 50; i++) {
                    idleMachines.put(i, 0);
                }
            }
        }
    }
}
