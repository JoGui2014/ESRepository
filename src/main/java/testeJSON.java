import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;


public class testeJSON extends JFrame{
    private static final long serialVersionUID = 1L;
    private static File CSVFile;
    private static BufferedReader read;
    private static BufferedWriter write;

    public testeJSON() {
        FileNameExtensionFilter filter = new FileNameExtensionFilter("comma separated values", "csv");
        JFileChooser choice = new JFileChooser();
        choice.setFileFilter(filter); //limit the files displayed

        // add option to open from URL
        String[] options = {"Local file", "URL"};
        int urlOption = JOptionPane.showOptionDialog(this,
                "Do you want to open a local file or provide a URL?",
                "Open file", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options, options[0]);

        if (urlOption == 1) { // URL option
            String urlStr = JOptionPane.showInputDialog(this,
                    "Enter the URL of the CSV file:");
            try {
                URL url = new URL(urlStr);
                convert(url.openStream());
            } catch (MalformedURLException e) {
                JOptionPane.showMessageDialog(this, "Invalid URL. Program will exit.",
                        "System Dialog", JOptionPane.PLAIN_MESSAGE);
                System.exit(1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else { // local file option
            int option = choice.showOpenDialog(this);
            if (option == JFileChooser.APPROVE_OPTION) {
                File CSVFile = choice.getSelectedFile();
                try {
                    convert(new FileInputStream(CSVFile));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Did not select file. Program will exit.",
                        "System Dialog", JOptionPane.PLAIN_MESSAGE);
                System.exit(1);
            }
        }
    }


    public static void main(String args[]){
        testeJSON parse = new testeJSON();
        System.exit(0);
    }

    private static void convert(InputStream input) {
        /* Converts a .csv file to .json. Assumes first line is header with columns */
        try {
            read = new BufferedReader(new InputStreamReader(input));

            String outputName = CSVFile.toString().substring(0,
                    CSVFile.toString().lastIndexOf(".")) + ".json";
            write = new BufferedWriter(new FileWriter(new File(outputName)));

            String line;
            String columns[]; //contains column names
            int num_cols;
            String tokens[];

            int progress = 0; //check progress

            //initialize columns
            line = read.readLine();
            columns = line.split(",");
            num_cols = columns.length;


            write.write("["); //begin file as array
            line = read.readLine();


            while (true) {
                tokens = line.split(",");

                if (tokens.length == num_cols) { // if number columns equal to number entries
                    write.write("{");

                    for (int k = 0; k < num_cols; ++k) { // for each column
                        if (tokens[k].matches("^-?[0-9]*\\.?[0-9]*$")) { // if a number
                            write.write("\"" + columns[k] + "\": " + tokens[k]);
                            if (k < num_cols - 1) write.write(", ");
                        } else { // if a string
                            write.write("\"" + columns[k] + "\": \"" + tokens[k] + "\"");
                            if (k < num_cols - 1) write.write(", ");
                        }
                    }

                    ++progress; // progress update
                    if (progress % 10000 == 0) System.out.println(progress); // print progress

                    if((line = read.readLine()) != null){//if not last line
                        write.write("},");
                        write.newLine();
                    }
                    else{
                        write.write("}]");//if last line
                        write.newLine();
                        break;
                    }
                }
                else{

                    System.exit(-1); //error message
                }
            }

            write.close();
            read.close();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}





