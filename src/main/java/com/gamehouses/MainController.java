package com.gamehouses;

import com.gamehouses.Gamer.Bombs;
import com.gamehouses.Gamer.GameBody;
import com.gamehouses.Gamer.Gamer;
import com.gamehouses.Gamer.Home;
import com.gamehouses.Gamer.Stars;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable
{
    @FXML
    public Label txtItsYourTurn;

    @FXML
    public Button btnStartGame;

    @FXML
    public TextField txtGamerName1;

    @FXML
    public TextField txtGamerName2;

    @FXML
    public Button btnStopGame;

    @FXML
    public GridPane gameBodyPane;

    @FXML
    public Text txtGamer1Score;

    @FXML
    public Text txtGamer2Score;

    @FXML
    public TextField txtXYGameBody;

    @FXML
    private GameBody gameBody;

    private Pane[][] columns;

    private boolean started;

    private String gamerName1, gamerName2;

    @FXML
    public void onClickBtnStartGame()
    {
        final String strXYGameBody = txtXYGameBody.getText();

        if (!strXYGameBody.isEmpty() && strXYGameBody.matches("[0-9]*"))
        {
            boolean ok = false;
            try
            {
                int xyGameBody = Integer.parseInt(strXYGameBody);
                if (xyGameBody >= GameBody.MIN_XY && xyGameBody <= GameBody.MAX_XY)
                {
                    GameBody.xyGameBody = xyGameBody;
                    ok = true;
                }
            }
            catch (Exception ignored)
            {
            }
            if (!ok)
            {
                Platform.runLater(() -> txtXYGameBody.setText(String.valueOf(GameBody.MIN_XY)));
                GameBody.xyGameBody = GameBody.MIN_XY;
            }
        }

        if (!started)
        {
            if (getGamerName())
            {
                Platform.runLater(() ->
                {
                    createPane();
                    gameBody = GameBody.createGameBody(gamerName1 , gamerName2);
                    fillBodyGame();
                    started = true;
                    setLblItsYourTrue();
                });
            }
            else showAlert("Start game" , "Error gamer name" , "Gamer start error" , Alert.AlertType.ERROR);
        }
        else showAlert("Start Game" , "Game has started!" , "Gamer start error" , Alert.AlertType.ERROR);
    }

    private void showAlert(final String title , final String body , final String content , final Alert.AlertType type)
    {
        Platform.runLater(() ->
        {
            final Alert alert = new Alert(type);

            alert.getButtonTypes().clear();
            alert.getButtonTypes().add(ButtonType.YES);

            alert.setTitle(title);
            alert.setContentText(content);
            alert.setHeaderText(body);
            alert.show();
        });
    }

    private void setLblItsYourTrue()
    {
        Platform.runLater(() ->
        {
            try
            {
                txtItsYourTurn.setText(gameBody.getItsYourTrue());
                txtItsYourTurn.setTextFill(Color.web(gameBody.getColorItsYourTrue()));
            }
            catch (Exception ignored)
            {
            }
        });
    }

    private boolean getGamerName()
    {
        return (getGamerName1() && getGamerName2());
    }

    @FXML
    public void onClickBtnStopGame()
    {
        if (started)
        {
            gameBody = null;
            Platform.runLater(() -> gameBodyPane.getChildren().clear());
            gamerName1 = "";
            gamerName2 = "";
            columns = null;
            started = false;
        }
        else showAlert("Stop game" , "Game not started!" , "Stop game error" , Alert.AlertType.ERROR);
    }

    @FXML
    public boolean getGamerName1()
    {
        gamerName1 = txtGamerName1.getText();
        if (gamerName1 != null && !gamerName1.isEmpty())
        {
            if (gamerName1.equals(gamerName2))
            {
                gamerName1 = "";
                showAlertGetGamerName();
            }
            else return true;
        }

        return false;
    }

    private void showAlertGetGamerName()
    {
        showAlert("Error message" , "Gamer 1 == Gamer 2" , "Gamer name error" , Alert.AlertType.ERROR);
    }

    public boolean getGamerName2()
    {
        gamerName2 = txtGamerName2.getText();
        if (gamerName2 != null && !gamerName2.isEmpty())
        {
            if (gamerName2.equals(gamerName1))
            {
                gamerName2 = "";
                showAlertGetGamerName();
            }
            else return true;
        }

        return false;
    }

    @Override
    public void initialize(URL url , ResourceBundle resourceBundle)
    {
        Platform.runLater(() -> txtXYGameBody.setText(String.valueOf(GameBody.xyGameBody)));
    }

    private void createPane()
    {
        gameBodyPane.setHgap(5);
        gameBodyPane.setVgap(5);
        columns = new Pane[GameBody.xyGameBody][GameBody.xyGameBody];
    }

    private void onClickColumn(final XY xy)
    {
        if (gameBody != null)
        {
            final Gamer gamerItsYourTrue = gameBody.getGamerItsYourTrue();

            if (gamerItsYourTrue != null)
            {
                final XY xyGamer = gamerItsYourTrue.getXy();
                if (!xyGamer.equals(xy))
                {
                    if ((xyGamer.x == xy.x && xyGamer.y != xy.y) || (xyGamer.y == xy.y && xyGamer.x != xy.x))
                    {
                        moveTo(getXYMoving(xyGamer , xy));
                        return;
                    }
                }
                showAlertErrorSelected();
            }
        }
    }

    private void showAlertErrorSelected()
    {
        showAlert("Wrong choice" , "The selected house is wrong" , "You can only move vertically and horizontally" , Alert.AlertType.ERROR);
    }

    private XY[] getXYMoving(final XY destination , final XY toWhere)
    {
        XY[] xy;

        if (destination.x != toWhere.x)
        {
            final Integer[] position = getPosition(destination.x , toWhere.x);
            xy = new XY[position.length];
            for (int i = 0; i < position.length; i++) xy[i] = new XY(position[i] , destination.y);
        }
        else
        {
            final Integer[] position = getPosition(destination.y , toWhere.y);
            xy = new XY[position.length];
            for (int i = 0; i < position.length; i++) xy[i] = new XY(destination.x , position[i]);
        }

        return xy;
    }

    private Integer[] getPosition(final int destination , final int toWhere)
    {
        final List<Integer> position = new ArrayList<>();
        if (toWhere < destination) for (int i = destination - 1; i >= toWhere; i--) position.add(i);
        else for (int i = destination + 1; i <= toWhere; i++) position.add(i);

        return position.toArray(new Integer[]{});
    }

    private void fillBodyGame()
    {
        final Home[][] homes = gameBody.getHomes();
        final int paneSize = 42 - GameBody.xyGameBody;
        for (int i = 0; i < homes.length; i++)
        {
            for (int j = 0; j < homes[i].length; j++)
            {
                final Pane pane = new Pane();
                pane.setMaxSize(paneSize , paneSize);
                pane.setMinSize(paneSize , paneSize);
                pane.setPrefSize(paneSize , paneSize);
                pane.setCursor(Cursor.HAND);
                final int finalI = i;
                final int finalJ = j;
                pane.setOnMouseClicked(mouseEvent -> onClickColumn(new XY(finalI , finalJ)));
                pane.setStyle("-fx-background-color: #e1e1e1");

                columns[i][j] = pane;

                fillColumn(homes[i][j] , columns[i][j]);
                gameBodyPane.add(columns[i][j] , i , j);
            }
        }

        final int len = GameBody.xyGameBody - 1;

        final Pane paneGamer1 = columns[0][len];
        gameBody.getGamer1().setXy(new XY(0 , len));
        setGamerPane(paneGamer1 , 1);

        final Pane paneGamer2 = columns[len][len];
        gameBody.getGamer2().setXy(new XY(len , len));
        setGamerPane(paneGamer2 , 2);

        gameBody.getHomes()[0][len].setHomeType(Home.HomeType.gamer);
        gameBody.getHomes()[len][len].setHomeType(Home.HomeType.gamer);
    }

    private void setGamerPane(final Pane pane , final int gamerName12)
    {
        if (gamerName12 == 1 || gamerName12 == 2)
            Platform.runLater(() -> pane.getChildren().add(getBackgroundPane("gamer" + gamerName12 + ".png" , pane.getPrefWidth())));
    }

    private void fillColumn(final Home home , final Pane pane)
    {
        final Home.HomeType homeType = home.getHomeType();

        switch (homeType)
        {
            case bomb:
                pane.getChildren().add(getBackgroundPane("bomb.png" , pane.getPrefWidth()));
                pane.getChildren().add(getLabelScore(((Bombs) home.getValue()).score));
                break;
            case star:
                pane.getChildren().add(getBackgroundPane("star.png" , pane.getPrefWidth()));
                pane.getChildren().add(getLabelScore(((Stars) home.getValue()).score));
                break;
            case obstacle:
                pane.getChildren().add(getBackgroundPane("obstacle.png" , pane.getPrefWidth()));
                break;
        }
    }

    private ImageView getBackgroundPane(final String imageName , final double size)
    {
        try
        {
            final String path = String.format("%s%simages%s%s" , System.getProperty("user.dir") , File.separator , File.separator , imageName);
            final ImageView imageView = new ImageView(new Image(new File(path).toURI().toURL().toString()));
            imageView.setFitWidth(size);
            imageView.setFitHeight(size);
            return imageView;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    private Label getLabelScore(final int score)
    {
        final Label label = new Label(String.valueOf(score));
        label.setMaxSize(25 , 25);
        return label;
    }

    private void moveTo(final XY[] xy)
    {
        final Gamer gamer = gameBody.getGamerItsYourTrue();
        if (gamer != null)
        {
            final XY hereXY = gamer.getXy();

            final Home[][] homes = gameBody.getHomes();

            int gamerScore = gamer.getScore();

            boolean getBomb = false;

            for (final XY position : xy)
            {
                final Home home = homes[position.x][position.y];
                switch (home.getHomeType())
                {
                    case obstacle:
                        showAlert("Obstacle" , "There are obstacles in your way" , "An obstacle was found" , Alert.AlertType.ERROR);
                        return;
                    case gamer:
                        showAlert("Gamer" , "There are gamer in your way" , "A gamer was found" , Alert.AlertType.ERROR);
                        return;
                    case star:
                        final Stars star = (Stars) home.getValue();
                        gamerScore += star.score;
                        break;
                    case bomb:
                        final Bombs bomb = (Bombs) home.getValue();
                        getBomb = (bomb.score > 0);
                        if (bomb.score >= gamerScore) gamerScore = 0;
                        else gamerScore = gamerScore - bomb.score;
                        break;
                }
            }

            if (getBomb && gamerScore <= 0)
            {
                showAlert("The game is over" , "Loser" , "You are the loser of the game" , Alert.AlertType.INFORMATION);
                onClickBtnStopGame();
            }
            else
            {
                for (final XY position : xy)
                {
                    homes[position.x][position.y].setHomeType(Home.HomeType.empty);
                    columns[position.x][position.y].getChildren().clear();
                }

                homes[hereXY.x][hereXY.y].setHomeType(Home.HomeType.empty);
                columns[hereXY.x][hereXY.y].getChildren().clear();

                final XY moveHere = xy[xy.length - 1];

                homes[moveHere.x][moveHere.y].setHomeType(Home.HomeType.gamer);
                gamer.setXy(moveHere);
                gamer.setScore(gamerScore);

                setGamerPane(columns[moveHere.x][moveHere.y] , gameBody.getNumberItsYourTrue());

                gameBody.setGamer(gamer);
                gameBody.setHomes(homes);

                gameBody.setItsYourTrue();
                setLblItsYourTrue();

                setTxtScore();

                hasStar();
            }
        }
    }

    private void hasStar()
    {
        if (gameBody != null)
        {
            final Home[][] homes = gameBody.getHomes();
            for (final Home[] home : homes)
            {
                for (final Home column : home)
                    if (column.getHomeType().equals(Home.HomeType.star)) return;
            }

            final Gamer gamer1 = gameBody.getGamer1();
            final Gamer gamer2 = gameBody.getGamer2();

            final String content = String.format("Players score\nGamer1: %d\nGamer2: %d" , gamer1.getScore() , gamer2.getScore());

            if (gamer1.getScore() > gamer2.getScore())
                showAlert("Winner Gamer 1" , "The gamer 1 is a winner of the game" , content , Alert.AlertType.CONFIRMATION);
            else if (gamer1.getScore() < gamer2.getScore())
                showAlert("Winner Gamer 2" , "The gamer 2 is a winner of the game" , content , Alert.AlertType.CONFIRMATION);
            else
                showAlert("The game equalised" , "Both gamers win the game" , content , Alert.AlertType.CONFIRMATION);

            onClickBtnStopGame();
        }
    }

    private void setTxtScore()
    {
        Platform.runLater(() ->
        {
            try
            {
                txtGamer1Score.setText(String.valueOf(gameBody.getGamer1().getScore()));
                txtGamer2Score.setText(String.valueOf(gameBody.getGamer2().getScore()));
            }
            catch (Exception ignored)
            {
            }
        });
    }
}