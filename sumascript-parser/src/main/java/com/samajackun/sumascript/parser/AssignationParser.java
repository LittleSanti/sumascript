package com.samajackun.sumascript.parser;

import java.io.IOException;

import com.samajackun.rodas.core.model.Expression;
import com.samajackun.rodas.parsing.parser.ParserException;
import com.samajackun.rodas.sql.parser.ParserContext;
import com.samajackun.sumascript.core.Instruction;
import com.samajackun.sumascript.core.expressions.Assignable;
import com.samajackun.sumascript.core.instructions.AddAssignationInstruction;
import com.samajackun.sumascript.core.instructions.AssignationInstruction;
import com.samajackun.sumascript.core.instructions.DecrementInstruction;
import com.samajackun.sumascript.core.instructions.DivideAssignationInstruction;
import com.samajackun.sumascript.core.instructions.IncrementInstruction;
import com.samajackun.sumascript.core.instructions.MultiplyAssignationInstruction;
import com.samajackun.sumascript.core.instructions.SubstractAssignationInstruction;
import com.samajackun.sumascript.tokenizer.SumaMatchingTokenizer;

public final class AssignationParser
{
	public static final AssignationParser INSTANCE=new AssignationParser();

	public static AssignationParser getInstance()
	{
		return INSTANCE;
	}

	public AssignationParser()
	{
	}

	public Instruction parseIncrementAssignation(SumaMatchingTokenizer tokenizer, ParserContext parserContext, Expression expression)
		throws InvalidLeftSideException
	{
		if (expression instanceof Assignable)
		{
			return new IncrementInstruction((Assignable)expression);
		}
		else
		{
			throw new InvalidLeftSideException(expression);
		}
	}

	public Instruction parseDecrementAssignation(SumaMatchingTokenizer tokenizer, ParserContext parserContext, Expression expression)
		throws InvalidLeftSideException
	{
		if (expression instanceof Assignable)
		{
			return new DecrementInstruction((Assignable)expression);
		}
		else
		{
			throw new InvalidLeftSideException(expression);
		}
	}

	public Instruction parseAssignation(SumaMatchingTokenizer tokenizer, ParserContext parserContext, Expression leftSide)
		throws ParserException,
		IOException
	{
		if (leftSide instanceof Assignable)
		{
			Expression rightSide=CommonParser.getInstance().parseExpression(tokenizer, parserContext);
			Instruction instruction=new AssignationInstruction((Assignable)leftSide, rightSide);
			return instruction;
		}
		else
		{
			throw new InvalidLeftSideException(leftSide);
		}
	}

	public Instruction parseAddAssignation(SumaMatchingTokenizer tokenizer, ParserContext parserContext, Expression leftSide)
		throws ParserException,
		IOException
	{
		if (leftSide instanceof Assignable)
		{
			Expression rightSide=CommonParser.getInstance().parseExpression(tokenizer, parserContext);
			Instruction instruction=new AddAssignationInstruction((Assignable)leftSide, rightSide);
			return instruction;
		}
		else
		{
			throw new InvalidLeftSideException(leftSide);
		}
	}

	public Instruction parseSubstractAssignation(SumaMatchingTokenizer tokenizer, ParserContext parserContext, Expression leftSide)
		throws ParserException,
		IOException
	{
		if (leftSide instanceof Assignable)
		{
			Expression rightSide=CommonParser.getInstance().parseExpression(tokenizer, parserContext);
			Instruction instruction=new SubstractAssignationInstruction((Assignable)leftSide, rightSide);
			return instruction;
		}
		else
		{
			throw new InvalidLeftSideException(leftSide);
		}
	}

	public Instruction parseMultiplyAssignation(SumaMatchingTokenizer tokenizer, ParserContext parserContext, Expression leftSide)
		throws ParserException,
		IOException
	{
		if (leftSide instanceof Assignable)
		{
			Expression rightSide=CommonParser.getInstance().parseExpression(tokenizer, parserContext);
			Instruction instruction=new MultiplyAssignationInstruction((Assignable)leftSide, rightSide);
			return instruction;
		}
		else
		{
			throw new InvalidLeftSideException(leftSide);
		}
	}

	public Instruction parseDivideAssignation(SumaMatchingTokenizer tokenizer, ParserContext parserContext, Expression leftSide)
		throws ParserException,
		IOException
	{
		if (leftSide instanceof Assignable)
		{
			Expression rightSide=CommonParser.getInstance().parseExpression(tokenizer, parserContext);
			Instruction instruction=new DivideAssignationInstruction((Assignable)leftSide, rightSide);
			return instruction;
		}
		else
		{
			throw new InvalidLeftSideException(leftSide);
		}
	}

}
