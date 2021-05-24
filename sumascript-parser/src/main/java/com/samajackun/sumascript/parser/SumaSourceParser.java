package com.samajackun.sumascript.parser;

import java.io.IOException;

import com.samajackun.rodas.core.eval.Name;
import com.samajackun.rodas.core.model.Source;
import com.samajackun.rodas.parsing.parser.ParserException;
import com.samajackun.rodas.sql.parser.ParserContext;
import com.samajackun.rodas.sql.parser.ParserFactory;
import com.samajackun.rodas.sql.parser.SourceParser;
import com.samajackun.rodas.sql.tokenizer.AbstractMatchingTokenizer;
import com.samajackun.rodas.sql.tokenizer.Token;
import com.samajackun.sumascript.core.sources.ReferenceSource;
import com.samajackun.sumascript.tokenizer.SumaTokenTypes;

public class SumaSourceParser extends SourceParser
{
	public SumaSourceParser(ParserFactory parserFactory)
	{
		super(parserFactory);
	}

	@Override
	protected Source unexpectedTokenAfterFrom(AbstractMatchingTokenizer tokenizer, ParserContext parserContext, Token unexpectedToken)
		throws ParserException,
		IOException
	{
		if (unexpectedToken.getType().equals(SumaTokenTypes.VARIABLE_REFERENCE))
		{
			return new ReferenceSource(Name.instanceOf(unexpectedToken.getImage()));
		}
		else
		{
			return super.unexpectedTokenAfterFrom(tokenizer, parserContext, unexpectedToken);
		}
	}
}
