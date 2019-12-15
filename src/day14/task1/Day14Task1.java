package day14.task1;

import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day14Task1 {

    //TODO

    public static void main(String[] args) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/day14/task1/input.txt"));
            List<String> lines = new ArrayList<>();
            String line = reader.readLine();

            while (line != null) {
                lines.add(line);
                line = reader.readLine();
            }

            new Day14Task1(lines.toArray(new String[0]));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Map<String, Recipe> recipes = new HashMap<>();
    private Map<String, Integer> needed = new HashMap<>();

    public Day14Task1(String[] rules) {
        for (String s : rules) {
            String[] inOut = s.split("=>");

            String[] out = inOut[1].trim().split(" ");
            int resAmount = Integer.parseInt(out[0]);
            String result = out[1];

            List<Pair<String, Integer>> ingredients = new ArrayList<>();
            String[] in = inOut[0].split(",");
            for (String s2 : in) {
                String[] ingredientSplit = s2.trim().split(" ");
                Pair<String, Integer> p =
                        new Pair<>(ingredientSplit[1], Integer.parseInt(ingredientSplit[0]));
                ingredients.add(p);
            }

            recipes.put(result, new Recipe(ingredients, result, resAmount));
        }

        Recipe result = recipes.get("FUEL");

        addAll(result, 1);

        int amountNeeded = 0;
        for (String s : needed.keySet()) {
            Recipe r = recipes.get(s);
            int amount = needed.get(s);

            int toCraft = amount % r.getResAmount() == 0
                    ? amount / r.getResAmount()
                    : amount / r.getResAmount() + 1;

            amountNeeded += toCraft * r.getResAmount();
        }

        System.out.println(amountNeeded);

    }

    private void addAll(Recipe r, int amount) {
        int toCraft = amount % r.getResAmount() == 0
                ? amount / r.getResAmount()
                : amount / r.getResAmount() + 1;

        for (Pair<String, Integer> p : r.getIngredients()) {
            if (!p.getKey().equals("ORE")) {
                addAll(recipes.get(p.getKey()), toCraft * p.getValue());
            } else {
                if (needed.containsKey(r.getResult())) {
                    needed.put(r.getResult(), needed.get(r.getResult()) + amount);
                } else {
                    needed.put(r.getResult(), amount);
                }
            }
        }
    }

    private class Recipe {
        String result;
        int resAmount;
        List<Pair<String, Integer>> ingredients;

        public Recipe(List<Pair<String, Integer>> ingredients, String result, int resAmount) {
            this.ingredients = ingredients;
            this.resAmount = resAmount;
            this.result = result;
        }

        public List<Pair<String, Integer>> getIngredients() {
            return ingredients;
        }

        public int getResAmount() {
            return resAmount;
        }

        public String getResult() {
            return result;
        }
    }

}
