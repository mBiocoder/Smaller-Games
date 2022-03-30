public class GameofLife {
    int width = 10;
    int height = 10;
    int[][] board;

    public GameofLife(int width, int height) {
        this.width = width;
        this.height = height;
        this.board = new int[width][height];
    }

    public void timestep(){
        int [][] board2 = new int[width][height];
        for (int y = 0; y <height; y++) {
            for (int x = 0; x < width; x++){
                int aliveNeighbors = countNeighbors(x, y);
                if (board[x][y] == 0){ //checking if dead
                    if (aliveNeighbors == 3){
                        board2[x][y] = 1;
                    }else {
                        board2[x][y] = 0;
                    }
                }else { // checking is alive
                    if (aliveNeighbors < 2){
                        board2[x][y] = 0;
                    }else if (aliveNeighbors > 3){
                        board2[x][y] = 0;
                    } else {
                        board2[x][y] = 1;
                    }
                }
            }
        }
        for (int y = 0; y <height; y++) {
            for (int x = 0; x < width; x++) {
                board[x][y] = board2[x][y];
            }
        }
    }

    public String printBoard(){
        String printedBoard = "";
        for (int x = 0; x < width; x++){
            printedBoard += "__";
        }
        printedBoard += ("_\n") ;
        for (int y = 0; y < height; y++) {
            printedBoard +=("|");
            for (int x = 0; x < width; x++){
                printedBoard +=((board[x][y] == 1 ? "x" : "_")+ "|");
            }
            printedBoard += "\n";
        }
        printedBoard += "\n";

        return printedBoard;
    }

    public int countNeighbors(int x, int y){
        int count = 0;
        int xleft = (x + width -1) % width;
        int xright = (x + 1) % width;
        int ytop = (y + height - 1) % height;
        int ybottom = (y + 1) % height;

        count += board[xleft][ytop];
        count += board[x][ytop];
        count += board[xright][ytop];
        count += board[xleft][y];
        count += board[xright][y];
        count += board[xleft][ybottom];
        count += board[x][ybottom];
        count += board[xright][ybottom];

        return count;
    }



    public void initialize(){
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                board[x][y] = Math.random() < 0.3 ? 1 : 0;
            }
        }
    }


    public static void main(String[] args) {
        GameofLife game = new GameofLife(10, 10);

        game.initialize();
        System.out.println("Initial board:");
        System.out.println(game.printBoard());

        for (int t = 1; t <= 200000; t++){
            System.out.print("\033[H\033[2J");
            System.out.flush();

            game.timestep();
            System.out.println("Current generation: " + t);
            System.out.println(game.printBoard());

            try {
                Thread.sleep(20);
            }catch (Exception e){
                e.getMessage();
            }
        }
    }
}
