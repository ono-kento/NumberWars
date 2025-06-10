package server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import shared.AIBrain;
import shared.AIStrategy;
import shared.GameBoard;
import shared.GameResponse;
import shared.MoveRequest;

public class GameSession implements Runnable {
    private final Socket socket;

    public GameSession(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream())
        ) {
            GameBoard board = new GameBoard();
            AIBrain ai = new AIBrain(AIStrategy.BFS); // 固定戦略

            while (true) {
                MoveRequest request = (MoveRequest) in.readObject();

                boolean valid = board.setNumber(request.row, request.col, request.number, true);
                if (!valid) {
                    out.writeObject(new GameResponse("invalid", -1, -1, -1, new int[]{0,0}));
                    continue;
                }

                if (board.isFull()) {
                    // ゲーム終了: AIの手はなし
                    int[] score = board.calculateScore();
                    out.writeObject(new GameResponse("gameover", -1, -1, -1, score));
                    break; // セッション終了もしくは待機
                }

                int[] aiMove = ai.chooseMove(board);
                int aiNumber = ai.chooseNumber();
                board.setNumber(aiMove[0], aiMove[1], aiNumber, false);

                int[] score = board.calculateScore();
                out.writeObject(new GameResponse("ok", aiMove[0], aiMove[1], aiNumber, score));
            }


        } catch (Exception e) {
            System.err.println("セッションエラー: " + e.getMessage());
        }
    }
}
