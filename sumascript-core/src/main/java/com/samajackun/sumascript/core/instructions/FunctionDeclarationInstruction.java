package com.samajackun.sumascript.core.instructions;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.sumascript.core.ExecutionException;
import com.samajackun.sumascript.core.Instruction;
import com.samajackun.sumascript.core.Jump;
import com.samajackun.sumascript.core.SumaInstructionSerializerException;
import com.samajackun.sumascript.core.jumps.NoJump;
import com.samajackun.sumascript.core.runtime.NamedCodedFunction;

public class FunctionDeclarationInstruction implements Instruction
{
	private static final long serialVersionUID=-4869462132467673157L;

	private final NamedCodedFunction codedFunction;

	public FunctionDeclarationInstruction(NamedCodedFunction codedFunction)
	{
		super();
		this.codedFunction=codedFunction;
	}

	public NamedCodedFunction getCodedFunction()
	{
		return this.codedFunction;
	}

	@Override
	public Jump execute(Context context)
		throws ExecutionException
	{
		// En el nivel de contexto más alto, localContext==globalContext.
		context.getVariablesManager().peekLocalContext().set(this.codedFunction.getName(), this.codedFunction);
		return NoJump.getInstance();
	}

	@Override
	public String toCode(SumaInstructionSerializer serializer)
		throws SumaInstructionSerializerException
	{
		return serializer.serializeFunctionDeclaration(this);
	}
}
