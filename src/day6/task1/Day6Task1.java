package day6.task1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day6Task1 {

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
        new Day6Task1(in);
    }

    public Day6Task1(List<String> input) {
        for (String s : input) {
            String[] parts = s.split("\\)");
            Node newNode = new Node(parts[1]);

            if (allNodes.containsKey(parts[0])) {
                Node upper = allNodes.get(parts[0]);
                newNode.add(upper);
            } else {
                Node newUpper = new Node(parts[0]);
                newNode.add(newUpper);
                allNodes.put(newUpper.getName(), newUpper);
            }

            allNodes.put(newNode.getName(), newNode);
        }

        System.out.println("All Orbits: " + getAllChildren());
    }

    protected void showDependencies(){
        for(Node n : allNodes.values()){
            System.out.println("Node " + n.getName() + ": ");
            n.printChilds();
            System.out.println("------");
        }
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
        private String name;

        public Node(String name) {
            this.name = name;
        }

        public void add(Node n) {
            orbits.add(n);
        }

        public String getName() {
            return name;
        }

        public int getChilds() {
            int sum = 0;
            if(orbits.size() > 0){
                sum = 1;
            }

            for (Node n : orbits) {
                sum += allNodes.get(n.getName()).getChilds();
            }

            return sum;
        }

        public void printChilds(){
            for(Node n : orbits){
                System.out.println(n.getName());
            }
            System.out.println("Child Count: " + getChilds());
        }
    }

}
