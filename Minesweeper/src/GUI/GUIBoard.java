//Autor: Carles Romero Salom
//Enllaç a YouTube: https://youtu.be/rAgY0TK6PM8

/*
JPanel que conté un tauler de joc, creat a partir de la informació obtinguda
de Board.
*/
package GUI;

//IMPORTS

import CellStatus.CellStatus;
import javax.swing.*;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import Logic.Board;
import java.awt.Toolkit;

public class GUIBoard extends JPanel {

    private Board board = null;

    private Color col;
    private BufferedImage[] numbersImg, iconsImg;

    private final int DIMENSIO = 9;// Número de caselles
    private final int MAXIM = 504; // Tamany del panell
    private final int COSTAT = MAXIM / DIMENSIO; //Costat de cada casella (quadrada)
    private Color greenLight = new Color(130, 194, 103); //Color verd clar
    private Color greenDark = new Color(130, 217, 103); // Color verd obscur
    private Color brownLight = new Color(225, 179, 132);//Color marró clar
    private Color brownDark = new Color(195, 166, 111);//Color marró obscur
    private final String numbersPath = "imgNumbers/"; //Ruta a la carpeta dels números
    private final String iconsPath = "imgIcons/";//Ruta a la carpeta de les icones
    private final int numberOfIcons = 6;//Número d'icones
    private final String extPng = ".png";//Extensió de les imatges
    private String labelText;
    private JLabel marcador;
    
    
    //CONSTRUCTOR
    public GUIBoard() {

        numbersImg = new BufferedImage[DIMENSIO + 1];
        iconsImg = new BufferedImage[numberOfIcons];
        try {
            //carreguem les imatges dels números 
            for (int i = 0; i < numbersImg.length; i++) {
                numbersImg[i] = ImageIO.read(new File(numbersPath + i + extPng));
            }
            //carreguem les imatges de les icones usades (flags, bombes...)    
            for (int i = 0; i < numberOfIcons; i++) {
                iconsImg[i] = ImageIO.read(new File(iconsPath + i + extPng));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    
    /*
    Ens servirà per indicar a aquesta classe quin tauler (Board) haurà d'usar
    */
    public void setTaulerRef(Board board) {
        this.board = board;
    }

    @Override
    public void paintComponent(Graphics g) {

        int x, y; // Serviran com a coordenades per pintar els quadres

        y = 0;
        for (int i = 0; i < DIMENSIO; i++) {

            x = 0;
            for (int j = 0; j < DIMENSIO; j++) {

                //Si NO és una mina i Status = KNOWN, pintarà  la casella
                //de color marró (obscur o clar)
                if (!board.getCellisMineBoard(i, j) 
                        && board.getCellStatusBoard(i, j) == CellStatus.KNOWN) {

                    chooseColor(i, j, brownLight, brownDark);
                    int num = board.getNumMinesAroundBoard(i, j);
                    g.setColor(col);
                    g.drawRect(x, y, COSTAT, COSTAT);
                    g.fillRect(x, y, COSTAT, COSTAT);

                    //En cas de que el valor de bombes al voltant sigui 0, 
                    //en lloc d'imprimir el 0, imprimirà la casella de distint
                    //color buida, sense icona de número.
                    //
                    //Si és distint a 0, posara l'icona del número que toca.
                    if (num != 0) {
                        g.drawImage(numbersImg[num], x + 14, y + 14 , 28, 28, null);
                    }

                } //Si ÉS una mina , Status = KNOWN i és la Mina Explotada
                  //pintarà la casella de color vermell i hi posarà una icona de
                  // "BOOM"
                else if (board.getCellisMineBoard(i, j) 
                        && board.getCellStatusBoard(i, j) == CellStatus.KNOWN
                        && board.getCellisMineExplodeBoard(i, j)) {

                    g.setColor(Color.RED);
                    g.drawRect(x, y, COSTAT, COSTAT);
                    g.fillRect(x, y, COSTAT, COSTAT);
                    g.drawImage(iconsImg[1], x + 5, y + 5, 45, 45, null);

                } //Si ÉS una mina , Status = KNOWN i  NO ÉS la Mina Explotada
                  //pintarà la casella de color marró(obscur o clar) i hi posarà
                  //una icona d'una bomba
                else if (board.getCellisMineBoard(i, j) 
                        && board.getCellStatusBoard(i, j) == CellStatus.KNOWN
                        && !board.getCellisMineExplodeBoard(i, j)) {

                    chooseColor(i, j, brownLight, brownDark);

                    g.setColor(col);
                    g.drawRect(x, y, COSTAT, COSTAT);
                    g.fillRect(x, y, COSTAT, COSTAT);
                    g.drawImage(iconsImg[0], x + 14, y + 14, 28, 28, null);

                  //Si s'hi ha posat un flag, deixarà la casella verda i 
                  //hi posarà una icona del flag
                } else if (board.getCellStatusBoard(i, j) == CellStatus.FLAG) {

                    chooseColor(i, j, greenLight, greenDark);
                    g.setColor(col);
                    g.drawRect(x, y, COSTAT, COSTAT);
                    g.fillRect(x, y, COSTAT, COSTAT);
                    g.drawImage(iconsImg[2],  x + 14, y + 14, 28, 28, null);
                } 
                //La resta de casos (quan s'inicia, quan llevem el flag)
                //pintarà la casella de color verd (obscur o clar)
                else {
                    chooseColor(i, j, greenLight, greenDark);
                    g.setColor(col);
                    g.drawRect(x, y, COSTAT, COSTAT);
                    g.fillRect(x, y, COSTAT, COSTAT);
                }

                x += COSTAT;
            }
            y += COSTAT;
        }

    }

    //Mètode que selecciona el color a pintar el rectangle, depenent
    //de la seva posició
    public void chooseColor(int i, int j, Color color1, Color color2) {

        if ((i % 2 == 1 && j % 2 == 1) || (i % 2 == 0 && j % 2 == 0)) {
            col = color1;
        } else {
            col = color2;
        }
    }

    //Retorna el tamany del costat de la casella
    public int getCOSTAT() {
        return this.COSTAT;
    }

    //Retorna el número màxim de caselles
    public int getMaxCells() {
        return this.DIMENSIO;
    }

    //Retorna el tamany del costat del panell de caselles
    public int getMaxSize() {
        return this.MAXIM;
    }

    //Retorna en forma d'icona una imatge de l'array iconsImg
    //amb una altura i amplària passats per paràmetre

    public Icon getImageIcon(int iconNumber, int height, int width) {

        Image img = this.iconsImg[iconNumber].getScaledInstance(height, width, Image.SCALE_DEFAULT);;

        ImageIcon icon = new ImageIcon(img);

        return icon;
    }

    //Accions a realitzar si es pitja el botó esquerre
    public void onLeftClick(int i, int j) {

        //Si la casella està TAPADA...
        if (board.getCellStatusBoard(i, j) == CellStatus.UNKNOWN) {

            //Destapa la casella, posant l'estat a KNOWN
            board.setNewCellStatusBoard(i, j, CellStatus.KNOWN);
            
//            //Augmenta el número de caselles destapades
//            board.updateKnownCellsCounter();

            //Si és una mina
            if (board.getCell(i, j).getMineBoolean()) {
                //Posa la casella a MINA EXPLOTADA
                board.getCell(i, j).setMineExploded(true);

                //Destapa totes les caselles i s'acaba el joc
                board.destapaCaselles();
                repaint();
                Toolkit.getDefaultToolkit().beep();

                //Mostra missatge "Has perdut!"
                showMessage("Has perdut!", "Fi del joc", 3, 45, 45);

            } //Si no és una mina
            else {

                //I encara no s'han destapat totes les caselles buides
                if (!board.emptyCellsReady()) {
                
                    //Augmenta el número de caselles destapades
                    board.updateKnownCellsCounter();

                    //Actualitza les de devora
                    board.uptadeNearStatus(i, j);
                    repaint();

                    //En el cas de destapar vàries caselles de cop i acabar el 
                    //joc i guanyar, destapa les caselles on hi ha les mines
                    if (board.emptyCellsReady()) {
                        board.destapaCaselles();
                        //Toolkit.getDefaultToolkit().beep();
                        showMessage("Has guanyat!", "Fi del joc", 4, 45, 45);
                        repaint();
                    }
                } //Si es destapa la darrera, acaba el joc
                else {
                    //Destapa totes les caselles, acaba el joc i guanya
                    board.destapaCaselles();
                    repaint();
                    //Toolkit.getDefaultToolkit().beep();
                    showMessage("Has guanyat!", "Fi del joc", 4, 45, 45);

                }
            }

        }

    }
    
    //Accions a realitzar si es pitja el botó dret
    public void onRightClick (int i, int j){
        
        
            if (board.getCellStatusBoard(i, j) == CellStatus.UNKNOWN 
                    && board.getNumFlags() < board.getmaxFlags()) {

                board.setNewCellStatusBoard(i, j, CellStatus.FLAG);
                board.increaseFlags();


            } else if (board.getCellStatusBoard(i, j) == CellStatus.UNKNOWN 
                    && board.getNumFlags() >= board.getmaxFlags()) {
                Toolkit.getDefaultToolkit().beep();
                showMessage("Has posat el màxim de flags!", "Flags", 2, 50, 50);


            } else if (board.getCellStatusBoard(i, j) == CellStatus.FLAG) {
                board.setNewCellStatusBoard(i, j, CellStatus.UNKNOWN);
                board.reduceFlags();
                
            }

            repaint();

    }

    //Mostra el missatge informatiu
    //Inclou una icona i dimensió d'aquesta, passat per paràmetre
    
    public void showMessage(String message1, String message2, int iconNumber, int seizeX, int seizeY) {

        JOptionPane.showMessageDialog(null, message1,
                message2, JOptionPane.INFORMATION_MESSAGE, getImageIcon(iconNumber, seizeX, seizeY));

    }

    //Retorna el tamany del panell
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(MAXIM - 11, MAXIM + 1);
    }

}
