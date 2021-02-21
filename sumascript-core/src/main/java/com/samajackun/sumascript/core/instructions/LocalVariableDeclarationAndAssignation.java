package com.samajackun.sumascript.core.instructions;

import com.samajackun.rodas.core.eval.Name;
import com.samajackun.rodas.core.model.Expression;

public class LocalVariableDeclarationAndAssignation extends LocalVariableAssignation
{
	private static final long serialVersionUID=-2746938954686777914L;

	public LocalVariableDeclarationAndAssignation(Name name, Expression expression)
	{
		super(name, expression);
	}

	public LocalVariableDeclarationAndAssignation(Name name)
	{
		super(name, null);
	}
}
