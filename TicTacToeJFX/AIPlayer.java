package TicTacToeJFX;

import java.util.Random;

public class AIPlayer extends Player { //the AI should make random moves and thus play as the opponent to the human

    /**
     * Needed for random moves
     */
    private Random randGen = new Random();

    public AIPlayer(int iID)
    {
        super(iID);
    }

    /**
     *
     * @return random board coordinated within board bounds
     */
    public int getRandomBoardCoord()
    {
        int coord = randGen.nextInt() % oPlayerBoard.getFields(); // modulo so that it is within the board bounds

        if (coord < 0)
            coord += oPlayerBoard.getFields();

        return coord; //coord is the randomly generated position
    }

    public Move nextMove(Move oOpponentMove) throws Exception {

        if (!this.handleOpponentMove(oOpponent, oOpponentMove))
            return null;

        while (true) // we can only do this because we know there is at least one free valid move!
        {
            int iCoord1 = getRandomBoardCoord(); //because we need x and y value for the board, thus call randomly generated coord 2 times
            int iCoord2 = getRandomBoardCoord();

            Move oMove = new Move(iCoord1, iCoord2);//set this as the move

            if (oPlayerBoard.isValidFreeMove(oMove)) { //check if this is valid
                try {
                    Thread.sleep(1000); //make us wait befor we see the Ai choice on screen
                } catch (InterruptedException e) {
                }

                this.oPlayerBoard.makeMove(this, oMove); //set this as our final valid move from AI

                return oMove; //return this as oMove
            }

        }
    }

}

