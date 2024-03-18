import java.util.Scanner;

public class BandPositionManager {
    private static final int MAX_ROWS = 10;
    private static final int MAX_POSITIONS = 8;
    private static final double MAX_WEIGHT = 200.0;
    private static final double MIN_WEIGHT = 45.0;
    private static final double MAX_WEIGHT_PER_POSITION = 100.0;
    private static final Scanner scanner = new Scanner(System.in);

    private double[][] musicianWeights;
    private int[] maxPositionsInRow;

    public BandPositionManager(int numberOfRows) {
        musicianWeights = new double[numberOfRows][];
        maxPositionsInRow = new int[numberOfRows];
        for (int i = 0; i < numberOfRows; i++) {
            System.out.printf("Please enter number of positions in row %c : ", 'A' + i);
            int positions = scanner.nextInt();
            while (positions < 1 || positions > MAX_POSITIONS) {
                System.out.println("ERROR: Out of range, try again            : ");
                positions = scanner.nextInt();
            }
            musicianWeights[i] = new double[positions];
            maxPositionsInRow[i] = positions;
        }
    }

    private void addMusician() {
        System.out.print("Please enter row letter                   : ");
        char rowLetter = scanner.next().toUpperCase().charAt(0);
        int row = rowLetter - 'A';
        if (row < 0 || row >= musicianWeights.length) {
            System.out.println("ERROR: Out of range, try again            : ");
            return;
        }

        System.out.print("Please enter position number (1 to " + maxPositionsInRow[row] + ")     : ");
        int position = scanner.nextInt() - 1;
        if (position < 0 || position >= maxPositionsInRow[row]) {
            System.out.println("ERROR: Out of range, try again            : ");
            return;
        }

        System.out.print("Please enter weight (45.0 to 200.0)       : ");
        double weight = scanner.nextDouble();
        while (weight < MIN_WEIGHT || weight > MAX_WEIGHT) {
            System.out.println("ERROR: Out of range, try again            : ");
            weight = scanner.nextDouble();
        }

        // Check if adding this musician exceeds total row weight limit
        double totalWeight = 0;
        for (double w : musicianWeights[row]) {
            totalWeight += w;
        }
        if (totalWeight + weight > maxPositionsInRow[row] * MAX_WEIGHT_PER_POSITION) {
            System.out.println("ERROR: Adding this musician would exceed the row's weight limit.");
            return;
        }

        if (musicianWeights[row][position] != 0) {
            System.out.println("ERROR: There is already a musician in that position.");
            return;
        }

        musicianWeights[row][position] = weight;
        System.out.println("****** Musician added.");
    }

    private void removeMusician() {
        System.out.print("Please enter row letter                   : ");
        char rowLetter = scanner.next().toUpperCase().charAt(0);
        int row = rowLetter - 'A';
        if (row < 0 || row >= musicianWeights.length) {
            System.out.println("ERROR: Out of range, try again            : ");
            return;
        }

        System.out.print("Please enter position number (1 to " + maxPositionsInRow[row] + ")     : ");
        int position = scanner.nextInt() - 1;
        if (position < 0 || position >= maxPositionsInRow[row] || musicianWeights[row][position] == 0) {
            System.out.println("ERROR: That position is vacant.");
            return;
        }

        musicianWeights[row][position] = 0;
        System.out.println("****** Musician removed.");
    }

    private void printAssignments() {
        for (int i = 0; i < musicianWeights.length; i++) {
            System.out.print((char) ('A' + i) + ": ");
            double totalWeight = 0;
            for (int j = 0; j < musicianWeights[i].length; j++) {
                System.out.printf("%6.1f ", musicianWeights[i][j]);
                totalWeight += musicianWeights[i][j];
            }
            double averageWeight = totalWeight > 0 ? totalWeight / musicianWeights[i].length : 0;
            System.out.printf("                               [ %6.1f, %6.1f]\n", totalWeight, averageWeight);
        }
    }

    public static void main(String[] args) {
        System.out.println("Welcome to the Band of the Hour");
        System.out.println("-------------------------------");
        System.out.print("Please enter number of rows               : ");
        int numberOfRows = scanner.nextInt();
        while (numberOfRows < 1 || numberOfRows > MAX_ROWS) {
            System.out.println("ERROR: Out of range, try again            : ");
            numberOfRows = scanner.nextInt();
        }

        BandPositionManager band = new BandPositionManager(numberOfRows);
        char option;
        do {
            System.out.print("(A)dd, (R)emove, (P)rint,          e(X)it : ");
            option = scanner.next().toUpperCase().charAt(0);
            switch (option) {
                case 'A':
                    band.addMusician();
                    break;
                case 'R':
                    band.removeMusician();
                    break;
                case 'P':
                    band.printAssignments();
                    break;
                case 'X':
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("ERROR: Invalid option, try again          : ");
            }
        } while (option != 'X');
    }
}
