package com.samajackun.sumascript.core.expression;

import java.io.File;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.IndexNotBoundException;
import com.samajackun.rodas.core.eval.MapList;
import com.samajackun.rodas.core.eval.NameNotBoundException;
import com.samajackun.rodas.core.eval.Runtime;
import com.samajackun.rodas.core.eval.VariableNotFoundException;
import com.samajackun.rodas.core.eval.VariablesManager;
import com.samajackun.rodas.core.execution.Cursor;
import com.samajackun.rodas.core.execution.CursorException;
import com.samajackun.rodas.core.model.Provider;
import com.samajackun.sumascript.core.runtime.FlexibleVariablesContext;
import com.samajackun.sumascript.core.runtime.FlexibleVariablesManager;
import com.samajackun.sumascript.core.runtime.ScriptRuntime;

public class MyContext implements Context
{
	private final ScriptRuntime runtime=new ScriptRuntime(new File(System.getProperty("user.dir")));

	private final VariablesManager variablesManager=new FlexibleVariablesManager(new FlexibleVariablesContext());

	@Override
	public Context fork(Context arg0)
	{
		return null;
	}

	@Override
	public Object getColumnByIndex(int arg0)
		throws IndexNotBoundException,
		CursorException
	{
		return null;
	}

	@Override
	public Object getColumnByName(String varName)
		throws NameNotBoundException,
		CursorException
	{
		return getColumnByName(varName, null);
	}

	@Override
	public Object getColumnByName(String varName, String prefix)
		throws NameNotBoundException,
		CursorException
	{
		// Esta implementación es sólo para ir probando el SumaParser.
		try
		{
			return getVariablesManager().getLocalVariable(varName);
		}
		catch (VariableNotFoundException e)
		{
			throw new NameNotBoundException(varName);
		}
	}

	@Override
	public int getColumnIndexByName(String arg0, String arg1)
		throws NameNotBoundException
	{
		return 0;
	}

	@Override
	public MapList<String, Cursor> getCursors()
	{
		return null;
	}

	@Override
	public Runtime getRuntime()
	{
		return this.runtime;
	}

	public ScriptRuntime getScriptRuntime()
	{
		return this.runtime;
	}

	@Override
	public VariablesManager getVariablesManager()
	{
		return this.variablesManager;
	}

	@Override
	public Provider getProvider()
	{
		return null;
	}
}
