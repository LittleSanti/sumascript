package com.samajackun.sumascript.parser;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import com.samajackun.rodas.core.model.NumericConstantExpression;
import com.samajackun.rodas.parsing.parser.ParserException;
import com.samajackun.rodas.parsing.parser.UnexpectedTokenException;
import com.samajackun.rodas.parsing.source.CharSequenceSource;
import com.samajackun.rodas.parsing.source.PushBackSource;
import com.samajackun.rodas.parsing.tokenizer.TokenizerException;
import com.samajackun.rodas.sql.parser.ParserContext;
import com.samajackun.sumascript.core.instructions.AbstractVariableAssignation;
import com.samajackun.sumascript.core.instructions.LocalVariableDeclarationAndAssignation;
import com.samajackun.sumascript.core.instructions.NearestVariableAssignation;
import com.samajackun.sumascript.tokenizer.SumaMatchingTokenizer;
import com.samajackun.sumascript.tokenizer.SumaTokenTypes;
import com.samajackun.sumascript.tokenizer.SumaTokenizer;

public class VariableParserTest
{
	private SumaMatchingTokenizer tokenize(String input)
		throws TokenizerException,
		IOException
	{
		return new SumaMatchingTokenizer(new SumaTokenizer(new PushBackSource(new CharSequenceSource(input))));
	}

	private ParserContext createParserContext()
	{
		return new ParserContext();
	}

	@Test
	public void parseVariableAssignationEmpty()
		throws TokenizerException,
		IOException
	{
		String input="{";
		SumaMatchingTokenizer tokenizer=tokenize(input);
		try
		{
			AbstractVariableAssignation variableAssignation=VariableParser.getInstance().parseVariableAssignation(tokenizer, createParserContext(), false);
			assertNull(variableAssignation);
			assertEquals(SumaTokenTypes.KEY_START, tokenizer.nextToken().getType());
		}
		catch (ParserException e)
		{
			fail(e.toString());
		}
	}

	@Test
	public void parseOneVariableDeclaration()
		throws TokenizerException,
		IOException
	{
		String input="a{";
		SumaMatchingTokenizer tokenizer=tokenize(input);
		try
		{
			AbstractVariableAssignation variableAssignation=VariableParser.getInstance().parseVariableAssignation(tokenizer, createParserContext(), false);
			assertNotNull(variableAssignation);
			assertTrue(variableAssignation instanceof NearestVariableAssignation);
			assertEquals("a", variableAssignation.getName().getBase().asString());
			assertNull(variableAssignation.getExpression());
			assertEquals(SumaTokenTypes.KEY_START, tokenizer.nextToken().getType());
		}
		catch (ParserException e)
		{
			fail(e.toString());
		}
	}

	@Test
	public void parseOneVariableDeclarationAndAssignaton()
		throws TokenizerException,
		IOException
	{
		String input="a=1{";
		SumaMatchingTokenizer tokenizer=tokenize(input);
		try
		{
			AbstractVariableAssignation variableAssignation=VariableParser.getInstance().parseVariableAssignation(tokenizer, createParserContext(), false);
			assertNotNull(variableAssignation);
			assertTrue(variableAssignation instanceof NearestVariableAssignation);
			assertEquals("a", variableAssignation.getName().getBase().asString());
			assertEquals(1L, ((NumericConstantExpression)variableAssignation.getExpression()).getNumericValue());
			assertEquals(SumaTokenTypes.KEY_START, tokenizer.nextToken().getType());
		}
		catch (ParserException e)
		{
			fail(e.toString());
		}
	}

	@Test
	public void parseVariableAssignationListEmpty()
		throws TokenizerException,
		IOException
	{
		String input="{";
		SumaMatchingTokenizer tokenizer=tokenize(input);
		try
		{
			List<AbstractVariableAssignation> variableAssignations=VariableParser.getInstance().parseVariableAssignationList(tokenizer, createParserContext());
			assertTrue(variableAssignations.isEmpty());
			assertEquals(SumaTokenTypes.KEY_START, tokenizer.nextToken().getType());
		}
		catch (ParserException e)
		{
			fail(e.toString());
		}
	}

	@Test
	public void parseVariableAssignationListWithOneVariable()
		throws TokenizerException,
		IOException
	{
		String input="a{";
		SumaMatchingTokenizer tokenizer=tokenize(input);
		try
		{
			List<AbstractVariableAssignation> variableAssignations=VariableParser.getInstance().parseVariableAssignationList(tokenizer, createParserContext());
			assertEquals(1, variableAssignations.size());
			AbstractVariableAssignation variableAssignation;

			variableAssignation=variableAssignations.get(0);
			assertTrue(variableAssignation instanceof NearestVariableAssignation);
			assertEquals("a", variableAssignation.getName().getBase().asString());
			assertNull(variableAssignation.getExpression());

			assertEquals(SumaTokenTypes.KEY_START, tokenizer.nextToken().getType());
		}
		catch (ParserException e)
		{
			fail(e.toString());
		}
	}

