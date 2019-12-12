package day8.task2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Day8Task2 {

    public static void main(String[] args) {
        new Day8Task2();
    }

    List<Layer> layers = new ArrayList<>();

    public Day8Task2(){
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

        Layer added = new Layer();


        for(Layer l : layers){

            for(int y = 0; y < 6; y++){
                for(int x = 0; x < 25; x++){
                    if(added.isTransparent(x,y)){
                        added.setColor(x,y,l.getColor(x,y));
                    }
                }
            }
        }

        for(int y = 0; y < 6; y++){
            for(int x = 0; x < 25; x++){
                int color = added.getColor(x,y);
                if(color == 1){
                    System.out.print("X");
                } else {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
    }

    private class Layer{
        int[][] layer = new int[6][25];

        public Layer(){
            for(int x = 0; x < 25; x++){
                for(int y = 0; y < 6; y++){
                    layer[y][x] = 2;
                }
            }
        }

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

        public boolean isTransparent(int x, int y){
            return layer[y][x] == 2;
        }

        public void setColor(int x, int y, int c){
            layer[y][x] = c;
        }

        public int getColor(int x, int y){
            return layer[y][x];
        }
    }
}
