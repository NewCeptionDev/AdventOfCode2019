package day13.task2;

import javafx.util.Pair;
import util.InputReader;
import util.IntCodeComputer;

import java.util.*;
import java.util.stream.Collectors;

public class Day13Task2 {

    //TODO

    public static void main(String[] args) {
        List<String> input = InputReader.read("src/day13/task1/input.txt");

        new Day13Task2(Arrays.stream(input.get(0).split(",")).map(Integer::parseInt).collect(
                Collectors.toList()));
    }


    private Pair<Integer, Integer> ballPosition = null;
    private Pair<Integer, Integer> paddlePosition = null;

    public Day13Task2(List<Integer> input){
        Map<Integer, Long> in = new HashMap<>();
        for(int i = 0; i < input.size(); i++){
            in.put(i, (long) input.get(i));
        }

        IntCodeComputer c = new IntCodeComputer(in, new ArrayList<>());

        Set<Pair<Integer, Integer>> blockPositions = new HashSet<>();

        boolean end = false;
        int score = 0;
        boolean ballChanged = false;

        while(!c.isRealDone() && !end){
            c.processCode();
            if (!c.isRealDone()) {
                int x = Math.toIntExact(c.getOutputs().get(0));
                c.continueProcess();
                int y = Math.toIntExact(c.getOutputs().get(0));
                c.continueProcess();
                int type = Math.toIntExact(c.getOutputs().get(0));

                if(x == -1 && y == 0){
                    score = type;
                }

                if(type == 2){
                    blockPositions.add(new Pair<>(x,y));
                } else {
                    if(type == 4){
                        ballPosition = new Pair<>(x,y);
                        ballChanged = true;

                    } else if(type == 3){
                        paddlePosition = new Pair<>(x,y);
                    }

                    blockPositions.remove(new Pair<>(x,y));
                    if(blockPositions.size() == 0){
                        end = true;
                    }
                }

                if(ballChanged){
                    c.updateInputs(onBallPosChange());
                    ballChanged = false;
                } else {
                    c.continueProcess();
                }
            }
        }

        System.out.println("Score at the End: " + score);
    }

    public List<Long> onBallPosChange(){
        List<Long> newInputs = new ArrayList<>();

        System.out.println("run");

        if (paddlePosition != null) {
            if(ballPosition.getKey() < paddlePosition.getKey()){
                newInputs.add(-1L);
            } else if(ballPosition.getKey().equals(paddlePosition.getKey())){
                newInputs.add(0L);
            } else {
                newInputs.add(1L);
            }
        } else {
            newInputs.add(-1L);
        }

        return newInputs;
    }

}
