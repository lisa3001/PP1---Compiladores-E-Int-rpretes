package producciones;

/**
 *
 * @author Angie Cooper
 */
public class MinusMinus extends Operation{
    private Operation ident;
    private int position[];
    
    //Constructor de la clase
    public MinusMinus(Operation pIdent, int pPosition[]){
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
