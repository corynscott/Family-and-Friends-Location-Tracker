package map.teamc.com.maplocationproject;

/**
 * 		Class for the DrawMenu list of items
 *
 */
public class Model{
	 
	/*
	 * Title of the elment on the side menu
	 */
    private String title;
    
    /*
     * Counter, this is used to check in the constructor if is a header or an
     * 	element
     */
    private String counter;
 
    /*
     * True if the element is a header
     */
    private boolean isGroupHeader = false;
 
    public Model(String title) {
        this(title,null);
        isGroupHeader = true;
    }
    public Model(String title, String counter) {
        super();
        this.title = title;
        this.counter = counter;
    }
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getCounter() {
		return counter;
	}
	public void setCounter(String counter) {
		this.counter = counter;
	}
	public boolean isGroupHeader() {
		return isGroupHeader;
	}
	public void setGroupHeader(boolean isGroupHeader) {
		this.isGroupHeader = isGroupHeader;
	}

    
}
