package com.moh.spaceinvader.game;


import com.moh.spaceinvader.engine.Entity;
import com.moh.spaceinvader.engine.GameUniverse;

import javafx.animation.FadeTransitionBuilder;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.CacheHint;
import javafx.scene.paint.Color;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.RadialGradientBuilder;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CircleBuilder;
import javafx.util.Duration;

public class Atom extends Entity {


    public Atom(double radius, Color fill, boolean gradientFill) {
        Circle sphere = CircleBuilder.create()
                .centerX(radius)
                .centerY(radius)
                .radius(radius)
                .fill(fill)
                .cache(true)
                .cacheHint(CacheHint.SPEED)
                .build();
        if (gradientFill) {
            RadialGradient rgrad = RadialGradientBuilder.create()
                    .centerX(sphere.getCenterX() - sphere.getRadius() / 3)
                    .centerY(sphere.getCenterY() - sphere.getRadius() / 3)
                    .radius(sphere.getRadius())
                    .proportional(false)
                    .stops(new Stop(0.0, Color.WHITE), new Stop(1.0, fill))
                    .build();

            sphere.setFill(rgrad);
        }
        // set javafx node to a circle
        node = sphere;
        collisionBounds = sphere;

    }

    @Override
    public void update() {
        node.setTranslateX(node.getTranslateX() + vX);
        node.setTranslateY(node.getTranslateY() + vY);
    }

    public Circle getAsCircle() {
        return (Circle) node;
    }

    public void implode(final GameUniverse gameUniverse) {
        vX = vY = 0;
        FadeTransitionBuilder.create()
                .node(node)
                .duration(Duration.millis(300))
                .fromValue(node.getOpacity())
                .toValue(0)
                .onFinished(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent arg0) {
                        isDead = true;
                        gameUniverse.getSceneNodes().getChildren().remove(node);

                    }
                })
                .build()
                .play();
    }

    public void handleDeath(GameUniverse gameUniverse) {
        implode(gameUniverse);
        super.handleDeath(gameUniverse);
    }
    
    public String getName() {
    	return "Atom";
    } 
}
