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
public class NullLiteral extends Operation {
    private Object value;
    private int position[];
    
    public NullLiteral(Object pValue, int pPosition[]){
        value = pValue;
        position = pPosition;
    }
    
    public Object getValue(){ return value;}
    
    public int[] getPosition(){ return position;}
}
