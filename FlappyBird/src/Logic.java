import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Logic implements ActionListener, KeyListener {

    public enum GameState {
        MENU,
        PLAYING,
        GAME_OVER
    }

    int frameWidth = 360; int frameHeight = 640;

    int playerStartPosX = frameWidth / 2;
    int playerStartPosY = frameHeight / 2;
    int playerWidth = 34;
    int playerHeight = 24;

    // tambahkan atribut posisi dan ukuran pipa
    int pipeStartPosX = frameWidth;
    int pipeStartPosY = 0;
    int pipeWidth = 64;
    int pipeHeight = 512;

    View view;
    Image birdImage;
    Player player;

    // tambahkan list Pipa, dan gambarNya
    Image lowerPipeImage;
    Image upperPipeImage;
    Image backgroundImage;
    ArrayList<Pipe> pipes;

    Timer gameLoop;
    Timer pipesCooldown;
    int gravity = 1;

    int pipeVelocityX = -2;

    private boolean gameOver = false;
    private int score = 0;

    private GameState gameState = GameState.MENU;

    // Game dimulai dalam keadaan dijeda
    private boolean isPaused = true;

    public Logic(){
        birdImage = new ImageIcon(getClass().getResource("assets/bird.png")).getImage();
        player = new Player(playerStartPosX, playerStartPosY, playerWidth, playerHeight, birdImage);

        lowerPipeImage = new ImageIcon(getClass().getResource("assets/lowerPipe.png")).getImage();
        upperPipeImage = new ImageIcon(getClass().getResource("assets/upperPipe.png")).getImage();
        backgroundImage = new ImageIcon(getClass().getResource("/assets/flappy-bird-background.jpg")).getImage();
        pipes = new ArrayList<Pipe>();

        pipesCooldown = new Timer(1500, new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent actionEvent){
                // System.out.println("Pipa");
                if (isPaused) return;
                placePipes();
            }
        });
        pipesCooldown.start();

        gameLoop = new Timer(1000/60, this);
        gameLoop.start();
    }

    public void resumeGame() {
        this.isPaused = false;
    }

    public GameState getGameState() {
        return this.gameState;
    }

    public boolean isGameOver() {
        return this.gameOver;
    }

    public void setView(View view){
        this.view = view;
    }

    public Player getPlayer() {
        return player;
    }

    public int getScore() {
        return this.score;
    }

    public Image getBackgroundImage() {
        return this.backgroundImage;
    }

    public ArrayList<Pipe> getPipes() { return pipes;}

    public void placePipes() {
        int randomPosY = (int) (pipeStartPosY - pipeHeight / 4 - Math.random() * (pipeHeight / 2));
        int openingSpace = frameHeight / 4;

        Pipe upperPipe = new Pipe(pipeStartPosX, randomPosY, pipeWidth, pipeHeight, upperPipeImage);
        pipes.add(upperPipe);

        Pipe lowerPipe = new Pipe(pipeStartPosX, (randomPosY + openingSpace + pipeHeight), pipeWidth, pipeHeight,
                lowerPipeImage);
        pipes.add(lowerPipe);

    }

    public void move(){
        player.setVelocityY(player.getVelocityY() + gravity);
        player.setPosY(player.getPosY() + player.getVelocityY());
        player.setPosY(Math.max(player.getPosY(), 0));

        if (player.getPosY() + player.getHeight() >= frameHeight) {
            gameOver = true;
        }

        for (int i = 0; i < pipes.size(); i++){
            Pipe pipe = pipes.get(i);
            pipe.setPosX(pipe.getPosX() + pipeVelocityX);

            Rectangle playerRect = new Rectangle(player.getPosX(), player.getPosY(), player.getWidth(), player.getHeight());
            Rectangle pipeRect = new Rectangle(pipe.getPosX(), pipe.getPosY(), pipe.getWidth(), pipe.getHeight());

            if (playerRect.intersects(pipeRect)) {
                gameOver = true;
            }

            if (i % 2 == 0 && !pipe.isPassed() && player.getPosX() > pipe.getPosX() + pipe.getWidth()) {
                pipe.setPassed(true); // Tandai pipa atas
                pipes.get(i + 1).setPassed(true); // Tandai pipa bawah
                score++; // Tambah skor
            }
        }
        if (gameOver) {
            gameLoop.stop(); // Hentikan pergerakan
            pipesCooldown.stop(); // Hentikan pipa baru muncul

            if (view != null) {
                view.showGameOverButtons(true);
            }
        }
    }

    public void restartGame() {
        // Reset semua state ke awal
        player.setPosY(playerStartPosY);
        player.setVelocityY(0);
        pipes.clear();
        score = 0;
        gameOver = false;

        if (view != null) {
            view.showGameOverButtons(false);
        }

        // Mulai ulang timer
        gameLoop.start();
        pipesCooldown.start();
    }

    public void startGame() {
        gameLoop.start();
        pipesCooldown.start();
        isPaused = false;
    }

    public void setGameState(GameState newState) {
        this.gameState = newState;
    }

    @Override
    public void actionPerformed(ActionEvent e){
        if (isPaused) return;

        if (!gameOver) {
            move();
        }

        if (view != null){
            view.repaint();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    public void keyPressed(KeyEvent e) {

        if (isPaused && !gameOver) return;

        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            // Hanya bisa lompat jika game tidak over
            if (!gameOver) {
                player.setVelocityY(-10);
            }
        }
        // --- Wajib 2: Logika Restart ---
        else if (e.getKeyCode() == KeyEvent.VK_R && gameOver) {
            isPaused = false;

            restartGame();
        }
        else if (e.getKeyCode() == KeyEvent.VK_Q) {
            if (view != null) {
                // Temukan JFrame utama yang menampung game ini
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(view);
                if (frame != null) {
                    frame.dispose(); // Tutup jendela game
                }
                // Jalankan ulang aplikasi dari awal (akan memuat App.main dan MenuMenu)
                App.main(null);
            }
        }
    }
    public void keyReleased(KeyEvent e) {}
}
