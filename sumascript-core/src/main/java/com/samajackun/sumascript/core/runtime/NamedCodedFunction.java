package com.samajackun.sumascript.core.runtime;

import java.util.List;

import com.samajackun.sumascript.core.Instruction;

public class NamedCodedFunction extends CodedFunction
{
	private final String name;

	public NamedCodedFunction(List<String> parameterNames, Instruction body, String name)
	{
		super(parameterNames, body);
		this.name=name;
	}

	public String getName()
	{
		return name;
	}
}
