package shared;

public class GameBoard {
    private final int[][] board = new int[5][5];
    private int moves = 0;

    public boolean setNumber(int row, int col, int number, boolean isPlayerOne) {
        if (board[row][col] != 0) return false;
        board[row][col] = isPlayerOne ? number : -number;
        moves++;
        return true;
    }

    public int getNumber(int row, int col) {
        return board[row][col];
    }

    public boolean isFull() {
        return moves >= 25;
    }

    public int[] calculateScore() {
        int p1 = 0, p2 = 0;
        int[][] dir = {{0, 1}, {1, 0}};

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                int curr = board[i][j];
                if (curr == 0) continue;

                for (int[] d : dir) {
                    int r = i + d[0], c = j + d[1];
                    if (r >= 0 && r < 5 && c >= 0 && c < 5) {
                        int neighbor = board[r][c];
                        if (neighbor == 0 || Integer.signum(curr) == Integer.signum(neighbor)) continue;

                        if (Math.abs(curr) > Math.abs(neighbor)) {
                            if (curr > 0) p1++;
                            else p2++;
                        }
                    }
                }
            }
        }
        return new int[]{p1, p2};
    }
}
