package com.samajackun.sumascript.tokenizer;

import com.samajackun.rodas.sql.tokenizer.Token;

public class SumaTokens
{
	public static final Token KEYWORD_FUNCTION=new Token(SumaTokenTypes.KEYWORD_FUNCTION, "function");

	public static final Token KEYWORD_FUTURE=new Token(SumaTokenTypes.KEYWORD_FUTURE, "future");

	public static final Token KEYWORD_NULL=new Token(SumaTokenTypes.KEYWORD_NULL, "null");

	public static final Token KEYWORD_FOR=new Token(SumaTokenTypes.KEYWORD_FOR, "for");

	public static final Token KEYWORD_WHILE=new Token(SumaTokenTypes.KEYWORD_WHILE, "while");

	public static final Token KEYWORD_DO=new Token(SumaTokenTypes.KEYWORD_DO, "do");

	public static final Token KEYWORD_VAR=new Token(SumaTokenTypes.KEYWORD_VAR, "var");

	public static final Token KEYWORD_IMPORT=new Token(SumaTokenTypes.KEYWORD_IMPORT, "import");

	public static final Token KEYWORD_RETURN=new Token(SumaTokenTypes.KEYWORD_RETURN, "return");

	public static final Token KEYWORD_IF=new Token(SumaTokenTypes.KEYWORD_IF, "if");

	public static final Token KEYWORD_ELSE=new Token(SumaTokenTypes.KEYWORD_ELSE, "else");

	public static final Token KEYWORD_SWITCH=new Token(SumaTokenTypes.KEYWORD_SWITCH, "switch");

	public static final Token KEYWORD_CASE=new Token(SumaTokenTypes.KEYWORD_CASE, "case");

	public static final Token KEYWORD_DEFAULT=new Token(SumaTokenTypes.KEYWORD_DEFAULT, "default");

	public static final Token KEYWORD_BREAK=new Token(SumaTokenTypes.KEYWORD_BREAK, "break");

	public static final Token KEYWORD_THROW=new Token(SumaTokenTypes.KEYWORD_THROW, "throw");

	public static final Token KEYWORD_EXIT=new Token(SumaTokenTypes.KEYWORD_EXIT, "exit");

	public static final Token KEYWORD_UNDEFINED=new Token(SumaTokenTypes.KEYWORD_UNDEFINED, "undefined");

	public static final Token PARENTHESIS_START=new Token(SumaTokenTypes.PARENTHESIS_START, "(");

	public static final Token PARENTHESIS_END=new Token(SumaTokenTypes.PARENTHESIS_END, ")");

	public static final Token BRACKET_START=new Token(SumaTokenTypes.BRACKET_START, "[");

	public static final Token BRACKET_END=new Token(SumaTokenTypes.BRACKET_END, "]");

	public static final Token KEY_START=new Token(SumaTokenTypes.KEY_START, "{");

	public static final Token KEY_END=new Token(SumaTokenTypes.KEY_END, "}");

	public static final Token OPERATOR_AND=new Token(SumaTokenTypes.OPERATOR_AND, "&&");

	public static final Token OPERATOR_AND_BITWISE=new Token(SumaTokenTypes.OPERATOR_AND_BITWISE, "&");

	public static final Token OPERATOR_OR=new Token(SumaTokenTypes.OPERATOR_OR, "||");

	public static final Token OPERATOR_OR_BITWISE=new Token(SumaTokenTypes.OPERATOR_OR_BITWISE, "|");

	public static final Token OPERATOR_NOT=new Token(SumaTokenTypes.OPERATOR_NOT, "!");

	public static final Token OPERATOR_EQUALS=new Token(SumaTokenTypes.OPERATOR_EQUALS, "=");

	public static final Token OPERATOR_LOWER=new Token(SumaTokenTypes.OPERATOR_LOWER, "<");

	public static final Token OPERATOR_GREATER=new Token(SumaTokenTypes.OPERATOR_GREATER, ">");

	public static final Token OPERATOR_LOWER_OR_EQUALS=new Token(SumaTokenTypes.OPERATOR_LOWER_OR_EQUALS, "<=");

	public static final Token OPERATOR_GREATER_OR_EQUALS=new Token(SumaTokenTypes.OPERATOR_GREATER_OR_EQUALS, ">=");

	public static final Token OPERATOR_DISTINCT1=new Token(SumaTokenTypes.OPERATOR_DISTINCT, "!=");

	public static final Token OPERATOR_DISTINCT2=new Token(SumaTokenTypes.OPERATOR_DISTINCT, "<>");

	public static final Token OPERATOR_PLUS=new Token(SumaTokenTypes.OPERATOR_PLUS, "+");

	public static final Token OPERATOR_PLUS_EQUALS=new Token(SumaTokenTypes.OPERATOR_PLUS_EQUALS, "+=");

	public static final Token OPERATOR_PLUS_PLUS=new Token(SumaTokenTypes.OPERATOR_PLUS_PLUS, "++");

	public static final Token OPERATOR_MINUS=new Token(SumaTokenTypes.OPERATOR_MINUS, "-");

	public static final Token OPERATOR_MINUS_EQUALS=new Token(SumaTokenTypes.OPERATOR_MINUS_EQUALS, "-=");

	public static final Token OPERATOR_MINUS_MINUS=new Token(SumaTokenTypes.OPERATOR_MINUS_MINUS, "--");

	public static final Token OPERATOR_ASTERISK_EQUALS=new Token(SumaTokenTypes.OPERATOR_ASTERISK_EQUALS, "*=");

	public static final Token OPERATOR_DIVIDE_EQUALS=new Token(SumaTokenTypes.OPERATOR_DIVIDE_EQUALS, "/=");

	public static final Token OPERATOR_PERCENT=new Token(SumaTokenTypes.OPERATOR_PERCENT, "%");

	public static final Token OPERATOR_TERNARY=new Token(SumaTokenTypes.OPERATOR_TERNARY, "?");

	public static final Token ASTERISK=new Token(SumaTokenTypes.ASTERISK, "*");

	public static final Token OPERATOR_DIV=new Token(SumaTokenTypes.OPERATOR_DIV, "/");

	public static final Token COMMA=new Token(SumaTokenTypes.COMMA, ",");

	public static final Token PERIOD=new Token(SumaTokenTypes.PERIOD, ".");

	public static final Token COLON=new Token(SumaTokenTypes.COLON, ":");

	public static final Token SEMICOLON=new Token(SumaTokenTypes.SEMICOLON, ";");

	public static final Token TRUE=new Token(SumaTokenTypes.TRUE, "true");

	public static final Token FALSE=new Token(SumaTokenTypes.FALSE, "false");

	public static final Token CR=new Token(SumaTokenTypes.NEWLINE, "\r");

	public static final Token LF=new Token(SumaTokenTypes.NEWLINE, "\n");

	public static final Token CRLF=new Token(SumaTokenTypes.NEWLINE, "\r\n");

}
