package com.samajackun.sumascript.core.instructions;

import java.io.File;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.model.Expression;
import com.samajackun.sumascript.core.ExecutionException;
import com.samajackun.sumascript.core.Instruction;
import com.samajackun.sumascript.core.Jump;
import com.samajackun.sumascript.core.SumaInstructionSerializerException;
import com.samajackun.sumascript.core.jumps.NoJump;

public class ChangeCurrentDirectoryInstruction implements Instruction
{
	private static final long serialVersionUID=-5466003702265616007L;

	private final Expression newDirectory;

	public ChangeCurrentDirectoryInstruction(Expression newDirectory)
	{
		super();
		this.newDirectory=newDirectory;
	}

	@Override
	public Jump execute(Context context)
		throws ExecutionException
	{
		try
		{
			Object value=this.newDirectory.evaluate(context, context.getEvaluatorFactory());
			String valueStr=value == null
				? "null"
				: value.toString();
			File path=new File(valueStr);
			if (!path.exists())
			{
				throw new UnexistingPathException(path);
			}
			context.getRuntime().putSystemObject(SystemObjects.CURRENT_DIR, path);
			return NoJump.getInstance();
		}
		catch (EvaluationException e)
		{
			throw new ExecutionException(e);
		}
	}

	@Override
	public String toCode(SumaInstructionSerializer serializer)
		throws SumaInstructionSerializerException
	{
		return serializer.serializeChangeCurrentDirectory(this);
	}

	public Expression getNewDirectory()
	{
		return this.newDirectory;
	}
}
