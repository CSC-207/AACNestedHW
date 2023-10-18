import structures.AssociativeArray;

/**
 * Represents a single category of items in the AAC, while 
 * storing the mapping between the image location and the text
 * that should be spoken and the name of the category.
 * 
 * @author Albert-Kenneth Okine
 */
public class AACCategory extends java.lang.Object {

  // +--------+---------------------------------------------------------
  // | Fields |
  // +--------+---------------------------------------------------------

  /** Mappings of image locations to their corresponding words */
  AssociativeArray<String, String> words;

  /** The name of the category. */
  String name;

  // +--------------+---------------------------------------------------
  // | Constructors |
  // +--------------+---------------------------------------------------

  /** 
   * Creates a new empty category with the given name.
   */
  public AACCategory(String name) {
    this.words = new AssociativeArray<String, String>();
    this.name = name;
	} // AACCategory(String)

  // +----------------+-------------------------------------------------
  // | Public Methods |
  // +----------------+-------------------------------------------------

  /** 
   * Adds the mapping of the imageLoc to the text to the category.
   */
	public void addItem (String imageLoc, String text) {
    this.words.set(imageLoc, text);
	} // addItem(String, String)

  /** 
   * Returns the name of the category.
   */
  public String getCategory() {
    return this.name;
  } // getCategory

  /** 
   * Returns the text associated with the given `imageLoc` in this
   * category.
   */
  public String getText(String imageLoc) {
    try { // Get the text from the associative array.
      return this.words.get(imageLoc);
    } catch (Exception e) { // Report an error message to the console.
      System.out.println("Error in getting text for " + imageLoc);
      return "";            // Return an empty string.
    } // try...catch
  } // getText(String)

  /** 
   * Determines if the provided image is stored in the category.
   */
  public boolean hasImage(String imageLoc) {
    return this.words.hasKey(imageLoc);
  } // hasImage(String)

  /** 
   * Returns an array of all the images in the category.
   */
  public String[] getImages() {
    return this.words.keyNames();
  } // getImages()

} // class AACCategory