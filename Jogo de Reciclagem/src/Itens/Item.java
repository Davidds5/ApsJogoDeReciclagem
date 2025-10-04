package java_aps;
import java.awt.*;
import javax.swing.*;

public class Item {
    public int x, y;
    public int largura = 30, altura = 30;
    public boolean reciclavel;
    public Image imagem;

    public Item(String caminhoImagem, boolean reciclavel) {
        this.reciclavel = reciclavel;
        this.imagem = new ImageIcon(getClass().getResource(caminhoImagem)).getImage();
        this.x = (int)(Math.random() * 700);
        this.y = (int)(Math.random() * 500);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, largura, altura);
    }

    public void draw(Graphics g, JPanel painel) {
        g.drawImage(imagem, x, y, largura, altura, painel);
    }
}

