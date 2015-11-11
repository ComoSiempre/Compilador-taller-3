package scanner;
import java_cup.runtime.*;
import parser.*;

%%
%unicode
%line
%column
%public
%cup

LETRA = [a-zA-Z]
DIGITO = [0-9]
NUMERO = 0 | [1-9][0-9]*
IF = ("i"|"I")("f"|"F")
ELSE = ([eE])([lL])([sS])([eE])
RETURN = ([rR])([eE])([tT])([uU])([rR])([nN])
INT = ([iI])([nN])([tT])
VOID = ([vV])([oO])([iI])([dD])
WHILE = ([wW])([hH])([iI])([lL])([eE])
DO = ([dD])([oO])
SALTO=\n|\r|\r\n /*saltos, que no nos interesa*/
InputCharacter = [^\r\n] /*cualquier cosa excepto /r/n*/
ESPACIOS     = {SALTO} | [ \t\f]
TraditionalComment   = "/*" ~"*/"
EndOfLineComment     = "//" {InputCharacter}* {SALTO}?
COMENT = {TraditionalComment} | {EndOfLineComment}
%{
%}
%%

{SALTO} {/*Ignore*/}
{ESPACIOS} {/*Ignore*/}
{COMENT} {/*ignore*/}
{IF} {return new Symbol(sym.IF,yyline,yycolumn,yytext());}
{INT} {return new Symbol(sym.INT,yyline,yycolumn,yytext());}
{ELSE} {return new Symbol(sym.ELSE,yyline,yycolumn,yytext());}
{RETURN} {return new Symbol(sym.RETURN,yyline,yycolumn,yytext());}
{VOID} {return new Symbol(sym.VOID,yyline,yycolumn,yytext());}
{WHILE} {return new Symbol(sym.WHILE,yyline,yycolumn,yytext());}
{DO} {return new Symbol(sym.DO,yyline,yycolumn,yytext());}
"=" {return new Symbol(sym.IGUAL,yyline,yycolumn,yytext());}
"+" {return new Symbol(sym.SUMA,yyline,yycolumn,yytext());}
"*" {return new Symbol(sym.MULT,yyline,yycolumn,yytext());}
"-" {return new Symbol(sym.RESTA,yyline,yycolumn,yytext());}
"/" {return new Symbol(sym.DIV,yyline,yycolumn,yytext());}
"<" {return new Symbol(sym.MENOR,yyline,yycolumn,yytext());}
"<=" {return new Symbol(sym.MENIGUAL,yyline,yycolumn,yytext());}
">" {return new Symbol(sym.MAYOR,yyline,yycolumn,yytext());}
">=" {return new Symbol(sym.MAYIGUAL,yyline,yycolumn,yytext());}
"==" {return new Symbol(sym.IGUALIGUAL,yyline,yycolumn,yytext());}
"<>" {return new Symbol(sym.DISTINTO,yyline,yycolumn,yytext());}
";" {return new Symbol(sym.PUNTOCOMA,yyline,yycolumn,yytext());}
"," {return new Symbol(sym.COMA,yyline,yycolumn,yytext());}
"(" {return new Symbol(sym.PARENTHLEFT,yyline,yycolumn,yytext());}
")" {return new Symbol(sym.PARENTHRIGHT,yyline,yycolumn,yytext());}
"[" {return new Symbol(sym.BRACKETLEFT,yyline,yycolumn,yytext());}
"]" {return new Symbol(sym.BRACKETRIGHT,yyline,yycolumn,yytext());}
"{" {return new Symbol(sym.BRACERLEFT,yyline,yycolumn,yytext());}
"}" {return new Symbol(sym.BRACERRIGHT,yyline,yycolumn,yytext());}

([A-Z]){LETRA}* {
	/*throw new Exception("Error Lexico: linea "+(yyline)+" columna "+(yycolumn)+ " token: "+yytext());*/
	System.out.println("Error Lexico: linea "+(yyline+1)+" columna "+(yycolumn+1)+ " token: "+yytext());

}
([a-z]){LETRA}*{DIGITO}* {return new Symbol(sym.ID,yyline,yycolumn,yytext());}
("_"){LETRA}+ {return new Symbol(sym.ID,yyline,yycolumn,yytext());}
("_"){LETRA}+{DIGITO} {return new Symbol(sym.ID,yyline,yycolumn,yytext());}
{NUMERO} {return new Symbol(sym.NUM,yyline,yycolumn,yytext());}

"[^]" {
	/*throw new Exception("Error Lexico: linea "+(yyline)+" columna "+(yycolumn)+ " token: "+yytext());*/
	System.out.println("Error Lexico: linea "+(yyline+1)+" columna "+(yycolumn+1)+ " token: "+yytext());
}
. {
	/*throw new Exception("Error Lexico: linea "+(yyline)+" columna "+(yycolumn)+ " token: "+yytext());*/
	System.out.println("Error Lexico: linea "+(yyline+1)+" columna "+(yycolumn+1)+ " token: "+yytext());
}
