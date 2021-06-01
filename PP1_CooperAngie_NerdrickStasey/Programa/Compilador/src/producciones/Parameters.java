package producciones;

/**
 *
 * @author Angie Cooper
 */
public class Parameters {
    private Type type;
    private Identifier ident;
    
    public Parameters(Type pType, Identifier pIdent){
        type = pType;
        ident = pIdent;
    }
    
    public Type getType(){
        return type;
    }
    
    public Identifier getIdentifier(){
        return ident;
    }
    
}
