package util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class IntCodeComputer {
    private Map<Integer, Long> memory;
    private int currentPosition = 0;
    private int relativeBase = 0;
    private boolean done = false;
    private boolean error = false;
    private List<Long> inputs;
    private int currentInput = 0;
    private List<Long> outputs = new ArrayList<>();
    private boolean realDone = false;

    public IntCodeComputer(Map<Integer, Long> in, List<Long> inputs) {
        this.memory = in;
        this.inputs = inputs;
    }

    public void updateInputs(List<Long> inputs) {
        this.inputs = inputs;
        currentInput = 0;
        done = false;
    }

    public void processCode() {
        while (!done && !error && !realDone) {
            int toParse = Math.toIntExact(getValue(currentPosition));
            Integer[] codeSplitted =
                    Arrays.stream(String.valueOf(toParse).split("")).map(Integer::parseInt)
                            .toArray(Integer[]::new);

            int code = 0;
            int mode1 = 0;
            int mode2 = 0;
            int mode3 = 0;

            if (codeSplitted.length == 1) {
                code = codeSplitted[0];
            }

            if (codeSplitted.length >= 2) {
                code = Integer.parseInt("" + codeSplitted[codeSplitted.length - 2] + codeSplitted[
                        codeSplitted.length - 1]);
            }
            if (codeSplitted.length >= 3) {
                mode1 = codeSplitted[codeSplitted.length - 3];
            }
            if (codeSplitted.length >= 4) {
                mode2 = codeSplitted[codeSplitted.length - 4];
            }

            if (codeSplitted.length >= 5) {
                mode3 = codeSplitted[codeSplitted.length - 5];
            }

            switch (code) {
                case 1:
                    add(transformPosition(currentPosition + 1, mode1),
                            transformPosition(currentPosition + 2, mode2),
                            Math.toIntExact(transformPosition(currentPosition + 3, mode3)));
                    currentPosition += 4;
                    break;
                case 2:
                    mult(transformPosition(currentPosition + 1, mode1),
                            transformPosition(currentPosition + 2, mode2),
                            Math.toIntExact(transformPosition(currentPosition + 3, mode3)));
                    currentPosition += 4;
                    break;
                case 3:
                    readInput(Math.toIntExact(transformPosition(currentPosition + 1, mode1)));
                    currentPosition += 2;
                    break;
                case 4:
                    writeOutput(Math.toIntExact(transformPosition(currentPosition + 1, mode1)));
                    currentPosition += 2;
                    done = true;
                    break;
                case 5:
                    jumpIf(transformPosition(currentPosition + 1, mode1),
                            Math.toIntExact(transformPosition(currentPosition + 2, mode2)), false);
                    break;
                case 6:
                    jumpIf(transformPosition(currentPosition + 1, mode1),
                            Math.toIntExact(transformPosition(currentPosition + 2, mode2)), true);
                    break;
                case 7:
                    compare(transformPosition(currentPosition + 1, mode1),
                            transformPosition(currentPosition + 2, mode2),
                            Math.toIntExact(transformPosition(currentPosition + 3, mode3)), false);
                    currentPosition += 4;
                    break;
                case 8:
                    compare(transformPosition(currentPosition + 1, mode1),
                            transformPosition(currentPosition + 2, mode2),
                            Math.toIntExact(transformPosition(currentPosition + 3, mode3)), true);
                    currentPosition += 4;
                    break;
                case 9:
                    adjustBase(Math.toIntExact(transformPosition(currentPosition + 1, mode1)));
                    currentPosition += 2;
                    break;
                case 99:
                    done = true;
                    realDone = true;
                    break;
                default:
                    error = true;
                    System.err.println("Catched an error!");
                    break;

            }
        }
    }

    private void add(int pos1, int pos2, int pos3) {
        setPosition(pos3, getValue(pos1) + getValue(pos2));
    }

    private void mult(int pos1, int pos2, int pos3) {
        setPosition(pos3, getValue(pos1) * getValue(pos2));
    }

    private void readInput(int pos) {
        setPosition(pos, inputs.get(currentInput));
        if(currentInput < inputs.size()-1){
            currentInput++;
        }
    }

    private void writeOutput(int pos) {
        outputs.add(getValue(pos));
    }

    private void jumpIf(int pos1, int pos2, boolean condition) {
        if ((getValue(pos1) == 0) == condition) {
            currentPosition = Math.toIntExact(getValue(pos2));
        } else {
            currentPosition += 3;
        }
    }

    private void compare(int pos1, int pos2, int pos, boolean equals) {
        if (equals) {
            if (getValue(pos1).equals(getValue(pos2))) {
                setPosition(pos, 1L);
            } else {
                setPosition(pos, 0L);
            }
        } else {
            if (getValue(pos1) < getValue(pos2)) {
                setPosition(pos, 1L);
            } else {
                setPosition(pos, 0L);
            }
        }
    }

    public List<Long> getOutputs() {
        return outputs;
    }

    private int transformPosition(int pos, int mode) {
        if (mode == 0) {
            return Math.toIntExact(getValue(pos));
        } else if (mode == 1) {
            return pos;
        } else {
            return Math.toIntExact(relativeBase + getValue(pos));
        }
    }

    private void adjustBase(int pos1) {
        relativeBase += getValue(pos1);
    }

    private void setPosition(int pos, Long value) {
        memory.put(pos, value);
    }

    private Long getValue(int pos) {
        if (!memory.containsKey(pos)) {
            memory.put(pos, 0L);
        }

        return memory.get(pos);
    }

    public Map<Integer, Long> getMemory() {
        return memory;
    }

}
