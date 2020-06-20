package com.samajackun.sumascript.program;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.samajackun.sumascript.parser.Program;

public interface ProgramLoader
{
	public Program load(InputStream input)
		throws IOException,
		ProgramLoadException;

	public void save(Program program, OutputStream output)
		throws IOException;
}
