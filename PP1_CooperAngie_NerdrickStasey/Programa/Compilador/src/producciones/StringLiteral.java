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
public class StringLiteral extends Operation {
    private String value;
    private int position[];
    
    public StringLiteral(String pValue, int pPosition[]){
        value = pValue;
        position = pPosition;
    }
    
    public String getValue(){ return value;}
    
    public int[] getPosition(){ return position;}
}
