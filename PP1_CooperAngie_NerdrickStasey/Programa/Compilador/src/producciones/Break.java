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
public class Break extends Sentence {
    private int position[];
    
        public Break(int pos[]){
        position = pos;
    }
    
    public int[] getPosition(){return position;}
}
