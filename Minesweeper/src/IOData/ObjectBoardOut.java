//Autor: Carles Romero Salom
//Enllaç a YouTube: https://youtu.be/rAgY0TK6PM8

/*

*/

package IOData;

import Logic.Board;
import java.io.*;

//Classe que servex per escriure objectes Board d'un arxiu

public class ObjectBoardOut {

ObjectOutputStream opo;
FileOutputStream fos;


        public  ObjectBoardOut (String arxiu) throws IOException{

            fos = new FileOutputStream (arxiu);
            opo = new ObjectOutputStream (fos);
        }
    
        public void writeObjectBoard (Board board) throws IOException{
         
         opo.writeObject(board);
            
        }
    
        
        public void close() throws IOException {
            
            opo.close();
            fos.close();
        }
    
}
