import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

	public static void main(String[] args) {
		System.out.println("");

		try {
			Encoder e = new Encoder();
			Decoder d = new Decoder();
			boolean f = true;
			Scanner scan = new Scanner(System.in);
			while (f) {
				System.out.println("------------------------------");
				System.out.println("Please make a choice");
				System.out.println("1.Compress File");
				System.out.println("2.Decompress File");
				System.out.println("3.Compress Folder");
				System.out.println("4.Decompress Folder");
				System.out.println("5.Exit");
				int choice = scan.nextInt();
				String path = "";
				switch (choice) {
				case 1:
					System.out.println("Enter File name and path");
					path = scan.next();
					System.out.println("----------------------");
					e.encodeSingle(path);
					break;
				case 2:
					System.out.println("Enter Compressed File name path");
					path = scan.next();
					System.out.println("----------------------");
					d.readHeaderAndBody(path);
					break;
				case 3:
					System.out.println("Enter Folder path");
					path = scan.next();
					System.out.println("----------------------");
					e.encodeFolder(path);
					break;
				case 4:
					System.out.println("Enter  Folder path");
					path = scan.next();
					System.out.println("----------------------");
					d.readMultiHeaderAndBody(path);
					break;

				default:
					f = false;
					break;

				}

			}

		} catch (

		Exception ex) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
		}

	}
}
