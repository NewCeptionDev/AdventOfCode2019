package day5.task2;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Day5Task2 {

    private List<Integer> code;
    private int currPos = 0;
    private boolean done = false;
    private boolean error = false;

    public static void main(String[] args) {
        List<Integer> c = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/day5/task1/input.txt"));
            String in = reader.readLine();
            String[] strings = in.split(",");
            c = Arrays.stream(strings).map(Integer::parseInt).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Day5Task2 t = new Day5Task2(c);
        t.processCode();
    }

    public Day5Task2(List<Integer> c) {
        this.code = c;
    }

    public List<Integer> processCode() {
        while (!done && !error) {
            int currCode = code.get(currPos);
            char[] digits = String.valueOf(currCode).toCharArray();

            String operationCodeString;
            if (digits.length >= 2) {
                operationCodeString = digits[digits.length - 2] + "" + digits[digits.length - 1];
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
                        System.out.println("Output: " + code.get(code.get(currPos + 1)));
                    } else {
                        System.out.println("Output: " + code.get(currPos + 1));
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

    private int readInput() {
        Scanner sc = new Scanner(System.in);

        System.out.println("Please enter the Input: ");

        String input = sc.next();

        return Integer.parseInt(input);
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
