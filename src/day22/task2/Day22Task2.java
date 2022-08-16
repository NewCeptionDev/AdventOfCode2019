package day22.task2;

import util.InputReader;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.math.BigInteger.*;
import static java.util.stream.Collectors.toCollection;

// Taken from https://github.com/saidaspen/aoc2019/blob/master/java/src/main/java/se/saidaspen/aoc2019/day22/Day22.java
public class Day22Task2 {

    public static void main(String[] args) {
        List<String> instructions = InputReader.read("src/day22/task1/input.txt");

        new Day22Task2(instructions);
    }

    private String input;

    private BigInteger nCards = valueOf(119315717514047L);
    private BigInteger nShuffles = valueOf(101741582076661L);

    public Day22Task2(List<String> instructions) {
        System.out.println("2020 is at Position: " + cardAt(instructions, valueOf(2020)));
    }

    public static class LinFunc {
        BigInteger k;
        BigInteger m;

        public LinFunc(BigInteger k, BigInteger m) {
            this.k = k;
            this.m = m;
        }

        BigInteger apply(BigInteger x) {
            return x.multiply(k).add(m);
        }
    }

    private static final LinFunc ID = new LinFunc(ONE, ZERO);

    private LinFunc agg(LinFunc f, LinFunc g) {
        return new LinFunc(g.k.multiply(f.k), g.k.multiply(f.m).add(g.m));
    }

    BigInteger cardAt(List<String> instructions, BigInteger in) {
        LinFunc shuffle = reverse(instructions.stream().map(line -> {
            String[] split = line.split(" ");

            if (split[0].equals("cut")) {
                BigInteger toCut = new BigInteger(split[1]);
                return new LinFunc(ONE, toCut.mod(nCards));
            } else if (split[1].equals("into")) {
                return new LinFunc(ONE.negate(), ONE.negate().subtract(nCards));
            } else {
                BigInteger increment = new BigInteger(split[3]).modInverse(nCards);
                return new LinFunc(ONE.multiply(increment).mod(nCards), ZERO);
            }
        })).reduce(ID, this::agg);

        return executeTimes(shuffle.k, shuffle.m, nShuffles).apply(in).mod(nCards);
    }

    public static <T> Stream<T> reverse(Stream<T> stream) {
        Iterable<T> iterable = () -> stream.collect(toCollection(LinkedList::new)).descendingIterator();
        return StreamSupport.stream(iterable.spliterator(), false);
    }

    private LinFunc executeTimes(BigInteger k, BigInteger m, BigInteger nShuffles) {
        if (nShuffles.equals(ZERO)) {
            return ID;
        } else if (nShuffles.mod(TWO).equals(ZERO)) {
            return executeTimes(k.multiply(k).mod(nCards), k.multiply(m).add(m).mod(nCards), nShuffles.divide(TWO));
        } else {
            LinFunc cd = executeTimes(k, m, nShuffles.subtract(ONE));
            return new LinFunc(k.multiply(cd.k).mod(nCards), k.multiply(cd.m).add(m).mod(nCards));
        }
    }
}