	@Test
	public void parseVariableAssignationListWithTwoVariables()
		throws TokenizerException,
		IOException
	{
		String input="a,b{";
		SumaMatchingTokenizer tokenizer=tokenize(input);
		try
		{
			List<AbstractVariableAssignation> variableAssignations=VariableParser.getInstance().parseVariableAssignationList(tokenizer, createParserContext());
			assertEquals(2, variableAssignations.size());
			AbstractVariableAssignation variableAssignation;

			variableAssignation=variableAssignations.get(0);
			assertTrue(variableAssignation instanceof NearestVariableAssignation);
			assertEquals("a", variableAssignation.getName().getBase().asString());
			assertNull(variableAssignation.getExpression());

			variableAssignation=variableAssignations.get(1);
			assertTrue(variableAssignation instanceof NearestVariableAssignation);
			assertEquals("b", variableAssignation.getName().getBase().asString());
			assertNull(variableAssignation.getExpression());

			assertEquals(SumaTokenTypes.KEY_START, tokenizer.nextToken().getType());
		}
		catch (ParserException e)
		{
			fail(e.toString());
		}
	}

	@Test
	public void parseVariableAssignationListWithThreeVariablesAndAssignations()
		throws TokenizerException,
		IOException
	{
		String input="a,b=2,c{";
		SumaMatchingTokenizer tokenizer=tokenize(input);
		try
		{
			List<AbstractVariableAssignation> variableAssignations=VariableParser.getInstance().parseVariableAssignationList(tokenizer, createParserContext());
			assertEquals(3, variableAssignations.size());
			AbstractVariableAssignation variableAssignation;

			variableAssignation=variableAssignations.get(0);
			assertTrue(variableAssignation instanceof NearestVariableAssignation);
			assertEquals("a", variableAssignation.getName().getBase().asString());
			assertNull(variableAssignation.getExpression());

			variableAssignation=variableAssignations.get(1);
			assertTrue(variableAssignation instanceof NearestVariableAssignation);
			assertEquals("b", variableAssignation.getName().getBase().asString());
			assertEquals(2L, ((NumericConstantExpression)variableAssignation.getExpression()).getNumericValue());

			variableAssignation=variableAssignations.get(2);
			assertTrue(variableAssignation instanceof NearestVariableAssignation);
			assertEquals("c", variableAssignation.getName().getBase().asString());
			assertNull(variableAssignation.getExpression());

			assertEquals(SumaTokenTypes.KEY_START, tokenizer.nextToken().getType());
		}
		catch (ParserException e)
		{
			fail(e.toString());
		}
	}

	@Test
	public void parseVariableAssignationListWithThreeLocalVariablesAndAssignations()
		throws TokenizerException,
		IOException
	{
		String input="var a,b=2,c{";
		SumaMatchingTokenizer tokenizer=tokenize(input);
		try
		{
			List<AbstractVariableAssignation> variableAssignations=VariableParser.getInstance().parseVariableAssignationList(tokenizer, createParserContext());
			assertEquals(3, variableAssignations.size());
			AbstractVariableAssignation variableAssignation;

			variableAssignation=variableAssignations.get(0);
			assertTrue(variableAssignation instanceof LocalVariableDeclarationAndAssignation);
			assertEquals("a", variableAssignation.getName().getBase().asString());
			assertNull(variableAssignation.getExpression());

			variableAssignation=variableAssignations.get(1);
			assertTrue(variableAssignation instanceof LocalVariableDeclarationAndAssignation);
			assertEquals("b", variableAssignation.getName().getBase().asString());
			assertEquals(2L, ((NumericConstantExpression)variableAssignation.getExpression()).getNumericValue());

			variableAssignation=variableAssignations.get(2);
			assertTrue(variableAssignation instanceof LocalVariableDeclarationAndAssignation);
			assertEquals("c", variableAssignation.getName().getBase().asString());
			assertNull(variableAssignation.getExpression());

			assertEquals(SumaTokenTypes.KEY_START, tokenizer.nextToken().getType());
		}
		catch (ParserException e)
		{
			fail(e.toString());
		}
	}

	@Test
	public void parseVariableAssignationListWithOneLocalVariableDeclaration()
		throws TokenizerException,
		IOException
	{
		String input="var a{";
		SumaMatchingTokenizer tokenizer=tokenize(input);
		try
		{
			List<AbstractVariableAssignation> variableAssignations=VariableParser.getInstance().parseVariableAssignationList(tokenizer, createParserContext());
			assertEquals(1, variableAssignations.size());
			AbstractVariableAssignation variableAssignation;

			variableAssignation=variableAssignations.get(0);
			assertTrue(variableAssignation instanceof LocalVariableDeclarationAndAssignation);
			assertEquals("a", variableAssignation.getName().getBase().asString());
			assertNull(variableAssignation.getExpression());

			assertEquals(SumaTokenTypes.KEY_START, tokenizer.nextToken().getType());
		}
		catch (ParserException e)
		{
			fail(e.toString());
		}
	}

	@Test
	public void parseVariableAssignationListWithZeroLocalVariables()
		throws TokenizerException,
		IOException
	{
		String input="var {";
		SumaMatchingTokenizer tokenizer=tokenize(input);
		try
		{
			VariableParser.getInstance().parseVariableAssignationList(tokenizer, createParserContext());
			fail("Expected UnexpectedTokenException");
		}
		catch (UnexpectedTokenException e)
		{
			assertEquals(SumaTokenTypes.KEY_START, e.getToken().getType());
			assertArrayEquals(new String[] {
				SumaTokenTypes.IDENTIFIER
			}, e.getExpected());
		}
		catch (ParserException e)
		{
			fail(e.toString());
		}
	}
}
