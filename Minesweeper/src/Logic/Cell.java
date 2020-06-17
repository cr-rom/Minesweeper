//Autor: Carles Romero Salom
//Enllaç a YouTube: https://youtu.be/rAgY0TK6PM8

/*
S'encarrega de generar Cells, que usarem per crear l'estructura de dades.
Cada Cell contindrà informació sobre l'estat actual de la casella del tauler.
*/

package Logic;

//IMPORTS

import CellStatus.CellStatus;
import java.io.Serializable;

public class Cell implements Serializable{

    // enum dels 3 estats possibles de la casella
    private CellStatus casellaStatus; 
    private int numMinesAround; // número total de mines devora
    private boolean isMineBoolean; // si la casella és una mina, true
    private boolean mineExploded; //si la casella és la que explota, true

    //Constructor
    public Cell() {
        
        isMineBoolean = false;

        mineExploded = false;

        numMinesAround = 0;

        //Per defecte, les caselles es pintaran "tapades"
        casellaStatus = CellStatus.UNKNOWN;

    }

    public boolean getMineBoolean() {

        return this.isMineBoolean;
    }

    public void setMineBoolean(boolean mineBool) {
        this.isMineBoolean = mineBool;

    }

    public boolean getMineExploded() {

        return this.mineExploded;
    }

    public void setMineExploded(boolean explode) {
        this.mineExploded = explode;

    }

    public int getNumMinesAround() {

        return this.numMinesAround;
    }

    public void incrementMinesAround() {
        this.numMinesAround++;
    }

    public CellStatus getCellStatus() {

        return this.casellaStatus;
    }

    public void setCellStatus(CellStatus status) {

        this.casellaStatus = status;
    }
}
