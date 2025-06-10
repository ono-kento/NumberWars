package shared;

import java.io.Serializable;

public class MoveRequest implements Serializable {
    public int row;
    public int col;
    public int number;
}