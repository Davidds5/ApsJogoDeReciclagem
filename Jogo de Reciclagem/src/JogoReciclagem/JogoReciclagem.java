package JogoReciclagem;

import GamePanel.GamePanel;

import javax.swing.*;
import java.awt.*;

public class JogoReciclagem extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;

    public JogoReciclagem() {
        setTitle("Jogo da Reciclagem");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Layout principal com CardLayout para alternar telas
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Criação dos painéis
        TelaInicial telaInicial = new TelaInicial(this);
        mainPanel.add(telaInicial, "TelaInicial");

        setContentPane(mainPanel);
        setVisible(true);
    }

    // Método para iniciar o jogo com tempo e dificuldade escolhidos
    public void iniciarJogo(int tempo, int dificuldade) {
        GamePanel gamePanel = new GamePanel(this, tempo, dificuldade);
        mainPanel.add(gamePanel, "GamePanel");
        cardLayout.show(mainPanel, "GamePanel");
        gamePanel.requestFocusInWindow();
    }

    // Voltar para a tela inicial
    public void voltarTelaInicial() {
        cardLayout.show(mainPanel, "TelaInicial");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(JogoReciclagem::new);
    }

    // === TELA INICIAL COM CONFIGURAÇÃO DE TEMPO E DIFICULDADE ===
    public static class TelaInicial extends JPanel {

        public TelaInicial(JogoReciclagem frame) {
            setLayout(null);
            setBackground(new Color(240, 255, 240));

            JLabel titulo = new JLabel("Jogo da Reciclagem", SwingConstants.CENTER);
            titulo.setFont(new Font("Arial", Font.BOLD, 36));
            titulo.setBounds(200, 50, 400, 50);
            add(titulo);

            // Tempo do jogo
            JLabel labelTempo = new JLabel("Tempo do jogo (segundos):");
            labelTempo.setFont(new Font("Arial", Font.PLAIN, 18));
            labelTempo.setBounds(250, 150, 300, 30);
            add(labelTempo);

            JTextField campoTempo = new JTextField("30");
            campoTempo.setBounds(400, 150, 80, 30);
            add(campoTempo);

            // Dificuldade
            JLabel labelDificuldade = new JLabel("Dificuldade:");
            labelDificuldade.setFont(new Font("Arial", Font.PLAIN, 18));
            labelDificuldade.setBounds(250, 200, 200, 30);
            add(labelDificuldade);

            String[] opcoes = {"1 - Fácil", "2 - Médio", "3 - Difícil"};
            JComboBox<String> comboDificuldade = new JComboBox<>(opcoes);
            comboDificuldade.setBounds(350, 200, 150, 30);
            add(comboDificuldade);

            // Botão Iniciar
            JButton btnIniciar = new JButton("Iniciar Jogo");
            btnIniciar.setBounds(300, 300, 200, 50);
            btnIniciar.setFont(new Font("Arial", Font.BOLD, 18));
            btnIniciar.addActionListener(e -> {
                int tempoEscolhido;
                int dificuldadeEscolhida;

                try {
                    tempoEscolhido = Integer.parseInt(campoTempo.getText());
                } catch (NumberFormatException ex) {
                    tempoEscolhido = 30; // default
                }

                dificuldadeEscolhida = comboDificuldade.getSelectedIndex() + 1;

                frame.iniciarJogo(tempoEscolhido, dificuldadeEscolhida);
            });
            add(btnIniciar);
        }
    }
}
