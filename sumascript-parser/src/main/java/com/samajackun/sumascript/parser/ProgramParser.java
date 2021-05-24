package com.samajackun.sumascript.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.parsing.parser.ParserException;
import com.samajackun.sumascript.core.Instruction;
import com.samajackun.sumascript.core.instructions.BlockInstruction;
import com.samajackun.sumascript.core.runtime.NamedCodedFunction;
import com.samajackun.sumascript.tokenizer.SumaMatchingTokenizer;

public final class ProgramParser
{
	private static final ProgramParser INSTANCE=new ProgramParser();

	public static ProgramParser getInstance()
	{
		return INSTANCE;
	}

	private ProgramParser()
	{
	}

	public Program parse(SumaMatchingTokenizer tokenizer, SumaParserContext parserContext)
		throws SumaParseException,
		IOException,
		ParserException,
		EvaluationException
	{
		List<Instruction> instructions=new ArrayList<>();
		List<NamedCodedFunction> parsedFunctions=new ArrayList<>();
		CommonParser.getInstance().parseInstructions(tokenizer, parserContext, instructions);
		Program program=new Program(new BlockInstruction(instructions), parsedFunctions);
		return program;
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
	// throw new SumaParseException("�Qu� co�o pinta aqu� un identificador hu�rfano?");
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

}
