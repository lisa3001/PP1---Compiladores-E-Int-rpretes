package producciones;

/**
 *
 * @author Angie Cooper
 */
public class Token {
    Object value;
    int row;
    int column;
    
    public Token(Object pValue, int pRow, int pColumn){
        value = pValue;
        row = pRow;
        column = pColumn;
    }
    
    public Object getValue(){
        return value;
    }
    public int getRow(){
        return row;
    }
    
    public int getColumn(){
        return column;
    }
}
