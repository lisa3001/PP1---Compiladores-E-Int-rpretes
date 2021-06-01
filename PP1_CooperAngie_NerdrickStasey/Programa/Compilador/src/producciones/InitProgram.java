package producciones;

/**
 *
 * @author Angie Cooper
 */
public class InitProgram {
    private Main main;
    private FunctionList functions;
    
    public InitProgram(Main pMain, FunctionList pFunctions){
        main = pMain;
        functions = pFunctions;
    }
    
    public Main getMain(){
        return main;
    }
    
    public FunctionList getFunctions(){
        return functions;
    }
}
