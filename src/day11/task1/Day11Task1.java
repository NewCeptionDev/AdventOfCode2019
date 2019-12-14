package day11.task1;

import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Day11Task1 {

    Map<Pair<Integer, Integer>, Integer> area = new HashMap<>();

    public static void main(String[] args) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/day11/task1/input.txt"));
            String line = reader.readLine();

            String[] splitted = line.split(",");
            List<Long> in =
                    Arrays.stream(splitted).map(Long::parseLong).collect(Collectors.toList());

            new Day11Task1(in);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Day11Task1(List<Long> in) {
        Map<Integer, Long> input = new HashMap<>();
        for (int i = 0; i < in.size(); i++) {
            input.put(i, in.get(i));
        }

        Intcode i = new Intcode(input);
        Pair<Integer, Integer> currentPosition = new Pair<>(0,0);
        List<Pair<Integer, Integer>> alreadyPainted = new ArrayList<>();
        int currDirection = 0; // 0 == UP - 1 == RIGHT - 2 == DOWN - 3 == LEFT

        while (!i.realDone) {
            i.updateInput(getField(currentPosition.getKey(), currentPosition.getValue()));
            i.processCode();
            if (!i.realDone) {
                List<Long> out = i.getOutputs();
                System.out.println("Got outputs " + out.size());
                area.put(currentPosition, Math.toIntExact(out.get(0)));

                boolean alreadyPaintedBefore = false;

                for(Pair<Integer, Integer> p : alreadyPainted){
                    if(p.getKey().equals(currentPosition.getKey()) && p.getValue().equals(currentPosition.getValue())){
                        alreadyPaintedBefore = true;
                    }
                }

                if(!alreadyPaintedBefore){
                    alreadyPainted.add(currentPosition);
                }

                if(out.get(1) == 0L){
                    switch (currDirection){
                        case 0:
                            currDirection = 3;
                            break;
                        case 1:
                            currDirection = 0;
                            break;
                        case 2:
                            currDirection = 1;
                            break;
                        case 3:
                            currDirection = 2;
                            break;
                    }
                } else if(out.get(1) == 1L){
                    switch (currDirection){
                        case 0:
                            currDirection = 1;
                            break;
                        case 1:
                            currDirection = 2;
                            break;
                        case 2:
                            currDirection = 3;
                            break;
                        case 3:
                            currDirection = 4;
                            break;
                    }
                } else {
                    System.err.println("Should never happen");
                }

                switch (currDirection){
                    case 0:
                        currentPosition = new Pair<>(currentPosition.getKey(), currentPosition.getValue()+1);
                        break;
                    case 1:
                        currentPosition = new Pair<>(currentPosition.getKey()+1, currentPosition.getValue());
                        break;
                    case 2:
                        currentPosition = new Pair<>(currentPosition.getKey(), currentPosition.getValue()-1);
                        break;
                    case 3:
                        currentPosition = new Pair<>(currentPosition.getKey()-1, currentPosition.getValue());
                        break;
                }
            }

        }
        System.out.println("Panels painted at least once: " + alreadyPainted.size());
    }

    private int getField(int x, int y) {
        return area.getOrDefault(new Pair<>(x, y), 0);
    }

    private class Intcode {
        private Map<Integer, Long> code;
        private int currPos = 0;
        private boolean done = false;
        private boolean error = false;
        private int relativeBase = 0;
        private boolean realDone = false;
        private Long currInput = 0L;
        private List<Long> outputs = new ArrayList<>();

        public Intcode(Map<Integer, Long> c) {
            this.code = c;
        }

        public void updateInput(long i) {
            currInput = i;
            outputs = new ArrayList<>();
            done = false;
        }

        public List<Long> getOutputs() {
            System.out.println("returning outputs");
            System.out.println("Size: " + outputs.size());
            return outputs;
        }

        public Map<Integer, Long> processCode() {
            while (!done && !error) {
                int currCode = Math.toIntExact(code.getOrDefault(currPos, 0L));
                char[] digits = String.valueOf(currCode).toCharArray();

                String operationCodeString;
                if (digits.length >= 2) {
                    operationCodeString =
                            digits[digits.length - 2] + "" + digits[digits.length - 1];
                } else {
                    operationCodeString = digits[digits.length - 1] + "";
                }

                int operationCode = Integer.parseInt(operationCodeString);
                int param1Mode;
                int param2Mode;
                int param3Mode;
                Long firstParam;
                Long secondParam;

                if (digits.length >= 3) {
                    param1Mode = Integer.parseInt(String.valueOf(digits[digits.length - 3]));
                } else {
                    param1Mode = 0;
                }

                if (digits.length >= 4) {
                    param2Mode = Integer.parseInt(String.valueOf(digits[digits.length - 4]));
                } else {
                    param2Mode = 0;
                }

                if (digits.length >= 5) {
                    param3Mode = Integer.parseInt(String.valueOf(digits[digits.length - 5]));
                } else {
                    param3Mode = 0;
                }

                switch (operationCode) {
                    case 1:
                        add(currPos + 1, currPos + 2, currPos + 3, param1Mode, param2Mode,
                                param3Mode, relativeBase);
                        currPos += 4;
                        break;
                    case 2:
                        multiply(currPos + 1, currPos + 2, currPos + 3, param1Mode, param2Mode,
                                param3Mode, relativeBase);
                        currPos += 4;
                        break;
                    case 3:
                        if (param1Mode == 0) {
                            code.put(Math.toIntExact(code.getOrDefault(currPos + 1, 0L)),
                                    readInput());
                        } else if (param1Mode == 2) {
                            code.put(Math.toIntExact(
                                    relativeBase + code.getOrDefault(currPos + 1, 0L)),
                                    readInput());
                        } else {
                            System.err.println("Error");
                        }
                        currPos += 2;
                        break;
                    case 4:
                        if (param1Mode == 0) {
                            outputs.add(code.getOrDefault(
                                    Math.toIntExact(code.getOrDefault(currPos + 1, 0L)), 0L));
                        } else if (param1Mode == 1) {
                            outputs.add(code.getOrDefault(currPos + 1, 0L));
                        } else {
                            outputs.add(code.getOrDefault(Math.toIntExact(
                                    relativeBase + code.getOrDefault(currCode + 1, 0L)), 0L));
                        }
                        currPos += 2;
                        System.out.println("Output added");
                        if (outputs.size() == 2) {
                            done = true;
                            System.out.println("2 Outputs done");
                        }
                        break;
                    case 5:
                        if (param1Mode == 0) {
                            firstParam = code.getOrDefault(
                                    Math.toIntExact(code.getOrDefault(currPos + 1, 0L)), 0L);
                        } else if (param1Mode == 1) {
                            firstParam = code.getOrDefault(currPos + 1, 0L);
                        } else {
                            firstParam = code.getOrDefault(Math.toIntExact(
                                    relativeBase + code.getOrDefault(currPos + 1, 0L)), 0L);
                        }

                        if (firstParam != 0) {
                            if (param2Mode == 0) {
                                currPos = Math.toIntExact(code.getOrDefault(
                                        Math.toIntExact(code.getOrDefault(currPos + 2, 0L)), 0L));
                            } else if (param2Mode == 1) {
                                currPos = Math.toIntExact(code.getOrDefault(currPos + 2, 0L));
                            } else {
                                currPos = Math.toIntExact(code.getOrDefault(Math.toIntExact(
                                        relativeBase + code.getOrDefault(currPos + 2, 0L)), 0L));
                            }
                        } else {
                            currPos += 3;
                        }
                        break;
                    case 6:
                        if (param1Mode == 0) {
                            firstParam = code.getOrDefault(
                                    Math.toIntExact(code.getOrDefault(currPos + 1, 0L)), 0L);
                        } else if (param1Mode == 1) {
                            firstParam = code.getOrDefault(currPos + 1, 0L);
                        } else {
                            firstParam = code.getOrDefault(Math.toIntExact(
                                    relativeBase + code.getOrDefault(currPos + 1, 0L)), 0L);
                        }

                        if (firstParam == 0) {
                            if (param2Mode == 0) {
                                currPos = Math.toIntExact(code.getOrDefault(
                                        Math.toIntExact(code.getOrDefault(currPos + 2, 0L)), 0L));
                            } else if (param2Mode == 1) {
                                currPos = Math.toIntExact(code.getOrDefault(currPos + 2, 0L));
                            } else {
                                currPos = Math.toIntExact(code.getOrDefault(Math.toIntExact(
                                        relativeBase + code.getOrDefault(currPos + 2, 0L)), 0L));
                            }
                        } else {
                            currPos += 3;
                        }
                        break;
                    case 7:
                        if (param1Mode == 0) {
                            firstParam = code.getOrDefault(
                                    Math.toIntExact(code.getOrDefault(currPos + 1, 0L)), 0L);
                        } else if (param1Mode == 1) {
                            firstParam = code.getOrDefault(currPos + 1, 0L);
                        } else {
                            firstParam = code.getOrDefault(Math.toIntExact(
                                    relativeBase + code.getOrDefault(currPos + 1, 0L)), 0L);
                        }

                        if (param2Mode == 0) {
                            secondParam = code.getOrDefault(
                                    Math.toIntExact(code.getOrDefault(currPos + 2, 0L)), 0L);
                        } else if (param2Mode == 1) {
                            secondParam = code.getOrDefault(currPos + 2, 0L);
                        } else {
                            secondParam = code.getOrDefault(Math.toIntExact(
                                    relativeBase + code.getOrDefault(currPos + 2, 0L)), 0L);
                        }

                        if (firstParam < secondParam) {
                            if (param3Mode == 0) {
                                code.put(Math.toIntExact(code.getOrDefault(currPos + 3, 0L)), 1L);
                            } else if (param3Mode == 1) {
                                code.put(currPos + 3, 1L);
                            } else {
                                code.put(Math.toIntExact(
                                        relativeBase + code.getOrDefault(currPos + 3, 0L)), 1L);
                            }

                        } else {
                            if (param3Mode == 0) {
                                code.put(Math.toIntExact(code.getOrDefault(currPos + 3, 0L)), 0L);
                            } else if (param3Mode == 1) {
                                code.put(currPos + 3, 0L);
                            } else {
                                code.put(Math.toIntExact(
                                        relativeBase + code.getOrDefault(currPos + 3, 0L)), 0L);
                            }
                        }
                        currPos += 4;
                        break;
                    case 8:
                        if (param1Mode == 0) {
                            firstParam = code.getOrDefault(
                                    Math.toIntExact(code.getOrDefault(currPos + 1, 0L)), 0L);
                        } else if (param1Mode == 1) {
                            firstParam = code.getOrDefault(currPos + 1, 0L);
                        } else {
                            firstParam = code.getOrDefault(Math.toIntExact(
                                    relativeBase + code.getOrDefault(currPos + 1, 0L)), 0L);
                        }

                        if (param2Mode == 0) {
                            secondParam = code.getOrDefault(
                                    Math.toIntExact(code.getOrDefault(currPos + 2, 0L)), 0L);
                        } else if (param2Mode == 1) {
                            secondParam = code.getOrDefault(currPos + 2, 0L);
                        } else {
                            secondParam = code.getOrDefault(Math.toIntExact(
                                    relativeBase + code.getOrDefault(currPos + 2, 0L)), 0L);
                        }

                        if (firstParam.equals(secondParam)) {
                            if (param3Mode == 0) {
                                code.put(Math.toIntExact(code.getOrDefault(currPos + 3, 0L)), 1L);
                            } else if (param3Mode == 1) {
                                code.put(currPos + 3, 1L);
                            } else {
                                code.put(Math.toIntExact(
                                        relativeBase + code.getOrDefault(currPos + 3, 0L)), 1L);
                            }
                        } else {
                            if (param3Mode == 0) {
                                code.put(Math.toIntExact(code.getOrDefault(currPos + 3, 0L)), 0L);
                            } else if (param3Mode == 1) {
                                code.put(currPos + 3, 0L);
                            } else {
                                code.put(Math.toIntExact(
                                        relativeBase + code.getOrDefault(currPos + 3, 0L)), 0L);
                            }
                        }
                        currPos += 4;
                        break;
                    case 9:
                        if (param1Mode == 0) {
                            relativeBase += Math.toIntExact(code.getOrDefault(
                                    Math.toIntExact(code.getOrDefault(currPos + 1, 0L)), 0L));
                        } else if (param1Mode == 1) {
                            relativeBase += Math.toIntExact(code.getOrDefault(currPos + 1, 0L));
                        } else {
                            relativeBase += Math.toIntExact(code.getOrDefault(Math.toIntExact(
                                    relativeBase + code.getOrDefault(currPos + 1, 0L)), 0L));
                        }
                        currPos += 2;
                        break;
                    case 99:
                        done = true;
                        realDone = true;
                        break;
                    default:
                        error = true;
                }
            }

            return code;
        }

        private Long readInput() {
            return currInput;
        }

        private void add(int pos1, int pos2, int pos3, int mode1, int mode2, int mode3, int base) {
            Long val1;
            Long val2;
            if (mode1 == 0) {
                val1 = code.getOrDefault(pos1, 0L);
                val1 = code.getOrDefault(Math.toIntExact(val1), 0L);
            } else if (mode1 == 1) {
                val1 = code.getOrDefault(pos1, 0L);
            } else {
                val1 = code.getOrDefault(pos1, 0L);
                val1 = code.getOrDefault(Math.toIntExact(base + val1), 0L);
            }
            if (mode2 == 0) {
                val2 = code.getOrDefault(pos2, 0L);
                val2 = code.getOrDefault(Math.toIntExact(val2), 0L);
            } else if (mode2 == 1) {
                val2 = code.getOrDefault(pos2, 0L);
            } else {
                val2 = code.getOrDefault(pos2, 0L);
                val2 = code.getOrDefault(Math.toIntExact(base + val2), 0L);
            }
            if (mode3 == 0) {
                code.put(Math.toIntExact(code.getOrDefault(pos3, 0L)), val1 + val2);
            } else if (mode3 == 2) {
                code.put(Math.toIntExact(base + code.getOrDefault(pos3, 0L)), val1 + val2);
            } else {
                System.err.println("Error");
            }
        }

        private void multiply(int pos1, int pos2, int pos3, int mode1, int mode2, int mode3,
                int base) {
            Long val1;
            Long val2;
            if (mode1 == 0) {
                val1 = code.getOrDefault(pos1, 0L);
                val1 = code.getOrDefault(Math.toIntExact(val1), 0L);
            } else if (mode1 == 1) {
                val1 = code.getOrDefault(pos1, 0L);
            } else {
                val1 = code.getOrDefault(pos1, 0L);
                val1 = code.getOrDefault(Math.toIntExact(base + val1), 0L);
            }
            if (mode2 == 0) {
                val2 = code.getOrDefault(pos2, 0L);
                val2 = code.getOrDefault(Math.toIntExact(val2), 0L);
            } else if (mode2 == 1) {
                val2 = code.getOrDefault(pos2, 0L);
            } else {
                val2 = code.getOrDefault(pos2, 0L);
                val2 = code.getOrDefault(Math.toIntExact(base + val2), 0L);
            }
            if (mode3 == 0) {
                code.put(Math.toIntExact(code.getOrDefault(pos3, 0L)), val1 * val2);
            } else if (mode3 == 2) {
                code.put(Math.toIntExact(base + code.getOrDefault(pos3, 0L)), val1 * val2);
            } else {
                System.err.println("Error");
            }
        }
    }
}
