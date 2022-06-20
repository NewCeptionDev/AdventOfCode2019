package day22.task1;

import util.InputReader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day22Task1 {

    public static void main(String[] args) {
        List<String> instructions = InputReader.read("src/day22/task1/input.txt");

        new Day22Task1(instructions);
    }

    private List<Integer> deck;

    public Day22Task1(List<String> instructions) {

        deck = IntStream.iterate(0, i -> i + 1).limit(10007).boxed().collect(Collectors.toList());
        //deck = IntStream.iterate(0, i -> i + 1).limit(10).boxed().collect(Collectors.toList()); //JUST FOR TESTS

        for (String s : instructions) {
            String[] splitted = s.split(" ");

            if (splitted[0].equals("cut")) {
                int toCut = Integer.parseInt(splitted[1]);
                cutDeck(toCut);
            } else if (splitted[1].equals("into")) {
                reverseDeck();
            } else {
                int increment = Integer.parseInt(splitted[3]);
                incrementDeck(increment);
            }
        }

        System.out.println("Finished: " + deck);
        System.out.println("2019 is at Position: " + deck.indexOf(2019));
    }

    private void cutDeck(int count) {
        List<Integer> newDeck;

        if (count < 0) {
            List<Integer> cut = deck.subList(deck.size() + count, deck.size());
            newDeck = new ArrayList<>(cut);
            newDeck.addAll(deck.subList(0, deck.size() + count));
        } else {
            List<Integer> cut = deck.subList(0, count);
            newDeck = new ArrayList<>(deck.subList(count, deck.size()));
            newDeck.addAll(cut);
        }

        deck = newDeck;
        System.out.println("Cutted: " + deck);
    }

    private void reverseDeck() {
        Collections.reverse(deck);

        System.out.println("Reversed: " + deck);
    }

    private void incrementDeck(int increment) {
        List<Integer> newDeck = new ArrayList<>();
        for(int i = 0; i < deck.size(); i++){
            newDeck.add(0);
        }

        increment--;

        int i = 0;
        for (int j = 0; j < deck.size(); j++, i += increment) {
            if (i > deck.size() - 1) {
                i -= deck.size();
            }

            newDeck.set(i, deck.get(j));
            i++;
        }

        deck = newDeck;
        System.out.println("Incremented: " + deck);
    }
}
