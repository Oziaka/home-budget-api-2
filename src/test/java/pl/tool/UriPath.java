package pl.tool;

public class UriPath {

   private static String wallet = "/wallet";
   private static String user = "/user";

   public static String addWallet() {
      return wallet + "/add";
   }

   public static String getWallets() {
      return wallet + "s";
   }

   public static String register() {
      return "/register";
   }

   public static String userPrincipal() {
      return user;
   }

   public static String userProperty() {
      return user + "/profile";
   }

   public static String editUser() {
      return user + "/edit";
   }
}
