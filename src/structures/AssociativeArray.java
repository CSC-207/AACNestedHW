package structures;

import static java.lang.reflect.Array.newInstance;

import java.math.BigInteger;

/**
 * A basic implementation of Associative Arrays with keys of type K
 * and values of type V. Associative Arrays store key/value pairs
 * and permit you to look up values by key.
 *
 * @author Albert-Kenneth Okine
 * @author Samuel A. Rebelsky
 */
public class AssociativeArray<K, V> {
  // +-----------+------------------------------------------------------
  // | Constants |
  // +-----------+------------------------------------------------------

  /**
   * The default capacity of the initial array.
   */
  static final int DEFAULT_CAPACITY = 16;

  // +--------+---------------------------------------------------------
  // | Fields |
  // +--------+---------------------------------------------------------

  /**
   * The size of the associative array (the number of key/value pairs).
   */
  int size;

  /**
   * The array of key/value pairs.
   */
  KVPair<K, V> pairs[];

  // +--------------+---------------------------------------------------
  // | Constructors |
  // +--------------+---------------------------------------------------

  /**
   * Create a new, empty associative array.
   */
  @SuppressWarnings({ "unchecked" })
  public AssociativeArray() {
    // Creating new arrays is sometimes a PITN.
    this.pairs = (KVPair<K, V>[]) newInstance((new KVPair<K, V>()).getClass(),
        DEFAULT_CAPACITY);
    this.size = 0;
  } // AssociativeArray()

  // +------------------+-----------------------------------------------
  // | Standard Methods |
  // +------------------+-----------------------------------------------

  /**
   * Create a copy of this AssociativeArray.
   */
  public AssociativeArray<K, V> clone() {
    // Define a new associative array object.
    AssociativeArray<K, V> copy = new AssociativeArray<K, V>();

    // 
    for (int i = 0; i < this.size(); i ++) {
      copy.set(this.pairs[i].key, this.pairs[i].value);
    } // for (each pair in the associative array)

    // 
    return copy;
  } // clone()

  /**
   * Convert the array to a string.
   */
  public String toString() {
    // Define a string to construct the final string with.
    String returnString = "";

    // Iterate throughout the associative array.
    for (int i = 0; i < this.size; i ++) {
      // Update the return string with the new pair's key and value.
      returnString += (i != this.size - 1)
      ? String.format(" %s: %s,", pairs[i].key, pairs[i].value)
      : String.format(" %s: %s ", pairs[i].key, pairs[i].value);
    } // for (each pair in the associative array)
    
    // Return the resulting string from iterating through the array.
    return "{" + returnString + "}";
  } // toString()

  // +----------------+-------------------------------------------------
  // | Public Methods |
  // +----------------+-------------------------------------------------

  /**
   * Set the value associated with key to value. Future calls to
   * get(key) will return value.
   */
  public void set(K key, V value) {
    // Check if the associative array has a key to set
    try {
      // Find and update the key value pair.
      pairs[find(key)].value = value;
    } // if (associative array contains key)

    // 
    catch (KeyNotFoundException e) {
      // Check if the associative array has is full.
      if (this.size >= this.pairs.length) {
        this.expand(); 
      } // if (associative array is full)
        
      // Set a new key value pair at the end of the array.
      pairs[this.size] = new KVPair<K, V>(key, value);
      // Increment and update the size of the array.
      this.size ++;

    } // catch (key not found in associative array)
  } // set(K,V)

  /**
   * Get the value associated with key.
   *
   * @throws KeyNotFoundException
   *   	   when the key does not appear in the associative
   *     array.
   */
  public V get(K key) throws KeyNotFoundException {
    // Check that the key exists in the associative array.
    if (hasKey(key)) {
      // Check if the current pair is the target key.
      return pairs[find(key)].value;
    } // if (associative array contains K key)
  
    // Otherwise, throw a KeyNotFoundException
    else throw new KeyNotFoundException();
  } // get(K)

  /**
   * Determine if key appears in the associative array.
   */
  public boolean hasKey(K key) {
    // Define a boolean showing the existence of the target key.
    boolean containsKey = false;
  
    // Iterate througout the associative array.
    for (int i = 0; i < this.size; i ++) {
      // Check if the current pair is the target key
      if (pairs[i].key.equals(key)) containsKey = true;
    } // for (each pair in the associative array)

    // Return the result of searching the array for K key
    return containsKey;
  } // hasKey(K)

  /**
   * Remove the key/value pair associated with a key. Future calls
   * to get(key) will throw an exception. If the key does not appear
   * in the associative array, does nothing.
   */
  public void remove(K key) {
    // Check if the associative array has a key to remove.
    if (hasKey(key)) {
      try {
        int i = find(key);
        pairs[i] = null;

        // Shift the rest of the key/value pairs to the left.
        for (int j = i; j < this.size - 1; j ++) {
          pairs[j] = pairs[j + 1];
        } // for (the rest of the pairs in the array)
        // Decrement and update the size of the array.
        this.size --;

      } catch (Exception e) { /** DO NOTHING*/ }
    } // if (AssociativeArray contains key)
  } // remove(K)

  /**
   * Determine how many values are in the associative array.
   */
  public int size() {
    return this.size;
  } // size()

  /** 
   * Return an array containing all the keys in the associative array.
   */
  public String[] keyNames() {
    // Create a new array of type K
    String[] keys = new String[this.size];

    for (int i = 0; i < this.size; i ++) {
      keys[i] = pairs[i].key.toString();
    } // for (each pair in the pairs array)

    return keys;
  } // keys()

  // +-----------------+------------------------------------------------
  // | Private Methods |
  // +-----------------+------------------------------------------------

  /**
   * Expand the underlying array.
   */
  public void expand() {
    this.pairs = java.util.Arrays.copyOf(this.pairs, this.pairs.length * 2);
  } // expand()

  /**
   * Find the index of the first entry in `pairs` that contains key.
   * If no such entry is found, throws an exception.
   * 
   * @throws KeyNotFoundException
   *         when the key does not appear in the associative
   *         array.
   */
  public int find(K key) throws KeyNotFoundException {
    // Iterate througout the associative array.
    for (int i = 0; i < this.size; i ++) {
      // Check if the current pair is the target key.
      if (pairs[i].key.equals(key)) return i;
    } // for (each pair in the associative array)

    // Else the key is not in the array, so throw an exception.
    throw new KeyNotFoundException();
  } // find(K)

  public static void main(String[] args) {
    AssociativeArray<String, BigInteger> arr 
      = new AssociativeArray<String, BigInteger>();

    try {
      arr.set("", new BigInteger("1"));
      System.out.println(arr.get("").multiply(new BigInteger("2")));
      
    } catch (Exception e) {
      System.out.println("Error: " + e);
    } // try...catch

  }

} // class AssociativeArray