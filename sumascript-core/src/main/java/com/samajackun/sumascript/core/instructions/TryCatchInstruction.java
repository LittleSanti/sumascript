package com.samajackun.sumascript.core.instructions;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.Name;
import com.samajackun.sumascript.core.ExecutionException;
import com.samajackun.sumascript.core.Instruction;
import com.samajackun.sumascript.core.Jump;
import com.samajackun.sumascript.core.SumaInstructionSerializerException;

public class TryCatchInstruction implements Instruction
{
	private static final long serialVersionUID=1954305392889278588L;

	private final Instruction tryInstruction;

	private final Instruction catchInstruction;

	private final Name exceptionName;

	public TryCatchInstruction(Instruction tryInstruction, Instruction catchInstruction, Name exceptionName)
	{
		super();
		this.tryInstruction=tryInstruction;
		this.catchInstruction=catchInstruction;
		this.exceptionName=exceptionName;
	}

	@Override
	public Jump execute(Context context)
		throws ExecutionException
	{
		Jump jump=this.tryInstruction.execute(context);
		if (jump.isThrow())
		{
			context.getVariablesManager().setLocalVariable(this.exceptionName, jump.getExpression());
			jump=this.catchInstruction.execute(context);
			context.getVariablesManager().removeLocalVariable(this.exceptionName);
		}
		return jump;
	}

	@Override
	public String toCode(SumaInstructionSerializer serializer)
		throws SumaInstructionSerializerException
	{
		return serializer.serializeTryCatch(this);
	}

	public Instruction getTryInstruction()
	{
		return this.tryInstruction;
	}

	public Instruction getCatchInstruction()
	{
		return this.catchInstruction;
	}

	public Name getExceptionName()
	{
		return this.exceptionName;
	}
}
