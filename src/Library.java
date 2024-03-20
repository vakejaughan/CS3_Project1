import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

class Book { //book object
    private int id;
    private String author;
    private int IBSN;
    private int rating;
    private int numPages;
    private boolean isAvailable = true; //books by default will be available to rent upon adding
    private String title;

    public Book() {} //default constructor

    public Book(int id, String title, String author, int IBSN, int rating, int numPages) { //parameterized constructor
        this.id = id;
        this.author = author;
        this.IBSN = IBSN;
        this.rating = rating;
        this.numPages = numPages;
        this.title = title;
    }

    // getters & setters
    public void setId(int id) {this.id = id;}
    public int getId() {return this.id;}
    public void setAuthor(String author) {this.author = author;}
    public String getAuthor() {return this.author;}
    public void setIBSN(int IBSN) {this.IBSN = IBSN;}
    public int getIBSN() {return this.IBSN;}
    public void setRating(int rating) {this.rating = rating;}
    public int getRating() {return this.rating;}
    public void setNumPages(int numPages) {this.numPages = numPages;}
    public int getNumPages() {return this.numPages;}
    public String getIsAvailable() {
        if (this.isAvailable){
            return "Book is available to check out";
        }
        return "Book is checked out";
    }

    public void setAvailable(boolean isAvailable) {this.isAvailable = isAvailable;}
    public void setTitle(String title) {this.title = title;}
    public String getTitle() {return this.title;}

    public static void printBookInfo(Book book) {
        System.out.println("Book ID: " + book.getId());
        System.out.println("Book Author: " + book.getAuthor());
        System.out.println("Book Title: " + book.getTitle());
        System.out.println("Book IBSN: " + book.getIBSN());
        System.out.println("Book Rating: " + book.getRating());
        System.out.println("Number of pages: " + book.getNumPages());
        System.out.println(book.getIsAvailable());
    }
}

class Inventory extends Book{ //data structure
    public ArrayList<Book> availableBooks = new ArrayList<Book>();
    public ArrayList<Book> checkedBooks = new ArrayList<Book>();
    public ArrayList<Book> allBooks = new ArrayList<Book>();
    public int numAvailable = availableBooks.size();
    public int numChecked = checkedBooks.size();
    public int totalBooks = allBooks.size();
    public int getTotalNumBooks() {return allBooks.size();}

