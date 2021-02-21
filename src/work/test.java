package work;

import java.util.Random;

public class test {
    public static void main(String[] args) {
        Random r = new Random();
        //todo
        int columnWidth = r.nextInt(500);
        int rowHeight = r.nextInt(500);
        System.out.println(columnWidth + " " + rowHeight);

        int startingX = r.nextInt(columnWidth);
        int startingY = r.nextInt(rowHeight);
        System.out.println(startingX + " " + startingY);

        int maxRockHeight = r.nextInt(16);
        System.out.println(maxRockHeight);

        int noOfDest = 1 + r.nextInt(10);
        System.out.println(noOfDest);

        for(int i=0; i<noOfDest; i++) {
            int destX = r.nextInt(columnWidth);
            int destY = r.nextInt(rowHeight);
            System.out.println(destX + " " + destY);
        }

        for(int i=0; i<rowHeight; i++) {
            for(int j=0; j<columnWidth; j++) {
                System.out.print((r.nextInt(35) - 10) + " ");
            }
            System.out.println();
        }
    }
}
