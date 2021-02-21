package com.samajackun.sumascript.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.MyOpenContext;
import com.samajackun.rodas.core.eval.Name;
import com.samajackun.rodas.core.eval.VariableNotFoundException;
import com.samajackun.rodas.core.eval.functions.Function;
import com.samajackun.rodas.core.eval.functions.FunctionEvaluationException;
import com.samajackun.rodas.core.eval.functions.FunctionNotFoundException;
import com.samajackun.sumascript.core.ExecutionException;
import com.samajackun.sumascript.core.expression.MyContext;
import com.samajackun.sumascript.core.instructions.BlockInstruction;
import com.samajackun.sumascript.core.instructions.SumaEvaluatorFactory;
import com.samajackun.sumascript.core.runtime.FlexibleVariablesContext;
import com.samajackun.sumascript.core.runtime.FlexibleVariablesManager;
import com.samajackun.sumascript.core.runtime.ScriptRuntime;
import com.samajackun.sumascript.core.runtime.Undefined;

public class ProgramTest
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
		// ScriptRuntime runtime=new ScriptRuntime(new File("."));
		// StringWriter myOut=new StringWriter(4096);
		// runtime.setOut(new PrintWriter(myOut, true));
		// StringWriter myErr=new StringWriter(4096);
		// runtime.setErr(new PrintWriter(myErr, true));
		// context.setRuntime(runtime);
		return context;
	}

	@Test
	public void parseAssignation()
		throws IOException
	{
		String code="a=10;";
		MyContext context=createContext();
		Program program=test(code);
		context.getVariablesManager().pushLocalContext(new FlexibleVariablesContext());
		try
		{
			program.getBlock().execute(context);
			assertEquals(10L, context.getVariablesManager().getGlobalVariable(Name.instanceOf("a")));
		}
		catch (ExecutionException | VariableNotFoundException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
		finally
		{
			context.getVariablesManager().popLocalContext();
		}
	}

	@Test
	public void parseEchoOut()
		throws IOException
	{
		String code="echoout a";
		Program program=test(code);
		MyContext context=createContext();
		ScriptRuntime runtime=new ScriptRuntime(new File("."));
		StringWriter myOut=new StringWriter(4096);
		runtime.setOut(new PrintWriter(myOut, true));
		StringWriter myErr=new StringWriter(4096);
		runtime.setErr(new PrintWriter(myErr, true));
		context.setRuntime(runtime);
		context.getVariablesManager().pushLocalContext(new FlexibleVariablesContext());
		context.getVariablesManager().setLocalVariable(Name.instanceOf("a"), 120);
		try
		{
			program.getBlock().execute(context);
			assertEquals("120\r\n", myOut.toString());
			assertEquals("", myErr.toString());
		}
		catch (ExecutionException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
		context.getVariablesManager().popLocalContext();
	}

	@Test
	public void parseEchoErr()
		throws IOException
	{
		String code="echoerr a";
		Program program=test(code);
		MyContext context=createContext();
		ScriptRuntime runtime=new ScriptRuntime(new File("."));
		StringWriter myOut=new StringWriter(4096);
		runtime.setOut(new PrintWriter(myOut, true));
		StringWriter myErr=new StringWriter(4096);
		runtime.setErr(new PrintWriter(myErr, true));
		context.setRuntime(runtime);
		context.getVariablesManager().pushLocalContext(new FlexibleVariablesContext());
		context.getVariablesManager().setLocalVariable(Name.instanceOf("a"), 120);
		try
		{
			program.getBlock().execute(context);
			assertEquals("120\r\n", myErr.toString());
			assertEquals("", myOut.toString());
		}
		catch (ExecutionException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
		context.getVariablesManager().popLocalContext();
	}

	@Test
	public void parseVarWithOneVariable()
		throws IOException
	{
		String code="var a";
		Program program=test(code);
		MyContext context=createContext();
		context.getVariablesManager().pushLocalContext(new FlexibleVariablesContext());
		try
		{
			program.getBlock().execute(context);
			assertTrue(context.getVariablesManager().peekLocalContext().contains(Name.instanceOf("a")));
		}
		catch (ExecutionException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
		context.getVariablesManager().popLocalContext();
	}

	@Test
	public void parseVarWithOneMultiply()
		throws IOException
	{
		String code="var a=1*2";
		Program program=test(code);
		MyContext context=createContext();
		try
		{
			program.getBlock().execute(context);
			assertEquals(2L, context.getVariablesManager().peekLocalContext().get(Name.instanceOf("a")));
		}
		catch (VariableNotFoundException | ExecutionException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void parseVarWithSomeVariablesNotInitialized()
		throws IOException
	{
		String code="var a,b";
		Program program=test(code);
		MyContext context=createContext();
		context.getVariablesManager().pushLocalContext(new FlexibleVariablesContext());
		try
		{
			program.getBlock().execute(context);
			assertTrue(context.getVariablesManager().peekLocalContext().contains(Name.instanceOf("a")));
			assertTrue(context.getVariablesManager().peekLocalContext().contains(Name.instanceOf("b")));
		}
		catch (ExecutionException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
		context.getVariablesManager().popLocalContext();
	}

	@Test
	public void parseVarWithSomeVariablesInitialized()
		throws IOException
	{
		String code="var a=120,b=130";
		Program program=test(code);
		MyContext context=createContext();
		context.getVariablesManager().pushLocalContext(new FlexibleVariablesContext());
		try
		{
			program.getBlock().execute(context);
			assertEquals(120L, context.getVariablesManager().getLocalVariable(Name.instanceOf("a")));
			assertEquals(130L, context.getVariablesManager().getLocalVariable(Name.instanceOf("b")));
		}
		catch (VariableNotFoundException | ExecutionException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
		context.getVariablesManager().popLocalContext();
	}

	@Test
	public void parseVarWithSomeVariablesSomeInitializedAndSomeNot()
		throws IOException
	{
		String code="var a=120,b";
		Program program=test(code);
		MyContext context=createContext();
		context.getVariablesManager().pushLocalContext(new FlexibleVariablesContext());
		try
		{
			program.getBlock().execute(context);
			assertEquals(120L, context.getVariablesManager().getLocalVariable(Name.instanceOf("a")));
			assertTrue(context.getVariablesManager().peekLocalContext().contains(Name.instanceOf("a")));
			assertTrue(context.getVariablesManager().peekLocalContext().contains(Name.instanceOf("b")));
		}
		catch (VariableNotFoundException | ExecutionException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
		context.getVariablesManager().popLocalContext();
	}

	@Test
	public void parseVarAndInitialization()
		throws IOException
	{
		String code="var a=120";
		Program program=test(code);
		MyContext context=createContext();
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
	public void parseIfWithEmptyBlock()
		throws IOException
	{
		String code="if (a=120) {}";
		Program program=test(code);
		MyContext context=createContext();
		context.getVariablesManager().pushLocalContext(new FlexibleVariablesContext());
		context.getVariablesManager().setLocalVariable(Name.instanceOf("a"), 120);
		try
		{
			program.getBlock().execute(context);
			assertEquals(120, context.getVariablesManager().getLocalVariable(Name.instanceOf("a")));
		}
		catch (VariableNotFoundException | ExecutionException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
		context.getVariablesManager().popLocalContext();
	}

	@Test
	public void parseIfWithPositiveInstruction()
		throws IOException
	{
		String code="if (a=120) {b=1}";
		Program program=test(code);
		MyContext context=createContext();
		context.getVariablesManager().pushLocalContext(new FlexibleVariablesContext());
		context.getVariablesManager().setLocalVariable(Name.instanceOf("a"), 120);
		try
		{
			program.getBlock().execute(context);
			assertEquals(1L, context.getVariablesManager().getGlobalVariable(Name.instanceOf("b")));
		}
		catch (VariableNotFoundException | ExecutionException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
		context.getVariablesManager().popLocalContext();
	}

	@Test
	public void parseIfWithNegativeInstruction()
		throws IOException
	{
		String code="if (a=120) {} else {b=1}";
		Program program=test(code);
		MyContext context=createContext();
		context.getVariablesManager().pushLocalContext(new FlexibleVariablesContext());
		context.getVariablesManager().setLocalVariable(Name.instanceOf("a"), 0);
		try
		{
			program.getBlock().execute(context);
			assertEquals(1L, context.getVariablesManager().getGlobalVariable(Name.instanceOf("b")));
		}
		catch (VariableNotFoundException | ExecutionException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
		context.getVariablesManager().popLocalContext();
	}

	@Test
	public void parseBlockEmpty()
		throws IOException
	{
		String code="{}";
		Program program=test(code);
		MyContext context=createContext();
		try
		{
			program.getBlock().execute(context);
		}
		catch (ExecutionException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void parseBlockWithOneInstruction()
		throws IOException
	{
		String code="{a=1}";
		Program program=test(code);
		MyContext context=createContext();
		context.getVariablesManager().pushLocalContext(new FlexibleVariablesContext());
		try
		{
			program.getBlock().execute(context);
			assertEquals(1L, context.getVariablesManager().getGlobalVariable(Name.instanceOf("a")));
		}
		catch (ExecutionException | VariableNotFoundException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
		finally
		{
			context.getVariablesManager().popLocalContext();
		}
	}

	@Test
	public void parseBlockWithOneInstructionAndSemicolon()
		throws IOException
	{
		String code="{a=1;}";
		Program program=test(code);
		MyContext context=createContext();
		context.getVariablesManager().pushLocalContext(new FlexibleVariablesContext());
		try
		{
			program.getBlock().execute(context);
			assertEquals(1L, context.getVariablesManager().getGlobalVariable(Name.instanceOf("a")));
		}
		catch (ExecutionException | VariableNotFoundException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
		finally
		{
			context.getVariablesManager().popLocalContext();
		}
	}

	@Test
	public void parseBlockWithTwoInstructions()
		throws IOException
	{
		String code="{a=1;b=2}";
		Program program=test(code);
		MyContext context=createContext();
		context.getVariablesManager().pushLocalContext(new FlexibleVariablesContext());
		try
		{
			program.getBlock().execute(context);
			assertEquals(1L, context.getVariablesManager().getGlobalVariable(Name.instanceOf("a")));
			assertEquals(2L, context.getVariablesManager().getGlobalVariable(Name.instanceOf("b")));
		}
		catch (ExecutionException | VariableNotFoundException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
		finally
		{
			context.getVariablesManager().popLocalContext();
		}
	}

	@Test
	public void parseSwitchOnFirstCase()
		throws IOException
	{
		String code="switch ( a ) {case 0: b=10; case 1: b=11; case 2: b=12; default: b=-1}";
		Program program=test(code);
		MyContext context=createContext();
		context.getVariablesManager().pushLocalContext(new FlexibleVariablesContext());
		context.getVariablesManager().setLocalVariable(Name.instanceOf("a"), 0L);
		try
		{
			program.getBlock().execute(context);
			assertEquals(10L, context.getVariablesManager().getGlobalVariable(Name.instanceOf("b")));
		}
		catch (ExecutionException | VariableNotFoundException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
		finally
		{
			context.getVariablesManager().popLocalContext();
		}
	}

	@Test
	public void parseSwitchOnSecondCase()
		throws IOException
	{
		String code="switch ( a ) {case 0: b=10; case 1: b=11; case 2: b=12; default: b=-1}";
		Program program=test(code);
		MyContext context=createContext();
		context.getVariablesManager().pushLocalContext(new FlexibleVariablesContext());
		context.getVariablesManager().setLocalVariable(Name.instanceOf("a"), 1L);
		try
		{
			program.getBlock().execute(context);
			assertEquals(11L, context.getVariablesManager().getGlobalVariable(Name.instanceOf("b")));
		}
		catch (ExecutionException | VariableNotFoundException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
		finally
		{
			context.getVariablesManager().popLocalContext();
		}
	}

	@Test
	public void parseSwitchOnDefaultCase()
		throws IOException
	{
		String code="switch ( a ) {case 0: b=10; case 1: b=11; case 2: b=12; default: b=-1}";
		Program program=test(code);
		MyContext context=createContext();
		context.getVariablesManager().pushLocalContext(new FlexibleVariablesContext());
		context.getVariablesManager().setLocalVariable(Name.instanceOf("a"), 112L);
		try
		{
			program.getBlock().execute(context);
			assertEquals(-1L, context.getVariablesManager().getGlobalVariable(Name.instanceOf("b")));
		}
		catch (ExecutionException | VariableNotFoundException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
		finally
		{
			context.getVariablesManager().popLocalContext();
		}
	}

	@Test
	public void parseCollectionLoopEmpty()
		throws IOException
	{
		String code="for (var a:set) {}";
		Program program=test(code);
		MyContext context=createContext();
		context.getVariablesManager().pushLocalContext(new FlexibleVariablesContext());
		context.getVariablesManager().setLocalVariable(Name.instanceOf("a"), Arrays.asList("january", "february", "march"));
		try
		{
			program.getBlock().execute(context);
		}
		catch (ExecutionException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
		context.getVariablesManager().popLocalContext();
	}

	@Test
	public void parseCollectionLoopWithOneLine()
		throws IOException
	{
		String code="for (var a:set) {i=i+1}";
		Program program=test(code);
		MyContext context=createContext();
		context.getVariablesManager().pushLocalContext(new FlexibleVariablesContext());
		context.getVariablesManager().setLocalVariable(Name.instanceOf("set"), Arrays.asList("january", "february", "march"));
		context.getVariablesManager().setLocalVariable(Name.instanceOf("i"), 0L);
		try
		{
			program.getBlock().execute(context);
			assertEquals(3L, context.getVariablesManager().getLocalVariable(Name.instanceOf("i")));
		}
		catch (ExecutionException | VariableNotFoundException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
		context.getVariablesManager().popLocalContext();
	}

	@Test
	public void parseCollectionLoopWithPreWhile()
		throws IOException
	{
		String code="for (var a:set) while (i<3) {i=i+1}";
		Program program=test(code);
		MyContext context=createContext();
		context.getVariablesManager().pushLocalContext(new FlexibleVariablesContext());
		context.getVariablesManager().setLocalVariable(Name.instanceOf("set"), Arrays.asList("january", "february", "march", "april", "may", "june"));
		context.getVariablesManager().setLocalVariable(Name.instanceOf("i"), 3L);
		try
		{
			program.getBlock().execute(context);
			assertEquals(3L, context.getVariablesManager().getLocalVariable(Name.instanceOf("i")));
		}
		catch (ExecutionException | VariableNotFoundException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
		context.getVariablesManager().popLocalContext();
	}

	// @Test
	// public void parseCollectionLoopWithPostWhile()
	// throws IOException
	// {
	// String code="for (var a:set) {i=i+1} while (i<3)";
	// Program program=test(code);
	// MyContext context=createContext();
	// context.getVariablesManager().pushLocalContext(new FlexibleVariablesContext());
	// context.getVariablesManager().setLocalVariable(Name.instanceOf("set"), Arrays.asList("january", "february", "march", "april", "may", "june"));
	// context.getVariablesManager().setLocalVariable(Name.instanceOf("i"), 3L);
	// try
	// {
	// program.getBlock().execute(context);
	// assertEquals(4L, context.getVariablesManager().getLocalVariable(Name.instanceOf("i")));
	// }
	// catch (ExecutionException | VariableNotFoundException e)
	// {
	// e.printStackTrace();
	// fail(e.toString());
	// }
	// context.getVariablesManager().popLocalContext();
	// }

	@SuppressWarnings("unchecked")
	@Test
	public void parseDereferencingOneLevel()
		throws IOException
	{
		String code="month.name='january'";
		Program program=test(code);
		MyContext context=createContext();
		context.getVariablesManager().pushLocalContext(new FlexibleVariablesContext());
		context.getVariablesManager().setLocalVariable(Name.instanceOf("month"), new HashMap<>());
		try
		{
			program.getBlock().execute(context);
			assertEquals("january", ((Map<Object, Object>)context.getVariablesManager().getLocalVariable(Name.instanceOf("month"))).get(Name.instanceOf("name")));
		}
		catch (VariableNotFoundException | ExecutionException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
		context.getVariablesManager().popLocalContext();
	}

	@Test
	public void parseVarPlusPlus()
		throws IOException
	{
		String code="a++";
		Program program=test(code);
		MyContext context=createContext();
		context.getVariablesManager().pushLocalContext(new FlexibleVariablesContext());
		context.getVariablesManager().setLocalVariable(Name.instanceOf("a"), 120);
		try
		{
			program.getBlock().execute(context);
			assertEquals(121, context.getVariablesManager().peekLocalContext().get(Name.instanceOf("a")));
		}
		catch (VariableNotFoundException | ExecutionException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
		context.getVariablesManager().popLocalContext();
	}

	@Test
	public void parseVarMinusMinus()
		throws IOException
	{
		String code="a--";
		Program program=test(code);
		MyContext context=createContext();
		context.getVariablesManager().pushLocalContext(new FlexibleVariablesContext());
		context.getVariablesManager().setLocalVariable(Name.instanceOf("a"), 120);
		try
		{
			program.getBlock().execute(context);
			assertEquals(119, context.getVariablesManager().peekLocalContext().get(Name.instanceOf("a")));
		}
		catch (VariableNotFoundException | ExecutionException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
		context.getVariablesManager().popLocalContext();
	}

	@Test
	public void parseVarAddAssignation()
		throws IOException
	{
		String code="a+=11";
		Program program=test(code);
		MyContext context=createContext();
		context.getVariablesManager().pushLocalContext(new FlexibleVariablesContext());
		context.getVariablesManager().setLocalVariable(Name.instanceOf("a"), 120);
		try
		{
			program.getBlock().execute(context);
			assertEquals(131L, context.getVariablesManager().peekLocalContext().get(Name.instanceOf("a")));
		}
		catch (VariableNotFoundException | ExecutionException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
		context.getVariablesManager().popLocalContext();
	}

	@Test
	public void parseVarSubstractAssignation()
		throws IOException
	{
		String code="a-=11";
		Program program=test(code);
		MyContext context=createContext();
		context.getVariablesManager().pushLocalContext(new FlexibleVariablesContext());
		context.getVariablesManager().setLocalVariable(Name.instanceOf("a"), 120);
		try
		{
			program.getBlock().execute(context);
			assertEquals(109L, context.getVariablesManager().peekLocalContext().get(Name.instanceOf("a")));
		}
		catch (VariableNotFoundException | ExecutionException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
		context.getVariablesManager().popLocalContext();
	}

	@Test
	public void parseVarMultiplyAssignation()
		throws IOException
	{
		String code="a*=2";
		Program program=test(code);
		MyContext context=createContext();
		context.getVariablesManager().pushLocalContext(new FlexibleVariablesContext());
		context.getVariablesManager().setLocalVariable(Name.instanceOf("a"), 120);
		try
		{
			program.getBlock().execute(context);
			assertEquals(240L, context.getVariablesManager().peekLocalContext().get(Name.instanceOf("a")));
		}
		catch (VariableNotFoundException | ExecutionException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
		context.getVariablesManager().popLocalContext();
	}

	@Test
	public void parseVarDivideAssignation()
		throws IOException
	{
		String code="a/=3";
		Program program=test(code);
		MyContext context=createContext();
		context.getVariablesManager().pushLocalContext(new FlexibleVariablesContext());
		context.getVariablesManager().setLocalVariable(Name.instanceOf("a"), 120);
		try
		{
			program.getBlock().execute(context);
			assertEquals(40L, context.getVariablesManager().peekLocalContext().get(Name.instanceOf("a")));
		}
		catch (VariableNotFoundException | ExecutionException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
		context.getVariablesManager().popLocalContext();
	}

	@Test
	public void parseFunctionCallAsLeftSide()
		throws IOException
	{
		String code="myfunction()";
		Program program=test(code);
		MyContext context=createContext();
		context.getVariablesManager().pushLocalContext(new FlexibleVariablesContext());
		context.getVariablesManager().setLocalVariable(Name.instanceOf("myfunction"), new Function()
		{
			@Override
			public Object call(Context context, List<Object> argumentValues)
				throws FunctionEvaluationException
			{
				context.getVariablesManager().setGlobalVariable(Name.instanceOf("a"), 120L);
				return null;
			}
		});
		try
		{
			BlockInstruction block=program.getBlock();
			block.execute(context);
			assertEquals(120L, context.getVariablesManager().getGlobalVariable(Name.instanceOf("a")));
		}
		catch (VariableNotFoundException | ExecutionException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
		context.getVariablesManager().popLocalContext();
	}

	@Test
	public void parseFunctionCallAsRightSide()
		throws IOException
	{
		String code="a=myfunction()";
		Program program=test(code);
		MyContext context=createContext();
		context.getVariablesManager().pushLocalContext(new FlexibleVariablesContext());
		context.getVariablesManager().setLocalVariable(Name.instanceOf("myfunction"), new Function()
		{
			@Override
			public Object call(Context context, List<Object> argumentValues)
				throws FunctionEvaluationException
			{
				return 120L;
			}
		});
		try
		{
			BlockInstruction block=program.getBlock();
			block.execute(context);
			assertEquals(120L, context.getVariablesManager().getGlobalVariable(Name.instanceOf("a")));
		}
		catch (VariableNotFoundException | ExecutionException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
		context.getVariablesManager().popLocalContext();
	}

	@Test
	public void parseFunctionCallAsRightSideWithOneArgument()
		throws IOException
	{
		String code="a=myfunction(12)";
		Program program=test(code);
		MyContext context=createContext();
		context.getVariablesManager().pushLocalContext(new FlexibleVariablesContext());
		context.getVariablesManager().setLocalVariable(Name.instanceOf("myfunction"), new Function()
		{
			@Override
			public Object call(Context context, List<Object> argumentValues)
				throws FunctionEvaluationException
			{
				return 2 * ((Number)argumentValues.get(0)).longValue();
			}
		});
		try
		{
			BlockInstruction block=program.getBlock();
			block.execute(context);
			assertEquals(24L, context.getVariablesManager().getGlobalVariable(Name.instanceOf("a")));
		}
		catch (VariableNotFoundException | ExecutionException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
		context.getVariablesManager().popLocalContext();
	}

	@Test
	public void parseFunctionDefinition()
		throws IOException
	{
		String code="function myfunction(){return 120}";
		Program program=test(code);
		MyContext context=createContext();
		context.getVariablesManager().pushLocalContext(new FlexibleVariablesContext());
		try
		{
			BlockInstruction block=program.getBlock();
			block.execute(context);
			Object obj=context.getVariablesManager().getLocalVariable(Name.instanceOf("myfunction"));
			assertTrue(obj instanceof Function);
			Function function=(Function)obj;
			Object returnValue=function.call(context, new ArrayList<>());
			assertEquals(120L, returnValue);
		}
		catch (VariableNotFoundException | ExecutionException | FunctionEvaluationException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
		context.getVariablesManager().popLocalContext();
	}

	@Test
	public void parseTwoFunctionDefinitions()
		throws IOException
	{
		String code="function myfunction1(){return 120}\r\nfunction myfunction2() {return 121}";
		Program program=test(code);
		MyContext context=createContext();
		context.getVariablesManager().pushLocalContext(new FlexibleVariablesContext());
		try
		{
			BlockInstruction block=program.getBlock();
			block.execute(context);
			Object obj1=context.getVariablesManager().getLocalVariable(Name.instanceOf("myfunction1"));
			assertTrue(obj1 instanceof Function);
			Object obj2=context.getVariablesManager().getLocalVariable(Name.instanceOf("myfunction2"));
			assertTrue(obj2 instanceof Function);
		}
		catch (VariableNotFoundException | ExecutionException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
		context.getVariablesManager().popLocalContext();
	}

	@Test
	public void parseFunctionDefinitionAndOneCall()
		throws IOException
	{
		String code="function myfunction(s){echoout s}\r\nmyfunction(120)";
		Program program=test(code);
		MyContext context=createContext();
		ScriptRuntime runtime=new ScriptRuntime(new File("."));
		StringWriter myOut=new StringWriter(4096);
		runtime.setOut(new PrintWriter(myOut, true));
		StringWriter myErr=new StringWriter(4096);
		runtime.setErr(new PrintWriter(myErr, true));
		context.setRuntime(runtime);
		try
		{
			BlockInstruction block=program.getBlock();
			block.execute(context);
			assertEquals("120\r\n", myOut.toString());
		}
		catch (ExecutionException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void parseFunctionDefinitionAndTwoCalls()
		throws IOException
	{
		String code="function myfunction(s){echoout s}\r\nmyfunction(120)\r\nmyfunction(121)";
		Program program=test(code);
		MyContext context=createContext();
		ScriptRuntime runtime=new ScriptRuntime(new File("."));
		StringWriter myOut=new StringWriter(4096);
		runtime.setOut(new PrintWriter(myOut, true));
		StringWriter myErr=new StringWriter(4096);
		runtime.setErr(new PrintWriter(myErr, true));
		context.setRuntime(runtime);
		try
		{
			BlockInstruction block=program.getBlock();
			block.execute(context);
			assertEquals("120\r\n121\r\n", myOut.toString());
		}
		catch (ExecutionException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void parseFunctionDefinitionWithTwoLines()
		throws IOException
	{
		String code="function nextNumber(n){s=1+n\r\nreturn s}\r\nx=nextNumber(120)";
		Program program=test(code);
		MyContext context=createContext();
		try
		{
			BlockInstruction block=program.getBlock();
			block.execute(context);
			assertEquals(121L, context.getVariablesManager().getLocalVariable(Name.instanceOf("x")));
			assertEquals(121L, context.getVariablesManager().getLocalVariable(Name.instanceOf("s")));
			assertEquals(Undefined.INSTANCE, context.getVariablesManager().getLocalVariable(Name.instanceOf("n")));
		}
		catch (ExecutionException | VariableNotFoundException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void parseCallUndefinedFunction()
		throws IOException
	{
		String code="x=myFunction(120)";
		Program program=test(code);
		MyContext context=createContext();
		try
		{
			BlockInstruction block=program.getBlock();
			block.execute(context);
			assertEquals(Undefined.INSTANCE, context.getVariablesManager().getLocalVariable(Name.instanceOf("x")));
		}
		catch (ExecutionException e)
		{
			assertTrue(e.getCause() instanceof FunctionNotFoundException);
			assertEquals("myFunction", ((FunctionNotFoundException)e.getCause()).getFunction());
		}
		catch (VariableNotFoundException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void parseFunctionDefinitionAsRightSide()
		throws IOException
	{
		String code="myfunction=function(){return 120}";
		Program program=test(code);
		MyContext context=createContext();
		context.getVariablesManager().pushLocalContext(new FlexibleVariablesContext());
		try
		{
			BlockInstruction block=program.getBlock();
			block.execute(context);
			Object obj=context.getVariablesManager().getGlobalVariable(Name.instanceOf("myfunction"));
			assertTrue(obj instanceof Function);
			Function function=(Function)obj;
			Object returnValue=function.call(context, new ArrayList<>());
			assertEquals(120L, returnValue);
		}
		catch (VariableNotFoundException | ExecutionException | FunctionEvaluationException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
		context.getVariablesManager().popLocalContext();
	}

	@Test
	public void parseFunctionDefinitionAndCallWithOneArgument()
		throws IOException
	{
		String code="myfunction=function(a){return 1+a};x=myfunction(120)";
		Program program=test(code);
		MyContext context=createContext();
		try
		{
			BlockInstruction block=program.getBlock();
			block.execute(context);
			Object returnValue=context.getVariablesManager().getLocalVariable(Name.instanceOf("x"));
			assertEquals(121L, returnValue);
		}
		catch (VariableNotFoundException | ExecutionException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void parseRecursiveFunctionDefinitionAndCallWithOneArgument()
		throws IOException
	{
		String code="factorial=function(a){if (a=1) {return 1} else {return a*factorial(a-1)}};x=factorial(5)";
		Program program=test(code);
		MyContext context=createContext();
		try
		{
			BlockInstruction block=program.getBlock();
			block.execute(context);
			Object returnValue=context.getVariablesManager().getLocalVariable(Name.instanceOf("x"));
			assertEquals(120L, returnValue);
		}
		catch (VariableNotFoundException | ExecutionException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void parseSimpleSelectAndLoop()
		throws IOException
	{
		String code="var rows=SELECT (1+120) AS num;for(var r:rows) {echoout r.num}";
		Program program=test(code);
		MyOpenContext context=new MyOpenContext();
		ScriptRuntime runtime=new ScriptRuntime(new File("."));
		StringWriter myOut=new StringWriter(4096);
		runtime.setOut(new PrintWriter(myOut, true));
		StringWriter myErr=new StringWriter(4096);
		runtime.setErr(new PrintWriter(myErr, true));
		context.setRuntime(runtime);
		context.setVariablesManager(new FlexibleVariablesManager(new FlexibleVariablesContext()));
		context.getVariablesManager().pushLocalContext(new FlexibleVariablesContext());
		context.setEvaluatorFactory(SumaEvaluatorFactory.getInstance());
		try
		{
			program.getBlock().execute(context);
			Object value=context.getVariablesManager().getLocalVariable(Name.instanceOf("rows"));
			assertTrue(value instanceof Collection);
			@SuppressWarnings("unchecked")
			Collection<Object> collection=(Collection<Object>)value;
			assertEquals(1, collection.size());
			assertEquals("121\r\n", myOut.toString());
		}
		catch (VariableNotFoundException | ExecutionException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
		context.getVariablesManager().popLocalContext();
	}

	@Test
	public void parseTwoInstructions()
		throws IOException
	{
		String code="a=10;b=20";
		Program program=test(code);
		MyContext context=createContext();
		context.getVariablesManager().pushLocalContext(new FlexibleVariablesContext());
		try
		{
			program.getBlock().execute(context);
			assertEquals(10L, context.getVariablesManager().getGlobalVariable(Name.instanceOf("a")));
			assertEquals(20L, context.getVariablesManager().getGlobalVariable(Name.instanceOf("b")));
		}
		catch (ExecutionException | VariableNotFoundException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
		finally
		{
			context.getVariablesManager().popLocalContext();
		}
	}

	@Test
	public void parseArrayDeclarationWithOneItem()
		throws IOException
	{
		String code="var days=[\"monday\"]; echoout days[0]";
		Program program=test(code);
		MyOpenContext context=new MyOpenContext();
		ScriptRuntime runtime=new ScriptRuntime(new File("."));
		StringWriter myOut=new StringWriter(4096);
		runtime.setOut(new PrintWriter(myOut, true));
		context.setRuntime(runtime);
		context.setVariablesManager(new FlexibleVariablesManager(new FlexibleVariablesContext()));
		context.getVariablesManager().pushLocalContext(new FlexibleVariablesContext());
		context.setEvaluatorFactory(SumaEvaluatorFactory.getInstance());
		try
		{
			program.getBlock().execute(context);
			assertEquals("monday\r\n", myOut.toString());
		}
		catch (ExecutionException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
		context.getVariablesManager().popLocalContext();
	}

	@Test
	public void parseLoopThreeItemCollection()
		throws IOException
	{
		String code="var days=[\"monday\",\"tuesday\",\"wednesday\"];for(var day:days) {echoout day}";
		Program program=test(code);
		MyOpenContext context=new MyOpenContext();
		ScriptRuntime runtime=new ScriptRuntime(new File("."));
		StringWriter myOut=new StringWriter(4096);
		runtime.setOut(new PrintWriter(myOut, true));
		StringWriter myErr=new StringWriter(4096);
		runtime.setErr(new PrintWriter(myErr, true));
		context.setRuntime(runtime);
		context.setVariablesManager(new FlexibleVariablesManager(new FlexibleVariablesContext()));
		context.getVariablesManager().pushLocalContext(new FlexibleVariablesContext());
		context.setEvaluatorFactory(SumaEvaluatorFactory.getInstance());
		try
		{
			program.getBlock().execute(context);
			assertEquals("monday\r\ntuesday\r\nwednesday\r\n", myOut.toString());
		}
		catch (ExecutionException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
		context.getVariablesManager().popLocalContext();
	}

	@Test
	public void parseLoopThreeItemMapCollection()
		throws IOException
	{
		String code="var dates=[{day:\"monday\",num:21},{day:\"tuesday\",num:22},{day:\"wednesday\",num:23}];for(var date:dates) {echoout date.day}";
		Program program=test(code);
		MyOpenContext context=new MyOpenContext();
		ScriptRuntime runtime=new ScriptRuntime(new File("."));
		StringWriter myOut=new StringWriter(4096);
		runtime.setOut(new PrintWriter(myOut, true));
		StringWriter myErr=new StringWriter(4096);
		runtime.setErr(new PrintWriter(myErr, true));
		context.setRuntime(runtime);
		context.setVariablesManager(new FlexibleVariablesManager(new FlexibleVariablesContext()));
		context.getVariablesManager().pushLocalContext(new FlexibleVariablesContext());
		context.setEvaluatorFactory(SumaEvaluatorFactory.getInstance());
		try
		{
			program.getBlock().execute(context);
			assertEquals("monday\r\ntuesday\r\nwednesday\r\n", myOut.toString());
		}
		catch (ExecutionException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
		context.getVariablesManager().popLocalContext();
	}

	@Test
	public void parseCallToConstant()
		throws IOException
	{
		String code="var f=120;x=f('pollos');";
		Program program=test(code);
		MyContext context=createContext();
		try
		{
			BlockInstruction block=program.getBlock();
			block.execute(context);
			Object returnValue=context.getVariablesManager().getLocalVariable(Name.instanceOf("x"));
			assertEquals(120L, returnValue);
		}
		catch (VariableNotFoundException | ExecutionException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}
}
