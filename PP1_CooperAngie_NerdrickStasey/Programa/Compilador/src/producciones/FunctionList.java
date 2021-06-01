package producciones;

import java.util.Vector;

/**
 *
 * @author Angie Cooper
 */
public class FunctionList {
    Vector<Function> functionList;
    
    //Constructor de la clase
    public FunctionList(){
        functionList = new Vector<Function>();
    }
    
    public void addFunction(Function pFunction){
        functionList.add(0, pFunction);
    }
    
    public Vector<Function> getFunctions(){
        return functionList;
    }
}
