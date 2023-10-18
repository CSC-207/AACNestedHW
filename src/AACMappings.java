import java.io.File;
import java.io.FileWriter;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

import structures.AssociativeArray;

/**
 * Keeps track of the complete set of AAC mappings. Stores the 
 * mappings of the images on the home page to the AACategories.
 * 
 * @author Albert-Kenneth Okine
 */
public class AACMappings extends java.lang.Object {

  /** Mappings of either file names or names to categories */
  AssociativeArray<String, AACCategory> categories;

  /** The current category. */
  String curCategory = "";
  /** The default category. */
  final String DEFAULT = "";

  // +--------------+---------------------------------------------------
  // | Constructors |
  // +--------------+---------------------------------------------------

  /**
   * Read in the given filename, create the relevant mappings from images
   * to categories and add all items to each category.
   */
  public AACMappings(String filename) {
    // Create a new associative array between categories and filenames.
    this.categories = new AssociativeArray<String, AACCategory>();
    // Create a category representing all categories
    this.categories.set(this.DEFAULT, new AACCategory(this.DEFAULT));

    try { // Attempt to read in from the input filename.
      Scanner inputScanner = new Scanner(new File(filename));
      // Iteratively read each line from the input file.
      while (inputScanner.hasNextLine()) {
        // Read in the next line from the input file.
        String line = inputScanner.nextLine();

        // Split the string and assign image location and category name.
        int n = line.indexOf(" ");
        String imageLoc = line.substring(0, n);
        String category = line.substring(n + 1);
        
        // Check if the line is a category or an item to add.
        if (imageLoc.startsWith(">")) { // Add item.
          this.add(imageLoc.substring(1), category);
        
        } else { // Add a new mapping to the associative array.
          this.categories.get(this.DEFAULT).addItem(imageLoc, category);
          // Create a new empty AACCategory.
          this.categories.set(category, new AACCategory(category));
          // Update the current category.
          this.curCategory = category;
        } // if...else
        
      } // while (file has next line)
      this.reset(); // Reset the current category to the home page.

    } catch (Exception e) { // Report an error to the console.
      System.out.println("Error in constructing AAC from " + filename);
    } // try...catch
  } // AACMappings(String)

  // +----------------+-------------------------------------------------
  // | Public Methods |
  // +----------------+-------------------------------------------------

  /**
   * Provides an array of all the images in the current category.
   */
  public String[] getImageLocs() {
    try { // Attempt to get the images in the category
      return this.categories.get(this.curCategory).getImages();
    } catch (Exception e) {   // Report an error to the console.
      System.out.println("Error in getting image locations.");
      return new String[] {}; // return an empty list of strings.
    } // try...catch
  } // getImageLocs()

  /**
   * Given the image location selected, determines the associated
   * text with the image. 
   * 
   * @implNote If the image provided is a category, also updates
   *           the AAC's current category to be the category
   *           associated with that image.
   */
  public String getText(String imageLoc) {
    try { // Attempt to get the text from the associated array.
      // Check if the image is a category, then update the current
      // category to that category if so.
      if (this.isCategory(imageLoc)) {
        this.curCategory = this.categories.get(this.DEFAULT).getText(imageLoc);
        return (this.categories.get(this.DEFAULT).getText(imageLoc));
      } else {
        // Find the associated text of the image, then return it.
        return (this.categories.get(this.curCategory).getText(imageLoc));
      }

    } catch (Exception e) { // Report an error to the console.
      System.out.println("Error in getting text for " + imageLoc);
      throw new NoSuchElementException("Image not found.");
    } // try...catch
  } // getText(String)

  /**
   * Resets the current category of the AAC back to the default 
   * category.
   */ 
  public void reset() {
    this.curCategory = this.DEFAULT;
  } // reset()

  /**
   * Returns the current category (or the empty string if on the
   * default category).
   */
  public String getCurrentCategory() {
    return this.curCategory;
  } // getCurrentCategory()

  /**
   * Determines if the image represents a category or text to speak.
   */
  public boolean isCategory(String imageLoc) {
    try { // Attempt to find the image in the associated array.
      return this.categories.get(this.DEFAULT).hasImage(imageLoc);
    } catch (Exception e) { // Write an error message to the console.
      System.out.println("Error in checking if category for " + imageLoc);
      return false;         // Return false.
    } // try...catch
  } // isCategory(String)

  /**
   * Writes the mappings stored to a file. 
   * 
   * @implNote The file is formatted as the text location of the
   *           categeory that starts with `>` and then has the file
   *           name and the text of the image.
   */
  public void writeToFile(String filename) {
    try { // Create a new file instance to the given filename.
      FileWriter output = new FileWriter(filename);
      
      this.reset(); // Reset to the home directory before starting.

      // Iteratively write each category and its contents.
      for (String categoryLoc : this.getImageLocs()) { 
        System.out.println(categoryLoc);
        this.reset();
        output.write(this.formattedString(categoryLoc));
        // Iterate through each item in the category.
        for (String itemLoc : this.getImageLocs()) { 
          System.out.println(itemLoc);
          output.write(this.formattedString(itemLoc));
        } // for (each image location in the category)
      } // for (each category in the home directory)
      output.close(); // Close the FileWriter.

    } catch (Exception e) { // Write an error message to the console.
      System.out.println("Error in writing to file " + filename);
    } // try...catch

    this.reset(); // Reset to the home directory when done.

  } // writeToFile(String)

  /** 
   * Adds the mapping to the current category (or the default
   * category if that is the current category).
   */
  public void add(String imageLoc, String text) {
    try { // Add the mappings, then update the associative array.
      this.categories.get(this.curCategory).addItem(imageLoc, text);
    } catch (Exception e) { // Report an error message to the console.
      System.out.println("Error in adding " + imageLoc + ": " + text);
    } // try...catch
  } // add(String, String)

  // +-----------------+------------------------------------------------
  // | Private Methods |
  // +-----------------+------------------------------------------------

  /**
   * Returns the appropriate AACMapping, formatted by whether it is a
   * category or an item within a category.
   */
  private String formattedString(String imageLoc) {
    // Format the string according to image location and text.
    return "%s %s\n".formatted(
      (this.isCategory(imageLoc)) ? imageLoc : ('>' + imageLoc),
      (this.getText(imageLoc)));
  } // writeString(String)

} // class AACMappings