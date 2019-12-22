package day22.task2;

import util.InputReader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class Day22Task2 {

    public static void main(String[] args) {
        List<String> instructions = InputReader.read("src/day22/task1/input.txt");

        new Day22Task2(instructions);
    }

    private List<Long> deck;

    public Day22Task2(List<String> instructions) {

        deck = LongStream.iterate(0, i -> i + 1).limit(119315717514047L).boxed().collect(Collectors.toList());
        //deck = IntStream.iterate(0, i -> i + 1).limit(10).boxed().collect(Collectors.toList()); //JUST FOR TESTS

        for (long i = 0; i < 101741582076661L; i++) {
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
        }

        System.out.println("Finished: " + deck);
        System.out.println("2020 is at Position: " + deck.indexOf(2020));
    }

    private void cutDeck(int count) {
        List<Long> newDeck;

        if (count < 0) {
            List<Long> cut = deck.subList(deck.size() + count, deck.size());
            newDeck = new ArrayList<>(cut);
            newDeck.addAll(deck.subList(0, deck.size() + count));
        } else {
            List<Long> cut = deck.subList(0, count);
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
        List<Long> newDeck = LongStream.iterate(0, i -> i).limit(deck.size()).boxed().collect(Collectors.toList());

        increment--;

        int i = 0;
        for (int j = 0; j < deck.size(); j++, i += increment) {
            if (i > deck.size() - 1) {
                i -= deck.size();
            }

            newDeck.set(i, deck.get(j));
            i++;
        }
    }
}
