package com.samajackun.sumascript.tokenizer;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import com.samajackun.rodas.parsing.source.PushBackSource;
import com.samajackun.rodas.parsing.tokenizer.IllegalDoubleEException;
import com.samajackun.rodas.parsing.tokenizer.IllegalDoublePeriodException;
import com.samajackun.rodas.parsing.tokenizer.IllegalPeriodInExponentException;
import com.samajackun.rodas.parsing.tokenizer.TokenizerException;
import com.samajackun.rodas.parsing.tokenizer.UnclosedCommentException;
import com.samajackun.rodas.parsing.tokenizer.UnclosedTextLiteralException;
import com.samajackun.rodas.parsing.tokenizer.UnexpectedSymbolException;
import com.samajackun.rodas.sql.tokenizer.SqlTokenizer;
import com.samajackun.rodas.sql.tokenizer.Token;
import com.samajackun.rodas.sql.tokenizer.TokenizerSettings;

public class SumaTokenizer extends SqlTokenizer
{
	private static final Map<String, String> OPERATORS=createOperatorsMap();

	private static final Map<String, String> KEYWORDS=createKeywordsMap();

	public SumaTokenizer(PushBackSource source)
		throws TokenizerException,
		IOException
	{
		this(source, new TokenizerSettings());
	}

	public SumaTokenizer(PushBackSource source, TokenizerSettings settings)
		throws TokenizerException,
		IOException
	{
		super(source, settings);
	}

	private static Map<String, String> createKeywordsMap()
	{
		Map<String, String> map=Arrays.asList(
		// @formatter:off
			SumaTokens.KEYWORD_FUNCTION,
			SumaTokens.KEYWORD_ECHO_OUT,
			SumaTokens.KEYWORD_ECHO_ERR,
			SumaTokens.KEYWORD_FUTURE,
			SumaTokens.KEYWORD_NULL,
			SumaTokens.KEYWORD_FOR,
			SumaTokens.KEYWORD_WHILE,
			SumaTokens.KEYWORD_DO,
			SumaTokens.KEYWORD_IMPORT,
			SumaTokens.KEYWORD_RETURN,
			SumaTokens.KEYWORD_IF,
			SumaTokens.KEYWORD_ELSE,
			SumaTokens.KEYWORD_SWITCH,
			SumaTokens.KEYWORD_CASE,
			SumaTokens.KEYWORD_DEFAULT,
			SumaTokens.KEYWORD_BREAK,
			SumaTokens.KEYWORD_THROW,
			SumaTokens.KEYWORD_EXIT,
			SumaTokens.KEYWORD_UNDEFINED,
			SumaTokens.KEYWORD_VAR,
			SumaTokens.TRUE,
			SumaTokens.FALSE
			// @formatter:on
		).stream().collect(Collectors.toMap(t -> t.getValue(), t -> t.getType()));
		return map;
	}

	private static Map<String, String> createOperatorsMap()
	{
		Map<String, String> map=Arrays.asList(
		// @formatter:off
			SumaTokens.OPERATOR_AND,
			SumaTokens.OPERATOR_OR,
			SumaTokens.OPERATOR_EQUALS,
			SumaTokens.OPERATOR_NEW,
			SumaTokens.OPERATOR_PLUS,
			SumaTokens.OPERATOR_PLUS_PLUS,
			SumaTokens.OPERATOR_MINUS,
			SumaTokens.OPERATOR_MINUS_MINUS,
			SumaTokens.OPERATOR_PERCENT,
			SumaTokens.OPERATOR_TERNARY
			// @formatter:on
		).stream().collect(Collectors.toMap(t -> t.getValue(), t -> t.getType()));
		// forEach(t -> map.put(t.getValue(), t.getType()));
		return map;
	}

	private enum State {
		INITIAL, READING_LETTERS, READING_INTEGER_DIGITS, READING_DECIMAL_DIGITS, READING_EXPONENT_DIGITS, READING_EXPONENT_DIGITS_WITHOUT_SIGN, READING_COMMENT, READING_LINE_COMMENT, READING_TEXT_LITERAL, READING_DOUBLE_QUOTED_TEXT_LITERAL, READING_DECIMAL_AND_EXPONENT_DIGITS, READING_DECIMAL_AND_EXPONENT_DIGITS_WITHOUT_SIGN, READING_VARIABLE_REFERENCE
	};

