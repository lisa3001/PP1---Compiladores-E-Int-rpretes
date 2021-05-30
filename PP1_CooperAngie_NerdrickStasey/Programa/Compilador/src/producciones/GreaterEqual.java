package producciones;

/**
 *
 * @author Angie Cooper
 */
public class GreaterEqual extends Operation{
    private Operation leftOperation;
    private Operation rightOperation;
    private Variable varible;
    private int position[];
    
    public GreaterEqual(Operation pLeftOperation, Operation pRightOperation, int pPosition[]){
        leftOperation = pLeftOperation;
        rightOperation = pRightOperation;
        position = pPosition;
    }
    
    //GET - Retorna la operación que está del lado izquierdo
    public Operation getLeftOperation(){
        return leftOperation;
    }
    
    //GET - Retorna la operación que está del lado derecho
    public Operation getRightOperation(){
        return rightOperation;
    }
    
    //GET - Retorna un arreglo de la position (fila, columna)
    public int[] getPosition(){
        return position;
    }
    
}