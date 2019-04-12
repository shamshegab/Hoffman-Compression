
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Shams Sherif
 */
public class Decoder {
    ArrayList<String > names=new ArrayList<>();
    public static final char END_OF_HEADER = '>';
    public static final char END_OF_ONE_HEADER = '^';
    ArrayList< Hashtable<String, Character>> headers = new ArrayList<>();
    private String header = "";
    private String body = "";

    public String getHeader() {
        return header;
    }

    public String getBody() {
        return body;
    }

    public String decode(String file, Hashtable<String, Character> charTable, int count)
            throws FileNotFoundException, IOException {

        String res = readbytes(file);

        String accu = "";
        String decoded = "";

        for (int i = 0; i < count; i++) {
            accu += res.charAt(i);

            if (charTable.containsKey(accu)) {
                decoded += charTable.get(accu);
                accu = "";
            }
        }
        File f = new File("decompressedFile.txt");
        FileWriter wr = new FileWriter(f);
        BufferedWriter out = new BufferedWriter(wr);
        out.write(decoded);
        out.close();
        wr.close();
        return decoded;
    }

    public String decodeSingle(String header, String file) throws IOException {
        File Header = new File(header);
        int count = 0;
        Hashtable<String, Character> charTable = new Hashtable<>();
        try (BufferedReader br = new BufferedReader(new FileReader(Header))) {
            String line;
            count = Integer.parseInt(br.readLine());
            while ((line = br.readLine()) != null) {
                if (line.equals("") || line == null) {
                    br.readLine();
                }
                String l = br.readLine();

                if (line.equals("") || line == null) {
                    charTable.put(l, '\n');
                    continue;

                }

                charTable.put(l, line.charAt(0));
            }

        }
        return decode(file, charTable, count);
    }

    public void decodeFolder(String path) throws FileNotFoundException, IOException {

        boolean f = new File(path + "decoded/").mkdir();
        String res = readbytes(path + "compressedFile.txt");
        File Header = new File(path + "header.txt");
        int count = 0;
        Hashtable<String, Character> charTable = new Hashtable<>();
        try (BufferedReader br = new BufferedReader(new FileReader(Header))) {
            String line;
            int from = 0;
            String name;
            name = br.readLine();
            count = Integer.parseInt(br.readLine());
            while ((line = br.readLine()) != null) {
                if (line.equals("--")) {
                    decodeFile(res, count, from, charTable, name, path);
                    if ((line = br.readLine()) == null) {
                        break;
                    }
                    name = line;
                    from += count;
                    charTable.clear();
                    charTable = new Hashtable<>();
                    count = Integer.parseInt(br.readLine());
                    continue;
                }
                if (line.equals("") || line == null) {
                    br.readLine();
                }
                String l = br.readLine();

                if (line.equals("") || line == null) {
                    charTable.put(l, '\n');
                    continue;

                }

                charTable.put(l, line.charAt(0));
            }

        }
    }

    public void decodeFile(String res, int count, int from, Hashtable<String, Character> charTable, String name,
            String path) throws IOException {
        String decoded = "";
        String accu = "";
        int c = from + count;
        for (int i = from; i < c; i++) {
            accu += res.charAt(i);
            // System.out.println(accu);

            if (charTable.containsKey(accu)) {

                decoded += charTable.get(accu);
                accu = "";
            }
        }

        File f = new File(path + "decoded/" + name);
        FileWriter wr = new FileWriter(f);
        BufferedWriter out = new BufferedWriter(wr);
        out.write(decoded);
        out.close();
        wr.close();

    }

    private String readbytes(String path) throws IOException {
        byte[] array = Files.readAllBytes(new File(path).toPath());
        String res = "";
        for (int i = 0; i < array.length; i++) {
            String s1 = String.format("%8s", Integer.toBinaryString(array[i] & 0xFF)).replace(' ', '0');
            res += s1;
        }
        System.out.println("res:" + res);

        return res;
    }

