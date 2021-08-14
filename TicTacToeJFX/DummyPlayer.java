package TicTacToeJFX;

public class DummyPlayer implements IPlayer { //implements IPlayer and is the human

    private int iID;

    public DummyPlayer(int iID) {
        this.iID = iID;
    }

    @Override
    public int getID() {
        return iID;
    }

    @Override
    public Move nextMove(Move oOpponentMove) {
        return null;
    }
}

