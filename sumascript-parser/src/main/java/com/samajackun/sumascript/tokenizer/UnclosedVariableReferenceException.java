package com.samajackun.sumascript.tokenizer;

import com.samajackun.rodas.parsing.source.Source;
import com.samajackun.rodas.parsing.tokenizer.TokenizerException;

public class UnclosedVariableReferenceException extends TokenizerException
{
	private static final long serialVersionUID=5896276911803899342L;

	public UnclosedVariableReferenceException(Source source)
	{
		super(source, "Unclosed variable reference");
	}
}
