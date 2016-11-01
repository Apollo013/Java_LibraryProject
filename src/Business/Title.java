package Business;

/**
 * @date 14/10/2013
 * @author Ehi Omorede & Validation By Paul Millar
 */

import java.io.Serializable;

    /*--------------------------*/
    // **** BUSINESS RULES **** //
    /*--------------------------*/
    // (1) Titles can only be loaned out or returned, one at a time.
    // (2) Quantity on loan cannot exceed total stock.
    // (3) Total Stock level cannot go below zero or below quantity on loan.
    // (4) Loan period cannot exceed MAXIMUM LOAN PERIOD in days.

public class Title extends ErrorBase implements Serializable{
    
    private String isbn;
    private String name;
    private String location;
    private int copies;
    private int quantityOnLoan;
    private byte loanPeriod;
    private String titleAuthors;
    
    public static final byte DEFAULTLOANPERIOD = 14;
    public static final byte MAXLOANPERIOD = 28;
    public static final String HEADER = String.format("%-13s\t%-60s\t%-8s%4s\t%-4s\t%-4s\t%-3s\t%-60s",
                                                        "ISBN","Title","Loc.","Stk","Loaned","Avail.","Days","Authors");
    
    /**
     * Default Constructor
     */
    public Title(){
        this.isbn ="";
        this.name ="";
        this.location = "";
        this.copies = 0;
        this.quantityOnLoan = 0;
        this.loanPeriod = DEFAULTLOANPERIOD; 
        this.titleAuthors = "";
    }

    /**
     * Constructor that takes parameters for all properties.
     * @param isbn
     * @param name
     * @param location
     * @param copies
     * @param quantityOnLoan
     * @param loanPeriod
     */
    public Title(String isbn,String name,String location,int totalStock,int quantityOnLoan,byte loanPeriod, String titleAuthors){
        this.isbn = isbn;
        this.name = name;
        this.location = location;
        this.copies = totalStock;
        this.quantityOnLoan = quantityOnLoan;
        this.loanPeriod = loanPeriod;
        this.titleAuthors = titleAuthors;
    }

    /**
     *
     * @return a 13 character long value representing the International Standard Book Number for this title.
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     *
     * @return The name of the title.
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return The location within the library where this title is stored.
     */
    public String getLocation() {
        return location;
    }

    /**
     *
     * @return The total stock for this title.
     */
    public int getCopies() {
        return copies;
    }

    /**
     *
     * @return The total quantity of copies of this title that are currently on loan.
     */
    public int getQuantityOnLoan() {
        return quantityOnLoan;
    }

    /**
     * This is length of time that this title can be loaned out for. It is measured in days. Typical values would be 7, 14 or 21.
     * @return The length of time that this title can be loaned out for.
     */
    public byte getLoanPeriod() {
        return loanPeriod;
    }

    /**
     * Gets related authors for this title.
     * @return
     */
    public String getTitleAuthors() {
        return titleAuthors;
    }
        
    /**
     * Sets the 13 character long value representing this titles International Standard Book Title.
     * @param isbn the value for this titles International Standard Book Title.
     */
    public void setIsbn(String isbn) {
        // Not going to do proper validation for isbn
        // just make sure it is less than or equal 13 characters.
        if(!this.isbn.equals(isbn)){
            this.isbn = isbn;
            if(this.isbn.length() <= 0 || this.isbn.length() > 13){
                this.addError("ISBN", "ISBN length must be between 1 and 13 characters long");
            } else {
                this.removeError("ISBN");
            }
        }
    }

    /**
     * Sets the name for this title.
     * @param name the name for this title.
     */
    public void setName(String name) {
        // Validate that something was specified
        if(!this.name.equals(name)){
            this.name = name; 
            if(this.name.isEmpty()){
                this.addError("NAME", "You must specify a name");
            } else {
                this.removeError("NAME");
            }            
        }        
    }

    /**
     * Sets the location within the library for this title.
     * @param location the location within the library for this title.
     */
    public void setLocation(String location) {
        // Validate that something was specified
        if(!this.location.equals(location)){
            this.location = location; 
            if(this.location.isEmpty()){
                this.addError("LOCATION", "You must specify a location please");
            } else {
                this.removeError("LOCATION");
            }            
        }  
    }

