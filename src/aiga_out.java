package AIGenetic;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class aiga_out 
{
	static FileWriter txtout = null;
	static BufferedWriter c = null;
	//
	public aiga_out(String filename)
	{
		try
		{
			String path = new File(".").getCanonicalPath();
			path = path+"/"+filename;
			txtout = new FileWriter(path, true);
			c = new BufferedWriter(txtout);
		}
		catch(Exception e)
		{
			System.out.println("Unexpected Error occured::Details: ");
			e.printStackTrace();
			System.exit(0);
		}
	}
	public static void write_msg(String msg, boolean nL)
	{
		try
		{
			c.append(msg);
			if(nL)
			{
				c.newLine();
			}			
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
	}
	public static void exit()
	{
		try
		{
			c.close();
			txtout.close();
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
	}
}
