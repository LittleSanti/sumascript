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
import com.samajackun.sumascript.core.instructions.BlockInstruction;
import com.samajackun.sumascript.core.instructions.EchoOutInstruction;
import com.samajackun.sumascript.parser.Program;

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
		Program program=new Program(block);
		ByteArrayOutputStream output=new ByteArrayOutputStream(8192);
		try
		{
			this.saver.save(program, output);
			byte[] data=output.toByteArray();
			ByteArrayInputStream input=new ByteArrayInputStream(data);
			Program program2=ProgramManager.getInstance().load(input, "my.ssc");
			assertEquals(program, program2);
		}
		catch (IOException | ProgramLoadException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}
}
