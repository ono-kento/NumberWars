package shared;

import java.util.HashSet;
import java.util.Set;

public class AIBrain {
    private final AIStrategy strategy;
    private final Set<Integer> usedNumbers = new HashSet<>();

    public AIBrain(AIStrategy strategy) {
        this.strategy = strategy;
    }

    public int[] chooseMove(GameBoard board) {
        return strategy == AIStrategy.BFS ? chooseMoveBFS(board) : chooseMoveDFS(board);
    }

    private int[] chooseMoveBFS(GameBoard board) {
        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 5; j++)
                if (board.getNumber(i, j) == 0)
                    return new int[]{i, j};
        return null;
    }

    private int[] chooseMoveDFS(GameBoard board) {
        for (int i = 4; i >= 0; i--)
            for (int j = 4; j >= 0; j--)
                if (board.getNumber(i, j) == 0)
                    return new int[]{i, j};
        return null;
    }

    public int chooseNumber() {
        for (int num = 9; num >= 1; num--) {
            if (!usedNumbers.contains(num)) {
                usedNumbers.add(num);
                return num;
            }
        }
        usedNumbers.clear();
        return chooseNumber();
    }
}