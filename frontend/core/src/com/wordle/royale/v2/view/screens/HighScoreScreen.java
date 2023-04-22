package com.wordle.royale.v2.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.wordle.royale.v2.model.other.HighScore;
import com.wordle.royale.v2.model.other.ScreenController;
import com.wordle.royale.v2.presenter.HighScorePresenter;

public class HighScoreScreen implements Screen, HighScorePresenter.changeScreens {

    private SpriteBatch batch;
    private Stage stage;
    private Viewport viewport;
    private OrthographicCamera camera;
    protected Skin skin;
    private final float GAME_WORLD_WIDTH = Gdx.graphics.getWidth();
    private final float GAME_WORLD_HEIGHT = Gdx.graphics.getHeight();
    private TextButton backToMenu;
    private TextButton playAgain;
    private ScreenController parent;
    private BitmapFont title;
    private GlyphLayout layout;
    private BitmapFont font;
    private HighScorePresenter presenter;

    public HighScoreScreen(ScreenController parent) {
        this.parent = parent;
    }

    @Override
    public void show() {
        font = new BitmapFont();
        font.getData().setScale(2f, 4f);
        title = new BitmapFont(Gdx.files.internal("craftacular/raw/font-title-export.fnt"));
        title.getData().setScale(.7f, 2f);
        layout = new GlyphLayout(title, "Top 5 Legends");
        batch = new SpriteBatch();
        skin = new Skin(Gdx.files.internal("craftacular/skin/craftacular-ui.json"));
        stage = new Stage();
        presenter = new HighScorePresenter(parent);
        float aspectRatio = (float) Gdx.graphics.getHeight() / (float) Gdx.graphics.getWidth();
        camera = new OrthographicCamera();
        viewport = new FillViewport(GAME_WORLD_WIDTH * aspectRatio, GAME_WORLD_HEIGHT, camera);
        viewport.apply();
        backToMenu = new TextButton("To main menu", skin, "default");
        backToMenu.setScale(1f, 2f);
        backToMenu.setTransform(true);
        backToMenu.setPosition(Gdx.graphics.getWidth() / 2f - backToMenu.getWidth() / 2f, backToMenu.getHeight());
        playAgain = new TextButton("To main menu", skin, "default");
        playAgain.setScale(1f, 2f);
        playAgain.setTransform(true);
        playAgain.setPosition(Gdx.graphics.getWidth() / 2f - playAgain.getWidth() / 2f,
                Gdx.graphics.getHeight() / 2f - playAgain.getHeight() * 2);
        setupChangeToGame();
        setupChangeToMenu();
        Gdx.input.setInputProcessor((stage));
        stage.addActor(backToMenu);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        title.draw(batch, "Top 10 Legends", Gdx.graphics.getWidth()/2f - layout.width/2, Gdx.graphics.getHeight()- layout.height*2);

        for (int i = 0; i < presenter.getHighScore().getHighscoreList().size(); i++) {
            float xPos = Gdx.graphics.getWidth() / 2.5f;
            float yPos = Gdx.graphics.getHeight() / 1.5f - (i * 75);
            String name = presenter.getHighScore().getHighscoreList().get(i).getUsername();
            Integer score = presenter.getHighScore().getHighscoreList().get(i).getScore();
            String text = i + 1 + ". " + name + " : " + score + " points";
            font.draw(batch, text, xPos, yPos);
        }

        stage.draw();

        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();
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
        batch.dispose();
        stage.dispose();
    }

    @Override
    public void setupChangeToMenu() {
        backToMenu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                presenter.changeScreensFunc(ScreenController.MENU);
            }
        });
    }

    @Override
    public void setupChangeToGame() {
        playAgain.addListener((new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                presenter.changeScreensFunc(ScreenController.GAME);
            }
        }));

    }
}
