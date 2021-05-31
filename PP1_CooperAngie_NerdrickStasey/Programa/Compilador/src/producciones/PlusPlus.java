package producciones;

/**
 *
 * @author Angie Cooper
 */
public class PlusPlus {
    private Identifier ident;
    private int position[];
    
    //Constructor de la clase
    public PlusPlus(Identifier pIdent, int pPosition[]){
        ident = pIdent;
        position = pPosition;
    }
    
    //GET - Retorna el nombre del identificador
    public Identifier getIdent(){
        return ident;
    }
    
    //GET - Retorna el nombre del
    public int[] getPosition(){
        return position;
    }
}
