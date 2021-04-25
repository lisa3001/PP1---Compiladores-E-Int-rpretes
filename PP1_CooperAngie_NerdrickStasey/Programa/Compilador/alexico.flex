package compilador;

import java_cup.runtime.*;
import java.io.Reader;
      
%% //opciones
    
   
%class AnalizadorLexico


%line //Contador de lineas yyline
%column //Contador de columna yycolumn
%cup
   

%{
    //  Guarda el tipo de token 
    private Symbol symbol(int type) {
        return new Symbol(type, yyline, yycolumn);
    }
    
    // Simbolo del token y su valor
    private Symbol symbol(int type, Object value) {
        return new Symbol(type, yyline, yycolumn, value);
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

/* comments */
Comment = {TraditionalComment} | {EndOfLineComment} | {DocumentationComment}

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

  "," {System.out.print(", -> COMMA\n"); return symbol(sym.COMMA);}
  ";" {System.out.print("; -> FINAL\n"); return symbol(sym.FINAL);}
  "=" {System.out.print("= -> EQUAL\n"); return symbol(sym.EQUAL);}

  "{" {System.out.print("{ -> OPEN_KEY\n"); return symbol(sym.OPEN_KEY);}
  "}" {System.out.print("} -> CLOSE_KEY\n"); return symbol(sym.CLOSE_KEY);}
  "[" {System.out.print("[ -> OPEN_BRACKET\n"); return symbol(sym.OPEN_BRACKET);}
  "]" {System.out.print("] -> CLOSE_BRACKET\n"); return symbol(sym.CLOSE_BRACKET);} 
  "(" {System.out.print("( -> OPEN_PAREN\n"); return symbol(sym.OPEN_PAREN);}
  ")" {System.out.print(") -> CLOSE_PAREN\n"); return symbol(sym.CLOSE_PAREN);}

  //  Operadores
  "+" {System.out.print("+ -> PLUS\n"); return symbol(sym.PLUS);}
  "-" {System.out.print("- -> MINUS\n"); return symbol(sym.MINUS);}
  "/" {System.out.print("/ -> DIVIDE\n"); return symbol(sym.DIVIDE);}
  "*" {System.out.print("* -> MULTI\n"); return symbol(sym.MULTI);}
  "%" {System.out.print("% -> MODULE\n"); return symbol(sym.MODULE);}
  "^" {System.out.print("^ -> POWER\n"); return symbol(sym.POWER);}

  //Operadores unarios
  "++" {System.out.print("++ -> PLUS_PLUS\n"); return symbol(sym.PLUS_PLUS);}
  "--" {System.out.print("-- -> MINUS_MINUS\n"); return symbol(sym.MINUS_MINUS);}

  //Operadores relacionales
  ">" {System.out.print("> -> GREATER\n"); return symbol(sym.GREATER);}
  ">=" {System.out.print(">= -> GREATER_EQUAL\n"); return symbol(sym.GREATER_EQUAL);}
  "<" {System.out.print("< -> MINOR\n"); return symbol(sym.MINOR);}
  "<=" {System.out.print("<= -> MINOR_EQUAL\n"); return symbol(sym.MINOR_EQUAL);}
  "==" {System.out.print("== -> EQUAL_EQUAL\n"); return symbol(sym.EQUAL_EQUAL);}
  "!=" {System.out.print("!= -> DIFFERENT\n"); return symbol(sym.DIFFERENT);}

  //Operadores lógicos
  "&" {System.out.print("& -> AND\n"); return symbol(sym.AND);}
  "|" {System.out.print("| -> OR\n"); return symbol(sym.OR);}

  "main" {System.out.print("main -> MAIN\n"); return symbol(sym.MAIN);}
  "not" {System.out.print("not -> NOT\n"); return symbol(sym.NOT);}
  "return" {System.out.print("return -> RETURN\n"); return symbol(sym.RETURN);}
  "read" {System.out.print("read -> READ\n"); return symbol(sym.READ);}
  "print" {System.out.print("print -> PRINT\n"); return symbol(sym.PRINT);}
  "true" {System.out.print("true -> TRUE\n"); return symbol(sym.TRUE);}
  "false" {System.out.print("false -> FALSE\n"); return symbol(sym.FALSE);}
  
  //Tipos
  "int" {System.out.print("int -> INT\n"); return symbol(sym.INT);}
  "float" {System.out.print("float -> FLOAT\n"); return symbol(sym.FLOAT);}
  "bool" {System.out.print("bool -> BOOL\n"); return symbol(sym.BOOL);}
  "char" {System.out.print("char -> CHAR\n"); return symbol(sym.CHAR);}
  "string" {System.out.print("string -> STRING\n"); return symbol(sym.STRING);}
  "null" {System.out.print("null -> NULL\n"); return symbol(sym.NULL);}

  //Condicionales
  "if" {System.out.print("if -> IF\n"); return symbol(sym.IF);}
  "else" {System.out.print("else -> ELSE\n"); return symbol(sym.ELSE);}
  "elif" {System.out.print("elif -> ELIF\n"); return symbol(sym.ELIF);}
  "for" {System.out.print("for -> FOR\n"); return symbol(sym.FOR);}
  "break" {System.out.print("break -> BREAK\n"); return symbol(sym.BREAK);}
 
   
  // YYTEXT DE EXPRESIONES REGULARES

  /* yytext es el token encontrado.
     Al encontrar una expresión regular se imprime su valor (el valor que se obtuvo de
     la cadena yytext) y este valor se convierte al tipo correspondiente.  
  
  */


  {INTEGER}      {   System.out.print(yytext()+ " -> INTEGER\n"); 
                    return symbol(sym.INTEGER, new Integer(yytext())); }
  {DECIMAL}      {   System.out.print(yytext()+ " -> DECIMAL\n"); 
                    return symbol(sym.DECIMAL, new Double(yytext())); }
  {CHARCHAIN}    {   System.out.print(yytext() + " -> CHARCHAIN\n"); 
                    return symbol(sym.CHARCHAIN, yytext()); }
  {CHARACTER}    {   System.out.print(yytext() + " -> CHARACTER\n"); 
                    return symbol(sym.CHARACTER, yytext()); }
  {IDENTIFIER}   {   System.out.print(yytext() + " -> IDENTIFIER\n"); 
                    return symbol(sym.IDENTIFIER, yytext()); }
  {Comment}      { /* ignora el espacio */ } 
  {WHITESPACE}   { /* ignora el espacio */ } 
}

/* Si el token contenido en la entrada no coincide con ninguna regla
    entonces se marca un token ilegal */
[^]              {System.err.print("\nError Léxico -> Caracter ilegal <"+yytext()+"> Línea: "+yyline+" Columna: "+yycolumn+"\n"); }
