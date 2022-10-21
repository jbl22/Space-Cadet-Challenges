import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {
  /**
   * List containing the initial code from the file in the
   * form of arrays.
   */
  ArrayList<String[]> code = new ArrayList<>();
  /**
   * Hashmap containing the variables used in the code file.
   * Variable name is the key and variable value is the value.
   */
  HashMap<String, Integer> variables = new HashMap<>();

  public static void main(String[] args) {
    Main main = new Main();

    // Validate that the file exists.
    if (!(main.readFile())) {
      System.out.println("No valid code file found.");
    }
    main.parseLine(main.code);
    System.out.println(main.variables);
  }

  /**
   * Takes a list of lines from the code file and executes each
   * instruction on each line individually.
   * @param lines List of arrays of strings containing the instructions
   *              and variables from each line of the code file.
   */
  private void parseLine(List<String[]> lines) {
    int lineCounter = 0;
    // Remove "end"s when passed from the while loop method.
    if (lines.get(0)[0].equals("end;") || lines.get(0)[0].equals("3end;")) {
      lines.remove(0);
    }

    mainLoop:
    for (String[] line : lines) {
      for (int j = 0; j < line.length; j++) {
        switch (line[j]) {
          case "clear" -> {
            clear(line[j + 1]);
            lineCounter++;
          }
          case "incr" -> {
            incr(line[j + 1]);
            lineCounter++;
          }
          case "decr" -> {
            decr(line[j + 1]);
            lineCounter++;
          }
          case "while", "3while" -> {
            break mainLoop;
          }
        }
      }
    }

    /*
    If the list contains a while loop the number of lines
    executed will not be the same as the size of the list.
     */
    if (lineCounter != lines.size()) {
      int endPos = 0;

      if (lines.get(lineCounter)[0].equals("while")) {
        for (int i = 0; i < lines.size(); i++) {
          if (lines.get(i)[0].equals("end;")) {
            endPos = i;
            break;
          }
        }
      } else if (lines.get(lineCounter)[0].equals("3while")) {
        for (int i = 0; i < lines.size(); i++) {
          if (lines.get(i)[0].equals("3end;")) {
            endPos = i;
            break;
          }
        }
      }

      List<String[]> whileLines = new ArrayList<>(lines.subList(lineCounter, endPos));
      lines.subList(0, endPos).clear();
      whLoop(whileLines, whileLines.get(0)[1], Integer.parseInt(whileLines.get(0)[3]));
    }
  }

  /**
   * Increments an argument variable by 1 and puts
   * it in the hashtable variables.
   * @param variable Variable to increment.
   */
  private void incr(String variable) {
    String varName = "" + variable.charAt(0);

    if (!(this.variables.containsKey(varName))) {
      this.variables.put(varName, 1);
    } else {
      this.variables.put(varName, this.variables.get(varName) + 1);
    }
  }

  /**
   * Checks a certain condition and will complete a while loop
   * depending on whether it is true.
   * @param lines List of string arrays containing instructions to
   *              be executed within a specific white loop.
   * @param variable Variable for comparison in while loop condition.
   * @param condition Condition to compare to variable.
   */
  private void whLoop(List<String[]> lines, String variable, Integer condition) {
    lines.remove(0);

    while (!Objects.equals(this.variables.get(variable), condition)) {
      this.parseLine(lines);
    }
  }

  /**
   * Decrements an argument variable by 1 if
   * it's not 0 and puts it in the hashtable variables.
   * @param variable Variable to decrement.
   */
  private void decr(String variable) {
    String varName = "" + variable.charAt(0);

    if (!(this.variables.containsKey(varName))) {
      this.variables.put(varName, 0);
    } else if (this.variables.get(varName) > 0) {
      this.variables.put(varName, this.variables.get(varName) - 1);
    }
  }

  /**
   * Sets a variables value to 0.
   * @param variable Variable to clear.
   */
  private void clear(String variable) {
    String varName = "" + variable.charAt(0);

    this.variables.put(varName, 0);
  }

  private boolean readFile() {
    Scanner scan;
    try {
      scan = new Scanner(new File("read.txt"));
    } catch (FileNotFoundException e) {
      return false;
    }
    while (scan.hasNextLine()) {
      String currentLine = scan.nextLine();
      // Find the number of leading whitespaces for whiles and ends.
      if (currentLine.length() - currentLine.trim().length() == 3 && (currentLine.startsWith("   while") || currentLine.startsWith("   end;"))) {
        currentLine = currentLine.trim();
        currentLine = "3" + currentLine;

        this.code.add(currentLine.trim().split(" "));
      } else {
        this.code.add(currentLine.trim().split(" "));
      }
    }
    return true;
  }
}