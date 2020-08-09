package pl.exception;

public class CanNotEditCategoryExcpetion extends RuntimeException {
   public CanNotEditCategoryExcpetion(String message) {
      super(message);
   }
}
