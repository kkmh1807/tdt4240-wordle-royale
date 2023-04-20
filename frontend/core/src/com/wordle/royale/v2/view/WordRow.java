package com.wordle.royale.v2.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.wordle.royale.v2.view.TextTile;

import java.util.ArrayList;

public class WordRow extends Actor {
    private ArrayList<TextTile> textTiles = new ArrayList<>();
    private float startPointX;
    private float startPointY;
    private float width;
    private float height;
    private int index = 0;


    public WordRow(float startPointX, float startPointY, float width, float height) {
        this.height = height;
        this.width = width;
        this.startPointX = startPointX;
        this.startPointY = startPointY;
        initTiles();
    }

    public int getIndex() {
        return index;
    }

    public String getChar(int index) {
        return textTiles.get(index).getChr();
    }

    public void handleCharacterChange(String s) {
        if(index < 5) {
            System.out.println(index);
            textTiles.get(index).setCharacter(s);
            index+=1;
            System.out.println(index);

        }
    }

    public void removeCharacter() {
        if (index == 0) {
            return;
        }
        textTiles.get(index-1).setCharacter(" ");
        index -= 1;
    }

    public void updateTileXColor(int index, int place, int exists) {
        if (exists == 1) {
             textTiles.get(index).setColor(new Color(Color.ORANGE.cpy()));
             if (place == 1) {
                 textTiles.get(index).setColor(new Color(Color.GREEN.cpy()));
             }
        }

    }

    private void initTiles() {
        float currentX = startPointX;
        for (int i = 0; i < 5; i++) {
            TextTile tt = new TextTile(i, currentX, startPointY, width, height);
            textTiles.add(tt);
            currentX += width + 25;
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        for(TextTile tt : textTiles) {
            tt.draw(batch, parentAlpha);
        }
    }
}


