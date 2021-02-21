package com.samajackun.sumascript.engine.cmd;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.sumascript.core.expression.MyContext;
import com.samajackun.sumascript.core.runtime.ScriptRuntime;
import com.samajackun.sumascript.parser.Program;
import com.samajackun.sumascript.program.ProgramManager;

public class SumaEngine
{
	private static Context createContext()
	{
		MyContext context=new MyContext();
		ScriptRuntime runtime=new ScriptRuntime(new File("."));
		runtime.setOut(new PrintWriter(System.out, true));
		runtime.setErr(new PrintWriter(System.err, true));
		context.setRuntime(runtime);
		return context;
	}

	public static void main(String[] args)
		throws Throwable
	{
		if (args.length < 0)
		{
			System.err.println("Execute a Sumascript file");
			System.err.println("Usage: <script>");
		}
		else
		{
			int n=0;
			File script=new File(args[n++]);
			System.out.println("Sumascript engine: Executing " + script);
			try (InputStream input=new FileInputStream(script))
			{
				Program program=ProgramManager.getInstance().load(input, script.getName());
				Context context=createContext();
				program.getBlock().execute(context);
			}
		}
	}
}
