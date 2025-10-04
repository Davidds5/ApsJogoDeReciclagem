package java_aps;

import GamePanel.GamePanel;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class JogoReciclagem extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;

    public JogoReciclagem() {
        setTitle("Jogo da Reciclagem");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Usando CardLayout para alternar entre telas facilmente
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Cria os painéis de tela inicial e de jogo
        TelaInicial telaInicial = new TelaInicial(this);
        GamePanel gamePanel = new GamePanel(this);

        mainPanel.add(telaInicial, "TelaInicial");
        mainPanel.add(gamePanel, "GamePanel");

        setContentPane(mainPanel);
        setVisible(true);
    }

    // Método para iniciar o jogo, chamado da TelaInicial
    public void iniciarJogo() {
        cardLayout.show(mainPanel, "GamePanel");
        // Garantir foco no painel do jogo para receber teclado
        ((GamePanel) mainPanel.getComponent(1)).requestFocusInWindow();
    }

    // Método para voltar à tela inicial
    public void voltarTelaInicial() {
        cardLayout.show(mainPanel, "TelaInicial");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new JogoReciclagem());
    }

    // Tela inicial com botão para iniciar o jogo
    public static class TelaInicial extends JPanel {
        public TelaInicial(JogoReciclagem frame) {
            setBackground(Color.WHITE);
            setLayout(null); // Usar layout absoluto para posicionar fácil componentes

            JLabel titulo = new JLabel("Jogo da Reciclagem");
            titulo.setFont(new Font("Arial", Font.BOLD, 36));
            titulo.setBounds(250, 150, 400, 50);
            add(titulo);

            JButton btnIniciar = new JButton("Iniciar Jogo");
            btnIniciar.setBounds(350, 300, 120, 40);
            btnIniciar.addActionListener(e -> frame.iniciarJogo());
            add(btnIniciar);
        }
    }
}

