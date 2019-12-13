package day9.task1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day9Task1 {

    public static void main(String[] args) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/day9/task1/input.txt"));
            String line = reader.readLine();

            String[] splitted = line.split(",");
            List<Long> in =
                    Arrays.stream(splitted).map(Long::parseLong).collect(Collectors.toList());

            new Day9Task1(in);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Day9Task1(List<Long> in) {
        Map<Integer, Long> input = new HashMap<>();
        for (int i = 0; i < in.size(); i++) {
            input.put(i, in.get(i));
        }

        Intcode i = new Intcode(input);

        i.processCode();
        //System.out.println(i.code);
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
                int currCode = Math.toIntExact(code.get(currPos));
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
                            code.put(Math.toIntExact(code.get(currPos + 1)), readInput());
                        } else if(param1Mode == 2) {
                            code.put(Math.toIntExact(relativeBase + code.get(currPos + 1)), readInput());
                        } else {
                            System.err.println("Error");
                        }
                        currPos += 2;
                        break;
                    case 4:
                        if (param1Mode == 0) {
                            System.out.println(
                                    "Output: " + code.get(Math.toIntExact(code.get(currPos + 1))));
                        } else if (param1Mode == 1) {
                            System.out.println("Output: " + code.get(currPos + 1));
                        } else {
                            System.out.println("Output: " + code
                                    .get(Math.toIntExact(relativeBase + code.getOrDefault(currCode + 1, 0L))));
                        }
                        currPos += 2;
                        done = true;
                        break;
                    case 5:
                        if (param1Mode == 0) {
                            firstParam = code.get(Math.toIntExact(code.get(currPos + 1)));
                        } else if (param1Mode == 1) {
                            firstParam = code.get(currPos + 1);
                        } else {
                            firstParam =
                                    code.get(Math.toIntExact(relativeBase + code.get(currPos + 1)));
                        }

                        if (firstParam != 0) {
                            if (param2Mode == 0) {
                                currPos = Math.toIntExact(
                                        code.get(Math.toIntExact(code.get(currPos + 2))));
                            } else if (param2Mode == 1) {
                                currPos = Math.toIntExact(code.get(currPos + 2));
                            } else {
                                currPos = Math.toIntExact(code.get(
                                        Math.toIntExact(relativeBase + code.get(currPos + 2))));
                            }
                        } else {
                            currPos += 3;
                        }
                        break;
                    case 6:
                        if (param1Mode == 0) {
                            firstParam = code.get(Math.toIntExact(code.get(currPos + 1)));
                        } else if (param1Mode == 1) {
                            firstParam = code.get(currPos + 1);
                        } else {
                            firstParam =
                                    code.get(Math.toIntExact(relativeBase + code.get(currPos + 1)));
                        }

                        if (firstParam == 0) {
                            if (param2Mode == 0) {
                                currPos = Math.toIntExact(
                                        code.get(Math.toIntExact(code.get(currPos + 2))));
                            } else if (param2Mode == 1) {
                                currPos = Math.toIntExact(code.get(currPos + 2));
                            } else {
                                currPos = Math.toIntExact(code.get(
                                        Math.toIntExact(relativeBase + code.get(currPos + 2))));
                            }
                        } else {
                            currPos += 3;
                        }
                        break;
                    case 7:
                        if (param1Mode == 0) {
                            firstParam = code.get(Math.toIntExact(code.get(currPos + 1)));
                        } else if (param1Mode == 1) {
                            firstParam = code.get(currPos + 1);
                        } else {
                            firstParam =
                                    code.get(Math.toIntExact(relativeBase + code.get(currPos + 1)));
                        }

                        if (param2Mode == 0) {
                            secondParam = code.get(Math.toIntExact(code.get(currPos + 2)));
                        } else if (param2Mode == 1) {
                            secondParam = code.get(currPos + 2);
                        } else {
                            secondParam =
                                    code.get(Math.toIntExact(relativeBase + code.get(currPos + 2)));
                        }

                        if (firstParam < secondParam) {
                            if (param3Mode == 0) {
                                code.put(Math.toIntExact(code.get(currPos + 3)), 1L);
                            } else if (param3Mode == 1) {
                                code.put(currPos + 3, 1L);
                            } else {
                                code.put(Math.toIntExact(relativeBase + code.get(currPos + 3)), 1L);
                            }

                        } else {
                            if (param3Mode == 0) {
                                code.put(Math.toIntExact(code.get(currPos + 3)), 0L);
                            } else if (param3Mode == 1) {
                                code.put(currPos + 3, 0L);
                            } else {
                                code.put(Math.toIntExact(relativeBase + code.get(currPos + 3)), 0L);
                            }
                        }
                        currPos += 4;
                        break;
                    case 8:
                        if (param1Mode == 0) {
                            firstParam = code.get(Math.toIntExact(code.get(currPos + 1)));
                        } else if (param1Mode == 1) {
                            firstParam = code.get(currPos + 1);
                        } else {
                            firstParam =
                                    code.get(Math.toIntExact(relativeBase + code.get(currPos + 1)));
                        }

                        if (param2Mode == 0) {
                            secondParam = code.get(Math.toIntExact(code.get(currPos + 2)));
                        } else if (param2Mode == 1) {
                            secondParam = code.get(currPos + 2);
                        } else {
                            secondParam =
                                    code.get(Math.toIntExact(relativeBase + code.get(currPos + 2)));
                        }

                        if (firstParam.equals(secondParam)) {
                            if (param3Mode == 0) {
                                code.put(Math.toIntExact(code.get(currPos + 3)), 1L);
                            } else if (param3Mode == 1) {
                                code.put(currPos + 3, 1L);
                            } else {
                                code.put(Math.toIntExact(relativeBase + code.get(currPos + 3)), 1L);
                            }
                        } else {
                            if (param3Mode == 0) {
                                code.put(Math.toIntExact(code.get(currPos + 3)), 0L);
                            } else if (param3Mode == 1) {
                                code.put(currPos + 3, 0L);
                            } else {
                                code.put(Math.toIntExact(relativeBase + code.get(currPos + 3)), 0L);
                            }
                        }
                        currPos += 4;
                        break;
                    case 9:
                        if (param1Mode == 0) {
                            relativeBase += Math.toIntExact(
                                    code.get(Math.toIntExact(code.get(currPos + 1))));
                        } else if (param1Mode == 1) {
                            relativeBase += Math.toIntExact(code.get(currPos + 1));
                        } else {
                            relativeBase += Math.toIntExact(code.get(
                                    Math.toIntExact(relativeBase + code.get(currPos + 1))));
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
            return 1L;
        }

        private void add(int pos1, int pos2, int pos3, int mode1, int mode2, int mode3, int base) {
            Long val1;
            Long val2;
            if (mode1 == 0) {
                val1 = code.get(pos1);
                val1 = code.get(Math.toIntExact(val1));
            } else if (mode1 == 1){
                val1 = code.get(pos1);
            } else {
                val1 = code.get(pos1);
                val1 = code.get(Math.toIntExact(base + val1));
            }
            if (mode2 == 0) {
                val2 = code.get(pos2);
                val2 = code.get(Math.toIntExact(val2));
            } else if(mode2 == 1){
                val2 = code.get(pos2);
            } else {
                val2 = code.get(pos2);
                val2 = code.get(Math.toIntExact(base + val2));
            }
            if (mode3 == 0) {
                code.put(Math.toIntExact(code.get(pos3)), val1 + val2);
            } else if(mode3 == 2){
                code.put(Math.toIntExact(base + code.get(pos3)), val1 + val2);
            } else {
                System.err.println("Error");
            }
        }

        private void multiply(int pos1, int pos2, int pos3, int mode1, int mode2, int mode3, int base) {
            Long val1;
            Long val2;
            if (mode1 == 0) {
                val1 = code.get(pos1);
                val1 = code.get(Math.toIntExact(val1));
            } else if(mode1 == 1) {
                val1 = code.get(pos1);
            } else {
                val1 = code.get(pos1);
                val1 = code.get(Math.toIntExact(base + val1));
            }
            if (mode2 == 0) {
                val2 = code.get(pos2);
                val2 = code.get(Math.toIntExact(val2));
            } else if(mode2 == 1){
                val2 = code.get(pos2);
            } else {
                val2 = code.get(pos2);
                val2 = code.get(Math.toIntExact(base + val2));
            }
            if (mode3 == 0) {
                code.put(Math.toIntExact(code.get(pos3)), val1 * val2);
            } else if(mode3 == 2){
                code.put(Math.toIntExact(base + code.get(pos3)), val1 * val2);
            } else {
                System.err.println("Error");
            }
        }
    }
}
