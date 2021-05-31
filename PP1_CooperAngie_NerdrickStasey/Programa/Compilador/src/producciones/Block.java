package producciones;

/**
 *
 * @author Angie Cooper
 */
public class Block extends Sentence{
    private Sentences sentenceList;
    
    //Constructor de la clase
    public Block(Sentences pSentences){
        sentenceList = pSentences;
    }
    
    public Sentences getSentences(){
        return sentenceList;
    }
}
