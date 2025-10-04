package GamePanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GamePanel extends JPanel implements ActionListener, KeyListener {

    private Timer gameTimer;            // Timer para frames do jogo
    private Timer cronometro;           // Timer para contagem regressiva do tempo
    private Timer itemGeneratorTimer;   // Timer para geração de itens

    private final int width = 800;
    private final int height = 600;

    private int playerX, playerY;
    private final int playerSize = 40;

    private List<Item> itens;
    private Random rand;

    private int score = 0;
    private int vidas = 3;
    private int tempo = 30; // segundos

    private boolean esquerda, direita, cima, baixo;

    private JFrame frame;

    public GamePanel(JFrame frame) {
        this.frame = frame;
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.WHITE);
        setFocusable(true);
        addKeyListener(this);

        initGame();
    }

    public void initGame() {
        playerX = width / 2 - playerSize / 2;
        playerY = height - 60;

        itens = new ArrayList<>();
        rand = new Random();

        // Timer do jogo para atualizar frames (~50 FPS)
        gameTimer = new Timer(20, this);
        gameTimer.start();

        // Timer do tempo - decrementa a cada segundo
        cronometro = new Timer(1000, e -> {
            tempo--;
            if (tempo <= 0 || vidas <= 0) {
                endGame();
            }
            repaint();
        });
        cronometro.start();

        // Timer para gerar itens a cada 1,5 segundo
        itemGeneratorTimer = new Timer(1500, e -> gerarItem());
        itemGeneratorTimer.start();
    }

    private void gerarItem() {
        int x = rand.nextInt(width - 20); // Largura do item é 20
        boolean reciclavel = rand.nextBoolean();
        itens.add(new Item(x, 0, reciclavel));
    }

    private void endGame() {
        // Para os timers
        if (gameTimer != null && gameTimer.isRunning()) {
            gameTimer.stop();
        }
        if (cronometro != null && cronometro.isRunning()) {
            cronometro.stop();
        }
        if (itemGeneratorTimer != null && itemGeneratorTimer.isRunning()) {
            itemGeneratorTimer.stop();
        }

        // Substituir o painel do jogo pelo painel de fim de jogo
        EndGamePanel endPanel = new EndGamePanel(score, frame);
        frame.setContentPane(endPanel);
        frame.revalidate();
        endPanel.requestFocusInWindow();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Desenha personagem
        g.setColor(Color.BLUE);
        g.fillRect(playerX, playerY, playerSize, playerSize);

        // Desenha itens
        for (Item item : itens) {
            item.desenhar(g);
        }

        // Desenha pontuação, vidas e tempo
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.drawString("Pontuação: " + score, 10, 25);
        g.drawString("Vidas: " + vidas, 10, 50);
        g.drawString("Tempo: " + tempo, width - 100, 25);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Movimentar personagem
        if (esquerda && playerX > 0) playerX -= 5;
        if (direita && playerX < width - playerSize) playerX += 5;
        if (cima && playerY > 0) playerY -= 5;
        if (baixo && playerY < height - playerSize) playerY += 5;

        // Mover itens
        Iterator<Item> it = itens.iterator();
        while (it.hasNext()) {
            Item item = it.next();
            item.y += 4; // velocidade dos itens

            // Verifica colisão com o jogador
            if (item.getBounds().intersects(new Rectangle(playerX, playerY, playerSize, playerSize))) {
                if (item.reciclavel) {
                    score += 10;
                } else {
                    vidas--;
                }
                it.remove();
            } else if (item.y > height) {
                it.remove(); // remove itens que passaram da tela
            }
        }

        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT -> esquerda = true;
            case KeyEvent.VK_RIGHT -> direita = true;
            case KeyEvent.VK_UP -> cima = true;
            case KeyEvent.VK_DOWN -> baixo = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT -> esquerda = false;
            case KeyEvent.VK_RIGHT -> direita = false;
            case KeyEvent.VK_UP -> cima = false;
            case KeyEvent.VK_DOWN -> baixo = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    // Classe interna Item
    class Item {
        int x, y;
        final int size = 20;
        boolean reciclavel;
        Color cor;

        public Item(int x, int y, boolean reciclavel) {
            this.x = x;
            this.y = y;
            this.reciclavel = reciclavel;
            this.cor = reciclavel ? Color.GREEN : Color.RED;
        }

        public void desenhar(Graphics g) {
            g.setColor(cor);
            g.fillOval(x, y, size, size);
        }

        public Rectangle getBounds() {
            return new Rectangle(x, y, size, size);
        }
    }

    // Painel para mostrar o fim do jogo
    public static class EndGamePanel extends JPanel {
        private int finalScore;
        private JFrame frame;

        public EndGamePanel(int score, JFrame frame) {
            this.finalScore = score;
            this.frame = frame;

            setBackground(Color.WHITE);
            setLayout(null);  // Layout absoluto

            // Label de pontuação final
            javax.swing.JLabel label = new javax.swing.JLabel("Fim de Jogo! Sua pontuação: " + finalScore);
            label.setFont(new Font("Arial", Font.BOLD, 24));
            label.setBounds(200, 150, 400, 50);
            add(label);

            // Botão para fechar o jogo
            JButton sairBtn = new JButton("Sair");
            sairBtn.setBounds(350, 250, 100, 40);
            sairBtn.addActionListener(e -> System.exit(0));
            add(sairBtn);

            // Botão para reiniciar o jogo
            JButton reiniciarBtn = new JButton("Jogar Novamente");
            reiniciarBtn.setBounds(320, 320, 160, 40);
            reiniciarBtn.addActionListener(e -> {
                // Reinicia o jogo
                GamePanel newGame = new GamePanel(frame);
                frame.setContentPane(newGame);
                frame.revalidate();
                newGame.requestFocusInWindow();
            });
            add(reiniciarBtn);
        }
    }

    // Classe main para iniciar o jogo
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Jogo de Reciclagem");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);

            GamePanel gamePanel = new GamePanel(frame);
            frame.setContentPane(gamePanel);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            // Garante que ao fechar a janela o programa encerre
            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    System.exit(0);
                }
            });
        });
    }
}


