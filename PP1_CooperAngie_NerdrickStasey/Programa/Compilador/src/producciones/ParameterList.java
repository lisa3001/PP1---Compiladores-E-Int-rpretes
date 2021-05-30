/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package producciones;
import java.util.Vector;
/**
 *
 * @author snerd
 */
public class ParameterList {
    private Vector<Operation> parameterList;
    
    public ParameterList() {parameterList = new Vector<Operation>();}
    
    public void AddOperation(Operation pOperation){parameterList.add(pOperation);}
    
    public Vector<Operation> getParameterList(){ return parameterList;}
    
    public Operation elementAt(int position){ return parameterList.elementAt(position);}
    
    public int size(){ return parameterList.size();}
    
}
