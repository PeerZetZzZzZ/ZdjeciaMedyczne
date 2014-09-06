package model.exception;

/**
 * Regex exception class. This exception is thrown when user inputs not valid
 * String.
 *
 * @author PeerZet
 */
public class RegexException extends Exception {

    private String message;
    public RegexException(String message) {
        this.message=message;
    }
    @Override
    public String getMessage(){
        return message;
    }
}
