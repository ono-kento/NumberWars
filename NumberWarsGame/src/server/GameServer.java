package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class GameServer {
    public static void main(String[] args) {
        final int PORT = 12345;

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("サーバー起動: ポート " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("クライアント接続: " + clientSocket.getInetAddress());

                Thread session = new Thread(new GameSession(clientSocket));
                session.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
