package day12.task1;

import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Day12Task1 {

    private List<Moon> moons = new ArrayList<>();
    private List<Pair<Moon, Moon>> moonPairs = new ArrayList<>();

    public static void main(String[] args) {
        try {
            List<String> lines = new ArrayList<>();

            BufferedReader reader = new BufferedReader(new FileReader("src/day12/task1/input.txt"));
            String line = reader.readLine();

            while(line != null){
                lines.add(line);
                line = reader.readLine();
            }

            new Day12Task1(lines.stream().toArray(String[]::new), 1000);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Day12Task1(String[] moons, int steps){

        for(String s : moons){
            String withoutBrackets = s.substring(1, s.length()-1);
            String[] coordinates = withoutBrackets.split(",");
            int x = 0;
            int y = 0;
            int z = 0;
            for(int i = 0; i < coordinates.length; i++){
                String[] numbers = coordinates[i].split("=");
                switch (i){
                    case 0:
                        x = Integer.parseInt(numbers[1]);
                        break;
                    case 1:
                        y = Integer.parseInt(numbers[1]);
                        break;
                    case 2:
                        z = Integer.parseInt(numbers[1]);
                        break;
                }
            }

            this.moons.add(new Moon(x,y,z));
        }

        for(int i = 0; i < this.moons.size(); i++){
            for(int j = i+1; j < this.moons.size(); j++){
                moonPairs.add(new Pair<>(this.moons.get(i), this.moons.get(j)));
            }
        }

        int step = 0;

        while(step < steps){
            for (Pair<Moon, Moon> p : moonPairs){
                p.getKey().changeVelocity(p.getValue());
                p.getValue().changeVelocity(p.getKey());
            }

            for(Moon m : this.moons){
                m.move();
            }

            step++;
        }

        long sum = 0;

        for(Moon m : this.moons){
            sum += m.calculateEnergy();
        }

        System.out.println("Total Energy: " + sum);
    }

    private class Moon{
        int x;
        int y;
        int z;

        int velx;
        int vely;
        int velz;

        public Moon(int x, int y, int z){
            this.x = x;
            this.y = y;
            this.z = z;;
        }

        public void move(){
            x += velx;
            y += vely;
            z += velz;
        }

        public void changeVelocity(Moon m){
            if(x < m.getX()){
                velx += 1;
            } else if(x > m.getX()){
                velx -= 1;
            }

            if(y < m.getY()){
                vely += 1;
            } else if(y > m.getY()){
                vely -= 1;
            }

            if(z < m.getZ()){
                velz += 1;
            } else if(z > m.getZ()){
                velz -= 1;
            }
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getZ() {
            return z;
        }

        public long calculateEnergy(){
            long pot = Math.abs(x) + Math.abs(y) + Math.abs(z);
            long kin = Math.abs(velx) + Math.abs(vely) + Math.abs(velz);

            return pot * kin;
        }

        public void print(){
            System.out.println("pos=<x=" + x + ", y=" + y + ", z=" + z + ">, vel=<x=" + velx + ", y=" + vely + ", z=" + velz + ">");
        }
    }
}
