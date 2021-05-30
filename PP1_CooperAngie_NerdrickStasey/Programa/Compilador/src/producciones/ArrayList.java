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
public class ArrayList {
    private Vector<Operation> arrayList;
    
    public ArrayList() {arrayList = new Vector<Operation>();}
    
    public void AddOperation(Operation pOperation){arrayList.add(pOperation);}
    
    public Vector<Operation> getParameterList(){ return arrayList;}
    
    public Operation elementAt(int position){ return arrayList.elementAt(position);}
    
    public int size(){ return arrayList.size();}
}
