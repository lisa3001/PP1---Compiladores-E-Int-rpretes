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
public class ForStructure {
    private Sentence var;
    private Operation condition;
    private Operation manageVar;
    
    public ForStructure(Sentence pVar, Operation pCondition, Operation pManageVar){
        var = pVar;
        condition = pCondition;
        manageVar = pManageVar;
    }
    
    public Sentence getVar(){return var;}
    
    public Operation getCondition(){return condition; }
    
    public Operation getManageVar(){return manageVar;}
}
