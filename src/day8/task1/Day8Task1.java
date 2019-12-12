package day8.task1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Day8Task1 {

    public static void main(String[] args) {
        new Day8Task1();
    }

    List<Layer> layers = new ArrayList<>();

    public Day8Task1(){
        try {
            int currRow = 0;
            int currCol = 0;
            int[] row = new int[25];
            Layer l = new Layer();

            BufferedReader reader = new BufferedReader(new FileReader("src/day8/task1/input.txt"));
            String line = reader.readLine();
            String[] splitted = line.split("");

            for(String s : splitted){
                if(currCol < 25){
                    row[currCol] = Integer.parseInt(s);
                    currCol++;

                    if(currCol == 25){
                        l.fillRow(row, currRow);
                        row = new int[25];

                        if(currRow < 5){
                            currCol = 0;
                            currRow++;
                        } else if(currRow == 5) {
                            layers.add(l);
                            l = new Layer();
                            currRow = 0;
                            currCol = 0;
                        }
                    }
                } else {
                    System.err.println("Should not have happend");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        Layer bestLayer = null;
        int minimumZero = Integer.MAX_VALUE;

        for(Layer l : layers){
            if(l.countNumber(0) < minimumZero){
                bestLayer = l;
                minimumZero = l.countNumber(0);
            }
        }

        int countOne = bestLayer.countNumber(1);
        int countTwo = bestLayer.countNumber(2);

        System.out.println(countOne * countTwo);
    }

    private class Layer{
        int[][] layer = new int[25][6];

        public void fillRow(int[] i, int row){
            layer[row] = i;
        }

        public int countNumber(int i){
            int sum = 0;

            for(int[] row : layer){
                for(int col : row){
                    if(col == i){
                        sum++;
                    }
                }
            }

            return sum;
        }
    }

}
