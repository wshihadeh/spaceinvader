package com.moh.spaceinvader.engine;


import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TimelineBuilder;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;


public abstract class GameUniverse {

    private Scene gameSurface;
    private Group sceneNodes;
    private static Timeline gameLoop;
    private final int framesPerSecond;
    private final String windowTitle;
    private final EntityController entityController = new EntityController();
    private final SoundPlayer soundPlayer = new SoundPlayer(3);


    public GameUniverse(final int fps, final String title) {
        framesPerSecond = fps;
        windowTitle = title;
        buildAndSetGameLoop();
    }

    protected final void buildAndSetGameLoop() {

        final Duration oneFrameAmt = Duration.millis(1000 / (float) getFramesPerSecond());
        final KeyFrame oneFrame = new KeyFrame(oneFrameAmt,
                new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(javafx.event.ActionEvent event) {
                        updateEntities();
                        checkCollisions();
                        cleanupEntities();

                    }
                });

        setGameLoop(TimelineBuilder.create()
                .cycleCount(Animation.INDEFINITE)
                .keyFrames(oneFrame)
                .build());
    }

    public abstract void initialize(final Stage primaryStage);
    public void beginGameLoop() {
        getGameLoop().play();
    }

    protected void updateEntities() {
        for (Entity Entity : entityController.getAllEntities()) {
            handleUpdate(Entity);
        }
    }


    protected void handleUpdate(Entity Entity) {
    }

    protected void checkCollisions() {
        entityController.resetCollisionsToCheck();
        for (Entity EntityA : entityController.getCollisionsToCheck()) {
            for (Entity EntityB : entityController.getAllEntities()) {
                if (handleCollision(EntityA, EntityB)) {
                    break;
                }
            }
        }
    }

    protected boolean handleCollision(Entity EntityA, Entity EntityB) {
        return false;
    }


    protected void cleanupEntities() {
        entityController.cleanupEntities();
    }

    protected int getFramesPerSecond() {
        return framesPerSecond;
    }
   
    public String getWindowTitle() {
        return windowTitle;
    }

    protected static Timeline getGameLoop() {
        return gameLoop;
    }


    protected static void setGameLoop(Timeline gameLoop) {
    	GameUniverse.gameLoop = gameLoop;
    }

    public EntityController getEntityController() {
        return entityController;
    }

    public Scene getGameSurface() {
        return gameSurface;
    }

    protected void setGameSurface(Scene gameSurface) {
        this.gameSurface = gameSurface;
    }

    public Group getSceneNodes() {
        return sceneNodes;
    }

    protected void setSceneNodes(Group sceneNodes) {
        this.sceneNodes = sceneNodes;
    }

    protected SoundPlayer getSoundPlayer() {
        return soundPlayer;
    }

    public void shutdown() {
        getGameLoop().stop();
        getSoundPlayer().shutdown();
    }
}