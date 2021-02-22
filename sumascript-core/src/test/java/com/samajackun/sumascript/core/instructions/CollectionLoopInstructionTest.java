package com.samajackun.sumascript.core.instructions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.junit.Test;

import com.samajackun.rodas.core.eval.Name;
import com.samajackun.rodas.core.model.ListConstantExpression;
import com.samajackun.rodas.core.model.TextConstantExpression;
import com.samajackun.sumascript.core.ExecutionException;
import com.samajackun.sumascript.core.Instruction;
import com.samajackun.sumascript.core.Jump;
import com.samajackun.sumascript.core.expression.MyContext;
import com.samajackun.sumascript.core.expressions.MapExpression;
import com.samajackun.sumascript.core.expressions.NearestVariableExpression;
import com.samajackun.sumascript.core.jumps.NoJump;
import com.samajackun.sumascript.core.runtime.FlexibleVariablesContext;

public class CollectionLoopInstructionTest
{
	@Test
	public void executeFromEmptyCollection()
	{
		MyContext context=new MyContext();
		StringWriter out=new StringWriter();
		context.getRuntime().setOut(new PrintWriter(out, true));
		Name varName=Name.instanceOf("a");
		Instruction stepInstruction=new EchoOutInstruction(new NearestVariableExpression(varName));
		ListConstantExpression varExpression=new ListConstantExpression();
		CollectionLoopInstruction instruction=new CollectionLoopInstruction(null, stepInstruction, null, varName, varExpression);
		try
		{
			context.getVariablesManager().pushLocalContext(new FlexibleVariablesContext());
			Jump jump=instruction.execute(context);
			assertSame(NoJump.getInstance(), jump);
			context.getVariablesManager().popLocalContext();
			assertEquals("", out.toString());
		}
		catch (ExecutionException e)
		{
			fail(e.toString());
			e.printStackTrace();
		}
	}

	@Test
	public void executeFromThreeItemCollection()
	{
		MyContext context=new MyContext();
		StringWriter out=new StringWriter();
		context.getRuntime().setOut(new PrintWriter(out, true));
		Name varName=Name.instanceOf("a");
		Instruction stepInstruction=new EchoOutInstruction(new NearestVariableExpression(varName));
		ListConstantExpression varExpression=new ListConstantExpression();
		varExpression.add(new TextConstantExpression("enero"));
		varExpression.add(new TextConstantExpression("febrero"));
		varExpression.add(new TextConstantExpression("marzo"));
		CollectionLoopInstruction instruction=new CollectionLoopInstruction(null, stepInstruction, null, varName, varExpression);
		try
		{
			context.getVariablesManager().pushLocalContext(new FlexibleVariablesContext());
			Jump jump=instruction.execute(context);
			assertSame(NoJump.getInstance(), jump);
			context.getVariablesManager().popLocalContext();
			assertEquals("enero\r\nfebrero\r\nmarzo\r\n", out.toString());
		}
		catch (ExecutionException e)
		{
			fail(e.toString());
			e.printStackTrace();
		}
	}

	@Test
	public void executeFromAThreeItemMap()
	{
		MyContext context=new MyContext();
		StringWriter out=new StringWriter();
		context.getRuntime().setOut(new PrintWriter(out, true));
		Name varName=Name.instanceOf("a");
		Instruction stepInstruction=new EchoOutInstruction(new NearestVariableExpression(varName));
		MapExpression varExpression=new MapExpression();
		varExpression.put("dia", new TextConstantExpression("lunes"));
		varExpression.put("mes", new TextConstantExpression("febrero"));
		varExpression.put("planeta", new TextConstantExpression("mercurio"));
		CollectionLoopInstruction instruction=new CollectionLoopInstruction(null, stepInstruction, null, varName, varExpression);
		try
		{
			context.getVariablesManager().pushLocalContext(new FlexibleVariablesContext());
			Jump jump=instruction.execute(context);
			assertSame(NoJump.getInstance(), jump);
			context.getVariablesManager().popLocalContext();
			assertEquals("dia\r\nmes\r\nplaneta\r\n", out.toString());
		}
		catch (ExecutionException e)
		{
			fail(e.toString());
			e.printStackTrace();
		}
	}

	@Test
	public void executeFromASingleConstant()
	{
		MyContext context=new MyContext();
		StringWriter out=new StringWriter();
		context.getRuntime().setOut(new PrintWriter(out, true));
		Name varName=Name.instanceOf("a");
		Instruction stepInstruction=new EchoOutInstruction(new NearestVariableExpression(varName));
		TextConstantExpression varExpression=new TextConstantExpression("enero");
		CollectionLoopInstruction instruction=new CollectionLoopInstruction(null, stepInstruction, null, varName, varExpression);
		try
		{
			context.getVariablesManager().pushLocalContext(new FlexibleVariablesContext());
			Jump jump=instruction.execute(context);
			assertSame(NoJump.getInstance(), jump);
			context.getVariablesManager().popLocalContext();
			assertEquals("enero\r\n", out.toString());
		}
		catch (ExecutionException e)
		{
			fail(e.toString());
			e.printStackTrace();
		}
	}
}
