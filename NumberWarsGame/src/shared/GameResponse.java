package shared;

import java.io.Serializable;

public class GameResponse implements Serializable {
    public String status;
    public int aiRow, aiCol, aiNumber;
    public int[] score;

    public GameResponse(String status, int aiRow, int aiCol, int aiNumber, int[] score) {
        this.status = status;
        this.aiRow = aiRow;
        this.aiCol = aiCol;
        this.aiNumber = aiNumber;
        this.score = score;
    }
}