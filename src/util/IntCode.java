package util;

import java.util.*;

public class IntCode {

    private Map<Long, Long> memory;

    private Queue<Long> input = new LinkedList<>();

    private long relativeBase = 0;

    private long currentIndex = 0;

    private long defaultInput = 0;

    int j = 0;

    public IntCode(List<Long> listOfIntCode) {

        this.memory = new HashMap<>();

        for (int i = 0; i < listOfIntCode.size(); i++) {
            memory.put((long) i, listOfIntCode.get(i));
        }
    }

    public Long runCode(boolean haltAtOutput) {
        while (true) {
            Pair<Boolean, Long> result = doStep(haltAtOutput, false);
            if(result != null) {
                return result.getValue();
            }
        }
    }

    public Pair<Boolean, Long> doStep(boolean haltAtOutput, boolean informReadInput) {
        int opCode = (int) (getMemoryValue(currentIndex) - (Math.floorDiv(getMemoryValue(currentIndex), 100) * 100));
        List<Integer> parameterMode = new ArrayList<>();

        for (int position = String.valueOf(getMemoryValue(currentIndex)).length() - 3; position >= 0; position--) {
            parameterMode.add(Integer.parseInt(String.valueOf(String.valueOf(getMemoryValue(currentIndex)).charAt(position))));
        }

        long firstValue = parameterMode.size() < 1 || parameterMode.get(0) == 0
                ? getMemoryValue(getMemoryValue(currentIndex + 1))
                : parameterMode.get(0) == 1 ? getMemoryValue(currentIndex + 1) : getMemoryValue(getMemoryValue(currentIndex + 1) + relativeBase);
        long secondValue = parameterMode.size() < 2 || parameterMode.get(1) == 0
                ? getMemoryValue(getMemoryValue(currentIndex + 2))
                : parameterMode.get(1) == 1 ? getMemoryValue(currentIndex + 2) : getMemoryValue(getMemoryValue(currentIndex + 2) + relativeBase);
        long thirdValue = parameterMode.size() < 3 || parameterMode.get(2) == 0 ? getMemoryValue(
                currentIndex + 3) : getMemoryValue(currentIndex + 3) + relativeBase;

        if (opCode == 1 || opCode == 2 || opCode == 7 || opCode == 8) { // 3 Parameter
            if (opCode == 1) { // Addition
                memory.put(thirdValue, firstValue + secondValue);
                //                    System.out.println("add with result: " + memory.get(thirdValue));
            } else if (opCode == 2) { // Multiplication
                memory.put(thirdValue, firstValue * secondValue);
                //                    System.out.println("mult with result: " + memory.get(thirdValue));
            } else if (opCode == 7) { // Smaller than
                //                    System.out.println("compare less than");
                if (firstValue < secondValue) {
                    memory.put(thirdValue, 1L);
                    //                        System.out.println("set " + thirdValue + " to 1");
                } else {
                    memory.put(thirdValue, 0L);
                    //                        System.out.println("set " + thirdValue + " to 0");
                }
            } else if (opCode == 8) { // Equal
                //                    System.out.println("equal");
                if (firstValue == secondValue) {
                    memory.put(thirdValue, 1L);
                    //                        System.out.println("set " + thirdValue + " to 1");
                } else {
                    memory.put(thirdValue, 0L);
                    //                        System.out.println("set " + thirdValue + " to 0");
                }
            }
            currentIndex += 4;
        } else if (opCode == 3 || opCode == 4 || opCode == 9) { // 1 Parameter
            if (opCode == 3) { // Take Input
                //                    System.out.println("Reading Input");
                Long head = input.poll();
                if (head == null) {
                    head = defaultInput;
                }

                memory.put(parameterMode.size() < 1 || parameterMode.get(0) == 0 ? getMemoryValue(
                                currentIndex + 1) : getMemoryValue(currentIndex + 1) + relativeBase,
                        head);
                currentIndex += 2;

                if(informReadInput) {
                    return new Pair<>(false, null);
                }
            } else if (opCode == 4) { // Output
                currentIndex += 2;
                if (haltAtOutput) {
                    //                        System.out.println("outputting: " + firstValue + " at " + (currentIndex - 2));
                    return new Pair<>(true, firstValue);
                } else {
                    System.out.println(firstValue);
                }
            } else if (opCode == 9) { // Adjust relative Base
                relativeBase += firstValue;
                currentIndex += 2;
                //                    System.out.println("adjusted relative base to " + relativeBase);
            }
        } else if (opCode == 5 || opCode == 6) { // 2 Parameter
            if (opCode == 5) { // Not Zero
                if (firstValue != 0) {
                    currentIndex = secondValue - 3;
                    //                        System.out.println("not zero jump to: " + (currentIndex + 3));
                }
            } else if (opCode == 6) { // Is Zero
                if (firstValue == 0) {
                    currentIndex = secondValue - 3;
                    //                        System.out.println("zero jump to: " + (currentIndex + 3));
                }
            }
            currentIndex += 3;
        } else if (opCode == 99) {
            return new Pair<>(true, null);
        }
        return null;
    }

    public Map<Long, Long> getMemory() {
        return memory;
    }

    public void setMemory(Map<Long, Long> memory) {
        this.memory = memory;
    }

    public void addToInput(long toAdd) {
        input.offer(toAdd);
    }

    public long getMemoryValue(long index) {
        return memory.get(index) != null ? memory.get(index) : 0;
    }

    public void clearInput() {
        input = new LinkedList<>();
    }

    public void setDefaultInput(long defaultInput) {
        this.defaultInput = defaultInput;
    }
}
