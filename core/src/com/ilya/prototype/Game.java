package com.ilya.prototype;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.Array;
import com.ilya.prototype.Screens.Menu;


public class Game extends ScreenAdapter implements InputProcessor {

    boolean GAME_OVER;
    SpriteBatch batch;
    GlyphLayout glyphLayout;
    OrthographicCamera camera;
    ShapeRenderer shapeRenderer;
    Vector2 coordsOfRectangle;
    Vector2 dimensionOfRectangle;
    Array<Enemy> arrayOfEnemies;
    int timer;
    public static final int BOARDER_X = 640;
    Rectangle player;
    BitmapFont font;
    int inGameTimer;
    Array<Rectangle> lineIsTaken;
    private static final boolean GOD_MODE = false;
    private boolean checkBottom;
    private STATE state;
    private long move_update_timer;

    public Game() {

    }

    public void createFonts() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.local("8bitOperatorPlus-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 30;
        font = generator.generateFont(parameter); // font size 12 pixels
        glyphLayout = new GlyphLayout();

    }

    public enum STATE{
        STABLE,UP,DOWN,LEFT,RIGHT,LEFT_UP,RIGHT_UP,LEFT_DOWN,RIGHT_DOWN
    }

    @Override
    public void show () {
        create();
    }


    public void create() {
        System.out.println("Game Activated");
        Enemy.reset();
        GAME_OVER = false;
        checkBottom = false;
        createFonts();
        batch = new SpriteBatch();
        camera = new OrthographicCamera(640, 480);
        shapeRenderer = new ShapeRenderer();
        camera.setToOrtho(false);
        coordsOfRectangle = new Vector2(320, 0);
        dimensionOfRectangle = new Vector2(20, 20);
        Gdx.input.setInputProcessor(this);
        arrayOfEnemies = new Array<Enemy>();
        inGameTimer = 0;
        lineIsTaken = new Array<Rectangle>();

        //Bottom
        lineIsTaken.add(new Rectangle(0,-20,640,20));
        //Top
        lineIsTaken.add(new Rectangle(0,480,640,20));
        //Left size
        lineIsTaken.add(new Rectangle(-20,0,20,480));
        //Right size
        lineIsTaken.add(new Rectangle(640,0,20,480));

        player = new Rectangle();
        state = STATE.STABLE;
        move_update_timer = 5;
    }

    @Override
    public void render(float delta) {
        if (!GAME_OVER) {
            inGameTimer++;
            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            shapeRenderer.setProjectionMatrix(camera.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.rect(coordsOfRectangle.x, coordsOfRectangle.y, dimensionOfRectangle.x, dimensionOfRectangle.y);
            shapeRenderer.end();
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(Color.BLUE);

            drawSharp();

            shapeRenderer.end();

            renderTakenLines();

            batch.begin();
            font.draw(batch, "" + (lineIsTaken.size-4), 560, 460);
            batch.end();
            timer++;
            if (timer > 15) {
                createEnemie();
                timer = 0;
            }
            if (arrayOfEnemies.size != 0) {
                for (Enemy enemy : arrayOfEnemies) {
                    enemy.render(shapeRenderer);
                    enemy.update();
                }
            }
            update();
        } else endGame();
    }

    private void renderTakenLines() {
        if (lineIsTaken.size != 0) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(Color.GREEN);
            for (Rectangle r : lineIsTaken) {
                shapeRenderer.rect(r.x, r.y, r.width, r.height);
            }
            shapeRenderer.end();
        }
    }

    public void drawSharp() {
        int x = 0;
        int y = 0;
        int x2 = 640;
        int y2 = 0;
        while (y2 != 480) {
            shapeRenderer.line(x, y, x2, y2);
            y += 20;
            y2 += 20;
        }
        while (x != 640) {
            x2 = x;
            shapeRenderer.line(x, 0, x2, 480);
            x += 20;

        }
    }

    @Override
    public boolean keyDown(int keycode) {
            //Right
            if (keycode == 22) {
                state = STATE.RIGHT;
            }
            //Left
            if (keycode == 21) {
                state = STATE.LEFT;
            }
            //Up
            if (keycode == 19) {
                state = STATE.UP;
            }
            //Down
            if (keycode == 20) {
                state = STATE.DOWN;
            }
        return false;
    }

    private boolean checkForBoundaries() {
        player.set(coordsOfRectangle.x,coordsOfRectangle.y,dimensionOfRectangle.x,dimensionOfRectangle.y);
        for (int i = 0;i<lineIsTaken.size;i++){
            if (player.overlaps(lineIsTaken.get(i)))
                return false;
        }
//        if (player.x < 0)
//            coordsOfRectangle.x = 0;
//        if (player.y < 0)
//            coordsOfRectangle.y = 0;
//        if (player.x > 620)
//            coordsOfRectangle.x = 620;
//        if (player.y > 460)
//            coordsOfRectangle.y = 460;
        return true;

    }