    public void readMultiHeaderAndBody(String path) {
        try {
            char c;
            byte[] bytes = Files.readAllBytes(new File(path+"compressedFile.txt").toPath());
            int i;
            StringBuilder sb = new StringBuilder("");
            System.out.println("Start decompressing");
            System.out.println("Reading Header....yes");
            ArrayList<Integer> counts=new ArrayList<>();
            OUTER:
            for (i = 0; i < bytes.length; i++) {
                c = (char) bytes[i];
                switch (c) {
                    case END_OF_ONE_HEADER:
                          
                        counts.add(buildCharTable(sb.toString()));
                      
                        sb = new StringBuilder("");
                        break;
                    case END_OF_HEADER:
                        break OUTER;
                    default:
                        sb.append(c);
                        break;
                }
            }
            header = sb.toString();
            counts.add(buildCharTable(header));
           
          
         sb = new StringBuilder("");
            i++;
            System.out.println("Reading Body.....");
            for (int j = i; j < bytes.length; j++) {
                String s1 = String.format("%8s", Integer.toBinaryString(bytes[j] & 0xFF)).replace(' ', '0');
                sb.append(s1);
            }
            body = sb.toString();
   
            System.out.println("Decompressing......");
            DecompressFolder( body,counts,path);

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {

        }
    }

    public void readHeaderAndBody(String compressedFilePath) {
        try {
            char c;
            byte[] bytes = Files.readAllBytes(new File(compressedFilePath).toPath());
            int i;
            StringBuilder sb = new StringBuilder("");
            System.out.println("Start decompressing");
            System.out.println("Reading Header....");
            for (i = 0; i < bytes.length; i++) {
                c = (char) bytes[i];
                if (c == END_OF_HEADER) {

                    break;
                }
                sb.append(c);
            }
            header = sb.toString();
            sb = new StringBuilder("");
            i++;
            System.out.println("Reading Body.....");
            for (int j = i; j < bytes.length; j++) {
                String s1 = String.format("%8s", Integer.toBinaryString(bytes[j] & 0xFF)).replace(' ', '0');
                sb.append(s1);
            }
            body = sb.toString();

            System.out.println("Decompressing......");
            Decompress(header, body);

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {

        }
    }

    public void Decompress(String header, String body) throws IOException {

        Hashtable<String, Character> charTable = new Hashtable<>();
        String lines[] = header.split("\\r?\\n");

        int count = Integer.parseInt(lines[0]);
        for (int i = 1; i < lines.length; i = i + 2) {
            if (!"".equals(lines[i])) {

                charTable.put(lines[i + 1], lines[i].charAt(0));
            } else if ("".equals(lines[i + 1])) {

                charTable.put(lines[i + 2], '\n');
                i++;
            } else {

                charTable.put(lines[i + 1], ' ');
            }
        }

        translateToFile(charTable, count, body);
    }

    private void translateToFile(Hashtable<String, Character> charTable, int count, String body) throws IOException {
        StringBuilder sb = new StringBuilder("");
        StringBuilder sb2 = new StringBuilder("");
        String accu = "";
        String decoded = "";

        for (int i = 0; i < count; i++) {
            sb.append(body.charAt(i));

            if (charTable.containsKey(sb.toString())) {

                sb2.append(charTable.get(sb.toString()));
                sb = new StringBuilder("");
            }
        }
        decoded = sb2.toString();

        File f = new File("decompressedFile.txt");
        FileWriter wr = new FileWriter(f);
        BufferedWriter out = new BufferedWriter(wr);
        out.write(decoded);
        out.close();
        wr.close();
        System.out.println("Decompressed");
    }

    private int buildCharTable(String toString) {
         Hashtable<String, Character> charTable = new Hashtable<>();
        String lines[] = toString.split("\\r?\\n");
        String name=lines[1];
        names.add(name);
        int count = Integer.parseInt(lines[0]);
        for (int i = 2; i < lines.length; i = i + 2) {
            if (!"".equals(lines[i])) {

                charTable.put(lines[i + 1], lines[i].charAt(0));
            } else if ("".equals(lines[i + 1])) {
              
                charTable.put(lines[i + 2], '\n');
                i++;
            } else {
                 
                charTable.put(lines[i + 1], ' ');
            }
        }
        
        headers.add(charTable);
        return count;
    }

    private void DecompressFolder(String body,ArrayList<Integer> counts,String path) throws IOException {
          boolean f = new File(path + "decoded/").mkdir();
          String p= path+"decoded/";
          int last=0;
        for (int j = 0; j < headers.size(); j++) {
            
             Hashtable<String, Character> charTable=headers.get(j);
             int count =counts.get(j);
         
              StringBuilder sb = new StringBuilder("");
        StringBuilder sb2 = new StringBuilder("");
        String accu = "";
        String decoded = "";

int i;
        for ( i = last; i < count+last; i++) {
            sb.append(body.charAt(i));

            if (charTable.containsKey(sb.toString())) {

                sb2.append(charTable.get(sb.toString()));
                sb = new StringBuilder("");
            }
        }
           
        last=i;
        decoded = sb2.toString();

        File file = new File(p+names.get(j));
        FileWriter wr = new FileWriter(file);
        BufferedWriter out = new BufferedWriter(wr);
        out.write(decoded);
        out.close();
        wr.close();
        System.out.println("Decompressed");
            
        }
    }

}
