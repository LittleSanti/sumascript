package com.samajackun.sumascript.program;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import com.samajackun.sumascript.parser.Program;

class BinaryCodeProgramLoader implements ProgramLoader
{
	private final VersionId loaderId;

	public BinaryCodeProgramLoader(VersionId loaderId)
	{
		super();
		this.loaderId=loaderId;
	}

	@Override
	public Program load(InputStream input)
		throws IOException,
		ProgramLoadException
	{
		try (ObjectInputStream objectInputStream=new ObjectInputStream(input))
		{
			return Program.class.cast(objectInputStream.readObject());
		}
		catch (ClassNotFoundException e)
		{
			throw new ProgramLoadException(e.getMessage());
		}
	}

	@Override
	public void save(Program program, OutputStream output)
		throws IOException
	{
		output.write(this.loaderId.getB0());
		output.write(this.loaderId.getB1());
		output.write(this.loaderId.getB2());
		output.write(this.loaderId.getB3());
		try (ObjectOutputStream objectOutputStream=new ObjectOutputStream(output))
		{
			objectOutputStream.writeObject(program);
		}
	}
}
