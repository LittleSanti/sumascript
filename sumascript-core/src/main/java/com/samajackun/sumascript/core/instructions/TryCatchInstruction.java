package com.samajackun.sumascript.core.instructions;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.sumascript.core.ExecutionException;
import com.samajackun.sumascript.core.Instruction;
import com.samajackun.sumascript.core.Jump;

public class TryCatchInstruction implements Instruction
{
	private final Instruction tryInstruction;

	private final Instruction catchInstruction;

	private final String exceptionName;

	public TryCatchInstruction(Instruction tryInstruction, Instruction catchInstruction, String exceptionName)
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
}
