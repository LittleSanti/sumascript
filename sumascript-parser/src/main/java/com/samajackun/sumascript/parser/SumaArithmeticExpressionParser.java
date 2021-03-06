package com.samajackun.sumascript.parser;

import java.io.IOException;
import java.util.LinkedHashMap;

import com.samajackun.rodas.core.model.Expression;
import com.samajackun.rodas.core.model.FunctionCallExpression;
import com.samajackun.rodas.core.model.IdentifierExpression;
import com.samajackun.rodas.core.model.TextConstantExpression;
import com.samajackun.rodas.parsing.parser.ParserException;
import com.samajackun.rodas.parsing.parser.UnexpectedTokenException;
import com.samajackun.rodas.sql.parser.GenericArithmeticExpressionParser;
import com.samajackun.rodas.sql.parser.ParserContext;
import com.samajackun.rodas.sql.tokenizer.AbstractMatchingTokenizer;
import com.samajackun.rodas.sql.tokenizer.SqlTokenTypes;
import com.samajackun.rodas.sql.tokenizer.Token;
import com.samajackun.sumascript.core.expressions.IndexedExpression;
import com.samajackun.sumascript.core.expressions.ScopedExpression;
import com.samajackun.sumascript.expressions.ArrayExpression;
import com.samajackun.sumascript.expressions.InstantiationCallExpression;
import com.samajackun.sumascript.expressions.JsonExpression;
import com.samajackun.sumascript.expressions.UndefinedConstantExpression;
import com.samajackun.sumascript.tokenizer.SumaMatchingTokenizer;
import com.samajackun.sumascript.tokenizer.SumaTokenTypes;

public final class SumaArithmeticExpressionParser extends GenericArithmeticExpressionParser
{
	private static final SumaArithmeticExpressionParser INSTANCE=new SumaArithmeticExpressionParser();

	private enum JsonState {
		READ_ENTRY, READ_COMMA
	};

	public static SumaArithmeticExpressionParser getInstance()
	{
		return INSTANCE;
	}

	private SumaArithmeticExpressionParser()
	{
		super(SumaParserFactory.getInstance());
	}

	@Override
	protected Expression unexpectedToken(AbstractMatchingTokenizer tokenizer, ParserContext parserContext, Token token)
		throws ParserException
	{
		Expression expression=null;
		try
		{
			switch (token.getType())
			{
				// A partir de aqu� son los casos "inyectados" en parseTerminal:
				case SumaTokenTypes.KEY_START:
					tokenizer.pushBack(token);
					expression=parseJson(tokenizer, parserContext);
					break;
				case SumaTokenTypes.BRACKET_START:
					tokenizer.pushBack(token);
					expression=parseArray(tokenizer, parserContext);
					break;
				case SumaTokenTypes.DOUBLE_QUOTED_TEXT_LITERAL:
					expression=new TextConstantExpression(token.getValue());
					break;
				case SumaTokenTypes.KEYWORD_UNDEFINED:
					expression=UndefinedConstantExpression.getInstance();
					break;
				case SumaTokenTypes.KEYWORD_FUNCTION:
					expression=SumaParser.getInstance().parseUnnamedFunctionDeclaration((SumaMatchingTokenizer)tokenizer, (SumaParserContext)parserContext);
					break;
				case SumaTokenTypes.OPERATOR_NEW:
					expression=parseInstantiationCallExpression(token, tokenizer, parserContext);
					break;
				// case SumaTokenTypes.IDENTIFIER:
				// Token token2=tokenizer.nextOptionalToken();
				// if (token2.getType().equals(SumaTokenTypes.COLON))
				// {
				// expression=new TextConstantExpression(token.getValue());
				// tokenizer.pushBack(token2);
				// }
				// else
				// {
				// throw new UnexpectedTokenException(token2, SumaTokenTypes.COLON);
				// }
				// break;
			}
			// if (expression == null)
			// {
			// throw new ParserException("Unparseable");
			// }
			return expression;
		}
		catch (IOException e)
		{
			// FIXME
			throw new ParserException(e);
		}
	}

