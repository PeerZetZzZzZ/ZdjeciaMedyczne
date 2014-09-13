package model.exception;

/**
 * Thrown when technician doesn't modify the picture description such as doctor and etc.
 * @author PeerZet
 */
public class PictureDataException extends Exception{

    private String message;

    public PictureDataException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