    public void searchBook() { // this needs work
        String userInput;
        if (allBooks.size() > 0) {
            System.out.println("Enter the title of the book for which you are looking for below or type 'EXIT' to return to main menu:");
            Scanner scnr = new Scanner(System.in);
            userInput = scnr.next().toLowerCase().strip();
            if (userInput.equals("EXIT")) {
                System.out.println("Returning you to main menu ->");
            }
            else {
                boolean bookFound = false;
                for (Book bk : allBooks) {
                    if (bk.getTitle().toLowerCase().strip().equals(userInput)) {
                        System.out.println("BOOK FOUND:");
                        printBookInfo(bk);
                        bookFound = true;
                        break;
                    }
                }
                if (!bookFound) { System.out.println("There are no books in the library under that title. Confirm you are entering the exact title of the book.");}
            }
        }
        else {
            System.out.println("There are no books in the library currently. Add a book and try again! Returning you to the main menu ->");
        }
    }
    public void addBook() {
        String title;
        String author;
        int IBSN;
        int rating;
        int numPages;
        System.out.println("--------");
        System.out.println("Please enter the new book's information following the pattern below, separated by commas.");
        System.out.println("Title, Author Name, IBSN, Rating, Number of pages. The Book-ID will be automatically assigned.");
        System.out.println("If you no longer wish to add a new book, enter 'EXIT' and you will be redirected back to the main menu.");
        Scanner scnr = new Scanner(System.in);
        String userInput = scnr.nextLine();

        if (userInput.equals("EXIT")) { //This needs to return to the main
            System.out.println("Returning you to the main menu ->");
        } else {
            String[] bookData = userInput.split(",");
            if (bookData.length != 5) {
                System.out.println("Please ensure the book information is entered correctly and try again.");
                addBook();
            } else {
                try {
                    ++totalBooks;
                    ++numAvailable;
                    title = bookData[0].trim();
                    author = bookData[1].trim();
                    IBSN = Integer.valueOf(bookData[2].trim());
                    rating = Integer.valueOf(bookData[3].trim());
                    numPages = Integer.valueOf(bookData[4].trim());
                    Book newBook = new Book(totalBooks, title, author, IBSN, rating, numPages);
                    availableBooks.add(newBook);
                    allBooks.add(newBook);
                    System.out.println("New Book Added to library!");
                    printBookInfo(newBook);
                } catch (NumberFormatException e) {
                    System.out.println("Please make sure the book information is formatted correctly and try again.");
                    System.out.println("--------");
                    addBook();
                }
            }
        }
    }
    public void borrowBook() {
        try {
            if (totalBooks == 0) { // if there are no books available to check out
                System.out.println("There are no books in the library, add a book and try again.");
            } else if (numAvailable == 0) { // if there are books in the library but they are all checked out
                System.out.println("All books are checked out.");
            } else { // if there is at least one book in the library available
                int i = 0;
                while (i != numAvailable) { //while the iteration is less than the len of the numBooks arr
                    System.out.println(i + " -- " + availableBooks.get(i).getTitle() + " by " + availableBooks.get(i).getAuthor());
                    ++i;
                }
                Scanner scnr = new Scanner(System.in); //Scanner object for user input
                System.out.println("If no books interests you, enter '1111' and you will be redirected.");
                System.out.println("Enter the number of the book which you want to check out: ");
                int userInput = scnr.nextInt(); //assign book number from user input
                if (userInput >= 0 && userInput < numAvailable) { // if user input is less than or equal to the number of available books
                    Book userSelection = availableBooks.get(userInput);
                    checkedBooks.add(userSelection);
                    availableBooks.remove(userInput);
                    --numAvailable;
                    ++numChecked;
                    userSelection.setAvailable(false);
                    System.out.println("Please take care of the book while it is checked out by you! I know who you are!");
                } else if (userInput == 1111) { // if the user wishes to exit
                    System.out.println("Returning to main menu ->");
                } else { // invalid input
                    System.out.println("Please make sure you are entering the number that corresponds to the book you wish to check out");
                    borrowBook();
                }
            }
        } catch (InputMismatchException e) {
            System.out.println("Make sure you are entering the corresponding number to the book you wish to check out.");
            borrowBook();
        }
    }
    public void returnBook() {
        try {
            if (numChecked == 0 && totalBooks > 0) {
                System.out.println("All books are in the library and ready to check out.");
            } else if (totalBooks == 0) {
                System.out.println("No books exist in the library currently, add a book and try again.");
            } else {
                int i = 0;
                while (i != numChecked) {
                    System.out.println(i + " -- " + checkedBooks.get(i).getTitle() + " by " + checkedBooks.get(i).getAuthor());
                    ++i;
                }
                Scanner scnr = new Scanner(System.in);
                System.out.println("If you would no longer like to return a book, enter '1111' and you will be redirected.");
                System.out.println("Enter the number associated with the book you would like to return:");
                int userInput = scnr.nextInt();
                if (userInput >= 0 && userInput < numChecked) {
                    Book userSelection = checkedBooks.get(userInput);
                    availableBooks.add(userSelection);
                    checkedBooks.remove(userSelection);
                    --numChecked;
                    ++numAvailable;
                    userSelection.setAvailable(true);
                    System.out.println("Thank you for returning your book!");
                } else if (userInput == 1111) {
                    System.out.println("Returning you to the main menu ->");
                } else {
                    System.out.println("Please make sure you are entering the number that corresponds to the book you wish to check out");
                    returnBook();
                }
            }
        } catch (InputMismatchException e) {
            System.out.println("Make sure you are entering the corresponding number to the book you wish to return.");
            returnBook();
        }
    }
    public void printLibrary() { //prints whole arraylist of books stored in the library
        if (totalBooks > 0) {
            System.out.println("Here are all the books the library owns:");
            for (Book bk : allBooks) {
                System.out.println("---------");
                printBookInfo(bk);
                System.out.println("---------");
            }
        }
        else {
            System.out.println("No books are loaded into the library.");
        }
    }
    public void printChecked() { //prints whole arraylist of books that are already checked out
        if (totalBooks == 0) {
            System.out.println("There are no books loaded into the library");
        }
        else if (numChecked == 0) {
            System.out.println("No books are checked out currently but here is what the library has available to check out:");
            printLibrary();
        }
        else {
            System.out.println("Printing books that are checked out: ");
            for (Book bk : checkedBooks) {
                System.out.println("---------");
                printBookInfo(bk);
                System.out.println("---------");
            }
        }
    }
}
public class Library { //driver class
    public static void main(String[] args) {
        Inventory inventory = new Inventory();
        System.out.println("WELCOME TO JAKE'S COMMUNITY LIBRARY");
        while (true) {
            System.out.println("--------");
            System.out.println("HOW CAN I ASSIST YOU?");
            System.out.println("1. Print entire library inventory     2. Print books that need to be returned    3. Check out book");
            System.out.println("4. Return checked book                5. Add a new book to library inventory     6. Search for a book by title");
            System.out.println("7. Close Library");
            System.out.println("--------");
            System.out.println("Input choice below:");

            Scanner scnr = new Scanner(System.in);
            try {
                int userInput = scnr.nextInt();
                switch (userInput) {
                    case 1:
                        inventory.printLibrary();
                        break;
                    case 2:
                        inventory.printChecked();
                        break;
                    case 3:
                        inventory.borrowBook();
                        break;
                    case 4:
                        inventory.returnBook();
                        break;
                    case 5:
                        inventory.addBook();
                        break;
                    case 6:
                        inventory.searchBook();
                        break;
                    case 7:
                        System.out.println("Come back soon!");
                        scnr.close();
                        System.exit(0);
                    default:
                        System.out.println("Invalid input, try again.");
                        main(new String[]{});

                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input, try again.");
                scnr.next();
            }
        }
    }
}