    @Override
    public boolean keyUp(int keycode) {
        state = STATE.STABLE;
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    private void movePlayer(STATE state) {
        if (move_update_timer <= 0) {
            switch (state) {
                case STABLE: {
                    break;
                }
                case RIGHT: {
                    coordsOfRectangle.x += 20;
                    if (!checkForBoundaries())
                        coordsOfRectangle.x -= 20;
                    break;
                }
                case LEFT: {
                    coordsOfRectangle.x -= 20;
                    if (!checkForBoundaries())
                        coordsOfRectangle.x += 20;
                    break;
                }
                case UP: {
                    coordsOfRectangle.y += 20;
                    if (!checkForBoundaries())
                        coordsOfRectangle.y -= 20;
                    break;
                }
                case DOWN: {
                    coordsOfRectangle.y -= 20;
                    if (!checkForBoundaries())
                        coordsOfRectangle.y += 20;
                    break;
                }
                case LEFT_DOWN:{
                    coordsOfRectangle.x-=20;
                    coordsOfRectangle.y-=20;
                    break;
                }
                case LEFT_UP: {
                    coordsOfRectangle.x-=20;
                    coordsOfRectangle.y+=20;
                    break;
                }
                case RIGHT_DOWN:{
                    coordsOfRectangle.x+=20;
                    coordsOfRectangle.y-=20;
                    break;
                }
                case RIGHT_UP:{
                    coordsOfRectangle.x+=20;
                    coordsOfRectangle.y+=20;
                    break;
                }
            }
            move_update_timer = 5;
        }
    }

    private void createEnemie() {
        arrayOfEnemies.add(new Enemy());

    }

    private void update() {

        move_update_timer-=Gdx.graphics.getDeltaTime();
        if (!GAME_OVER) {
            checkLine();
            movePlayer(state);
        }

        if (!GOD_MODE) {
             player.set(coordsOfRectangle.x, coordsOfRectangle.y, dimensionOfRectangle.x, dimensionOfRectangle.y);
            for (int i = 0; i < arrayOfEnemies.size; i++) {
                if (arrayOfEnemies.get(i).KILL) {
                    arrayOfEnemies.removeIndex(i);
                }
                for (Rectangle r : arrayOfEnemies.get(i).getRectangleArray()){
                    if (player.overlaps(r))
                        endGame();
                }
            }
        }
        if (lineIsTaken.size>27) {
            GAME_OVER = true;
        }
    }

    private void endGame() {
        GAME_OVER = true;
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (lineIsTaken.size>27){
            playerWin();
        } else {
            float font_y = camera.viewportHeight / 2;
            batch.begin();

            glyphLayout.setText(font,"GAME OVER");
            font.draw(batch,glyphLayout,(Gdx.graphics.getWidth()-glyphLayout.width)/2,font_y);

            glyphLayout.setText(font,"Score: "+(lineIsTaken.size-4));
            font.draw(batch,glyphLayout,(Gdx.graphics.getWidth()-glyphLayout.width)/2,font_y-40);

            glyphLayout.setText(font,"Press ENTER for restart...");
            font.draw(batch,glyphLayout,(Gdx.graphics.getWidth()-glyphLayout.width)/2,35);

            // font.draw(batch,glyphLayout,camera.viewportWidth / 2 - 90,camera.viewportHeight / 2);
            // font.draw(batch, "GAME OVER", camera.viewportWidth / 2 - 90, camera.viewportHeight / 2);
            // font.draw(batch, "Score: " + (lineIsTaken.size-4), camera.viewportWidth / 2 - 94, camera.viewportHeight / 2 - 50);
            // font.draw(batch, "Press ENTER for restart...", camera.viewportWidth / 2 - 215, camera.viewportHeight / 2 - 100);
            batch.end();
            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER))
                this.create();
        }
    }

    private void checkLine() {
        if (!checkBottom) {
                if (coordsOfRectangle.y >= Enemy.MAX_Y) {
                    lineIsTaken.add(new Rectangle(0, Enemy.MAX_Y, 640, 20));
                    coordsOfRectangle.y = Enemy.MAX_Y - 20;
                    Enemy.MAX_Y -= 20;
                    checkBottom = true;
                }
        } else {
            if (coordsOfRectangle.y <= Enemy.MIN_Y + 20) {
                lineIsTaken.add(new Rectangle(0, Enemy.MIN_Y, 640, 20));
                coordsOfRectangle.y = Enemy.MIN_Y + 20;
                Enemy.MIN_Y += 20;
                checkBottom = false;
            }
        }
    }

    private void playerWin(){
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        font.setColor(Color.FOREST);
        float font_y = Gdx.graphics.getHeight()/2;

        glyphLayout.setText(font,"YOU WIN!");
        font.draw(batch,glyphLayout,(Gdx.graphics.getWidth()-glyphLayout.width)/2,font_y);

        glyphLayout.setText(font,"Press ENTER to restart");
        font.draw(batch,glyphLayout,(Gdx.graphics.getWidth()-glyphLayout.width)/2,font_y-40);

        batch.end();
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER))
            this.create();
    }
}