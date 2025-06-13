package views;

import controller.CartController;
import controller.ProductController;
import controller.UserController;
import model.dto.cart.CartItemRequestDto;
import model.dto.product.ProductResponseDto;
import model.dto.user.UserRequestDto;
import model.dto.user.UserResponseDto;
import model.entity.CartItems;
import model.entity.Carts;
import model.entity.Product;
import model.entity.User;
import model.repository.CartRepository;
import org.w3c.dom.ls.LSOutput;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class UI {
    private static final UserController userController = new UserController();
    private static final ProductController productController = new ProductController();
    private static final CartController cartController = new CartController();
    private static final CartRepository  cartRepository = new CartRepository();

    private static void thumbnail(){
        System.out.println("""
                =========================
                Welcome E-Commerce System
                =========================
                1. Login
                2. Register
                3. Exit
                """);
    }

    private static boolean isLoggedIn = false;
    private static String currentUserEmail = null;
    private static void checkExistingSession(){
        File file = new File("src/session.txt");
        if(file.exists()){
            try(BufferedReader reader = new BufferedReader(new FileReader(file))){
                String email = reader.readLine();
                if (email != null && !email.isBlank()){
                    isLoggedIn = true;
                    currentUserEmail = email;
                    System.out.println("You are logged in as " + email);
                }
            } catch (IOException e){
                System.err.println("Error reading session file: " +  e.getMessage());
            }
        }
    }

    public static void auth(){
        checkExistingSession();
        int option = 0;
        if (isLoggedIn == false){
            while(true){

                thumbnail();
                System.out.print("[+] Insert Option: ");
                try{
                    option = new Scanner(System.in).nextInt();

                } catch (InputMismatchException e){
                    System.out.println("Must be a number");
                }
                switch (option){
                    case 1 -> {
                        System.out.println("""
                            =================
                               Login User
                            =================
                            """);
                        System.out.print("Enter your email: ");
                        String email = (new Scanner(System.in)).nextLine();
                        System.out.print("Enter your password: ");
                        String password = (new Scanner(System.in)).nextLine();
                        boolean success = userController.loginUser(email,password);
                        if (success == true){
                            home();
                        } else {
                            System.out.println("Invalid email or password");
                        }

                    }
                    case 2 -> {
                        System.out.println("""
                            ====================
                               Register User
                            ====================
                            """);
                        System.out.print("[+] Insert Username: ");
                        String username = (new Scanner(System.in)).nextLine();
                        System.out.print("[+] Insert Email: ");
                        String email = (new Scanner(System.in)).nextLine();
                        System.out.print("[+] Insert Password: ");
                        String password = (new Scanner(System.in)).nextLine();
                        User createNew = userController.createNewUser(
                                UserRequestDto.builder()
                                        .user_name(username)
                                        .email(email)
                                        .password(password)
                                        .build()
                        );

//                        System.out.println("User Created with id " + createNew.getId());
                        System.out.println(userController.createNewUser(
                                UserRequestDto.builder()
                                        .user_name(username)
                                        .email(email)
                                        .password(password)
                                        .build()
                        ));

                        // After user creation, create cart


                    }
                    case 3 -> {
                        System.exit(0);
                    }
                    default -> System.out.println("invalid option");
                }
            }
        }

        if (isLoggedIn == true){
            home();
        }
    }

    public static void thumbnailHome(){
        System.out.println("""
                =================================
                    Welcome E-Commerce System
                =================================
                1. View All Product
                2. Search By Name
                3. Add Product To Cart
                4. Order Product
                5. Logout
                """);
    }
    public static void home(){

        int option = 0;
        while(true){
            thumbnailHome();
            try{
                System.out.print("[+] Insert Option: ");
                option = new Scanner(System.in).nextInt();
            } catch (InputMismatchException e){
                System.out.println("Must be a number");
            }
            switch (option){
                case 1 -> {
                    System.out.println("""
                            ====================
                              View All Product
                            ====================
                            """);
                    List<ProductResponseDto> productResponseDtoList = productController.findALl();
                    Collections.reverse(productResponseDtoList);
                    new TableUI<ProductResponseDto>().getTableDisplay(productResponseDtoList);

                }
                case 2 -> {
                    System.out.println("""
                            ======================
                                Search Product
                            ======================
                            1. Search By Name
                            2. Search By Category
                            """);
                    System.out.print("[+] Insert Your Option: ");
                    option = new Scanner(System.in).nextInt();
                    switch (option){
                        case 1 -> {
                            System.out.println("Search By Name");
                            System.out.print("[+] Insert Name Product: ");
                            String name = new  Scanner(System.in).nextLine();

                            // Capture results from controller
                            List<ProductResponseDto> productsByName = productController.findProductByName(name);

                            // Display results in table
                            new TableUI<ProductResponseDto>().getTableDisplay(productsByName);
                        }
                        case 2 -> {
                            System.out.println("""
                                    ==========================
                                        Search By Category
                                    ==========================
                                    """);
                            System.out.print("[+] Insert Category: ");
                            String category = new  Scanner(System.in).nextLine();

                            // Capture results from controller
                            List<ProductResponseDto> productsByCategory = productController.findProductByCategory(category);

                            // Display results in table
                            new TableUI<ProductResponseDto>().getTableDisplay(productsByCategory);
                        }
                    }
                }
                case 3 -> {
                    System.out.println("""
                            =======================
                              Add Product To Cart
                            =======================
                            """);
                    System.out.print("[+] Insert Product UUID: ");
                    String uuid = (new Scanner(System.in)).nextLine();
                    System.out.print("[+] Insert User ID: ");
                    int userId = (new Scanner(System.in)).nextInt();
                    System.out.print("[+] Insert QTY: ");
                    int qty = (new Scanner(System.in)).nextInt();
                    // Build request DTO
                    CartItemRequestDto cartItemRequestDto = CartItemRequestDto.builder()
                            .user_id(userId)
                            .quantity(qty)
                            .build();

                    // Call Controller to add product to cart
                    CartItems result = cartController.addProductToCartByUuid(userId, uuid, cartItemRequestDto);

                    if (result != null) {
                        System.out.println("✅ Product added successfully to cart!");
                    } else {
                        System.out.println("❌ Failed to add product to cart.");
                    }

                    new Scanner(System.in).nextLine();

                }
                case 4 -> {
                    System.out.println("Order Product");
                }
                case 5 -> {
                    File file = new File("src/session.txt");
                    if (file.exists()) file.delete();
                    isLoggedIn = false;
                    currentUserEmail = null;
                    System.out.println("Logout Successful");
                    auth();
                }
            }
        }
    }

}
