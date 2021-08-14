package TicTacToeJFX;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Board {

    private int iFields = 0;
    private int[][] aBoard = null;

    private HashMap<Integer, IPlayer> mSeenPlayers = new HashMap<>();

    public Board(int iFields)
    {
        this.iFields = iFields;
        this.aBoard = new int[iFields][iFields];

    }

    public IPlayer makeMove(IPlayer oPlayer, Move oMove) throws Exception {

        mSeenPlayers.put(oPlayer.getID(), oPlayer);

        if (oMove == null)
            return null;

        if (aBoard[oMove.x][oMove.y] != 0)
        {
            System.err.println("Move. " + oMove);
            System.err.println(this.toString());

            throw new Exception("Invalid Move!");
        }

        aBoard[oMove.x][oMove.y] = oPlayer.getID();

        return checkWinner();

    }

    /**
     *
     * @param seenIDs a set of player IDs
     * @return the player for the first player ID
     */
    private IPlayer getPlayerFromIDSet(Set<Integer> seenIDs)
    {
        for (Integer playerID : seenIDs)
            return this.mSeenPlayers.getOrDefault(playerID, null);

        return null;
    }

    /**
     *
     * @return the winner player, otherwise null
     */
    private IPlayer checkWinner() {

        IPlayer foundWinner = checkRows();

        if (foundWinner != null)
            return foundWinner;

        foundWinner = checkCols();

        if (foundWinner != null)
            return foundWinner;

        foundWinner = checkDiags();

        return foundWinner;

    }

    /**
     *
     * @return the winner on any diagonal, otherwise null
     */
    private IPlayer checkDiags() {

        // there's only 2 diags, thus quick and dirty ok
        HashSet<Integer> seenIDs = new HashSet<>();
        for (int i = 0; i < iFields; ++i)
        {
            seenIDs.add( aBoard[i][i] );
        }
        if (seenIDs.size() == 1)
        {
            return getPlayerFromIDSet(seenIDs);
        }

        seenIDs.clear();
        for (int i = 0; i < iFields; ++i)
        {
            seenIDs.add( aBoard[i][iFields-i-1] );
        }


        if (seenIDs.size() == 1)
        {
            return getPlayerFromIDSet(seenIDs);
        }

        return null;

    }

    /**
     *
     * @return the winner on the rows, otherwise null
     */
    private IPlayer checkRows()
    {
        // any player got a whole row?
        for (int i = 0; i < iFields; ++i)
        {

            HashSet<Integer> seenIDs = new HashSet<>();

            for (int j = 0; j < iFields; ++j)
            {
                seenIDs.add(this.aBoard[i][j]);
            }

            if (seenIDs.size() == 1)
            {
                return getPlayerFromIDSet(seenIDs);
            }

        }

        return null;
    }


    /**
     *
     * @return a winner, null if no winner is on the columns
     */
    private IPlayer checkCols()
    {
        // any player got a whole row?
        for (int i = 0; i < iFields; ++i)
        {

            HashSet<Integer> seenIDs = new HashSet<>();

            for (int j = 0; j < iFields; ++j)
            {
                seenIDs.add(this.aBoard[j][i]);
            }

            if (seenIDs.size() == 1)
            {
                return getPlayerFromIDSet(seenIDs);
            }

        }

        return null;
    }


    /**
     *
     * @return String representation of board
     */
    @Override
    public String toString() {

        StringBuilder oSB = new StringBuilder();

        oSB.append("x:\t" );
        for (int i = 0; i < iFields; ++i)
        {
            oSB.append(""+i+"\t");
        }
        oSB.append("\ny:\n" );

        for (int i = 0; i < iFields; ++i)
        {
            oSB.append("" + i + "\t");

            for (int j = 0; j < iFields; ++j)
            {
                oSB.append(this.aBoard[i][j] + "\t");
            }

            oSB.append('\n');
        }

        return oSB.toString();
    }

    /**
     *
     * @return field size
     */
    public int getFields() {
        return iFields;
    }

    /**
     *
     * @return true if a valid free move on the board exists
     */
    public boolean validFreeMoveExists()
    {
        for (int i = 0; i < iFields; ++i)
        {
            for (int j = 0; j < iFields; ++j)
            {
                if (this.aBoard[i][j] == 0)
                    return true;
            }
        }

        return false;
    }

    /**
     *
     * @param oMove the move to check
     * @return true if this is a valid free move
     */
    public boolean isValidFreeMove(Move oMove) {

        return this.aBoard[oMove.x][oMove.y] == 0;  //Setzen alle emty Stellen auf 0

    }

    public IPlayer getPlayer(int i, int j) {

        int iPlayerID = this.aBoard[i][j];
        if (iPlayerID == 0)
            return null;

        return this.mSeenPlayers.getOrDefault(iPlayerID, null);

    }
}

