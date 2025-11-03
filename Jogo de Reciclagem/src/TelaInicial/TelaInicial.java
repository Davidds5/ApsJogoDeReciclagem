package JogoReciclagem;

import javax.swing.*;
import java.awt.*;

public class TelaInicial extends JPanel {

    private final JogoReciclagem frame;
    private final JComboBox<String> dificuldadeBox;
    private final JTextField tempoField;

    public TelaInicial(JogoReciclagem frame) {
        this.frame = frame;
        setLayout(null);
        setBackground(new Color(240, 255, 240));

        JLabel titulo = new JLabel("Jogo da Reciclagem");
        titulo.setFont(new Font("Arial", Font.BOLD, 36));
        titulo.setBounds(220, 50, 400, 50);
        add(titulo);

        JLabel tempoLabel = new JLabel("Tempo (segundos):");
        tempoLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        tempoLabel.setBounds(300, 150, 200, 30);
        add(tempoLabel);

        tempoField = new JTextField("30");
        tempoField.setBounds(450, 150, 80, 30);
        add(tempoField);

        JLabel dificuldadeLabel = new JLabel("Dificuldade:");
        dificuldadeLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        dificuldadeLabel.setBounds(300, 200, 200, 30);
        add(dificuldadeLabel);

        String[] opcoes = {"Fácil", "Médio", "Difícil"};
        dificuldadeBox = new JComboBox<>(opcoes);
        dificuldadeBox.setBounds(450, 200, 100, 30);
        add(dificuldadeBox);

        JButton iniciarBtn = new JButton("Iniciar Jogo");
        iniciarBtn.setBounds(325, 300, 150, 40);
        iniciarBtn.addActionListener(e -> startGame());
        add(iniciarBtn);
    }

    private void startGame() {
        int tempo = 30;
        int dificuldade = 2; // Médio como padrão
        try {
            tempo = Integer.parseInt(tempoField.getText());
        } catch (NumberFormatException ignored) {}

        dificuldade = dificuldadeBox.getSelectedIndex() + 1;

        frame.iniciarJogo(tempo, dificuldade);
    }
}
