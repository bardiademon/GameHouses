package com.gamehouses;

import com.gamehouses.Gamer.GameBody;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public final class Application extends javafx.application.Application
{

    @Override
    public void start(final Stage stage) throws IOException
    {
        final TextInputDialog inputDialog = new TextInputDialog();
        inputDialog.setTitle("Rows and columns");
        inputDialog.setHeaderText("Please enter rows and columns");
        final Optional<String> result = inputDialog.showAndWait();

        if (result.isPresent())
        {
            final String nStr = result.get();

            if (nStr.matches("[0-9]*"))
            {
                GameBody.xyGameBody = Integer.parseInt(nStr);
                if (GameBody.xyGameBody >= 8 && GameBody.xyGameBody <= 20)
                {
                    final FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("Main.fxml"));
                    Scene scene = new Scene(fxmlLoader.load());
                    stage.setTitle("Game!");
                    stage.setScene(scene);
                    stage.show();
                    return;
                }
            }
        }
        System.gc();
        Platform.exit();
        System.exit(0);
    }

    public static void main(String[] args)
    {
        launch();
    }

}