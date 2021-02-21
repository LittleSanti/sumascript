package com.samajackun.sumascript.serializer;

import java.util.Set;

import com.samajackun.rodas.core.eval.Name;
import com.samajackun.sumascript.core.Instruction;
import com.samajackun.sumascript.core.SumaInstructionSerializerException;
import com.samajackun.sumascript.core.instructions.AbstractVariableAssignation;
import com.samajackun.sumascript.core.instructions.AddAssignationInstruction;
import com.samajackun.sumascript.core.instructions.AssignationInstruction;
import com.samajackun.sumascript.core.instructions.BlockInstruction;
import com.samajackun.sumascript.core.instructions.BreakInstruction;
import com.samajackun.sumascript.core.instructions.ChangeCurrentDirectoryInstruction;
import com.samajackun.sumascript.core.instructions.CollectionLoopInstruction;
import com.samajackun.sumascript.core.instructions.DecrementInstruction;
import com.samajackun.sumascript.core.instructions.DivideAssignationInstruction;
import com.samajackun.sumascript.core.instructions.EchoErrInstruction;
import com.samajackun.sumascript.core.instructions.EchoOutInstruction;
import com.samajackun.sumascript.core.instructions.ExpressionInstruction;
import com.samajackun.sumascript.core.instructions.FunctionDeclarationInstruction;
import com.samajackun.sumascript.core.instructions.IfInstruction;
import com.samajackun.sumascript.core.instructions.IncrementInstruction;
import com.samajackun.sumascript.core.instructions.IndexedAssignationInstruction;
import com.samajackun.sumascript.core.instructions.IndexedLoopInstruction;
import com.samajackun.sumascript.core.instructions.LocalVariableAssignation;
import com.samajackun.sumascript.core.instructions.MultiplyAssignationInstruction;
import com.samajackun.sumascript.core.instructions.NearestVariableAssignation;
import com.samajackun.sumascript.core.instructions.ReturnInstruction;
import com.samajackun.sumascript.core.instructions.SubstractAssignationInstruction;
import com.samajackun.sumascript.core.instructions.SumaInstructionSerializer;
import com.samajackun.sumascript.core.instructions.SwitchInstruction;
import com.samajackun.sumascript.core.instructions.SwitchInstruction.Pair;
import com.samajackun.sumascript.core.instructions.ThrowInstruction;
import com.samajackun.sumascript.core.instructions.TryCatchInstruction;
import com.samajackun.sumascript.core.instructions.VariableAssignationsInstruction;
import com.samajackun.sumascript.core.runtime.NamedCodedFunction;
import com.samajackun.sumascript.tokenizer.SumaTokens;

public final class SumaCodeInstructionSerializer implements SumaInstructionSerializer
{
	private static final SumaCodeInstructionSerializer INSTANCE=new SumaCodeInstructionSerializer();

	public static SumaCodeInstructionSerializer getInstance()
	{
		return INSTANCE;
	}

	private SumaCodeInstructionSerializer()
	{
	}

	private static void addLine(StringBuilder stb, String code)
	{
		stb.append(code);
		stb.append("\n");
	}

	@Override
	public String serializeCollectionLoop(CollectionLoopInstruction collectionLoopInstruction)
		throws SumaInstructionSerializerException
	{
		StringBuilder stb=new StringBuilder(2048);
		addLine(stb, SumaTokens.KEYWORD_FOR.getImage() + SumaTokens.PARENTHESIS_START.getImage() + SumaTokens.KEYWORD_VAR.getImage() + " " + collectionLoopInstruction.getVarName().toString() + ":" + collectionLoopInstruction.getExpressionCollection().toCode() + SumaTokens.PARENTHESIS_END.getImage());
		addLine(stb, collectionLoopInstruction.getInnerInstruction().toCode(this));
		return stb.toString();
	}

	@Override
	public String serializeIndexedLoop(IndexedLoopInstruction indexedLoopInstruction)
		throws SumaInstructionSerializerException
	{
		StringBuilder stb=new StringBuilder(2048);
		StringBuilder stbAssignations=new StringBuilder(2048);
		// FIXME Falta indexedLoopInstruction.getPreCondition()
		for (AbstractVariableAssignation assignation : indexedLoopInstruction.getAssignations())
		{
			stbAssignations.append(assignation.toCode(this)).append(SumaTokens.COLON.getImage());
		}
		StringBuilder stbPostSteps=new StringBuilder(2048);
		for (Instruction instruction : indexedLoopInstruction.getPostStepInstructions())
		{
			stbAssignations.append(instruction.toCode(this)).append(SumaTokens.COLON.getImage());
		}
		addLine(stb, SumaTokens.KEYWORD_FOR.getImage() + SumaTokens.PARENTHESIS_START.getImage() + stbAssignations.toString() + SumaTokens.COLON.getImage() + indexedLoopInstruction.getPreCondition().toCode() + SumaTokens.COLON.getImage() + stbPostSteps + SumaTokens.PARENTHESIS_END.getImage());
		addLine(stb, indexedLoopInstruction.getInnerInstruction().toCode(this));
		return stb.toString();
	}

