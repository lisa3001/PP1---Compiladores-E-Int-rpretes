package compilador;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;

/**
 *
 * @author Angie Cooper
 */
public class Mips {
    private String[] codigo3d;
    private String data = ".data\n";
    private String[] sentences;
    private String main = ".text\n\n .globl main\n";
    private String funciones = "";
    private ArrayList<Object> bloquemain = new ArrayList();
    private ArrayList<Object> bloquefunc = new ArrayList();
    
    
    //Constructor de la clase
    public Mips(String pCodigo3d){
        codigo3d = pCodigo3d.split("\n");
    }
    
    //Entrada: No tiene
    //Salida: No tiene
    //Objetivo: Se encarga de limpiar el archivo en el que se va a escribir el código en 3 direcciones
    public void limpiarArchivo(){
        try {     
            FileWriter fichero = new FileWriter("mips.s", false); 
            BufferedWriter writer = new BufferedWriter (fichero);
            writer.write("");
            writer.close();

          } catch (Exception ew) {
            ew.printStackTrace();
          }
    }
    
    //Entrada: No tiene
    //Salida: No tiene
    //Objetivo: Se encarga de escribir en un archivo de texto el código en 3 direcciones generado
    public void guardarCodigoMips()
    {
      try {    
        limpiarArchivo();
        FileWriter fichero = new FileWriter("mips.s", true); 
        BufferedWriter writer = new BufferedWriter (fichero);
        writer.write(data + main + funciones);
        writer.close();

      } catch (Exception ew) {
        ew.printStackTrace();
      }
    }
    
   //Entrada: No tiene
   //Salida: No tiene
    //Objetivo: Leer el código intermedio y generar el código mips
    public void generarCodigoMips(){
      for (int i = 0; i < codigo3d.length; i++){
          String linea = codigo3d[i];
          String[] instruccion = linea.split(" ");
          String function = isFunction(instruccion);
          if (function != ""){
              if (function.equals("main")){
                  main += function + ":\n";
                  ArrayList<Object> temporal = new ArrayList();
                  temporal.add("main");
                  temporal.add(i);
                  bloquemain.add(temporal); 
                  
              }
              else{
                  //funciones += function +":\n";
                  ArrayList<Object> temporal = new ArrayList();
                  temporal.add(function);
                  temporal.add(i);
                  bloquefunc.add(temporal); 
              }
              
          }
      }
      generarBloque("main");
      generarBloque("funciones");
      main += "     j end\n";
      funciones += "Print:\n" +
"	li $v0, 4\n" +
"     	syscall  \n" +
"	jr $ra\n";
      funciones += "ReadOption:\n" +
"	li $v0, 5\n" +
"	syscall\n" +
"	move $v1, $v0\n" +
"	jr $ra\n";
      funciones += "end:\n      li $v0, 10\n       syscall";
      guardarCodigoMips();
    }
    
    //Entrada: Un array
    //Salida: String con el nombre de la función, si la linea es el inicio de una función
    //Objetivo: Verificar si una línea del código intermedio es el inicio de una función
    public String isFunction(String[] inst){
        String resultado = "";
        if (inst.length >= 3){
          if (inst[0].equals("func") && inst[1].equals("begin")) resultado = inst[2];  
        }
        return resultado;
    }
    
    public void generarBloque(String tipoFuncion){
        if (tipoFuncion == "main"){
            
        }
        else{
            for(int i = 0; i < bloquefunc.size(); i++){
                funciones += ":\n";
                System.out.println(bloquefunc.get(i));
            }
        }
    
    }
    
}
