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
public class FloatLiteral extends Operation {
    private float value;
    private int position[];
    
    public FloatLiteral(float pValue, int pPosition[]){
        value = pValue;
        position = pPosition;
    }
    
    public float getValue(){ return value;}
    
    public int[] getPosition(){ return position;}
}
