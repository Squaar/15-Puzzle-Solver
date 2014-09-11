package test;

import java.util.Scanner;
import java.io.File;
import java.io.IOException;

public class Main{
	public static void main(String args[]){
		try{
			System.out.println(args[0]);
			Board board = getPuzzle(args[0]);

		}
		catch(ArrayIndexOutOfBoundsException e){
			System.err.println("Please specify a file with the puzzle.");
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}

	public static Board getPuzzle(String filePath) throws IOException{
		Scanner sc = new Scanner(new File(filePath));
		String json = sc.useDelimiter("\\A").next();
		sc.close();

		System.out.println(json);
		return null;
	}
}
