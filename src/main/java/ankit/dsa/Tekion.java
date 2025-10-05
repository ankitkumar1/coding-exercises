package ankit.dsa;

import java.util.Arrays;

public class Tekion {
    public static void main(String[] args) {
        System.out.println(getMinSupportingPillers(
                new int[][]{{2,8},{1,6},{7,12},{10,16}, {9, 17}, {15, 30}}));

        System.out.println(getMinSupportingPillers(
                new int[][]{{1,3},{3,6},{6,9},{9,12},{12,15}}));
    }

    public static int getMinSupportingPillers(int[][] coordinates){
        if(coordinates == null || coordinates.length == 0){
            return 0;
        }
        Arrays.sort(coordinates, (coordinate1, coordinate2) -> {
            if(coordinate1[1] != coordinate2[1]){
                return Integer.compare(coordinate1[1], coordinate2[1]);
            }
            return Integer.compare(coordinate1[0], coordinate2[0]);
        });

        //{3,4}, {1,6},{2,8},{7,12},{10,16}
        int[] previousCoordinate = coordinates[0];
        int minPillers = 1;
        int i=1;
        while(i<coordinates.length){
            int[] currentCoordinate = coordinates[i];
            if(previousCoordinate[1] <=  currentCoordinate[1] &&
                        previousCoordinate[1] >=  currentCoordinate[0]){
                i++;
                continue;
            }
            previousCoordinate = currentCoordinate;
            minPillers++;
            i++;
        }
        return minPillers;
    }


}

/*
* Q1: Array of 2 coordinates.. 2 D array
* each item is a surface coordinate
* put pillers on y axis to support the surface.
* if the piller touches the surface then
*
* [10,16],[2,8],[1,6],[7,12]
* [0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16]
*

         10--------16
 2-----8
1----6
      7-----12

[1,6],[2,8],[3-4],[7,12], [10,16]

[3-4],[1,6],[2,8],[7,12], [10,16]
 */