	@Override
	public String serializeLocalVariableAssignation(LocalVariableAssignation localVariableAssignation)
		throws SumaInstructionSerializerException
	{
		return SumaTokens.KEYWORD_VAR.getImage() + localVariableAssignation.getName().toString() + SumaTokens.OPERATOR_EQUALS.getImage() + localVariableAssignation.getExpression().toCode();
	}

	@Override
	public String serializeNearestVariableAssignation(NearestVariableAssignation nearestVariableAssignation)
		throws SumaInstructionSerializerException
	{
		return nearestVariableAssignation.getName().toString() + SumaTokens.OPERATOR_EQUALS.getImage() + nearestVariableAssignation.getExpression().toCode();
	}

	@Override
	public String serializeAssignation(AssignationInstruction assignationInstruction)
		throws SumaInstructionSerializerException
	{
		return assignationInstruction.getLeftSide().toCode() + SumaTokens.OPERATOR_EQUALS.getImage() + assignationInstruction.getRightSide().toCode();
	}

	@Override
	public String serializeAddAssignation(AddAssignationInstruction addAssignationInstruction)
		throws SumaInstructionSerializerException
	{
		return addAssignationInstruction.getLeftSide().toCode() + SumaTokens.OPERATOR_PLUS_EQUALS.getImage() + addAssignationInstruction.getRightSide().toCode();
	}

	@Override
	public String serializeDivideAssignation(DivideAssignationInstruction divideAssignationInstruction)
		throws SumaInstructionSerializerException
	{
		return divideAssignationInstruction.getLeftSide().toCode() + SumaTokens.OPERATOR_DIVIDE_EQUALS.getImage() + divideAssignationInstruction.getRightSide().toCode();
	}

	@Override
	public String serializeMultiplyAssignation(MultiplyAssignationInstruction multiplyAssignationInstruction)
		throws SumaInstructionSerializerException
	{
		return multiplyAssignationInstruction.getLeftSide().toCode() + SumaTokens.OPERATOR_ASTERISK_EQUALS.getImage() + multiplyAssignationInstruction.getRightSide().toCode();
	}

	@Override
	public String serializeSubtractsAssignation(SubstractAssignationInstruction substractAssignationInstruction)
		throws SumaInstructionSerializerException
	{
		return substractAssignationInstruction.getLeftSide().toCode() + SumaTokens.OPERATOR_MINUS_EQUALS.getImage() + substractAssignationInstruction.getRightSide().toCode();
	}

	@Override
	public String serializeBreak(BreakInstruction breakInstruction)
		throws SumaInstructionSerializerException
	{
		return SumaTokens.KEYWORD_BREAK.getImage();
	}

	@Override
	public String serializeBlock(BlockInstruction blockInstruction)
		throws SumaInstructionSerializerException
	{
		StringBuilder stb=new StringBuilder(2048);
		addLine(stb, "{");
		for (Instruction instruction : blockInstruction.getInstructions())
		{
			addLine(stb, instruction.toCode(this));
		}
		addLine(stb, "}");
		return stb.toString();
	}

	@Override
	public String serializeChangeCurrentDirectory(ChangeCurrentDirectoryInstruction changeCurrentDirectoryInstruction)
		throws SumaInstructionSerializerException
	{
		return "cd " + changeCurrentDirectoryInstruction.getNewDirectory();
	}

	@Override
	public String serializeDecrement(DecrementInstruction decrementInstruction)
		throws SumaInstructionSerializerException
	{
		return decrementInstruction.getLeftSide().toCode() + SumaTokens.OPERATOR_MINUS_MINUS.getImage();
	}

	@Override
	public String serializeEchoErr(EchoErrInstruction echoErrInstruction)
		throws SumaInstructionSerializerException
	{
		return SumaTokens.KEYWORD_ECHO_ERR.getImage() + " " + echoErrInstruction.getExpression().toCode();
	}

	@Override
	public String serializeEchoOut(EchoOutInstruction echoOutInstruction)
		throws SumaInstructionSerializerException
	{
		return SumaTokens.KEYWORD_ECHO_OUT.getImage() + " " + echoOutInstruction.getExpression().toCode();
	}

	@Override
	public String serializeExpression(ExpressionInstruction expressionInstruction)
		throws SumaInstructionSerializerException
	{
		return expressionInstruction.getExpression().toCode();
	}

	@Override
	public String serializeFunctionDeclaration(FunctionDeclarationInstruction functionDeclarationInstruction)
		throws SumaInstructionSerializerException
	{
		return SumaTokens.KEYWORD_VAR.getImage() + " " + serializeCodedFunction(functionDeclarationInstruction.getCodedFunction());
	}

	private String serializeCodedFunction(NamedCodedFunction codedFunction)
	{
		StringBuilder stbParameters=new StringBuilder(100 * codedFunction.getParameterNames().size());
		for (Name parameter : codedFunction.getParameterNames())
		{
			if (stbParameters.length() > 0)
			{
				stbParameters.append(SumaTokens.COMMA).append(" ");
			}
			stbParameters.append(parameter.getBase().toString());
		}
		return codedFunction.getName().toString() + SumaTokens.PARENTHESIS_START.getImage() + stbParameters + SumaTokens.PARENTHESIS_END.getImage() + "{unknown body}";
	}

