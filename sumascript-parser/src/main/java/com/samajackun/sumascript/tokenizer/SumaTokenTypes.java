package com.samajackun.sumascript.tokenizer;

import com.samajackun.rodas.sql.tokenizer.SqlTokenTypes;

public final class SumaTokenTypes
{
	public static final String KEYWORD_FUNCTION="suma.keyword_function";

	public static final String KEYWORD_FUTURE="suma.keyword_future";

	public static final String KEYWORD_NULL=SqlTokenTypes.KEYWORD_NULL;

	public static final String KEYWORD_FOR="suma.keyword_for";

	public static final String KEYWORD_WHILE="suma.keyword_while";

	public static final String KEYWORD_DO="suma.keyword_do";

	public static final String KEYWORD_VAR="suma.keyword_var";

	public static final String KEYWORD_IMPORT="suma.keyword_import";

	public static final String KEYWORD_RETURN="suma.keyword_return";

	public static final String KEYWORD_IF="suma.keyword_if";

	public static final String KEYWORD_ELSE="suma.keyword_else";

	public static final String KEYWORD_SWITCH="suma.keyword_switch";

	public static final String KEYWORD_CASE="suma.keyword_case";

	public static final String KEYWORD_DEFAULT="suma.keyword_default";

	public static final String KEYWORD_BREAK="suma.keyword_break";

	public static final String KEYWORD_THROW="suma.keyword_throw";

	public static final String KEYWORD_EXIT="suma.keyword_exit";

	public static final String KEYWORD_UNDEFINED="suma.keyword_undefined";

	public static final String PARENTHESIS_START=SqlTokenTypes.PARENTHESIS_START;

	public static final String PARENTHESIS_END=SqlTokenTypes.PARENTHESIS_END;

	public static final String BRACKET_START="suma.bracket_start";

	public static final String BRACKET_END="suma.bracket_end";

	public static final String KEY_START="suma.key_start";

	public static final String KEY_END="suma.key_end";

	public static final String OPERATOR_EQUALS=SqlTokenTypes.OPERATOR_EQUALS;

	public static final String OPERATOR_LOWER=SqlTokenTypes.OPERATOR_LOWER;

	public static final String OPERATOR_LOWER_OR_EQUALS=SqlTokenTypes.OPERATOR_LOWER_OR_EQUALS;

	public static final String OPERATOR_GREATER=SqlTokenTypes.OPERATOR_GREATER;

	public static final String OPERATOR_GREATER_OR_EQUALS=SqlTokenTypes.OPERATOR_GREATER_OR_EQUALS;

	public static final String OPERATOR_DISTINCT=SqlTokenTypes.OPERATOR_DISTINCT;

	// public static final String OPERATOR_DISTINCT2=SqlTokenTypes.OPERATOR_DISTINCT2;

	public static final String OPERATOR_PLUS=SqlTokenTypes.OPERATOR_PLUS;

	public static final String OPERATOR_PLUS_PLUS="suma.operator_plus_plus";

	public static final String OPERATOR_MINUS=SqlTokenTypes.OPERATOR_MINUS;

	public static final String OPERATOR_PLUS_EQUALS="suma.operator_plus_equals";

	public static final String OPERATOR_MINUS_EQUALS="suma.operator_minus_equals";

	public static final String OPERATOR_ASTERISK_EQUALS="suma.operator_asterisk_equals";

	public static final String OPERATOR_DIVIDE_EQUALS="suma.operator_divide_equals";

	public static final String OPERATOR_MINUS_MINUS="suma.operator_minus_minus";

	public static final String OPERATOR_PERCENT="suma.operator_percent";

	public static final String OPERATOR_AND=SqlTokenTypes.OPERATOR_AND;

	public static final String OPERATOR_AND_BITWISE="suma.operator_and_bitwise";

	public static final String OPERATOR_OR=SqlTokenTypes.OPERATOR_OR;

	public static final String OPERATOR_OR_BITWISE="suma.operator_or_bitwise";

	public static final String OPERATOR_NOT=SqlTokenTypes.OPERATOR_NOT;

	public static final String OPERATOR_TERNARY="suma.operator_ternary";

	public static final String ASTERISK=SqlTokenTypes.ASTERISK;

	public static final String OPERATOR_DIV=SqlTokenTypes.OPERATOR_DIV;

	public static final String COMMA=SqlTokenTypes.COMMA;

	public static final String PERIOD=SqlTokenTypes.PERIOD;

	public static final String COLON="suma.colon";

	public static final String SEMICOLON=SqlTokenTypes.SEMICOLON;

	public static final String DOUBLE_QUOTED_TEXT_LITERAL=SqlTokenTypes.DOUBLE_QUOTED_TEXT_LITERAL;

	public static final String SINGLE_QUOTED_TEXT_LITERAL=SqlTokenTypes.TEXT_LITERAL;

	public static final String IDENTIFIER=SqlTokenTypes.IDENTIFIER;

	public static final String INTEGER_NUMBER_LITERAL=SqlTokenTypes.INTEGER_NUMBER_LITERAL;

	public static final String DECIMAL_NUMBER_LITERAL=SqlTokenTypes.DECIMAL_NUMBER_LITERAL;

	public static final String VARIABLE_REFERENCE=SqlTokenTypes.NAMED_PARAMETER;

	public static final String TRUE=SqlTokenTypes.TRUE;

	public static final String FALSE=SqlTokenTypes.FALSE;

	public static final String WHITESPACE=SqlTokenTypes.WHITESPACE;

	public static final String COMMENT=SqlTokenTypes.COMMENT;

	public static final String LINE_COMMENT="suma.line_comment";

	public static final String NEWLINE="suma.newline";

	private SumaTokenTypes()
	{
	}
}
