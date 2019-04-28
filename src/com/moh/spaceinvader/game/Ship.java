package com.moh.spaceinvader.game;

import javafx.animation.*;
import javafx.scene.CacheHint;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CircleBuilder;

import com.moh.spaceinvader.engine.Entity;

public class Ship extends Entity {

    private final static int TWO_PI_DEGREES = 360;
    private final static int NUM_DIRECTIONS = 32;
    private final static float UNIT_ANGLE_PER_FRAME = ((float) TWO_PI_DEGREES / NUM_DIRECTIONS);
   
    private final static float MISSILE_THRUST_AMOUNT = 6.3F;
   


    private int uIndex = 8;

    private final Circle stopArea = new Circle();
    private final Group flipBook = new Group();;
    FadeTransition shieldFade;
    ImageView shipImageView ;

    public Ship() {
    	
    
        // Load one image.
   
        Image shipImage;
        shipImage = new Image(getClass().getClassLoader().getResource("ship.png").toExternalForm(), true);
        stopArea.setRadius(10);
        stopArea.setStroke(Color.ORANGE);
        
    

        
        shipImageView = new ImageView();
        
        shipImageView.setImage(shipImage);
        shipImageView.setVisible(true);
        shipImageView.setCache(true);
        shipImageView.setCacheHint(CacheHint.SPEED);
        shipImageView.setManaged(false);

        flipBook.getChildren().add(shipImageView);
        
        // set javafx node to an image
        
        shipImageView.setVisible(true);
        
        
        double x = 35;
        double y = 55;
        
        Circle shield = CircleBuilder.create()
                .radius(60)
                .strokeWidth(5)
                .stroke(Color.LIMEGREEN)
                .centerX(x)
                .centerY(y)
                .opacity(.30)
                .build();
        collisionBounds = shield;
        flipBook.getChildren().add(0, shield);
        
        this.node = flipBook;
        
        flipBook.setTranslateX(380);
        flipBook.setTranslateY(490);
        flipBook.setCache(true);
        flipBook.setCacheHint(CacheHint.SPEED);
        flipBook.setManaged(false);
        flipBook.setAutoSizeChildren(false);

    }



    @Override
    public void update() {
        flipBook.setTranslateX(flipBook.getTranslateX() + vX);
        flipBook.setTranslateY(flipBook.getTranslateY() + vY);
    }

  
    public void applyTheBrakes(double screenX, double screenY) {
        stopArea.setCenterX(screenX);
        stopArea.setCenterY(screenY);
    }

    public Missile fire() {
        Missile m1;

        float slowDownAmt = 0;
        int scaleBeginningMissle;
 
        m1 = new Missile(Color.RED);
        scaleBeginningMissle = 18;
        // velocity vector of the missile
        m1.vX = Math.cos(Math.toRadians(uIndex * UNIT_ANGLE_PER_FRAME)) * (MISSILE_THRUST_AMOUNT - slowDownAmt);
        m1.vY = -Math.sin(Math.toRadians(uIndex * UNIT_ANGLE_PER_FRAME)) * (MISSILE_THRUST_AMOUNT - slowDownAmt);
        
        // start to appear in the center of the ship to come out the direction of the nose of the ship.
        double offsetX = (shipImageView.getBoundsInLocal().getWidth() - m1.node.getBoundsInLocal().getWidth()) / 2;
        double offsetY = (shipImageView.getBoundsInLocal().getHeight() - m1.node.getBoundsInLocal().getHeight()) / 2;

        // initial launch of the missile   (multiply vector by 4 makes it appear at the nose of the ship)
        m1.node.setTranslateX(node.getTranslateX() + (offsetX + (m1.vX * scaleBeginningMissle)));
        m1.node.setTranslateY(node.getTranslateY() - (offsetY + (m1.vY * scaleBeginningMissle)));
        return m1;
    }

    void moveLeft() {
    	double new_x = flipBook.getTranslateX() - 5;
    	if (new_x >= 0) 
    	flipBook.setTranslateX(new_x);
    }

    void moveRight(double max) {
    	double new_x = flipBook.getTranslateX() + 5;
    	if (new_x + shipImageView.getBoundsInLocal().getWidth() < max)
    	flipBook.setTranslateX(new_x);	
    }
    
    public String getName() {
    	return "Ship"; 	
    }

}
