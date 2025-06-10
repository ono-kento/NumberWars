package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import shared.GameResponse;
import shared.MoveRequest;

public class NumberWarsClient extends JFrame {
    private final JButton[][] buttons = new JButton[5][5];
    private final Set<Integer> usedNumbers = new HashSet<>();
    private final JLabel scoreLabel = new JLabel("Player 1: 0 | AI: 0");

    private ObjectOutputStream out;
    private ObjectInputStream in;

    public NumberWarsClient(String host, int port) {
        setTitle("Number Wars - クライアント");
        setSize(600, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        scoreLabel.setFont(new Font("Arial", Font.BOLD, 20));
        scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(scoreLabel, BorderLayout.NORTH);

        JPanel boardPanel = new JPanel(new GridLayout(5, 5));
        Font font = new Font("Arial", Font.BOLD, 24);

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                final int row = i;
                final int col = j;
                JButton btn = new JButton();
                btn.setFont(font);
                btn.addActionListener(e -> handleClick(row, col));
                buttons[i][j] = btn;
                boardPanel.add(btn);
            }
        }

        add(boardPanel, BorderLayout.CENTER);
        setVisible(true);

        connectToServer(host, port);
    }

    private void connectToServer(String host, int port) {
        try {
            Socket socket = new Socket(host, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "サーバー接続に失敗しました。");
            System.exit(1);
        }
    }

    private void handleClick(int row, int col) {
        if (!buttons[row][col].isEnabled()) return;

        String input = JOptionPane.showInputDialog(this, "1～9の数字を入力してください：");
        if (input == null) return;

        try {
            int num = Integer.parseInt(input);
            if (num < 1 || num > 9) throw new Exception();

            if (usedNumbers.contains(num)) {
                if (usedNumbers.size() == 9) usedNumbers.clear();
                else throw new Exception("すでに使用した数字です");
            }
            usedNumbers.add(num);

            MoveRequest req = new MoveRequest();
            req.row = row;
            req.col = col;
            req.number = num;

            out.writeObject(req);
            GameResponse res = (GameResponse) in.readObject();

            if (res.status.equals("invalid")) {
                JOptionPane.showMessageDialog(this, "無効な手です。");
                return;
            }

            // 自分の手
            buttons[row][col].setText(String.valueOf(num));
            buttons[row][col].setBackground(Color.CYAN);
            buttons[row][col].setEnabled(false);

            // AIの手
            buttons[res.aiRow][res.aiCol].setText(String.valueOf(res.aiNumber));
            buttons[res.aiRow][res.aiCol].setBackground(Color.PINK);
            buttons[res.aiRow][res.aiCol].setEnabled(false);

            updateScore(res.score);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "入力が無効です。1～9の未使用の数字を入力してください。");
        }
    }

    private void updateScore(int[] score) {
        scoreLabel.setText("Player 1: " + score[0] + " | AI: " + score[1]);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new NumberWarsClient("localhost", 12345));
    }
}
