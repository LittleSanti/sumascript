package com.samajackun.sumascript.program;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.samajackun.rodas.core.model.NumericConstantExpression;
import com.samajackun.sumascript.core.Instruction;
import com.samajackun.sumascript.core.SumaInstructionSerializerException;
import com.samajackun.sumascript.core.instructions.BlockInstruction;
import com.samajackun.sumascript.core.instructions.EchoOutInstruction;
import com.samajackun.sumascript.parser.Program;
import com.samajackun.sumascript.serializer.SumaCodeInstructionSerializer;

public abstract class AbstractProgramLoaderTest
{
	private final ProgramLoader saver;

	protected AbstractProgramLoaderTest(ProgramLoader saver)
	{
		super();
		this.saver=saver;
	}

	@Test
	public void loadAndSave()
	{
		List<Instruction> instructions=new ArrayList<>();
		instructions.add(new EchoOutInstruction(new NumericConstantExpression("120", 120L)));
		BlockInstruction block=new BlockInstruction(instructions);
		Program program1=new Program(block);
		ByteArrayOutputStream output=new ByteArrayOutputStream(8192);
		try
		{
			String codeProgram1=program1.toCode(SumaCodeInstructionSerializer.getInstance());
			this.saver.save(program1, output);
			byte[] data=output.toByteArray();
			ByteArrayInputStream input=new ByteArrayInputStream(data);
			Program program2=ProgramManager.getInstance().load(input, "my.ssc");
			String codeProgram2=program2.toCode(SumaCodeInstructionSerializer.getInstance());
			System.out.println(codeProgram1);

			assertEquals(codeProgram1, codeProgram2);
		}
		catch (IOException | ProgramLoadException | SumaInstructionSerializerException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}
}
