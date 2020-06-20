package com.samajackun.sumascript.parser;

public class SumaParserTest
{
	// private Program test(String code)
	// throws IOException
	// {
	// try
	// {
	// Program program=SumaParser.getInstance().parse(new StringReader(code));
	// return program;
	// }
	// catch (SumaParseException e)
	// {
	// e.printStackTrace();
	// fail(e.toString());
	// return null;
	// }
	// }
	//
	// @Test
	// public void parseAssignation()
	// throws IOException
	// {
	// String code="a=10;";
	// Program program=test(code);
	// MyContext context=new MyContext();
	// context.getVariablesManager().pushLocalContext(new FlexibleVariablesContext());
	// try
	// {
	// program.getBlock().execute(context);
	// assertEquals(10L, context.getVariablesManager().getGlobalVariable(Name.instanceOf("a")));
	// }
	// catch (ExecutionException | VariableNotFoundException e)
	// {
	// e.printStackTrace();
	// fail(e.toString());
	// }
	// finally
	// {
	// context.getVariablesManager().popLocalContext();
	// }
	// }
	//
	// @Test
	// public void parseVarWithOneVariable()
	// throws IOException
	// {
	// String code="var a";
	// Program program=test(code);
	// MyContext context=new MyContext();
	// context.getVariablesManager().pushLocalContext(new FlexibleVariablesContext());
	// try
	// {
	// program.getBlock().execute(context);
	// assertSame(context.getVariablesManager().getLocalVariablesContext(), context.getVariablesManager().getVariablesContext(Name.instanceOf("a")));
	// }
	// catch (VariableNotFoundException | ExecutionException e)
	// {
	// e.printStackTrace();
	// fail(e.toString());
	// }
	// context.getVariablesManager().popLocalContext();
	// }
	//
	// @Test
	// public void parseVarWithOneMultiply()
	// throws IOException
	// {
	// String code="var a=1*2";
	// Program program=test(code);
	// MyContext context=new MyContext();
	// try
	// {
	// program.getBlock().execute(context);
	// assertEquals(2L, context.getVariablesManager().getVariablesContext(Name.instanceOf("a")).get(Name.instanceOf("a")));
	// }
	// catch (VariableNotFoundException | ExecutionException e)
	// {
	// e.printStackTrace();
	// fail(e.toString());
	// }
	// }
	//
	// @Test
	// public void parseVarWithSomeVariablesNotInitialized()
	// throws IOException
	// {
	// String code="var a,b";
	// Program program=test(code);
	// MyContext context=new MyContext();
	// context.getVariablesManager().pushLocalContext(new FlexibleVariablesContext());
	// try
	// {
	// program.getBlock().execute(context);
	// assertSame(context.getVariablesManager().getLocalVariablesContext(), context.getVariablesManager().getVariablesContext(Name.instanceOf("a")));
	// assertSame(context.getVariablesManager().getLocalVariablesContext(), context.getVariablesManager().getVariablesContext(Name.instanceOf("b")));
	// }
	// catch (VariableNotFoundException | ExecutionException e)
	// {
	// e.printStackTrace();
	// fail(e.toString());
	// }
	// context.getVariablesManager().popLocalContext();
	// }
	//
	// @Test
	// public void parseVarWithSomeVariablesInitialized()
	// throws IOException
	// {
	// String code="var a=120,b=130";
	// Program program=test(code);
	// MyContext context=new MyContext();
	// context.getVariablesManager().pushLocalContext(new FlexibleVariablesContext());
	// try
	// {
	// program.getBlock().execute(context);
	// assertEquals(120L, context.getVariablesManager().getLocalVariable(Name.instanceOf("a")));
	// assertEquals(130L, context.getVariablesManager().getLocalVariable(Name.instanceOf("b")));
	// }
	// catch (VariableNotFoundException | ExecutionException e)
	// {
	// e.printStackTrace();
	// fail(e.toString());
	// }
	// context.getVariablesManager().popLocalContext();
	// }
	//
	// @Test
	// public void parseVarWithSomeVariablesSomeInitializedAndSomeNot()
	// throws IOException
	// {
	// String code="var a=120,b";
	// Program program=test(code);
	// MyContext context=new MyContext();
	// context.getVariablesManager().pushLocalContext(new FlexibleVariablesContext());
	// try
	// {
	// program.getBlock().execute(context);
	// assertEquals(120L, context.getVariablesManager().getLocalVariable(Name.instanceOf("a")));
	// assertSame(context.getVariablesManager().getLocalVariablesContext(), context.getVariablesManager().getVariablesContext(Name.instanceOf("b")));
	// }
	// catch (VariableNotFoundException | ExecutionException e)
	// {
	// e.printStackTrace();
	// fail(e.toString());
	// }
	// context.getVariablesManager().popLocalContext();
	// }
	//
	// @Test
	// public void parseVarAndInitialization()
	// throws IOException
	// {
	// String code="var a=120";
	// Program program=test(code);
	// MyContext context=new MyContext();
	// context.getVariablesManager().pushLocalContext(new FlexibleVariablesContext());
	// try
	// {
	// program.getBlock().execute(context);
	// assertEquals(120L, context.getVariablesManager().getLocalVariable(Name.instanceOf("a")));
	// }
	// catch (VariableNotFoundException | ExecutionException e)
	// {
	// e.printStackTrace();
	// fail(e.toString());
	// }
	// context.getVariablesManager().popLocalContext();
	// }
	//
	// @Test
	// public void parseIfWithEmptyBlock()
	// throws IOException
	// {
	// String code="if (a=120) {}";
	// Program program=test(code);
	// MyContext context=new MyContext();
	// context.getVariablesManager().pushLocalContext(new FlexibleVariablesContext());
	// context.getVariablesManager().setLocalVariable(Name.instanceOf("a"), 120);
	// try
	// {
	// program.getBlock().execute(context);
	// assertEquals(120, context.getVariablesManager().getLocalVariable(Name.instanceOf("a")));
	// }
	// catch (VariableNotFoundException | ExecutionException e)
	// {
	// e.printStackTrace();
	// fail(e.toString());
	// }
	// context.getVariablesManager().popLocalContext();
	// }
	//
	// @Test
	// public void parseIfWithPositiveInstruction()
	// throws IOException
	// {
	// String code="if (a=120) {b=1}";
	// Program program=test(code);
	// MyContext context=new MyContext();
	// context.getVariablesManager().pushLocalContext(new FlexibleVariablesContext());
	// context.getVariablesManager().setLocalVariable(Name.instanceOf("a"), 120);
	// try
	// {
	// program.getBlock().execute(context);
	// assertEquals(1L, context.getVariablesManager().getGlobalVariable(Name.instanceOf("b")));
	// }
	// catch (VariableNotFoundException | ExecutionException e)
	// {
	// e.printStackTrace();
	// fail(e.toString());
	// }
	// context.getVariablesManager().popLocalContext();
	// }
	//
	// @Test
	// public void parseIfWithNegativeInstruction()
	// throws IOException
	// {
	// String code="if (a=120) {} else {b=1}";
	// Program program=test(code);
	// MyContext context=new MyContext();
	// context.getVariablesManager().pushLocalContext(new FlexibleVariablesContext());
	// context.getVariablesManager().setLocalVariable(Name.instanceOf("a"), 0);
	// try
	// {
	// program.getBlock().execute(context);
	// assertEquals(1L, context.getVariablesManager().getGlobalVariable(Name.instanceOf("b")));
	// }
	// catch (VariableNotFoundException | ExecutionException e)
	// {
	// e.printStackTrace();
	// fail(e.toString());
	// }
	// context.getVariablesManager().popLocalContext();
	// }
	//
	// @Test
	// public void parseBlockEmpty()
	// throws IOException
	// {
	// String code="{}";
	// Program program=test(code);
	// MyContext context=new MyContext();
	// try
	// {
	// program.getBlock().execute(context);
	// }
	// catch (ExecutionException e)
	// {
	// e.printStackTrace();
	// fail(e.toString());
	// }
	// }
	//
	// @Test
	// public void parseBlockWithOneInstruction()
	// throws IOException
	// {
	// String code="{a=1}";
	// Program program=test(code);
	// MyContext context=new MyContext();
	// context.getVariablesManager().pushLocalContext(new FlexibleVariablesContext());
	// try
	// {
	// program.getBlock().execute(context);
	// assertEquals(1L, context.getVariablesManager().getGlobalVariable(Name.instanceOf("a")));
	// }
	// catch (ExecutionException | VariableNotFoundException e)
	// {
	// e.printStackTrace();
	// fail(e.toString());
	// }
	// finally
	// {
	// context.getVariablesManager().popLocalContext();
	// }
	// }
	//
	// @Test
	// public void parseBlockWithOneInstructionAndSemicolon()
	// throws IOException
	// {
	// String code="{a=1;}";
	// Program program=test(code);
	// MyContext context=new MyContext();
	// context.getVariablesManager().pushLocalContext(new FlexibleVariablesContext());
	// try
	// {
	// program.getBlock().execute(context);
	// assertEquals(1L, context.getVariablesManager().getGlobalVariable(Name.instanceOf("a")));
	// }
	// catch (ExecutionException | VariableNotFoundException e)
	// {
	// e.printStackTrace();
	// fail(e.toString());
	// }
	// finally
	// {
	// context.getVariablesManager().popLocalContext();
	// }
	// }
	//
	// @Test
	// public void parseBlockWithTwoInstructions()
	// throws IOException
	// {
	// String code="{a=1;b=2}";
	// Program program=test(code);
	// MyContext context=new MyContext();
	// context.getVariablesManager().pushLocalContext(new FlexibleVariablesContext());
	// try
	// {
	// program.getBlock().execute(context);
	// assertEquals(1L, context.getVariablesManager().getGlobalVariable(Name.instanceOf("a")));
	// assertEquals(2L, context.getVariablesManager().getGlobalVariable(Name.instanceOf("b")));
	// }
	// catch (ExecutionException | VariableNotFoundException e)
	// {
	// e.printStackTrace();
	// fail(e.toString());
	// }
	// finally
	// {
	// context.getVariablesManager().popLocalContext();
	// }
	// }
	//
	// @Test
	// public void parseSwitchOnFirstCase()
	// throws IOException
	// {
	// String code="switch ( a ) {case 0: b=10; case 1: b=11; case 2: b=12; default: b=-1}";
	// Program program=test(code);
	// MyContext context=new MyContext();
	// context.getVariablesManager().pushLocalContext(new FlexibleVariablesContext());
	// context.getVariablesManager().setLocalVariable(Name.instanceOf("a"), 0L);
	// try
	// {
	// program.getBlock().execute(context);
	// assertEquals(10L, context.getVariablesManager().getGlobalVariable(Name.instanceOf("b")));
	// }
	// catch (ExecutionException | VariableNotFoundException e)
	// {
	// e.printStackTrace();
	// fail(e.toString());
	// }
	// finally
	// {
	// context.getVariablesManager().popLocalContext();
	// }
	// }
	//
	// @Test
	// public void parseSwitchOnSecondCase()
	// throws IOException
	// {
	// String code="switch ( a ) {case 0: b=10; case 1: b=11; case 2: b=12; default: b=-1}";
	// Program program=test(code);
	// MyContext context=new MyContext();
	// context.getVariablesManager().pushLocalContext(new FlexibleVariablesContext());
	// context.getVariablesManager().setLocalVariable(Name.instanceOf("a"), 1L);
	// try
	// {
	// program.getBlock().execute(context);
	// assertEquals(11L, context.getVariablesManager().getGlobalVariable(Name.instanceOf("b")));
	// }
	// catch (ExecutionException | VariableNotFoundException e)
	// {
	// e.printStackTrace();
	// fail(e.toString());
	// }
	// finally
	// {
	// context.getVariablesManager().popLocalContext();
	// }
	// }
	//
	// @Test
	// public void parseSwitchOnDefaultCase()
	// throws IOException
	// {
	// String code="switch ( a ) {case 0: b=10; case 1: b=11; case 2: b=12; default: b=-1}";
	// Program program=test(code);
	// MyContext context=new MyContext();
	// context.getVariablesManager().pushLocalContext(new FlexibleVariablesContext());
	// context.getVariablesManager().setLocalVariable(Name.instanceOf("a"), 112L);
	// try
	// {
	// program.getBlock().execute(context);
	// assertEquals(-1L, context.getVariablesManager().getGlobalVariable(Name.instanceOf("b")));
	// }
	// catch (ExecutionException | VariableNotFoundException e)
	// {
	// e.printStackTrace();
	// fail(e.toString());
	// }
	// finally
	// {
	// context.getVariablesManager().popLocalContext();
	// }
	// }
	//
	// @Test
	// public void parseCollectionLoopEmpty()
	// throws IOException
	// {
	// String code="for (var a:set) {}";
	// Program program=test(code);
	// MyContext context=new MyContext();
	// context.getVariablesManager().pushLocalContext(new FlexibleVariablesContext());
	// context.getVariablesManager().setLocalVariable(Name.instanceOf("a"), Arrays.asList("january", "february", "march"));
	// try
	// {
	// program.getBlock().execute(context);
	// }
	// catch (ExecutionException e)
	// {
	// e.printStackTrace();
	// fail(e.toString());
	// }
	// context.getVariablesManager().popLocalContext();
	// }
	//
	// @Test
	// public void parseCollectionLoopWithOneLine()
	// throws IOException
	// {
	// String code="for (var a:set) {i=i+1}";
	// Program program=test(code);
	// MyContext context=new MyContext();
	// context.getVariablesManager().pushLocalContext(new FlexibleVariablesContext());
	// context.getVariablesManager().setLocalVariable(Name.instanceOf("set"), Arrays.asList("january", "february", "march"));
	// context.getVariablesManager().setLocalVariable(Name.instanceOf("i"), 0L);
	// try
	// {
	// program.getBlock().execute(context);
	// assertEquals(3L, context.getVariablesManager().getLocalVariable(Name.instanceOf("i")));
	// }
	// catch (ExecutionException | VariableNotFoundException e)
	// {
	// e.printStackTrace();
	// fail(e.toString());
	// }
	// context.getVariablesManager().popLocalContext();
	// }
	//
	// @Test
	// public void parseCollectionLoopWithPreWhile()
	// throws IOException
	// {
	// String code="for (var a:set) while (i<3) {i=i+1}";
	// Program program=test(code);
	// MyContext context=new MyContext();
	// context.getVariablesManager().pushLocalContext(new FlexibleVariablesContext());
	// context.getVariablesManager().setLocalVariable(Name.instanceOf("set"), Arrays.asList("january", "february", "march", "april", "may", "june"));
	// context.getVariablesManager().setLocalVariable(Name.instanceOf("i"), 3L);
	// try
	// {
	// program.getBlock().execute(context);
	// assertEquals(3L, context.getVariablesManager().getLocalVariable(Name.instanceOf("i")));
	// }
	// catch (ExecutionException | VariableNotFoundException e)
	// {
	// e.printStackTrace();
	// fail(e.toString());
	// }
	// context.getVariablesManager().popLocalContext();
	// }
	//
	// @Test
	// public void parseCollectionLoopWithPostWhile()
	// throws IOException
	// {
	// String code="for (var a:set) {i=i+1} while (i<3)";
	// Program program=test(code);
	// MyContext context=new MyContext();
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
	//
	// @SuppressWarnings("unchecked")
	// @Test
	// public void parseDereferencingOneLevel()
	// throws IOException
	// {
	// String code="month.name='january'";
	// Program program=test(code);
	// MyContext context=new MyContext();
	// context.getVariablesManager().pushLocalContext(new FlexibleVariablesContext());
	// context.getVariablesManager().setLocalVariable(Name.instanceOf("month"), new HashMap<>());
	// try
	// {
	// program.getBlock().execute(context);
	// assertEquals("january", ((Map<Object, Object>)context.getVariablesManager().getLocalVariable(Name.instanceOf("month"))).get(Name.instanceOf("name")));
	// }
	// catch (VariableNotFoundException | ExecutionException e)
	// {
	// e.printStackTrace();
	// fail(e.toString());
	// }
	// context.getVariablesManager().popLocalContext();
	// }
	//
	// @Test
	// public void parseVarPlusPlus()
	// throws IOException
	// {
	// String code="a++";
	// Program program=test(code);
	// MyContext context=new MyContext();
	// context.getVariablesManager().pushLocalContext(new FlexibleVariablesContext());
	// context.getVariablesManager().setLocalVariable(Name.instanceOf("a"), 120);
	// try
	// {
	// program.getBlock().execute(context);
	// assertEquals(121, context.getVariablesManager().getVariablesContext(Name.instanceOf("a")).get(Name.instanceOf("a")));
	// }
	// catch (VariableNotFoundException | ExecutionException e)
	// {
	// e.printStackTrace();
	// fail(e.toString());
	// }
	// context.getVariablesManager().popLocalContext();
	// }
	//
	// @Test
	// public void parseVarMinusMinus()
	// throws IOException
	// {
	// String code="a--";
	// Program program=test(code);
	// MyContext context=new MyContext();
	// context.getVariablesManager().pushLocalContext(new FlexibleVariablesContext());
	// context.getVariablesManager().setLocalVariable(Name.instanceOf("a"), 120);
	// try
	// {
	// program.getBlock().execute(context);
	// assertEquals(119, context.getVariablesManager().getVariablesContext(Name.instanceOf("a")).get(Name.instanceOf("a")));
	// }
	// catch (VariableNotFoundException | ExecutionException e)
	// {
	// e.printStackTrace();
	// fail(e.toString());
	// }
	// context.getVariablesManager().popLocalContext();
	// }
	//
	// @Test
	// public void parseVarAddAssignation()
	// throws IOException
	// {
	// String code="a+=11";
	// Program program=test(code);
	// MyContext context=new MyContext();
	// context.getVariablesManager().pushLocalContext(new FlexibleVariablesContext());
	// context.getVariablesManager().setLocalVariable(Name.instanceOf("a"), 120);
	// try
	// {
	// program.getBlock().execute(context);
	// assertEquals(131L, context.getVariablesManager().getVariablesContext(Name.instanceOf("a")).get(Name.instanceOf("a")));
	// }
	// catch (VariableNotFoundException | ExecutionException e)
	// {
	// e.printStackTrace();
	// fail(e.toString());
	// }
	// context.getVariablesManager().popLocalContext();
	// }
	//
	// @Test
	// public void parseVarSubstractAssignation()
	// throws IOException
	// {
	// String code="a-=11";
	// Program program=test(code);
	// MyContext context=new MyContext();
	// context.getVariablesManager().pushLocalContext(new FlexibleVariablesContext());
	// context.getVariablesManager().setLocalVariable(Name.instanceOf("a"), 120);
	// try
	// {
	// program.getBlock().execute(context);
	// assertEquals(109L, context.getVariablesManager().getVariablesContext(Name.instanceOf("a")).get(Name.instanceOf("a")));
	// }
	// catch (VariableNotFoundException | ExecutionException e)
	// {
	// e.printStackTrace();
	// fail(e.toString());
	// }
	// context.getVariablesManager().popLocalContext();
	// }
	//
	// @Test
	// public void parseVarMultiplyAssignation()
	// throws IOException
	// {
	// String code="a*=2";
	// Program program=test(code);
	// MyContext context=new MyContext();
	// context.getVariablesManager().pushLocalContext(new FlexibleVariablesContext());
	// context.getVariablesManager().setLocalVariable(Name.instanceOf("a"), 120);
	// try
	// {
	// program.getBlock().execute(context);
	// assertEquals(240L, context.getVariablesManager().getVariablesContext(Name.instanceOf("a")).get(Name.instanceOf("a")));
	// }
	// catch (VariableNotFoundException | ExecutionException e)
	// {
	// e.printStackTrace();
	// fail(e.toString());
	// }
	// context.getVariablesManager().popLocalContext();
	// }
	//
	// @Test
	// public void parseVarDivideAssignation()
	// throws IOException
	// {
	// String code="a/=3";
	// Program program=test(code);
	// MyContext context=new MyContext();
	// context.getVariablesManager().pushLocalContext(new FlexibleVariablesContext());
	// context.getVariablesManager().setLocalVariable(Name.instanceOf("a"), 120);
	// try
	// {
	// program.getBlock().execute(context);
	// assertEquals(40L, context.getVariablesManager().getVariablesContext(Name.instanceOf("a")).get(Name.instanceOf("a")));
	// }
	// catch (VariableNotFoundException | ExecutionException e)
	// {
	// e.printStackTrace();
	// fail(e.toString());
	// }
	// context.getVariablesManager().popLocalContext();
	// }
	//
	// @Test
	// public void parseFunctionCallAsLeftSide()
	// throws IOException
	// {
	// String code="myfunction()";
	// Program program=test(code);
	// MyContext context=new MyContext();
	// context.getVariablesManager().pushLocalContext(new FlexibleVariablesContext());
	// context.getVariablesManager().setLocalVariable(Name.instanceOf("myfunction"), new Function()
	// {
	// @Override
	// public Object call(Context context, List<Object> argumentValues)
	// throws FunctionEvaluationException
	// {
	// context.getVariablesManager().setGlobalVariable(Name.instanceOf("a"), 120L);
	// return null;
	// }
	// });
	// try
	// {
	// BlockInstruction block=program.getBlock();
	// block.execute(context);
	// assertEquals(120L, context.getVariablesManager().getVariablesContext(Name.instanceOf("a")).get(Name.instanceOf("a")));
	// }
	// catch (VariableNotFoundException | ExecutionException e)
	// {
	// e.printStackTrace();
	// fail(e.toString());
	// }
	// context.getVariablesManager().popLocalContext();
	// }
	//
	// @Test
	// public void parseFunctionCallAsRightSide()
	// throws IOException
	// {
	// String code="a=myfunction()";
	// Program program=test(code);
	// MyContext context=new MyContext();
	// context.getVariablesManager().pushLocalContext(new FlexibleVariablesContext());
	// context.getVariablesManager().setLocalVariable(Name.instanceOf("myfunction"), new Function()
	// {
	// @Override
	// public Object call(Context context, List<Object> argumentValues)
	// throws FunctionEvaluationException
	// {
	// return 120L;
	// }
	// });
	// try
	// {
	// BlockInstruction block=program.getBlock();
	// block.execute(context);
	// assertEquals(120L, context.getVariablesManager().getVariablesContext(Name.instanceOf("a")).get(Name.instanceOf("a")));
	// }
	// catch (VariableNotFoundException | ExecutionException e)
	// {
	// e.printStackTrace();
	// fail(e.toString());
	// }
	// context.getVariablesManager().popLocalContext();
	// }
	//
	// @Test
	// public void parseFunctionCallAsRightSideWithOneArgument()
	// throws IOException
	// {
	// String code="a=myfunction(12)";
	// Program program=test(code);
	// MyContext context=new MyContext();
	// context.getVariablesManager().pushLocalContext(new FlexibleVariablesContext());
	// context.getVariablesManager().setLocalVariable(Name.instanceOf("myfunction"), new Function()
	// {
	// @Override
	// public Object call(Context context, List<Object> argumentValues)
	// throws FunctionEvaluationException
	// {
	// return 2 * ((Number)argumentValues.get(0)).longValue();
	// }
	// });
	// try
	// {
	// BlockInstruction block=program.getBlock();
	// block.execute(context);
	// assertEquals(24L, context.getVariablesManager().getVariablesContext(Name.instanceOf("a")).get(Name.instanceOf("a")));
	// }
	// catch (VariableNotFoundException | ExecutionException e)
	// {
	// e.printStackTrace();
	// fail(e.toString());
	// }
	// context.getVariablesManager().popLocalContext();
	// }
	//
	// @Test
	// public void parseFunctionDefinition()
	// throws IOException
	// {
	// String code="function myfunction(){return 120}";
	// Program program=test(code);
	// MyContext context=new MyContext();
	// context.getVariablesManager().pushLocalContext(new FlexibleVariablesContext());
	// try
	// {
	// BlockInstruction block=program.getBlock();
	// block.execute(context);
	// Object obj=context.getVariablesManager().getLocalVariable(Name.instanceOf("myfunction"));
	// assertTrue(obj instanceof Function);
	// Function function=(Function)obj;
	// Object returnValue=function.call(context, new ArrayList<>());
	// assertEquals(120L, returnValue);
	// }
	// catch (VariableNotFoundException | ExecutionException | FunctionEvaluationException e)
	// {
	// e.printStackTrace();
	// fail(e.toString());
	// }
	// context.getVariablesManager().popLocalContext();
	// }
	//
	// @Test
	// public void parseFunctionDefinitionAsRightSide()
	// throws IOException
	// {
	// String code="myfunction=function(){return 120}";
	// Program program=test(code);
	// MyContext context=new MyContext();
	// context.getVariablesManager().pushLocalContext(new FlexibleVariablesContext());
	// try
	// {
	// BlockInstruction block=program.getBlock();
	// block.execute(context);
	// Object obj=context.getVariablesManager().getGlobalVariable(Name.instanceOf("myfunction"));
	// assertTrue(obj instanceof Function);
	// Function function=(Function)obj;
	// Object returnValue=function.call(context, new ArrayList<>());
	// assertEquals(120L, returnValue);
	// }
	// catch (VariableNotFoundException | ExecutionException | FunctionEvaluationException e)
	// {
	// e.printStackTrace();
	// fail(e.toString());
	// }
	// context.getVariablesManager().popLocalContext();
	// }
	//
	// @Test
	// public void parseFunctionDefinitionAndCallWithOneArgument()
	// throws IOException
	// {
	// String code="myfunction=function(a){return 1+a};x=myfunction(120)";
	// Program program=test(code);
	// MyContext context=new MyContext();
	// try
	// {
	// BlockInstruction block=program.getBlock();
	// block.execute(context);
	// Object returnValue=context.getVariablesManager().getLocalVariable(Name.instanceOf("x"));
	// assertEquals(121L, returnValue);
	// }
	// catch (VariableNotFoundException | ExecutionException e)
	// {
	// e.printStackTrace();
	// fail(e.toString());
	// }
	// }
	//
	// @Test
	// public void parseRecursiveFunctionDefinitionAndCallWithOneArgument()
	// throws IOException
	// {
	// String code="factorial=function(a){if (a=1) {return 1} else {return a*factorial(a-1)}};x=factorial(5)";
	// Program program=test(code);
	// MyContext context=new MyContext();
	// try
	// {
	// BlockInstruction block=program.getBlock();
	// block.execute(context);
	// Object returnValue=context.getVariablesManager().getLocalVariable(Name.instanceOf("x"));
	// assertEquals(120L, returnValue);
	// }
	// catch (VariableNotFoundException | ExecutionException e)
	// {
	// e.printStackTrace();
	// fail(e.toString());
	// }
	// }
	//
	// @Test
	// public void parseSelect()
	// throws IOException
	// {
	// String code="var a=SELECT (1+119) AS x";
	// Program program=test(code);
	// MyOpenContext context=new MyOpenContext();
	// context.setVariablesManager(new FlexibleVariablesManager(new FlexibleVariablesContext()));
	// context.getVariablesManager().pushLocalContext(new FlexibleVariablesContext());
	// try
	// {
	// program.getBlock().execute(context);
	// Object value=context.getVariablesManager().getLocalVariable(Name.instanceOf("a"));
	// assertTrue(value instanceof Cursor);
	// }
	// catch (VariableNotFoundException | ExecutionException e)
	// {
	// e.printStackTrace();
	// fail(e.toString());
	// }
	// context.getVariablesManager().popLocalContext();
	// }
	//
	// @Test
	// public void parseSelectAndOtherInstruction()
	// throws IOException
	// {
	// String code="var a=SELECT (1+119) AS x;b=2";
	// Program program=test(code);
	// MyOpenContext context=new MyOpenContext();
	// context.setVariablesManager(new FlexibleVariablesManager(new FlexibleVariablesContext()));
	// context.getVariablesManager().pushLocalContext(new FlexibleVariablesContext());
	// try
	// {
	// program.getBlock().execute(context);
	// Object value=context.getVariablesManager().getLocalVariable(Name.instanceOf("a"));
	// assertTrue(value instanceof Cursor);
	// assertEquals(2L, context.getVariablesManager().getGlobalVariable(Name.instanceOf("b")));
	// }
	// catch (VariableNotFoundException | ExecutionException e)
	// {
	// e.printStackTrace();
	// fail(e.toString());
	// }
	// context.getVariablesManager().popLocalContext();
	// }
	//
	// @Test
	// public void parseTwoInstructions()
	// throws IOException
	// {
	// String code="a=10;b=20";
	// Program program=test(code);
	// MyContext context=new MyContext();
	// context.getVariablesManager().pushLocalContext(new FlexibleVariablesContext());
	// try
	// {
	// program.getBlock().execute(context);
	// assertEquals(10L, context.getVariablesManager().getGlobalVariable(Name.instanceOf("a")));
	// assertEquals(20L, context.getVariablesManager().getGlobalVariable(Name.instanceOf("b")));
	// }
	// catch (ExecutionException | VariableNotFoundException e)
	// {
	// e.printStackTrace();
	// fail(e.toString());
	// }
	// finally
	// {
	// context.getVariablesManager().popLocalContext();
	// }
	// }
}
