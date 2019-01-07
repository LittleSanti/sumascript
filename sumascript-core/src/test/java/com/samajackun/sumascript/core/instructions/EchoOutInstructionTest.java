package com.samajackun.sumascript.core.instructions;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.junit.Assert;
import org.junit.Test;

import com.samajackun.rodas.core.model.Expression;
import com.samajackun.rodas.core.model.TextConstantExpression;
import com.samajackun.sumascript.core.ExecutionException;
import com.samajackun.sumascript.core.expression.MyContext;
import com.samajackun.sumascript.core.instructions.EchoOutInstruction;

public class EchoOutInstructionTest
{

	@Test
	public void execute()
	{
		Expression expression=new TextConstantExpression("january");
		EchoOutInstruction echoOut=new EchoOutInstruction(expression);
		MyContext context=new MyContext();
		StringWriter out=new StringWriter();
		context.getScriptRuntime().setOut(new PrintWriter(out));
		try
		{
			String NL=System.getProperty("line.separator");
			echoOut.execute(context);
			Assert.assertEquals("january" + NL, out.toString());
		}
		catch (ExecutionException e)
		{
			e.printStackTrace();
			Assert.fail(e.toString());
		}

	}

}
