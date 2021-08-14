package TicTacToeJFX;

public interface IPlayer {


    /**
     *
     * @return the id of this player
     */
    public int getID();

    /**
     *
     * @param oOpponentMove the move of any other player
     * @return the move of this player
     * @throws Exception
     */
    public Move nextMove(Move oOpponentMove) throws Exception;
}

