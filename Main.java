import java.util.*;

public class Main {
    static int n, gX, gY, gZ, maxTime,coins;
    static int[][][] grid;
    static boolean[][][] collected;
    static boolean[][][] occupied;

    static List<Robot> robots;
    static int[][] directions = {{1, 0, 0},{-1, 0, 0},{0, 1, 0},{0, -1, 0},{0, 0, 1},{0, 0, -1},{0, 0, 0}};

    static class Robot {
        int x, y, z, coins;
        Robot(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.coins = 0;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        n = sc.nextInt();
        gX = sc.nextInt();
        gY = sc.nextInt();
        gZ = sc.nextInt();
        maxTime = Math.max(gX, Math.max(gY, gZ));

        grid = new int[gZ][gY][gX];
        collected = new boolean[gZ][gY][gX];
        occupied = new boolean[gZ][gY][gX];

        for (int z = 0; z < gZ; z++) {
            for (int y = 0; y < gY; y++) {
                for (int x = 0; x < gX; x++) {
                    grid[z][y][x] = sc.nextInt();
                    if(grid[z][y][x] == 1){
                        coins++;
                    }
                }
            }
        }


        if(n>5){
            forMoreThanFiveRobots();
        }else{
            forLessThanSixRobots();
        }
    }


    static void forMoreThanFiveRobots(){
        System.out.println( Math.min(coins, n * (maxTime + 1)));

    }

    static void forLessThanSixRobots(){
        initializeRobots();
        for (int t = 1; t <= maxTime; t++) {
            boolean[][][] nextOccupied = new boolean[gZ][gY][gX];

            for (Robot r : robots) {
                int bestX = r.x, bestY = r.y, bestZ = r.z;
                int minDistance = Integer.MAX_VALUE;

                for (int[] d : directions) {
                    int nx = r.x + d[0];
                    int ny = r.y + d[1];
                    int nz = r.z + d[2];

                    if (nx >= 0 && ny >= 0 && nz >= 0 && nx < gX && ny < gY && nz < gZ) {
                        if (!occupied[nz][ny][nx] && !nextOccupied[nz][ny][nx]) {
                            int dist = nearestCoinDistance(nx, ny, nz);
                            if (dist < minDistance) {
                                minDistance = dist;
                                bestX = nx;
                                bestY = ny;
                                bestZ = nz;
                            }
                        }
                    }
                }

                r.x = bestX;
                r.y = bestY;
                r.z = bestZ;

                if (grid[r.z][r.y][r.x] == 1 && !collected[r.z][r.y][r.x]) {
                    collected[r.z][r.y][r.x] = true;
                    r.coins++;
                }

                nextOccupied[r.z][r.y][r.x] = true;
            }
            occupied = nextOccupied;
        }
        totalCoins();

    }


    static void initializeRobots(){
        robots = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            int x = i % gX;
            int y = (i / gX) % gY;
            int z = (i / (gX * gY)) % gZ;
            robots.add(new Robot(x, y, z));
        }

        for (Robot r : robots) {
            if (grid[r.z][r.y][r.x] == 1 && !collected[r.z][r.y][r.x]) {
                collected[r.z][r.y][r.x] = true;
                r.coins++;
            }
            occupied[r.z][r.y][r.x] = true;
        }

    }



    static int nearestCoinDistance(int x, int y, int z) {
        int minDist = Integer.MAX_VALUE;
        for (int k = 0; k < gZ; k++) {
            for (int j = 0; j < gY; j++) {
                for (int i = 0; i < gX; i++) {
                    if (grid[k][j][i] == 1 && !collected[k][j][i]) {
                        int dist = Math.abs(x - i) + Math.abs(y - j) + Math.abs(z - k);
                        minDist = Math.min(minDist, dist);
                    }
                }
            }
        }
        return minDist;
    }

    static void totalCoins(){
        int total = 0;
        for (Robot r : robots) {
            total += r.coins;
        }
        System.out.println(total);
    }
}
