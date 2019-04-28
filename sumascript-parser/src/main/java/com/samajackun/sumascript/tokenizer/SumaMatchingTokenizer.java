package com.samajackun.sumascript.tokenizer;

import com.samajackun.rodas.sql.tokenizer.AbstractMatchingTokenizer;
import com.samajackun.rodas.sql.tokenizer.Token;
import com.samajackun.rodas.sql.tokenizer.TokenEvaluator;

public class SumaMatchingTokenizer extends AbstractMatchingTokenizer
{
	public SumaMatchingTokenizer(SumaTokenizer tokenizer)
	{
		super(tokenizer, new TokenEvaluator()
		{
			@Override
			public boolean isUseful(Token token)
			{
				return token.getType() != SumaTokenTypes.COMMENT && token.getType() != SumaTokenTypes.LINE_COMMENT;
			}
		});
	}
}
