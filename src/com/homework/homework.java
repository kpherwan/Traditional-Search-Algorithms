package com.homework;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

class Coordinate {
    int x;
    int y;
    Coordinate parent;

    Coordinate(int x, int y, Coordinate parent) {
        this.x = x;
        this.y = y;
        this.parent = parent;
    }

    public String toString(){
        return x + "_" + y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinate that = (Coordinate) o;
        return x == that.x && y == that.y;
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
            scanner = new Scanner(new File("src/com/homework/input4.txt"));
            //scanner = new Scanner(new File("input.txt"));
            String typeOfAlgorithm = scanner.nextLine();

            String[] dimensions = scanner.nextLine().split(" ");
            int columnWidth = Integer.parseInt(dimensions[0]);
            int rowHeight = Integer.parseInt(dimensions[1]);
            int matrix[][] = new int[rowHeight][columnWidth];

            String[] startingPos = scanner.nextLine().split(" ");
            Coordinate startingCoord = new Coordinate(Integer.parseInt(startingPos[0]), Integer.parseInt(startingPos[1]), null);

            int maxRockHeight = Integer.parseInt(scanner.nextLine());

            int noOfSites = Integer.parseInt(scanner.nextLine());
            Map<String, Coordinate> allSitesPaths = new LinkedHashMap<>();

            for(int i=0; i<noOfSites; i++) {
                String[] site = scanner.nextLine().split(" ");
                Coordinate siteCoord = new Coordinate(Integer.parseInt(site[0]), Integer.parseInt(site[1]), null);
                allSitesPaths.put(siteCoord.toString(), new Coordinate(siteCoord.x, siteCoord.y, null));
            }

            for(int i=0; i<rowHeight; i++) {
                String[] nextRow = scanner.nextLine().split(" ");
                for(int j=0; j<columnWidth; j++) {
                    matrix[i][j] = Integer.parseInt(nextRow[j]);
                }
            }

            switch(typeOfAlgorithm) {
                case BFS:
                    solveUsingBFS(columnWidth, rowHeight, startingCoord, maxRockHeight, noOfSites, allSitesPaths, matrix);
                    break;

                case UCS:
                    solveUsingUCS(columnWidth, rowHeight, startingCoord, maxRockHeight, noOfSites, allSitesPaths, matrix);
                    break;

                case A_STAR:
                    solveUsingAStar(columnWidth, rowHeight, startingCoord, maxRockHeight, noOfSites, allSitesPaths, matrix);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void solveUsingBFS(int columnWidth, int rowHeight, Coordinate startingCoord, int maxRockHeight,
                                      int noOfSites, Map<String, Coordinate> allSitesPaths, int[][] matrix) {
        Queue<Coordinate> queue = new LinkedList<>();
        boolean visited[][] = new boolean[rowHeight][columnWidth];
        visited[startingCoord.y][startingCoord.x] = true;
        queue.add(startingCoord);

        int noOfPendingSites = noOfSites;

        while(!queue.isEmpty() && noOfPendingSites  != 0) {
            Coordinate currentCoord = queue.peek();
            if(allSitesPaths.get(currentCoord.toString()) != null) {
                System.out.println("reached " + currentCoord.toString());
                noOfPendingSites--;
                allSitesPaths.put(currentCoord.toString(), currentCoord);
            }

            queue.poll();

            List<Coordinate> allNeighbors = getAllUnvisitedNeighboursBFS(matrix, currentCoord, columnWidth, rowHeight, maxRockHeight, visited);
            if (!allNeighbors.isEmpty()) {
                queue.addAll(allNeighbors);
            }
            System.out.println("QUEUE " + queue.toString());
        }

        printAllSitesPaths(allSitesPaths);

        System.out.println("BFS implementation done");
    }

    private static void printAllSitesPaths(Map<String, Coordinate> allSitesPaths) {

        try {
            FileWriter fw = new FileWriter("output.txt");

            for (Map.Entry<String, Coordinate> entry : allSitesPaths.entrySet()) {
                if (entry.getValue() == null || entry.getValue().parent == null) {
                    fw.write("FAIL");
                } else {
                    Coordinate currentCoord = entry.getValue();
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

    private static List<Coordinate> getAllUnvisitedNeighboursBFS(int[][] matrix, Coordinate currentCoord, int columnWidth, int rowHeight,
                                                                 int maxRockHeight, boolean[][] visited) {
        int currentHeight = matrix[currentCoord.y][currentCoord.x] < 0 ? Math.abs(matrix[currentCoord.y][currentCoord.x]) : 0;
        List<Coordinate> neighbours = new ArrayList<>();

        for(int y = Math.max(0, currentCoord.y-1); y <= Math.min(currentCoord.y+1, rowHeight-1); y++){
                for(int x = Math.max(0, currentCoord.x-1); x <= Math.min(currentCoord.x+1, columnWidth-1); x++){
                    Coordinate neighbour = new Coordinate(x,y, currentCoord);
                    if((neighbour.x != currentCoord.x || neighbour.y != currentCoord.y) && !visited[y][x]){
                        int height = matrix[y][x] < 0 ? Math.abs(matrix[y][x]) : 0;
                        if (Math.abs(currentHeight - height) <= maxRockHeight) {
                            neighbours.add(neighbour);
                            visited[neighbour.y][neighbour.x] = true;
                    }
                }
            }
        }
        return neighbours;
    }

    private static void solveUsingUCS(int columnWidth, int rowHeight, Coordinate startingCoord, int maxRockHeight,
                                      int noOfSites, Map<String, Coordinate> allSitesPaths, int matrix[][]) {
        System.out.println("UCS to be implemented");
    }

    private static void solveUsingAStar(int columnWidth, int rowHeight, Coordinate startingCoord, int maxRockHeight,
                                      int noOfSites, Map<String, Coordinate> allSitesPaths, int matrix[][]) {
        System.out.println("A* to be implemented");
    }
}
