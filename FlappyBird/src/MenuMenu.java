
import javax.swing.*;
import javax.swing.border.EmptyBorder; // Diperlukan untuk padding
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream; // Untuk memuat font
import java.io.IOException; // Untuk error font

public class MenuMenu extends JDialog {

    private Logic logic;
    private Font pixelFontBase; // Kita simpan base font di sini

    // Helper method untuk memuat font (SAMA seperti di App.java/View.java)
    private Font loadBaseFont(String fontPath) {
        try (InputStream is = getClass().getResourceAsStream(fontPath)) {
            if (is == null) {
                System.err.println("Font file not found: " + fontPath);
                return new Font("Arial", Font.BOLD, 12);
            }
            return Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (FontFormatException | IOException e) {
            System.err.println("Error loading font: " + e.getMessage());
            return new Font("Arial", Font.BOLD, 12);
        }
    }

    // Helper method untuk styling tombol (agar tidak berulang)
    private void styleButton(JButton button, Font font) {
        button.setFont(font);
        button.setBackground(new Color(255, 165, 0)); // Warna Oranye
        button.setForeground(Color.WHITE); // Teks putih
        button.setFocusPainted(false); // Menghilangkan fokus aneh
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 2), // Garis pinggir
                new EmptyBorder(10, 20, 10, 20) // Padding di dalam tombol
        ));
    }

    public MenuMenu(JFrame parent, Logic logic) {
        super(parent, "Flappy Bird Menu", true);
        this.logic = logic;

        // --- 1. Muat Font Pixel ---
        pixelFontBase = loadBaseFont("/font/PressStart2P-Regular.ttf");
        Font fontJudul = pixelFontBase.deriveFont(24f); // Ukuran font untuk judul
        Font fontTombol = pixelFontBase.deriveFont(14f); // Ukuran font untuk tombol

        // --- 2. Pengaturan Jendela Menu ---
        setSize(340, 220); // Sedikit lebih besar
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(parent);
        setResizable(false);

        // --- 3. Panel Utama (Ganti Warna & Beri Border) ---
        JPanel panelUtama = new JPanel(new BorderLayout(10, 10));
        panelUtama.setBackground(new Color(240, 230, 140)); // Warna krem/Kuning pucat
        // Border tebal 3px
        panelUtama.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        setContentPane(panelUtama);

        // --- 4. Judul (Pakai Font Pixel) ---
        JLabel title = new JLabel("Flappy Bird", SwingConstants.CENTER);
        title.setFont(fontJudul);
        title.setForeground(new Color(255, 140, 0)); // Oranye tua
        title.setBorder(new EmptyBorder(20, 10, 10, 10)); // Beri jarak (padding)
        add(title, BorderLayout.NORTH);

        // --- 5. Panel untuk Tombol ---
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setOpaque(false); // Transparan (ikut warna panel utama)

        // --- 6. Tombol "Start Game" (Pakai Style) ---
        JButton startButton = new JButton("Start Game");
        styleButton(startButton, fontTombol); // Panggil helper style
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logic.setGameState(Logic.GameState.PLAYING);
                logic.resumeGame();
                dispose();
            }
        });
        buttonPanel.add(startButton);

        // --- 7. Tombol "Exit" (Pakai Style) ---
        JButton exitButton = new JButton("Exit");
        styleButton(exitButton, fontTombol); // Panggil helper style
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        buttonPanel.add(exitButton);

        add(buttonPanel, BorderLayout.CENTER);
    }
}