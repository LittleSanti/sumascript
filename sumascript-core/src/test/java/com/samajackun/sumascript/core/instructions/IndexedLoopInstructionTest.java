package com.samajackun.sumascript.core.instructions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.samajackun.rodas.core.eval.Name;
import com.samajackun.rodas.core.model.AddExpression;
import com.samajackun.rodas.core.model.EqualsExpression;
import com.samajackun.rodas.core.model.Expression;
import com.samajackun.rodas.core.model.LowerThanExpression;
import com.samajackun.rodas.core.model.NumericConstantExpression;
import com.samajackun.sumascript.core.ExecutionException;
import com.samajackun.sumascript.core.Instruction;
import com.samajackun.sumascript.core.Jump;
import com.samajackun.sumascript.core.expression.MyContext;
import com.samajackun.sumascript.core.expressions.NearestVariableExpression;
import com.samajackun.sumascript.core.jumps.NoJump;
import com.samajackun.sumascript.core.runtime.FlexibleVariablesContext;

public class IndexedLoopInstructionTest
{
	@Test
	public void executeZeroTimes()
	{
		MyContext context=new MyContext();
		StringWriter out=new StringWriter();
		context.getRuntime().setOut(new PrintWriter(out, true));
		List<AbstractVariableAssignation> assignations=Arrays.asList(new AbstractVariableAssignation[] { new LocalVariableDeclarationAndAssignation(Name.instanceOf("i"), new NumericConstantExpression("0", 0)) });
		Expression loopingCondition=new LowerThanExpression("<", new NearestVariableExpression(Name.instanceOf("i")), new NumericConstantExpression("0", 0));
		Instruction stepInstruction=new EchoOutInstruction(new NearestVariableExpression(Name.instanceOf("i")));
		List<Instruction> postStepInstructions=Arrays.asList(new LocalVariableAssignation(Name.instanceOf("i"), new AddExpression("+", new NumericConstantExpression("1", 1), new NearestVariableExpression(Name.instanceOf("i")))));
		IndexedLoopInstruction instruction=new IndexedLoopInstruction(assignations, loopingCondition, stepInstruction, postStepInstructions);
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
	public void executeOnce()
	{
		MyContext context=new MyContext();
		StringWriter out=new StringWriter();
		context.getRuntime().setOut(new PrintWriter(out, true));
		List<AbstractVariableAssignation> assignations=Arrays.asList(new AbstractVariableAssignation[] { new LocalVariableDeclarationAndAssignation(Name.instanceOf("i"), new NumericConstantExpression("0", 0)) });
		Expression loopingCondition=new LowerThanExpression("<", new NearestVariableExpression(Name.instanceOf("i")), new NumericConstantExpression("1", 1));
		Instruction stepInstruction=new EchoOutInstruction(new NearestVariableExpression(Name.instanceOf("i")));
		List<Instruction> postStepInstructions=Arrays.asList(new LocalVariableAssignation(Name.instanceOf("i"), new AddExpression("+", new NumericConstantExpression("1", 1), new NearestVariableExpression(Name.instanceOf("i")))));
		IndexedLoopInstruction instruction=new IndexedLoopInstruction(assignations, loopingCondition, stepInstruction, postStepInstructions);
		try
		{
			context.getVariablesManager().pushLocalContext(new FlexibleVariablesContext());
			Jump jump=instruction.execute(context);
			assertSame(NoJump.getInstance(), jump);
			context.getVariablesManager().popLocalContext();
			assertEquals("0\r\n", out.toString());
		}
		catch (ExecutionException e)
		{
			fail(e.toString());
			e.printStackTrace();
		}
	}

	@Test
	public void executeSeveralTimes()
	{
		MyContext context=new MyContext();
		StringWriter out=new StringWriter();
		context.getRuntime().setOut(new PrintWriter(out, true));
		List<AbstractVariableAssignation> assignations=Arrays.asList(new AbstractVariableAssignation[] { new LocalVariableDeclarationAndAssignation(Name.instanceOf("i"), new NumericConstantExpression("0", 0)) });
		Expression loopingCondition=new LowerThanExpression("<", new NearestVariableExpression(Name.instanceOf("i")), new NumericConstantExpression("5", 5));
		Instruction stepInstruction=new EchoOutInstruction(new NearestVariableExpression(Name.instanceOf("i")));
		List<Instruction> postStepInstructions=Arrays.asList(new LocalVariableAssignation(Name.instanceOf("i"), new AddExpression("+", new NumericConstantExpression("1", 1), new NearestVariableExpression(Name.instanceOf("i")))));
		IndexedLoopInstruction instruction=new IndexedLoopInstruction(assignations, loopingCondition, stepInstruction, postStepInstructions);
		try
		{
			context.getVariablesManager().pushLocalContext(new FlexibleVariablesContext());
			Jump jump=instruction.execute(context);
			assertSame(NoJump.getInstance(), jump);
			context.getVariablesManager().popLocalContext();
			assertEquals("0\r\n1\r\n2\r\n3\r\n4\r\n", out.toString());
		}
		catch (ExecutionException e)
		{
			fail(e.toString());
			e.printStackTrace();
		}
	}

	@Test
	public void executeAndBreakInTheMiddle()
	{
		MyContext context=new MyContext();
		StringWriter out=new StringWriter();
		context.getRuntime().setOut(new PrintWriter(out, true));
		List<AbstractVariableAssignation> assignations=Arrays.asList(new AbstractVariableAssignation[] { new LocalVariableDeclarationAndAssignation(Name.instanceOf("i"), new NumericConstantExpression("0", 0)) });
		Expression loopingCondition=new LowerThanExpression("<", new NearestVariableExpression(Name.instanceOf("i")), new NumericConstantExpression("5", 5));
		Instruction stepInstruction=new BlockInstruction(
		// @formatter:off
			Arrays.<Instruction>asList(
				new EchoOutInstruction(new NearestVariableExpression(Name.instanceOf("i"))),
				new IfInstruction(new EqualsExpression("=", new NearestVariableExpression(Name.instanceOf("i")), new NumericConstantExpression("3", 3)), BreakInstruction.getInstance(), null)
			)
		// @formatter:on
		);
		List<Instruction> postStepInstructions=Arrays.asList(new LocalVariableAssignation(Name.instanceOf("i"), new AddExpression("+", new NumericConstantExpression("1", 1), new NearestVariableExpression(Name.instanceOf("i")))));
		IndexedLoopInstruction instruction=new IndexedLoopInstruction(assignations, loopingCondition, stepInstruction, postStepInstructions);
		try
		{
			context.getVariablesManager().pushLocalContext(new FlexibleVariablesContext());
			Jump jump=instruction.execute(context);
			assertSame(NoJump.getInstance(), jump);
			context.getVariablesManager().popLocalContext();
			assertEquals("0\r\n1\r\n2\r\n3\r\n", out.toString());
		}
		catch (ExecutionException e)
		{
			fail(e.toString());
			e.printStackTrace();
		}
	}
}
