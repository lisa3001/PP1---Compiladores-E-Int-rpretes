package compilador;

import java_cup.runtime.*;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.BufferedWriter ;
      
%% //opciones
    
   
%class AnalizadorLexico


%line //Contador de lineas yyline
%column //Contador de columna yycolumn
%cup

   

%{
    ArrayList<String> identifiers = new ArrayList<String>();

    //  Guarda el tipo de token 
    private Symbol symbol(int type) {
        return new Symbol(type, yyline, yycolumn);
    }
    
    // Simbolo del token y su valor
    private Symbol symbol(int type, Object value) {
        return new Symbol(type, yyline, yycolumn, value);
    }

    private void GuardarToken (String t){
        if (identifiers.size() == 0 || identifiers.indexOf(t) < 0){
            System.out.print(t + " -> IDENTIFIER |Se guardará en la tabla de símbolos con nombre, tipo, valor, ámbito y rol \n");
            identifiers.add(t);
        }else{
            System.out.print(t + " -> IDENTIFIER \n");
        }
    }

    public static void guardarTokenTxt(String contenido, String ruta)
    {
      try {     
        FileWriter fichero = new FileWriter(ruta, true); 
        BufferedWriter writer = new BufferedWriter (fichero);
        writer.write(contenido);
        writer.close();

      } catch (Exception ew) {
        ew.printStackTrace();
      }
    }

%}
   

//------------------Expresiones regulares  
POINT = \.
DIGIT = [0-9]
DIGITLIM = [1-9]
INTEGER = 0 | {DIGITLIM} {DIGIT}*
DECIMAL = (0|{INTEGER}) {POINT} 0* {INTEGER}
LETTERS = [a-zA-Z]
CHARCHAIN = \" [^\"]* \"
CHARACTER = '.'
IDENTIFIER = {LETTERS}({LETTERS}|{INTEGER})*
JUMP = \r|\n|\r\n
WHITESPACE = [ \t]|{JUMP}
InputCharacter = [^\r\n]

//comments
COMMENT = {TraditionalComment} | {EndOfLineComment} | {DocumentationComment}

TraditionalComment   = "/*" [^*] ~"*/" | "/*" "*"+ "/"
// Comment can be the last line of the file, without line terminator.
EndOfLineComment     = "//" {InputCharacter}* {JUMP}?
DocumentationComment = "/**" {CommentContent} "*"+ "/"
CommentContent       = ( [^*] | \*+ [^\*] )*

%% //fin de opciones

// SIMBOLOS DEL PROGRAMA   

