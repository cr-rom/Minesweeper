//Autor: Carles Romero Salom
//Enllaç a YouTube: https://youtu.be/rAgY0TK6PM8

/*
Crea l'estructura de dades que usarem per jugar al Minesweeper.
Genera una matriu de 9x9 cells.
*/

package Logic;

//IMPORTS

import CellStatus.CellStatus;
import java.io.Serializable;

public class Board implements Serializable {

    //ATRIBUTS
    private Cell[][] cells;
    private static int idxMines; //número de mines generades;
    private int x, y;
    private int numFlags;// comptador de flags usats
    private int knownCellsCounter; // comptador de caselles descobertes
    //CONSTANTS
    private static final int[] neighborX = {-1, 0, 1, -1, 1, -1, 0, 1};
    private static final int[] neighborY = {-1, -1, -1, 0, 0, 1, 1, 1};
    private final int maxCaselles = 9;//Núm. de cells per costat
    private final int dimX = maxCaselles;
    private final int dimY = maxCaselles;
    
    //Màx. i min. coordenades TAULER
    private final int max = (maxCaselles - 1), min = 0;
    private final int maxMines = (maxCaselles + 1); // número màxim de mines
    
    //Número de caselles sense mines
    private final int emptyCells = (dimX * dimY) - maxMines;
     

    //Constructor
    public Board() {
        startBoard();
        printBoard();
    }

    private void startBoard() {

        idxMines = 0; //Iniciem l'índex a 0
        knownCellsCounter = 0; // Iniciem el comptador a 0
        numFlags = 0;
        cells = new Cell[dimX][dimY];// Generem matriu de 9x9

        //Creem un tauler de les dimensions indicades
        for (int i = 0; i < dimX; i++) {

            for (int j = 0; j < dimY; j++) {

                cells[i][j] = new Cell();

            }
        }
        //Mentre no s'hagin generat les 10 mines, genera mines aleatòries
        while (!minesReady()) {
            this.plantRandomMine();
        }

    }

    //Imprimeix per la cònsola la disposició de les bombes, número de bombes 
    //properes de cada casella i una petita llegenda
    
    public void printBoard() {

            for (int i = 0; i < dimX; i++) {

            for (int j = 0; j < dimY; j++) {

                if (cells[i][j].getMineBoolean() == true) {
                    System.out.print("X  ");
                } else {
                    System.out.print(cells[i][j].getNumMinesAround() + "  ");
                }

            }
            System.out.println();
        }
        System.out.println("");
        System.out.println("LLEGENDA:");
        System.out.println("  -  X = mina");
        System.out.println("  -  Els números indiquen les mines al voltant");
        System.out.println("_________________________________\n\n");
    }

    //Genera 2 números aleatoris, que seran X i Y del tauler, modifica el valor
    //boolean isMineBoolean de la casella que pertoca i actualitza el valor de
    //numMinesAround de cada una de les caselles de devora la mina
    private void plantRandomMine() {

        x = genRandomNumber();
        y = genRandomNumber();

        //Si la casella dels valors x i y ja té una mina, generarà nous valors
        //aleatoris fins que trobi una casella amb una x i una y sense mina
        while (cells[x][y].getMineBoolean()) {
            x = genRandomNumber();
            y = genRandomNumber();
        }
        //Modifica el valor de la casella i ara té una mina
        this.cells[x][y].setMineBoolean(true);
        //Augmenta el comptador de mines plantades
        idxMines++;
        //
        uptadeNearMines();

    }

    private int genRandomNumber() {

        int randomNum = (int) Math.floor(Math.random() * (max - min + 1) + min);
        return randomNum;

    }

    //GETTERS & SETTERS

    //Retorna la casella de la posició passada per paràmetre
    public Cell getCell(int x, int y) {

        return this.cells[x][y];
    }
    //Retorna el número de Flags posats
    public int getNumFlags() {
        return this.numFlags;
    }
    //Retorna el màxim de Flags que es poden posar, equivalent al número de
    //bombes que hi ha
    public int getmaxFlags() {
        return this.maxMines;
    }

    //Retorna l'estat d'una casella
    public CellStatus getCellStatusBoard(int i, int j) {

        return this.cells[i][j].getCellStatus();
    }
    //Modifica l'estat d'una casella a l'estat passat per paràmetre
    public void setNewCellStatusBoard(int i, int j, CellStatus status) {

        this.cells[i][j].setCellStatus(status);

    }
    //Retorna si una casella es mina o no (True si és mina; False, si no)
    public boolean getCellisMineBoard(int i, int j) {

        return this.cells[i][j].getMineBoolean();
    }
    //Retorna si una casella ha explotat (True si ha explotat; False, si no)
    public boolean getCellisMineExplodeBoard(int i, int j) {

        return this.cells[i][j].getMineExploded();
    }
    //Retorna el número de mines de devora una casella
    public int getNumMinesAroundBoard(int i, int j) {

        return this.cells[i][j].getNumMinesAround();
    }

