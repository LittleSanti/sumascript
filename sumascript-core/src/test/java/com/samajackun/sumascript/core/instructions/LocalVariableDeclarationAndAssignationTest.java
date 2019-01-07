package com.samajackun.sumascript.core.instructions;

import org.junit.Assert;
import org.junit.Test;

import com.samajackun.rodas.core.eval.VariableNotFoundException;
import com.samajackun.rodas.core.model.Expression;
import com.samajackun.rodas.core.model.TextConstantExpression;
import com.samajackun.sumascript.core.ExecutionException;
import com.samajackun.sumascript.core.expression.MyContext;
import com.samajackun.sumascript.core.instructions.LocalVariableDeclarationAndAssignation;
import com.samajackun.sumascript.core.runtime.Undefined;

public class LocalVariableDeclarationAndAssignationTest
{

	@Test
	public void execute()
	{
		try
		{
			String varName="month";
			Expression expression=new TextConstantExpression("january");
			MyContext context=new MyContext();
			context.getVariablesManager().pushLocalContext();
			Assert.assertSame(Undefined.getInstance(), context.getVariablesManager().getLocalVariable(varName));
			LocalVariableDeclarationAndAssignation localVariableAssignation=new LocalVariableDeclarationAndAssignation(varName, expression);
			localVariableAssignation.execute(context);
			Assert.assertEquals("january", context.getVariablesManager().getLocalVariable(varName));
			context.getVariablesManager().popLocalContext();
		}
		catch (ExecutionException | VariableNotFoundException e)
		{
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}

}
