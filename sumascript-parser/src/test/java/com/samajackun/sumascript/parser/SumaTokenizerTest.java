package com.samajackun.sumascript.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;

import com.samajackun.rodas.parsing.source.CharSequenceSource;
import com.samajackun.rodas.parsing.source.PushBackSource;
import com.samajackun.rodas.parsing.tokenizer.TokenizerException;
import com.samajackun.rodas.parsing.tokenizer.UnclosedCommentException;
import com.samajackun.rodas.parsing.tokenizer.UnclosedTextLiteralException;
import com.samajackun.rodas.parsing.tokenizer.UnexpectedSymbolException;
import com.samajackun.rodas.sql.tokenizer.SqlTokenTypes;
import com.samajackun.rodas.sql.tokenizer.Token;
import com.samajackun.rodas.sql.tokenizer.TokenizerSettings;
import com.samajackun.rodas.sql.tokenizer.TokenizerSettings.CommentsBehaviour;
import com.samajackun.rodas.sql.tokenizer.TokenizerSettings.WhitespaceBehaviour;
import com.samajackun.sumascript.tokenizer.SumaTokenTypes;
import com.samajackun.sumascript.tokenizer.SumaTokenizer;
import com.samajackun.sumascript.tokenizer.SumaTokens;
import com.samajackun.sumascript.tokenizer.UnclosedVariableReferenceException;

public class SumaTokenizerTest
{
	private void test(String src, Token... expectedTokens)
		throws IOException
	{
		test(src, new TokenizerSettings(), expectedTokens);
	}

	private void testWithProducingTokensForComments(String src, Token... expectedTokens)
		throws IOException
	{
		TokenizerSettings settings=new TokenizerSettings();
		settings.setCommentsBehaviour(CommentsBehaviour.PRODUCE_TOKENS);
		test(src, settings, expectedTokens);
	}

	private void testWithProducingTokensForCommentsAndIncludingWhitespaceInFollowingToken(String src, Token... expectedTokens)
		throws IOException
	{
		TokenizerSettings settings=new TokenizerSettings();
		settings.setCommentsBehaviour(CommentsBehaviour.PRODUCE_TOKENS);
		settings.setWhitespaceBehaviour(WhitespaceBehaviour.INCLUDE_IN_FOLLOWING_TOKEN);
		test(src, settings, expectedTokens);
	}

