package com.samajackun.sumascript.core.runtime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.samajackun.rodas.core.eval.StrictVariablesManager;
import com.samajackun.rodas.core.eval.VariableNotFoundException;
import com.samajackun.rodas.core.eval.VariablesManager;

public class StrictVariablesManagerTest
{
	@Test
	public void getUnexistingGlobalVariable()
	{
		VariablesManager variablesManager=new StrictVariablesManager();
		String varName="month";
		try
		{
			variablesManager.getGlobalVariable(varName);
			fail("Expected VariableNotFoundException");
		}
		catch (VariableNotFoundException e)
		{
			assertEquals(varName, e.getName());
		}
	}

	@Test
	public void getExistingGlobalVariable()
	{
		VariablesManager variablesManager=new StrictVariablesManager();
		String varName="month";
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
		VariablesManager variablesManager=new StrictVariablesManager();
		String varName="month";
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
		VariablesManager variablesManager=new StrictVariablesManager();
		String varName="month";
		variablesManager.setGlobalVariable(varName, "january");
		variablesManager.removeGlobalVariable(varName);
		try
		{
			variablesManager.getGlobalVariable(varName);
			fail("Expected VariableNotFoundException");
		}
		catch (VariableNotFoundException e)
		{
			assertEquals(varName, e.getName());
		}
	}

	@Test
	public void getUnexistingLocalVariable()
	{
		VariablesManager variablesManager=new StrictVariablesManager();
		variablesManager.pushLocalContext();
		String varName="day";
		try
		{
			variablesManager.getLocalVariable(varName);
			fail("Expected VariableNotFoundException");
		}
		catch (VariableNotFoundException e)
		{
			assertEquals(varName, e.getName());
		}
	}

	@Test
	public void getExistingLocalVariable()
	{
		VariablesManager variablesManager=new StrictVariablesManager();
		variablesManager.pushLocalContext();
		String varName="day";
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
		VariablesManager variablesManager=new StrictVariablesManager();
		variablesManager.pushLocalContext();
		String varName="day";
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
		VariablesManager variablesManager=new StrictVariablesManager();
		variablesManager.pushLocalContext();
		String varName="day";
		variablesManager.setLocalVariable(varName, "monday");
		variablesManager.removeLocalVariable(varName);
		try
		{
			variablesManager.getLocalVariable(varName);
			fail("Expected VariableNotFoundException");
		}
		catch (VariableNotFoundException e)
		{
			assertEquals(varName, e.getName());
		}
		variablesManager.popLocalContext();
	}

	@Test
	public void twoManagersOfLocalVariables()
	{
		try
		{
			VariablesManager variablesManager=new StrictVariablesManager();
			variablesManager.pushLocalContext();
			String varName1="day";
			variablesManager.setLocalVariable(varName1, "monday");
			assertEquals("monday", variablesManager.getLocalVariable(varName1));
			String varName2="month";
			variablesManager.setLocalVariable(varName2, "january");
			assertEquals("january", variablesManager.getLocalVariable(varName2));

			variablesManager.pushLocalContext();
			variablesManager.setLocalVariable(varName1, "tuesday");
			assertEquals("tuesday", variablesManager.getLocalVariable(varName1));
			try
			{
				variablesManager.getLocalVariable(varName2);
				fail("Expected VariableNotFoundException");
			}
			catch (VariableNotFoundException e)
			{
				assertEquals(varName2, e.getName());
			}
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