	@Override
	protected Token fetch(PushBackSource source)
		throws TokenizerException,
		IOException
	{
		State state=State.INITIAL;
		Token token=null;
		// source.startRecord();
		StringBuilder trailingText=new StringBuilder(80);

		char c=0;
		while (token == null && source.hasMoreChars())
		{
			c=(char)source.nextChar();
			char c2=(char)source.lookahead();
			switch (state)
			{
				case INITIAL:
					if (Character.isWhitespace(c))
					{
						switch (this.getSettings().getWhitespaceBehaviour())
						{
							case IGNORE:
								break;
							case PRODUCE_TOKENS:
								token=new Token(SumaTokenTypes.WHITESPACE, trailingText.toString() + c);
								break;
							case INCLUDE_IN_FOLLOWING_TOKEN:
								trailingText.append(c);
								break;
						}
					}
					else if (Character.isLetter(c) || c == '_')
					{
						source.unget(c);
						source.startRecord();
						state=State.READING_LETTERS;
					}
					else if (Character.isDigit(c))
					{
						source.unget(c);
						source.startRecord();
						state=State.READING_INTEGER_DIGITS;
					}
					else if (c == '*')
					{
						if (c2 == '=')
						{
							source.nextChar();
							token=createNewTokenIfNecessary(trailingText, SumaTokens.OPERATOR_ASTERISK_EQUALS);
						}
						else
						{
							token=createNewTokenIfNecessary(trailingText, SumaTokens.ASTERISK);
						}
					}
					else if (c == '/')
					{
						switch (c2)
						{
							case '*':
								source.unget(c);
								source.startRecord();
								source.nextChar();
								source.nextChar();
								state=State.READING_COMMENT;
								break;
							case '/':
								source.unget(c);
								source.startRecord();
								source.nextChar();
								source.nextChar();
								state=State.READING_LINE_COMMENT;
								break;
							case '=':
								source.nextChar();
								token=createNewTokenIfNecessary(trailingText, SumaTokens.OPERATOR_DIVIDE_EQUALS);
								break;
							default:
								token=createNewTokenIfNecessary(trailingText, SumaTokens.OPERATOR_DIV);
								break;
						}
					}
					else if (c == '\'')
					{
						source.startRecord();
						state=State.READING_TEXT_LITERAL;
					}
					else if (c == '\"')
					{
						source.startRecord();
						state=State.READING_DOUBLE_QUOTED_TEXT_LITERAL;
					}
					else if (c == '$')
					{
						if (c2 == '{')
						{
							source.nextChar();
							source.startRecord();
							state=State.READING_VARIABLE_REFERENCE;
						}
						else
						{
							source.unget(c);
							source.startRecord();
							state=State.READING_LETTERS;
						}
					}
					else if (c == '.')
					{
						token=createNewTokenIfNecessary(trailingText, SumaTokens.PERIOD);
					}
					else if (c == ',')
					{
						token=createNewTokenIfNecessary(trailingText, SumaTokens.COMMA);
					}
					else if (c == ';')
					{
						token=createNewTokenIfNecessary(trailingText, SumaTokens.SEMICOLON);
					}
					else if (c == '?')
					{
						token=createNewTokenIfNecessary(trailingText, SumaTokens.OPERATOR_TERNARY);
					}
					else if (c == ':')
					{
						token=createNewTokenIfNecessary(trailingText, SumaTokens.COLON);
					}
					else if (c == '(')
					{
						token=createNewTokenIfNecessary(trailingText, SumaTokens.PARENTHESIS_START);
					}
					else if (c == ')')
					{
						token=createNewTokenIfNecessary(trailingText, SumaTokens.PARENTHESIS_END);
					}
					else if (c == '[')
					{
						token=createNewTokenIfNecessary(trailingText, SumaTokens.BRACKET_START);
					}
					else if (c == ']')
					{
						token=createNewTokenIfNecessary(trailingText, SumaTokens.BRACKET_END);
					}
					else if (c == '+')
					{
						switch (c2)
						{
							case '+':
								source.nextChar();
								token=createNewTokenIfNecessary(trailingText, SumaTokens.OPERATOR_PLUS_PLUS);
								break;
							case '=':
								source.nextChar();
								token=createNewTokenIfNecessary(trailingText, SumaTokens.OPERATOR_PLUS_EQUALS);
								break;
							default:
								token=createNewTokenIfNecessary(trailingText, SumaTokens.OPERATOR_PLUS);
								break;
						}
					}
					else if (c == '-')
					{
						switch (c2)
						{
							case '-':
								source.nextChar();
								token=createNewTokenIfNecessary(trailingText, SumaTokens.OPERATOR_MINUS_MINUS);
								break;
							case '=':
								source.nextChar();
								token=createNewTokenIfNecessary(trailingText, SumaTokens.OPERATOR_MINUS_EQUALS);
								break;
							default:
								token=createNewTokenIfNecessary(trailingText, SumaTokens.OPERATOR_MINUS);
								break;
						}
					}
					else if (c == '!')
					{
						if (c2 == '=')
						{
							source.nextChar();
							token=SumaTokens.OPERATOR_DISTINCT2;
							// source.incCurrentIndex();
						}
						else
						{
							token=SumaTokens.OPERATOR_NOT;
						}
					}
					else if (c == '|')
					{
						if (c2 == '|')
						{
							source.nextChar();
							token=createNewTokenIfNecessary(trailingText, SumaTokens.OPERATOR_OR);
						}
						else
						{
							token=createNewTokenIfNecessary(trailingText, SumaTokens.OPERATOR_OR_BITWISE);
						}
					}
					else if (c == '&')
					{
						if (c2 == '&')
						{
							source.nextChar();
							token=createNewTokenIfNecessary(trailingText, SumaTokens.OPERATOR_AND);
						}
						else
						{
							token=createNewTokenIfNecessary(trailingText, SumaTokens.OPERATOR_AND_BITWISE);
						}
					}
					else if (c == '=')
					{
						token=createNewTokenIfNecessary(trailingText, SumaTokens.OPERATOR_EQUALS);
					}
					else if (c == '<')
					{
						if (c2 == '>')
						{
							source.nextChar();
							token=SumaTokens.OPERATOR_DISTINCT1;
							// source.incCurrentIndex();
						}
						else if (c2 == '=')
						{
							source.nextChar();
							token=createNewTokenIfNecessary(trailingText, SumaTokens.OPERATOR_LOWER_OR_EQUALS);
							// source.incCurrentIndex();
						}
						else
						{
							token=createNewTokenIfNecessary(trailingText, SumaTokens.OPERATOR_LOWER);
						}
					}
					else if (c == '>')
					{
						if (c2 == '=')
						{
							source.nextChar();
							token=createNewTokenIfNecessary(trailingText, SumaTokens.OPERATOR_GREATER_OR_EQUALS);
							// source.incCurrentIndex();
						}
						else
						{
							token=createNewTokenIfNecessary(trailingText, SumaTokens.OPERATOR_GREATER);
						}
					}
					else if (c == '{')
					{
						token=createNewTokenIfNecessary(trailingText, SumaTokens.KEY_START);
					}
					else if (c == '}')
					{
						token=createNewTokenIfNecessary(trailingText, SumaTokens.KEY_END);
					}
					else if (c == '\n')
					{
						token=SumaTokens.LF;
					}
					else if (c == '\r')
					{
						if (c2 == '\n')
						{
							source.nextChar();
							token=SumaTokens.CRLF;
						}
						else
						{
							token=SumaTokens.CR;
						}
					}
					else
					{
						throw new UnexpectedSymbolException(source, c);
					}
					break;
				case READING_VARIABLE_REFERENCE:
					if (c == '}')
					{
						source.unget(c);
						token=createToken(trailingText, SumaTokenTypes.VARIABLE_REFERENCE, source.endRecord().toString());
						source.nextChar();
					}
					else if (Character.isLetterOrDigit(c) || c == '.' || c == '_')
					{
						// Seguir leyendo en este estado.
					}
					else
					{
						throw new UnclosedVariableReferenceException(source);
					}
					break;
				case READING_COMMENT:
					if (c == '*' && c2 == '/')
					{
						source.nextChar();
						switch (this.getSettings().getCommentsBehaviour())
						{
							case IGNORE:
								break;
							case PRODUCE_TOKENS:
								token=new Token(SumaTokenTypes.COMMENT, trailingText.toString() + source.endRecord().toString());
								break;
							case INCLUDE_IN_FOLLOWING_TOKEN:
								trailingText.append(source.endRecord().toString());
								break;
						}
						state=State.INITIAL;
						// source.incCurrentIndex();
					}
					else
					{
						// Seguir en este estado.
					}
					break;
				case READING_LINE_COMMENT:
					if (c == '\n')
					{
						switch (this.getSettings().getCommentsBehaviour())
						{
							case IGNORE:
								break;
							case PRODUCE_TOKENS:
								String image=source.endRecord().toString();
								token=new Token(SumaTokenTypes.LINE_COMMENT, trailingText.toString() + image, image.substring(2, image.length() - 2));
								break;
							case INCLUDE_IN_FOLLOWING_TOKEN:
								trailingText.append(source.endRecord().toString());
								break;
						}
						state=State.INITIAL;
					}
					else
					{
						// Seguir en este estado.
					}
					break;
				case READING_LETTERS:
					if (Character.isLetterOrDigit(c) || c == '_' || c == '$')
					{
						// Seguir en este estado.
					}
					else
					{
						source.unget(c);
						token=createTextToken(trailingText, source.endRecord().toString());
						// source.decCurrentIndex();
						state=State.INITIAL;
					}
					break;
				case READING_INTEGER_DIGITS:
					if (Character.isDigit(c))
					{
						// Seguir en este estado.
					}
					else if (c == 'E' || c == 'e')
					{
						state=State.READING_EXPONENT_DIGITS;
					}
					else if (c == '.')
					{
						state=State.READING_DECIMAL_DIGITS;
					}
					else
					{
						source.unget(c);
						token=createToken(trailingText, SumaTokenTypes.INTEGER_NUMBER_LITERAL, source.endRecord().toString());
						// source.decCurrentIndex();
						state=State.INITIAL;
					}
					break;
				case READING_DECIMAL_DIGITS:
					if (Character.isDigit(c))
					{
						// Seguir en este estado.
					}
					else if (c == 'E' || c == 'e')
					{
						state=State.READING_DECIMAL_AND_EXPONENT_DIGITS;
					}
					else if (c == '.')
					{
						throw new IllegalDoublePeriodException(source);
					}
					else
					{
						source.unget(c);
						token=createToken(trailingText, SumaTokenTypes.DECIMAL_NUMBER_LITERAL, source.endRecord().toString());
						// source.decCurrentIndex();
						state=State.INITIAL;
					}
					break;
				case READING_DECIMAL_AND_EXPONENT_DIGITS:
					if (c == '-' || c == '+')
					{
						// Seguir en este estado
					}
					else if (Character.isDigit(c))
					{
						state=State.READING_DECIMAL_AND_EXPONENT_DIGITS_WITHOUT_SIGN;
					}
					else if (c == 'E' || c == 'e')
					{
						throw new IllegalDoubleEException(source);
					}
					else if (c == '.')
					{
						throw new IllegalPeriodInExponentException(source);
					}
					else
					{
						token=createToken(trailingText, SumaTokenTypes.DECIMAL_NUMBER_LITERAL, source.endRecord().toString());
						// source.decCurrentIndex();
						state=State.INITIAL;
					}
					break;
				case READING_DECIMAL_AND_EXPONENT_DIGITS_WITHOUT_SIGN:
					if (Character.isDigit(c))
					{
						// Seguir en este estado.
					}
					else if (c == 'E' || c == 'e')
					{
						throw new IllegalDoubleEException(source);
					}
					else if (c == '.')
					{
						throw new IllegalPeriodInExponentException(source);
					}
					else
					{
						token=createToken(trailingText, SumaTokenTypes.DECIMAL_NUMBER_LITERAL, source.endRecord().toString());
						// source.decCurrentIndex();
						state=State.INITIAL;
					}
					break;
				case READING_EXPONENT_DIGITS:
					if (c == '-' || c == '+')
					{
						// Seguir en este estado: Podr�a haber varios signos seguidos y ser�a una notaci�n v�lida.
					}
					else if (Character.isDigit(c))
					{
						state=State.READING_EXPONENT_DIGITS_WITHOUT_SIGN;
					}
					else if (c == 'E' || c == 'e')
					{
						throw new IllegalDoubleEException(source);
					}
					else if (c == '.')
					{
						throw new IllegalPeriodInExponentException(source);
					}
					else
					{
						token=createToken(trailingText, SumaTokenTypes.INTEGER_NUMBER_LITERAL, source.endRecord().toString());
						// source.decCurrentIndex();
						state=State.INITIAL;
					}
					break;
				case READING_EXPONENT_DIGITS_WITHOUT_SIGN:
					if (Character.isDigit(c))
					{
						// Seguir en este estado.
					}
					else if (c == 'E' || c == 'e')
					{
						throw new IllegalDoubleEException(source);
					}
					else if (c == '.')
					{
						throw new IllegalPeriodInExponentException(source);
					}
					else
					{
						source.unget(c);
						token=createToken(trailingText, SumaTokenTypes.INTEGER_NUMBER_LITERAL, source.endRecord().toString());
						// source.decCurrentIndex();
						state=State.INITIAL;
					}
					break;
				case READING_TEXT_LITERAL:
					if (c == '\'')
					{
						if (c2 == '\'')
						{
							// Comilla escapeada:
							source.discardChar();
						}
						else
						{
							source.unget(c);
							token=createToken(trailingText, SumaTokenTypes.SINGLE_QUOTED_TEXT_LITERAL, source.endRecord().toString());
							source.nextChar();
							state=State.INITIAL;
						}
					}
					break;
				case READING_DOUBLE_QUOTED_TEXT_LITERAL:
					if (c == '\"')
					{
						source.unget(c);
						token=createToken(trailingText, SumaTokenTypes.DOUBLE_QUOTED_TEXT_LITERAL, source.endRecord().toString());
						source.nextChar();
						state=State.INITIAL;
					}
					break;
				default:
					// No hay m�s casos.
					break;
			}
		}
		// Completar el resto, si queda:
		if (token == null)
		{
			switch (state)
			{
				case READING_LETTERS:
					token=createTextToken(trailingText, source.endRecord().toString());
					break;
				case READING_INTEGER_DIGITS:
				case READING_EXPONENT_DIGITS_WITHOUT_SIGN:
					token=createToken(trailingText, SumaTokenTypes.INTEGER_NUMBER_LITERAL, source.endRecord().toString());
					break;
				case READING_DECIMAL_DIGITS:
				case READING_DECIMAL_AND_EXPONENT_DIGITS_WITHOUT_SIGN:
					token=createToken(trailingText, SumaTokenTypes.DECIMAL_NUMBER_LITERAL, source.endRecord().toString());
					break;
				case READING_LINE_COMMENT:
					switch (this.getSettings().getCommentsBehaviour())
					{
						case IGNORE:
							break;
						case INCLUDE_IN_FOLLOWING_TOKEN:
						case PRODUCE_TOKENS:
							String image=source.endRecord().toString();
							token=new Token(SumaTokenTypes.LINE_COMMENT, trailingText.toString() + image, image.substring(2, image.length() - 2));
							break;
					}
					break;
				case READING_COMMENT:
					throw new UnclosedCommentException(source);
				case READING_TEXT_LITERAL:
					throw new UnclosedTextLiteralException(source);
				case READING_VARIABLE_REFERENCE:
					throw new UnclosedVariableReferenceException(source);
				case INITIAL:
				default:
					// Ignorar.
			}
		}
		return token;
	}