	private void test(String src, TokenizerSettings settings, Token... expectedTokens)
		throws IOException
	{
		try
		{
			SumaTokenizer tokenizer=new SumaTokenizer(new PushBackSource(new CharSequenceSource(src)), settings);
			Token token;

			for (Token expectedToken : expectedTokens)
			{
				token=tokenizer.nextToken();
				assertNotNull(token);
				assertEquals(expectedToken, token);
			}
			assertNull(tokenizer.nextToken());
		}
		catch (TokenizerException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void empty()
		throws IOException
	{
		String src="";
		Token[] expectedTokens= {};
		test(src, expectedTokens);
	}

	@Test
	public void integerNumberLiteral()
		throws IOException
	{
		String src="12";
		Token[] expectedTokens= {
			new Token(SumaTokenTypes.INTEGER_NUMBER_LITERAL, "12")
		};
		test(src, expectedTokens);
	}

	@Test
	public void integerNumberLiteralPositive()
		throws IOException
	{
		String src="+12";
		Token[] expectedTokens= {
			SumaTokens.OPERATOR_PLUS,
			new Token(SumaTokenTypes.INTEGER_NUMBER_LITERAL, "12")
		};
		test(src, expectedTokens);
	}

	@Test
	public void integerNumberLiteralNegative()
		throws IOException
	{
		String src="-12";
		Token[] expectedTokens= {
			SumaTokens.OPERATOR_MINUS,
			new Token(SumaTokenTypes.INTEGER_NUMBER_LITERAL, "12")
		};
		test(src, expectedTokens);
	}

	@Test
	public void decimalNumberLiteral()
		throws IOException
	{
		String src="1.2";
		Token[] expectedTokens= {
			new Token(SumaTokenTypes.DECIMAL_NUMBER_LITERAL, "1.2")
		};
		test(src, expectedTokens);
	}

	@Test
	public void integerExponentialNumberLiteral()
		throws IOException
	{
		String src="9E4";
		Token[] expectedTokens= {
			new Token(SumaTokenTypes.INTEGER_NUMBER_LITERAL, "9E4")
		};
		test(src, expectedTokens);
	}

	@Test
	public void integerExponentialNumberLiteralWithPlusSign()
		throws IOException
	{
		String src="9E+4";
		Token[] expectedTokens= {
			new Token(SumaTokenTypes.INTEGER_NUMBER_LITERAL, "9E+4")
		};
		test(src, expectedTokens);
	}

	@Test
	public void integerExponentialNumberLiteralWithMinusSign()
		throws IOException
	{
		String src="9E-4";
		Token[] expectedTokens= {
			new Token(SumaTokenTypes.INTEGER_NUMBER_LITERAL, "9E-4")
		};
		test(src, expectedTokens);
	}

	@Test
	public void integerExponentialNumberLiteralWithMinusMinusSign()
		throws IOException
	{
		String src="9E--4";
		Token[] expectedTokens= {
			new Token(SumaTokenTypes.INTEGER_NUMBER_LITERAL, "9E--4")
		};
		test(src, expectedTokens);
	}

	@Test
	public void decimalExponentialNumberLiteralWithMinusSign()
		throws IOException
	{
		String src="9.2E-4";
		Token[] expectedTokens= {
			new Token(SumaTokenTypes.DECIMAL_NUMBER_LITERAL, "9.2E-4")
		};
		test(src, expectedTokens);
	}

	@Test
	public void commentAsTokenProducingTokensForComments()
		throws IOException
	{
		String src="/*enero*/";
		Token[] expectedTokens= {
			new Token(SumaTokenTypes.COMMENT, "/*enero*/")
		};
		testWithProducingTokensForComments(src, expectedTokens);
	}

	@Test
	public void commentAsToken()
		throws IOException
	{
		String src="/*enero*/";
		Token[] expectedTokens= {};
		test(src, expectedTokens);
	}

	@Test
	public void commentIgnored()
		throws IOException
	{
		String src="/*enero*/";
		Token[] expectedTokens= {};
		test(src, expectedTokens);
	}

	@Test
	public void blanksCommentWithProducingTokensForComments()
		throws IOException
	{
		String src=" /*enero*/";
		Token[] expectedTokens= {
			new Token(SumaTokenTypes.COMMENT, "/*enero*/")
		};
		testWithProducingTokensForComments(src, expectedTokens);
	}

	@Test
	public void blanksCommentWithProducingTokensForCommentsAndIncludingWhitespaceInFollowingToken()
		throws IOException
	{
		String src=" /*enero*/";
		Token[] expectedTokens= {
			new Token(SumaTokenTypes.COMMENT, " /*enero*/")
		};
		testWithProducingTokensForCommentsAndIncludingWhitespaceInFollowingToken(src, expectedTokens);
	}

	@Test
	public void blanksComment()
		throws IOException
	{
		String src=" /*enero*/";
		Token[] expectedTokens= {};
		test(src, expectedTokens);
	}

	@Test
	public void commentWithAsteriskWithProducingTokensForComments()
		throws IOException
	{
		String src="/*enero*febrero*/";
		Token[] expectedTokens= {
			new Token(SumaTokenTypes.COMMENT, "/*enero*febrero*/")
		};
		testWithProducingTokensForComments(src, expectedTokens);
	}

	@Test
	public void commentWithAsterisk()
		throws IOException
	{
		String src="/*enero*febrero*/";
		Token[] expectedTokens= {};
		test(src, expectedTokens);
	}

	@Test
	public void twoCommentsWithProducingTokensForComments()
		throws IOException
	{
		String src="/*enero*//*febrero*/";
		Token[] expectedTokens= {
			new Token(SumaTokenTypes.COMMENT, "/*enero*/"),
			new Token(SumaTokenTypes.COMMENT, "/*febrero*/")
		};
		testWithProducingTokensForComments(src, expectedTokens);
	}

	@Test
	public void twoComments()
		throws IOException
	{
		String src="/*enero*//*febrero*/";
		Token[] expectedTokens= {};
		test(src, expectedTokens);
	}

	@Test
	public void doubleQuotedTextLiteral()
		throws IOException
	{
		String src="\"enero\"";
		Token[] expectedTokens= {
			new Token(SumaTokenTypes.DOUBLE_QUOTED_TEXT_LITERAL, "enero")
		};
		test(src, expectedTokens);
	}

	@Test
	public void textLiteral()
		throws IOException
	{
		String src="'enero'";
		Token[] expectedTokens= {
			new Token(SumaTokenTypes.SINGLE_QUOTED_TEXT_LITERAL, "enero")
		};
		test(src, expectedTokens);
	}

	@Test
	public void textLiteralWithQuoteEscapped()
		throws IOException
	{
		String src="'o''donell'";
		Token[] expectedTokens= {
			new Token(SumaTokenTypes.SINGLE_QUOTED_TEXT_LITERAL, "o'donell")
		};
		test(src, expectedTokens);
	}

	@Test
	public void identifierStartingWithLetter()
		throws IOException
	{
		String src="enero";
		Token[] expectedTokens= {
			new Token(SumaTokenTypes.IDENTIFIER, "enero")
		};
		test(src, expectedTokens);
	}

	@Test
	public void identifierStartingWithUnderscore()
		throws IOException
	{
		String src="_enero";
		Token[] expectedTokens= {
			new Token(SumaTokenTypes.IDENTIFIER, "_enero")
		};
		test(src, expectedTokens);
	}

	@Test
	public void identifierStartingWithDollar()
		throws IOException
	{
		String src="$enero";
		Token[] expectedTokens= {
			new Token(SumaTokenTypes.IDENTIFIER, "$enero")
		};
		test(src, expectedTokens);
	}

	@Test
	public void identifierWithUnderscore()
		throws IOException
	{
		String src="enero_febrero";
		Token[] expectedTokens= {
			new Token(SumaTokenTypes.IDENTIFIER, "enero_febrero")
		};
		test(src, expectedTokens);
	}

	@Test
	public void identifierWithDollar()
		throws IOException
	{
		String src="enero$febrero";
		Token[] expectedTokens= {
			new Token(SumaTokenTypes.IDENTIFIER, "enero$febrero")
		};
		test(src, expectedTokens);
	}

	@Test
	public void prefixedIdentifier()
		throws IOException
	{
		String src="enero.febrero";
		Token[] expectedTokens= {
			new Token(SumaTokenTypes.IDENTIFIER, "enero"),
			SumaTokens.PERIOD,
			new Token(SumaTokenTypes.IDENTIFIER, "febrero")
		};
		test(src, expectedTokens);
	}

	// @Test
	// public void keywordInLowercase()
	// throws IOException
	// {
	// String src="select";
	// Token[] expectedTokens= {
	// new Token(SumaTokenTypes.KEYWORD_SELECT, "select")
	// };
	// test(src, expectedTokens);
	// }
	//
	// @Test
	// public void keywordSelectInUppercase()
	// throws IOException
	// {
	// String src="SELECT";
	// Token[] expectedTokens= {
	// new Token(SumaTokenTypes.KEYWORD_SELECT, "SELECT")
	// };
	// test(src, expectedTokens);
	// }

	@Test
	public void asterisk()
		throws IOException
	{
		String src="*";
		Token[] expectedTokens= {
			SumaTokens.ASTERISK
		};
		test(src, expectedTokens);
	}

	@Test
	public void distinct1()
		throws IOException
	{
		String src="<>";
		Token[] expectedTokens= {
			SumaTokens.OPERATOR_DISTINCT1
		};
		test(src, expectedTokens);
	}

	@Test
	public void distinct2()
		throws IOException
	{
		String src="!=";
		Token[] expectedTokens= {
			SumaTokens.OPERATOR_DISTINCT2
		};
		test(src, expectedTokens);
	}

	@Test
	public void div()
		throws IOException
	{
		String src="/";
		Token[] expectedTokens= {
			SumaTokens.OPERATOR_DIV
		};
		test(src, expectedTokens);
	}

	@Test
	public void equals()
		throws IOException
	{
		String src="=";
		Token[] expectedTokens= {
			SumaTokens.OPERATOR_EQUALS
		};
		test(src, expectedTokens);
	}

	@Test
	public void greater()
		throws IOException
	{
		String src=">";
		Token[] expectedTokens= {
			SumaTokens.OPERATOR_GREATER
		};
		test(src, expectedTokens);
	}

	@Test
	public void greaterOrEquals()
		throws IOException
	{
		String src=">=";
		Token[] expectedTokens= {
			SumaTokens.OPERATOR_GREATER_OR_EQUALS
		};
		test(src, expectedTokens);
	}

	@Test
	public void lower()
		throws IOException
	{
		String src="<";
		Token[] expectedTokens= {
			SumaTokens.OPERATOR_LOWER
		};
		test(src, expectedTokens);
	}

	@Test
	public void lowerOrEquals()
		throws IOException
	{
		String src="<=";
		Token[] expectedTokens= {
			SumaTokens.OPERATOR_LOWER_OR_EQUALS
		};
		test(src, expectedTokens);
	}

	@Test
	public void plus()
		throws IOException
	{
		String src="+";
		Token[] expectedTokens= {
			SumaTokens.OPERATOR_PLUS
		};
		test(src, expectedTokens);
	}

	@Test
	public void plusPlus()
		throws IOException
	{
		String src="++";
		Token[] expectedTokens= {
			SumaTokens.OPERATOR_PLUS_PLUS
		};
		test(src, expectedTokens);
	}

	@Test
	public void minus()
		throws IOException
	{
		String src="-";
		Token[] expectedTokens= {
			SumaTokens.OPERATOR_MINUS
		};
		test(src, expectedTokens);
	}

	@Test
	public void minusMinus()
		throws IOException
	{
		String src="--";
		Token[] expectedTokens= {
			SumaTokens.OPERATOR_MINUS_MINUS
		};
		test(src, expectedTokens);
	}

	@Test
	public void parenthesisStart()
		throws IOException
	{
		String src="(";
		Token[] expectedTokens= {
			SumaTokens.PARENTHESIS_START
		};
		test(src, expectedTokens);
	}

	@Test
	public void parenthesisEnd()
		throws IOException
	{
		String src=")";
		Token[] expectedTokens= {
			SumaTokens.PARENTHESIS_END
		};
		test(src, expectedTokens);
	}

	// @Test
	// public void period() throws IOException
	// {
	// String src=".";
	// Token[] expectedTokens= { SumaTokens.TOKEN_PERIOD };
	// test(src, expectedTokens);
	// }

	@Test
	public void semicolon()
		throws IOException
	{
		String src=";";
		Token[] expectedTokens= {
			SumaTokens.SEMICOLON
		};
		test(src, expectedTokens);
	}

	@Test
	public void comma()
		throws IOException
	{
		String src=",";
		Token[] expectedTokens= {
			SumaTokens.COMMA
		};
		test(src, expectedTokens);
	}

	// @Test
	// public void select()
	// throws IOException
	// {
	// String src="SELECT a, b FROM c WHERE z=10+x";
	// Token[] expectedTokens= {
	// new Token(SumaTokenTypes.KEYWORD_SELECT, "SELECT"),
	// new Token(SumaTokenTypes.IDENTIFIER, "a"),
	// SumaTokens.TOKEN_COMMA,
	// new Token(SumaTokenTypes.IDENTIFIER, "b"),
	// new Token(SumaTokenTypes.KEYWORD_FROM, "FROM"),
	// new Token(SumaTokenTypes.IDENTIFIER, "c"),
	// new Token(SumaTokenTypes.KEYWORD_WHERE, "WHERE"),
	// new Token(SumaTokenTypes.IDENTIFIER, "z"),
	// SumaTokens.TOKEN_EQUALS,
	// new Token(SumaTokenTypes.INTEGER_NUMBER_LITERAL, "10"),
	// SumaTokens.TOKEN_PLUS,
	// new Token(SumaTokenTypes.IDENTIFIER, "x"),
	// };
	// test(src, expectedTokens);
	// }

	@Test
	public void exclamation()
		throws IOException,
		TokenizerException
	{
		String src="!";
		Token[] expectedTokens= {
			SumaTokens.OPERATOR_NOT
		};
		test(src, expectedTokens);
	}

	@Test(expected=UnexpectedSymbolException.class)
	public void angle()
		throws IOException,
		TokenizerException
	{
		String src="^";
		SumaTokenizer tokenizer=new SumaTokenizer(new PushBackSource(new CharSequenceSource(src)));
		tokenizer.nextToken();
	}

	public void ampersand()
		throws IOException,
		TokenizerException
	{
		String src="&";
		Token[] expectedTokens= {
			SumaTokens.OPERATOR_AND_BITWISE
		};
		test(src, expectedTokens);
	}

	public void ampersandAmpersand()
		throws IOException,
		TokenizerException
	{
		String src="&&";
		Token[] expectedTokens= {
			SumaTokens.OPERATOR_AND
		};
		test(src, expectedTokens);
	}

	public void pipe()
		throws IOException,
		TokenizerException
	{
		String src="1";
		Token[] expectedTokens= {
			SumaTokens.OPERATOR_OR_BITWISE
		};
		test(src, expectedTokens);
	}

	public void pipePipe()
		throws IOException,
		TokenizerException
	{
		String src="||";
		Token[] expectedTokens= {
			SumaTokens.OPERATOR_OR
		};
		test(src, expectedTokens);
	}

	@Test(expected=UnclosedCommentException.class)
	public void commentUnclosed()
		throws IOException,
		TokenizerException
	{
		String src="/*enero";
		SumaTokenizer tokenizer=new SumaTokenizer(new PushBackSource(new CharSequenceSource(src)));
		tokenizer.nextToken();
	}

	@Test(expected=UnclosedTextLiteralException.class)
	public void textLiteralUnclosed()
		throws IOException,
		TokenizerException
	{
		String src="'enero";
		SumaTokenizer tokenizer=new SumaTokenizer(new PushBackSource(new CharSequenceSource(src)));
		tokenizer.nextToken();
	}

	@Test
	public void readAfterLastToken()
		throws IOException,
		TokenizerException
	{
		String src="enero";
		SumaTokenizer tokenizer=new SumaTokenizer(new PushBackSource(new CharSequenceSource(src)));
		tokenizer.nextToken();
		assertNull(tokenizer.nextToken());
	}

	@Test
	public void variableReference()
		throws IOException
	{
		String src="${enero}";
		Token[] expectedTokens= {
			new Token(SumaTokenTypes.VARIABLE_REFERENCE, "enero"),
		};
		test(src, expectedTokens);
	}

	@Test(expected=UnclosedVariableReferenceException.class)
	public void variableReferenceUnclosed()
		throws IOException,
		TokenizerException
	{
		String src="${enero";
		SumaTokenizer tokenizer=new SumaTokenizer(new PushBackSource(new CharSequenceSource(src)));
		tokenizer.nextToken();
	}

	@Test
	public void namedParameterPlusNumberLiteral()
		throws IOException
	{
		String src="${enero}+12";
		Token[] expectedTokens= {
			new Token(SumaTokenTypes.VARIABLE_REFERENCE, "enero"),
			new Token(SumaTokenTypes.OPERATOR_PLUS, "+"),
			new Token(SumaTokenTypes.INTEGER_NUMBER_LITERAL, "12"),
		};
		test(src, expectedTokens);
	}

	@Test
	public void keywordTrue()
		throws IOException
	{
		String src="true";
		Token[] expectedTokens= {
			new Token(SumaTokenTypes.TRUE, "true")
		};
		test(src, expectedTokens);
	}

	@Test
	public void keywordFalse()
		throws IOException
	{
		String src="false";
		Token[] expectedTokens= {
			new Token(SumaTokenTypes.FALSE, "false")
		};
		test(src, expectedTokens);
	}

	@Test
	public void keywordSelect()
		throws IOException
	{
		String src="select";
		Token[] expectedTokens= {
			new Token(SqlTokenTypes.KEYWORD_SELECT, "select")
		};
		test(src, expectedTokens);
	}
}
