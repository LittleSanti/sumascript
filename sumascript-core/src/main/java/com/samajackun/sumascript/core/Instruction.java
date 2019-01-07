package com.samajackun.sumascript.core;

import com.samajackun.rodas.core.eval.Context;

public interface Instruction
{
	public Jump execute(Context context)
		throws ExecutionException;
}
