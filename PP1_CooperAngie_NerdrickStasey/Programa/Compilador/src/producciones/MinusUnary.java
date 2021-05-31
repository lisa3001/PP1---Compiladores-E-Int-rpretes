package producciones;

/**
 *
 * @author Angie Cooper
 */
public class MinusUnary {
    private Identifier ident;
    private int position[];
    
    //Constructor de la clase
    public MinusUnary(Identifier pIdent, int pPosition[]){
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
