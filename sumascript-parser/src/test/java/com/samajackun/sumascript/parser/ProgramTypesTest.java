package com.samajackun.sumascript.parser;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.io.StringReader;

public class ProgramTypesTest
{
	private Program parseToProgram(String code)
	{
		try
		{
			Program program=SumaParser.getInstance().parse(new StringReader(code));
			return program;
		}
		catch (IOException | SumaParseException e)
		{
			e.printStackTrace();
			fail(e.toString());
			return null;
		}
	}

	// @Test
	// public void date()
	// {
	// String code="var a=new Date(2020, 1, 2)";
	// Program program=parseToProgram(code);
	// MyContext context=new MyContext();
	// context.getVariablesManager().pushLocalContext(new FlexibleVariablesContext());
	// try
	// {
	// program.getBlock().execute(context);
	// assertEquals(LocalDate.of(2020, 1, 2), context.getVariablesManager().getLocalVariable(Name.instanceOf("a")));
	// }
	// catch (VariableNotFoundException | ExecutionException e)
	// {
	// e.printStackTrace();
	// fail(e.toString());
	// }
	// context.getVariablesManager().popLocalContext();
	// }
}
