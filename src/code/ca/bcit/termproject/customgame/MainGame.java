package ca.bcit.termproject.customgame;

import ca.bcit.termproject.customgame.orbs.BlueOrb;
import ca.bcit.termproject.customgame.orbs.GreenOrb;
import ca.bcit.termproject.customgame.orbs.Orb;
import ca.bcit.termproject.customgame.orbs.RedOrb;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.Iterator;

public class MainGame extends Application
{
    public static final int WINDOW_WIDTH = 800;
    public static final int WINDOW_HEIGHT = 600;
    public static final int PLAYER_SIZE = 30;
    public static final int ORB_SIZE = 20;
    public static final int CANNON_X = WINDOW_WIDTH / 2;
    public static final int CANNON_Y = 50;
    private static final int BLUE_ORB_POINTS = 1;
    private static final int GREEN_ORB_POINTS = 3;

    private Pane root;
    private Player player;
    private Cannon cannon;
    private Label scoreLabel;
    private int score = 0;

    @Override
    public void start(Stage primaryStage)
    {
        root = new Pane();
        Scene scene;
        scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        primaryStage.setTitle("Bullet Hell Game");
        primaryStage.setScene(scene);
        primaryStage.show();

        setupGame();
        startGameLoop();
        setupKeyHandlers(scene);
    }

    private void setupGame()
    {
        player = new Player(WINDOW_WIDTH / 2, WINDOW_HEIGHT - 50, PLAYER_SIZE);
        cannon = new Cannon(CANNON_X, CANNON_Y);

        // Initialize the score label
        scoreLabel = new Label("Score: 0");
        scoreLabel.setFont(new Font("Arial", 20));
        scoreLabel.setLayoutX(10); // Position in the top-left corner
        scoreLabel.setLayoutY(10);

        root.getChildren().addAll(player, scoreLabel);
    }

    private void startGameLoop()
    {
        AnimationTimer gameLoop = new AnimationTimer()
        {
            @Override
            public void handle(long now)
            {
                cannon.shootOrb(root);
                player.update();
                updateOrbs();
                checkCollisions();
            }
        };
        gameLoop.start();
    }

    private void updateOrbs()
    {
        for (Orb orb : cannon.getOrbs())
        {
            orb.update();
        }
    }

    private void setupKeyHandlers(Scene scene)
    {
        scene.setOnKeyPressed(event ->
        {
            switch (event.getCode())
            {
                case LEFT:
                    player.setLeft(true);
                    break;
                case RIGHT:
                    player.setRight(true);
                    break;
                case UP:
                    player.setUp(true);
                    break;
                case DOWN:
                    player.setDown(true);
                    break;
            }
        });

        scene.setOnKeyReleased(event ->
        {
            switch (event.getCode())
            {
                case LEFT:
                    player.setLeft(false);
                    break;
                case RIGHT:
                    player.setRight(false);
                    break;
                case UP:
                    player.setUp(false);
                    break;
                case DOWN:
                    player.setDown(false);
                    break;
            }
        });
    }

    private void checkCollisions()
    {
        Iterator<Orb> iterator = cannon.getOrbs().iterator();
        while (iterator.hasNext())
        {
            Orb orb = iterator.next();
            if (player.getBoundsInParent().intersects(orb.getBoundsInParent()))
            {
                if (orb instanceof RedOrb)
                {
                    gameOver("Game over! You hit a red orb!");
                }
                else if (orb instanceof GreenOrb)
                {
                    score += GREEN_ORB_POINTS;
                    iterator.remove();
                    root.getChildren().remove(orb);
                }
                else if (orb instanceof BlueOrb)
                {
                    score += BLUE_ORB_POINTS;
                    iterator.remove();
                    root.getChildren().remove(orb);
                }

                scoreLabel.setText("Score: " + score);
            }
        }
    }

    private void gameOver(final String message)
    {
        System.out.println(message);
        System.exit(0);
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}