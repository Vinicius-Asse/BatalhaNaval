import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ManipuladorArquivo  {

	public static String leituraArquivo() throws IOException {
		BufferedReader leitor = new BufferedReader(new FileReader ("src/save.txt"));
		String s = "";
		while(leitor.read() > 0) {
			s += leitor.readLine() + "\n";
		}
		leitor.close();
		System.out.println(s);
		return s;
	}
	public static void escritaArquivo(String texto) throws IOException {
		BufferedWriter escritor = new BufferedWriter(new FileWriter("src/save.txt"));
		escritor.write(texto);
		escritor.close();
	}
}
