package com.samajackun.sumascript.parser;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.samajackun.rodas.core.eval.functions.Function;
import com.samajackun.sumascript.core.Namespace;
import com.samajackun.sumascript.core.instructions.BlockInstruction;

public class Program
{
	private final BlockInstruction block;

	private final Set<Namespace> namespaceReferences=new HashSet<>();

	private final Map<String, Function> functions=new HashMap<>();

	public Program(BlockInstruction block)
	{
		super();
		this.block=block;
	}

	public BlockInstruction getBlock()
	{
		return this.block;
	}

	public Set<Namespace> getNamespaceReferences()
	{
		return this.namespaceReferences;
	}

	public Map<String, Function> getFunctions()
	{
		return this.functions;
	}
}
