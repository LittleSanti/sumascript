package com.samajackun.sumascript.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.StringReader;
import java.util.Map;

import org.junit.Test;

import com.samajackun.rodas.core.eval.Name;
import com.samajackun.rodas.core.eval.VariableNotFoundException;
import com.samajackun.sumascript.core.ExecutionException;
import com.samajackun.sumascript.core.expression.MyContext;
import com.samajackun.sumascript.core.runtime.FlexibleVariablesContext;

public class ProgramVariablesTest
{
	private Program parseToProgram(String code)
	{
		try
		{
			Program program=SumaParser.getInstance().parse(new StringReader(code));
			return program;
		}
		catch (IOException | SumaParseException e)
		{
			e.printStackTrace();
			fail(e.toString());
			return null;
		}
	}

	@Test
	public void variableLocal()
	{
		String code="var a=120";
		Program program=parseToProgram(code);
		MyContext context=new MyContext();
		context.getVariablesManager().pushLocalContext(new FlexibleVariablesContext());
		try
		{
			program.getBlock().execute(context);
			assertEquals(120L, context.getVariablesManager().getLocalVariable(Name.instanceOf("a")));
		}
		catch (VariableNotFoundException | ExecutionException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
		context.getVariablesManager().popLocalContext();
	}

	@Test
	public void variableHash()
	{
		String code="var a={name:\"january\", days:31}";
		Program program=parseToProgram(code);
		MyContext context=new MyContext();
		context.getVariablesManager().pushLocalContext(new FlexibleVariablesContext());
		try
		{
			program.getBlock().execute(context);
			@SuppressWarnings("unchecked")
			Map<String, Object> map=(Map<String, Object>)context.getVariablesManager().getLocalVariable(Name.instanceOf("a"));
			assertEquals("january", map.get("name"));
			assertEquals(31L, map.get("days"));
		}
		catch (VariableNotFoundException | ExecutionException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
		context.getVariablesManager().popLocalContext();
	}

	@Test
	public void indexedLoop()
	{
		String code="var s=\"\";for (var i=0;i<5;i++) {s=s+\"pollas,\";}";
		Program program=parseToProgram(code);
		MyContext context=new MyContext();
		context.getVariablesManager().pushLocalContext(new FlexibleVariablesContext());
		try
		{
			program.getBlock().execute(context);
			assertEquals("pollas,pollas,pollas,pollas,pollas,", context.getVariablesManager().getLocalVariable(Name.instanceOf("s")));
		}
		catch (VariableNotFoundException | ExecutionException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
		context.getVariablesManager().popLocalContext();
	}

	@Test
	public void array()
	{
		String code="var array=[120, 1 , 140]; var s=array[0]+array[1];";
		Program program=parseToProgram(code);
		MyContext context=new MyContext();
		context.getVariablesManager().pushLocalContext(new FlexibleVariablesContext());
		try
		{
			program.getBlock().execute(context);
			assertEquals(121L, context.getVariablesManager().getLocalVariable(Name.instanceOf("s")));
		}
		catch (VariableNotFoundException | ExecutionException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
		context.getVariablesManager().popLocalContext();
	}

	@Test
	public void collectionLoop()
	{
		String code="var array=[120, -1 , 2]; var s=0; for (var i:array) {s+=i}";
		Program program=parseToProgram(code);
		MyContext context=new MyContext();
		context.getVariablesManager().pushLocalContext(new FlexibleVariablesContext());
		try
		{
			program.getBlock().execute(context);
			assertEquals(121L, context.getVariablesManager().getLocalVariable(Name.instanceOf("s")));
		}
		catch (VariableNotFoundException | ExecutionException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
		context.getVariablesManager().popLocalContext();
	}
}
