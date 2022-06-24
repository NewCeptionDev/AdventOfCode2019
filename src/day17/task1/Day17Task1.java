package day17.task1;

import util.InputReader;
import util.IntCode;

import java.util.*;
import java.util.stream.Collectors;

public class Day17Task1 {

    public static void main(String[] args) {
        List<String> inputString = InputReader.read("src/day17/task1/input.txt");

        String[] splitted = inputString.get(0).split(",");

        new Day17Task1(Arrays.stream(splitted).map(Long::parseLong).collect(Collectors.toList()));
    }

    List<String> stringMap = new ArrayList<>();
    char[][] map;
    StringBuilder currLine = new StringBuilder();


    public Day17Task1(List<Long> in){
        IntCode intCode = new IntCode(in);

        Long result = 0L;

        while (result != null){
            result = intCode.runCode(true);

            if (result != null) {
                int output = Math.toIntExact(result);

                if(output == 10){
                    stringMap.add(currLine.toString());
                    currLine = new StringBuilder();
                } else {
                    char ascii = (char) output;
                    currLine.append(ascii);
                }
            }
        }

        transformToArray();

        System.out.println("Sum of Alignment Parameters: " + getAlignmentSum());
    }

    private void transformToArray(){
        map = new char[stringMap.size()][stringMap.get(0).length()];

        for(int i = 0; i < stringMap.size(); i++){
            char[] chars = stringMap.get(i).toCharArray();

            map[i] = chars;
        }

    }

    private int getAlignmentSum(){
        int sum = 0;

        for(int y = 0; y < map.length; y++){
            for(int x = 0; x < map[y].length; x++){
                if(isIntersection(x,y)){
                    sum += x * y;
                }
            }
        }

        return sum;
    }

    private boolean isIntersection(int x, int y){
        if(x > 0 && x < map[0].length-1){
            if(y > 0 && y < map.length-2){

                boolean isIntersection = true;
                if(!isStructure(x-1, y)){
                    isIntersection = false;
                }

                if(isIntersection && !isStructure(x+1, y)){
                    isIntersection = false;
                }

                if(isIntersection && !isStructure(x, y+1)){
                    isIntersection = false;
                }

                if(isIntersection && !isStructure(x, y-1)){
                    isIntersection = false;
                }

                return isIntersection;
            }
        }

        return false;
    }

    private boolean isStructure(int x, int y){
        return map[y][x] == '#';
    }

}
