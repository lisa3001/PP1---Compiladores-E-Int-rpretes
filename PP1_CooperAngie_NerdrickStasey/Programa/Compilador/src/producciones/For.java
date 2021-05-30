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
public class For extends Sentence{
    private ForStructure struct;
    private Vector<Sentence> sentences;
    private int position[];
    
    public For(ForStructure pStruct, Vector<Sentence> pSentences, int pos[]){
        struct = pStruct;
        sentences = pSentences;
        position = pos;
    }
    
    public ForStructure getStructure(){return struct;}
    
    public Vector<Sentence> getSentences(){return sentences;}
    
    public int[] getPosition(){return position;}
    
}
