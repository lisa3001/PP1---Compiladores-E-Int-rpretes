package producciones;

/**
 *
 * @author Angie Cooper
 */
public class Main {
    private Block block;
    private Sentence returnValue;
    
    public Main(Block pBlock, Sentence pReturn){
        block = pBlock;
        returnValue = pReturn;
    }
    
    public Block getBlock(){
        return block;
    }
    
    public Sentence getReturnValue(){
        return returnValue;
    }
}
