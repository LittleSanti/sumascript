package com.samajackun.sumascript.program;

public class VersionId implements Comparable<VersionId>
{
	private final byte b0;

	private final byte b1;

	private final byte b2;

	private final byte b3;

	public VersionId(byte b0, byte b1, byte b2, byte b3)
	{
		super();
		this.b0=b0;
		this.b1=b1;
		this.b2=b2;
		this.b3=b3;
	}

	@Override
	public int compareTo(VersionId o)
	{
		return this.b0 < o.b0
			? -1
			: (this.b0 > o.b0
				? 1
				: (this.b1 < o.b1
					? -1
					: (this.b1 > o.b1
						? 1
						: (this.b2 < o.b2
							? -1
							: (this.b2 > o.b2
								? 1
								: (this.b3 < o.b3
									? -1
									: (this.b3 > o.b3
										? 1
										: (0))))))));
	}

	public byte getB0()
	{
		return this.b0;
	}

	public byte getB1()
	{
		return this.b1;
	}

	public byte getB2()
	{
		return this.b2;
	}

	public byte getB3()
	{
		return this.b3;
	}

	@Override
	public String toString()
	{
		return this.b0 + ", " + this.b1 + ", " + this.b2 + ", " + this.b3;
	}

}
