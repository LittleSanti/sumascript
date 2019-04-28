package com.samajackun.sumascript.parser;

public final class DereferenceParser
{
	private static final DereferenceParser INSTANCE=new DereferenceParser();

	public static DereferenceParser getInstance()
	{
		return INSTANCE;
	}

	private DereferenceParser()
	{
	}

	// public Instruction parseAfterIdentifier(SumaMatchingTokenizer tokenizer, Expression parentExpression)
	// throws SumaParseException,
	// IOException,
	// ParserException
	// {
	// boolean looping=true;
	// Expression base=parentExpression;
	// Instruction instruction=null;
	// do
	// {
	// SumaToken token=tokenizer.nextToken();
	// if (token == null)
	// {
	// looping=false;
	// }
	// else
	// {
	// Expression index;
	// switch (token.getType())
	// {
	// case PERIOD:
	// SumaToken token3=tokenizer.matchToken(Type.IDENTIFIER);
	// Expression index3=new TextConstantExpression(token3.getValue());
	// SumaToken token4=tokenizer.nextOptionalToken();
	// if (token4.getType() == Type.OPERATOR_EQUALS)
	// {
	// Expression value=parseExpression(tokenizer);
	// instruction=new IndexedAssignationInstruction(base, index3, value);
	// }
	// else
	// {
	// tokenizer.pushBack(token4);
	// base=new IndexedExpression(base, index3);
	// }
	// break;
	// case BRACKET_START:
	// index=parseExpression(tokenizer);
	// tokenizer.matchToken(Type.BRACKET_END);
	// SumaToken token2=tokenizer.nextToken();
	// if (token2 != null)
	// {
	// if (token2.getType() == Type.OPERATOR_EQUALS)
	// {
	// Expression value=parseExpression(tokenizer);
	// instruction=new IndexedAssignationInstruction(base, index, value);
	// }
	// else
	// {
	// tokenizer.pushBack(token2);
	// base=new IndexedExpression(base, index);
	// }
	// }
	// else
	// {
	// base=new IndexedExpression(base, index);
	// }
	// break;
	// case PARENTHESIS_START:
	// break;
	// case OPERATOR_EQUALS:
	// Expression value=parseExpression(tokenizer);
	// instruction=new NearestVariableAssignation(base, value);
	// break;
	// default:
	// tokenizer.pushBack(token);
	// looping=false;
	// }
	// if (looping)
	// {
	// }
	// }
	// }
	// while (instruction == null && looping);
	// return instruction;
	// }
	//
	// private Expression parseExpression(SumaMatchingTokenizer tokenizer)
	// throws TokenizerException,
	// ParserException,
	// IOException
	// {
	// return ExpressionParser.getInstance().parse(new SqlMatchingTokenizer(new SqlTokenizer(tokenizer.getSource(), new SqlTokenizerSettings().setUnexpectedSymbolBehaviour(UnexpectedSymbolBehaviour.END_PARSING))));
	// }
	//
	// public Instruction parseAfterIdentifier2(SumaMatchingTokenizer tokenizer, SumaToken tokenIdentifier, SumaToken token)
	// throws ParserException,
	// IOException
	// {
	// Instruction instruction;
	// if (token != null)
	// {
	// Expression index;
	// switch (token.getType())
	// {
	// case PERIOD:
	// SumaToken token3=tokenizer.matchToken(Type.IDENTIFIER);
	// Expression index3=new TextConstantExpression(token3.getValue());
	// SumaToken token4=tokenizer.nextOptionalToken();
	// if (token4.getType() == Type.OPERATOR_EQUALS)
	// {
	// Expression value=parseExpression(tokenizer);
	// instruction=new IndexedAssignationInstruction(base, index3, value);
	// }
	// else
	// {
	// tokenizer.pushBack(token4);
	// base=new IndexedExpression(base, index3);
	// }
	// break;
	// case BRACKET_START:
	// index=parseExpression(tokenizer);
	// tokenizer.matchToken(Type.BRACKET_END);
	// SumaToken token2=tokenizer.nextToken();
	// if (token2 != null)
	// {
	// if (token2.getType() == Type.OPERATOR_EQUALS)
	// {
	// Expression value=parseExpression(tokenizer);
	// instruction=new IndexedAssignationInstruction(base, index, value);
	// }
	// else
	// {
	// tokenizer.pushBack(token2);
	// base=new IndexedExpression(base, index);
	// }
	// }
	// else
	// {
	// base=new IndexedExpression(base, index);
	// }
	// break;
	// case PARENTHESIS_START:
	// break;
	// case OPERATOR_EQUALS:
	// Expression value=parseExpression(tokenizer);
	// instruction=new NearestVariableAssignation(base, value);
	// break;
	// default:
	// tokenizer.pushBack(token);
	// looping=false;
	// }
	// }
	// return instruction;
	// }

}
