package ca.bcit.termproject.numbergame;

import javafx.application.Application;
import javafx.stage.Stage;

public class NumberGame extends Application
{

    @Override
    public void start(final Stage stage) throws Exception
    {
        stage.setTitle("NumberGame");
        stage.show();
    }
    
    public static void main(final String[] args)
    {
        launch(args);
    }
}
