package TicTacToeJFX;

public class CMDGame implements ITicTacToeGame {

    private Board oGameBoard = null;
    private IPlayer oPlayer1, oPlayer2;  //2 different players mit 1 = Cross und 2 = O evtul.
    private IPlayer lastPlayer; //aktuellerSpieler
    private Move lastPlayerMove; //Letzter Zug des aktuellen Spielers


    public CMDGame()
    {
        this.init();
    }

    private void showBoard()
    {
        System.out.println(oGameBoard);
        System.out.println("\n");
    }

    public void play() throws Exception {

        this.showBoard(); //Show board constallation before

        while (true)
        {
            if (lastPlayer == oPlayer1)
            {
                lastPlayer = oPlayer2; //Switching players
            } else {
                lastPlayer = oPlayer1; //Switching players
            }

            lastPlayerMove = lastPlayer.nextMove(lastPlayerMove); //Get what is last move
            IPlayer oWinner = oGameBoard.makeMove(lastPlayer, lastPlayerMove); // get Winner after making next move, null if there is no winner yet

            this.showBoard(); //Show board constallation after

            if (oWinner != null)
            {
                System.out.println("We got a winner!" + oWinner.getID()); //Print if we have a winner
                break;
            }
        }
    }

    public static void main(String[] args) throws Exception {

        CMDGame oTTTCMDGame = new CMDGame(); //Create object
        oTTTCMDGame.play(); //Play the game
    }


    @Override
    public void resetGame() { //From interface ITicTacToeGame method
        oGameBoard = new Board(3);

        oPlayer1 = new CmdPlayer(1);  //player1 is Human
        //oPlayer2 = new CmdPlayer(2); //player2 is also human
        //oPlayer2 = new DummyPlayer(2); //player2 is a dummyplayer
        oPlayer2 = new AIPlayer(2); // player 2 is the AI

        lastPlayerMove = null;
        lastPlayer = oPlayer2; //AI starts the game
    }

    @Override
    public void init() {

        this.resetGame(); //Initializing the game means basically resetting it

    }
}

