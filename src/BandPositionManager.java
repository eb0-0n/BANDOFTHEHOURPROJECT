import java.util.Scanner;

/**
 * Manages the positions of musicians in a band, ensuring that weight restrictions are respected for each position.
 * Allows for adding and removing musicians from specific positions and printing the current arrangement.
 */
public class BandPositionManager {
    private static final int MAX_ROWS = 10;
    private static final int MAX_POSITIONS = 8;
    private static final double MAX_WEIGHT = 200.0;
    private static final double MIN_WEIGHT = 45.0;
    private static final double MAX_WEIGHT_PER_POSITION = 100.0;
    private static final Scanner scanner = new Scanner(System.in);

    private double[][] musicianWeights;
    private int[] maxPositionsInRow;

    /**
     * Constructs a new BandPositionManager with a specified number of rows.
     * Initializes arrays to hold the weights of musicians and the max positions in each row.
     *
     * @param numberOfRows The number of rows to be managed.
     */
    public BandPositionManager(int numberOfRows) {
        musicianWeights = new double[numberOfRows][];
        maxPositionsInRow = new int[numberOfRows];
        for (int i = 0; i < numberOfRows; i++) {
            System.out.printf("Please enter number of positions in row %c : ", 'A' + i);
            int positions = scanner.nextInt();
            while (positions < 1 || positions > MAX_POSITIONS) {
                System.out.print("ERROR: Out of range, try again            : ");
                positions = scanner.nextInt();
            }
            musicianWeights[i] = new double[positions];
            maxPositionsInRow[i] = positions;
        }
    }

    /**
     * Adds a musician to a specified position if it's vacant and if the musician's weight is within the allowed range.
     * Checks if adding the musician would exceed the maximum allowed weight per position.
     */
    private void addMusician() {
        // Row selection
        System.out.print("Please enter row letter                   : ");
        char rowLetter = scanner.next().toUpperCase().charAt(0);
        scanner.nextLine(); // Consume newline
        int row = rowLetter - 'A';
        while (row < 0 || row >= musicianWeights.length) {
            System.out.print("ERROR: Out of range, try again            : ");
            rowLetter = scanner.next().toUpperCase().charAt(0);
            scanner.nextLine(); // Consume newline
            row = rowLetter - 'A';
        }

        // Position selection
        System.out.print("Please enter position number (1 to " + maxPositionsInRow[row] + ")     : ");
        int position = scanner.nextInt() - 1;
        scanner.nextLine(); // Consume newline
        while (position < 0 || position >= maxPositionsInRow[row]) {
            System.out.print("ERROR: Out of range, try again            : ");
            position = scanner.nextInt() - 1;
            scanner.nextLine(); // Consume newline
        }

        // Check if the position is already occupied
        if (musicianWeights[row][position] != 0) {
            System.out.println("ERROR: There is already a musician there.");
            return;
        }

        // Weight input
        System.out.print("Please enter weight (45.0 to 200.0)       : ");
        double weight = scanner.nextDouble();
        scanner.nextLine(); // Consume newline
        while (weight < MIN_WEIGHT || weight > MAX_WEIGHT) {
            System.out.print("ERROR: Out of range, try again            : ");
            weight = scanner.nextDouble();
            scanner.nextLine(); // Consume newline
        }

        // Check if the added weight exceeds the limit
        double totalRowWeight = 0;
        for (double w : musicianWeights[row]) {
            totalRowWeight += w;
        }
        if (totalRowWeight + weight > MAX_WEIGHT_PER_POSITION * maxPositionsInRow[row]) {
            System.out.println("ERROR: That would exceed the average weight limit.");
            return;
        }

        musicianWeights[row][position] = weight;
        System.out.println("****** Musician added.");
    }

    /**
     * Removes a musician from a specified position.
     */
    private void removeMusician() {
        // Row selection
        System.out.print("Please enter row letter                   : ");
        char rowLetter = scanner.next().toUpperCase().charAt(0);
        scanner.nextLine(); // Consume newline
        int row = rowLetter - 'A';
        if (row < 0 || row >= musicianWeights.length) {
            System.out.println("ERROR: Out of range, try again            : ");
            return;
        }

        // Position selection and validation
        System.out.print("Please enter position number (1 to " + maxPositionsInRow[row] + ")     : ");
        int position = scanner.nextInt() - 1;
        scanner.nextLine(); // Consume newline
        if (position < 0 || position >= maxPositionsInRow[row] || musicianWeights[row][position] == 0) {
            System.out.println("ERROR: That position is vacant.");
            return;
        }

        // Removing the musician
        musicianWeights[row][position] = 0;
        System.out.println("****** Musician removed.");
    }

    /**
     * Prints the current assignments of musicians across all rows, showing their weight and the total and average weights per row.
     */
    private void printAssignments() {
        System.out.println();
        int maxPositionsLength = 0;
        for (double[] musicianWeight : musicianWeights) {
            maxPositionsLength = Math.max(maxPositionsLength, musicianWeight.length);
        }
        String spaceTemplate = "                              "; // 30 spaces, adjust based on your needs
        for (int i = 0; i < musicianWeights.length; i++) {
            System.out.printf("%c:", 'A' + i);
            double totalWeight = 0;
            for (int j = 0; j < musicianWeights[i].length; j++) {
                System.out.printf(" %5.1f", musicianWeights[i][j]);
                totalWeight += musicianWeights[i][j];
            }
            int spacesNeeded = maxPositionsLength - musicianWeights[i].length;
            String spaces = spacesNeeded > 0 ? spaceTemplate.substring(0, spacesNeeded * 6) : ""; // 6 spaces per missing position
            double averageWeight = totalWeight > 0 ? totalWeight / maxPositionsInRow[i] : 0;
            System.out.printf("%s[  %5.1f,  %5.1f]\n", spaces, totalWeight, averageWeight);
        }
    }

    /**
     * The main method to start the application.
     * Allows the user to add or remove musicians, print the current setup, or exit the application.
     */
    public static void main(String[] args) {
        System.out.println("Welcome to the Band of the Hour");
        System.out.println("-------------------------------");
        System.out.print("Please enter number of rows               : ");
        int numberOfRows = scanner.nextInt();
        while (numberOfRows < 1 || numberOfRows > MAX_ROWS) {
            System.out.print("ERROR: Out of range, try again            : ");
            numberOfRows = scanner.nextInt();
        }
        scanner.nextLine(); // Consume newline

        BandPositionManager band = new BandPositionManager(numberOfRows);

        char option;
        do {
            System.out.println();
            System.out.print("(A)dd, (R)emove, (P)rint,          e(X)it : ");
            option = scanner.next().toUpperCase().charAt(0);
            scanner.nextLine(); // Consume the rest of the line
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
                    return; // Use return to exit immediately
                default:
                    System.out.println("ERROR: Invalid option, try again. ");
                    break;
            }
        } while (true); // option != 'X changed to true for simplicity
    }
}
