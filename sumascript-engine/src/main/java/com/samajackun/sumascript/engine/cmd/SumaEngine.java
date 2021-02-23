package com.samajackun.sumascript.engine.cmd;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.DefaultContext;
import com.samajackun.sumascript.core.instructions.SumaEvaluatorFactory;
import com.samajackun.sumascript.core.runtime.FlexibleVariablesContext;
import com.samajackun.sumascript.core.runtime.FlexibleVariablesManager;
import com.samajackun.sumascript.core.runtime.ScriptRuntime;
import com.samajackun.sumascript.engine.providers.FileSystemProvider;
import com.samajackun.sumascript.parser.Program;
import com.samajackun.sumascript.program.ProgramManager;

public class SumaEngine
{
	private static Context createContext()
	{
		DefaultContext context=new DefaultContext();
		context.setProvider(new FileSystemProvider());
		ScriptRuntime runtime=new ScriptRuntime(new File("."));
		runtime.setOut(new PrintWriter(System.out, true));
		runtime.setErr(new PrintWriter(System.err, true));
		context.setRuntime(runtime);
		context.setEvaluatorFactory(SumaEvaluatorFactory.getInstance());
		context.setVariablesManager(new FlexibleVariablesManager(new FlexibleVariablesContext()));
		context.getVariablesManager().pushLocalContext(new FlexibleVariablesContext());
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
				try
				{
					program.getBlock().execute(context);
				}
				finally
				{
					context.getVariablesManager().popLocalContext();
				}
			}
		}
	}
}
