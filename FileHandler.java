import java.util.Scanner;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

interface FileOperation {
    // Method to create a file
    void createFile() throws IOException;
    
    // Method to read from a file
    void readFromFile(String sCh);
    
    // Method to write to a file
    void writeToFile(String sCh) throws IOException;
    
    // Method to delete a file
    void deleteFile();
    
    // Method to delete a directory
    void deleteDirectory();
}

// Implement the FileOperation interface in FileHandler class
public class FileHandler implements FileOperation {
    // Method to create a file
    public void createFile() throws IOException {
        try {
            String path;
            System.out.print("Enter File Path Where To Be Saved: ");
            Scanner inPath = new Scanner(System.in);
            path = inPath.nextLine();

            File f1 = new File(path);
            if (f1.createNewFile()) {
                System.out.println("File is created...");
            } else {
                System.out.println("File already exists...");
            }
        } catch (IOException e) {
            System.out.println("Error occurred...");
            e.printStackTrace();
            throw e;
        }
    }

    // Method to read from a file
    public void readFromFile(String sCh) {
        try {
            System.out.print("Enter File Path To Be Read: ");
            Scanner inpt = new Scanner(System.in);
            String path = inpt.nextLine();
            File file = new File(path);

            if (sCh.equals("a")) {
                // Read the file using Scanner
                Scanner reader = new Scanner(file);
                while (reader.hasNextLine()) {
                    System.out.println(reader.nextLine());
                }
                reader.close();
                System.out.println("File is successfully read using Scanner...");
            } else if (sCh.equals("b")) {
                // Read the file using ByteStream Reader
                FileInputStream fis = new FileInputStream(file);
                BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
                reader.close();
                System.out.println("File is successfully read using ByteStream Reader...");
            } else if (sCh.equals("c")) {
                // Read the file using CharacterStream Reader
                FileReader fileReader = new FileReader(file);
                BufferedReader reader = new BufferedReader(fileReader);
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
                reader.close();
                System.out.println("File is successfully read using CharacterStream Reader...");
            } else {
                System.out.println("Invalid sub-choice. Please try again.");
            }
        } catch (FileNotFoundException e) {
            System.out.println("File Doesn't Exist...");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error Occurred...");
            e.printStackTrace();
        }
    }

    // Method to write to a file
    public void writeToFile(String sCh) throws IOException {
        try {
            String path, content;
            System.out.print("Enter File Path To Be Written: ");
            Scanner inpt = new Scanner(System.in);
            path = inpt.nextLine();

            System.out.print("Enter Content To Be Written: ");
            content = inpt.nextLine();

            if (sCh.equals("a")) {
                // Write to the file using FileWriter
                FileWriter writer = new FileWriter(path);
                writer.write(content);
                writer.close();
                System.out.println("Content is successfully written to the File using FileWriter...");
            } else if (sCh.equals("b")) {
                // Write to the file using ByteStream Writer
                FileOutputStream fos = new FileOutputStream(path);
                OutputStreamWriter osw = new OutputStreamWriter(fos);
                BufferedWriter writer = new BufferedWriter(osw);
                writer.write(content);
                writer.close();
                System.out.println("Content is successfully written to the File using ByteStream Writer...");
            } else if (sCh.equals("c")) {
                // Write to the file using CharacterStream Writer
                FileOutputStream fos = new FileOutputStream(path);
                Writer writer = new OutputStreamWriter(fos, "UTF-8");
                writer.write(content);
                writer.close();
                System.out.println("Content is successfully written to the File using CharacterStream Writer...");
            } else {
                System.out.println("Invalid sub-choice. Please try again.");
            }
        } catch (IOException e) {
            System.out.println("Error Occurred...");
            e.printStackTrace();
            throw e;
        }
    }

    // Method to delete a file
    public void deleteFile() {
        try {
            Scanner inpt = new Scanner(System.in);
            System.out.print("Enter File Path To Be Deleted: ");
            String path = inpt.nextLine();
            File f1 = new File(path);

            if (f1.delete()) {
                System.out.println("File is Successfully Deleted...");
            } else {
                System.out.println("Error Occurred...");
            }
        } catch (Exception e) {
            System.out.println("Exception occurred...");
            e.printStackTrace();
        }
    }

    // Method to delete a directory
    public void deleteDirectory() {
        Scanner inpt = new Scanner(System.in);
        System.out.print("Enter Directory Path To Be Deleted: ");
        String dirPath = inpt.nextLine();
        File dir = new File(dirPath);

        deleteRecursive(dir);

        System.out.println("Directory is Successfully Deleted...");
    }

    // Recursive method to delete files and directories inside a directory
    private void deleteRecursive(File file) {
        if (file.isDirectory()) {
            for (File subFile : file.listFiles()) {
                deleteRecursive(subFile);
            }
        }
        file.delete();
    }

    // Main method
    public static void main(String[] args) {
        Scanner inpt = new Scanner(System.in);
        FileOperation ob;
        ob = new FileHandler();

        while (true) {
            System.out.println("\nEnter choice: ");
            System.out.println("1. Create File\n"+
            "2. Read From File\n\t" +
            "a) Using Scanner\n\t" +
            "b) Using Output ByteStream \n\t" +
            "c) Using Output CharStream\n" + 
            "3. Write To File\n\t" +
            "a) Using FileWriter\n\t" +
            "b) Using Input ByteStream\n\t" +
            "c) Using Input CharStream\n" +
            "4. Delete File \n" +
            "5. Delete Directory\n" +
            "6. Exit");
            System.out.print("Input: ");

            int ch = Integer.parseInt(inpt.nextLine());

            try {
                if (ch == 1)
                    ob.createFile();
                else if (ch == 2){
                    System.out.print("Enter Sub-Choice (a, b, c): ");
                    String subCh = inpt.nextLine();
                    ob.readFromFile(subCh);
                }
                else if (ch == 3){
                    System.out.print("Enter Sub-Choice (a, b, c): ");
                    String subCh = inpt.nextLine();
                    ob.writeToFile(subCh);
                }
                else if (ch == 4)
                    ob.deleteFile();
                else if (ch == 5)
                    ob.deleteDirectory();
                else
                    System.exit(0);
            } catch (IOException e) {
                System.out.println("IO Error Occurred...");
                e.printStackTrace();
            }
        }
    }
}
