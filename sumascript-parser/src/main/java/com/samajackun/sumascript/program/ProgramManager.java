package com.samajackun.sumascript.program;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.samajackun.sumascript.parser.Program;

public final class ProgramManager
{
	public static final String SOURCE_CODE_EXT=".sss";

	public static final String BINARY_EXT=".ssc";

	private static final ProgramManager INSTANCE=new ProgramManager();

	public static ProgramManager getInstance()
	{
		return INSTANCE;
	}

	private ProgramManager()
	{
	}

	public Program load(InputStream input, String name)
		throws IOException,
		ProgramLoadException
	{
		if (hasTextSourceExtension(name))
		{
			return ProgramLoaderManager.getInstance().getLoader(Versions.TEXT_SOURCE_CODE).load(input);
		}
		else if (hasBinaryExtension(name))
		{
			VersionId loaderId=new VersionId((byte)input.read(), (byte)input.read(), (byte)input.read(), (byte)input.read());
			return ProgramLoaderManager.getInstance().getLoader(loaderId).load(input);
		}
		else
		{
			throw new ProgramLoadException("Unknown source code format " + name);
		}
	}

	private static boolean hasTextSourceExtension(String name)
	{
		return name.endsWith(SOURCE_CODE_EXT);
	}

	private static boolean hasBinaryExtension(String name)
	{
		return name.endsWith(BINARY_EXT);
	}

	public void save(Program program, OutputStream output, String name)
		throws IOException,
		ProgramLoadException
	{
		if (hasTextSourceExtension(name))
		{
			ProgramLoaderManager.getInstance().getLoader(Versions.TEXT_SOURCE_CODE).save(program, output);
		}
		else if (hasBinaryExtension(name))
		{
			ProgramLoaderManager.getInstance().getNewestLoader().save(program, output);
		}
		else
		{
			throw new ProgramLoadException("Unknown fileame extension format " + name);
		}
		// try (ObjectOutputStream objectOutputStream=new ObjectOutputStream(output))
		// {
		// objectOutputStream.writeObject(program);
		// }
	}
}
