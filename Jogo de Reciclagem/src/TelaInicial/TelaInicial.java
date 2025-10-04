package java_aps;
import GamePanel.GamePanel;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class TelaInicial extends JPanel {
    private JFrame frame;

    public TelaInicial(JFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout());
        JLabel titulo = new JLabel("Jogo da Reciclagem", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 30));
        add(titulo, BorderLayout.CENTER);

        JButton btnIniciar = new JButton("Iniciar Jogo");
        btnIniciar.setFont(new Font("Arial", Font.PLAIN, 20));
        btnIniciar.addActionListener(e -> startGame());
        add(btnIniciar, BorderLayout.SOUTH);
    }

    private void startGame() {
        frame.setContentPane(new GamePanel(frame));
        frame.revalidate();
    }
}

