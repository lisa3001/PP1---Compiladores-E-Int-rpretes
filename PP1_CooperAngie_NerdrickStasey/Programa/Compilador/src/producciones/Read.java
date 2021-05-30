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
public class Read extends Sentence {
    private Identifier varName;
    private int position[];
    
    public Read(Identifier pVar, int pos[]){
        varName = pVar;
        position = pos;
    }
    
    public Identifier getVarName(){return varName;}
    
    public int[] getPosition(){return position;}
}
