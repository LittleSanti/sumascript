package com.samajackun.sumascript.core;

import java.io.Serializable;

import com.samajackun.rodas.core.eval.Context;

public interface Instruction extends Serializable
{
	public Jump execute(Context context)
		throws ExecutionException;
}
