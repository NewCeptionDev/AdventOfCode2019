package day20.task2;

import util.InputReader;
import util.Pair;

import java.util.*;

import static day20.task1.Day20Task1.calculatePossibleNextPositions;

public class Day20Task2 {

    public static void main(String[] args) {
        List<String> in = InputReader.read("src/day20/task1/input.txt");

        new Day20Task2(in);
    }

    Map<String, Portal> portals = new HashMap<>();

    public Day20Task2(List<String> lines) {
        Map<Integer, Map<Integer, Character>> map = parseMap(lines);

        // Update OuterPortal Flags
        for(Portal p : portals.values()) {
            boolean isOuter = p.getPos().getValue() == 2 || p.getPos().getValue() == map.size() - 3;
            final int checkX = p.getPos().getKey();
            isOuter |= checkX == 2 || map.get(p.getPos().getValue()).keySet().stream()
                    .allMatch(key -> key <= checkX + 2);

            p.setOuterPortal(isOuter);
        }

        Map<String, Map<String, Integer>> distanceToPortals = populateMemory(map);


        Queue<QueueElement> queue = new LinkedList<>();
        queue.add(new QueueElement("AA",
                List.of("AA_0"), 0, 0));

        List<String> should = new ArrayList<>();
        should.add("AA_0");
        should.add("FX");

        int maxDistance = Integer.MAX_VALUE;
        while (!queue.isEmpty()) {
            QueueElement element = queue.poll();

            Map<String, Integer> possible = distanceToPortals.get(element.getPortal());

            for (String portal : possible.keySet()) {
                Portal portalElement = portals.get(portal);
                Portal nextPortal = portalElement.getLink();

                int updatedDistance = element.getDistance() + possible.get(portal) + 1;
                if (portal.equals("ZZ") && element.getCurrentLevel() == 0) {
                    if (updatedDistance < maxDistance) {
                        maxDistance = element.getDistance() + possible.get(portal);
                    }
                } else if(nextPortal != null){
                    int updatedLevel =
                            element.getCurrentLevel() + (nextPortal.isOuterPortal() ? 1 : -1);
                    if (!portal.equals("AA") && updatedDistance < maxDistance && updatedLevel >= 0) {
                        List<String> newVisited = new ArrayList<>(element.getVisited());
                        newVisited.add(portal + "_" + element.getCurrentLevel());
                        queue.add(new QueueElement(nextPortal.getName(), newVisited,
                                updatedDistance, updatedLevel));
                    }
                }
            }
        }

        System.out.println("Shortest Distance: " + maxDistance);
    }

    public Map<String, Map<String, Integer>> populateMemory(
            Map<Integer, Map<Integer, Character>> map) {
        Set<String> portalIdents = portals.keySet();
        Map<String, Map<String, Integer>> memory = new HashMap<>();

        for (String portalIdent : portalIdents) {
            memory.put(portalIdent, new HashMap<>());

            Queue<SimpleQueueElement> queue = new LinkedList<>();
            queue.add(new SimpleQueueElement(portals.get(portalIdent).getPos(),
                    List.of(portals.get(portalIdent).getPos()), 0));

            while (!queue.isEmpty()) {
                SimpleQueueElement element = queue.poll();

                List<Pair<Integer, Integer>> possibleNextPositions =
                        calculatePossibleNextPositions(element.getCurrentPosition(),
                                map.size()).stream()
                                .filter(pos -> map.get(pos.getValue()).containsKey(pos.getKey()))
                                .filter(pos -> element.getVisited().stream().allMatch(
                                        visited -> !visited.getKey().equals(pos.getKey())
                                                || !visited.getValue().equals(pos.getValue())))
                                .toList();

                for (Pair<Integer, Integer> possible : possibleNextPositions) {
                    Character mapElement = map.get(possible.getValue()).get(possible.getKey());

                    if (Character.isLetter(mapElement)) {
                        Optional<Portal> portalOptional = portals.values().stream()
                                .filter(portal1 -> portal1.getPos().getKey()
                                        .equals(element.getCurrentPosition().getKey())
                                        && portal1.getPos().getValue()
                                        .equals(element.getCurrentPosition().getValue()))
                                .findFirst();

                        if (portalOptional.isPresent() && !portalOptional.get().getName()
                                .equals(portalIdent)) {
                            Portal portal = portalOptional.get();

                            memory.get(portalIdent)
                                    .put(portal.getName(), element.getDistance());
                        }
                    } else if (mapElement == '.') {
                        List<Pair<Integer, Integer>> newVisited =
                                new ArrayList<>(element.getVisited());
                        newVisited.add(possible);
                        queue.add(new SimpleQueueElement(possible, newVisited,
                                element.getDistance() + 1));
                    }
                }
            }
        }
        return memory;
    }

