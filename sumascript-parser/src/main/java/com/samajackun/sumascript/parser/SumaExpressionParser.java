package com.samajackun.sumascript.parser;

import java.io.IOException;

import com.samajackun.rodas.core.eval.Name;
import com.samajackun.rodas.core.model.Expression;
import com.samajackun.rodas.core.model.ExpressionCollection;
import com.samajackun.rodas.core.model.FunctionCallExpression;
import com.samajackun.rodas.core.model.IdentifierExpression;
import com.samajackun.rodas.parsing.parser.ParserException;
import com.samajackun.rodas.parsing.parser.UnexpectedTokenException;
import com.samajackun.rodas.sql.parser.GenericExpressionParser;
import com.samajackun.rodas.sql.parser.ParserContext;
import com.samajackun.rodas.sql.tokenizer.AbstractMatchingTokenizer;
import com.samajackun.rodas.sql.tokenizer.SqlTokenTypes;
import com.samajackun.rodas.sql.tokenizer.Token;
import com.samajackun.sumascript.core.expressions.IndexedExpression;
import com.samajackun.sumascript.core.expressions.NearestVariableExpression;
import com.samajackun.sumascript.core.expressions.ReferencedExpression;
import com.samajackun.sumascript.tokenizer.SumaTokenTypes;

public final class SumaExpressionParser extends GenericExpressionParser
{
	private enum State {
		INITIAL, DEREFERENCING, IDENTIFIER_READ, COMPLETE
	}

	private static final SumaExpressionParser INSTANCE=new SumaExpressionParser();

	public static SumaExpressionParser getInstance()
	{
		return INSTANCE;
	}

	private SumaExpressionParser()
	{
		super(SumaParserFactory.getInstance());
	}

	public Expression parseLeftSide(AbstractMatchingTokenizer tokenizer, ParserContext parserContext)
		throws ParserException,
		IOException
	{
		Expression expression=null;
		State state=State.INITIAL;
		do
		{
			Token token=tokenizer.nextOptionalUsefulToken();
			if (token == null)
			{
				break;
			}
			switch (state)
			{
				case INITIAL:
					switch (token.getType())
					{
						case SqlTokenTypes.IDENTIFIER:
							expression=new NearestVariableExpression(Name.instanceOf(token.getValue()));
							state=State.IDENTIFIER_READ;
							break;
						default:
							throw new UnexpectedTokenException(token, SumaTokenTypes.IDENTIFIER);
					}
					break;
				case IDENTIFIER_READ:
					switch (token.getType())
					{
						case SumaTokenTypes.PERIOD:
							state=State.DEREFERENCING;
							break;
						case SumaTokenTypes.PARENTHESIS_START:
							ExpressionCollection arguments=getParserFactory().getExpressionCollectionParser().parse(tokenizer, parserContext);
							FunctionCallExpression functionExpression=new FunctionCallExpression(expression, arguments.getExpressions());
							tokenizer.matchToken(SqlTokenTypes.PARENTHESIS_END);
							expression=functionExpression;
							state=State.COMPLETE;
							break;
						case SumaTokenTypes.BRACKET_START:
							Expression index=parse(tokenizer, parserContext);
							tokenizer.matchToken(SumaTokenTypes.BRACKET_END);
							expression=new IndexedExpression(expression, index);
							state=State.COMPLETE;
							break;
						case SumaTokenTypes.OPERATOR_EQUALS:
						case SumaTokenTypes.OPERATOR_PLUS_EQUALS:
						case SumaTokenTypes.OPERATOR_PLUS_PLUS:
						case SumaTokenTypes.OPERATOR_MINUS_EQUALS:
						case SumaTokenTypes.OPERATOR_MINUS_MINUS:
						case SumaTokenTypes.OPERATOR_ASTERISK_EQUALS:
						case SumaTokenTypes.OPERATOR_DIVIDE_EQUALS:
							tokenizer.pushBack(token);
							state=State.COMPLETE;
							break;
						default:
							// TODO
							throw new UnexpectedTokenException(token, SumaTokenTypes.PERIOD, SqlTokenTypes.PARENTHESIS_START, SqlTokenTypes.BRACKET_START);
					}
					break;
				case DEREFERENCING:
					switch (token.getType())
					{
						case SqlTokenTypes.IDENTIFIER:
							expression=new ReferencedExpression(expression, new IdentifierExpression(token.getValue()));
							state=State.IDENTIFIER_READ;
							break;
						default:
							throw new UnexpectedTokenException(token, SumaTokenTypes.IDENTIFIER);
					}
					break;
				case COMPLETE:
					break;
			}
		}
		while (state != State.COMPLETE && tokenizer.tokenWasRead());
		return expression;
	}
}
