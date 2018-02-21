package com.ilya.prototype;

import com.badlogic.gdx.*;
import com.ilya.prototype.Screens.Menu;

public class Main extends com.badlogic.gdx.Game {

    public static int STATUS = 0;
    @Override
    public void create() {
        setScreen(new Menu(this));
    }
}
