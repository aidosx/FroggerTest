package com.ilya.prototype;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Enemy {
    int length;
    int speed;
    Vector2 coordsOfEnemy;
    Vector2 dimensionOfEnemy;
    Array<Rectangle> rectangleArray;
    boolean KILL;
    protected static int MAX_Y = 460;
    protected static int MIN_Y = 0;


    public Enemy(){
        KILL = false;
        length = MathUtils.random(1,5);
        speed = MathUtils.random(1,5);
        rectangleArray = new Array<Rectangle>();
        int y;
        try {
            do {
                y = MathUtils.random(MIN_Y, MAX_Y);
            }
            while (y % 20 != 0);
        } catch (IllegalArgumentException e){
            y = 800;
        }

        coordsOfEnemy = new Vector2(0,y);
        dimensionOfEnemy = new Vector2(20,20);
        rectangleArray.add(new Rectangle(coordsOfEnemy.x,coordsOfEnemy.y,dimensionOfEnemy.x,dimensionOfEnemy.y));
        for (int i = 1;i<length+1;i++){
            rectangleArray.add(new Rectangle(rectangleArray.get(i-1).x-20,rectangleArray.get(i-1).y,dimensionOfEnemy.x,dimensionOfEnemy.y));

        }

    }


    protected void render(ShapeRenderer shapeRenderer){
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(com.badlogic.gdx.graphics.Color.YELLOW);
        for (int i = 0;i<rectangleArray.size;i++){
            shapeRenderer.rect(rectangleArray.get(i).x,rectangleArray.get(i).y,dimensionOfEnemy.x,dimensionOfEnemy.y);
        }

        shapeRenderer.end();
    }

    protected void update(){
        if (rectangleArray.first().x> Game.BOARDER_X)
            dispose();
        for (int i = 0;i<rectangleArray.size;i++){
            rectangleArray.get(i).x+= Gdx.graphics.getDeltaTime()*220+speed;
        }
    }

    private void dispose() {
        KILL = true;
    }

    public Array<Rectangle> getRectangleArray(){
        return rectangleArray;
    }

    protected static void reset(){
        MAX_Y = 460;
        MIN_Y = 0;
    }
}
