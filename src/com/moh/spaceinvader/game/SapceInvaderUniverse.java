package com.moh.spaceinvader.game;

import javafx.scene.CacheHint;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import com.moh.spaceinvader.engine.Entity;
import com.moh.spaceinvader.engine.GameUniverse;

public class SapceInvaderUniverse extends GameUniverse {

    Label mousePtLabel = new Label();
    Label mousePressPtLabel = new Label();

    TextField xCoordinate = new TextField("234");
    TextField yCoordinate = new TextField("200");
    Button moveShipButton = new Button("Rotate ship");
    int atomsPassTheSip=0;
    int MAX_ATOMS_PASS_SHIP=10;

    Ship myShip;

    public SapceInvaderUniverse(int fps, String title) {
        super(fps, title);
    }

    public void initialize(final Stage primaryStage) {
        // Sets the window title
        primaryStage.setTitle(getWindowTitle());
        //primaryStage.setFullScreen(true);

        // Create the scene
        setSceneNodes(new Group());
        setGameSurface(new Scene(getSceneNodes(), 800, 600));
        getGameSurface().setFill(Color.BLACK);
        primaryStage.setScene(getGameSurface());
        
        myShip = new Ship();
        
        getEntityController().addEntities(myShip);
        getSceneNodes().getChildren().add(0, myShip.node);

        // Setup Game input
        setupInput(primaryStage);

        // Create many spheres
        generateManySpheres(5, 1);

        // load sound file
        getSoundPlayer().loadSoundEffects("laser", getClass().getClassLoader().getResource("laser_2.mp3"));

      
    }

    public void createEntities() {
    	Random rnd = new Random();
      if (getEntityController().getAtomCount() <= 3)
    	  generateManySpheres(rnd.nextInt(5), (rnd.nextInt(1)+rnd.nextDouble()+1));
    }
    private void setupInput(Stage primaryStage) {
    	getGameSurface().setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case A:
                	myShip.moveLeft();
                    break;
                case D:
                	myShip.moveRight(getGameSurface().getWidth());
                    break;
                case L:
                	Missile m1 = myShip.fire();
                	getEntityController().addEntities(m1);
                    // play sound
                    getSoundPlayer().playSound("laser");

                    getSceneNodes().getChildren().add(0, m1.node);
                    break;
            }
        });

    }
    private void generateManySpheres(int numSpheres, double speed) {
        Random rnd = new Random();
        Scene gameSurface = getGameSurface();
        for (int i = 0; i < numSpheres; i++) {
            Color c = Color.rgb(rnd.nextInt(255), rnd.nextInt(255), rnd.nextInt(255));
            Atom b = new Atom(rnd.nextInt(15) + 5, c, true);
            Circle circle = b.getAsCircle();
            // random 0 to 2 + (.0 to 1) * random (1 or -1)
            b.vX = 0;
            b.vY = speed;

            // random x between 0 to width of scene
            double newX = rnd.nextInt((int) gameSurface.getWidth());

            // check for the right of the width newX is greater than width 
            // minus radius times 2(width of sprite)
            if (newX > (gameSurface.getWidth() - (circle.getRadius() * 2))) {
                newX = gameSurface.getWidth() - (circle.getRadius() * 2);
            }

            // check for the bottom of screen the height newY is greater than height 
            // minus radius times 2(height of sprite)
            double newY = -10;
            if (newY > (gameSurface.getHeight() - (circle.getRadius() * 2))) {
                newY = gameSurface.getHeight() - (circle.getRadius() * 2);
            }
       

            circle.setTranslateX(newX);
            circle.setTranslateY(newY);
            circle.setVisible(true);
            circle.setId(b.toString());
            circle.setCache(true);
            circle.setCacheHint(CacheHint.SPEED);
            circle.setManaged(false);

            getEntityController().addEntities(b); 
            getSceneNodes().getChildren().add(b.node);

        }
    }

    @Override
    protected void handleUpdate(Entity entity) {
        // advance object
    	entity.update();
        if (entity instanceof Missile) {
            removeMissiles((Missile) entity);
        }if (entity instanceof Atom) {
            removeAtom((Atom) entity);
        } else if (entity instanceof Ship){
            bounceOffWalls(entity);
        }
        
        checkforGameEnd();
    }

    private void checkforGameEnd() {
    	if (atomsPassTheSip >= MAX_ATOMS_PASS_SHIP)
    	{
    		gameOver();
    	}
    }
    
    private void gameOver() {
    	
    	Text gameOver = new Text("GAME OVER !");
		gameOver.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 50));
		gameOver.setFill(Color.RED);
		gameOver.setStroke(Color.BLACK);
		gameOver.setStrokeWidth(3);
		gameOver.setX(90);
		gameOver.setY(300);
		
		getSceneNodes().getChildren().add(gameOver);
		getSoundPlayer().playSound("laser");
		this.getGameLoop().stop();
    }
    private void bounceOffWalls(Entity entity) {
        // bounce off the walls when outside of boundaries

        Node displayNode = entity.node;
        // Get the group node's X and Y but use the ImageView to obtain the width.
        if (entity.node.getTranslateX() > (getGameSurface().getWidth() - displayNode.getBoundsInParent().getWidth()) ||
                displayNode.getTranslateX() < 0) {

            // bounce the opposite direction
        	entity.vX = entity.vX * -1;

        }
        // Get the group node's X and Y but use the ImageView to obtain the height.
        if (entity.node.getTranslateY() > getGameSurface().getHeight() - displayNode.getBoundsInParent().getHeight() ||
        		entity.node.getTranslateY() < 0) {
        	entity.vY = entity.vY * -1;
        }
    }

    private void removeMissiles(Missile missile) {
    
        if (missile.node.getTranslateY() > getGameSurface().getHeight() -
                missile.node.getBoundsInParent().getHeight() ||
                missile.node.getTranslateY() < 0) {

            getEntityController().addEntitiesToBeRemoved(missile);
            getSceneNodes().getChildren().remove(missile.node);
          
        }
    }

    
    private void removeAtom(Atom atom) {
        
        if (atom.node.getTranslateY() > getGameSurface().getHeight()) {
            getEntityController().addEntitiesToBeRemoved(atom);
            getSceneNodes().getChildren().remove(atom.node);
            atomsPassTheSip++;
        }
    }
 
    @Override
    protected boolean handleCollision(Entity entityA, Entity entityB) {
        if (entityA != entityB) {
            if (entityA.collide(entityB)) {

                if (entityA == myShip || entityB == myShip) {
                	if (entityA.getName().equals("Atom") || entityB.getName().equals("Atom")) {
                	   entityA.handleDeath(this);
                	   entityB.handleDeath(this);
                	   gameOver();
                	   
                	}
                }else {
                	entityB.handleDeath(this);
                	entityA.handleDeath(this);
                }
            }
        }

        return false;
    }
}
