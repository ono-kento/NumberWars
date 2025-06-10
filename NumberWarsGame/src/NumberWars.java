import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class NumberWars extends JFrame {
    private final JButton[][] cells = new JButton[5][5];
    private final int[][] numbers = new int[5][5];
    private boolean playerOneTurn = true;
    private int moves = 0;

    public NumberWars() {
        setTitle("Number Wars");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(5, 5));
        setSize(600, 600);

        Font font = new Font("Arial", Font.BOLD, 24);

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                JButton btn = new JButton();
                btn.setFont(font);
                int row = i, col = j;
                btn.addActionListener(e -> handleClick(row, col));
                cells[i][j] = btn;
                add(btn);
            }
        }

        setVisible(true);
    }

    private void handleClick(int row, int col) {
        if (!cells[row][col].getText().isEmpty()) return;

        String input = JOptionPane.showInputDialog(this, "Enter a number (1-9):");
        if (input == null) return;

        int number;
        try {
            number = Integer.parseInt(input);
            if (number < 1 || number > 9) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid number.");
            return;
        }

        cells[row][col].setText(String.valueOf(number));
        numbers[row][col] = playerOneTurn ? number : -number; // 正負でどちらのプレイヤーかを記録
        cells[row][col].setBackground(playerOneTurn ? Color.CYAN : Color.PINK);
        moves++;

        drawInequalities(row, col);

        if (moves == 25) {
            showResult();
        }

        playerOneTurn = !playerOneTurn;
    }

    private void drawInequalities(int row, int col) {
        int current = Math.abs(numbers[row][col]);
        String currentText = cells[row][col].getText();

        int[][] directions = { {0, 1}, {1, 0}, {-1, 0}, {0, -1} };

        for (int[] dir : directions) {
            int r = row + dir[0];
            int c = col + dir[1];

            if (r >= 0 && r < 5 && c >= 0 && c < 5 && numbers[r][c] != 0) {
                int neighbor = Math.abs(numbers[r][c]);
                String sign = current > neighbor ? ">" : current < neighbor ? "<" : "=";

                String updated = currentText + sign;
                cells[row][col].setText(updated);
            }
        }
    }

    private void showResult() {
        int p1Score = 0, p2Score = 0;

        int[][] directions = { {0, 1}, {1, 0} }; // 右と下だけ比較

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                int n1 = numbers[i][j];
                for (int[] dir : directions) {
                    int r = i + dir[0];
                    int c = j + dir[1];
                    if (r >= 0 && r < 5 && c >= 0 && c < 5) {
                        int n2 = numbers[r][c];
                        if (n1 > n2) {
                            if (n1 > 0) p1Score++;
                            else p2Score++;
                        } else if (n1 < n2) {
                            if (n2 > 0) p1Score++;
                            else p2Score++;
                        }
                    }
                }
            }
        }

        String result = "Player 1: " + p1Score + " pts\nPlayer 2: " + p2Score + " pts\n";
        if (p1Score > p2Score) result += "🎉 Player 1 wins!";
        else if (p2Score > p1Score) result += "🎉 Player 2 wins!";
        else result += "🤝 It's a draw!";
        JOptionPane.showMessageDialog(this, result);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(NumberWars::new);
    }
}