	// private Token createNewTokenIfNecessary(StringBuilder text, Token token)
	// {
	// return text.length() == 0
	// ? token
	// : new Token(token.getType(), text.toString());
	// }
	//
	// // private Token createToken(StringBuilder text, Source source, String type, int initialIndex, int endIndex)
	// // {
	// // return new Token(type, text.toString() + source.endRecord().toString());
	// // }
	//
	// private Token createToken(StringBuilder trailingText, String type, String text)
	// {
	// return new Token(type, trailingText.toString() + text);
	// }
	//
	// private Token createTextToken(StringBuilder leftText, String text)
	// {
	// // Decide si se trata de una palabra reservada, un operador, o un identificador:
	// String type=KEYWORDS.get(text.toLowerCase());
	// if (type == null)
	// {
	// type=OPERATORS.get(text.toLowerCase());
	// if (type == null)
	// {
	// type=SumaTokenTypes.IDENTIFIER;
	// }
	// }
	// if (leftText.length() > 0)
	// {
	// text=leftText.toString() + text;
	// }
	// return new Token(type, text);
	// }

	@Override
	protected String lookupKeywordOrOperator(String text)
	{
		String textLower=text.toLowerCase();
		String type=KEYWORDS.get(textLower);
		if (type == null)
		{
			type=OPERATORS.get(textLower);
		}
		if (type == null)
		{
			type=super.lookupKeywordOrOperator(text);
		}
		return type;
	}
}
