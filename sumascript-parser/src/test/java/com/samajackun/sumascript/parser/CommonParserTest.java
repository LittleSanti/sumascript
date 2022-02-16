package com.samajackun.sumascript.parser;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import com.samajackun.rodas.core.model.Expression;
import com.samajackun.rodas.core.model.SelectSentence;
import com.samajackun.rodas.parsing.parser.ParserException;
import com.samajackun.rodas.parsing.source.CharSequenceSource;
import com.samajackun.rodas.parsing.source.PushBackSource;
import com.samajackun.rodas.parsing.tokenizer.TokenizerException;
import com.samajackun.rodas.sql.parser.ParserContext;
import com.samajackun.sumascript.tokenizer.SumaMatchingTokenizer;
import com.samajackun.sumascript.tokenizer.SumaTokenizer;

public class CommonParserTest
{
	private SumaMatchingTokenizer tokenize(String input)
		throws TokenizerException,
		IOException
	{
		return new SumaMatchingTokenizer(new SumaTokenizer(new PushBackSource(new CharSequenceSource(input))));
	}

	private Expression test(String code)
		throws IOException,
		ParserException
	{
		try
		{
			SumaMatchingTokenizer tokenizer=tokenize(code);
			ParserContext parserContext=new ParserContext();
			Expression expression=CommonParser.getInstance().parseExpression(tokenizer, parserContext);
			return expression;
		}
		catch (RuntimeException | TokenizerException e)
		{
			e.printStackTrace();
			fail(e.toString());
			return null;
		}
	}

	@Test
	public void parseSelect()
		throws IOException
	{
		String input="SELECT a";
		try
		{
			Expression expression=test(input);
			assertTrue(expression instanceof SelectSentence);
		}
		catch (ParserException e)
		{
			fail(e.toString());
		}
	}

	@Test
	public void parseSelectFrom()
		throws IOException
	{
		String input="SELECT a FROM b";
		try
		{
			Expression expression=test(input);
			assertTrue(expression instanceof SelectSentence);
		}
		catch (ParserException e)
		{
			fail(e.toString());
		}
	}

	@Test
	public void parseSelectFromReference()
		throws IOException
	{
		String input="SELECT a FROM ${b}";
		try
		{
			Expression expression=test(input);
			assertTrue(expression instanceof SelectSentence);
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}
}
