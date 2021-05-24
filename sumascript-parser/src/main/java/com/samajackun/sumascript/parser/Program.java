package com.samajackun.sumascript.parser;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.samajackun.rodas.core.eval.Name;
import com.samajackun.rodas.core.eval.functions.Function;
import com.samajackun.sumascript.core.Namespace;
import com.samajackun.sumascript.core.SumaInstructionSerializerException;
import com.samajackun.sumascript.core.instructions.BlockInstruction;
import com.samajackun.sumascript.core.instructions.SumaInstructionSerializer;
import com.samajackun.sumascript.core.runtime.NamedCodedFunction;

public class Program implements Serializable
{
	private static final long serialVersionUID=1532897221852451925L;

	private final BlockInstruction block;

	private final Set<Namespace> namespaceReferences=new HashSet<>();

	private final Map<Name, Function> functions=new HashMap<>();

	public Program(BlockInstruction block)
	{
		this(block, Collections.emptyList());
	}

	public Program(BlockInstruction block, List<NamedCodedFunction> namedCodedFunctions)
	{
		super();
		this.block=block;
		namedCodedFunctions.stream().forEach(f -> this.functions.put(f.getName(), f));
	}

	public BlockInstruction getBlock()
	{
		return this.block;
	}

	public Set<Namespace> getNamespaceReferences()
	{
		return this.namespaceReferences;
	}

	public Map<Name, Function> getFunctions()
	{
		return this.functions;
	}

	public String toCode(SumaInstructionSerializer serializer)
		throws SumaInstructionSerializerException
	{
		return this.block.toCode(serializer);
	}
}
