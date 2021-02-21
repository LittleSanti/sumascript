package com.samajackun.sumascript.core.instructions;

import com.samajackun.sumascript.core.SumaInstructionSerializerException;

public interface SumaInstructionSerializer
{
	String serializeCollectionLoop(CollectionLoopInstruction collectionLoopInstruction)
		throws SumaInstructionSerializerException;

	String serializeIndexedLoop(IndexedLoopInstruction indexedLoopInstruction)
		throws SumaInstructionSerializerException;

	String serializeLocalVariableAssignation(LocalVariableAssignation localVariableAssignation)
		throws SumaInstructionSerializerException;

	String serializeNearestVariableAssignation(NearestVariableAssignation nearestVariableAssignation)
		throws SumaInstructionSerializerException;

	String serializeAssignation(AssignationInstruction assignationInstruction)
		throws SumaInstructionSerializerException;

	String serializeAddAssignation(AddAssignationInstruction addAssignationInstruction)
		throws SumaInstructionSerializerException;

	String serializeDivideAssignation(DivideAssignationInstruction divideAssignationInstruction)
		throws SumaInstructionSerializerException;

	String serializeMultiplyAssignation(MultiplyAssignationInstruction multiplyAssignationInstruction)
		throws SumaInstructionSerializerException;

	String serializeSubtractsAssignation(SubstractAssignationInstruction substractAssignationInstruction)
		throws SumaInstructionSerializerException;

	String serializeBreak(BreakInstruction breakInstruction)
		throws SumaInstructionSerializerException;

	String serializeBlock(BlockInstruction blockInstruction)
		throws SumaInstructionSerializerException;

	String serializeChangeCurrentDirectory(ChangeCurrentDirectoryInstruction changeCurrentDirectoryInstruction)
		throws SumaInstructionSerializerException;

	String serializeDecrement(DecrementInstruction decrementInstruction)
		throws SumaInstructionSerializerException;

	String serializeEchoErr(EchoErrInstruction echoErrInstruction)
		throws SumaInstructionSerializerException;

	String serializeEchoOut(EchoOutInstruction echoOutInstruction)
		throws SumaInstructionSerializerException;

	String serializeExpression(ExpressionInstruction expressionInstruction)
		throws SumaInstructionSerializerException;

	String serializeFunctionDeclaration(FunctionDeclarationInstruction functionDeclarationInstruction)
		throws SumaInstructionSerializerException;

	String serializeIf(IfInstruction ifInstruction)
		throws SumaInstructionSerializerException;

	String serializeIncrement(IncrementInstruction incrementInstruction)
		throws SumaInstructionSerializerException;

	String serializeIndexedAssignation(IndexedAssignationInstruction indexedAssignationInstruction)
		throws SumaInstructionSerializerException;

	String serializeReturn(ReturnInstruction returnInstruction)
		throws SumaInstructionSerializerException;

	String serializeSwitch(SwitchInstruction switchInstruction)
		throws SumaInstructionSerializerException;

	String serializeThrow(ThrowInstruction throwInstruction)
		throws SumaInstructionSerializerException;

	String serializeTryCatch(TryCatchInstruction tryCatchInstruction)
		throws SumaInstructionSerializerException;

	String serializeVariableAssignation(VariableAssignationsInstruction variableAssignationsInstruction)
		throws SumaInstructionSerializerException;

}
