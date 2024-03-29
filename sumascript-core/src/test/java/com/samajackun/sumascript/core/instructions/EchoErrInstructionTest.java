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

public class EchoErrInstructionTest
{

	@Test
	public void execute()
	{
		Expression expression=new TextConstantExpression("january");
		EchoErrInstruction echoErr=new EchoErrInstruction(expression);
		MyContext context=new MyContext();
		StringWriter err=new StringWriter();
		context.getRuntime().setErr(new PrintWriter(err));
		try
		{
			String NL=System.getProperty("line.separator");
			echoErr.execute(context);
			assertEquals("january" + NL, err.toString());
		}
		catch (ExecutionException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}

	}

}
