import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.BufferedReader;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NameFinder {
  private URL url;
  private String name;

  public static void main(String[] args) throws IOException {
    String name;
    NameFinder formatData = new NameFinder();
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    System.out.print("Enter the email code of the person you want to find: ");
    while (true) {
      formatData.setName(reader.readLine());
      formatData.setAddress("https://www.ecs.soton.ac.uk/people/" + formatData.getName());
      name = formatData.nameFromUrl(formatData.getUrl());
      if (name.equals("")) {
        System.out.print("Value entered did not return a valid name, re-enter: ");
      } else {
        break;
      }
    }

    int start = 0;
    int end = 0;
    for (int i = name.length() - 1; i >= 0; i--) {
      if (name.charAt(i) == ('"') && end != 0) {
        start = i + 1;
        break;
      }
      if (name.charAt(i) == ('"') && end == 0) {
        end = i;
      }
    }
    System.out.println("The name corresponding with that email code is: " + name.substring(start, end));
  }

  /**
   * @param url The URL of the page containing the email
   *            code's corresponding name.
   * @return String name corresponding to the email code. Returns ""
   * if no name can be found on the page.
   */
  public String nameFromUrl(URL url) throws IOException {
    String pattern = " *\"name\": \"[a-zA-Z]++ ([a-zA-Z]|-)++\"";
    String failPattern = "http://www.ecs.soton.ac.uk/people/[a-zA-Z0-9]++";
    Pattern nameRegex = Pattern.compile(pattern);
    Pattern failRegex = Pattern.compile(failPattern);

    BufferedReader urlReader = new BufferedReader(new InputStreamReader(url.openStream()));

    Object[] pageArr = (urlReader.lines()).toArray(String[]::new);
    String page = Arrays.toString(pageArr);

    Matcher nameMatcher = nameRegex.matcher(page);
    Matcher failMatcher = failRegex.matcher(page);
    boolean found = nameMatcher.find();
    boolean failed = failMatcher.find();

    if (found) {
      return (page.substring(nameMatcher.start(), nameMatcher.end()));
    } else {
      if (failed) {
        return nameFromUrl(new URL((page.substring(failMatcher.start(), failMatcher.end()))));
      }
      return "";
    }
  }

  public void setAddress(String url) throws MalformedURLException {
    this.url = new URL(url);
  }

  public String getName() {
    return name;
  }

  public URL getUrl() {
    return this.url;
  }

  public void setName(String name) {
    this.name = name;
  }
}
