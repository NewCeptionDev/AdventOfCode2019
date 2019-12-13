package day9.task2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day9Task2 {

    //TODO NOT WORKING

    public static void main(String[] args) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/day9/task1/input.txt"));
            String line = reader.readLine();

            String[] splitted = line.split(",");
            List<Long> in =
                    Arrays.stream(splitted).map(Long::parseLong).collect(Collectors.toList());


            new Day9Task2(in);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Day9Task2(List<Long> in) {
        Map<Integer, Long> input = new HashMap<>();
        for (int i = 0; i < in.size(); i++) {
            input.put(i, in.get(i));
        }

        Intcode i = new Intcode(input);

        i.processCode();
        System.out.println(i.code.values());
    }

    private class Intcode {
        private Map<Integer, Long> code;
        private int currPos = 0;
        private boolean done = false;
        private boolean error = false;
        private int relativeBase = 0;

        public Intcode(Map<Integer, Long> c) {
            this.code = c;
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
                        add(currPos + 1, currPos + 2, currPos + 3, param1Mode, param2Mode, param3Mode, relativeBase);
                        currPos += 4;
                        break;
                    case 2:
                        multiply(currPos + 1, currPos + 2, currPos + 3, param1Mode, param2Mode, param3Mode, relativeBase);
                        currPos += 4;
                        break;
                    case 3:
                        if(param1Mode == 0) {
                            code.put(Math.toIntExact(code.getOrDefault(currPos + 1,0L)), readInput());
                        } else if(param1Mode == 2) {
                            code.put(Math.toIntExact(relativeBase + code.getOrDefault(currPos + 1,0L)), readInput());
                        } else {
                            System.err.println("Error");
                        }
                        currPos += 2;
                        break;
                    case 4:
                        if (param1Mode == 0) {
                            System.out.println(
                                    "Output: " + code.getOrDefault(Math.toIntExact(code.getOrDefault(currPos + 1,0L)),0L));
                        } else if (param1Mode == 1) {
                            System.out.println("Output: " + code.getOrDefault(currPos + 1, 0L));
                        } else {
                            System.out.println("Output: " + code
                                    .getOrDefault(Math.toIntExact(relativeBase + code.getOrDefault(currCode + 1, 0L)), 0L));
                        }
                        currPos += 2;
                        done = true;
                        break;
                    case 5:
                        if (param1Mode == 0) {
                            firstParam = code.getOrDefault(Math.toIntExact(code.getOrDefault(currPos + 1, 0L)),0L);
                        } else if (param1Mode == 1) {
                            firstParam = code.getOrDefault(currPos + 1,0L);
                        } else {
                            firstParam =
                                    code.getOrDefault(Math.toIntExact(relativeBase + code.getOrDefault(currPos + 1, 0L)), 0L);
                        }

                        if (firstParam != 0) {
                            if (param2Mode == 0) {
                                currPos = Math.toIntExact(
                                        code.getOrDefault(Math.toIntExact(code.getOrDefault(currPos + 2, 0L)), 0L));
                            } else if (param2Mode == 1) {
                                currPos = Math.toIntExact(code.getOrDefault(currPos + 2, 0L));
                            } else {
                                currPos = Math.toIntExact(code.getOrDefault(
                                        Math.toIntExact(relativeBase + code.getOrDefault(currPos + 2, 0L)), 0L));
                            }
                        } else {
                            currPos += 3;
                        }
                        break;
                    case 6:
                        if (param1Mode == 0) {
                            firstParam = code.getOrDefault(Math.toIntExact(code.getOrDefault(currPos + 1, 0L)),0L);
                        } else if (param1Mode == 1) {
                            firstParam = code.getOrDefault(currPos + 1, 0L);
                        } else {
                            firstParam =
                                    code.getOrDefault(Math.toIntExact(relativeBase + code.getOrDefault(currPos + 1, 0L)), 0L);
                        }

                        if (firstParam == 0) {
                            if (param2Mode == 0) {
                                currPos = Math.toIntExact(
                                        code.getOrDefault(Math.toIntExact(code.getOrDefault(currPos + 2,0L)), 0L));
                            } else if (param2Mode == 1) {
                                currPos = Math.toIntExact(code.getOrDefault(currPos + 2, 0L));
                            } else {
                                currPos = Math.toIntExact(code.getOrDefault(
                                        Math.toIntExact(relativeBase + code.getOrDefault(currPos + 2,0L)), 0L));
                            }
                        } else {
                            currPos += 3;
                        }
                        break;
                    case 7:
                        if (param1Mode == 0) {
                            firstParam = code.getOrDefault(Math.toIntExact(code.getOrDefault(currPos + 1, 0L)), 0L);
                        } else if (param1Mode == 1) {
                            firstParam = code.getOrDefault(currPos + 1, 0L);
                        } else {
                            firstParam =
                                    code.getOrDefault(Math.toIntExact(relativeBase + code.getOrDefault(currPos + 1, 0L)), 0L);
                        }

                        if (param2Mode == 0) {
                            secondParam = code.getOrDefault(Math.toIntExact(code.getOrDefault(currPos + 2, 0L)), 0L);
                        } else if (param2Mode == 1) {
                            secondParam = code.getOrDefault(currPos + 2, 0L);
                        } else {
                            secondParam =
                                    code.getOrDefault(Math.toIntExact(relativeBase + code.getOrDefault(currPos + 2, 0L)), 0L);
                        }

                        if (firstParam < secondParam) {
                            if (param3Mode == 0) {
                                code.put(Math.toIntExact(code.getOrDefault(currPos + 3, 0L)), 1L);
                            } else if (param3Mode == 1) {
                                code.put(currPos + 3, 1L);
                            } else {
                                code.put(Math.toIntExact(relativeBase + code.getOrDefault(currPos + 3, 0L)), 1L);
                            }

                        } else {
                            if (param3Mode == 0) {
                                code.put(Math.toIntExact(code.getOrDefault(currPos + 3, 0L)), 0L);
                            } else if (param3Mode == 1) {
                                code.put(currPos + 3, 0L);
                            } else {
                                code.put(Math.toIntExact(relativeBase + code.getOrDefault(currPos + 3, 0L)), 0L);
                            }
                        }
                        currPos += 4;
                        break;
                    case 8:
                        if (param1Mode == 0) {
                            firstParam = code.getOrDefault(Math.toIntExact(code.getOrDefault(currPos + 1, 0L)), 0L);
                        } else if (param1Mode == 1) {
                            firstParam = code.getOrDefault(currPos + 1, 0L);
                        } else {
                            firstParam =
                                    code.getOrDefault(Math.toIntExact(relativeBase + code.getOrDefault(currPos + 1, 0L)), 0L);
                        }

                        if (param2Mode == 0) {
                            secondParam = code.getOrDefault(Math.toIntExact(code.getOrDefault(currPos + 2, 0L)), 0L);
                        } else if (param2Mode == 1) {
                            secondParam = code.getOrDefault(currPos + 2, 0L);
                        } else {
                            secondParam =
                                    code.getOrDefault(Math.toIntExact(relativeBase + code.getOrDefault(currPos + 2, 0L)), 0L);
                        }

                        if (firstParam.equals(secondParam)) {
                            if (param3Mode == 0) {
                                code.put(Math.toIntExact(code.getOrDefault(currPos + 3, 0L)), 1L);
                            } else if (param3Mode == 1) {
                                code.put(currPos + 3, 1L);
                            } else {
                                code.put(Math.toIntExact(relativeBase + code.getOrDefault(currPos + 3, 0L)), 1L);
                            }
                        } else {
                            if (param3Mode == 0) {
                                code.put(Math.toIntExact(code.getOrDefault(currPos + 3, 0L)), 0L);
                            } else if (param3Mode == 1) {
                                code.put(currPos + 3, 0L);
                            } else {
                                code.put(Math.toIntExact(relativeBase + code.getOrDefault(currPos + 3, 0L)), 0L);
                            }
                        }
                        currPos += 4;
                        break;
                    case 9:
                        if (param1Mode == 0) {
                            relativeBase += Math.toIntExact(
                                    code.getOrDefault(Math.toIntExact(code.getOrDefault(currPos + 1, 0L)),0L));
                        } else if (param1Mode == 1) {
                            relativeBase += Math.toIntExact(code.getOrDefault(currPos + 1,0L));
                        } else {
                            relativeBase += Math.toIntExact(code.getOrDefault(
                                    Math.toIntExact(relativeBase + code.getOrDefault(currPos + 1, 0L)), 0L));
                        }
                        currPos += 2;
                        break;
                    case 99:
                        done = true;
                        break;
                    default:
                        error = true;
                }
            }

            return code;
        }

        private Long readInput() {
            return 2L;
        }

        private void add(int pos1, int pos2, int pos3, int mode1, int mode2, int mode3, int base) {
            Long val1;
            Long val2;
            if (mode1 == 0) {
                val1 = code.getOrDefault(pos1, 0L);
                val1 = code.getOrDefault(Math.toIntExact(val1), 0L);
            } else if (mode1 == 1){
                val1 = code.getOrDefault(pos1, 0L);
            } else {
                val1 = code.getOrDefault(pos1,0L);
                val1 = code.getOrDefault(Math.toIntExact(base + val1), 0L);
            }
            if (mode2 == 0) {
                val2 = code.getOrDefault(pos2, 0L);
                val2 = code.getOrDefault(Math.toIntExact(val2),0L);
            } else if(mode2 == 1){
                val2 = code.getOrDefault(pos2, 0L);
            } else {
                val2 = code.getOrDefault(pos2, 0L);
                val2 = code.getOrDefault(Math.toIntExact(base + val2), 0L);
            }
            if (mode3 == 0) {
                code.put(Math.toIntExact(code.getOrDefault(pos3, 0L)), val1 + val2);
            } else if(mode3 == 2){
                code.put(Math.toIntExact(base + code.getOrDefault(pos3, 0L)), val1 + val2);
            } else {
                System.err.println("Error");
            }
        }

        private void multiply(int pos1, int pos2, int pos3, int mode1, int mode2, int mode3, int base) {
            Long val1;
            Long val2;
            if (mode1 == 0) {
                val1 = code.getOrDefault(pos1, 0L);
                val1 = code.getOrDefault(Math.toIntExact(val1), 0L);
            } else if(mode1 == 1) {
                val1 = code.getOrDefault(pos1, 0L);
            } else {
                val1 = code.getOrDefault(pos1, 0L);
                val1 = code.getOrDefault(Math.toIntExact(base + val1), 0L);
            }
            if (mode2 == 0) {
                val2 = code.getOrDefault(pos2, 0L);
                val2 = code.getOrDefault(Math.toIntExact(val2), 0L);
            } else if(mode2 == 1){
                val2 = code.getOrDefault(pos2, 0L);
            } else {
                val2 = code.getOrDefault(pos2, 0L);
                val2 = code.getOrDefault(Math.toIntExact(base + val2), 0L);
            }
            if (mode3 == 0) {
                code.put(Math.toIntExact(code.getOrDefault(pos3, 0L)), val1 * val2);
            } else if(mode3 == 2){
                code.put(Math.toIntExact(base + code.getOrDefault(pos3, 0L)), val1 * val2);
            } else {
                System.err.println("Error");
            }
        }
    }
}
