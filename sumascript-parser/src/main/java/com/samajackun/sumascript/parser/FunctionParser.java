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
import com.samajackun.sumascript.core.instructions.AbstractVariableAssignation;
import com.samajackun.sumascript.core.instructions.FunctionDeclarationInstruction;
import com.samajackun.sumascript.core.instructions.LocalVariableAssignation;
import com.samajackun.sumascript.core.instructions.ReturnInstruction;
import com.samajackun.sumascript.core.instructions.VariableAssignationsInstruction;
import com.samajackun.sumascript.core.runtime.CodedFunction;
import com.samajackun.sumascript.core.runtime.NamedCodedFunction;
import com.samajackun.sumascript.expressions.UndefinedConstantExpression;
import com.samajackun.sumascript.expressions.UnnamedFunctionDeclarationExpression;
import com.samajackun.sumascript.tokenizer.SumaMatchingTokenizer;
import com.samajackun.sumascript.tokenizer.SumaTokenTypes;

public final class FunctionParser
{
	private static final FunctionParser INSTANCE=new FunctionParser();

	public static FunctionParser getInstance()
	{
		return INSTANCE;
	}

	private FunctionParser()
	{
	}

	public FunctionDeclarationInstruction parseFunctionDefinition(SumaMatchingTokenizer tokenizer, SumaParserContext parserContext)
		throws IOException,
		ParserException,
		EvaluationException
	{
		Token token=tokenizer.matchToken(SumaTokenTypes.IDENTIFIER);
		String functionName=token.getValue();
		List<Name> parameterNames=parseParameterNames(tokenizer, parserContext);
		Instruction body=CommonParser.getInstance().parseBlock(tokenizer, parserContext);
		NamedCodedFunction codedFunction=new NamedCodedFunction(parameterNames, body, Name.instanceOf(functionName));
		return new FunctionDeclarationInstruction(codedFunction);
	}

	private List<Name> parseParameterNames(SumaMatchingTokenizer tokenizer, ParserContext parserContext)
		throws IOException,
		ParserException
	{
		tokenizer.matchToken(SumaTokenTypes.PARENTHESIS_START);
		List<Name> params=new ArrayList<>();
		boolean looping=true;
		boolean readIdentifier=false;
		do
		{
			Token token=tokenizer.nextToken();
			if (readIdentifier)
			{
				switch (token.getType())
				{
					case SumaTokenTypes.PARENTHESIS_END:
						looping=false;
						break;
					case SumaTokenTypes.COMMA:
						break;
					default:
						throw new UnexpectedTokenException(token, SumaTokenTypes.COMMA, SumaTokenTypes.PARENTHESIS_END);
				}
			}
			else
			{
				switch (token.getType())
				{
					case SumaTokenTypes.PARENTHESIS_END:
						looping=false;
						break;
					case SumaTokenTypes.IDENTIFIER:
						params.add(Name.instanceOf(token.getValue()));
						break;
					default:
						throw new UnexpectedTokenException(token, SumaTokenTypes.COMMA, SumaTokenTypes.PARENTHESIS_END);
				}
			}
		}
		while (looping);
		return params;
	}

	public Expression parseUnnamedFunctionDeclaration(SumaMatchingTokenizer tokenizer, SumaParserContext parserContext)
		throws IOException,
		ParserException,
		EvaluationException
	{
		List<Name> parameterNames=parseParameterNames(tokenizer, parserContext);
		tokenizer.matchToken(SumaTokenTypes.KEY_START);
		Instruction body=CommonParser.getInstance().parseInstruction(tokenizer, parserContext);
		tokenizer.matchToken(SumaTokenTypes.KEY_END);
		CodedFunction codedFunction=new CodedFunction(parameterNames, body);
		return new UnnamedFunctionDeclarationExpression(codedFunction);
	}

	public VariableAssignationsInstruction parseLocalVars(SumaMatchingTokenizer tokenizer, SumaParserContext parserContext)
		throws ParserException,
		IOException,
		SumaParseException
	{
		List<AbstractVariableAssignation> variables=new ArrayList<>();
		boolean looping=true;
		do
		{
			Token tokenName=tokenizer.matchToken(SumaTokenTypes.IDENTIFIER);
			Token token=tokenizer.nextOptionalUsefulToken();
			if (token != null)
			{
				Expression initialization;
				switch (token.getType())
				{
					case SumaTokenTypes.OPERATOR_EQUALS:
						initialization=CommonParser.getInstance().parseExpression(tokenizer, parserContext);
						// TODO Hay que chequear que la variable no estuviera ya declarada.
						LocalVariableAssignation variableAssignation=new LocalVariableAssignation(Name.instanceOf(tokenName.getValue()), initialization);
						variables.add(variableAssignation);
						token=tokenizer.nextOptionalToken();
						if (token != null)
						{
							if (token.getType() != SumaTokenTypes.COMMA)
							{
								looping=false;
								tokenizer.pushBack(token);
							}
						}
						else
						{
							looping=false;
						}
						break;
					case SumaTokenTypes.COMMA:
						// TODO Hay que chequear que la variable no estuviera ya declarada.
						LocalVariableAssignation variableAssignation2=new LocalVariableAssignation(Name.instanceOf(tokenName.getValue()), UndefinedConstantExpression.getInstance());
						variables.add(variableAssignation2);
						break;
					case SumaTokenTypes.NEWLINE:
					case SumaTokenTypes.SEMICOLON:
						looping=false;
						break;
					default:
						tokenizer.pushBack(token);
						initialization=UndefinedConstantExpression.getInstance();
						looping=false;
				}
			}
			else
			{
				LocalVariableAssignation variableAssignation2=new LocalVariableAssignation(Name.instanceOf(tokenName.getValue()), UndefinedConstantExpression.getInstance());
				variables.add(variableAssignation2);
				looping=false;
			}
		}
		while (looping);
		VariableAssignationsInstruction variableAssignationsInstruction=new VariableAssignationsInstruction(variables);
		return variableAssignationsInstruction;
	}

	public Instruction parseReturn(SumaMatchingTokenizer tokenizer, ParserContext parserContext)
		throws IOException,
		ParserException,
		EvaluationException
	{
		Expression expression=CommonParser.getInstance().parseExpression(tokenizer, parserContext);
		return new ReturnInstruction(expression);
	}

}
