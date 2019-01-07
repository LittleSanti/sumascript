package com.samajackun.sumascript.core.instructions;

import com.samajackun.rodas.core.model.Expression;

public class LocalVariableDeclarationAndAssignation extends LocalVariableAssignation
{
	public LocalVariableDeclarationAndAssignation(String name, Expression expression)
	{
		super(name, expression);
	}
}
