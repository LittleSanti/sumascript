package com.samajackun.sumascript.core.expression;

import java.io.File;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.IndexNotBoundException;
import com.samajackun.rodas.core.eval.MapList;
import com.samajackun.rodas.core.eval.NameNotBoundException;
import com.samajackun.rodas.core.eval.Runtime;
import com.samajackun.rodas.core.eval.VariablesManager;
import com.samajackun.rodas.core.model.Cursor;
import com.samajackun.rodas.core.model.CursorException;
import com.samajackun.sumascript.core.runtime.FlexibleVariablesManager;
import com.samajackun.sumascript.core.runtime.ScriptRuntime;

public class MyContext implements Context
{
	private final ScriptRuntime runtime=new ScriptRuntime(new File(System.getProperty("user.dir")));

	private final VariablesManager variablesManager=new FlexibleVariablesManager();

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
	public Object getColumnByName(String arg0)
		throws NameNotBoundException,
		CursorException
	{
		return null;
	}

	@Override
	public Object getColumnByName(String arg0, String arg1)
		throws NameNotBoundException,
		CursorException
	{
		return null;
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
}
