package com.samajackun.sumascript.parser;

import java.util.List;

import com.samajackun.rodas.core.model.Expression;
import com.samajackun.sumascript.core.Instruction;
import com.samajackun.sumascript.core.instructions.AbstractVariableAssignation;

public class VariablesLoopClause
{
	private final List<AbstractVariableAssignation> initializations;

	private final Expression condition;

	List<Instruction> postInstructions;

	public VariablesLoopClause(List<AbstractVariableAssignation> initializations, Expression condition, List<Instruction> postInstructions)
	{
		super();
		this.initializations=initializations;
		this.condition=condition;
		this.postInstructions=postInstructions;
	}

	public List<Instruction> getPostInstructions()
	{
		return this.postInstructions;
	}

	public void setPostInstructions(List<Instruction> postInstructions)
	{
		this.postInstructions=postInstructions;
	}

	public List<AbstractVariableAssignation> getInitializations()
	{
		return this.initializations;
	}

	public Expression getCondition()
	{
		return this.condition;
	}

}
