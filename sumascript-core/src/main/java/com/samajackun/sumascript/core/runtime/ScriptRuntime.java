package com.samajackun.sumascript.core.runtime;

import java.io.File;

import com.samajackun.rodas.core.eval.ProtectedRuntime;
import com.samajackun.sumascript.core.instructions.SystemObjects;

public class ScriptRuntime extends ProtectedRuntime
{
	public ScriptRuntime(File currentDir)
	{
		super();
		getSystemObjects().put(SystemObjects.CURRENT_DIR, currentDir);
	}
}
