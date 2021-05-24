package com.samajackun.sumascript.engine.cmd;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.Collection;
import java.util.Map;

import org.junit.Test;

import com.samajackun.rodas.core.eval.Name;
import com.samajackun.rodas.core.eval.VariableNotFoundException;
import com.samajackun.sumascript.core.ExecutionException;
import com.samajackun.sumascript.core.expression.MyContext;
import com.samajackun.sumascript.core.instructions.SumaEvaluatorFactory;
import com.samajackun.sumascript.core.runtime.FlexibleVariablesContext;
import com.samajackun.sumascript.core.runtime.FlexibleVariablesManager;
import com.samajackun.sumascript.core.runtime.ScriptRuntime;
import com.samajackun.sumascript.engine.providers.FileSystemProvider;
import com.samajackun.sumascript.parser.Program;
import com.samajackun.sumascript.parser.SumaParseException;
import com.samajackun.sumascript.parser.SumaParser;

public class QueryParserTest
{
	private Program test(String code)
		throws IOException
	{
		try
		{
			Program program=SumaParser.getInstance().parse(new StringReader(code));
			return program;
		}
		catch (SumaParseException e)
		{
			e.printStackTrace();
			fail(e.toString());
			return null;
		}
	}

	private MyContext createContext()
	{
		MyContext context=new MyContext();
		context.setProvider(new FileSystemProvider());
		ScriptRuntime runtime=new ScriptRuntime(new File("."));
		runtime.setOut(new PrintWriter(System.out, true));
		runtime.setErr(new PrintWriter(System.err, true));
		context.setRuntime(runtime);
		context.setVariablesManager(new FlexibleVariablesManager(new FlexibleVariablesContext()));
		context.getVariablesManager().pushLocalContext(new FlexibleVariablesContext());
		context.setEvaluatorFactory(SumaEvaluatorFactory.getInstance());
		return context;
	}

	@Test
	public void parseSelectWithFrom()
		throws IOException
	{
		String code="var rows=SELECT name FROM \"s:\\proyectos\\sumascript\\sumascript-engine\\src\\test\\resources\";for(var r:rows) {x=r.name}";
		Program program=test(code);
		MyContext context=createContext();
		try
		{
			program.getBlock().execute(context);
			Object value=context.getVariablesManager().getLocalVariable(Name.instanceOf("rows"));
			assertTrue(value instanceof Collection);
			@SuppressWarnings("unchecked")
			Collection<Map> collection=(Collection<Map>)value;
			for (Map rowData : collection)
			{
				System.out.println(rowData);
			}
			assertEquals(7, collection.size());
		}
		catch (VariableNotFoundException | ExecutionException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
		context.getVariablesManager().popLocalContext();
	}

	@Test
	public void parseSelectWithFromReference()
		throws IOException
	{
		String code="var data=[{name:'madrid',code:28},{name:'toledo',code:45}];var rows=SELECT name, code FROM ${data};for(var r:rows) {x=r.name}";
		Program program=test(code);
		MyContext context=createContext();
		try
		{
			program.getBlock().execute(context);
			Object value=context.getVariablesManager().getLocalVariable(Name.instanceOf("rows"));
			assertTrue(value instanceof Collection);
			@SuppressWarnings("unchecked")
			Collection<Map> collection=(Collection<Map>)value;
			for (Map rowData : collection)
			{
				System.out.println(rowData);
			}
			assertEquals(2, collection.size());
		}
		catch (VariableNotFoundException | ExecutionException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
		context.getVariablesManager().popLocalContext();
	}

	@Test
	public void parseSelectWithFromAndWhere()
		throws IOException
	{
		String code="var rows=SELECT name, size FROM \"s:\\proyectos\\sumascript\\sumascript-engine\\src\\test\\resources\" WHERE size>93;for(var r:rows) {echoout r.name+':'+r.size}";
		Program program=test(code);
		MyContext context=createContext();
		try
		{
			program.getBlock().execute(context);
			Object value=context.getVariablesManager().getLocalVariable(Name.instanceOf("rows"));
			// assertTrue(value instanceof Collection);
			// @SuppressWarnings("unchecked")
			// Collection<Map> collection=(Collection<Map>)value;
			// for (Map rowData : collection)
			// {
			// System.out.println(rowData);
			// }
			// assertEquals(7, collection.size());
		}
		catch (VariableNotFoundException | ExecutionException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
		finally
		{
			context.getVariablesManager().popLocalContext();
		}
	}
}
