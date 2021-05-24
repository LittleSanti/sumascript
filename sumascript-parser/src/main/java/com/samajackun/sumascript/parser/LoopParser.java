package com.samajackun.sumascript.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.Name;
import com.samajackun.rodas.core.model.Expression;
import com.samajackun.rodas.parsing.parser.ParserException;
import com.samajackun.rodas.parsing.parser.UnexpectedTokenException;
import com.samajackun.rodas.sql.parser.ParserContext;
import com.samajackun.rodas.sql.tokenizer.Token;
import com.samajackun.sumascript.core.Instruction;
import com.samajackun.sumascript.core.instructions.AbstractLoopInstruction;
import com.samajackun.sumascript.core.instructions.AbstractVariableAssignation;
import com.samajackun.sumascript.core.instructions.CollectionLoopInstruction;
import com.samajackun.sumascript.core.instructions.IndexedLoopInstruction;
import com.samajackun.sumascript.tokenizer.SumaMatchingTokenizer;
import com.samajackun.sumascript.tokenizer.SumaTokenTypes;

public class LoopParser
{
	private static final LoopParser INSTANCE=new LoopParser();

	public static LoopParser getInstance()
	{
		return INSTANCE;
	}

	private LoopParser()
	{
	}

	// for ([<initialization...>];[<condition>];[<operation>]) [while (<condition>)] {...}
	// for (var <varname>:<expression>) [while (<condition>)] {...}
	public AbstractLoopInstruction parseForLoop(SumaMatchingTokenizer tokenizer, SumaParserContext parserContext)
		throws ParserException,
		IOException,
		SumaParseException,
		EvaluationException
	{
		AbstractLoopInstruction loopInstruction;
		tokenizer.matchToken(SumaTokenTypes.PARENTHESIS_START);
		CollectionLoopClause collectionLoopClause=parseLocalOrGlobalCollectionLoopClause(tokenizer, parserContext);
		if (collectionLoopClause != null)
		{
			// Se trata de un bucle de colecci�n:
			tokenizer.matchToken(SumaTokenTypes.PARENTHESIS_END);
			Expression preCondition=parseWhileLoopClause(tokenizer, parserContext);
			Instruction innerInstruction=CommonParser.getInstance().parseInstruction(tokenizer, parserContext);
			// TODO Falta la condici�n posterior opcional.
			loopInstruction=new CollectionLoopInstruction(preCondition, innerInstruction, null, collectionLoopClause.getVarName(), collectionLoopClause.getCollection());
		}
		else
		{
			VariablesLoopClause variablesLoopClause=parseVariablesLoopClause(tokenizer, parserContext);
			if (variablesLoopClause != null)
			{
				// Se trata de un bucle de variables:
				tokenizer.matchToken(SumaTokenTypes.PARENTHESIS_END);
				Instruction innerInstruction=CommonParser.getInstance().parseInstruction(tokenizer, parserContext);
				// TODO Falta la condici�n posterior opcional.
				loopInstruction=new IndexedLoopInstruction(variablesLoopClause.getInitializations(), variablesLoopClause.getCondition(), innerInstruction, variablesLoopClause.getPostInstructions());
			}
			else
			{
				throw new SumaParseException("Not an indexed loop nor collection loop");
			}
		}
		return loopInstruction;
	}

	public Expression parseWhileLoopClause(SumaMatchingTokenizer tokenizer, ParserContext parserContext)
		throws ParserException,
		IOException,
		SumaParseException
	{
		Expression expression;
		Token token=tokenizer.nextUsefulToken();
		if (token.getType() == SumaTokenTypes.KEYWORD_WHILE)
		{
			expression=CommonParser.getInstance().parseExpression(tokenizer, parserContext);
		}
		else
		{
			tokenizer.pushBack(token);
			expression=null;
		}
		return expression;
	}

	public CollectionLoopClause parseLocalOrGlobalCollectionLoopClause(SumaMatchingTokenizer tokenizer, ParserContext parserContext)
		throws ParserException,
		IOException,
		SumaParseException
	{
		CollectionLoopClause collectionLoopClause;
		Token token1=tokenizer.nextUsefulToken();
		Token local;
		if (token1.getType() == SumaTokenTypes.KEYWORD_VAR)
		{
			local=token1;
			token1=tokenizer.nextUsefulToken();
		}
		else
		{
			local=null;
		}
		if (token1.getType() == SumaTokenTypes.IDENTIFIER)
		{
			Name varName=Name.instanceOf(token1.getImage());
			Token token2=tokenizer.nextUsefulToken();
			if (token2.getType() == SumaTokenTypes.COLON)
			{
				Expression expression=CommonParser.getInstance().parseExpression(tokenizer, parserContext);
				collectionLoopClause=new CollectionLoopClause(varName, expression, local != null);
			}
			else
			{
				tokenizer.pushBack(token2);
				tokenizer.pushBack(token1);
				if (local != null)
				{
					tokenizer.pushBack(local);
				}
				collectionLoopClause=null;
			}
		}
		else
		{
			if (local != null)
			{
				tokenizer.pushBack(local);
				throw new UnexpectedTokenException(token1, SumaTokenTypes.IDENTIFIER);
			}
			else
			{
				tokenizer.pushBack(token1);
				collectionLoopClause=null;
			}
		}
		return collectionLoopClause;
	}

	public VariablesLoopClause parseVariablesLoopClause(SumaMatchingTokenizer tokenizer, SumaParserContext parserContext)
		throws ParserException,
		IOException,
		EvaluationException
	{
		List<AbstractVariableAssignation> assignationsList=VariableParser.getInstance().parseVariableAssignationList(tokenizer, parserContext);
		tokenizer.matchToken(SumaTokenTypes.SEMICOLON);
		Token token=tokenizer.nextUsefulToken();
		Expression condition;
		switch (token.getType())
		{
			case SumaTokenTypes.SEMICOLON:
				condition=null;
				break;
			default:
				tokenizer.pushBack(token);
				condition=CommonParser.getInstance().parseExpression(tokenizer, parserContext);
				tokenizer.matchToken(SumaTokenTypes.SEMICOLON);
				break;
		}
		List<Instruction> postInstructions=parseInstructionList(tokenizer, parserContext);
		return new VariablesLoopClause(assignationsList, condition, postInstructions);
	}

	public List<Instruction> parseInstructionList(SumaMatchingTokenizer tokenizer, SumaParserContext parserContext)
		throws SumaParseException,
		IOException,
		ParserException,
		EvaluationException
	{
		List<Instruction> instructions=new ArrayList<>();
		Instruction instruction;
		boolean looping=true;
		do
		{
			instruction=CommonParser.getInstance().parseInstruction(tokenizer, parserContext);
			if (instruction != null)
			{
				instructions.add(instruction);
				Token token=tokenizer.nextUsefulToken();
				if (token.getImage() == SumaTokenTypes.COMMA)
				{
				}
				else
				{
					tokenizer.pushBack(token);
					looping=false;
				}
			}
			else
			{
				looping=false;
			}
		}
		while (looping);
		return instructions;
	}
}
