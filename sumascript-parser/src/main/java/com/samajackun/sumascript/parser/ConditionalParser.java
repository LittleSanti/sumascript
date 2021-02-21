package com.samajackun.sumascript.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.model.ConstantExpression;
import com.samajackun.rodas.core.model.Expression;
import com.samajackun.rodas.parsing.parser.ParserException;
import com.samajackun.rodas.parsing.parser.UnexpectedTokenException;
import com.samajackun.rodas.parsing.tokenizer.TokenizerException;
import com.samajackun.rodas.sql.parser.ParserContext;
import com.samajackun.rodas.sql.tokenizer.Token;
import com.samajackun.sumascript.core.Instruction;
import com.samajackun.sumascript.core.instructions.IfInstruction;
import com.samajackun.sumascript.core.instructions.SwitchInstruction;
import com.samajackun.sumascript.core.instructions.SwitchInstruction.Pair;
import com.samajackun.sumascript.tokenizer.SumaMatchingTokenizer;
import com.samajackun.sumascript.tokenizer.SumaTokenTypes;

public class ConditionalParser
{
	private static final ConditionalParser INSTANCE=new ConditionalParser();

	public static ConditionalParser getInstance()
	{
		return INSTANCE;
	}

	private ConditionalParser()
	{
	}

	public Instruction parseIf(SumaMatchingTokenizer tokenizer, ParserContext parserContext)
		throws ParserException,
		IOException,
		SumaParseException,
		EvaluationException
	{
		tokenizer.matchToken(SumaTokenTypes.PARENTHESIS_START);
		Expression expression=CommonParser.getInstance().parseExpression(tokenizer, parserContext);
		tokenizer.matchToken(SumaTokenTypes.PARENTHESIS_END);
		Instruction positiveInstruction=CommonParser.getInstance().parseInstruction(tokenizer, parserContext);
		Token token=tokenizer.nextOptionalUsefulToken();
		Instruction negativeInstruction;
		if (token != null)
		{
			if (token.getType() == SumaTokenTypes.KEYWORD_ELSE)
			{
				negativeInstruction=CommonParser.getInstance().parseInstruction(tokenizer, parserContext);
			}
			else
			{
				negativeInstruction=null;
				tokenizer.pushBack(token);
			}
		}
		else
		{
			negativeInstruction=null;
		}
		IfInstruction ifInstruction=new IfInstruction(expression, positiveInstruction, negativeInstruction);
		return ifInstruction;
	}

	public Instruction parseSwitch(SumaMatchingTokenizer tokenizer, ParserContext parserContext)
		throws ParserException,
		IOException,
		SumaParseException,
		EvaluationException
	{
		tokenizer.matchToken(SumaTokenTypes.PARENTHESIS_START);
		Expression expression=CommonParser.getInstance().parseExpression(tokenizer, parserContext);
		tokenizer.matchToken(SumaTokenTypes.PARENTHESIS_END);
		tokenizer.matchToken(SumaTokenTypes.KEY_START);
		Token token;
		boolean looping=true;
		List<Pair> cases=new ArrayList<>();
		Instruction defaultInstruction=null;
		do
		{
			token=tokenizer.nextToken();
			switch (token.getType())
			{
				case SumaTokenTypes.KEYWORD_CASE:
					Set<Object> values=parseListOfValues(tokenizer, parserContext);
					Instruction caseInstruction=CommonParser.getInstance().parseInstruction(tokenizer, parserContext);
					Pair pair=new Pair(values, caseInstruction);
					cases.add(pair);
					break;
				case SumaTokenTypes.KEYWORD_DEFAULT:
					token=tokenizer.matchToken(SumaTokenTypes.COLON);
					defaultInstruction=CommonParser.getInstance().parseInstruction(tokenizer, parserContext);
					break;
				case SumaTokenTypes.KEY_END:
					looping=false;
					break;
				default:
					throw new UnexpectedTokenException(token, SumaTokenTypes.KEYWORD_CASE, SumaTokenTypes.KEYWORD_DEFAULT, SumaTokenTypes.KEY_END);
			}
		}
		while (looping);
		SwitchInstruction switchInstruction=new SwitchInstruction(expression, cases, defaultInstruction);
		return switchInstruction;
	}

	private Set<Object> parseListOfValues(SumaMatchingTokenizer tokenizer, ParserContext parserContext)
		throws ParserException,
		IOException,
		EvaluationException,
		SumaParseException
	{
		Set<Object> set=new HashSet<>();
		boolean looping=true;
		do
		{
			Object value=parseConstant(tokenizer, parserContext);
			if (!set.add(value))
			{
				// TODO Aquí hay que lanzar un warning de que hay un valor repe.
			}
			Token token=tokenizer.nextToken();
			switch (token.getType())
			{
				case SumaTokenTypes.COMMA:
					break;
				case SumaTokenTypes.COLON:
					looping=false;
					break;
				default:
					throw new UnexpectedTokenException(token, SumaTokenTypes.COLON, SumaTokenTypes.COMMA);
			}
		}
		while (looping);
		return set;
	}

	private Object parseConstant(SumaMatchingTokenizer tokenizer, ParserContext parserContext)
		throws TokenizerException,
		ParserException,
		IOException,
		EvaluationException,
		SumaParseException
	{
		Expression expression=CommonParser.getInstance().parseExpression(tokenizer, parserContext);
		expression=expression.reduce(parserContext.getEvaluationFactory());
		if (expression instanceof ConstantExpression)
		{
			Object value=((ConstantExpression)expression).evaluateAsConstant();
			return value;
		}
		else
		{
			throw new SumaParseException("Only constant values are allowed into a CASE declaration");
		}
	}
}
