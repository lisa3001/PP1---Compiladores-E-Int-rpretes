package producciones;

/**
 *
 * @author Angie Cooper
 */
public class Function {
    private Type type;
    private Identifier ident;
    private ParametersFunction parameterList;
    private Block block;
    private Sentence returnValue; 
    
    public Function(Type pType, Identifier pIdent, ParametersFunction pParameterList, Block pBlock, Sentence pReturn){
        type = pType;
        ident = pIdent;
        parameterList = pParameterList;
        block = pBlock;
        returnValue = pReturn;
    }
    
    public Type getType(){ 
        return type; 
    }
    
    public Identifier getIdentifier(){
        return ident; 
    }
    
    public ParametersFunction getParameterList(){
        return parameterList;
    }
    
    public Block getBlock(){
        return block;
    }
    
    public Sentence getReturnValue(){
        return returnValue;
    }
    
}