    //UPDATERS
    //Modifica l'estat de totes les caselles a KNOWN
    public void destapaCaselles() {
        for (int fil = 0; fil < maxCaselles; fil++) {

            for (int col = 0; col < maxCaselles; col++) {

                setNewCellStatusBoard(fil, col, CellStatus.KNOWN);
            }
        }
    }
    
//    Actualitza el valor de numMinesAround de cada una de les caselles al voltant
//    d'una mina
    private void uptadeNearMines() {

        for (int i = 0; i < neighborX.length; i++) {

            int dx = neighborX[i];
            int dy = neighborY[i];
            //Usarem els valors neighX i neighY per calcular les posicions
            //de les caselles al voltant de la casella X, Y determinada
            int neighX = x + dx;
            int neighY = y + dy;
            
            //Calcula si les caselles al voltant de la mina entren dins el nostre
            //tauler o no; si entren en el nostre tauler, actualitza el número de mines
            //de dites caselles
            if (neighX <= max && neighX >= min && neighY <= max && neighY >= min) {
                this.cells[neighX][neighY].incrementMinesAround();
            }
        }

    }

//    Algorisme recursiu. Si se selecciona una casella que no té mines al voltant
//    (numMinesAround == 0), destapa les caselles que no són mines del seu voltant
//    Repeteix el mateix procés si una de les caselles trobades té numMinesAround == 0,
//    fins trobar una casella que no és una mina però sí té mines al seu voltant
    
    public void uptadeNearStatus(int mainX, int mainY) {

        for (int i = 0; i < neighborX.length; i++) {
            //Fiquem dins dx el valor i dels arrays neighborX i neighborY
            int dx = neighborX[i];
            int dy = neighborY[i];
            
            //Usarem els valors neighX i neighY per calcular les posicions
            //de les caselles al voltant de la casella X, Y indicats per paràmetre
            int neighX = mainX + dx;
            int neighY = mainY + dy;
            
            //Calcula si cada casella al voltant de la casella pitjada entren dins 
            //el nostre tauler o no; si entren en el nostre tauler I, A MÉS, la 
            //casella pitjada NO té mines al voltant, destapa les caselles
            // que no són mines del seu voltant, actualitzant el seu estat a KNOWN
            
            if (neighX <= max && neighX >= min && neighY <= max && neighY >= min
                    && this.cells[mainX][mainY].getNumMinesAround() == 0) {
                
                //Verifica que només siguin caselles "tapades i sense flags" i 
                //que no siguin mines
                if (this.cells[neighX][neighY].getCellStatus() == CellStatus.UNKNOWN
                        && !this.cells[neighX][neighY].getMineBoolean()) {
                    
                    //Si es donen les condicions anterior, actualitza l'estat
                    //a KNOWN i actualitza el comptador de caselles destapades
                    this.cells[neighX][neighY].setCellStatus(CellStatus.KNOWN);
                    updateKnownCellsCounter();
                    
                    //Si la casella del voltant de la pitjada no té mines al voltant 
                    //(és a dir, numMinesAround == 0), repeteix el mètode a la nova
                    //casella
                    uptadeNearStatus(neighX, neighY);
                }

            }
            
//            En el cas de destapar vàries caselles de cop i acabar el joc
//            i guanyar, destapa les caselles on hi ha les mines

//            if (emptyCellsReady()) {
//                destapaCaselles();
//
//            }
        }

    }
    
    //UPDATERS
    
    //Actualitza el comptador de caselles obertes. Com les caselles només es poden
    //destapar, i no tapar de nou, update sempre incrementa el comptador
    public void updateKnownCellsCounter() {

        knownCellsCounter++;
    }
    
    //Augmenta el número de flags posats
    public void increaseFlags() {

        this.numFlags++;
    }
    
    //Redueix el número de flags posats
    public void reduceFlags() {
        this.numFlags--;
    }

    //CHECKERS
    
    //Verifica si ja s'ha plantat el número de mines que toca. Retorna true
    //quan el número de mines plantedes == max mines; false, si encara no.
    public boolean minesReady() {

        boolean ready;
        if (idxMines < maxMines) {
            ready = false;
        } else {
            ready = true;
        }
        return ready;
    }

    //Verifica si ja s'han destapat totes les caselles sense mines.
    //Retorna true si s'han destapat totes; false, si encara no.
    public boolean emptyCellsReady() {

        return knownCellsCounter == emptyCells;

    }

}
