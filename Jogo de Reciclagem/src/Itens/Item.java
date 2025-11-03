package Itens;

import javax.swing.*;
import java.awt.*;

public class Item {
    private int x, y;
    private final int largura = 30;
    private final int altura = 30;
    private final boolean reciclavel;
    private final Color cor;

    public Item(int x, int y, boolean reciclavel) {
        this.x = x;
        this.y = y;
        this.reciclavel = reciclavel;
        this.cor = reciclavel ? new Color(46, 204, 113) : new Color(231, 76, 60);
    }

    // Move o item para baixo
    public void moverParaBaixo(int velocidade) {
        y += velocidade;
    }

    // Desenha o item no painel
    public void draw(Graphics g, JPanel painel) {
        g.setColor(cor);
        g.fillOval(x, y, largura, altura);
    }

    // Retorna um retângulo para detecção de colisão
    public Rectangle getBounds() {
        return new Rectangle(x, y, largura, altura);
    }

    public boolean isReciclavel() {
        return reciclavel;
    }

    public int getY() {
        return y;
    }
}
