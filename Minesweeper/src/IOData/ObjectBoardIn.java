//Autor: Carles Romero Salom
//Enllaç a YouTube: https://youtu.be/rAgY0TK6PM8

/*

*/

package IOData;

import Logic.Board;
import java.io.*;

//Classe que servex per escriure objectes Board d'un arxiu

public class ObjectBoardIn {
    
ObjectInputStream ois;
FileInputStream fis;


        public void openObjectBoardIn (String arxiu) throws IOException{

            fis = new FileInputStream (arxiu);
            ois = new ObjectInputStream (fis);
        }
    
        public Board readObjectBoard () throws IOException, ClassNotFoundException{
         
         return (Board) ois.readObject();
            
        }
    
        
        public void close() throws IOException {
            
            ois.close();
            fis.close();
        }
    
}
