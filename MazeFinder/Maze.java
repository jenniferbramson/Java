/*
Throughout this class, 'x' and 'y' correspond to what they would on
a graph, horizontal and vertical directions respectively. When they
are printed out, however, the y cordinate comes first though, as was
required to meet the homework requirements.
*/

import java.io.*;

public class Maze {
  private static BufferedReader br;
  private int shortestPath;
  private char[][] maze;
  private int numX = 0;
  private int numY = 0;
  private int startingX;
  private int startingY;
  private char[][] shortestSolution;
  private String[] shortestSS;

  public Maze(String input, BufferedReader br) {
    // if input file is valid, create a file stream and initialize maze
    try {
      this.br = br;
      String[] line = br.readLine().split(" ");
      numX = Integer.parseInt(line[1]); // number of x = y
      numY = Integer.parseInt(line[0]); // number of y = x
      maze = new char[numX][numY];

      String[] startingPoint = br.readLine().split(" ");
      startingX = Integer.parseInt(startingPoint[1]);
      startingY = Integer.parseInt(startingPoint[0]);

      initializeMaze();

      System.out.println("\nThe new board is: ");
      System.out.println(this);
    } catch (Exception e) {
      System.out.println("File error. File not formatted correctly.");
    }
  }

  // A helper method that adds the characters in the file to the maze
  private void initializeMaze() throws IOException {
    for (int y = 0; y < numY; y++) {
      String[] line = br.readLine().split(" ");
      for (int x = 0; x < numX; x++) {
        // .charAt(0) just transforms the String into a char
        maze[x][y] = line[x].charAt(0);
      }
    }
  }

  // Makes a copy of the maze, putting it in the shortestSolution array
  private void copySolution() {
    for (int y = 0; y < numY; y++) {
      for (int x = 0; x < numX; x++) {
        // Since char is primitive, don't need to worry about reference sharing
        shortestSolution[x][y] = maze[x][y];
      }
    }
  }


  // Finds the paths from the starting point recursively by using nextSpot()
  // recursively and prints out any solutions within nextSpot() then a final
  // summary in this method after nextSpot() completes
  public void findPaths() {
    System.out.println("\nSearching for solutions starting at (" + startingY
    + "," + startingX + ")\n");

    // Initialize the shortest solution holder
    shortestSolution = new char[numX][numY];

    // Solution set is numX*numY because that's the most that could be in it
    // numCalls starts at 1 because this is the first call here
    // nextSpot(starting x, starting y, number of points in the path, saved
    //          shortest solution points array, index for that array, number
    //          of calls);
    long numCalls = nextSpot(startingX, startingY, 0, new String[numX*numY], 0, 1);

    System.out.println("There were a total of " + numSolutions + " solutions found");
    System.out.println("A total of " + numCalls +  " recursive calls were made");
    // If a solution is found, print it out
    if (shortestPath != 0) {
      System.out.println("The shortest solution had " + shortestPath + " segments");
      System.out.println(ssString(shortestSolution));
      System.out.print("Path: ");
      for (int i = 0; i < shortestSS.length; i++) {
        System.out.print(shortestSS[i] + " ");
      }
    }
    System.out.println("\n");
  }


  // Helper method for finding solutions to the maze
  // ss = solution set
  private int numSolutions = 0;
  private long nextSpot(int x, int y, int pathNum, String[] ss, int ssIndex, long numCalls) {
    // Check current character for end of maze (2) or open space (0)
    if (maze[x][y] == '0') {
      // the space is valid to move through, so change to an x
      maze[x][y] = 'x';

      pathNum++; // increment the number of points in the current path
      ss[ssIndex] = "(" + y + "," + x + ")";
      ssIndex++;

      // Move on to the next space with a recursive call:
      // If a spot is invalid (or reaches the maze end), the recursive
      // call completes, causing it to attempt a new recursive call
      // in a new direction
      // Check appropriate boundary conditions for each one to save recurses
      if (y+1 < numY)
        numCalls = nextSpot(x, y+1, pathNum, ss, ssIndex, numCalls+1); // down
      if (x+1 < numX)
        numCalls = nextSpot(x+1, y, pathNum, ss, ssIndex, numCalls+1); // right
      if (y-1 > -1)
        numCalls = nextSpot(x, y-1, pathNum, ss, ssIndex, numCalls+1); // up
      if (x-1 > -1)
        numCalls = nextSpot(x-1, y, pathNum, ss, ssIndex, numCalls+1); // left

      // If none of the directions are possible, backtrack.
      // This happens via the activation record. When it hits the end of
      // the method, it causes the previous step in the AR
      // to try the next direction. First we must change the spot back to a
      // '0' though
      maze[x][y] = '0';
      pathNum--;  // decrement the number of points in the current path
      ssIndex--;  // decrement the solution set index so the current spot
                  // will be overwritten
    } else if (maze[x][y] == '2') {
      // Found the exit
      pathNum++; // this is for the exit spot
      System.out.println("Solution found with " + pathNum + " segments");
      numSolutions++;

      // Add the current point to the solution set coordinates
      ss[ssIndex] = "(" + y + "," + x + ")";

      // If it is the first solution found, set it as the shortest solution
      // Otherwise, see if it is the shortest solution found
      if (shortestPath == 0) {
        shortestPath = pathNum;
        // Save this solution
        copySolution();
        shortestSS = new String[ssIndex+1];
        for (int i = 0; i <= ssIndex; i++) {
          shortestSS[i] = new String(ss[i]);
        }
      } else if (pathNum < shortestPath){
        shortestPath = pathNum;
        // Save this solution
        copySolution();
        shortestSS = new String[ssIndex+1];
        for (int i = 0; i <= ssIndex; i++) {
          shortestSS[i] = new String(ss[i]);
        }
      }

      // Print out the image of it
      System.out.println(this);
      // Print out the solution set
      System.out.print("Path: ");
      // <= because you want it to print out the point at ssIndex
      for (int i = 0; i <= ssIndex; i++) {
        System.out.print(ss[i] + " ");
      }
      System.out.println("\n");

      // find the next solution by returning
    } else {
      // The char is an 'x', so have already been here
      // or the char is a '1', so a wall
      // Try next row/column instead
    }

    return numCalls;
  }

  // returns the maze is layout form as a string
  public String toString() {
    // numX*numY = total characters in array, *2 to add a space between each
    // + numY to add space for a new line char after each y, except last
    char[] toReturn = new char[numX*numY*2 + numY - 1];
    int index = 0;
    for (int y = 0; y < numY; y++) {
      for (int x = 0; x < numX; x++) {
        toReturn[index] = maze[x][y];
        index++;
        toReturn[index] = ' ';
        index++;
      }
      // Add a new line character unless it is the last line
      if (y != numY - 1) {
        toReturn[index] = '\n';
      }
      index++;
    }

    return (new String(toReturn));
  }

  // returns the maze in layout form as a string
  // it's the same as the toString() method, but for the shortest solution
  public String ssString(char[][] m) {
    // numX*numY = total characters in array, *2 to add a space between each
    // + numY to add space for a new line char after each y, except last
    char[] toReturn = new char[numX*numY*2 + numY - 1];
    int index = 0;
    for (int y = 0; y < numY; y++) {
      for (int x = 0; x < numX; x++) {
        toReturn[index] = m[x][y];
        index++;
        toReturn[index] = ' ';
        index++;
      }
      // Add a new line character unless it is the last line
      if (y != numY - 1) {
        toReturn[index] = '\n';
      }
      index++;
    }

    return (new String(toReturn));
  }

}
