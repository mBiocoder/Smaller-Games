package TicTacToeJFX;

public abstract class Player implements IPlayer { //Player is AI and implemented by AIPlayer

    private int iID;
    protected Board oPlayerBoard;
    protected DummyPlayer oOpponent; //oppponent is our dummyplayer now that this is  AI

    public Player(int iID)
    {
        this.iID = iID;
        oOpponent = new DummyPlayer(-1); // id is -1
        oPlayerBoard = new Board(3);

    }

    public int getID()
    {
        return this.iID;
    }

    /**
     *
     * @param oOpponentMove the move of any other player
     * @return this player's move or null if no move is possible
     * @throws Exception
     */
    abstract public Move nextMove(Move oOpponentMove) throws Exception;

    /**
     *
     * @param oOpponent the other player
     * @param oOpponentMove the other player's move
     * @return true if this player can make a valid move
     * @throws Exception
     */
    protected boolean handleOpponentMove(IPlayer oOpponent, Move oOpponentMove) throws Exception {
        oPlayerBoard.makeMove(oOpponent, oOpponentMove);

        return oPlayerBoard.validFreeMoveExists(); //other player, the opponent, made a valid move and this player can make a valid move
    }

}
