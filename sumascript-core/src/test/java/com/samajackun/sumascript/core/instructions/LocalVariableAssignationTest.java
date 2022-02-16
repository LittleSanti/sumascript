package com.samajackun.sumascript.core.instructions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import com.samajackun.rodas.core.eval.Name;
import com.samajackun.rodas.core.eval.VariableNotFoundException;
import com.samajackun.rodas.core.model.Expression;
import com.samajackun.rodas.core.model.TextConstantExpression;
import com.samajackun.sumascript.core.ExecutionException;
import com.samajackun.sumascript.core.expression.MyContext;
import com.samajackun.sumascript.core.runtime.FlexibleVariablesContext;
import com.samajackun.sumascript.core.runtime.Undefined;

public class LocalVariableAssignationTest
{

	@Test
	public void execute()
	{
		try
		{
			Name varName=Name.instanceOf("month");
			Expression expression=new TextConstantExpression("january");
			MyContext context=new MyContext();
			context.getVariablesManager().pushLocalContext(new FlexibleVariablesContext());
			assertSame(Undefined.getInstance(), context.getVariablesManager().getLocalVariable(varName));
			LocalVariableAssignation localVariableAssignation=new LocalVariableAssignation(varName, expression);
			localVariableAssignation.execute(context);
			assertEquals("january", context.getVariablesManager().getLocalVariable(varName));
			context.getVariablesManager().popLocalContext();
		}
		catch (ExecutionException | VariableNotFoundException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

}
