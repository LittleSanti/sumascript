package com.samajackun.sumascript.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;

import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.model.Expression;
import com.samajackun.rodas.core.model.GreaterThanExpression;
import com.samajackun.rodas.core.model.IdentifierExpression;
import com.samajackun.rodas.core.model.ParehentesizedExpression;
import com.samajackun.rodas.parsing.parser.ParserException;
import com.samajackun.rodas.parsing.parser.UnexpectedTokenException;
import com.samajackun.rodas.parsing.source.CharSequenceSource;
import com.samajackun.rodas.parsing.source.PushBackSource;
import com.samajackun.rodas.parsing.tokenizer.TokenizerException;
import com.samajackun.sumascript.core.instructions.AbstractLoopInstruction;
import com.samajackun.sumascript.core.instructions.CollectionLoopInstruction;
import com.samajackun.sumascript.core.instructions.IndexedLoopInstruction;
import com.samajackun.sumascript.tokenizer.SumaMatchingTokenizer;
import com.samajackun.sumascript.tokenizer.SumaTokenTypes;
import com.samajackun.sumascript.tokenizer.SumaTokenizer;

public class LoopParserTest
{
	private SumaMatchingTokenizer tokenize(String input)
		throws TokenizerException,
		IOException
	{
		return new SumaMatchingTokenizer(new SumaTokenizer(new PushBackSource(new CharSequenceSource(input))));
	}

	private SumaParserContext createParserContext()
	{
		return new SumaParserContext();
	}

	@Test
	public void parseLocalOrGlobalCollectionLoopClauseWithEmpty()
		throws TokenizerException,
		IOException
	{
		String input=";{";
		SumaMatchingTokenizer tokenizer=tokenize(input);
		try
		{
			CollectionLoopClause clause=LoopParser.getInstance().parseLocalOrGlobalCollectionLoopClause(tokenizer, createParserContext());
			assertNull(clause);
			assertEquals(SumaTokenTypes.SEMICOLON, tokenizer.nextToken().getType());
		}
		catch (ParserException e)
		{
			fail(e.toString());
		}
	}

	@Test
	public void parseLocalOrGlobalCollectionLoopClauseWithLocalDeclaration()
		throws TokenizerException,
		IOException
	{
		String input="var i:collection{";
		SumaMatchingTokenizer tokenizer=tokenize(input);
		try
		{
			CollectionLoopClause clause=LoopParser.getInstance().parseLocalOrGlobalCollectionLoopClause(tokenizer, createParserContext());
			assertTrue(clause.getCollection() instanceof IdentifierExpression);
			assertEquals("i", clause.getVarName().asString());
			assertEquals("collection", ((IdentifierExpression)clause.getCollection()).getIdentifier());
			assertTrue(clause.isLocal());
			assertEquals(SumaTokenTypes.KEY_START, tokenizer.nextToken().getType());
		}
		catch (ParserException e)
		{
			fail(e.toString());
		}
	}

	@Test
	public void parseLocalOrGlobalCollectionLoopClauseWithGlobalDeclaration()
		throws TokenizerException,
		IOException
	{
		String input="i:collection{";
		SumaMatchingTokenizer tokenizer=tokenize(input);
		try
		{
			CollectionLoopClause clause=LoopParser.getInstance().parseLocalOrGlobalCollectionLoopClause(tokenizer, createParserContext());
			assertTrue(clause.getCollection() instanceof IdentifierExpression);
			assertEquals("i", clause.getVarName().asString());
			assertEquals("collection", ((IdentifierExpression)clause.getCollection()).getIdentifier());
			assertFalse(clause.isLocal());
			assertEquals(SumaTokenTypes.KEY_START, tokenizer.nextToken().getType());
		}
		catch (ParserException e)
		{
			fail(e.toString());
		}
	}

	@Test
	public void parseLocalOrGlobalCollectionLoopClauseWithLocalVarInitialization()
		throws TokenizerException,
		IOException
	{
		String input="var i=1";
		SumaMatchingTokenizer tokenizer=tokenize(input);
		try
		{
			CollectionLoopClause clause=LoopParser.getInstance().parseLocalOrGlobalCollectionLoopClause(tokenizer, createParserContext());
			assertNull(clause);
			assertEquals(SumaTokenTypes.KEYWORD_VAR, tokenizer.nextToken().getType());
		}
		catch (ParserException e)
		{
			fail(e.toString());
		}
	}

