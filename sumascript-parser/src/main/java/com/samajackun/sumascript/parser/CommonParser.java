package com.samajackun.sumascript.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.model.Expression;
import com.samajackun.rodas.parsing.parser.ParserException;
import com.samajackun.rodas.parsing.tokenizer.TokenizerException;
import com.samajackun.rodas.sql.parser.ParserContext;
import com.samajackun.rodas.sql.tokenizer.Token;
import com.samajackun.sumascript.core.Instruction;
import com.samajackun.sumascript.core.instructions.BlockInstruction;
import com.samajackun.sumascript.core.instructions.EchoErrInstruction;
import com.samajackun.sumascript.core.instructions.EchoOutInstruction;
import com.samajackun.sumascript.core.instructions.ExpressionInstruction;
import com.samajackun.sumascript.core.instructions.FunctionDeclarationInstruction;
import com.samajackun.sumascript.tokenizer.SumaMatchingTokenizer;
import com.samajackun.sumascript.tokenizer.SumaTokenTypes;

public class CommonParser
{
	private static final CommonParser INSTANCE=new CommonParser();

	public static CommonParser getInstance()
	{
		return INSTANCE;
	}

	private CommonParser()
	{
	}

	public Expression parseExpression(SumaMatchingTokenizer tokenizer, ParserContext parserContext)
		throws TokenizerException,
		ParserException,
		IOException
	{
		return SumaExpressionParser.getInstance().parse(tokenizer, parserContext);
	}

	public Expression parseParenthesizedExpression(SumaMatchingTokenizer tokenizer, ParserContext parserContext)
		throws ParserException,
		IOException
	{
		tokenizer.matchToken(SumaTokenTypes.PARENTHESIS_START);
		Expression expression=parseExpression(tokenizer, parserContext);
		tokenizer.matchToken(SumaTokenTypes.PARENTHESIS_END);
		return expression;
	}

	public Instruction parseInstruction(SumaMatchingTokenizer tokenizer, SumaParserContext parserContext)
		throws SumaParseException,
		IOException,
		ParserException,
		EvaluationException
	{
		Instruction instruction;
		Token token=tokenizer.nextOptionalToken();
		if (token != null)
		{
			switch (token.getType())
			{
				case SumaTokenTypes.KEY_START:
					tokenizer.pushBack(token);
					instruction=parseBlock(tokenizer, parserContext);
					break;
				case SumaTokenTypes.KEY_END:
					// Ojo: Esto tiene sentido: Ocurre cuando estamos parseando las l�neas de un bloque, y en alg�n momento llegamos al cierre de bloque.
					tokenizer.pushBack(token);
					instruction=null;
					break;
				case SumaTokenTypes.KEYWORD_IF:
					instruction=ConditionalParser.getInstance().parseIf(tokenizer, parserContext);
					break;
				case SumaTokenTypes.KEYWORD_FOR:
					instruction=LoopParser.getInstance().parseForLoop(tokenizer, parserContext);
					break;
				case SumaTokenTypes.KEYWORD_SWITCH:
					instruction=ConditionalParser.getInstance().parseSwitch(tokenizer, parserContext);
					break;
				case SumaTokenTypes.KEYWORD_VAR:
					instruction=FunctionParser.getInstance().parseLocalVars(tokenizer, parserContext);
					break;
				case SumaTokenTypes.KEYWORD_RETURN:
					instruction=FunctionParser.getInstance().parseReturn(tokenizer, parserContext);
					break;
				case SumaTokenTypes.KEYWORD_FUNCTION:
					FunctionDeclarationInstruction functionDeclarationInstruction=FunctionParser.getInstance().parseFunctionDefinition(tokenizer, parserContext);
					instruction=functionDeclarationInstruction;
					parserContext.getFunctionMap().put(functionDeclarationInstruction.getCodedFunction().getName(), functionDeclarationInstruction.getCodedFunction());
					break;
				case SumaTokenTypes.KEYWORD_ECHO_OUT:
					instruction=parseEchoOut(tokenizer, parserContext);
					break;
				case SumaTokenTypes.KEYWORD_ECHO_ERR:
					instruction=parseEchoErr(tokenizer, parserContext);
					break;
				default:
					tokenizer.pushBack(token);
					Expression expression=parseLeftSideExpression(tokenizer, parserContext);
					Token token2=tokenizer.nextOptionalToken();
					if (token2 != null)
					{
						switch (token2.getType())
						{
							case SumaTokenTypes.OPERATOR_EQUALS:
								instruction=AssignationParser.getInstance().parseAssignation(tokenizer, parserContext, expression);
								break;
							case SumaTokenTypes.OPERATOR_PLUS_PLUS:
								instruction=AssignationParser.getInstance().parseIncrementAssignation(tokenizer, parserContext, expression);
								break;
							case SumaTokenTypes.OPERATOR_MINUS_MINUS:
								instruction=AssignationParser.getInstance().parseDecrementAssignation(tokenizer, parserContext, expression);
								break;
							case SumaTokenTypes.OPERATOR_PLUS_EQUALS:
								instruction=AssignationParser.getInstance().parseAddAssignation(tokenizer, parserContext, expression);
								break;
							case SumaTokenTypes.OPERATOR_MINUS_EQUALS:
								instruction=AssignationParser.getInstance().parseSubstractAssignation(tokenizer, parserContext, expression);
								break;
							case SumaTokenTypes.OPERATOR_ASTERISK_EQUALS:
								instruction=AssignationParser.getInstance().parseMultiplyAssignation(tokenizer, parserContext, expression);
								break;
							case SumaTokenTypes.OPERATOR_DIVIDE_EQUALS:
								instruction=AssignationParser.getInstance().parseDivideAssignation(tokenizer, parserContext, expression);
								break;
							default:
								instruction=new ExpressionInstruction(expression);
								tokenizer.pushBack(token2);
						}
					}
					else
					{
						if (expression != null)
						{
							// Hay que evaluar la expresi�n en cualquier caso: Podr�a involucrar una llamada a alguna funci�n.
							instruction=new ExpressionInstruction(expression);
						}
						else
						{
							instruction=null;
						}
					}
					break;
			}
			// Consumir possible punto-y-coma o newline al final:
			token=tokenizer.nextOptionalToken();
			if (token != null)
			{
				if (token.getType() != SumaTokenTypes.SEMICOLON && token.getType() != SumaTokenTypes.NEWLINE)
				{
					tokenizer.pushBack(token);
				}
			}
		}
		else
		{
			instruction=null;
		}
		return instruction;
	}

