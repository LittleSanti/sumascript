package com.samajackun.sumascript.core.runtime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.samajackun.rodas.core.eval.Name;
import com.samajackun.rodas.core.eval.VariableNotFoundException;
import com.samajackun.rodas.core.eval.VariablesManager;

public class FlexibleVariablesManagerTest
{
	@Test
	public void getUnexistingGlobalVariable()
	{
		VariablesManager variablesManager=new FlexibleVariablesManager(new FlexibleVariablesContext());
		Name varName=Name.instanceOf("month");
		try
		{
			assertSame(Undefined.getInstance(), variablesManager.getGlobalVariable(varName));
		}
		catch (VariableNotFoundException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void getExistingGlobalVariable()
	{
		VariablesManager variablesManager=new FlexibleVariablesManager(new FlexibleVariablesContext());
		Name varName=Name.instanceOf("month");
		variablesManager.setGlobalVariable(varName, "january");
		try
		{
			assertEquals("january", variablesManager.getGlobalVariable(varName));
		}
		catch (VariableNotFoundException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void overwriteGlobalVariable()
	{
		VariablesManager variablesManager=new FlexibleVariablesManager(new FlexibleVariablesContext());
		Name varName=Name.instanceOf("month");
		variablesManager.setGlobalVariable(varName, "january");
		variablesManager.setGlobalVariable(varName, "february");
		try
		{
			assertEquals("february", variablesManager.getGlobalVariable(varName));
		}
		catch (VariableNotFoundException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void removeGlobalVariable()
	{
		VariablesManager variablesManager=new FlexibleVariablesManager(new FlexibleVariablesContext());
		Name varName=Name.instanceOf("month");
		variablesManager.setGlobalVariable(varName, "january");
		variablesManager.removeGlobalVariable(varName);
		try
		{
			assertSame(Undefined.getInstance(), variablesManager.getGlobalVariable(varName));
		}
		catch (VariableNotFoundException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void getUnexistingLocalVariable()
	{
		VariablesManager variablesManager=new FlexibleVariablesManager(new FlexibleVariablesContext());
		variablesManager.pushLocalContext(new FlexibleVariablesContext());
		Name varName=Name.instanceOf("day");
		try
		{
			assertSame(Undefined.getInstance(), variablesManager.getLocalVariable(varName));
		}
		catch (VariableNotFoundException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void getExistingLocalVariable()
	{
		VariablesManager variablesManager=new FlexibleVariablesManager(new FlexibleVariablesContext());
		variablesManager.pushLocalContext(new FlexibleVariablesContext());
		Name varName=Name.instanceOf("day");
		variablesManager.setLocalVariable(varName, "monday");
		try
		{
			assertEquals("monday", variablesManager.getLocalVariable(varName));
		}
		catch (VariableNotFoundException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void overwriteLocalVariable()
	{
		VariablesManager variablesManager=new FlexibleVariablesManager(new FlexibleVariablesContext());
		variablesManager.pushLocalContext(new FlexibleVariablesContext());
		Name varName=Name.instanceOf("day");
		variablesManager.setLocalVariable(varName, "monday");
		variablesManager.setLocalVariable(varName, "tuesday");
		try
		{
			assertEquals("tuesday", variablesManager.getLocalVariable(varName));
		}
		catch (VariableNotFoundException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void removeLocalVariable()
	{
		VariablesManager variablesManager=new FlexibleVariablesManager(new FlexibleVariablesContext());
		variablesManager.pushLocalContext(new FlexibleVariablesContext());
		Name varName=Name.instanceOf("day");
		variablesManager.setLocalVariable(varName, "monday");
		variablesManager.removeLocalVariable(varName);
		try
		{
			assertSame(Undefined.getInstance(), variablesManager.getLocalVariable(varName));
		}
		catch (VariableNotFoundException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
		variablesManager.popLocalContext();
	}

	@Test
	public void twoManagersOfLocalVariables()
	{
		try
		{
			VariablesManager variablesManager=new FlexibleVariablesManager(new FlexibleVariablesContext());
			variablesManager.pushLocalContext(new FlexibleVariablesContext());
			Name varName1=Name.instanceOf("day");
			variablesManager.setLocalVariable(varName1, "monday");
			assertEquals("monday", variablesManager.getLocalVariable(varName1));
			Name varName2=Name.instanceOf("month");
			variablesManager.setLocalVariable(varName2, "january");
			assertEquals("january", variablesManager.getLocalVariable(varName2));

			variablesManager.pushLocalContext(new FlexibleVariablesContext());
			variablesManager.setLocalVariable(varName1, "tuesday");
			assertEquals("tuesday", variablesManager.getLocalVariable(varName1));
			assertSame(Undefined.getInstance(), variablesManager.getGlobalVariable(varName2));
			variablesManager.popLocalContext();

			assertEquals("monday", variablesManager.getLocalVariable(varName1));
			assertEquals("january", variablesManager.getLocalVariable(varName2));

			variablesManager.popLocalContext();
		}
		catch (VariableNotFoundException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}
}
