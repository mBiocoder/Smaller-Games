package TicTacToeJFX;

public class Move {

    final public int x, y;

    public Move(int iX, int iY)
    {
        x = iX;
        y = iY;
    }

    public String toString()
    {
        return "<Move x=" + x + " y=" + y + "/>";
    }
}
