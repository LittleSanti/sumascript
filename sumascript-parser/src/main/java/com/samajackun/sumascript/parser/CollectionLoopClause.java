package com.samajackun.sumascript.parser;

import com.samajackun.rodas.core.eval.Name;
import com.samajackun.rodas.core.model.Expression;

public class CollectionLoopClause
{
	private final Name varName;

	private final Expression collection;

	private final boolean local;

	public CollectionLoopClause(Name varName, Expression collection, boolean local)
	{
		super();
		this.varName=varName;
		this.collection=collection;
		this.local=local;
	}

	public Name getVarName()
	{
		return this.varName;
	}

	public Expression getCollection()
	{
		return this.collection;
	}

	public boolean isLocal()
	{
		return this.local;
	}

}
