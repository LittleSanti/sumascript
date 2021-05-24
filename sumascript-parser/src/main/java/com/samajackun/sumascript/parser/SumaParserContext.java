package com.samajackun.sumascript.parser;

import java.util.HashMap;
import java.util.Map;

import com.samajackun.rodas.core.eval.Name;
import com.samajackun.rodas.sql.parser.ParserContext;
import com.samajackun.sumascript.core.runtime.NamedCodedFunction;

public class SumaParserContext extends ParserContext
{
	private final Map<Name, NamedCodedFunction> functionMap=new HashMap<>();

	public Map<Name, NamedCodedFunction> getFunctionMap()
	{
		return this.functionMap;
	}

}