	@Override
	public String serializeIf(IfInstruction ifInstruction)
		throws SumaInstructionSerializerException
	{
		return SumaTokens.KEYWORD_IF.getImage() + SumaTokens.PARENTHESIS_START.getImage() + ifInstruction.getExpression().toCode() + SumaTokens.PARENTHESIS_END.getImage() + ifInstruction.getAfirmative().toCode(this) + " " + SumaTokens.KEYWORD_ELSE.getImage() + " " + ifInstruction.getNegative().toCode(this);
	}

	@Override
	public String serializeIncrement(IncrementInstruction incrementInstruction)
		throws SumaInstructionSerializerException
	{
		return incrementInstruction.getLeftSide().toCode() + SumaTokens.OPERATOR_PLUS_PLUS.getImage();
	}

	@Override
	public String serializeIndexedAssignation(IndexedAssignationInstruction indexedAssignationInstruction)
		throws SumaInstructionSerializerException
	{
		return indexedAssignationInstruction.getBase() + SumaTokens.BRACKET_START.getImage() + indexedAssignationInstruction.getIndex().toCode() + SumaTokens.BRACKET_END.getImage() + SumaTokens.OPERATOR_EQUALS.getImage() + indexedAssignationInstruction.getExpression().toCode();
	}

	@Override
	public String serializeReturn(ReturnInstruction returnInstruction)
		throws SumaInstructionSerializerException
	{
		return SumaTokens.KEYWORD_RETURN.getImage() + " " + returnInstruction.getExpression().toCode();
	}

	@Override
	public String serializeSwitch(SwitchInstruction switchInstruction)
		throws SumaInstructionSerializerException
	{
		StringBuilder stb=new StringBuilder(1024 * switchInstruction.getPairs().size());
		addLine(stb, SumaTokens.KEYWORD_SWITCH.getImage() + SumaTokens.PARENTHESIS_START.getImage() + switchInstruction.getExpression().toCode() + SumaTokens.PARENTHESIS_END.getImage());
		addLine(stb, SumaTokens.KEY_START.getImage());
		for (Pair pair : switchInstruction.getPairs())
		{
			String values=serializeWithInterSeparator(pair.getMatchingValues(), SumaTokens.COMMA.getImage());
			addLine(stb, SumaTokens.KEYWORD_CASE.getImage() + " " + values + SumaTokens.COLON.getImage());
			addLine(stb, pair.getInstruction().toCode(this));
		}
		if (switchInstruction.getDefaultInstruction() != null)
		{
			addLine(stb, SumaTokens.KEYWORD_DEFAULT.getImage() + SumaTokens.COLON.getImage());
			addLine(stb, switchInstruction.getDefaultInstruction().toCode(this));
		}
		addLine(stb, SumaTokens.KEY_END.getImage());
		return stb.toString();
	}

	private static String serializeWithInterSeparator(Set<Object> values, String interSeparator)
	{
		String s="";
		for (Object value : values)
		{
			if (s.length() > 0)
			{
				s+=interSeparator;
			}
			// FIXME Este Object.toString es sospechoso.
			s+=value.toString();
		}
		return s;
	}

	@Override
	public String serializeThrow(ThrowInstruction throwInstruction)
		throws SumaInstructionSerializerException
	{
		return SumaTokens.KEYWORD_THROW.getImage() + " " + throwInstruction.getThrownExpression().toCode();
	}

	@Override
	public String serializeTryCatch(TryCatchInstruction tryCatchInstruction)
		throws SumaInstructionSerializerException
	{
		StringBuilder stb=new StringBuilder(2048);
		addLine(stb, SumaTokens.KEYWORD_TRY.getImage());
		addLine(stb, SumaTokens.KEY_START.getImage());
		addLine(stb, tryCatchInstruction.getTryInstruction().toCode(this));
		addLine(stb, SumaTokens.KEY_END.getImage());
		addLine(stb, SumaTokens.KEYWORD_CATCH.getImage() + SumaTokens.PARENTHESIS_START.getImage() + " " + tryCatchInstruction.getExceptionName().toString() + SumaTokens.PARENTHESIS_END.getImage());
		addLine(stb, SumaTokens.KEY_START.getImage());
		addLine(stb, tryCatchInstruction.getCatchInstruction().toCode(this));
		addLine(stb, SumaTokens.KEY_END.getImage());
		return stb.toString();
	}

	@Override
	public String serializeVariableAssignation(VariableAssignationsInstruction variableAssignationsInstruction)
		throws SumaInstructionSerializerException
	{
		StringBuilder stb=new StringBuilder(1024 * variableAssignationsInstruction.getList().size());
		for (AbstractVariableAssignation assignation : variableAssignationsInstruction.getList())
		{
			if (stb.length() > 0)
			{
				stb.append(",");
			}
			addLine(stb, assignation.toCode(this));
		}
		return SumaTokens.KEYWORD_VAR.getImage() + " " + stb.toString();
	}
}
