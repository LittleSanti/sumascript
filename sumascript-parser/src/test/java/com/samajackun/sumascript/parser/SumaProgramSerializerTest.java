package com.samajackun.sumascript.parser;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.samajackun.rodas.core.model.NumericConstantExpression;
import com.samajackun.sumascript.core.Instruction;
import com.samajackun.sumascript.core.SumaInstructionSerializerException;
import com.samajackun.sumascript.core.instructions.BlockInstruction;
import com.samajackun.sumascript.core.instructions.EchoOutInstruction;
import com.samajackun.sumascript.serializer.SumaCodeInstructionSerializer;

public class SumaProgramSerializerTest
{
	// private Program parseToProgram(String code)
	// throws IOException
	// {
	// try
	// {
	// Program program=SumaParser.getInstance().parse(new StringReader(code));
	// return program;
	// }
	// catch (SumaParseException e)
	// {
	// e.printStackTrace();
	// fail(e.toString());
	// return null;
	// }
	// }

	@Test
	public void block1()
	{
		List<Instruction> instructions=new ArrayList<>();
		instructions.add(new EchoOutInstruction(new NumericConstantExpression("120", 120L)));
		BlockInstruction block=new BlockInstruction(instructions);
		try
		{
			System.out.println(block.toCode(SumaCodeInstructionSerializer.getInstance()));
		}
		catch (SumaInstructionSerializerException e)
		{
			fail(e.toString());
		}
	}

}
