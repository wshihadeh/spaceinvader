package com.moh.spaceinvader.game;
	
import com.moh.spaceinvader.engine.GameUniverse;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public class Main extends Application {

	GameUniverse gameUniverse = new SapceInvaderUniverse(59, "Spasce Invader");
	
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
    	gameUniverse.initialize(primaryStage);
    	gameUniverse.beginGameLoop();
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        Platform.exit();
        gameUniverse.shutdown();
    }
}
