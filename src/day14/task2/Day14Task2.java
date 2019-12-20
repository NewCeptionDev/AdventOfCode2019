package day14.task2;

import day14.task1.Day14Task1;
import javafx.util.Pair;
import util.InputReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day14Task2 {

    public static void main(String[] args) {
        List<String> in = InputReader.read("src/day14/task1/input.txt");

        new Day14Task2(in);
    }

    private Map<String, Integer> unUsedMats = new HashMap<>();
    private Map<String, Recipe> recipes = new HashMap<>();
    private Map<String, Integer> usedForOneCraft = new HashMap<>();
    private Map<String, Integer> totalUnUsedMats;
    private long usedOre = 0;

    public Day14Task2(List<String> in) {
        for (String s : in) {
            Recipe r = parseRecipe(s);

            recipes.put(r.getResult(), r);
        }

        craftRecipe(recipes.get("FUEL"), 1);

        int seperateCrafts = Math.toIntExact(1000000000000L / usedOre);

        System.out.println("Unused Mats: " + unUsedMats);

        totalUnUsedMats = new HashMap<>(unUsedMats);
        totalUnUsedMats.replaceAll((s, v) -> v * seperateCrafts);

        System.out.println(totalUnUsedMats);

        int extraCrafts = 0;

        while(hasEnoughRessources()){
            for(String s : usedForOneCraft.keySet()){
                totalUnUsedMats.put(s, totalUnUsedMats.get(s) - usedForOneCraft.get(s));
            }
            extraCrafts++;
        }

        System.out.println("Seperate: " + seperateCrafts);
        System.out.println("Crafted: " + (seperateCrafts + extraCrafts));
    }

    //TODO Craft with limited Resources

    private boolean hasEnoughRessources(){
        boolean hasEnough = true;

        for(String s : usedForOneCraft.keySet()){
            if (usedForOneCraft.containsKey(s)) {
                if (totalUnUsedMats.containsKey(s)) {
                    if(totalUnUsedMats.get(s) < usedForOneCraft.get(s)){
                        hasEnough = false;
                    }
                } else {
                    System.out.println("Total does not contain " + s);
                    hasEnough = false;
                }
            }
        }

        return hasEnough;
    }

    private void craftRecipe(Recipe r, int amount) {
        if(usedForOneCraft.containsKey(r.getResult())){
            usedForOneCraft.put(r.getResult(), usedForOneCraft.get(r.getResult()) + amount);
        } else {
            usedForOneCraft.put(r.getResult(), amount);
        }

        if (unUsedMats.containsKey(r.getResult())) {
            if (unUsedMats.get(r.getResult()) > amount) {
                unUsedMats.put(r.getResult(), unUsedMats.get(r.getResult()) - amount);
                amount = 0;
            } else {
                amount -= unUsedMats.get(r.getResult());
                unUsedMats.remove(r.getResult());
            }
        }

        if (amount > 0) {
            int recipeCrafts = amount % r.getResAmount() == 0 ? amount / r.getResAmount() : amount / r.getResAmount() + 1;

            if (r.getIngredients().size() == 1 && r.getIngredients().get(0).getKey().equals("ORE")) {
                usedOre += r.getIngredients().get(0).getValue() * recipeCrafts;
            } else {
                for (Pair<String, Integer> p : r.getIngredients()) {
                    craftRecipe(recipes.get(p.getKey()), recipeCrafts * p.getValue());
                }
            }

            if ((r.getResAmount() * recipeCrafts) != amount) {
                int tooMuch = (r.getResAmount() * recipeCrafts) - amount;

                if (unUsedMats.containsKey(r.getResult())) {
                    unUsedMats.put(r.getResult(), unUsedMats.get(r.getResult()) + tooMuch);
                } else {
                    unUsedMats.put(r.getResult(), tooMuch);
                }
            }
        }
    }

    private Recipe parseRecipe(String s) {
        List<Pair<String, Integer>> ingredients = new ArrayList<>();

        String[] splitInOut = s.split("=>");

        String[] ing = splitInOut[0].split(",");
        for (String in : ing) {
            String[] splitted = in.trim().split(" ");
            ingredients.add(new Pair<>(splitted[1], Integer.parseInt(splitted[0])));
        }

        String[] outSplit = splitInOut[1].trim().split(" ");

        return new Recipe(outSplit[1], Integer.parseInt(outSplit[0]), ingredients);
    }

    private class Recipe {
        private String result;
        private int resAmount;
        private List<Pair<String, Integer>> ingredients;

        public Recipe(String result, int resAmount, List<Pair<String, Integer>> ingredients) {
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
