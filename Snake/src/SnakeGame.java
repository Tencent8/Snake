import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener, KeyListener{
    int boardWidth;
    int boardHeight;
    int tileSize = 25;
    int appleCount = 0;


    private class Tile {
        int x;
        int y;
        Tile(int x, int y){
            this.x = x;
            this.y = y;
        }
    }

    Tile apple;
    Tile snakeHead;
    ArrayList<Tile> snakeBody;
    Random random;
    Timer gameLoop;
    int velocityX;
    int velocityY;
    boolean gameOver = false;


    // Constructor
    SnakeGame(int boardWidth, int boardHeight){
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.GREEN);

        snakeHead = new Tile(5, 5);
        snakeBody = new ArrayList<Tile>();

        apple = new Tile(10, 10);
        random = new Random();
        placeApple();

        velocityX = 0;
        velocityY = 0;

        gameLoop = new Timer(100, this);
        gameLoop.start();

        addKeyListener(this);
        setFocusable(true);

    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        /*
        for (int i = 0; i < boardWidth/tileSize; i++) {
            g.drawLine(i * tileSize, 0, i * tileSize, boardHeight);
            g.drawLine(0, i * tileSize, boardWidth, i * tileSize);
        }
         */
        // Apple
        g.setColor(Color.red);
        //g.fillRect(apple.x * tileSize, apple.y * tileSize, tileSize, tileSize);
        g.fill3DRect(apple.x * tileSize, apple.y * tileSize, tileSize, tileSize, true);

        // Snake Head
        g.setColor(Color.blue);
        //g.fillRect(snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize, tileSize);
        g.fill3DRect(snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize, tileSize, true);

        // Snake Body
        g.setColor(Color.blue);
        for (int i = 0; i < snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);
            //g.fillRect(snakePart.x * tileSize, snakePart.y * tileSize, tileSize, tileSize);
            g.fill3DRect(snakePart.x * tileSize, snakePart.y * tileSize, tileSize, tileSize, true);
        }


        // Score
        g.setFont(new Font("ARIAL", Font.PLAIN, 16));
        if (gameOver){
            g.setColor(Color.red);
            g.drawString("Game Over" + String.valueOf(snakeBody.size()), tileSize -16, tileSize);
        } else {
            g.drawString("Score: " + String.valueOf(snakeBody.size()), tileSize -16, tileSize);
        }
    }



    public void placeApple(){
        apple.x = random.nextInt(boardWidth/tileSize);
        apple.y = random.nextInt(boardHeight/tileSize);
        // Check if apple is on snakebody or snakehead
        for (int i = 0; i < snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);
            if (collision(apple, snakePart)){
                placeApple();
            }
        }
        if (collision(apple, snakeHead)){
            placeApple();
        }
    }

    public boolean collision(Tile tile1, Tile tile2){
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }

    //Move function
    public void move() {
        if (collision(snakeHead, apple)){
            snakeBody.add(new Tile(apple.x, apple.y));
            placeApple();
            appleCount++;
        }

        // Move snake body
        for (int i = snakeBody.size() - 1; i >= 0; i--) {
            Tile snakePart = snakeBody.get(i);
            if (i == 0) {
                snakePart.x = snakeHead.x;
                snakePart.y = snakeHead.y;
            } else {
                Tile prevsnakePart = snakeBody.get(i - 1);
                snakePart.x = prevsnakePart.x;
                snakePart.y = prevsnakePart.y;
            }
        }

        snakeHead.x += velocityX;
        snakeHead.y += velocityY;

        //game over conditions
        for (int i = 0; i < snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);
            if (collision(snakeHead, snakePart)){
                gameOver = true;
            }
        }

        if (snakeHead.x*tileSize < 0 || snakeHead.x*tileSize > boardWidth - 1||
            snakeHead.y*tileSize < 0 || snakeHead.y*tileSize > boardHeight - 1){
            gameOver = true;
        }
    }




    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP && velocityY != 1){
            velocityX = 0;
            velocityY = -1;
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN && velocityY != -1){
            velocityX = 0;
            velocityY = 1;
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT && velocityX != 1){
            velocityX = -1;
            velocityY = 0;
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX != -1){
            velocityX = 1;
            velocityY = 0;
        }
        // Restart the game if the user clicks space
        if (e.getKeyCode() == KeyEvent.VK_SPACE){
            snakeHead = new Tile(5, 5);
            snakeBody = new ArrayList<Tile>();
            placeApple();
            velocityX = 0;
            velocityY = 0;
            gameOver = false;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameOver){
            gameLoop.stop();
            // Create a popup window
            JOptionPane.showMessageDialog(this, "Game Over", "Game Over", JOptionPane.YES_NO_OPTION);
            // Restart the game if the user clicks yes
            if (JOptionPane.YES_OPTION == 0){
                snakeHead = new Tile(5, 5);
                snakeBody = new ArrayList<Tile>();
                placeApple();
                velocityX = 0;
                velocityY = 0;
                gameOver = false;
                gameLoop.start();
            }
        }
    }


    // These methods are required by the KeyListener interface.

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

}
