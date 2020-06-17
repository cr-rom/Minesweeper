//Autor: Carles Romero Salom
//Enllaç a YouTube: https://youtu.be/rAgY0TK6PM8

/*
El joc de Minesweeper consisteix en trobar les mines i marcar-les amb flags
o simplement no descobrir les caselles on es troben), per descobrir totes les 
demés caselles.
Per tal cosa, aquesta aplicació consta d'un panell de 9x9 caselles on, 10, són
mines.
Per anar descobrint les caselles, clickarem el botó esquerre del mouse; per marcar
amb un flag, podrem usar el click dret i, així, inhabilitar les caselles.
La partida acaba quan es destapen totes les caselles buides o quan es destapa 
una casella amb una mina.
Podem usar les opcions de "Menú" per:
    1. Obrir una partida anteriorment guardada.
    2. Guardar una partida.
    3. Reiniciar el joc sencer, amb noves bombes.
    4. Sortir del joc.
Podem usar l'opció de "Crèdits" per poder veure l'autoria de l'aplicació.
*/

package Minesweeper;

//IMPORTS

import Logic.Board;
import GUI.GUIBoard;
import IOData.*;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class Minesweeper extends JFrame implements MouseListener {

    Board b;
    GUIBoard gb;
    MenuItems mi;
    ObjectBoardOut obo;
    ObjectBoardIn obi;

    private File rutaFile = null;//File per obtenir full path per obrir un arxiu
    private String rutaOpenGame;//String amb full path des d'on obrir un arxiu
    private final String rutaSavedGames = "saved_games";
    //Ruta per defecte on 
    private final String missatgeInici1 = "Benvingut/da a MINESWEEPER!"
            + "\n\n Per descobrir una casella, feu click amb el botó "
            + "esquerre."
            + "\n\n Si sospiteu que hi ha una mina, podeu fer click amb "
            + "el botó dret per posar un 'flag' i 'bloquejar' la casella.\n\n";
    private final String missatgeInici2 = "Hi ha un total de 10 mines."
            + "\n\n El màxim de 'flags' que es poden usar són 10.\n\n";
    private String labelText;//Text del JLabel
    private JLabel marcador;//Marcador dels flags disponibles

    private final Font menuFonts = new Font("sans-serif", Font.PLAIN, 14);

    public static void main(String[] args) {

        Minesweeper ms = new Minesweeper();
        ms.setVisible(true);

    }

    //CONSTRUCTOR
    public Minesweeper() {

        inici();
        setTitle("Minesweeper - Carles Romero Salom");
        gb.addMouseListener(this);

        this.setLayout(new BorderLayout());
        this.setJMenuBar(mi);
        this.getContentPane().add(marcador, BorderLayout.NORTH);
        this.getContentPane().add(gb, BorderLayout.SOUTH);
        this.setSize(gb.getPreferredSize());
        this.pack();
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    private void inici() {

        b = new Board();
        gb = new GUIBoard();
        gb.setTaulerRef(b);//Fem referència al tauler que usa la classe GUIBoard
        mi = new MenuItems();
        labelText = "Flags usats: " + b.getNumFlags() + " de " + b.getmaxFlags()
                + " disponibles.";
        marcador = new JLabel(labelText, gb.getImageIcon(2, 35, 35),
                JLabel.CENTER);
        marcador.setFont(menuFonts);
        marcador.getAlignmentY();
        gb.showMessage(missatgeInici1, "Benvinguda", 5, 75, 75);
        gb.showMessage(missatgeInici2, "Benvinguda", 5, 75, 75);

    }

    //Modifica el text del JLabel quan es posa o lleva un Flag
    private void updateJLabel() {

        marcador.setText("Flags usats: " + b.getNumFlags() + " de " + b.getmaxFlags() + " disponibles.");
    }

    //Mètode que crea un nou joc i reinicia el tauler
    private void setNewGame() {

        this.b = new Board();

        this.gb.setTaulerRef(b);
        this.gb.repaint();
        updateJLabel();

        this.repaint();

    }

    //Obre el gestor d'arxius per guardar una partida, en format .dat
    private void openFolderSaveGame() {

        JFileChooser chooser;
        String choosertitle = "Guardar partida";

        chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File(rutaSavedGames));
        chooser.setDialogTitle(choosertitle);
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                obo = new ObjectBoardOut(chooser.getSelectedFile() + ".dat");
                obo.writeObjectBoard(b);
                obo.close();
            } catch (IOException e) {
                e.getMessage();
            }
            //Indica la carpeta on s'ha guardat la partida
            System.out.println("Partida guardada a: " + chooser.getSelectedFile() + ".dat");
        } else {
            System.out.println("Partida no guardada.");
        }

    }

    //Obre el gestor d'arxius per obrir una partida
    private void openFolderLoadGame() {

        JFileChooser chooser;
        String chooserTitle = "Obrir partida guardada";

        chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File(rutaSavedGames));
        chooser.setDialogTitle(chooserTitle);
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {

            try {
                rutaFile = chooser.getSelectedFile();
                rutaOpenGame = rutaFile.toString();

                obi = new ObjectBoardIn();
                obi.openObjectBoardIn(rutaOpenGame);
                this.b = obi.readObjectBoard();

                obi.close();

                this.gb.setTaulerRef(b);
                this.gb.repaint();
                updateJLabel();

            } catch (IOException e) {
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Minesweeper.class.getName()).log(Level.SEVERE, null, ex);
            }

            //Indica la carpeta des d'on s'ha obert la partida
            System.out.println("Partida oberta de: " + rutaOpenGame);

            this.b.printBoard();

        } else {
            System.out.println("Pardida no seleccionada.");
        }

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent me) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //Costat de la casella, quadrada
        int costat = gb.getCOSTAT();

        //X i Y s'usen per captar les coordenades del punt pitjat
        //sobre el panell
        int x, y;
        //I i J, s'usen per obtenir la "coordenada" del nostre tauler
        int i, j;
        
        x = e.getY();
        y = e.getX();

        //Calcula la coordenada del nostre tauler
        i = (int) Math.floor(x / costat);
        j = (int) Math.floor(y / costat);

        //SI ES PITJA EL BOTÓ ESQUERRE DEL MOUSE
        if (e.getButton() == MouseEvent.BUTTON1) {

            System.out.println("Click DESCOBRIR a: " + i + ", " + j);
            gb.onLeftClick(i, j);

        }

        //SI ES PITJA EL BOTÓ DRET DEL MOUSE
        if (e.getButton() == MouseEvent.BUTTON3) {

            System.out.println("Click FLAG a: " + i + ", " + j);

            gb.onRightClick(i, j);
            updateJLabel();

        }

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent me) {

    }

    //Barra de menú d'opcions

    public class MenuItems extends JMenuBar implements ActionListener {
        
        
        JMenu menu, credits;//2 opcions del JMenuBar: "Menú" i "Crèdits"
        JMenuItem open, save, restart, exit;//Opcions de "Menú"
        JMenuItem veureCredits;//Opció de "Crèdits"
        

        MenuItems() {

            menu = new JMenu("Menú");

            open = new JMenuItem("Obrir");
            open.setFont(menuFonts);
            open.addActionListener(this);

            save = new JMenuItem("Guardar");
            save.setFont(menuFonts);
            save.addActionListener(this);

            restart = new JMenuItem("Reiniciar joc");
            restart.setFont(menuFonts);
            restart.addActionListener(this);

            exit = new JMenuItem("Sortir");
            exit.setFont(menuFonts);
            exit.addActionListener(this);

            menu.add(open);
            menu.add(save);
            menu.add(restart);
            menu.add(exit);

            menu.setFont(menuFonts);
            this.add(menu);
            
            credits = new JMenu ("Crèdits");
            veureCredits = new JMenuItem("Veure");
            veureCredits.setFont(menuFonts);
            veureCredits.addActionListener(this);
            
            
            
            credits.add(veureCredits);
            credits.setFont(menuFonts);
            this.add(credits);
            
            

        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            if (ae.getSource() == open) {
                openFolderLoadGame();
            }
            if (ae.getSource() == save) {
                openFolderSaveGame();
            }
            if (ae.getSource() == restart) {
                setNewGame();

            }
            if (ae.getSource() == exit) {
                System.exit(0);
            }
            
            if (ae.getSource()==veureCredits){
                
                showCredits();
            }

        }
        
        public void showCredits () {
            
            String message1 = "Carles Romero Salom"
                    +"\n\n Programació 2 - Grup 02X02"
                    + "\n Universitat de les Illes Balears\n";
            String message2 ="Crèdits";
            
            JOptionPane.showMessageDialog(null, message1,
                message2, JOptionPane.INFORMATION_MESSAGE, null);
            
        }

    }

}
