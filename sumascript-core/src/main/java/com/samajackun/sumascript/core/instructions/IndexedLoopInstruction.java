package com.samajackun.sumascript.core.instructions;

import java.util.List;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.model.Expression;
import com.samajackun.sumascript.core.ExecutionException;
import com.samajackun.sumascript.core.Instruction;
import com.samajackun.sumascript.core.Jump;
import com.samajackun.sumascript.core.SumaInstructionSerializerException;
import com.samajackun.sumascript.core.jumps.NoJump;

public class IndexedLoopInstruction extends AbstractLoopInstruction
{
	private static final long serialVersionUID=6337991806370145295L;

	private final List<AbstractVariableAssignation> assignations;

	private final List<Instruction> postStepInstructions;

	public IndexedLoopInstruction(List<AbstractVariableAssignation> assignations, Expression loopingCondition, Instruction innerInstruction, List<Instruction> postStepInstructions)
	{
		super(loopingCondition, innerInstruction, null);
		this.assignations=assignations;
		this.postStepInstructions=postStepInstructions;
	}

	@Override
	protected Jump initializations(Context context)
		throws ExecutionException
	{
		Jump jump=NoJump.getInstance();
		for (AbstractVariableAssignation asssignation : this.assignations)
		{
			jump=asssignation.execute(context);
			if (jump.isExit() || jump.isThrow())
			{
				return jump;
			}
		}
		return jump;
	}

	@Override
	protected Jump postInstructions(Context context)
		throws ExecutionException
	{
		Jump jump=NoJump.getInstance();
		for (Instruction postStepInstruction : this.postStepInstructions)
		{
			jump=postStepInstruction.execute(context);
		}
		return jump;
	}

	@Override
	public String toCode(SumaInstructionSerializer serializer)
		throws SumaInstructionSerializerException
	{
		return serializer.serializeIndexedLoop(this);
	}

	public List<AbstractVariableAssignation> getAssignations()
	{
		return this.assignations;
	}

	public List<Instruction> getPostStepInstructions()
	{
		return this.postStepInstructions;
	}
}
