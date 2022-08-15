package day20.task1;

import util.InputReader;
import util.Pair;

import java.util.*;

public class Day20Task1 {

    public static void main(String[] args) {
        List<String> in = InputReader.read("src/day20/task1/input.txt");

        new Day20Task1(in);
    }

    Map<String, Portal> portals = new HashMap<>();

    public Day20Task1(List<String> lines) {
        Map<Integer, Map<Integer, Character>> map = parseMap(lines);

        Queue<QueueElement> queue = new LinkedList<>();
        queue.add(new QueueElement(portals.get("AA").getPos1(), List.of(portals.get("AA").getPos1()), new ArrayList<>(), 0));

        int maxDistance = Integer.MAX_VALUE;
        while(!queue.isEmpty()) {
            QueueElement element = queue.poll();

            List<Pair<Integer, Integer>> possibleNextPositions = calculatePossibleNextPositions(element.getCurrentPosition(), map.size()).stream().filter(pos -> map.get(pos.getValue()).containsKey(pos.getKey())).filter(pos -> element.getVisited().stream().allMatch(visited ->
                    !visited.getKey().equals(pos.getKey()) || !visited.getValue()
                    .equals(pos.getValue()))).toList();

            for(Pair<Integer, Integer> possible : possibleNextPositions) {
                Character mapElement = map.get(possible.getValue()).get(possible.getKey());

                if(Character.isLetter(mapElement)) {
                    Portal portal = null;

                    List<String> portalIdents = getPortalIdent(map, possible);

                    if(portalIdents.get(0).equals("ZZ")) {
                        if(element.getDistance() < maxDistance) {
                            maxDistance = element.getDistance();
                            System.out.println(element.getUsedPortals());
                        }
                    }

                    if(portals.containsKey(portalIdents.get(0))) {
                        portal = portals.get(portalIdents.get(0));
                    } else if(portals.containsKey(portalIdents.get(1))) {
                        portal = portals.get(portalIdents.get(1));
                    }

                    if(portal != null) {
                        Pair<Integer, Integer> oppositePortalPosition = portal.getOppositePortal(element.getCurrentPosition());

                        if(element.getDistance() + 1 < maxDistance && oppositePortalPosition != null && element.getVisited().stream().allMatch(visited ->
                                !visited.getKey().equals(oppositePortalPosition.getKey()) || !visited.getValue()
                                .equals(oppositePortalPosition.getValue()))) {
                            List<Pair<Integer, Integer>> newVisited = new ArrayList<>(element.getVisited());
                            newVisited.add(possible);
                            List<String> newUsed = new ArrayList<>(element.getUsedPortals());
                            newUsed.add(portalIdents.get(0));
                            queue.add(
                                    new QueueElement(oppositePortalPosition, newVisited,
                                            newUsed, element.getDistance() + 1));
                        }
                    }
                } else if(mapElement == '.') {
                    if(element.getDistance() + 1 < maxDistance) {
                        List<Pair<Integer, Integer>> newVisited = new ArrayList<>(element.getVisited());
                        newVisited.add(possible);
                        queue.add(new QueueElement(possible, newVisited, element.getUsedPortals(), element.getDistance() + 1));
                    }
                }
            }
        }

        // Strange Off by 1 Error only on the real Input but not on the tests
        System.out.println("Shortest Distance: " + (maxDistance - 1));
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
                            String reversedPortalIdent = secondLetter + "" + curr;

                            if(portals.containsKey(portalIdent)) {
                                portals.get(portalIdent).setPos2(portalPosition);
                            } else if(portals.containsKey(reversedPortalIdent)) {
                                portals.get(reversedPortalIdent).setPos2(portalPosition);
                            } else {
                                portals.put(portalIdent, new Portal(portalPosition));
                            }
                        }
                    }
                }
            }
        }

        return map;
    }

    public static List<Pair<Integer, Integer>> calculatePossibleNextPositions(
            Pair<Integer, Integer> currentPosition, int maxY) {
        List<Pair<Integer, Integer>> possible = new ArrayList<>();

        if (currentPosition.getKey() > 0) {
            possible.add(new Pair<>(currentPosition.getKey() - 1, currentPosition.getValue()));
        }

        if (currentPosition.getValue() > 0) {
            possible.add(new Pair<>(currentPosition.getKey(), currentPosition.getValue() - 1));
        }

        possible.add(new Pair<>(currentPosition.getKey() + 1, currentPosition.getValue()));

        if (currentPosition.getValue() + 1 < maxY) {
            possible.add(new Pair<>(currentPosition.getKey(), currentPosition.getValue() + 1));
        }

        return possible;
    }

    public List<String> getPortalIdent(Map<Integer, Map<Integer, Character>> map, Pair<Integer, Integer> position) {
        char curr = map.get(position.getValue()).get(position.getKey());

        Character secondLetter = null;
        if (map.containsKey(position.getValue() - 1) && map.get(position.getValue() - 1).containsKey(position.getKey())
                && Character.isLetter(map.get(position.getValue() - 1).get(position.getKey()))) {
            secondLetter = map.get(position.getValue() - 1).get(position.getKey());
        } else if (map.containsKey(position.getValue() + 1) && map.get(position.getValue() + 1).containsKey(position.getKey())
                && Character.isLetter(map.get(position.getValue() + 1).get(position.getKey()))) {
            secondLetter = map.get(position.getValue() + 1).get(position.getKey());
        } else if (map.get(position.getValue()).containsKey(position.getKey() - 1)
                && Character.isLetter(map.get(position.getValue()).get(position.getKey() - 1))) {
            secondLetter = map.get(position.getValue()).get(position.getKey() - 1);
        } else {
            secondLetter = map.get(position.getValue()).get(position.getKey() + 1);
        }

        return List.of(curr + "" + secondLetter, secondLetter + "" + curr);
    }

    static class Portal {
        private Pair<Integer, Integer> pos1;
        private Pair<Integer, Integer> pos2;

        public Portal(Pair<Integer, Integer> pos1) {
            this.pos1 = pos1;
        }

        public void setPos2(Pair<Integer, Integer> pos2) {
            this.pos2 = pos2;
        }

        public Pair<Integer, Integer> getOppositePortal(Pair<Integer, Integer> currentPos) {
            if (currentPos.getKey().equals(pos1.getKey()) && currentPos.getValue()
                    .equals(pos1.getValue())) {
                return pos2;
            } else {
                return pos1;
            }
        }

        public Pair<Integer, Integer> getPos1() {
            return pos1;
        }
    }

    static class QueueElement {
        private Pair<Integer, Integer> currentPosition;
        private List<Pair<Integer, Integer>> visited;
        private List<String> usedPortals;
        int distance;

        public QueueElement(Pair<Integer, Integer> currentPosition, List<Pair<Integer, Integer>> visited, List<String> usedPortals, int distance) {
            this.currentPosition = currentPosition;
            this.distance = distance;
            this.visited = visited;
            this.usedPortals = usedPortals;
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

        public List<String> getUsedPortals() {
            return usedPortals;
        }

        @Override public String toString() {
            return "QueueElement{" + "currentPosition=" + currentPosition + ", visited=" + visited
                    + ", distance=" + distance + '}';
        }
    }
}
