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
public class CreateArray extends Sentence {
    private Type type;
    private Identifier name;
    private int length;
    private ArrayList arrayList;
    private int position[];
    
    public CreateArray(Type pType, Identifier pName,int pLength ,ArrayList pArrayList, int pPosition[]){
        type = pType;
        name = pName;
        length = pLength;
        arrayList = pArrayList;
        position = pPosition;
    }
    
    public Type getType(){return type;}
    
    public Identifier getIdentifier(){return name;}
    
    public int getLength(){return length;}
    
    public ArrayList getArrayList(){return arrayList;}
    
    public int[] getPosition(){return position;}
}
