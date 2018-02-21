package com.ilya.prototype.Screens;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.graphics.g2d.GlyphLayout.GlyphRun;
import com.ilya.prototype.Game;
import com.ilya.prototype.Main;
import com.badlogic.gdx.math.Vector3;

public class Menu extends InputAdapter implements Screen {

    OrthographicCamera camera;
    SpriteBatch batch;
    BitmapFont fontForWelcome;
    BitmapFont fontForPressEnter;
    GlyphLayout glyphLayoutForWelcome;
    GlyphLayout glyphLayoutForPressEnter;
    Main main;
    Vector3 target_color;
    Vector3 standart_color;
    Vector3 color_of_font;
    boolean switch_colors;

    final public float LERP_SPEED = 0.2f;

    float r,g,b;

    // int test_parameter_size = 40;


    public Menu(Main main) {
        this.main = main;
    }

    public void createFonts() {
        switch_colors = true;
        r = 255f/255f;
        g = 131f/255f;
        b = 94f/255f;
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.local("Pacifico.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 40;
        fontForWelcome = generator.generateFont(parameter);
        glyphLayoutForWelcome.setText(fontForWelcome,"Welcome to the game");
        parameter.size = 20;
        fontForPressEnter = generator.generateFont(parameter);
        fontForPressEnter.setColor(r,g,b,1f);
        glyphLayoutForPressEnter.setText(fontForPressEnter,"Press Enter to proceed");
        standart_color = new Vector3(r,g,b);
        target_color = new Vector3(1,1,1);
        color_of_font = new Vector3(r,g,b);
    }

    private void update(){
        System.out.println(switch_colors);
        if (switch_colors){

            color_of_font.lerp(target_color,LERP_SPEED);
            fontForPressEnter.setColor(color_of_font.x,color_of_font.y,
                                       color_of_font.z,1f);

            // System.out.println( (int) color_of_font.x+ "   :"+ (int) target_color.x+"\n"+
            //                     (int) color_of_font.x+"   :"+ (int) target_color.y+"\n"+
            //                     (int) color_of_font.x+"   :"+ (int) target_color.z);

             if (MathUtils.isEqual(color_of_font.x,target_color.x) &&
                 MathUtils.isEqual(color_of_font.y,target_color.y)&&
                 MathUtils.isEqual(color_of_font.z,target_color.z)){

                    switch_colors = false;
            }

} else {
    color_of_font.lerp(standart_color,LERP_SPEED);
    fontForPressEnter.setColor(color_of_font.x,color_of_font.y,
                                       color_of_font.z,1f);

    if (MathUtils.isEqual(color_of_font.x,standart_color.x) &&
        MathUtils.isEqual(color_of_font.y,standart_color.y)&&
        MathUtils.isEqual(color_of_font.z,standart_color.z))
            switch_colors = true;
}

    // }
        // else {
        // fontForPressEnter.setColor(MathUtils.lerp(r,255f/255f,10.0f*Gdx.graphics.getDeltaTime()),
        //                            MathUtils.lerp(g,131f/255f,10.0f*Gdx.graphics.getDeltaTime()),
        //                            MathUtils.lerp(b,94f/255f,10.0f*Gdx.graphics.getDeltaTime()),
        //                                                  1f);    
        // }
        // glyphLayoutForPressEnter.GlyphRun.color.r = 0;
        // glyphLayoutForPressEnter.GlyphRun.color.g = 0;
        // glyphLayoutForPressEnter.GlyphRun.color.b = 0;
        glyphLayoutForPressEnter.setText(fontForPressEnter,"Press Enter to proceed");
        
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
        camera = new OrthographicCamera(640,480);
        batch = new SpriteBatch();
        glyphLayoutForWelcome = new GlyphLayout();
        glyphLayoutForPressEnter = new GlyphLayout();
        createFonts();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.188f, 0.179f, 0.49f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        fontForWelcome.draw(batch,glyphLayoutForWelcome,-glyphLayoutForWelcome.width/2,40);
        fontForPressEnter.draw(batch,glyphLayoutForPressEnter,
                -glyphLayoutForPressEnter.width/2,-Gdx.graphics.getHeight()/2+59);

        batch.end();

        update();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        fontForWelcome.dispose();
        fontForPressEnter.dispose();
        batch.dispose();

    }
    @Override
    public boolean keyDown(int keycode) {
        if (keycode== Input.Keys.ENTER) {
            main.setScreen(new Game());
        }
        // if (keycode==Input.Keys.UP){
        //     test_parameter_size++;
        //     createFonts();
        // }
        // if (keycode==Input.Keys.DOWN){
        //     test_parameter_size--;
        //     createFonts();
        // }

        return false;
    }
}
