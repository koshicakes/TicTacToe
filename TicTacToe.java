import java.util.Arrays;
import java.util.Scanner;

public class TicTacToe {

    private static final char HUMAN = 'X';
    private static final char AI = 'O';
    private static final char[] board = new char[9];

    private static final int[][] LINES = {
        {0, 1, 2}, {3, 4, 5}, {6, 7, 8},   // rows
        {0, 3, 6}, {1, 4, 7}, {2, 5, 8},   // columns
        {0, 4, 8}, {2, 4, 6}               // diagonals
    };

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Arrays.fill(board, ' ');

        System.out.println("=== Tic-Tac-Toe vs Unbeatable AI ===");
        System.out.println("You are X, the AI is O. Enter a number 1-9 to move.");

        while (true) {
            printBoard();
            humanMove(scanner);
            if (isOver()) break;
            aiMove();
            if (isOver()) break;
        }

        printBoard();
        char winner = winner();
        if (winner == HUMAN) {
            System.out.println("You win!");
        } else if (winner == AI) {
            System.out.println("The AI wins!");
        } else {
            System.out.println("It's a draw!");
        }
        scanner.close();
    }

    private static void humanMove(Scanner scanner) {
        while (true) {
            System.out.print("Your move (1-9): ");
            if (!scanner.hasNextInt()) {
                scanner.next();
                System.out.println("Please enter a number.");
                continue;
            }
            int pos = scanner.nextInt() - 1;
            if (pos >= 0 && pos < 9 && board[pos] == ' ') {
                board[pos] = HUMAN;
                return;
            }
            System.out.println("Invalid or taken square.");
        }
    }

    private static void aiMove() {
        int bestScore = Integer.MIN_VALUE;
        int bestMove = -1;

        for (int i = 0; i < 9; i++) {
            if (board[i] == ' ') {
                board[i] = AI;
                int score = minimax(0, false);
                board[i] = ' ';
                if (score > bestScore) {
                    bestScore = score;
                    bestMove = i;
                }
            }
        }

        board[bestMove] = AI;
        System.out.println("AI plays " + (bestMove + 1));
    }

    /** Tries every possible game from here. Positive score = good for the AI. */
    private static int minimax(int depth, boolean aiTurn) {
        char winner = winner();
        if (winner == AI) return 10 - depth;      // faster wins score higher
        if (winner == HUMAN) return depth - 10;   // slower losses score higher
        if (isFull()) return 0;

        int best = aiTurn ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        for (int i = 0; i < 9; i++) {
            if (board[i] == ' ') {
                board[i] = aiTurn ? AI : HUMAN;
                int score = minimax(depth + 1, !aiTurn);
                board[i] = ' ';
                best = aiTurn ? Math.max(best, score) : Math.min(best, score);
            }
        }
        return best;
    }

    private static char winner() {
        for (int[] line : LINES) {
            char a = board[line[0]];
            if (a != ' ' && a == board[line[1]] && a == board[line[2]]) {
                return a;
            }
        }
        return ' ';
    }

    private static boolean isFull() {
        for (char c : board) {
            if (c == ' ') return false;
        }
        return true;
    }

    private static boolean isOver() {
        return winner() != ' ' || isFull();
    }

    private static void printBoard() {
        System.out.println();
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                int i = row * 3 + col;
                System.out.print(" " + (board[i] == ' ' ? (char) ('1' + i) : board[i]) + " ");
                if (col < 2) System.out.print("|");
            }
            System.out.println();
            if (row < 2) System.out.println("---+---+---");
        }
        System.out.println();
    }
}