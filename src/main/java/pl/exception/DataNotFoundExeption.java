package pl.exception;

public class DataNotFoundExeption extends RuntimeException {
   public DataNotFoundExeption(String message) {
      super(message);
   }
}
