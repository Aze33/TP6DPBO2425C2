import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.io.IOException;
import java.io.InputStream;

public class View  extends JPanel{
    int width = 360;
    int height = 640;

    private Logic logic;
    private Font pixelFontGameOver;
    private Font pixelFontButton;
    private Font pixelFontScore;

    private JButton restartButton;
    private JButton menuButton;

    // Metode untuk memuat base font kustom (lebih aman)
    private Font loadBaseFont(String fontPath) {
        // try-with-resources akan menutup InputStream secara otomatis
        try (InputStream is = getClass().getResourceAsStream(fontPath)) {
            if (is == null) {
                System.err.println("Font file not found: " + fontPath);
                return new Font("Arial", Font.BOLD, 12); // Fallback
            }
            // Buat base font
            return Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (FontFormatException | IOException e) {
            System.err.println("Error loading font: " + e.getMessage());
            return new Font("Arial", Font.BOLD, 12);
        }
    }

    // constructor
    public View(Logic logic){
        this.logic = logic; // memasukkan instance ke atribut

        setLayout(null);

        Font pixelFontBase = loadBaseFont("/font/PressStart2P-Regular.ttf");

        // Derive ukuran yang kita inginkan dari base font
        pixelFontGameOver = pixelFontBase.deriveFont(32f);
        pixelFontButton = pixelFontBase.deriveFont(14f);
        pixelFontScore = pixelFontBase.deriveFont(40f);

        setPreferredSize(new Dimension(width, height));

        // Tombol Restart
        restartButton = new JButton("(R) Restart");
        restartButton.setFont(pixelFontButton);
        restartButton.setFocusPainted(false);
        restartButton.setBackground(Color.ORANGE);
        restartButton.setForeground(Color.WHITE);

        // Tombol Kembali ke Menu
        menuButton = new JButton("(Q) Menu");
        menuButton.setFont(pixelFontButton);
        menuButton.setFocusPainted(false);
        menuButton.setBackground(Color.LIGHT_GRAY);
        menuButton.setForeground(Color.BLACK);

        // --- 4. Atur Posisi Tombol ---
        int btnWidth = 250;
        int btnHeight = 50;
        int horizontalGap = 20;
        int verticalGap = 15;
        int startX = (width - btnWidth) / 2;
        int restartY = height / 2 + 30;
        int menuY = restartY + btnHeight + verticalGap;

        restartButton.setBounds(startX, restartY, btnWidth, btnHeight);
        menuButton.setBounds(startX, menuY, btnWidth, btnHeight);

        // Jika "Restart" diklik, panggil restartGame() di Logic
        restartButton.addActionListener(e -> logic.restartGame());

        // Jika "Menu" diklik, kita tutup game dan jalankan App.main lagi
        menuButton.addActionListener(e -> {
            // Temukan JFrame utama yang menampung game ini
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            // Tutup jendela game
            frame.dispose();
            // Jalankan ulang aplikasi dari awal (akan memuat App.main dan MenuMenu)
            App.main(null);
        });

        // --- 6. Tambahkan ke Panel dan Sembunyikan ---
        add(restartButton);
        add(menuButton);
        showGameOverButtons(false); // Sembunyikan saat game baru dimulai

        // tambahkan fokus dan Key Listener
        // untuk menerima input keyboard
        setFocusable(true);
        addKeyListener(logic);
    }

    public void showGameOverButtons(boolean show) {
        restartButton.setVisible(show);
        menuButton.setVisible(show);
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        Image bg = logic.getBackgroundImage();
        if (bg != null) {
            g.drawImage(bg, 0, 0, getWidth(), getHeight(), null);
        }

        draw(g);

        if (logic.getGameState() == Logic.GameState.GAME_OVER) {
            draw(g);
        }
    }

    public void draw(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        Player player = logic.getPlayer();

        if(player != null){
            g2d.drawImage(player.getImage(), player.getPosX(), player.getPosY(),
                    player.getWidth(), player.getHeight(), null);
        }

        ArrayList<Pipe> pipes = logic.getPipes();
        if (pipes != null){
            for (int i = 0;i < pipes.size(); i++){
                Pipe pipe = pipes.get(i);
                g2d.drawImage(pipe.getImage(), pipe.getPosX(), pipe.getPosY(),
                        pipe.getWidth(), pipe.getHeight(), null);

            }
        }

        String scoreText = String.valueOf(logic.getScore());
        g2d.setFont(pixelFontScore);
        FontMetrics metrics = g2d.getFontMetrics(pixelFontScore);
        int scoreX = (width - metrics.stringWidth(scoreText)) / 2; // Tengah secara horizontal
        int scoreY = 70;

        g2d.setColor(Color.BLACK);
        g2d.drawString(scoreText, scoreX + 2, scoreY + 2); // Geser sedikit

        // Gambar teks utama
        g2d.setColor(Color.WHITE); // Teks putih agar kontras
        g2d.drawString(scoreText, scoreX, scoreY);

        if (logic.isGameOver()) {

            // --- Efek Outline dan Ukuran Font "GAME OVER" ---
            String gameOverText = "GAME OVER";
            g2d.setFont(pixelFontGameOver);
            FontMetrics metricsGameOver = g2d.getFontMetrics(pixelFontGameOver);
            int gameOverX = (width - metricsGameOver.stringWidth(gameOverText)) / 2;
            int gameOverY = height / 2 - 50;

            // Gambar outline hitam (geser sedikit)
            g2d.setColor(Color.BLACK);
            g2d.drawString(gameOverText, gameOverX + 2, gameOverY + 2); // Geser 2 pixel ke kanan bawah
            g2d.drawString(gameOverText, gameOverX - 2, gameOverY - 2); // Geser 2 pixel ke kiri atas
            g2d.drawString(gameOverText, gameOverX + 2, gameOverY - 2); // Geser 2 pixel ke kanan atas
            g2d.drawString(gameOverText, gameOverX - 2, gameOverY + 2); // Geser 2 pixel ke kiri bawah

            // Gambar teks utama
            g2d.setColor(Color.RED);
            g2d.drawString(gameOverText, gameOverX, gameOverY);

        }
    }
}
