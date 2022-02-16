package com.samajackun.sumascript.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.samajackun.rodas.core.eval.DummyContext;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.model.Expression;
import com.samajackun.rodas.core.model.IdentifierExpression;
import com.samajackun.rodas.parsing.parser.ParserException;
import com.samajackun.rodas.parsing.source.PushBackSource;
import com.samajackun.rodas.parsing.source.ReaderSource;
import com.samajackun.rodas.sql.parser.ParserContext;
import com.samajackun.sumascript.core.instructions.SumaEvaluatorFactory;
import com.samajackun.sumascript.core.runtime.Undefined;
import com.samajackun.sumascript.expressions.ArrayExpression;
import com.samajackun.sumascript.expressions.JsonExpression;
import com.samajackun.sumascript.tokenizer.SumaMatchingTokenizer;
import com.samajackun.sumascript.tokenizer.SumaTokenizer;

public class SumaArithmeticExpressionParserTest
{
	private Expression parse(String code)
		throws IOException,
		ParserException
	{
		SumaMatchingTokenizer tokenizer=new SumaMatchingTokenizer(new SumaTokenizer(new PushBackSource(new ReaderSource(new StringReader(code)))));
		ParserContext parserContext=new ParserContext();
		return SumaArithmeticExpressionParser.getInstance().parse(tokenizer, parserContext);
	}

	@Test
	public void jsonEmpty()
		throws ParserException,
		IOException
	{
		Expression expression=parse("{}");
		assertTrue(expression instanceof JsonExpression);
		Map<String, Expression> map=((JsonExpression)expression).getMap();
		assertEquals(0, map.size());
	}

