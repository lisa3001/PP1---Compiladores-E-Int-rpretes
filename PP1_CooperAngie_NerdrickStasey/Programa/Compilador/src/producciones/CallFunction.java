/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package producciones;

/**
 *
 * @author snerd
 */
public class CallFunction extends Operation{
    private String name;
    private ParameterList parameterList;
    private int position[];
    
    public CallFunction(String pName, ParameterList pParameters, int pPosition[]){
        name = pName;
        parameterList = pParameters;
        position = pPosition;
    }
    
    public String getName(){return name;}
    
    public ParameterList getParameterList(){return parameterList;}
    
    public int[] getPosition() {return position;}
}
