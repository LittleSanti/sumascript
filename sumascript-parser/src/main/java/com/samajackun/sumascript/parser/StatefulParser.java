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
import com.samajackun.sumascript.core.expressions.Assignable;
import com.samajackun.sumascript.core.instructions.AbstractVariableAssignation;
import com.samajackun.sumascript.core.instructions.AddAssignationInstruction;
import com.samajackun.sumascript.core.instructions.AssignationInstruction;
import com.samajackun.sumascript.core.instructions.BlockInstruction;
import com.samajackun.sumascript.core.instructions.CollectionLoopInstruction;
import com.samajackun.sumascript.core.instructions.DecrementInstruction;
import com.samajackun.sumascript.core.instructions.DivideAssignationInstruction;
import com.samajackun.sumascript.core.instructions.ExpressionInstruction;
import com.samajackun.sumascript.core.instructions.FunctionDeclarationInstruction;
import com.samajackun.sumascript.core.instructions.IfInstruction;
import com.samajackun.sumascript.core.instructions.IncrementInstruction;
import com.samajackun.sumascript.core.instructions.LocalVariableAssignation;
import com.samajackun.sumascript.core.instructions.MultiplyAssignationInstruction;
import com.samajackun.sumascript.core.instructions.ReturnInstruction;
import com.samajackun.sumascript.core.instructions.SubstractAssignationInstruction;
import com.samajackun.sumascript.core.instructions.SumaEvaluatorFactory;
import com.samajackun.sumascript.core.instructions.SwitchInstruction;
import com.samajackun.sumascript.core.instructions.SwitchInstruction.Pair;
import com.samajackun.sumascript.core.instructions.VariableAssignationsInstruction;
import com.samajackun.sumascript.core.runtime.CodedFunction;
import com.samajackun.sumascript.core.runtime.NamedCodedFunction;
import com.samajackun.sumascript.expressions.UndefinedConstantExpression;
import com.samajackun.sumascript.expressions.UnnamedFunctionDeclarationExpression;
import com.samajackun.sumascript.tokenizer.SumaMatchingTokenizer;
import com.samajackun.sumascript.tokenizer.SumaTokenTypes;

public class StatefulParser
{
	public Program parse(SumaMatchingTokenizer tokenizer, ParserContext parserContext)
		throws SumaParseException,
		IOException,
		ParserException,
		EvaluationException
	{
		List<Instruction> instructions=new ArrayList<>();
		parseInstructions(tokenizer, parserContext, instructions);
		Program program=new Program(new BlockInstruction(instructions));
		return program;
	}

	public Expression parseExpression(SumaMatchingTokenizer tokenizer, ParserContext parserContext)
		throws TokenizerException,
		ParserException,
		IOException
	{
		return SumaExpressionParser.getInstance().parse(tokenizer, parserContext);
	}

	private Expression parseLeftSideExpression(SumaMatchingTokenizer tokenizer, ParserContext parserContext)
		throws TokenizerException,
		ParserException,
		IOException
	{
		return SumaExpressionParser.getInstance().parseLeftSide(tokenizer, parserContext);
	}

	private void parseInstructions(SumaMatchingTokenizer tokenizer, ParserContext parserContext, List<Instruction> instructions)
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

	private BlockInstruction parseBlock(SumaMatchingTokenizer tokenizer, ParserContext parserContext)
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

