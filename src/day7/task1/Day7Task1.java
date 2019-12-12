package day7.task1;

import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Day7Task1 {

    public static void main(String[] args) {
        List<Integer> c = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/day7/task1/input.txt"));
            String in = reader.readLine();
            String[] strings = in.split(",");
            c = Arrays.stream(strings).map(Integer::parseInt).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }

        new Day7Task1(c);

    }

    private List<Integer> input;

    public Day7Task1(List<Integer> l) {
        this.input = l;
        int maxOut = Integer.MIN_VALUE;

        List<int[]> combinations = new ArrayList<>();

        for (int a = 0; a < 5; a++) {
            for (int b = 0; b < 5; b++) {
                for (int c = 0; c < 5; c++) {
                    for (int d = 0; d < 5; d++) {
                        for (int e = 0; e < 5; e++) {
                            if (a != b && a != c && a != d && a != e && b != c && b != d && b != e
                                    && c != d && c != e && d != e) {
                                int[] res = new int[]{a,b,c,d,e,};
                                combinations.add(res);
                            }
                        }
                    }
                }
            }
        }

        for(int[] in : combinations){
            Intcode i1 = new Intcode(new ArrayList<>(input), new Pair<Integer, Integer>(in[0], 0));
            i1.processCode();
            int res = i1.getOutput();

            for(int i = 1; i < 5; i++){
                Intcode i2 = new Intcode(new ArrayList<>(input), new Pair<Integer, Integer>(in[i], res));
                i2.processCode();
                res = i2.getOutput();
            }


            if(res > maxOut){
                maxOut = res;
            }
        }

        System.out.println("Maximum: " + maxOut);
    }

    private class Intcode {
        private List<Integer> code;
        private int currPos = 0;
        private boolean done = false;
        private boolean error = false;
        private Pair<Integer, Integer> inputs;
        private int inputCounter = 0;
        private int output = 0;

        public Intcode(List<Integer> c, Pair<Integer, Integer> inputs) {
            this.code = c;
            this.inputs = inputs;
        }

        public List<Integer> processCode() {
            while (!done && !error) {
                int currCode = code.get(currPos);
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
                int firstParam;
                int secondParam;

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
                        add(currPos + 1, currPos + 2, currPos + 3, param1Mode, param2Mode);
                        currPos += 4;
                        break;
                    case 2:
                        multiply(currPos + 1, currPos + 2, currPos + 3, param1Mode, param2Mode);
                        currPos += 4;
                        break;
                    case 3:
                        code.set(code.get(currPos + 1), readInput());
                        currPos += 2;
                        break;
                    case 4:
                        if (param1Mode == 0) {
                            output = code.get(code.get(currPos + 1));
                        } else {
                            output = code.get(currPos + 1);
                        }
                        currPos += 2;
                        break;
                    case 5:
                        if (param1Mode == 0) {
                            firstParam = code.get(code.get(currPos + 1));
                        } else {
                            firstParam = code.get(currPos + 1);
                        }

                        if (firstParam != 0) {
                            if (param2Mode == 0) {
                                currPos = code.get(code.get(currPos + 2));
                            } else {
                                currPos = code.get(currPos + 2);
                            }
                        } else {
                            currPos += 3;
                        }
                        break;
                    case 6:
                        if (param1Mode == 0) {
                            firstParam = code.get(code.get(currPos + 1));
                        } else {
                            firstParam = code.get(currPos + 1);
                        }

                        if (firstParam == 0) {
                            if (param2Mode == 0) {
                                currPos = code.get(code.get(currPos + 2));
                            } else {
                                currPos = code.get(currPos + 2);
                            }
                        } else {
                            currPos += 3;
                        }
                        break;
                    case 7:
                        if (param1Mode == 0) {
                            firstParam = code.get(code.get(currPos + 1));
                        } else {
                            firstParam = code.get(currPos + 1);
                        }

                        if (param2Mode == 0) {
                            secondParam = code.get(code.get(currPos + 2));
                        } else {
                            secondParam = code.get(currPos + 2);
                        }

                        if (firstParam < secondParam) {
                            if (param3Mode == 0) {
                                code.set(code.get(currPos + 3), 1);
                            } else {
                                code.set(currPos + 3, 1);
                            }

                        } else {
                            if (param3Mode == 0) {
                                code.set(code.get(currPos + 3), 0);
                            } else {
                                code.set(currPos + 3, 0);
                            }
                        }
                        currPos += 4;
                        break;
                    case 8:
                        if (param1Mode == 0) {
                            firstParam = code.get(code.get(currPos + 1));
                        } else {
                            firstParam = code.get(currPos + 1);
                        }

                        if (param2Mode == 0) {
                            secondParam = code.get(code.get(currPos + 2));
                        } else {
                            secondParam = code.get(currPos + 2);
                        }

                        if (firstParam == secondParam) {
                            if (param3Mode == 0) {
                                code.set(code.get(currPos + 3), 1);
                            } else {
                                code.set(currPos + 3, 1);
                            }
                        } else {
                            if (param3Mode == 0) {
                                code.set(code.get(currPos + 3), 0);
                            } else {
                                code.set(currPos + 3, 0);
                            }
                        }
                        currPos += 4;
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

        public int getOutput() {
            return output;
        }

        private int readInput() {
            switch (inputCounter) {
                case 0:
                    inputCounter++;
                    return inputs.getKey();
                case 1:
                    return inputs.getValue();
            }

            return 0;
        }

        private void add(int pos1, int pos2, int pos3, int mode1, int mode2) {
            int val1;
            int val2;
            if (mode1 == 0) {
                val1 = code.get(pos1);
                val1 = code.get(val1);
            } else {
                val1 = code.get(pos1);
            }
            if (mode2 == 0) {
                val2 = code.get(pos2);
                val2 = code.get(val2);
            } else {
                val2 = code.get(pos2);
            }
            code.set(code.get(pos3), val1 + val2);
        }

        private void multiply(int pos1, int pos2, int pos3, int mode1, int mode2) {
            int val1;
            int val2;
            if (mode1 == 0) {
                val1 = code.get(pos1);
                val1 = code.get(val1);
            } else {
                val1 = code.get(pos1);
            }
            if (mode2 == 0) {
                val2 = code.get(pos2);
                val2 = code.get(val2);
            } else {
                val2 = code.get(pos2);
            }
            code.set(code.get(pos3), val1 * val2);
        }
    }
}