//Estado incial del analizador.
<YYINITIAL> {
   
  // En esta sección, para cada símbolo indica que el token declarado en la clase sym fue encontrado. */

  "," {
        System.out.print(", -> COMMA\n"); 
        guardarTokenTxt(", -> COMMA\n", "tokens.txt");
        return symbol(sym.COMMA);}

  ";" {
        System.out.print("; -> FINAL\n"); 
        guardarTokenTxt("; -> FINAL\n", "tokens.txt");
        return symbol(sym.FINAL);}

  "=" {
        System.out.print("= -> EQUAL\n");
        guardarTokenTxt("= -> EQUAL\n", "tokens.txt");
        return symbol(sym.EQUAL);}

  "{" {
        System.out.print("{ -> OPEN_KEY\n"); 
        guardarTokenTxt("{ -> OPEN_KEY\n", "tokens.txt");
        return symbol(sym.OPEN_KEY);}

  "}" {
        System.out.print("} -> CLOSE_KEY\n"); 
        guardarTokenTxt("} -> CLOSE_KEY\n", "tokens.txt");
        return symbol(sym.CLOSE_KEY);}

  "[" {
        System.out.print("[ -> OPEN_BRACKET\n"); 
        guardarTokenTxt("[ -> OPEN_BRACKET\n", "tokens.txt");
        return symbol(sym.OPEN_BRACKET);}

  "]" {
        System.out.print("] -> CLOSE_BRACKET\n");
        guardarTokenTxt("] -> CLOSE_BRACKET\n", "tokens.txt");
        return symbol(sym.CLOSE_BRACKET);} 

  "(" {
        System.out.print("( -> OPEN_PAREN\n");
        guardarTokenTxt("( -> OPEN_PAREN\n", "tokens.txt");
        return symbol(sym.OPEN_PAREN);}

  ")" {
        System.out.print(") -> CLOSE_PAREN\n"); 
        guardarTokenTxt(") -> CLOSE_PAREN\n", "tokens.txt");
        return symbol(sym.CLOSE_PAREN);}

  //  Operadores
  "+" {
        System.out.print("+ -> PLUS\n");
        guardarTokenTxt("+ -> PLUS\n", "tokens.txt");
        return symbol(sym.PLUS);}

  "-" {
        System.out.print("- -> MINUS\n");
        guardarTokenTxt("- -> MINUS\n", "tokens.txt");
        return symbol(sym.MINUS);}

  "/" {
        System.out.print("/ -> DIVIDE\n");
        guardarTokenTxt("/ -> DIVIDE\n", "tokens.txt");
        return symbol(sym.DIVIDE);}
  "*" {
        System.out.print("* -> MULTI\n");
        guardarTokenTxt("* -> MULTI\n", "tokens.txt");
        return symbol(sym.MULTI);}
  "~" {
        System.out.print("~ -> MODULE\n");
        guardarTokenTxt("~ -> MODULE\n", "tokens.txt");
        return symbol(sym.MODULE);}
  "**" {
        System.out.print("** -> POWER\n");
        guardarTokenTxt("** -> POWER\n", "tokens.txt");
        return symbol(sym.POWER);}

  //Operadores unarios
  "++" {
        System.out.print("++ -> PLUS_PLUS\n");
        guardarTokenTxt("++ -> PLUS_PLUS\n", "tokens.txt");
        return symbol(sym.PLUS_PLUS);}

  "--" {
        System.out.print("-- -> MINUS_MINUS\n");
        guardarTokenTxt("-- -> MINUS_MINUS\n", "tokens.txt");
        return symbol(sym.MINUS_MINUS);}

  //Operadores relacionales
  ">" {
        System.out.print("> -> GREATER\n");
        guardarTokenTxt("> -> GREATER\n", "tokens.txt");
        return symbol(sym.GREATER);}
  ">=" {
        System.out.print(">= -> GREATER_EQUAL\n");
        guardarTokenTxt(">= -> GREATER_EQUAL\n", "tokens.txt");
        return symbol(sym.GREATER_EQUAL);}
  "<" {
        System.out.print("< -> MINOR\n");
        guardarTokenTxt("< -> MINOR\n", "tokens.txt");
        return symbol(sym.MINOR);}
  "<=" {
        System.out.print("<= -> MINOR_EQUAL\n");
        guardarTokenTxt("<= -> MINOR_EQUAL\n", "tokens.txt");
        return symbol(sym.MINOR_EQUAL);}
  "==" {
        System.out.print("== -> EQUAL_EQUAL\n");
        guardarTokenTxt("== -> EQUAL_EQUAL\n", "tokens.txt");
        return symbol(sym.EQUAL_EQUAL);}
  "!=" {
        System.out.print("!= -> DIFFERENT\n");
        guardarTokenTxt("!= -> DIFFERENT\n", "tokens.txt");
        return symbol(sym.DIFFERENT);}

  //Operadores lógicos
  "&" {
        System.out.print("& -> AND\n");
        guardarTokenTxt("& -> AND\n", "tokens.txt");
        return symbol(sym.AND);}

  "|" {
        System.out.print("| -> OR\n");
        guardarTokenTxt("| -> OR\n", "tokens.txt");
        return symbol(sym.OR);}

  "main" {
        System.out.print("main -> MAIN\n");
        guardarTokenTxt("main -> MAIN\n", "tokens.txt");
        return symbol(sym.MAIN);}
  "not" {
        System.out.print("not -> NOT\n"); 
        guardarTokenTxt("not -> NOT\n", "tokens.txt");
        return symbol(sym.NOT);}
  "return" {
        System.out.print("return -> RETURN\n"); 
        guardarTokenTxt("return -> RETURN\n", "tokens.txt");
        return symbol(sym.RETURN);}
  "read" {
        System.out.print("read -> READ\n");
        guardarTokenTxt("read -> READ\n", "tokens.txt");
         return symbol(sym.READ);}
  "print" {
        System.out.print("print -> PRINT\n"); 
        guardarTokenTxt("print -> PRINT\n", "tokens.txt");
        return symbol(sym.PRINT);}
  "true" {
        System.out.print("true -> TRUE\n"); 
        guardarTokenTxt("true -> TRUE\n", "tokens.txt");
        return symbol(sym.TRUE);}
  "false" {
        System.out.print("false -> FALSE\n"); 
        guardarTokenTxt("false -> FALSE\n", "tokens.txt");
        return symbol(sym.FALSE);}
  
  //Tipos
  "int" {
        System.out.print("int -> INT\n"); 
        guardarTokenTxt("int -> INT\n", "tokens.txt");
        return symbol(sym.INT);}
  "float" {
        System.out.print("float -> FLOAT\n");
        guardarTokenTxt("float -> FLOAT\n", "tokens.txt");
        return symbol(sym.FLOAT);}
  "bool" {
        System.out.print("bool -> BOOL\n"); 
        guardarTokenTxt("bool -> BOOL\n", "tokens.txt");
        return symbol(sym.BOOL);}
  "char" {
        System.out.print("char -> CHAR\n"); 
        guardarTokenTxt("char -> CHAR\n", "tokens.txt");
        return symbol(sym.CHAR);}
  "string" {
        System.out.print("string -> STRING\n"); 
        guardarTokenTxt("string -> STRING\n", "tokens.txt");
        return symbol(sym.STRING);}
  "null" {
        System.out.print("null -> NULL\n"); 
        guardarTokenTxt("null -> NULL\n", "tokens.txt");
        return symbol(sym.NULL);}

  //Condicionales
  "if" {
        System.out.print("if -> IF\n"); 
        guardarTokenTxt("if -> IF\n", "tokens.txt");
        return symbol(sym.IF);}
  "else" {
        System.out.print("else -> ELSE\n");
        guardarTokenTxt("else -> ELSE\n", "tokens.txt");
        return symbol(sym.ELSE);}
  "elif" {
        System.out.print("elif -> ELIF\n");
        guardarTokenTxt("elif -> ELIF\n", "tokens.txt");
        return symbol(sym.ELIF);}
  "for" {
        System.out.print("for -> FOR\n");
        guardarTokenTxt("for -> FOR\n", "tokens.txt");
        return symbol(sym.FOR);}
  "break" {
        System.out.print("break -> BREAK\n");
        guardarTokenTxt("break -> BREAK\n", "tokens.txt");
        return symbol(sym.BREAK);}
 
   
  // YYTEXT DE EXPRESIONES REGULARES

  /* yytext es el token encontrado.
     Al encontrar una expresión regular se imprime su valor (el valor que se obtuvo de
     la cadena yytext) y este valor se convierte al tipo correspondiente.  
  
  */

  {INTEGER}      {  System.out.print(yytext()+ " -> INTEGER\n"); 
                    guardarTokenTxt(yytext()+ " -> INTEGER\n", "tokens.txt");
                    return symbol(sym.INTEGER, new Integer(yytext())); }


  {DECIMAL}      {   System.out.print(yytext()+ " -> DECIMAL\n"); 
                    guardarTokenTxt(yytext()+ " -> DECIMAL\n", "tokens.txt");
                    return symbol(sym.DECIMAL, new Double(yytext())); }


  {CHARCHAIN}    {   System.out.print(yytext() + " -> CHARCHAIN\n"); 
                    guardarTokenTxt(yytext()+ " -> CHARCHAIN\n", "tokens.txt");
                    return symbol(sym.CHARCHAIN, yytext()); }

  {CHARACTER}    {   System.out.print(yytext() + " -> CHARACTER\n");
                    guardarTokenTxt(yytext()+ " -> CHARACTER\n", "tokens.txt"); 
                    return symbol(sym.CHARACTER, yytext()); }


  {IDENTIFIER}   {   GuardarToken(yytext()); 
                    guardarTokenTxt(yytext()+ " -> IDENTIFIER\n", "tokens.txt");
                    return symbol(sym.IDENTIFIER, yytext()); }


  {COMMENT}      { /* ignora el espacio */ } 
  {WHITESPACE}   { /* ignora el espacio */ } 
}

//Si el token contenido en la entrada no coincide con ninguna regla entonces se marca un token ilegal
[^]              {System.err.print("\nError Léxico -> Caracter ilegal <"+yytext()+"> Línea: "+yyline+" Columna: "+yycolumn+"\n"); }
