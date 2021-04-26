package com.group9.partysnake.gameElements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Snake {

    private int snakeX = 0, snakeY = 0;
    private boolean hasHit = false;

    private Vector2 formerPosition = new Vector2(0,0);

    private int SNAKE_MOVEMENT = 32;
    private Texture snakeHead = new Texture("snakehead.png");
    private Array<BodyPart> bodyParts = new Array<BodyPart>();

    // For handling the direction changes
    private static final int RIGHT = 0;
    private static final int LEFT = 1;
    private static final int UP = 2;
    private static final int DOWN = 3;

    //Initial movement

    private int snakeDirection = RIGHT;

    //For stoppping doublebacks
    private boolean directionSet = false;

    public class BodyPart {

        private Vector2 bodyPosition;
        private int x, y;

        private Texture texture = new Texture("snakebody.png");

        public BodyPart() {
        }

        public void updateBodyPosition(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public void updateBodyPosition1(Vector2 pos) {
            this.bodyPosition = pos;
        }

        public void draw(Batch batch) {
            if (!(x == (int)snakeX && y == (int)snakeY)) batch.draw(texture, x, y);
        }


        public void draw1(Batch batch) {
            if (!(bodyPosition.x ==snakeX && bodyPosition.y ==snakeY)) {
                batch.draw(texture, bodyPosition.x, bodyPosition.y);
            }
        }

    }

    public void increaseLength(){
        BodyPart bodyPart = new BodyPart();
        bodyPart.updateBodyPosition((int)snakeX,(int)snakeY);
        bodyParts.insert(0,bodyPart);
//        System.out.println("increaseLength");
//        System.out.println(bodyParts.size);


    }


    public void checkSnakeEat(SuperEatable eatable){
        if (eatable.isAvailable &&
                eatable.position.x == snakeX && eatable.position.y == snakeY)
        {
            increaseLength();
//            System.out.println("You just got eaten");
//            System.out.println(position);
//            System.out.println(bodyParts.get(0).bodyPosition);
//
//            System.out.println(position.equals(bodyParts.get(0).bodyPosition));
            eatable.isAvailable = false;
        }
    }




    private void checkForOutBounds(){
        if (snakeX >= Gdx.graphics.getWidth()){
           snakeX = 0;
        }
        if (snakeX < 0){
           snakeX = Gdx.graphics.getWidth() - SNAKE_MOVEMENT;
        }
        if (snakeY >= Gdx.graphics.getHeight()){
           snakeY= 0;
        }
        if (snakeY < 0){
           snakeY= Gdx.graphics.getHeight() - SNAKE_MOVEMENT;
        }
    }

    private void checkSnakeBodyCollision() {
        for (BodyPart bodyPart : bodyParts) {
            if (bodyPart.x == snakeX && bodyPart.y == snakeY) hasHit = true;
        }
    }


    public void setSnakeDirection(int direction){
        //snakeDirection = direction;
        updateDirection(direction);
    }

    private void updateIfNotOppositeDirection(int newSnakeDirection, int
            oppositeDirection) {
        if (snakeDirection != oppositeDirection || bodyParts.size == 0) snakeDirection =
                newSnakeDirection;
    }

    private void updateDirection(int newSnakeDirection) {
        if (!directionSet && snakeDirection != newSnakeDirection) {
            directionSet = true;
            switch (newSnakeDirection) {
                case LEFT: {
                    updateIfNotOppositeDirection(newSnakeDirection, RIGHT);
                }
                break;
                case RIGHT: {
                    updateIfNotOppositeDirection(newSnakeDirection, LEFT);
                }
                break;
                case UP: {
                    updateIfNotOppositeDirection(newSnakeDirection, DOWN);
                }
                break;
                case DOWN: {
                    updateIfNotOppositeDirection(newSnakeDirection, UP);
                }
                break;
            }
        }
    }


    public void moveSnake(){
        formerPosition.x =snakeX;
        formerPosition.y =snakeY;

        switch(snakeDirection){
            case RIGHT: {
               snakeX += SNAKE_MOVEMENT;
                return;
            }
            case LEFT: {
               snakeX -= SNAKE_MOVEMENT;
                return;
            }
            case UP: {
               snakeY+= SNAKE_MOVEMENT;
                return;
            }
            case DOWN: {
               snakeY-= SNAKE_MOVEMENT;
                return;
            }
        }

    }

    public void updateBodyPartsPosition() {
        if (bodyParts.size > 0) {
            BodyPart bodyPart = bodyParts.removeIndex(0);
//            System.out.println("-----------------------");
//            System.out.println("former pos" +bodyPart.bodyPosition);
            bodyPart.updateBodyPosition((int) formerPosition.x, (int) formerPosition.y);
//            System.out.println("Currentpos" +bodyPart.bodyPosition);
//            System.out.println("-----------------------");

            bodyParts.add(bodyPart);
        }
    }

    public void draw(Batch batch){
        batch.draw(snakeHead,snakeX,snakeY);
        for (BodyPart bodyPart : bodyParts) {
            bodyPart.draw(batch);
        }

    }

    public void updateSnake(){
        if (!hasHit){
            moveSnake();
            checkForOutBounds();
            updateBodyPartsPosition();
            checkSnakeBodyCollision();
            directionSet = false;
        }



    }

    public int getSnakeX() {
        return snakeX;
    }

    public int getSnakeY() {
        return snakeY;
    }

    public Snake(){
        System.out.print("it's alive!!");
        this.snakeX = 0;
        this.snakeY = 0;
    }
}