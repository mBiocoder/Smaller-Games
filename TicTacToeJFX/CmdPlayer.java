package TicTacToeJFX;

import java.util.Scanner;

public class CmdPlayer extends Player {

    private Scanner clScanner = new Scanner(System.in);

    public CmdPlayer(int iID) {
        super(iID);
    }

    @Override
    public Move nextMove(Move oOpponentMove) throws Exception {

        if (!this.handleOpponentMove(oOpponent, oOpponentMove))
            return null;

        while(true)  //What we do after opponent did a valid move
        {

            try
            {
                System.out.println("Player: " + this.getID()); //Player1 is human and thus can input his values
                System.out.println("Enter next x coord: ");
                int iCol = clScanner.nextInt();
                System.out.println("Enter next y coord: ");
                int iRow = clScanner.nextInt();

                Move oMove = new Move(iRow, iCol); //set the values from human User to as our move; oMove is the move that we currently made

                if (oPlayerBoard.isValidFreeMove(oMove)) // check if the move that was instructed is also valid, thus was empty before
                {
                    this.oPlayerBoard.makeMove(this, oMove); //if it was empty before, then really set the move
                    return oMove; // return the move that we have checked as valid

                }

                System.out.println("Invalid Move: " + oMove + "\n\n"); //In case move was invalid, type this on console

            } catch (Exception e) {
                System.err.println("Possibly incorrect number format for Integer!");
                clScanner.next();
            }

        }

    }
}