	private JsonExpression parseJson(AbstractMatchingTokenizer tokenizer, ParserContext parserContext)
		throws ParserException,
		IOException
	{
		LinkedHashMap<String, Expression> map=new LinkedHashMap<>();
		tokenizer.matchToken(SumaTokenTypes.KEY_START);
		boolean looping=true;
		JsonState state=JsonState.READ_COMMA;
		do
		{
			Token token=tokenizer.nextToken();
			switch (state)
			{
				case READ_COMMA:
					switch (token.getType())
					{
						case SumaTokenTypes.DOUBLE_QUOTED_TEXT_LITERAL:
						case SumaTokenTypes.IDENTIFIER:
							tokenizer.matchToken(SumaTokenTypes.COLON);
							Expression value=parse(tokenizer, parserContext);
							map.put(token.getValue(), value);
							state=JsonState.READ_ENTRY;
							break;
						case SumaTokenTypes.KEY_END:
							looping=false;
							break;
						default:
							throw new UnexpectedTokenException(token, SumaTokenTypes.DOUBLE_QUOTED_TEXT_LITERAL, SumaTokenTypes.IDENTIFIER, SumaTokenTypes.KEY_END);
					}
					break;
				case READ_ENTRY:
					switch (token.getType())
					{
						case SumaTokenTypes.COMMA:
							state=JsonState.READ_COMMA;
							break;
						case SumaTokenTypes.KEY_END:
							looping=false;
							break;
						default:
							throw new UnexpectedTokenException(token, SumaTokenTypes.COMMA, SumaTokenTypes.KEY_END);
					}
					break;
			}
		}
		while (looping);
		return new JsonExpression(map);
	}

	private ArrayExpression parseArray(AbstractMatchingTokenizer tokenizer, ParserContext parserContext)
		throws ParserException,
		IOException
	{
		ArrayExpression array=new ArrayExpression();
		tokenizer.matchToken(SumaTokenTypes.BRACKET_START);
		boolean looping=true;
		JsonState state=JsonState.READ_COMMA;
		do
		{
			Token token=tokenizer.nextToken();
			switch (state)
			{
				case READ_COMMA:
					switch (token.getType())
					{
						case SumaTokenTypes.BRACKET_END:
							looping=false;
							break;
						default:
							tokenizer.pushBack(token);
							Expression value=parse(tokenizer, parserContext);
							array.add(value);
							state=JsonState.READ_ENTRY;
							break;
					}
					break;
				case READ_ENTRY:
					switch (token.getType())
					{
						case SumaTokenTypes.COMMA:
							state=JsonState.READ_COMMA;
							break;
						case SumaTokenTypes.BRACKET_END:
							looping=false;
							break;
						default:
							throw new UnexpectedTokenException(token, SumaTokenTypes.COMMA, SumaTokenTypes.BRACKET_END);
					}
					break;
			}
		}
		while (looping);
		return array;
	}

	@Override
	protected Expression unexpectedTokenAfterIdentifier(AbstractMatchingTokenizer tokenizer, ParserContext parserContext, Token identifierToken, Token unexpectedToken)
		throws ParserException,
		IOException
	{
		Expression expression;
		if (unexpectedToken.getType().equals(SumaTokenTypes.BRACKET_START))
		{
			Expression indexExpression=getParserFactory().getExpressionParser().parse(tokenizer, parserContext);
			expression=new IndexedExpression(new IdentifierExpression(identifierToken.getValue()), indexExpression);
			tokenizer.matchToken(SumaTokenTypes.BRACKET_END);
		}
		else
		{
			expression=null;
			// throw new UnexpectedTokenException(unexpectedToken, SumaTokenTypes.BRACKET_START);
		}
		return expression;
	}

	@Override
	protected Expression parsePrefixedExpression(AbstractMatchingTokenizer tokenizer, String prefix)
		throws ParserException,
		IOException
	{
		Expression expression=null;
		Token token=tokenizer.nextToken();
		if (token != null)
		{
			switch (token.getType())
			{
				case SqlTokenTypes.IDENTIFIER:
					expression=new ScopedExpression(new IdentifierExpression(prefix), token.getValue());
					break;
				default:
					throw new UnexpectedTokenException(token);
			}
		}
		return expression;
	}

	protected Expression parseInstantiationCallExpression(Token token, AbstractMatchingTokenizer tokenizer, ParserContext parserContext)
		throws IOException,
		ParserException
	{
		// Se trata de una instanciación: Vamos a tratarlo como si fuera la invocación a una función (a fina de cuentas, lo es)
		// reusando la superimplementación deste método.
		Token token2=tokenizer.nextToken();
		Expression expression2=super.parseIdentifier(token2, tokenizer, parserContext);
		if (expression2 instanceof FunctionCallExpression)
		{
			FunctionCallExpression functionCallExpression=(FunctionCallExpression)expression2;
			return new InstantiationCallExpression(functionCallExpression.getFunctionObject(), functionCallExpression.getArguments());
		}
		else
		{
			throw new ParserException("An invocation is expected after new");
		}
	}
}
