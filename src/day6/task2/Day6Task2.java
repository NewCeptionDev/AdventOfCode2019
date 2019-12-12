package day6.task2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day6Task2 {
    public Map<String, Node> allNodes = new HashMap<>();

    public static void main(String[] args) {
        List<String> in = new ArrayList<>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("src/day6/task1/input.txt"));
            String line = reader.readLine();

            while (line != null) {
                in.add(line);
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        new Day6Task2(in);
    }

    public Day6Task2(List<String> input) {
        for (String s : input) {
            String[] parts = s.split("\\)");
            Node newNode = new Node(parts[1]);

            if (allNodes.containsKey(parts[0])) {
                Node upper = allNodes.get(parts[0]);
                newNode.add(upper);
                upper.addOrbiting(newNode);
            } else {
                Node newUpper = new Node(parts[0]);
                newNode.add(newUpper);
                allNodes.put(newUpper.getName(), newUpper);
                newUpper.addOrbiting(newNode);
            }

            allNodes.put(newNode.getName(), newNode);
        }

        System.out.println("All Orbits: " + getAllChildren());
        System.out.println("Shortest Way: " + findCrossPoint());
    }

    protected void showDependencies() {
        for (Node n : allNodes.values()) {
            System.out.println("Node " + n.getName() + ": ");
            n.printChilds();
            System.out.println("------");
        }
    }

    public int findCrossPoint(){
        Node you = allNodes.get("YOU");
        Node san = allNodes.get("SAN");

        Map<String, Integer> youUp = new HashMap<>();
        List<Node> toDo = you.getOrbits();
        toDo.addAll(you.getHasOrbiting());
        List<String> nextStep = new ArrayList<>();
        int distance = 0;
        while(!toDo.isEmpty()) {
            for(Node n : toDo){
                youUp.put(n.getName(), distance);
                List<Node> orbits = n.getOrbits();
                orbits.addAll(n.getHasOrbiting());

                for(Node n2 : orbits){
                    if(!youUp.containsKey(n2.name)){
                        nextStep.add(n2.getName());
                    }
                }
            }
            toDo = new ArrayList<>();
            for(String s : nextStep){
                toDo.add(allNodes.get(s));
            }
            distance++;
            nextStep = new ArrayList<>();
        }

        Map<String, Integer> sanUp = new HashMap<>();
        toDo = san.getOrbits();
        toDo.addAll(san.getHasOrbiting());
        nextStep = new ArrayList<>();
        distance = 0;
        while(!toDo.isEmpty()) {
            for(Node n : toDo){
                sanUp.put(n.getName(), distance);
                List<Node> orbits = allNodes.get(n.getName()).getOrbits();
                orbits.addAll(allNodes.get(n.getName()).getHasOrbiting());

                for(Node n2 : orbits){
                    if(!sanUp.containsKey(n2.name)){
                        nextStep.add(n2.getName());
                    }
                }
            }
            toDo = new ArrayList<>();
            for(String s : nextStep){
                toDo.add(allNodes.get(s));
            }
            distance++;
            nextStep = new ArrayList<>();
        }

        return minimumDistance(youUp, sanUp);
    }

    public int minimumDistance(Map<String, Integer> m1, Map<String, Integer> m2){
        int min = Integer.MAX_VALUE;
        for(String n : m1.keySet()){
            if(m2.containsKey(n)){
                int sum = m1.get(n) + m2.get(n);

                if(sum < min){
                    min = sum;
                }
            }
        }

        return min;
    }

    public int getAllChildren() {
        int sum = 0;
        for (String s : allNodes.keySet()) {
            sum += allNodes.get(s).getChilds();
        }

        return sum;
    }

    private class Node {
        private List<Node> orbits = new ArrayList<>();
        private List<Node> hasOrbiting = new ArrayList<>();
        private String name;

        public Node(String name) {
            this.name = name;
        }

        public void add(Node n) {
            orbits.add(n);
        }

        public void addOrbiting(Node n){
            hasOrbiting.add(n);
        }

        public List<Node> getHasOrbiting() {
            return hasOrbiting;
        }

        public String getName() {
            return name;
        }

        public List<Node> getOrbits(){
            return orbits;
        }

        public int getChilds() {
            int sum = 0;
            if (orbits.size() > 0) {
                sum = 1;
            }

            for (Node n : orbits) {
                sum += allNodes.get(n.getName()).getChilds();
            }

            return sum;
        }

        public void printChilds() {
            for (Node n : orbits) {
                System.out.println(n.getName());
            }
            System.out.println("Child Count: " + getChilds());
        }
    }
}
