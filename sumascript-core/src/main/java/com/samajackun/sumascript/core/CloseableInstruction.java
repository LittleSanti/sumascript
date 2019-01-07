package com.samajackun.sumascript.core;

public interface CloseableInstruction extends Instruction
{
	public void close(Runtime runtime)
		throws ExecutionException;
}
