package day14.task2;

import util.Pair;
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

    private Map<String, Long> unUsedMats = new HashMap<>();
    private Map<String, Recipe> recipes = new HashMap<>();
    private Map<String, Integer> usedForOneCraft = new HashMap<>();
    private long usedOre = 0;
    private boolean isDone = false;

    public Day14Task2(List<String> in) {
        for (String s : in) {
            Recipe r = parseRecipe(s);

            recipes.put(r.getResult(), r);
        }

        long crafted = 0;


        while (!isDone) {
            while (!hasEnoughRessources() && usedOre < 1000000000000L) {
                craftRecipe(recipes.get("FUEL"), 1);
                crafted++;
            }
            craftRecipeWithLimitedResources(unUsedMats, recipes.get("FUEL"), 1);
            crafted++;

        }

        System.out.println("Crafted: " + (crafted - 1));
    }

    private boolean hasEnoughRessources() {
        boolean hasEnough = true;

        for (String s : usedForOneCraft.keySet()) {
                if (unUsedMats.containsKey(s)) {
                    if (unUsedMats.get(s) < usedForOneCraft.get(s)) {
                        hasEnough = false;
                    }
                } else {
                    hasEnough = false;
                }
        }

        return hasEnough;
    }

    private void craftRecipe(Recipe r, int amount) {
        if (usedForOneCraft.containsKey(r.getResult())) {
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
                    unUsedMats.put(r.getResult(), (long) tooMuch);
                }
            }
        }
    }

    private void craftRecipeWithLimitedResources(Map<String, Long> resource, Recipe r, int amount) {
        if (resource.containsKey(r.getResult())) {
            if (resource.get(r.getResult()) > amount) {
                resource.put(r.getResult(), resource.get(r.getResult()) - amount);
                amount = 0;
            } else {
                amount -= resource.get(r.getResult());
                resource.remove(r.getResult());
            }
        }

        if (amount > 0) {
            int recipeCrafts = amount % r.getResAmount() == 0 ? amount / r.getResAmount() : amount / r.getResAmount() + 1;

            if (r.getIngredients().size() == 1 && r.getIngredients().get(0).getKey().equals("ORE")) {
                if (((long) (r.getIngredients().get(0).getValue() * recipeCrafts + usedOre)) < 1000000000000L) {
                    usedOre += r.getIngredients().get(0).getValue() * recipeCrafts;
                } else {
                    isDone = true;
                    return;
                }
            } else {
                for (Pair<String, Integer> p : r.getIngredients()) {
                    craftRecipeWithLimitedResources(resource, recipes.get(p.getKey()), recipeCrafts * p.getValue());
                }
            }

            if ((r.getResAmount() * recipeCrafts) != amount) {
                int tooMuch = (r.getResAmount() * recipeCrafts) - amount;

                if (resource.containsKey(r.getResult())) {
                    resource.put(r.getResult(), resource.get(r.getResult()) + tooMuch);
                } else {
                    resource.put(r.getResult(), (long) tooMuch);
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