	private Instruction parseInstruction(SumaMatchingTokenizer tokenizer, ParserContext parserContext)
		throws SumaParseException,
		IOException,
		ParserException,
		EvaluationException
	{
		Instruction instruction=null;
		Token token=tokenizer.nextOptionalToken();
		if (token != null)
		{
			switch (token.getType())
			{
				case SumaTokenTypes.KEY_START:
					instruction=parseBlock(tokenizer, parserContext);
					break;
				case SumaTokenTypes.KEY_END:
					tokenizer.pushBack(token);
					break;
				case SumaTokenTypes.KEYWORD_IF:
					instruction=parseIf(tokenizer, parserContext);
					break;
				case SumaTokenTypes.KEYWORD_FOR:
					instruction=parseFor(tokenizer, parserContext);
					break;
				case SumaTokenTypes.KEYWORD_SWITCH:
					instruction=parseSwitch(tokenizer, parserContext);
					break;
				case SumaTokenTypes.KEYWORD_VAR:
					instruction=parseLocalVars(tokenizer, parserContext);
					break;
				case SumaTokenTypes.KEYWORD_RETURN:
					instruction=parseReturn(tokenizer, parserContext);
					break;
				case SumaTokenTypes.KEYWORD_FUNCTION:
					instruction=parseFunctionDefinition(tokenizer, parserContext);
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
								instruction=parseAssignation(tokenizer, parserContext, expression);
								break;
							case SumaTokenTypes.OPERATOR_PLUS_PLUS:
								instruction=parseIncrementAssignation(tokenizer, parserContext, expression);
								break;
							case SumaTokenTypes.OPERATOR_MINUS_MINUS:
								instruction=parseDecrementAssignation(tokenizer, parserContext, expression);
								break;
							case SumaTokenTypes.OPERATOR_PLUS_EQUALS:
								instruction=parseAddAssignation(tokenizer, parserContext, expression);
								break;
							case SumaTokenTypes.OPERATOR_MINUS_EQUALS:
								instruction=parseSubstractAssignation(tokenizer, parserContext, expression);
								break;
							case SumaTokenTypes.OPERATOR_ASTERISK_EQUALS:
								instruction=parseMultiplyAssignation(tokenizer, parserContext, expression);
								break;
							case SumaTokenTypes.OPERATOR_DIVIDE_EQUALS:
								instruction=parseDivideAssignation(tokenizer, parserContext, expression);
								break;
						}
					}
					else
					{
						if (expression != null)
						{
							// Hay que evaluar la expresión en cualquier caso: Podría involucrar una llamada a alguna función.
							instruction=new ExpressionInstruction(expression);
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
		return instruction;
	}

	private Instruction parseIncrementAssignation(SumaMatchingTokenizer tokenizer, ParserContext parserContext, Expression expression)
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

	private Instruction parseDecrementAssignation(SumaMatchingTokenizer tokenizer, ParserContext parserContext, Expression expression)
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

	private Instruction parseIf(SumaMatchingTokenizer tokenizer, ParserContext parserContext)
		throws ParserException,
		IOException,
		SumaParseException,
		EvaluationException
	{
		tokenizer.matchToken(SumaTokenTypes.PARENTHESIS_START);
		Expression expression=parseExpression(tokenizer, parserContext);
		tokenizer.matchToken(SumaTokenTypes.PARENTHESIS_END);
		Instruction positiveInstruction=parseInstruction(tokenizer, parserContext);
		Token token=tokenizer.nextOptionalUsefulToken();
		Instruction negativeInstruction;
		if (token != null)
		{
			if (token.getType() == SumaTokenTypes.KEYWORD_ELSE)
			{
				negativeInstruction=parseInstruction(tokenizer, parserContext);
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

	private Instruction parseSwitch(SumaMatchingTokenizer tokenizer, ParserContext parserContext)
		throws ParserException,
		IOException,
		SumaParseException,
		EvaluationException
	{
		tokenizer.matchToken(SumaTokenTypes.PARENTHESIS_START);
		Expression expression=parseExpression(tokenizer, parserContext);
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
					Instruction caseInstruction=parseInstruction(tokenizer, parserContext);
					Pair pair=new Pair(values, caseInstruction);
					cases.add(pair);
					break;
				case SumaTokenTypes.KEYWORD_DEFAULT:
					token=tokenizer.matchToken(SumaTokenTypes.COLON);
					defaultInstruction=parseInstruction(tokenizer, parserContext);
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
		Expression expression=parseExpression(tokenizer, parserContext);
		expression=expression.reduce(SumaEvaluatorFactory.getInstance());
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

	private Instruction parseLocalVars(SumaMatchingTokenizer tokenizer, ParserContext parserContext)
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
						initialization=parseExpression(tokenizer, parserContext);
						// TODO Hay que chequear que la variable no estuviera ya declarada.
						LocalVariableAssignation variableAssignation=new LocalVariableAssignation(tokenName.getValue(), initialization);
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
						LocalVariableAssignation variableAssignation2=new LocalVariableAssignation(tokenName.getValue(), UndefinedConstantExpression.getInstance());
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
				LocalVariableAssignation variableAssignation2=new LocalVariableAssignation(tokenName.getValue(), UndefinedConstantExpression.getInstance());
				variables.add(variableAssignation2);
				looping=false;
			}
		}
		while (looping);
		VariableAssignationsInstruction variableAssignationsInstruction=new VariableAssignationsInstruction(variables);
		return variableAssignationsInstruction;
	}

	// private Instruction parseNearestVars(SumaMatchingTokenizer tokenizer, ParserContext parserContext)
	// throws ParserException,
	// IOException,
	// SumaParseException
	// {
	// List<AbstractVariableAssignation> variables=new ArrayList<>();
	// boolean looping=true;
	// do
	// {
	// Token tokenName=tokenizer.matchToken(SumaTokenTypes.IDENTIFIER);
	// Token token=tokenizer.matchToken(SumaTokenTypes.OPERATOR_EQUALS);
	// Expression initialization=parseExpression(tokenizer, parserContext);
	// // TODO Hay que chequear que la variable no estuviera ya declarada.
	// LocalVariableAssignation variableAssignation=new LocalVariableAssignation(tokenName.getValue(), initialization);
	// variables.add(variableAssignation);
	// token=tokenizer.nextOptionalToken();
	// if (token.getType() != SumaTokenTypes.COMMA)
	// {
	// looping=false;
	// tokenizer.pushBack(token);
	// }
	// else
	// {
	// looping=false;
	// }
	// }
	// while (looping);
	// VariableAssignationsInstruction variableAssignationsInstruction=new VariableAssignationsInstruction(variables);
	// return variableAssignationsInstruction;
	// }
	//
	// private Instruction parseAfterIdentifier(Token identifierToken, SumaMatchingTokenizer tokenizer)
	// throws ParserException,
	// IOException,
	// SumaParseException,
	// EvaluationException
	// {
	// Token token=tokenizer.nextToken();
	// if (token == null)
	// {
	// throw new SumaParseException("¿Qué coño pinta aquí un identificador huérfano?");
	// }
	// else
	// {
	// Instruction instruction;
	// switch (token.getType())
	// {
	// case SumaTokenTypes.OPERATOR_EQUALS:
	// Expression rightSide=parseExpression(tokenizer, parserContext);
	// instruction=new AssignationInstruction(identifierToken.getValue(), rightSide);
	// break;
	// case SumaTokenTypes.PARENTHESIS_START:
	// List<Expression> arguments=parseExpressionList(tokenizer, parserContext);
	// tokenizer.matchToken(SumaTokenTypes.PARENTHESIS_END);
	// instruction=new ExpressionInstruction(new FunctionCallExpression(new IdentifierExpression(identifierToken.getValue()), arguments));
	// break;
	// case SumaTokenTypes.BRACKET_START:
	// Expression index=parseExpression(tokenizer, parserContext);
	// tokenizer.matchToken(SumaTokenTypes.BRACKET_END);
	// instruction=new ExpressionInstruction(new IndexedExpression(new IdentifierExpression(identifierToken.getValue()), index));
	// break;
	// case SumaTokenTypes.PERIOD:
	// instruction=DereferenceParser.getInstance().parseAfterIdentifier(tokenizer, new IdentifierExpression(token.getValue()));
	// break;
	// default:
	// throw new UnexpectedTokenException(token, SumaTokenTypes.OPERATOR_EQUALS, SumaTokenTypes.PARENTHESIS_START, SumaTokenTypes.PERIOD);
	// }
	// return instruction;
	// }
	// }

	private Instruction parseFor(SumaMatchingTokenizer tokenizer, ParserContext parserContext)
		throws ParserException,
		IOException,
		SumaParseException,
		EvaluationException
	{
		Instruction loopInstruction;
		tokenizer.matchToken(SumaTokenTypes.PARENTHESIS_START);
		Token token=tokenizer.nextToken();
		if (token.getType() == SumaTokenTypes.KEYWORD_VAR)
		{
			// Bucle colección:
			Token tokenVariable=tokenizer.matchToken(SumaTokenTypes.IDENTIFIER);
			tokenizer.matchToken(SumaTokenTypes.COLON);
			Expression expressionCollection=parseExpression(tokenizer, parserContext);
			tokenizer.matchToken(SumaTokenTypes.PARENTHESIS_END);
			token=tokenizer.nextToken();
			Expression preExpression;
			if (token.getType() == SumaTokenTypes.KEYWORD_WHILE)
			{
				preExpression=parseParenthesizedExpression(tokenizer, parserContext);
			}
			else
			{
				preExpression=null;
				tokenizer.pushBack(token);
			}
			Instruction innerInstruction=parseInstruction(tokenizer, parserContext);
			Expression postExpression;
			token=tokenizer.nextOptionalToken();
			if (token != null)
			{
				if (token.getType() == SumaTokenTypes.KEYWORD_WHILE)
				{
					postExpression=parseParenthesizedExpression(tokenizer, parserContext);
				}
				else
				{
					postExpression=null;
					tokenizer.pushBack(token);
				}
			}
			else
			{
				postExpression=null;
			}
			loopInstruction=new CollectionLoopInstruction(preExpression, innerInstruction, postExpression, tokenVariable.getValue(), expressionCollection);
		}
		else
		{
			// TODO Otros tipos de bucle
			throw new SumaParseException("Not supported yet!");
		}
		return loopInstruction;
	}

	private Expression parseParenthesizedExpression(SumaMatchingTokenizer tokenizer, ParserContext parserContext)
		throws ParserException,
		IOException
	{
		tokenizer.matchToken(SumaTokenTypes.PARENTHESIS_START);
		Expression expression=parseExpression(tokenizer, parserContext);
		tokenizer.matchToken(SumaTokenTypes.PARENTHESIS_END);
		return expression;
	}

	private Instruction parseAssignation(SumaMatchingTokenizer tokenizer, ParserContext parserContext, Expression leftSide)
		throws ParserException,
		IOException
	{
		if (leftSide instanceof Assignable)
		{
			Expression rightSide=parseExpression(tokenizer, parserContext);
			Instruction instruction=new AssignationInstruction((Assignable)leftSide, rightSide);
			return instruction;
		}
		else
		{
			throw new InvalidLeftSideException(leftSide);
		}
	}

	private Instruction parseAddAssignation(SumaMatchingTokenizer tokenizer, ParserContext parserContext, Expression leftSide)
		throws ParserException,
		IOException
	{
		if (leftSide instanceof Assignable)
		{
			Expression rightSide=parseExpression(tokenizer, parserContext);
			Instruction instruction=new AddAssignationInstruction((Assignable)leftSide, rightSide);
			return instruction;
		}
		else
		{
			throw new InvalidLeftSideException(leftSide);
		}
	}

	private Instruction parseSubstractAssignation(SumaMatchingTokenizer tokenizer, ParserContext parserContext, Expression leftSide)
		throws ParserException,
		IOException
	{
		if (leftSide instanceof Assignable)
		{
			Expression rightSide=parseExpression(tokenizer, parserContext);
			Instruction instruction=new SubstractAssignationInstruction((Assignable)leftSide, rightSide);
			return instruction;
		}
		else
		{
			throw new InvalidLeftSideException(leftSide);
		}
	}

	private Instruction parseMultiplyAssignation(SumaMatchingTokenizer tokenizer, ParserContext parserContext, Expression leftSide)
		throws ParserException,
		IOException
	{
		if (leftSide instanceof Assignable)
		{
			Expression rightSide=parseExpression(tokenizer, parserContext);
			Instruction instruction=new MultiplyAssignationInstruction((Assignable)leftSide, rightSide);
			return instruction;
		}
		else
		{
			throw new InvalidLeftSideException(leftSide);
		}
	}

	private Instruction parseDivideAssignation(SumaMatchingTokenizer tokenizer, ParserContext parserContext, Expression leftSide)
		throws ParserException,
		IOException
	{
		if (leftSide instanceof Assignable)
		{
			Expression rightSide=parseExpression(tokenizer, parserContext);
			Instruction instruction=new DivideAssignationInstruction((Assignable)leftSide, rightSide);
			return instruction;
		}
		else
		{
			throw new InvalidLeftSideException(leftSide);
		}
	}

	private Instruction parseFunctionDefinition(SumaMatchingTokenizer tokenizer, ParserContext parserContext)
		throws IOException,
		ParserException,
		EvaluationException
	{
		Token token=tokenizer.matchToken(SumaTokenTypes.IDENTIFIER);
		String functionName=token.getValue();
		List<String> parameterNames=parseParameterNames(tokenizer, parserContext);
		tokenizer.matchToken(SumaTokenTypes.KEY_START);
		Instruction body=parseInstruction(tokenizer, parserContext);
		tokenizer.matchToken(SumaTokenTypes.KEY_END);
		NamedCodedFunction codedFunction=new NamedCodedFunction(parameterNames, body, functionName);
		return new FunctionDeclarationInstruction(codedFunction);
	}

	private List<String> parseParameterNames(SumaMatchingTokenizer tokenizer, ParserContext parserContext)
		throws IOException,
		ParserException
	{
		tokenizer.matchToken(SumaTokenTypes.PARENTHESIS_START);
		List<String> params=new ArrayList<>();
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
						params.add(token.getValue());
						break;
					default:
						throw new UnexpectedTokenException(token, SumaTokenTypes.COMMA, SumaTokenTypes.PARENTHESIS_END);
				}
			}
		}
		while (looping);
		return params;
	}

	private Instruction parseReturn(SumaMatchingTokenizer tokenizer, ParserContext parserContext)
		throws IOException,
		ParserException,
		EvaluationException
	{
		Expression expression=parseExpression(tokenizer, parserContext);
		return new ReturnInstruction(expression);
	}

	public Expression parseUnnamedFunctionDeclaration(SumaMatchingTokenizer tokenizer, ParserContext parserContext)
		throws IOException,
		ParserException,
		EvaluationException
	{
		List<String> parameterNames=parseParameterNames(tokenizer, parserContext);
		tokenizer.matchToken(SumaTokenTypes.KEY_START);
		Instruction body=parseInstruction(tokenizer, parserContext);
		tokenizer.matchToken(SumaTokenTypes.KEY_END);
		CodedFunction codedFunction=new CodedFunction(parameterNames, body);
		return new UnnamedFunctionDeclarationExpression(codedFunction);
	}
}
