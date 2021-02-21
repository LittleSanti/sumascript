package com.samajackun.sumascript.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.samajackun.rodas.core.eval.Name;
import com.samajackun.rodas.core.model.Expression;
import com.samajackun.rodas.parsing.parser.ParserException;
import com.samajackun.rodas.parsing.parser.UnexpectedTokenException;
import com.samajackun.rodas.sql.parser.ParserContext;
import com.samajackun.rodas.sql.tokenizer.Token;
import com.samajackun.sumascript.core.instructions.AbstractVariableAssignation;
import com.samajackun.sumascript.core.instructions.LocalVariableDeclarationAndAssignation;
import com.samajackun.sumascript.core.instructions.NearestVariableAssignation;
import com.samajackun.sumascript.tokenizer.SumaMatchingTokenizer;
import com.samajackun.sumascript.tokenizer.SumaTokenTypes;

public final class VariableParser
{
	private static final VariableParser INSTANCE=new VariableParser();

	public static VariableParser getInstance()
	{
		return INSTANCE;
	}

	private VariableParser()
	{
	}

	public List<AbstractVariableAssignation> parseVariableAssignationList(SumaMatchingTokenizer tokenizer, ParserContext parserContext)
		throws IOException,
		ParserException
	{
		// var i -> LocalVariableDeclarationAndAssignation
		// var i=1 -> LocalVariableDeclarationAndAssignation
		// i=1 -> NearestVariableAssignation
		List<AbstractVariableAssignation> assignations=new ArrayList<>();
		Token token=tokenizer.nextToken();
		boolean local;
		if (token.getType() == SumaTokenTypes.KEYWORD_VAR)
		{
			local=true;
		}
		else
		{
			local=false;
			tokenizer.pushBack(token);
		}
		boolean looping=true;
		while (looping)
		{
			AbstractVariableAssignation variableAssignation=parseVariableAssignation(tokenizer, parserContext, local);
			if (variableAssignation != null)
			{
				assignations.add(variableAssignation);
			}
			token=tokenizer.nextToken();
			if (token.getType() != SumaTokenTypes.COMMA)
			{
				looping=false;
				tokenizer.pushBack(token);
			}
		}
		return assignations;
	}

	public AbstractVariableAssignation parseVariableAssignation(SumaMatchingTokenizer tokenizer, ParserContext parserContext, boolean local)
		throws IOException,
		ParserException
	{
		AbstractVariableAssignation variableAssignation;
		Token token=tokenizer.nextToken();
		// Buscando identificador
		if (token.getType() == SumaTokenTypes.IDENTIFIER)
		{
			Name varName=Name.instanceOf(token.getImage());
			// Buscando asignación opcional:
			token=tokenizer.nextToken();
			if (token.getType() == SumaTokenTypes.OPERATOR_EQUALS)
			{
				Expression initialValue=CommonParser.getInstance().parseExpression(tokenizer, parserContext);
				variableAssignation=(local)
					? new LocalVariableDeclarationAndAssignation(varName, initialValue)
					: new NearestVariableAssignation(varName, initialValue);
			}
			else
			{
				variableAssignation=local
					? new LocalVariableDeclarationAndAssignation(varName)
					: new NearestVariableAssignation(varName);
				tokenizer.pushBack(token);
			}
		}
		else
		{
			if (local)
			{
				throw new UnexpectedTokenException(token, SumaTokenTypes.IDENTIFIER);
			}
			else
			{
				variableAssignation=null;
				tokenizer.pushBack(token);
			}
		}
		return variableAssignation;
	}

}
