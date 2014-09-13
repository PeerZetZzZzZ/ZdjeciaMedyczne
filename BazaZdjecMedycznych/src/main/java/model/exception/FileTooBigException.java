package model.exception;

/**
 *
 * @author PeerZet
 */
public class FileTooBigException extends Exception{
    private String message;
    public FileTooBigException(String message) {
        this.message=message;
    }
    @Override
    public String getMessage(){
        return message;
    }
}
