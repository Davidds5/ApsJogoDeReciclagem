package GamePanel;

import Itens.Item;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Painel principal do Jogo de Reciclagem.
 * Controla jogador, itens, pontuação, vidas, tempo e dificuldade.
 */
public class GamePanel extends JPanel implements ActionListener, KeyListener {

    private final int width = 800;
    private final int height = 600;

    private int playerX, playerY;
    private final int playerSize = 40;

    private int score = 0;
    private int vidas = 3;
    private int tempo;

    private boolean esquerda, direita, cima, baixo;

    private List<Item> itens;
    private final Random rand = new Random();

    private int velocidadeBase;
    private int frequenciaItens;

    private Timer gameTimer;
    private Timer cronometro;
    private Timer itemGeneratorTimer;

    private final JFrame frame;

    public GamePanel(JFrame frame, int tempoInicial, int dificuldade) {
        this.frame = frame;
        this.tempo = tempoInicial;

        // Configura velocidade e frequência dos itens de acordo com a dificuldade
        switch (dificuldade) {
            case 1 -> { velocidadeBase = 3; frequenciaItens = 2000; }
            case 2 -> { velocidadeBase = 5; frequenciaItens = 1500; }
            case 3 -> { velocidadeBase = 7; frequenciaItens = 1000; }
            default -> { velocidadeBase = 5; frequenciaItens = 1500; }
        }

        setPreferredSize(new Dimension(width, height));
        setBackground(new Color(240, 255, 240)); // fundo verde-claro
        setFocusable(true);
        addKeyListener(this);

        iniciarJogo();
    }

    private void iniciarJogo() {
        playerX = width / 2 - playerSize / 2;
        playerY = height - 60;

        itens = new ArrayList<>();

        // Timer do loop principal (~50 FPS)
        gameTimer = new Timer(20, this);
        gameTimer.start();

        // Timer do cronômetro (1 segundo)
        cronometro = new Timer(1000, e -> {
            tempo--;
            if (tempo <= 0 || vidas <= 0) encerrarJogo();
            repaint();
        });
        cronometro.start();

        // Timer para gerar itens
        itemGeneratorTimer = new Timer(frequenciaItens, e -> gerarItem());
        itemGeneratorTimer.start();
    }

    private void gerarItem() {
        int x = rand.nextInt(width - 30);
        boolean reciclavel = rand.nextBoolean();
        itens.add(new Item(x, 0, reciclavel));
    }

    private void encerrarJogo() {
        pararTimers();
        EndGamePanel endPanel = new EndGamePanel(score, frame);
        frame.setContentPane(endPanel);
        frame.revalidate();
        endPanel.requestFocusInWindow();
    }

    private void pararTimers() {
        if (gameTimer != null) gameTimer.stop();
        if (cronometro != null) cronometro.stop();
        if (itemGeneratorTimer != null) itemGeneratorTimer.stop();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        moverJogador();
        atualizarItens();
        repaint();
    }

    private void moverJogador() {
        int velocidadeAtual = velocidadeBase + (3 - vidas); // aumenta velocidade a cada vida perdida

        if (esquerda && playerX > 0) playerX -= velocidadeAtual;
        if (direita && playerX < width - playerSize) playerX += velocidadeAtual;
        if (cima && playerY > 0) playerY -= velocidadeAtual;
        if (baixo && playerY < height - playerSize) playerY += velocidadeAtual;
    }

    private void atualizarItens() {
        Iterator<Item> it = itens.iterator();
        Rectangle jogador = new Rectangle(playerX, playerY, playerSize, playerSize);

        while (it.hasNext()) {
            Item item = it.next();
            int velocidadeAtual = velocidadeBase + (3 - vidas);
            item.moverParaBaixo(velocidadeAtual);

            if (item.getBounds().intersects(jogador)) {
                if (item.isReciclavel()) score += 10;
                else vidas--;
                it.remove();
            } else if (item.getY() > height) it.remove();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Fundo
        g.setColor(getBackground());
        g.fillRect(0, 0, width, height);

        // Jogador
        g.setColor(Color.BLUE);
        g.fillRect(playerX, playerY, playerSize, playerSize);

        // Itens
        for (Item item : itens) item.draw(g, this);

        // HUD
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 18));
        int padding = 10;

        g.drawString("Pontuação: " + score, padding, 25);
        g.drawString("Vidas: " + vidas, padding, 50);

        String tempoTexto = "Tempo: " + tempo;
        int textoLargura = g.getFontMetrics().stringWidth(tempoTexto);
        g.drawString(tempoTexto, width - textoLargura - padding, 25);
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

    // === Painel de fim de jogo ===
    public static class EndGamePanel extends JPanel {
        public EndGamePanel(int score, JFrame frame) {
            setBackground(Color.WHITE);
            setLayout(null);

            JLabel label = new JLabel("Fim de Jogo! Sua pontuação: " + score, SwingConstants.CENTER);
            label.setFont(new Font("Arial", Font.BOLD, 24));
            label.setBounds(150, 150, 500, 50);
            add(label);

            // Carro do lixo como efeito visual
            JLabel carro = new JLabel("🚛");
            carro.setBounds(-100, 300, 100, 50);
            add(carro);

            JButton btnReiniciar = new JButton("Jogar Novamente");
            btnReiniciar.setBounds(300, 250, 200, 40);
            btnReiniciar.addActionListener(e -> {
                GamePanel novoJogo = new GamePanel(frame, 30, 2);
                frame.setContentPane(novoJogo);
                frame.revalidate();
                novoJogo.requestFocusInWindow();
            });
            add(btnReiniciar);

            JButton btnSair = new JButton("Sair");
            btnSair.setBounds(350, 320, 100, 40);
            btnSair.addActionListener(e -> System.exit(0));
            add(btnSair);
        }
    }
}
