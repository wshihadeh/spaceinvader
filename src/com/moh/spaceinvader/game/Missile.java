package com.moh.spaceinvader.game;

import javafx.scene.paint.Color;

public class Missile extends Atom {
    public Missile(Color fill) {
        super(5, fill, false);
    }

    public Missile(int radius, Color fill) {
        super(radius, fill, true);
    }
}