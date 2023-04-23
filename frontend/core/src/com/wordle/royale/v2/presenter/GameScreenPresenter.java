package com.wordle.royale.v2.presenter;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.wordle.royale.v2.model.Keyboard;
import com.wordle.royale.v2.model.Player;
import com.wordle.royale.v2.model.guessedWord;
import com.wordle.royale.v2.model.network.WordApiService;
import com.wordle.royale.v2.model.other.ScreenController;
import com.wordle.royale.v2.model.utils.WordleTimer;
import com.wordle.royale.v2.view.TextTileGrid;

public class GameScreenPresenter extends AbstractPresenter implements IKeyboard {

    private Keyboard keyboard;
    private TextTileGrid textTileGrid;
    private String feedback;
    private WordApiService api;
    private int word_id;

    private int score;

    public GameScreenPresenter(ScreenController screenController, Stage stage) {
        super(stage, screenController);
        score = 0;
        this.api = new WordApiService();
        keyboard = new Keyboard(this);
        textTileGrid = new TextTileGrid(25, 600);
        feedback = " ";
        getWord();
        addActor(keyboard);
        addActor(textTileGrid);
        /*
         * if (parent.getPreferences().getMusic()) {
         * music = Gdx.audio.newMusic(Gdx.files.internal("data/music.mp3"));
         * music.setLooping(true);
         * music.play();
         * }
         */
    }

    private void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public boolean checkTimer(WordleTimer timer) {
        if (timer.getInterval().equals("0:00")) {
            timer.stop();
            Player.getInstance().setScore(score);
            // music.stop();
            screenController.changeScreens(ScreenController.MENU);
            return true;
        }
        return false;
    }

    public void getWord() {
        api.getNewWord(new WordApiService.CallbackNewWord<Integer>() {
            @Override
            public void onSuccess(Integer wordID) {
                setWord_id(wordID);
                System.out.println("Your wordID:  " + wordID);
            }

            @Override
            public void onFailure(Throwable t) {
                System.out.println("Failed to connect to API");
            }
        });
    }

    public void setWord_id(int word_id) {
        this.word_id = word_id;
    }

    public int getWord_id() {
        return word_id;
    }

    public void handleKeyBoardInput(String s) {
        if (textTileGrid.getActiveRow().getIndex() == 5 && s.equals("OK")) {
            StringBuilder word = new StringBuilder();
            for (int i = 0; i < 5; i++) {
                word.append(textTileGrid.getActiveRow().getChar(i));
            }
            guess(word.toString().toLowerCase());
            return;
        }

        if (textTileGrid.getActiveRow().getIndex() >= 0 && s.equals("Del")) {
            this.textTileGrid.getActiveRow().removeCharacter();
            return;
        }

        if (textTileGrid.getActiveRow().getIndex() < 5) {
            if (s.equals("OK")) {
                return;
            }
            this.textTileGrid.getActiveRow().handleCharacterChange(s);
        }
    }

    public void guess(String word) {
        setFeedback(" ");
        api.guessWord(word, getWord_id(), new WordApiService.CallbackGuessWord<Boolean, guessedWord>() {

            @Override
            public void onSuccess(Boolean valid, guessedWord guessedWord) {
                colorTiles(guessedWord);
                score = (guessedWord.getGreen()*10) + (guessedWord.getYellow()*5);
                if (guessedWord.getCorrect() || textTileGrid.getActiveRowIndex() == 0) {
                    if (guessedWord.getCorrect()) {
                        setFeedback("     Correct!     \nHere is a new word");
                        Player.getInstance().setScore(50+(25*(textTileGrid.getActiveRowIndex())));
                    } else {
                        setFeedback("  No more tries  \nhere is a new word");
                        Player.getInstance().setScore(score);
                    }
                    resetGame();
                }
                textTileGrid.nextRow();
                System.out.println(Player.getInstance().getScore());
            }


            @Override
            public void onFailure(Throwable t) {
                setFeedback("\nWord not in list");
            }
        });
    }

    public void musicControls() {

    }

    public void resetGame() {
        for (int i = 0; i < 6; i++) {
            this.textTileGrid.setActiveRowIndex(i);
            this.textTileGrid.getActiveRow().setIndex(0);

            this.textTileGrid.getActiveRow().removeCharacters();
            this.textTileGrid.getActiveRow().resetTileXColor();
        }
        this.textTileGrid.setActiveRowIndex(6);
        getWord();

    }

    public void colorTiles(guessedWord guessedWord) {
        for (int i = 0; i < 5; i++) {
            int place = guessedWord.getGuessLetters().get(i).getPlacement();
            int exists = guessedWord.getGuessLetters().get(i).getStatus();
            textTileGrid.getActiveRow().updateTileXColor(i, place, exists);
        }
    }


    public String getFeedback() {
        return feedback;
    }

    public interface gameScreenView {
        void handleKeyBoardInput(String s);

        String getFeedbackFunc();
    }

    public interface keyboardButton {
        void handleKeyBoardInput();
    }

}