	@Test
	public void jsonOneEntry()
		throws ParserException,
		IOException
	{
		Expression expression=parse("{month:\"january\"}");
		assertTrue(expression instanceof JsonExpression);
		Map<String, Expression> map=((JsonExpression)expression).getMap();
		assertEquals(1, map.size());
		try
		{
			assertEquals("january", map.get("month").evaluate(DummyContext.getInstance(), SumaEvaluatorFactory.getInstance()));
		}
		catch (EvaluationException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void jsonTwoEntries()
		throws ParserException,
		IOException
	{
		Expression expression=parse("{month:\"january\",day:\"monday\"}");
		assertTrue(expression instanceof JsonExpression);
		Map<String, Expression> map=((JsonExpression)expression).getMap();
		assertEquals(2, map.size());
		try
		{
			assertEquals("january", map.get("month").evaluate(DummyContext.getInstance(), SumaEvaluatorFactory.getInstance()));
			assertEquals("monday", map.get("day").evaluate(DummyContext.getInstance(), SumaEvaluatorFactory.getInstance()));
		}
		catch (EvaluationException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void jsonQuotedEntry()
		throws ParserException,
		IOException
	{
		Expression expression=parse("{\"month\":\"january\"}");
		assertTrue(expression instanceof JsonExpression);
		Map<String, Expression> map=((JsonExpression)expression).getMap();
		assertEquals(1, map.size());
		try
		{
			assertEquals("january", map.get("month").evaluate(DummyContext.getInstance(), SumaEvaluatorFactory.getInstance()));
		}
		catch (EvaluationException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void jsonIntegerEntry()
		throws ParserException,
		IOException
	{
		Expression expression=parse("{month:10}");
		assertTrue(expression instanceof JsonExpression);
		Map<String, Expression> map=((JsonExpression)expression).getMap();
		assertEquals(1, map.size());
		try
		{
			assertEquals(10L, map.get("month").evaluate(DummyContext.getInstance(), SumaEvaluatorFactory.getInstance()));
		}
		catch (EvaluationException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void jsonDecimalEntry()
		throws ParserException,
		IOException
	{
		Expression expression=parse("{month:10.23}");
		assertTrue(expression instanceof JsonExpression);
		Map<String, Expression> map=((JsonExpression)expression).getMap();
		assertEquals(1, map.size());
		try
		{
			assertEquals(10.23d, map.get("month").evaluate(DummyContext.getInstance(), SumaEvaluatorFactory.getInstance()));
		}
		catch (EvaluationException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void jsonTrueEntry()
		throws ParserException,
		IOException
	{
		Expression expression=parse("{assigned:true}");
		assertTrue(expression instanceof JsonExpression);
		Map<String, Expression> map=((JsonExpression)expression).getMap();
		assertEquals(1, map.size());
		try
		{
			assertEquals(true, map.get("assigned").evaluate(DummyContext.getInstance(), SumaEvaluatorFactory.getInstance()));
		}
		catch (EvaluationException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void jsonFalseEntry()
		throws ParserException,
		IOException
	{
		Expression expression=parse("{assigned:false}");
		assertTrue(expression instanceof JsonExpression);
		Map<String, Expression> map=((JsonExpression)expression).getMap();
		assertEquals(1, map.size());
		try
		{
			assertEquals(false, map.get("assigned").evaluate(DummyContext.getInstance(), SumaEvaluatorFactory.getInstance()));
		}
		catch (EvaluationException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void unlocatedJson()
		throws ParserException,
		IOException
	{
		Expression expression=parse("month:120");
		assertTrue(expression instanceof IdentifierExpression);
		assertEquals("month", ((IdentifierExpression)expression).getIdentifier());
	}

	@Test
	public void arrayEmpty()
		throws ParserException,
		IOException
	{
		Expression expression=parse("[]");
		assertTrue(expression instanceof ArrayExpression);
		List<Expression> list=((ArrayExpression)expression).getExpressions();
		assertEquals(0, list.size());
	}

	@Test
	public void arrayOneItem()
		throws ParserException,
		IOException
	{
		Expression expression=parse("[120]");
		assertTrue(expression instanceof ArrayExpression);
		List<Expression> list=((ArrayExpression)expression).getExpressions();
		assertEquals(1, list.size());
		try
		{
			assertEquals(120L, list.get(0).evaluate(DummyContext.getInstance(), SumaEvaluatorFactory.getInstance()));
		}
		catch (EvaluationException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void arrayTwoItems()
		throws ParserException,
		IOException
	{
		Expression expression=parse("[120,121]");
		assertTrue(expression instanceof ArrayExpression);
		List<Expression> list=((ArrayExpression)expression).getExpressions();
		assertEquals(2, list.size());
		try
		{
			assertEquals(120L, list.get(0).evaluate(DummyContext.getInstance(), SumaEvaluatorFactory.getInstance()));
			assertEquals(121L, list.get(1).evaluate(DummyContext.getInstance(), SumaEvaluatorFactory.getInstance()));
		}
		catch (EvaluationException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void arrayOneItemStringTypeDoubleQuoted()
		throws ParserException,
		IOException
	{
		Expression expression=parse("[\"april\"]");
		assertTrue(expression instanceof ArrayExpression);
		List<Expression> list=((ArrayExpression)expression).getExpressions();
		assertEquals(1, list.size());
		try
		{
			assertEquals("april", list.get(0).evaluate(DummyContext.getInstance(), SumaEvaluatorFactory.getInstance()));
		}
		catch (EvaluationException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void arrayOneItemStringTypeSingleQuoted()
		throws ParserException,
		IOException
	{
		Expression expression=parse("['april']");
		assertTrue(expression instanceof ArrayExpression);
		List<Expression> list=((ArrayExpression)expression).getExpressions();
		assertEquals(1, list.size());
		try
		{
			assertEquals("april", list.get(0).evaluate(DummyContext.getInstance(), SumaEvaluatorFactory.getInstance()));
		}
		catch (EvaluationException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void arrayOneItemNullType()
		throws ParserException,
		IOException
	{
		Expression expression=parse("[null]");
		assertTrue(expression instanceof ArrayExpression);
		List<Expression> list=((ArrayExpression)expression).getExpressions();
		assertEquals(1, list.size());
		try
		{
			assertNull(list.get(0).evaluate(DummyContext.getInstance(), SumaEvaluatorFactory.getInstance()));
		}
		catch (EvaluationException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void arrayOneItemUndefinedType()
		throws ParserException,
		IOException
	{
		Expression expression=parse("[undefined]");
		assertTrue(expression instanceof ArrayExpression);
		List<Expression> list=((ArrayExpression)expression).getExpressions();
		assertEquals(1, list.size());
		try
		{
			assertSame(Undefined.getInstance(), list.get(0).evaluate(DummyContext.getInstance(), SumaEvaluatorFactory.getInstance()));
		}
		catch (EvaluationException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void arrayOneItemIntegerType()
		throws ParserException,
		IOException
	{
		Expression expression=parse("[120]");
		assertTrue(expression instanceof ArrayExpression);
		List<Expression> list=((ArrayExpression)expression).getExpressions();
		assertEquals(1, list.size());
		try
		{
			assertEquals(120L, list.get(0).evaluate(DummyContext.getInstance(), SumaEvaluatorFactory.getInstance()));
		}
		catch (EvaluationException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void arrayOneItemDecimalType()
		throws ParserException,
		IOException
	{
		Expression expression=parse("[120.3]");
		assertTrue(expression instanceof ArrayExpression);
		List<Expression> list=((ArrayExpression)expression).getExpressions();
		assertEquals(1, list.size());
		try
		{
			assertEquals(120.3d, list.get(0).evaluate(DummyContext.getInstance(), SumaEvaluatorFactory.getInstance()));
		}
		catch (EvaluationException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void arrayOneItemTrueType()
		throws ParserException,
		IOException
	{
		Expression expression=parse("[true]");
		assertTrue(expression instanceof ArrayExpression);
		List<Expression> list=((ArrayExpression)expression).getExpressions();
		assertEquals(1, list.size());
		try
		{
			assertEquals(true, list.get(0).evaluate(DummyContext.getInstance(), SumaEvaluatorFactory.getInstance()));
		}
		catch (EvaluationException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void arrayOneItemFalseType()
		throws ParserException,
		IOException
	{
		Expression expression=parse("[false]");
		assertTrue(expression instanceof ArrayExpression);
		List<Expression> list=((ArrayExpression)expression).getExpressions();
		assertEquals(1, list.size());
		try
		{
			assertEquals(false, list.get(0).evaluate(DummyContext.getInstance(), SumaEvaluatorFactory.getInstance()));
		}
		catch (EvaluationException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void arrayThreeItemsMixedTypes()
		throws ParserException,
		IOException
	{
		Expression expression=parse("[120,\"april\",true]");
		assertTrue(expression instanceof ArrayExpression);
		List<Expression> list=((ArrayExpression)expression).getExpressions();
		assertEquals(3, list.size());
		try
		{
			assertEquals(120L, list.get(0).evaluate(DummyContext.getInstance(), SumaEvaluatorFactory.getInstance()));
			assertEquals("april", list.get(1).evaluate(DummyContext.getInstance(), SumaEvaluatorFactory.getInstance()));
			assertEquals(true, list.get(2).evaluate(DummyContext.getInstance(), SumaEvaluatorFactory.getInstance()));
		}
		catch (EvaluationException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void arrayOneItemArithmeticOperation()
		throws ParserException,
		IOException
	{
		Expression expression=parse("[1+2]");
		assertTrue(expression instanceof ArrayExpression);
		List<Expression> list=((ArrayExpression)expression).getExpressions();
		assertEquals(1, list.size());
		try
		{
			assertEquals(3L, list.get(0).evaluate(DummyContext.getInstance(), SumaEvaluatorFactory.getInstance()));
		}
		catch (EvaluationException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void arrayOneItemArrayType()
		throws ParserException,
		IOException
	{
		Expression expression=parse("[[120]]");
		assertTrue(expression instanceof ArrayExpression);
		List<Expression> list=((ArrayExpression)expression).getExpressions();
		assertEquals(1, list.size());
		try
		{
			Object item=list.get(0).evaluate(DummyContext.getInstance(), SumaEvaluatorFactory.getInstance());
			assertTrue(item instanceof List);
			@SuppressWarnings({
				"unchecked",
				"rawtypes"
			})
			List<Object> itemList=(List)item;
			assertEquals(120L, itemList.get(0));
		}
		catch (EvaluationException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

}