	@Test
	public void parseLocalOrGlobalCollectionLoopClauseWithGlobalVarInitialization()
		throws TokenizerException,
		IOException
	{
		String input="i=1";
		SumaMatchingTokenizer tokenizer=tokenize(input);
		try
		{
			CollectionLoopClause clause=LoopParser.getInstance().parseLocalOrGlobalCollectionLoopClause(tokenizer, createParserContext());
			assertNull(clause);
			assertEquals(SumaTokenTypes.IDENTIFIER, tokenizer.nextToken().getType());
		}
		catch (ParserException e)
		{
			fail(e.toString());
		}
	}

	@Test
	public void parseLocalOrGlobalCollectionLoopClauseWithWrongLocalInitialization()
		throws TokenizerException,
		IOException
	{
		String input="var 12";
		SumaMatchingTokenizer tokenizer=tokenize(input);
		try
		{
			LoopParser.getInstance().parseLocalOrGlobalCollectionLoopClause(tokenizer, createParserContext());
		}
		catch (UnexpectedTokenException e)
		{
			assertEquals(SumaTokenTypes.INTEGER_NUMBER_LITERAL, e.getToken().getType());
		}
		catch (ParserException e)
		{
			fail(e.toString());
		}
	}

	@Test
	public void parseWhileLoopClauseWithEmpty()
		throws TokenizerException,
		IOException
	{
		String input="{";
		SumaMatchingTokenizer tokenizer=tokenize(input);
		try
		{
			Expression expression=LoopParser.getInstance().parseWhileLoopClause(tokenizer, createParserContext());
			assertNull(expression);
			assertEquals(SumaTokenTypes.KEY_START, tokenizer.nextToken().getType());
		}
		catch (ParserException e)
		{
			fail(e.toString());
		}
	}

	@Test
	public void parseWhileLoopClauseWithCompleteWhile()
		throws TokenizerException,
		IOException
	{
		String input="while (a>1){";
		SumaMatchingTokenizer tokenizer=tokenize(input);
		try
		{
			Expression expression=LoopParser.getInstance().parseWhileLoopClause(tokenizer, createParserContext());
			assertTrue(expression instanceof ParehentesizedExpression);
			assertTrue(((ParehentesizedExpression)expression).getExpression() instanceof GreaterThanExpression);
			assertEquals(SumaTokenTypes.KEY_START, tokenizer.nextToken().getType());
		}
		catch (ParserException e)
		{
			fail(e.toString());
		}
	}

	@Test
	public void parseForLoopWithVariablesDeclaration()
		throws TokenizerException,
		IOException
	{
		String input="(var i=0;i<10;i++){echoout(i)}";
		SumaMatchingTokenizer tokenizer=tokenize(input);
		try
		{
			AbstractLoopInstruction loopInstruction=LoopParser.getInstance().parseForLoop(tokenizer, createParserContext());
			assertTrue(loopInstruction instanceof IndexedLoopInstruction);
		}
		catch (ParserException | EvaluationException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void parseForLoopWithCollectionDeclaration()
		throws TokenizerException,
		IOException
	{
		String input="(var i:collection){echoout(i)}";
		SumaMatchingTokenizer tokenizer=tokenize(input);
		try
		{
			AbstractLoopInstruction loopInstruction=LoopParser.getInstance().parseForLoop(tokenizer, createParserContext());
			assertTrue(loopInstruction instanceof CollectionLoopInstruction);
		}
		catch (ParserException | EvaluationException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void parseForLoopWithCollectionDeclarationAndWhileClause()
		throws TokenizerException,
		IOException
	{
		String input="(var i:collection) while (i!=null) {echoout(i)}";
		SumaMatchingTokenizer tokenizer=tokenize(input);
		try
		{
			AbstractLoopInstruction loopInstruction=LoopParser.getInstance().parseForLoop(tokenizer, createParserContext());
			assertTrue(loopInstruction instanceof CollectionLoopInstruction);
		}
		catch (ParserException | EvaluationException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}
}
