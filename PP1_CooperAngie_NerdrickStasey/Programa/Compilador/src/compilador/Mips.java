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
    private String main = "\n.text\n.globl main\n";
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
      int esMain = 0;
      for (String linea: codigo3d){
          if (linea.contains("func begin")){
              String[] instruccion = linea.split(" ");
              String function = isFunction(instruccion);
              if (function.equals("main")){
                  main += function + ":\n";
                  esMain = 1;
              }
              else{
                  funciones += function +":\n";
                  esMain = 0;
              }     
          }
          else if(linea.contains("return") && !linea.contains("=")){
              String[] instruccion = linea.split(" ");
              instruccion[0] = instruccion[0].trim();
              instruccion[1] = instruccion[1].trim();
              String inst = "";
              String registro = obtenerRegistro(instruccion[1]);
              inst += "     move  ";
              inst += "$v1" + ", $" + registro;
              if (esMain == 1)main += inst + "\n";
              else funciones += inst + "\n";
          }
          else if(linea.contains("if_go")){
              String[] instruccion = linea.split(" ");
              String temp = instruccion[1].replace("(", "");
              String cond = temp.replace(")", "");
              String registro = obtenerRegistro(cond);
              String temp2 = instruccion[3].replace("(", "");
              String tack = temp2.replace(")", "");   
              String inst = "";
              inst += "     beq $"+ registro+ ", 1, "+tack;
              if (esMain == 1)main += inst + "\n";
              else funciones += inst + "\n";
          }
          else if(linea.contains("goto")){
              String[] instruccion = linea.split(" ");
              String temp = instruccion[1].replace("(", "");
              String tack = temp.replace(")", "");
              String inst = "";
              inst += "     j "+tack;
              if (esMain == 1)main += inst + "\n";
              else funciones += inst + "\n";
          }
          else if(linea.contains("IF_") || linea.contains("ELIF_") || linea.contains("ELSE_")){
              String inst = "";
              inst += linea;
              if (esMain == 1)main += inst + "\n";
              else funciones += inst + "\n";
          }
          else if (linea.contains("=") ){
              String[] instruccion = linea.split("=");
              instruccion[0] = instruccion[0].trim();
              instruccion[1] = instruccion[1].trim();
              String inst = "";
              String registro = obtenerRegistro(instruccion[0]);
              if (instruccion[1].contains("\"")){
                  inst += "     la  ";
                  data += instruccion[0] + ":   .asciiz " + instruccion[1] + "\n";
                  inst += "$" + registro + ", " + instruccion[0];
              }else{
                  if (instruccion[1].contains("+") && !instruccion[1].contains("++")){
                     String[] operandos = instruccion[1].split("\\+"); 
                     String operando1 = obtenerRegistro(operandos[0]);
                     String operando2 = obtenerRegistro(operandos[1]);
                     inst += "     add ";
                     inst += "$" + registro + ", $" + operando1 + ",$" + operando2;
                  }
                  else if (instruccion[1].contains("-")){
                     instruccion[1] = instruccion[1].trim();
                     String[] operandos = instruccion[1].split("\\-");
                     if (!operandos[0].equals("")){
                        String operando1 = obtenerRegistro(operandos[0]);
                        String operando2 = obtenerRegistro(operandos[1]);
                        inst += "     sub ";
                        inst += "$" + registro + ", $" + operando1 + ",$" + operando2;
                     }else{
                         inst += "     mulo ";
                         inst += "$" + registro + ", $" + operandos[1] + ",-1";
                     }                          
                  }else if (instruccion[1].contains("*") && !instruccion[1].contains("**")){
                     String[] operandos = instruccion[1].split("\\*"); 
                     String operando1 = obtenerRegistro(operandos[0]);
                     String operando2 = obtenerRegistro(operandos[1]);
                     inst += "     mulo ";
                     inst += "$" + registro + ", $" + operando1 + ",$" + operando2;
                  }else if (instruccion[1].contains("/")){
                     String[] operandos = instruccion[1].split("/"); 
                     String operando1 = obtenerRegistro(operandos[0]);
                     String operando2 = obtenerRegistro(operandos[1]);
                     inst += "     div ";
                     inst += "$" + registro + ", $" + operando1 + ",$" + operando2;
                  }else if (instruccion[1].contains("++")){
                     String[] operandos = instruccion[1].split("\\++");
                     String operando1 = obtenerRegistro(operandos[1]);
                     inst += "     add ";
                     inst += "$" + registro + ", $" + operando1 + ", 1";
                  }else if (instruccion[1].contains("--")){
                     String[] operandos = instruccion[1].split("\\--");
                     String operando1 = obtenerRegistro(operandos[1]);
                     inst += "     sub ";
                     inst += "$" + registro + ", $" + operando1 + ", 1";
                   }else if (instruccion[1].contains("&")){
                     String[] operandos = instruccion[1].split("\\&"); 
                     String operando1 = obtenerRegistro(operandos[0]);
                     String operando2 = obtenerRegistro(operandos[1]);
                     inst += "     and ";
                     inst += "$" + registro + ", $" + operando1 + ",$" + operando2;
                   }else if (instruccion[1].contains("|")){
                     String[] operandos = instruccion[1].split("\\|"); 
                     String operando1 = obtenerRegistro(operandos[0]);
                     String operando2 = obtenerRegistro(operandos[1]);
                     inst += "     or ";
                     inst += "$" + registro + ", $" + operando1 + ",$" + operando2;
                   }else if (instruccion[1].contains("~") ){
                     String[] operandos = instruccion[1].split("\\~"); 
                     String operando1 = obtenerRegistro(operandos[0]);
                     String operando2 = obtenerRegistro(operandos[1]);
                     inst += "     rem ";
                     inst += "$" + registro + ", $" + operando1 + ",$" + operando2;
                   }else if (instruccion[1].contains("<") && !instruccion[1].contains("<<") ){
                     String[] operandos = instruccion[1].split("\\<"); 
                     String operando1 = obtenerRegistro(operandos[0]);
                     String operando2 = obtenerRegistro(operandos[1]);
                     inst += "     sltu ";
                     inst += "$" + registro + ", $" + operando1 + ",$" + operando2;
                   }else if (instruccion[1].contains("<<") ){
                     String[] operandos = instruccion[1].split("\\<<"); 
                     String operando1 = obtenerRegistro(operandos[0]);
                     String operando2 = obtenerRegistro(operandos[1]);
                     inst += "     sleu ";
                     inst += "$" + registro + ", $" + operando1 + ",$" + operando2;
                   }else if ( instruccion[1].contains(">") && !instruccion[1].contains(">>") ){
                     String[] operandos = instruccion[1].split("\\>"); 
                     String operando1 = obtenerRegistro(operandos[0]);
                     String operando2 = obtenerRegistro(operandos[1]);
                     inst += "     sgtu ";
                     inst += "$" + registro + ", $" + operando1 + ",$" + operando2;
                   }else if (instruccion[1].contains(">>") ){
                     String[] operandos = instruccion[1].split("\\>>"); 
                     String operando1 = obtenerRegistro(operandos[0]);
                     String operando2 = obtenerRegistro(operandos[1]);
                     inst += "     sgeu ";
                     inst += "$" + registro + ", $" + operando1 + ",$" + operando2;
                   }else if (instruccion[1].contains("@@") ){
                     String[] operandos = instruccion[1].split("\\@@"); 
                     String operando1 = obtenerRegistro(operandos[0]);
                     String operando2 = obtenerRegistro(operandos[1]);
                     inst += "     seq ";
                     inst += "$" + registro + ", $" + operando1 + ",$" + operando2;
                   }else if (instruccion[1].contains("!@") ){
                     String[] operandos = instruccion[1].split("\\!@"); 
                     String operando1 = obtenerRegistro(operandos[0]);
                     String operando2 = obtenerRegistro(operandos[1]);
                     inst += "     sne ";
                     inst += "$" + registro + ", $" + operando1 + ",$" + operando2;
                  }else{
                      String asignacion = obtenerRegistro(instruccion[1]);
                      if (asignacion != ""){
                         inst += "     move  ";
                         inst += "$" + registro + ", $" + asignacion;
                      }else{
                         inst += "     li  ";
                         inst += "$" + registro + ", " + instruccion[1]; 
                      }
                  }
              }
              if (esMain == 1)main += inst + "\n";
              else funciones += inst + "\n"; 
          }
      }
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
    
    public String obtenerRegistro(String etiqueta){
        String resultado = "";
        int contador = etiqueta.lastIndexOf("t");
        if (contador >= 0){
            while(contador<etiqueta.length()){
            
            if (etiqueta.charAt(contador) == '_') contador = etiqueta.length();
            else{
                resultado += etiqueta.charAt(contador);
                contador += 1;
            }
         }  
        }
        return resultado;
    }
    
}
