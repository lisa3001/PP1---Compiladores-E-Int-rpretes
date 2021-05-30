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
public class BoolLiteral extends Operation {
    private boolean bool;
    private int position[];
    
    public BoolLiteral(boolean pBool, int pPosition[]){
        bool = pBool;
        position = pPosition;
    }
    
    public boolean getValue(){ return bool;}
    
    public int[] getPosition(){ return position;}
}
