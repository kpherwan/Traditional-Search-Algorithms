package work;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

class Node implements Comparable<Node> {
    public int x;
    public int y;
    public Node parent;
    public int pathCost;
    public int gCost;
    public int hCost;

    Node(int x, int y, Node parent) {
        this.x = x;
        this.y = y;
        this.parent = parent;
        this.pathCost = 0;
    }

    Node(int x, int y, Node parent, int pathCost) {
        this.x = x;
        this.y = y;
        this.parent = parent;
        this.pathCost = pathCost;
    }

    Node(int x, int y, Node parent, int gCost, int hCost) {
        this.x = x;
        this.y = y;
        this.parent = parent;
        this.pathCost = gCost + hCost;
        this.gCost = gCost;
        this.hCost = hCost;
    }

    public String toString(){
        return x + "_" + y;
    }

    @Override
    public int compareTo(Node o) {
        if(this.pathCost > o.pathCost) {
            return 1;
        } else if (this.pathCost < o.pathCost) {
            return -1;
        } else {
            return 0;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return x == node.x && y == node.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}

public class homework {

    private static final String BFS = "BFS";
    private static final String UCS = "UCS";
    private static final String A_STAR = "A*";

    public static void main(String[] args) {
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File("src/work/input8.txt"));
            //scanner = new Scanner(new File("input.txt"));
            String typeOfAlgorithm = scanner.nextLine();

            String[] dimensions = scanner.nextLine().split(" ");
            int columnWidth = Integer.parseInt(dimensions[0]);
            int rowHeight = Integer.parseInt(dimensions[1]);
            int grid[][] = new int[rowHeight][columnWidth];

            String[] startingPos = scanner.nextLine().split(" ");
            Node startingCoord = new Node(Integer.parseInt(startingPos[0]), Integer.parseInt(startingPos[1]), null);

            int maxRockHeight = Integer.parseInt(scanner.nextLine());

            int noOfSites = Integer.parseInt(scanner.nextLine());
            Map<String, Node> allDestinations = new LinkedHashMap<>();

            for(int i=0; i<noOfSites; i++) {
                String[] site = scanner.nextLine().split(" ");
                Node siteCoord = new Node(Integer.parseInt(site[0]), Integer.parseInt(site[1]), null);
                allDestinations.put(siteCoord.toString(), new Node(siteCoord.x, siteCoord.y, null));
            }

            for(int i=0; i<rowHeight; i++) {
                String[] nextRow = scanner.nextLine().split(" ");
                for(int j=0; j<columnWidth; j++) {
                    grid[i][j] = Integer.parseInt(nextRow[j]);
                }
            }

            switch(typeOfAlgorithm) {
                case BFS:
                    solveUsingBFS(columnWidth, rowHeight, startingCoord, maxRockHeight, noOfSites, allDestinations, grid);
                    break;

                case UCS:
                    solveUsingUCS(columnWidth, rowHeight, startingCoord, maxRockHeight, noOfSites, allDestinations, grid);
                    break;

                case A_STAR:
                    for (Map.Entry<String, Node> entry : allDestinations.entrySet()) {
                        Node result = getResultUsingAStar(columnWidth, rowHeight, startingCoord, maxRockHeight, grid, entry.getValue());
                        allDestinations.put(entry.getKey(), result);
                    }
                    addToOutputFile(allDestinations);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void solveUsingBFS(int columnWidth, int rowHeight, Node startingCoord, int maxRockHeight,
                                      int noOfSites, Map<String, Node> allSitesPaths, int[][] grid) {
        Queue<Node> queue = new LinkedList<>();
        boolean closed[][] = new boolean[rowHeight][columnWidth];
        closed[startingCoord.y][startingCoord.x] = true;
        queue.add(startingCoord);

        int noOfPendingSites = noOfSites;

        while(!queue.isEmpty() && noOfPendingSites  != 0) {
            Node currentCoord = queue.peek();
            if(allSitesPaths.get(currentCoord.toString()) != null) {
                noOfPendingSites--;
                allSitesPaths.put(currentCoord.toString(), currentCoord);
            }

            queue.poll();

            List<Node> allNeighbors = getAllUnclosedNeighboursBFS(grid, currentCoord, columnWidth, rowHeight, maxRockHeight, closed);
            if (!allNeighbors.isEmpty()) {
                queue.addAll(allNeighbors);
            }
        }

        addToOutputFile(allSitesPaths);
    }



    private static List<Node> getAllUnclosedNeighboursBFS(int[][] grid, Node currentCoord, int columnWidth, int rowHeight,
                                                          int maxRockHeight, boolean[][] closed) {
        int currentHeight = grid[currentCoord.y][currentCoord.x] < 0 ? Math.abs(grid[currentCoord.y][currentCoord.x]) : 0;
        List<Node> neighbours = new ArrayList<>();

        for(int y = Math.max(0, currentCoord.y-1); y <= Math.min(currentCoord.y+1, rowHeight-1); y++){
                for(int x = Math.max(0, currentCoord.x-1); x <= Math.min(currentCoord.x+1, columnWidth-1); x++){
                    Node neighbour = new Node(x,y, currentCoord);
                    if((neighbour.x != currentCoord.x || neighbour.y != currentCoord.y) && !closed[y][x]){
                        int height = grid[y][x] < 0 ? Math.abs(grid[y][x]) : 0;
                        if (Math.abs(currentHeight - height) <= maxRockHeight) {
                            neighbours.add(neighbour);
                            closed[neighbour.y][neighbour.x] = true;
                    }
                }
            }
        }
        return neighbours;
    }

    private static void solveUsingUCS(int columnWidth, int rowHeight, Node startingCoord, int maxRockHeight,
                                      int noOfSites, Map<String, Node> allSitesPaths, int[][] grid) {
        PriorityQueue<Node> ucsOpenQueue = new PriorityQueue<>();
        Map<String, Integer> costMap = new HashMap<>();
        Set<Node> closed = new HashSet<>();
        ucsOpenQueue.add(startingCoord);
        int distance = 0;
        costMap.put(startingCoord.toString(), 0);

        int noOfPendingSites = noOfSites;

        while(!ucsOpenQueue.isEmpty() && noOfPendingSites  != 0) {
            Node currentNode = ucsOpenQueue.poll();
            closed.add(currentNode);
            if(allSitesPaths.get(currentNode.toString()) != null) {
                System.out.println("Goal reached " + currentNode.toString());
                noOfPendingSites--;
                allSitesPaths.put(currentNode.toString(), currentNode);
            }

            int currentHeight = grid[currentNode.y][currentNode.x] < 0 ? Math.abs(grid[currentNode.y][currentNode.x]) : 0;

            for(int y = Math.max(0, currentNode.y-1); y <= Math.min(currentNode.y+1, rowHeight-1); y++){
                for(int x = Math.max(0, currentNode.x-1); x <= Math.min(currentNode.x+1, columnWidth-1); x++){
                    //not the same node as current
                    if(x != currentNode.x || y != currentNode.y){
                        int height = grid[y][x] < 0 ? Math.abs(grid[y][x]) : 0;
                        if (Math.abs(currentHeight - height) <= maxRockHeight) {
                            distance = (x != currentNode.x && y != currentNode.y) ? 14 : 10;
                            Node child = new Node(x,y, currentNode, currentNode.pathCost + distance);
                            //add node to queue if node has not been explored
                            if (!closed.contains(child) && !ucsOpenQueue.contains(child)) {
                                ucsOpenQueue.add(child);
                                costMap.put(child.toString(), child.pathCost);
                            }
                            //current path is shorter than previous path found
                            else if(ucsOpenQueue.contains(child) && child.pathCost < costMap.get(child.toString())) {
                                ucsOpenQueue.remove(child);
                                ucsOpenQueue.add(child);
                                costMap.put(child.toString(), child.pathCost);
                            }
                            else if(closed.contains(child) && child.pathCost < costMap.get(child.toString())) {
                                closed.remove(child);
                                ucsOpenQueue.add(child);
                                costMap.put(child.toString(), child.pathCost);
                            }
                        }
                    }
                }
            }
        }

        addToOutputFile(allSitesPaths);
    }

    private static Node getResultUsingAStar(int columnWidth, int rowHeight, Node startingCoord, int maxRockHeight,
                                            int grid[][], Node goal) {
        PriorityQueue<Node> aStarOpenQueue = new PriorityQueue<>();
        Map<String, Integer> costMap = new HashMap<>();
        Set<Node> closed = new HashSet<>();
        aStarOpenQueue.add(startingCoord);
        int distance = 0;
        costMap.put(startingCoord.toString(), 0);

        while(!aStarOpenQueue.isEmpty()) {
            Node currentNode = aStarOpenQueue.poll();
            closed.add(currentNode);
            if(currentNode.x == goal.x && currentNode.y == goal.y) {
                System.out.println("Goal reached " + currentNode.toString());
                return currentNode;
            }

            int currentHeight = grid[currentNode.y][currentNode.x] < 0 ? Math.abs(grid[currentNode.y][currentNode.x]) : 0;

            int gCost, hCost;
            for(int y = Math.max(0, currentNode.y-1); y <= Math.min(currentNode.y+1, rowHeight-1); y++){
                for(int x = Math.max(0, currentNode.x-1); x <= Math.min(currentNode.x+1, columnWidth-1); x++){
                    //not the same node as current
                    if(x != currentNode.x || y != currentNode.y){
                        int height = grid[y][x] < 0 ? Math.abs(grid[y][x]) : 0;
                        if (Math.abs(currentHeight - height) <= maxRockHeight) {
                            // movement cost
                            gCost = (x != currentNode.x && y != currentNode.y) ? 14 : 10;
                            
                            //muddiness cost
                            gCost += grid[y][x] > 0 ? Math.abs(grid[y][x]) : 0;

                            //height cost
                            gCost += Math.abs(currentHeight - height);

                            //distance till parent
                            gCost += currentNode.gCost;

                            //heuristic cost
                            hCost = Math.max(Math.abs(x - goal.x), Math.abs(y - goal.y));

                            distance = gCost + hCost;
                            Node child = new Node(x,y, currentNode, gCost, hCost);

                            //add node to queue if node has not been explored
                            if (!closed.contains(child) && !aStarOpenQueue.contains(child)) {
                                aStarOpenQueue.add(child);
                                costMap.put(child.toString(), distance);
                            }
                            //current path is shorter than previous path found
                            else if(aStarOpenQueue.contains(child) && distance < costMap.get(child.toString())) {
                                System.out.println("Replacing cost as better found");
                                aStarOpenQueue.remove(child);
                                aStarOpenQueue.add(child);
                                costMap.put(child.toString(), child.pathCost);
                            }
                            else if(closed.contains(child) && distance < costMap.get(child.toString())) {
                                System.out.println("Replacing cost as better found");
                                closed.remove(child);
                                aStarOpenQueue.add(child);
                                costMap.put(child.toString(), child.pathCost);
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    private static void addToOutputFile(Map<String, Node> allSitesPaths) {

        try {
            FileWriter fw = new FileWriter("output.txt");

            for (Map.Entry<String, Node> entry : allSitesPaths.entrySet()) {
                System.out.println("path cost " + entry.getValue().pathCost);
                if (entry.getValue() == null || entry.getValue().parent == null) {
                    fw.write("FAIL");
                } else {
                    Node currentCoord = entry.getValue();
                    StringBuilder sbr = new StringBuilder();
                    while (currentCoord != null) {
                        sbr.insert(0, currentCoord.x + "," + currentCoord.y + " ");
                        currentCoord = currentCoord.parent;
                    }
                    fw.write(sbr.toString() + "\n");
                }
            }
            fw.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