    /**
     * Sets the total stock (copies) value for this title.
     * @param copies the total stock quantity for this title
     * @return true if the quantity value was changed successfully, false otherwise.
     */
    public void setCopies(int copies){
        if(this.copies != copies){
            
            this.copies = copies;

            if(copies < 0){
                this.addError("COPIES","Stock Quantity Cannot be below zero.");
            } else if(copies < this.quantityOnLoan){
                this.addError("COPIES2","Stock Quantity cannot be below the On-Loan Quantity.");         
            } else {
                this.removeError("COPIES");
                this.removeError("COPIES2");
            }            
        }
    }

    /**
     * Increases the quantity on loan by 1.
     * <br/>The current quantity on loan must be less than or equal to the total stock on hand before it can be changed.
     * @return true if the quantity value was changed successfully, false otherwise.
     */
    public void increaseQuantityOnLoan(){     
        if(this.quantityOnLoan + 1 <= this.copies){
            this.quantityOnLoan++;
            this.removeError("INCREASE_LOAN_QUANTITY");
        } else{
            this.addError("INCREASE_LOAN_QUANTITY","There is not enough stock to book out");            
        }         
    }
    
    /**
     * Decreases the quantity on loan by 1.
     * <br/>The current quantity on loan must be less than or equal to the total stock on hand before it can be changed.
     * @return true if the quantity value was changed successfully, false otherwise.
     */    
    public void decreaseQuantityOnLoan(){
        if(this.quantityOnLoan - 1 >= 0){
            this.quantityOnLoan--;
            this.removeError("DECREASE_LOAN_QUANTITY");
        } else{
            this.addError("DECREASE_LOAN_QUANTITY","Too much stock is being booked in.");            
        }           
    }
    
    /**
     * Sets the value for the quantity currently on loan.
     * @param quantityOnLoan the value for the quantity currently on loan.
     * @see increaseQuantityOnLoan()
     * @see decreaseQuantityOnLoan()
     */    
    public void setQuantityOnLoan(int quantityOnLoan) {        
        if(this.quantityOnLoan != quantityOnLoan){
            this.quantityOnLoan = quantityOnLoan;

            if(this.quantityOnLoan >= 0 && this.quantityOnLoan <= this.copies){
                this.removeError("LOAN_QUANTITY");
            } else{
                this.addError("LOAN_QUANTITY","Too much stock is being booked in.");            
            }             
        }        
    }

    /**
     * Sets the number of days that this title can be loaned out for. The maximum value is 127 days. The lowest is 0.
     * @param loanPeriod The number of days that this title can be loaned out for.
     */
    public void setLoanPeriod(byte loanPeriod) {
        if(this.loanPeriod != loanPeriod){
            if(loanPeriod > 0 && loanPeriod <= MAXLOANPERIOD){
                this.removeError("LOAN_PERIOD");        
            } else {
                this.loanPeriod = DEFAULTLOANPERIOD; 
                this.addError("LOAN_PERIOD","Loan period either exceeds the maximum " + MAXLOANPERIOD + " days or it is set to zero or below.");                 
            }            
        }
    }

    /**
     * Sets related authors for this title.
     * @param titleAuthors
     */
    public void setTitleAuthors(String titleAuthors) {
        if(!this.titleAuthors.equals(titleAuthors)){
            this.titleAuthors = titleAuthors; 
            if(this.name.isEmpty()){
                this.addError("TITLE_AUTHORS", "You must specify at least one author");
            } else {
                this.removeError("TITLE_AUTHORS");
            }              
        }
    }
                
    /**
     * Calculates the available stock that can be loaned out.
     * @return the available stock that can be loaned out.
     */
    public int availableQuantity(){
        return this.copies - this.quantityOnLoan;
    }
    
    @Override
    public int hashCode() {
        int hash = 29;        
        hash = (this.name != null) ? 3 * hash + this.name.hashCode() : 0;
        hash = (this.isbn != null) ? 3 * hash + this.isbn.hashCode() : 0;        
        return hash;
    }
    
    @Override
    public boolean equals(Object obj) {

        if (obj == null) {return false;}        
        
        if(obj instanceof Title){
            Title t = (Title)obj;
            if(t.getName().equals(this.name) && t.getIsbn().equals(this.isbn)){
                return true;
            }
        }
        return false;
    }
        
    @Override
    public String toString() {  
        return String.format("%-13s\t%-60s\t%-8s%4d\t%4d\t%4d\t%3d\t%-60s", 
                this.isbn, 
                this.name,
                this.location,
                this.copies,
                this.quantityOnLoan,
                this.availableQuantity(),
                this.loanPeriod,
                this.titleAuthors); 
    }

}
