/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package producciones;

import java.util.Vector;
/**
 *
 * @author Angie Cooper
 */
public class Sentences {
    Vector<Sentence> sentenceList;
    
    //Constructor de la clase
    public Sentences(){
        sentenceList = new Vector<Sentence>();
    }
    
    public void addSentence(Sentence pSentence){
        sentenceList.add(0, pSentence);
    }
    
    public Vector<Sentence> getSentences(){
        return sentenceList;
    }
}
