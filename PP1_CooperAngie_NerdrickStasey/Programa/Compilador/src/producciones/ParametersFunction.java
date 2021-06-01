package producciones;

import java.util.Vector;

/**
 *
 * @author Angie Cooper
 */
public class ParametersFunction {
    private Vector<Parameters> parameterList;
    
    public ParametersFunction(){
        parameterList = new Vector<Parameters>();
    }
    
    public void addParameter(Parameters pParameters){
        parameterList.add(pParameters);
    }
    
    public Vector<Parameters> getParameters(){
        return parameterList;
    }
    
     public int size(){ 
         return parameterList.size();
     }
}
