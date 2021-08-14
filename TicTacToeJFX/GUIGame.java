package TicTacToeJFX;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

class BoardVisualizer extends Pane
{

    private Label[][] myLabels = null;
    private Pane[][] myPanes = null;
    private ArrayList<String> mySymbols = null;

    public BoardVisualizer( Board oBoard )
    {
        super();

        mySymbols = new ArrayList<String>();
        mySymbols.add("O");
        mySymbols.add("X");

        this.setBackground(new Background(new BackgroundFill(Color.LAVENDER, CornerRadii.EMPTY, null)));
        GridPane oLabelsPane = this.prepareLabels(oBoard);
        this.getChildren().add( oLabelsPane );


        Pane self = this;

        ChangeListener<Number> sizeListener = new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

                oLabelsPane.setPrefWidth(self.getWidth());
                oLabelsPane.setPrefHeight(self.getHeight());

            }
        };

        this.heightProperty().addListener(sizeListener);
        this.widthProperty().addListener(sizeListener);


    }

    private String getSymbolForPlayerID(int iID) //getSymbols for a player
    {

        int cycID = iID % mySymbols.size();

        if (cycID < 0)
            cycID += mySymbols.size();

        return mySymbols.get(cycID);

    }

    public void update(Board oBoard) //
    {
        for (int i = 0; i < oBoard.getFields(); ++i)
        {
            for (int j = 0; j < oBoard.getFields(); ++j)
            {
                IPlayer oPlayerOnField = oBoard.getPlayer(i,j); //currentPlyer on board

                this.myLabels[i][j].setText( (oPlayerOnField != null) ? getSymbolForPlayerID(oPlayerOnField.getID()) : "" ); //print the currentplayer on screen
            }
        }
    }

    private GridPane prepareLabels(Board oBoard)
    {

        myLabels = new Label[oBoard.getFields()][oBoard.getFields()]; //Buttons
        myPanes = new GridPane[oBoard.getFields()][oBoard.getFields()]; //create Board on GridPane

        GridPane mainElem = new GridPane();
        mainElem.setGridLinesVisible(true); //lines seperating Buttons/Fields
        mainElem.setHgap(8);
        mainElem.setVgap(8);



        ColumnConstraints cconsAlways = new ColumnConstraints();
        cconsAlways.setHgrow(Priority.ALWAYS);

        RowConstraints rconsAlways = new RowConstraints();
        rconsAlways.setVgrow(Priority.ALWAYS);


        ColumnConstraints cconsFraction = new ColumnConstraints();
        cconsFraction.setPercentWidth(100.0/oBoard.getFields());

        RowConstraints rconsFraction = new RowConstraints();
        rconsFraction.setPercentHeight(100.0/oBoard.getFields());

        for (int i = 0; i < oBoard.getFields(); ++i)
        {
            mainElem.getRowConstraints().add(rconsFraction);

            for (int j = 0; j < oBoard.getFields(); ++j)
            {

                if (i == 0)
                {
                    mainElem.getColumnConstraints().add(cconsFraction);
                }

                int idx = j + oBoard.getFields() * i;

                Label oLabel = new Label("L" + idx);
                oLabel.setAlignment(Pos.CENTER);
                oLabel.setFont( new Font("Monospaced", 0));

                myLabels[i][j] = oLabel;

                GridPane oLabelGrid = new GridPane();
                oLabelGrid.getRowConstraints().add(rconsAlways);
                oLabelGrid.getColumnConstraints().add(cconsAlways);

                oLabelGrid.setBackground(new Background(new BackgroundFill(Color.AZURE, CornerRadii.EMPTY, null)));

                oLabelGrid.add(oLabel, 0,0);
                GridPane.setHgrow(oLabel, Priority.ALWAYS);
                GridPane.setVgrow(oLabel, Priority.ALWAYS);
                GridPane.setHalignment(oLabel, HPos.CENTER);
                myPanes[i][j] = oLabelGrid;


                ChangeListener<Number> oListener = (observable, oldValue, newValue) -> {
                    oLabel.setFont(new Font("Monospaced", oLabelGrid.getHeight()*0.5));
                };


                oLabelGrid.heightProperty().addListener( oListener );

                mainElem.add(oLabelGrid, j, i);

            }

        }

        return mainElem;
    }

    public void connectPlayer(Board oBoard, GUIPlayer oPlayer1) {

        for (int i = 0; i < oBoard.getFields(); ++i)
        {
            for (int j = 0; j < oBoard.getFields(); ++j)
            {
                final int iX = i;
                final int iY = j;

                this.myPanes[i][j].setOnMouseClicked( (MouseEvent evt) -> {
                    oPlayer1.setMoveCoords( iX, iY ); //set the moves made by Human as coordinates
                });
            }
        }

    }

}


public class GUIGame extends Application implements ITicTacToeGame {

    public static void main(String[] args) {
        launch(args);
    }

    private Board oBoard;
    private IPlayer oPlayer1, oPlayer2, lastPlayer;
    private Move lastPlayerMove = null;
    private BoardVisualizer oBoardVis = null;
    private boolean bGameStarted = false;

    private Button oStartButton = null; //Button showing "Start" on screen

    public GUIGame()
    {
        super();
        this.resetGame();

    }

    public void resetGame()
    {

        bGameStarted = false;

        // must remove any threads from previous game
        this.endWorkers();


        if (oStartButton != null)
        {
            oStartButton.setText("Start");
        }


        oBoard = new Board(3);  //change here fpr n sized board

        if (oBoardVis == null)
        {
            this.oBoardVis = new BoardVisualizer(this.oBoard);
        }

        GUIPlayer oGUIPlayer = new GUIPlayer(1);
        oPlayer1 = oGUIPlayer; //human player

        oBoardVis.connectPlayer(oBoard, oGUIPlayer);

        oPlayer2 = new AIPlayer(2); //player2 is the AI

        lastPlayer = oPlayer2; //AI is last player
        lastPlayerMove = null; // AI has not begun

        oBoardVis.update(oBoard);
    }

