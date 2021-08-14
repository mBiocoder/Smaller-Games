package TicTacToeJFX;

public class GUIPlayer extends Player {
    public GUIPlayer(int iID) {
        super(iID);
    }

    private Move extMove = null;
    private boolean bAcceptExt = false;

    @Override
    public Move nextMove(Move oOpponentMove) throws Exception {

        extMove = null;
        bAcceptExt = true;

        System.out.println("Expecting input: " + bAcceptExt);

        while(true)
        {
            // busy waiting - sorry!
            if (extMove == null)
            {
                Thread.sleep(100);
                continue;
            }

            Move currentMove = extMove;

            if (this.oPlayerBoard.isValidFreeMove(currentMove))
            {
                bAcceptExt = false;
                this.oPlayerBoard.makeMove(this, currentMove);
                return currentMove;
            } else {
                extMove = null;
            }

            System.out.println("Expecting input: " + extMove);


        }



    }

    public void setMoveCoords(int x, int y)
    {
        if (bAcceptExt)
        {
            extMove = new Move(x,y);
        } else {
            System.out.println("Can't accept");
        }
    }
}
