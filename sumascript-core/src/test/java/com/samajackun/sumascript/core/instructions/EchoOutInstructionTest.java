package com.samajackun.sumascript.core.instructions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.junit.jupiter.api.Test;

import com.samajackun.rodas.core.model.Expression;
import com.samajackun.rodas.core.model.TextConstantExpression;
import com.samajackun.sumascript.core.ExecutionException;
import com.samajackun.sumascript.core.expression.MyContext;

public class EchoOutInstructionTest
{

	@Test
	public void execute()
	{
		Expression expression=new TextConstantExpression("january");
		EchoOutInstruction echoOut=new EchoOutInstruction(expression);
		MyContext context=new MyContext();
		StringWriter out=new StringWriter();
		context.getRuntime().setOut(new PrintWriter(out));
		try
		{
			String NL=System.getProperty("line.separator");
			echoOut.execute(context);
			assertEquals("january" + NL, out.toString());
		}
		catch (ExecutionException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}

	}

}