    public Map<Integer, Map<Integer, Character>> parseMap(List<String> lines) {
        Map<Integer, Map<Integer, Character>> map = new HashMap<>();

        for (int y = 0; y < lines.size(); y++) {
            map.put(y, new HashMap<>());
            for (int x = 0; x < lines.get(y).length(); x++) {
                char curr = lines.get(y).charAt(x);

                if (curr != ' ') {
                    map.get(y).put(x, curr);

                    if (Character.isLetter(curr)) {
                        Character secondLetter = null;

                        Pair<Integer, Integer> portalPosition = null;
                        if (map.containsKey(y - 1) && map.get(y - 1).containsKey(x)
                                && Character.isLetter(map.get(y - 1).get(x))) {
                            secondLetter = map.get(y - 1).get(x);

                            if (y > 1 && map.get(y - 2).containsKey(x)) {
                                portalPosition = new Pair<>(x, y - 2);
                            } else {
                                portalPosition = new Pair<>(x, y + 1);
                            }
                        } else if (map.containsKey(y) && map.get(y).containsKey(x - 1)
                                && Character.isLetter(map.get(y).get(x - 1))) {
                            secondLetter = map.get(y).get(x - 1);

                            if (map.get(y).containsKey(x - 2)) {
                                portalPosition = new Pair<>(x - 2, y);
                            } else {
                                portalPosition = new Pair<>(x + 1, y);
                            }
                        }

                        if (secondLetter != null) {
                            String portalIdent = curr + "" + secondLetter;
                            String secondPortalIdent;

                            if(portalIdent.equals("AA") || portalIdent.equals("ZZ")) {
                                secondPortalIdent = portalIdent;
                            } else if (portals.containsKey(portalIdent + "1")) {
                                secondPortalIdent = portalIdent + "1";
                                portalIdent = portalIdent + "2";
                            } else {
                                portalIdent = portalIdent + "1";
                                secondPortalIdent = portalIdent + "2";
                            }

                            Portal newPortal = new Portal(portalPosition, portalIdent);

                            if (portals.containsKey(secondPortalIdent)) {
                                portals.get(secondPortalIdent).setLink(newPortal);
                                newPortal.setLink(portals.get(secondPortalIdent));
                            }

                            portals.put(portalIdent, newPortal);
                        }
                    }
                }
            }
        }

        return map;
    }

    static class SimpleQueueElement {
        private Pair<Integer, Integer> currentPosition;
        private List<Pair<Integer, Integer>> visited;
        int distance;

        public SimpleQueueElement(Pair<Integer, Integer> currentPosition,
                List<Pair<Integer, Integer>> visited, int distance) {
            this.currentPosition = currentPosition;
            this.visited = visited;
            this.distance = distance;
        }

        public Pair<Integer, Integer> getCurrentPosition() {
            return currentPosition;
        }

        public List<Pair<Integer, Integer>> getVisited() {
            return visited;
        }

        public int getDistance() {
            return distance;
        }
    }

    static class QueueElement {
        private String portal;
        private List<String> visited;
        int currentLevel;
        int distance;

        public QueueElement(String portal,
                List<String> visited, int distance, int currentLevel) {
            this.portal = portal;
            this.distance = distance;
            this.visited = visited;
            this.currentLevel = currentLevel;
        }

        public List<String> getVisited() {
            return visited;
        }

        public int getDistance() {
            return distance;
        }

        public int getCurrentLevel() {
            return currentLevel;
        }

        public String getPortal() {
            return portal;
        }

        @Override public String toString() {
            return "QueueElement{" + "portal='" + portal + '\''
                    + ", distance=" + distance + '}';
        }
    }

    public static class Portal {
        private Pair<Integer, Integer> pos;
        private String name;
        private Portal link;
        private boolean isOuterPortal;

        public Portal(Pair<Integer, Integer> pos, String name) {
            this.pos = pos;
            this.name = name;
        }

        public void setOuterPortal(boolean outerPortal) {
            isOuterPortal = outerPortal;
        }

        public void setLink(Portal link) {
            this.link = link;
        }

        public Pair<Integer, Integer> getPos() {
            return pos;
        }

        public boolean isOuterPortal() {
            return isOuterPortal;
        }

        public String getName() {
            return name;
        }

        public Portal getLink() {
            return link;
        }

        @Override public String toString() {
            return "Portal{" + "name='" + name + '\'' + ", link=" + (link != null ? link.getName() : "") + ", outer: " + isOuterPortal + ", pos: " + pos + '}';
        }
    }

}
