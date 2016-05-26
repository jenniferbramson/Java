import java.io.*;

public class Assig4 {

  public static void main(String[] args) throws IOException{
    // If a maze file was passed in, create a Maze object and find the possible
    // solutions using findPaths(). Passing in BufferedReader causes each
    // maze object created to copy the reference, and thereby share the same
    // BufferedReader. This allows the reader to continue reading from the last
    // point rather than restarting from the top of the file each time.
    if (args.length != 0){
      try {
        String filename = args[0];
        BufferedReader br = new BufferedReader(new FileReader(filename));

        // br.ready() is true if there are unread chars left in the file
        // This will err if there are empty lines at the end of the file.
        while(br.ready()){
          System.out.println("-----------------------------------------");
          new Maze(filename, br).findPaths();
        }

        br.close();
      } catch(IOException e) {
        System.out.println("File error. Does not exist or does not contain a maze.");
      }
    } else {
      System.out.println("Maze file missing");
    }

  }

}
