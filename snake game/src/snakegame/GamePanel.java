package snakegame;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

public class GamePanel extends JPanel implements KeyListener {
    private static final int WIDTH = 600;
    private static final int HEIGHT = 600;
    private static final int UNIT_SIZE = 25;
    private static final int GAME_UNITS = (WIDTH * HEIGHT) / (UNIT_SIZE * UNIT_SIZE);
    private static final int DELAY = 75;

    private final int[] snakeX;
    private final int[] snakeY;
    private int snakeLength;
    private int appleX;
    private int appleY;
    private char direction;
    private boolean isRunning;

    public GamePanel() {
        snakeX = new int[GAME_UNITS];
        snakeY = new int[GAME_UNITS];
        snakeLength = 1;
        direction = 'R';
        isRunning = false;

        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);
    }

    public void startGame() {
        isRunning = true;
        spawnApple();
        new Thread(this::gameLoop).start();
    }

    private void gameLoop() {
        while (isRunning) {
            move();
            checkCollision();
            repaint();
            try {
                Thread.sleep(DELAY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void move() {
        for (int i = snakeLength; i > 0; i--) {
            snakeX[i] = snakeX[i - 1];
            snakeY[i] = snakeY[i - 1];
        }

        switch (direction) {
            case 'U':
                snakeY[0] -= UNIT_SIZE;
                break;
            case 'D':
                snakeY[0] += UNIT_SIZE;
                break;
            case 'L':
                snakeX[0] -= UNIT_SIZE;
                break;
            case 'R':
                snakeX[0] += UNIT_SIZE;
                break;
        }
    }

    private void checkCollision() {
        if (snakeX[0] < 0 || snakeY[0] < 0 || snakeX[0] >= WIDTH || snakeY[0] >= HEIGHT) {
            gameOver();
        }

        for (int i = snakeLength - 1; i > 0; i--) {
            if (snakeX[0] == snakeX[i] && snakeY[0] == snakeY[i]) {
                gameOver();
            }
        }

        if (snakeX[0] == appleX && snakeY[0] == appleY) {
            snakeLength++;
            spawnApple();
        }
    }

    private void gameOver() {
        isRunning = false;
        JOptionPane.showMessageDialog(this, "Game Over", "Snake Game", JOptionPane.PLAIN_MESSAGE);
    }




        private void spawnApple() {
            Random random = new Random();
            appleX = random.nextInt((WIDTH / UNIT_SIZE)) * UNIT_SIZE;
            appleY = random.nextInt((HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            draw(g);
        }

        private void draw(Graphics g) {
            if (isRunning) {
                g.setColor(Color.RED);
                g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

                for (int i = 0; i < snakeLength; i++) {
                    if (i == 0) {
                        g.setColor(Color.GREEN);
                        g.fillRect(snakeX[i], snakeY[i], UNIT_SIZE, UNIT_SIZE);
                    } else {
                        g.setColor(new Color(45, 180, 0));
                        g.fillRect(snakeX[i], snakeY[i], UNIT_SIZE, UNIT_SIZE);
                    }
                }

                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.BOLD, 20));
                FontMetrics metrics = getFontMetrics(g.getFont());
                g.drawString("Score: " + (snakeLength - 1), (WIDTH - metrics.stringWidth("Score: " + (snakeLength - 1))) / 2, g.getFont().getSize());
            }
        }

        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            if (key == KeyEvent.VK_UP && direction != 'D') {
                direction = 'U';
            } else if (key == KeyEvent.VK_DOWN && direction != 'U') {
                direction = 'D';
            } else if (key == KeyEvent.VK_LEFT && direction != 'R') {
                direction = 'L';
            } else if (key == KeyEvent.VK_RIGHT && direction != 'L') {
                direction = 'R';
            }
        }

        @Override
        public void keyTyped(KeyEvent e) {}

        @Override
        public void keyReleased(KeyEvent e) {}
    }
