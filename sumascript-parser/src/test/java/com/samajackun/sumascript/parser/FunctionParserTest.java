package com.samajackun.sumascript.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;

import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.parsing.parser.ParserException;
import com.samajackun.rodas.parsing.source.CharSequenceSource;
import com.samajackun.rodas.parsing.source.PushBackSource;
import com.samajackun.rodas.parsing.tokenizer.TokenizerException;
import com.samajackun.sumascript.core.instructions.FunctionDeclarationInstruction;
import com.samajackun.sumascript.tokenizer.SumaMatchingTokenizer;
import com.samajackun.sumascript.tokenizer.SumaTokenizer;

public class FunctionParserTest
{
	private SumaMatchingTokenizer tokenize(String input)
		throws TokenizerException,
		IOException
	{
		return new SumaMatchingTokenizer(new SumaTokenizer(new PushBackSource(new CharSequenceSource(input))));
	}

	private SumaParserContext createParserContext()
	{
		return new SumaParserContext();
	}

	@Test
	public void parseLocalOrGlobalCollectionLoopClauseWithEmpty()
		throws TokenizerException,
		IOException
	{
		String input="sayHello(s) { echoout \"Hello, \"+s}";
		SumaMatchingTokenizer tokenizer=tokenize(input);
		try
		{
			FunctionDeclarationInstruction function=FunctionParser.getInstance().parseFunctionDefinition(tokenizer, createParserContext());
			assertEquals("sayHello", function.getCodedFunction().getName().asString());
		}
		catch (ParserException | EvaluationException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void parseFunctionWithReturn()
		throws TokenizerException,
		IOException
	{
		String input="nextNumber(n)\r\n{\r\nvar sig=1+n\r\nreturn sig\r\n}\r\n";
		SumaMatchingTokenizer tokenizer=tokenize(input);
		try
		{
			FunctionDeclarationInstruction function=FunctionParser.getInstance().parseFunctionDefinition(tokenizer, createParserContext());
			assertEquals("nextNumber", function.getCodedFunction().getName().asString());
		}
		catch (ParserException | EvaluationException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}
}
