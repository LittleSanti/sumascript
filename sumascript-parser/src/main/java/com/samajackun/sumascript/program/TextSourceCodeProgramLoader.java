package com.samajackun.sumascript.program;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

import com.samajackun.sumascript.core.SumaInstructionSerializerException;
import com.samajackun.sumascript.parser.Program;
import com.samajackun.sumascript.parser.SumaParseException;
import com.samajackun.sumascript.parser.SumaParser;
import com.samajackun.sumascript.serializer.SumaCodeInstructionSerializer;

class TextSourceCodeProgramLoader implements ProgramLoader
{
	private static final TextSourceCodeProgramLoader INSTANCE=new TextSourceCodeProgramLoader();

	public static TextSourceCodeProgramLoader getInstance()
	{
		return INSTANCE;
	}

	private TextSourceCodeProgramLoader()
	{
	}

	@Override
	public Program load(InputStream input)
		throws IOException,
		ProgramLoadException
	{
		// TODO Permitir especificar un encoding distinto
		try
		{
			return SumaParser.getInstance().parse(new InputStreamReader(input, StandardCharsets.ISO_8859_1));
		}
		catch (SumaParseException e)
		{
			throw new ProgramLoadException(e);
		}
	}

	@Override
	public void save(Program program, OutputStream output)
		throws IOException
	{
		// TODO Permitir especificar un encoding distinto
		try (Writer writer=new OutputStreamWriter(output, StandardCharsets.ISO_8859_1))
		{
			writer.write(program.getBlock().toCode(SumaCodeInstructionSerializer.getInstance()));
		}
		catch (SumaInstructionSerializerException e)
		{
			throw new IOException(e);
		}
	}
}
