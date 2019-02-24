/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paint2.pkg0;

/**
 *
 * @author ilmar
 */

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
  
        
public class Painel extends JPanel {
    private int x;
    private int y;
    private int altura;
    private int largura;
    private String tipoForma;
    private ArrayList<Forma> formas = new ArrayList();
    private Color[] cores = {Color.BLACK, Color.BLUE, Color.CYAN, 
                             Color.GRAY, Color.GREEN, Color.LIGHT_GRAY,
                             Color.MAGENTA, Color.ORANGE, Color.PINK,
                             Color.RED, Color.WHITE, Color.YELLOW};
    private int idCor;
    private int idTool = 1;
    private int idSelecionado = 0;
    private boolean[] mouseCoords = new boolean[4]; //Leste, Oeste, Norte e Sul
    private Forma formaBackup;
    
    public Painel() {
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                x = e.getX();
                y = e.getY();
                if (idTool == 1)
                    formas.add(new Retangulo(0, 0, 0, 0, null)); //Dummy
                else if (idTool == 2) { 
                    for (int i = formas.size() - 1; i >= 0; i--) {
                        if (regiaoOcupada(formas.get(i), x, y)) {
                            idSelecionado = i;
                            /**Mover a forma selecionada para cima
                            Forma tmp = formas.get(idSelecionado);
                            formas.remove(idSelecionado);
                            formas.add(tmp);
                            idSelecionado = formas.size() - 1;
                            repaint();
                            **/
                            break;
                        }
                    }
                }
                else if (idTool == 3) {                
                    if (x < formas.get(idSelecionado).getX() && x > formas.get(idSelecionado).getX() - 10) {
                        mouseCoords[0] = true;
                    }
                    else if (x > formas.get(idSelecionado).getX() + formas.get(idSelecionado).getLargura()
                             && x < formas.get(idSelecionado).getX() + formas.get(idSelecionado).getLargura() + 10) {
                        mouseCoords[1] = true;
                    }
                   
                    if (y < formas.get(idSelecionado).getY() && y > formas.get(idSelecionado).getY() - 10) {
                        mouseCoords[2] = true;
                    }
                    if (y > formas.get(idSelecionado).getY() + formas.get(idSelecionado).getAltura()
                        && y < formas.get(idSelecionado).getY() + formas.get(idSelecionado).getAltura() + 10) {
                        mouseCoords[3] = true;
                    }
                }
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (idTool == 1) {
                    int tmpX = Math.min(x, e.getX());
                    int tmpY = Math.min(y, e.getY());
                    largura = Math.abs(x - e.getX());
                    altura = Math.abs(y - e.getY());
                    Retangulo formaNova = new Retangulo(
                            tmpX, tmpY, largura, altura, cores[idCor]);
                    /**switch (tipoForma) {
                    case "Circunferência": formaNova = (Circunferência) formaNova;
                    case "Elipse": formaNova = (Elipse) formaNova;
                    case "Triângulo": formaNova = (Triângulo) formaNova;
                    } **/
                    formas.set(formas.size() - 1, formaNova);
                }
                else if (idTool == 2) {
                    formas.get(idSelecionado).setX(
                            formas.get(idSelecionado).getX() + (e.getX() - x));
                    formas.get(idSelecionado).setY(
                            formas.get(idSelecionado).getY() + (e.getY() - y));
                    x = e.getX();
                    y = e.getY();
                }
                else if (idTool == 3) {
                    System.out.println(mouseCoords[0]);
                    System.out.println(mouseCoords[1]);
                    System.out.println(mouseCoords[2]);
                    System.out.println(mouseCoords[3]);
                    System.out.println("====");
                    if (mouseCoords[0] == true && x < formas.get(idSelecionado).getX()) {
                        formas.get(idSelecionado).setX(formas.get(idSelecionado).getX() + (e.getX() - x));
                        formas.get(idSelecionado).setLargura(formas.get(idSelecionado).getLargura() + (x - e.getX()));
                    }
                    else if (mouseCoords[1] == true && x > formas.get(idSelecionado).getX() + formas.get(idSelecionado).getLargura()) {
                        formas.get(idSelecionado).setLargura(formas.get(idSelecionado).getLargura() + (x - e.getX()) * -1);
                    }
                    
                    if (mouseCoords[2] == true && y < formas.get(idSelecionado).getY()) {
                        formas.get(idSelecionado).setY(formas.get(idSelecionado).getY() + (e.getY() - y));
                        formas.get(idSelecionado).setAltura(formas.get(idSelecionado).getAltura() + (y - e.getY()));
                    }
                    else if (mouseCoords[3] == true && y > formas.get(idSelecionado).getY() + formas.get(idSelecionado).getAltura()) {
                        formas.get(idSelecionado).setAltura(formas.get(idSelecionado).getAltura() + (e.getY() - y));
                    }
                    
                    x = e.getX();
                    y = e.getY();
                }
                repaint();
            }
        });
        addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                if (idTool == 1)
                    idSelecionado = formas.size() - 1;
                else if (idTool == 3)
                    mouseCoords = new boolean[4];
            }
        });
    }
    
    public void mudarCor(int i) {
       idCor = i;
    }
    
    public void mudarForma(String f) {
        tipoForma = f;
    }
    
    public void mudarTool(int i) {
        idTool = i;
    }

    public void removerForma() {
        formaBackup = formas.get(idSelecionado);
        formas.remove(idSelecionado);
        idSelecionado --;
        repaint();
    }
    
    public void Desfazer() {
        formas.add(formaBackup);
        repaint();
    }
    
    public boolean regiaoOcupada(Forma f, int x, int y) {
        return f.noLimite(x, y);
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Forma forma : formas) {
            forma.Desenhar(g);
        }
    }
}