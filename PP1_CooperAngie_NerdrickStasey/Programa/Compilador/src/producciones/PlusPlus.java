package producciones;

/**
 *
 * @author Angie Cooper
 */
public class PlusPlus extends Operation{
    private Operation ident;
    private int position[];
    
    //Constructor de la clase
    public PlusPlus(Operation pIdent, int pPosition[]){
        ident = pIdent;
        position = pPosition;
    }
    
    //GET - Retorna el nombre del identificador
    public Operation getIdent(){
        return ident;
    }
    
    //GET - Retorna el nombre del
    public int[] getPosition(){
        return position;
    }
}
