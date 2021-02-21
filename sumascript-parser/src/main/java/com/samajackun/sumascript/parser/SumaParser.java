package com.samajackun.sumascript.parser;

import java.io.IOException;
import java.io.Reader;

import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.model.Expression;
import com.samajackun.rodas.parsing.parser.ParserException;
import com.samajackun.rodas.parsing.source.PushBackSource;
import com.samajackun.rodas.parsing.source.ReaderSource;
import com.samajackun.rodas.sql.parser.ParserContext;
import com.samajackun.sumascript.tokenizer.SumaMatchingTokenizer;
import com.samajackun.sumascript.tokenizer.SumaTokenizer;

public final class SumaParser
{
	private static final SumaParser INSTANCE=new SumaParser();

	private final ProgramParser statefulParser=ProgramParser.getInstance();

	public static SumaParser getInstance()
	{
		return INSTANCE;
	}

	private SumaParser()
	{
	}

	public Program parse(Reader reader)
		throws IOException,
		SumaParseException
	{
		try
		{
			SumaMatchingTokenizer tokenizer=new SumaMatchingTokenizer(new SumaTokenizer(new PushBackSource(new ReaderSource(reader))));
			ParserContext parserContext=new ParserContext(); // TODO
			return this.statefulParser.parse(tokenizer, parserContext);
		}
		catch (ParserException | EvaluationException e)
		{
			throw new SumaParseException(e);
		}
	}

	public Expression parseExpression(SumaMatchingTokenizer tokenizer, ParserContext parserContext)
		throws IOException,
		ParserException
	{
		return CommonParser.getInstance().parseExpression(tokenizer, parserContext);
	}

	public Expression parseUnnamedFunctionDeclaration(SumaMatchingTokenizer tokenizer, ParserContext parserContext)
		throws IOException,
		ParserException
	{
		try
		{
			return FunctionParser.getInstance().parseUnnamedFunctionDeclaration(tokenizer, parserContext);
		}
		catch (EvaluationException e)
		{
			throw new SumaParseException(e.getMessage(), e);
		}
	}
}