    @Override
    public void init()
    {
        System.out.println("Init called");
        System.out.println(javafx.scene.text.Font.getFamilies());
    }

    @Override
    public void start(Stage primaryStage) { //Start method GUI

        System.out.println("Start called");

        // BorderPane layout is quite what we want
        BorderPane mainLayout = new BorderPane();

        //add board visualizer to layout
        mainLayout.setCenter(oBoardVis);


        // create buttons
        GridPane oBottomButtons = new GridPane();
        oStartButton = new Button("Start"); //Positioning start and reset button in the following lines
        Button oResetButton = new Button("Reset");

        // add buttons to layouts
        oBottomButtons.add(oStartButton, 0,0);
        oBottomButtons.add(oResetButton, 1, 0);

        GridPane.setHalignment(oStartButton, HPos.LEFT);
        GridPane.setHalignment(oResetButton, HPos.RIGHT);

        oStartButton.prefWidthProperty().bind(oBottomButtons.widthProperty().multiply(0.49));
        oResetButton.prefWidthProperty().bind(oBottomButtons.widthProperty().multiply(0.49));

        ColumnConstraints oColAlways = new ColumnConstraints();
        oColAlways.setHgrow(Priority.ALWAYS);

        oBottomButtons.getColumnConstraints().add(oColAlways);

        mainLayout.setBottom(oBottomButtons);
        mainLayout.setAlignment(oBottomButtons, Pos.CENTER);
        oBottomButtons.setBackground(new Background(new BackgroundFill(Color.LAVENDER, CornerRadii.EMPTY, null)));


        // define what should happen when we click the start button
        oStartButton.setOnAction((ActionEvent evt) -> {
            this.bGameStarted = !this.bGameStarted;

            if (this.bGameStarted) {
                // ensures we have a defined board/players
                this.resetGame();


                // Update this button:
                oStartButton.setText("End"); //Change the start Button to an End button

                // this starts the game
                new Thread( makeTask() ).start();

            } else {
                oStartButton.setText("Start");
            }
        });


        // when pressing reset we expect the game to be reset
        oResetButton.setOnAction((ActionEvent evt) -> {
            resetGame();
        });

        /*
        ChangeListener<Number> resizeListener = new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                primaryStage.setTitle( "GridGUI: " + primaryStage.getWidth() + " " + primaryStage.getHeight() );

            }
        };

        primaryStage.widthProperty().addListener(resizeListener);
        primaryStage.heightProperty().addListener(resizeListener);
        */

        primaryStage.setTitle("TicTacToe");

        primaryStage.setScene(new Scene(mainLayout, 525, 525));
        primaryStage.show();
    }

    private Set<Task<IPlayer>> startedWorkers = new HashSet<>(); //Task

    private Task<IPlayer> makeTask()
    {
        GUIGame self = this;

        Task<IPlayer> oWorker =  new Task<IPlayer>() {
            @Override
            protected IPlayer call() throws Exception {

                if (self.lastPlayer == self.oPlayer1) //Switch currentplayers
                {
                    self.lastPlayer = self.oPlayer2;
                } else {
                    self.lastPlayer = self.oPlayer1;
                }

                if (lastPlayer instanceof GUIPlayer) //check where the lastPlayer is an instance of
                {
                    GUIPlayer oGUIPlayer = (GUIPlayer) lastPlayer;
                    self.lastPlayerMove = self.lastPlayer.nextMove(lastPlayerMove);


                } else {
                    self.lastPlayerMove = self.lastPlayer.nextMove(lastPlayerMove);
                }

                System.out.println("Player "+ lastPlayer.getID() +" made move " + self.lastPlayerMove);
                IPlayer oWinner = self.oBoard.makeMove(lastPlayer, lastPlayerMove); //check for a winner

                return oWinner; //return the winner

            }
        };

        oWorker.setOnSucceeded((WorkerStateEvent evt) -> { //If we have a winner we do the following:

            try {
                IPlayer oWinner = oWorker.get(); // get the winner
                oBoardVis.update(this.oBoard); //update the board
                oBoardVis.requestLayout();//print its layout

                boolean canContinue = oBoard.validFreeMoveExists(); //Check if we can still continue

                if ((oWinner != null) || (!canContinue)) //We have a winner case
                {

                    if (oWinner != null) //reset game in both cases
                    {
                        showMessageBox("Winner determined!", "We got a winner", "Winner is Player with ID: " + oWinner.getID());
                        resetGame();
                    } else {
                        showMessageBox("No Winner determined!", "We got no winner", "You achieved a draw");
                        resetGame();
                    }



                } else {

                    Task<IPlayer> oNextMove = makeTask();
                    startedWorkers.add(oNextMove); //add the move to started Workers

                    new Thread( oNextMove ).start();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

        });


        return oWorker;
    }

    public static void showMessageBox(String sTitle, String sHeader, String sText) //Message box
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, sText, ButtonType.OK);
        alert.setTitle(sTitle);
        alert.setHeaderText(sHeader);

        alert.setResizable(true);
        alert.getDialogPane().setPrefSize(480, 320);

        alert.showAndWait();
    }

    private void endWorkers()
    {

        for (Task<IPlayer> oTask : startedWorkers)
        {
            oTask.cancel(true);
        }

        startedWorkers.clear();
    }

    @Override
    public void stop()
    {
        System.out.println("Stop called");

        // otherwise a worker may still run in a thread blocking exit
        this.endWorkers();
    }
}

