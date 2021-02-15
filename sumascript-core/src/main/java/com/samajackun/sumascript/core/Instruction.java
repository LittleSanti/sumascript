package com.samajackun.sumascript.core;

import java.io.Serializable;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.sumascript.core.instructions.SumaInstructionSerializer;

public interface Instruction extends Serializable
{
	public Jump execute(Context context)
		throws ExecutionException;

	public String toCode(SumaInstructionSerializer serializer)
		throws SumaInstructionSerializerException;
}
