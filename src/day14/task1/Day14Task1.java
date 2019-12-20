package day14.task1;

import javafx.util.Pair;
import util.InputReader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day14Task1 {

    public static void main(String[] args) {
        List<String> in = InputReader.read("src/day14/task1/input.txt");

        new Day14Task1(in);
    }

    private Map<String, Integer> unUsedMats = new HashMap<>();
    private Map<String, Recipe> recipes = new HashMap<>();
    private int usedOre = 0;

    public Day14Task1(List<String> in){
        for(String s : in){
            Recipe r = parseRecipe(s);

            recipes.put(r.getResult(), r);
        }


    }

    private void calculate(Recipe r, int amount){

    }

    private void craftRecipe(Recipe r, int amount){
        int recipeCrafts = amount % r.getResAmount() == 0 ? amount / r.getResAmount() : amount / r.getResAmount() + 1;

            if(r.getIngredients().size() == 1 && r.getIngredients().get(0).getKey().equals("ORE")){
                usedOre += r.getResAmount() * recipeCrafts;

                if((r.getResAmount() * recipeCrafts) != amount){
                    int tooMuch = (r.getResAmount() * recipeCrafts) - amount;

                    if(unUsedMats.containsKey(r.getResult())){
                        unUsedMats.put(r.getResult(), unUsedMats.get(r.getResult()) + tooMuch);
                    }
                }
            } else {
                for(Pair<String, Integer> p : r.getIngredients()){
                    craftRecipe(recipes.get(p.getKey()), recipeCrafts * p.getValue());
                }
            }
    }

    private Recipe parseRecipe(String s){
        List<Pair<String, Integer>> ingredients = new ArrayList<>();

        String[] splitInOut = s.split("=>");

        String[] ing = splitInOut[0].split(",");
        for(String in : ing){
            String[] splitted = in.trim().split(" ");
            ingredients.add(new Pair<>(splitted[1], Integer.parseInt(splitted[0])));
        }

        String[] outSplit = splitInOut[1].trim().split(" ");

        return new Recipe(outSplit[1], Integer.parseInt(outSplit[0]), ingredients);
    }

    private class Recipe{
        private String result;
        private int resAmount;
        private List<Pair<String, Integer>> ingredients;

        public Recipe(String result, int resAmount, List<Pair<String, Integer>> ingredients){
            this.result = result;
            this.resAmount = resAmount;
            this.ingredients = ingredients;
        }

        public String getResult() {
            return result;
        }

        public int getResAmount() {
            return resAmount;
        }

        public List<Pair<String, Integer>> getIngredients() {
            return ingredients;
        }
    }
}
