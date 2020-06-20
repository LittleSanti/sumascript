package com.samajackun.sumascript.core.runtime;

import java.util.List;

import com.samajackun.rodas.core.eval.Name;
import com.samajackun.sumascript.core.Instruction;

public class NamedCodedFunction extends CodedFunction
{
	private final Name name;

	public NamedCodedFunction(List<Name> parameterNames, Instruction body, Name name)
	{
		super(parameterNames, body);
		this.name=name;
	}

	public Name getName()
	{
		return this.name;
	}
}
