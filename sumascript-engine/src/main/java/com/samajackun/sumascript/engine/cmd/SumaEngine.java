package com.samajackun.sumascript.engine.cmd;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.DefaultContext;
import com.samajackun.sumascript.core.ExecutionException;
import com.samajackun.sumascript.core.instructions.SumaEvaluatorFactory;
import com.samajackun.sumascript.core.runtime.FlexibleVariablesContext;
import com.samajackun.sumascript.core.runtime.FlexibleVariablesManager;
import com.samajackun.sumascript.core.runtime.ScriptRuntime;
import com.samajackun.sumascript.engine.providers.FileSystemProvider;
import com.samajackun.sumascript.parser.Program;
import com.samajackun.sumascript.program.ProgramLoadException;
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

	private static void executeScript(File script)
	{
		System.out.println("Sumascript engine: Executing "+script);
		try (InputStream input=new FileInputStream(script))
		{
			Program program=ProgramManager.getInstance().load(input, script.getName());
			Context context=createContext();
			try
			{
				program.getBlock().execute(context);
			}
			catch (ExecutionException e)
			{
				System.err.println("ERROR AT EXECUTION: "+e.getMessage());
			}
			finally
			{
				context.getVariablesManager().popLocalContext();
			}
		}
		catch (IOException | ProgramLoadException e)
		{
			System.err.println("ERROR LOADING FILE: "+e.getMessage());
		}
	}

	public static void main(String[] args)
		throws Throwable
	{
		if (args.length<0)
		{
			System.err.println("Execute Sumascript files");
			System.err.println("Usage: <scripts...>");
		}
		else
		{
			Arrays.asList(args).stream().map(File::new).forEach(SumaEngine::executeScript);
		}
	}
}
