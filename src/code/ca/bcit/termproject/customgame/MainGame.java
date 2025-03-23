package ca.bcit.termproject.customgame;

import ca.bcit.termproject.customgame.orbs.BlueOrb;
import ca.bcit.termproject.customgame.orbs.GreenOrb;
import ca.bcit.termproject.customgame.orbs.Orb;
import ca.bcit.termproject.customgame.orbs.RedOrb;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.Iterator;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
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

    private Pane root;
    private Player player;
    private Cannon cannon;

    @Override
    public void start(Stage primaryStage)
    {
        root = new Pane();
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
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

        root.getChildren().add(player);
    }

    private void startGameLoop()
    {
        AnimationTimer gameLoop = new AnimationTimer()
        {
            @Override
            public void handle(long now)
            {
                cannon.shootOrb(root); // Pass the root pane to add orbs
                player.update();
                updateOrbs(); // Update all orbs
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
            if (event.getCode() == KeyCode.LEFT)
            {
                player.setLeft(true);
            }
            else if (event.getCode() == KeyCode.RIGHT)
            {
                player.setRight(true);
            }
        });

        scene.setOnKeyReleased(event ->
        {
            if (event.getCode() == KeyCode.LEFT)
            {
                player.setLeft(false);
            }
            else if (event.getCode() == KeyCode.RIGHT)
            {
                player.setRight(false);
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
                    System.out.println("Game Over! You hit a red orb.");
                    System.exit(0);
                }
                else if (orb instanceof GreenOrb || orb instanceof BlueOrb)
                {
                    iterator.remove(); // Safely remove the orb from the list
                    root.getChildren().remove(orb); // Remove the orb from the scene graph
                }
            }
        }
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}