	public void parseInstructions(SumaMatchingTokenizer tokenizer, SumaParserContext parserContext, List<Instruction> instructions)
		throws SumaParseException,
		IOException,
		ParserException,
		EvaluationException
	{
		Instruction instruction;
		do
		{
			instruction=parseInstruction(tokenizer, parserContext);
			if (instruction != null)
			{
				instructions.add(instruction);
			}
		}
		while (instruction != null);
	}

	public BlockInstruction parseBlock(SumaMatchingTokenizer tokenizer, SumaParserContext parserContext)
		throws SumaParseException,
		IOException,
		ParserException,
		EvaluationException
	{
		List<Instruction> instructions=new ArrayList<>();
		Instruction instruction;
		boolean looping=true;
		tokenizer.matchToken(SumaTokenTypes.KEY_START);
		do
		{
			instruction=parseInstruction(tokenizer, parserContext);
			if (instruction != null)
			{
				instructions.add(instruction);
			}
			Token token=tokenizer.nextUsefulToken();
			if (token.getType() == SumaTokenTypes.KEY_END)
			{
				looping=false;
			}
			else
			{
				tokenizer.pushBack(token);
			}
		}
		while (looping);
		BlockInstruction blockInstruction=new BlockInstruction(instructions);
		return blockInstruction;
	}

	private Expression parseLeftSideExpression(SumaMatchingTokenizer tokenizer, ParserContext parserContext)
		throws TokenizerException,
		ParserException,
		IOException
	{
		return SumaExpressionParser.getInstance().parseLeftSide(tokenizer, parserContext);
	}

	private Instruction parseEchoOut(SumaMatchingTokenizer tokenizer, ParserContext parserContext)
		throws IOException,
		ParserException,
		EvaluationException
	{
		Expression expression=CommonParser.getInstance().parseExpression(tokenizer, parserContext);
		return new EchoOutInstruction(expression);
	}

	private Instruction parseEchoErr(SumaMatchingTokenizer tokenizer, ParserContext parserContext)
		throws IOException,
		ParserException,
		EvaluationException
	{
		Expression expression=CommonParser.getInstance().parseExpression(tokenizer, parserContext);
		return new EchoErrInstruction(expression);
	}

}
