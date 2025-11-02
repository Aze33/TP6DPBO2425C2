import javax.swing.*;
import java.awt.*;

public class App {

    public static void main(String[] args){
        JFrame frame = new JFrame("Flappy Bird");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(360,640);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        Logic logika = new Logic(); // instansiasi logic

        View tampilan = new View(logika);

        // begitu pula kebalikannya
        logika.setView(tampilan);

        frame.setLayout(new BorderLayout());
        frame.add(tampilan, BorderLayout.CENTER); // Panel game di tengah


        frame.pack(); // Atur ukuran frame berdasarkan komponen di dalamnya
        frame.setVisible(true);

        MenuMenu menu = new MenuMenu(frame, logika);
        menu.setVisible(true);

        tampilan.requestFocus();

        // frame.add(tampilan);
        // frame.pack();
        // frame.setVisible(true);
    }
}
