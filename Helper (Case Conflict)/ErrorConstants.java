package helper;


public final class ErrorConstants {
	  public static final boolean PASSES = true;
	  public static final boolean FAILS = false;
	  
	  public static final int SUCCESS = 1;
	  public static final int FAILURE = 0;

	  public static final int NOT_FOUND = -1;
	  
	  
	  public static final int SAME_NAME = -2;
	  public static final int PRICE_LESS_0 = -3;
	  public static final int SAME_EMAIL = -4;
	  public static final int SAME_ID = -5;
	  public static final int QUANTITY_LESS_0 = 0;

	  private ErrorConstants(){
	    throw new AssertionError();
	  }
}
