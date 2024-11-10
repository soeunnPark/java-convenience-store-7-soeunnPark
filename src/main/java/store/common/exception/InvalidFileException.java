package store.common.exception;

public class InvalidFileException extends ConvenienceStoreException{
    public InvalidFileException(String fileName) {
        super(ErrorMessage.INVALID_FILE_NAME, "(파일 이름: " + fileName + ")");
    }

    public InvalidFileException(String fileName, Exception cause) {
        super(ErrorMessage.INVALID_FILE_NAME, "(파일 이름: " + fileName + ")", cause);
    }
}
