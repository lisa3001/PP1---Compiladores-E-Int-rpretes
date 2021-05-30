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
public class Identifier extends Operation {
    private String name;
    private int position[];
    
    public Identifier(String pName, int pPosition[]){
        name = pName;
        position = pPosition;
    }
    
    public String getName() { return name; }
    
    public int[] getPosition() { return position; }
}
