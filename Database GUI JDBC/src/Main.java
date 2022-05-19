import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;

public class Main extends Application {
    private static Connection connection;
    VBox vbox_main;
    GridPane grid;

    @Override
    public void start(Stage stage) throws Exception {
        connection = createConnection(); // Create the connection to the Microsoft Access database

        vbox_main = new VBox(5); // Root node of the scene. Will contain the menu bar and the gridpane
        grid = new GridPane(); // This part will be dynamic and will change in response to user input
        grid.setVgap(5);
        grid.setHgap(5);
        grid.setAlignment(Pos.CENTER);

        // Create the Menu, MenuBar, and MenuItems objects for the main scene
        MenuBar menuBar = new MenuBar();
        Menu menu = new Menu("Menu");
        MenuItem menuItem_orderTotal = new MenuItem("Order Total");
        MenuItem menuItem_orderDetail = new MenuItem("Order Detail");
        MenuItem menuItem_customers = new MenuItem("Customers");
        MenuItem menuItem_employees = new MenuItem("Employees");
        menu.getItems().addAll(menuItem_orderTotal, menuItem_orderDetail, menuItem_customers, menuItem_employees);
        menuBar.getMenus().addAll(menu);

        vbox_main.getChildren().addAll(menuBar, grid);

        Scene scene_menu = new Scene(vbox_main, 600, 400); // Create the scene to display in the stage
        stage.setTitle("Midterm Project"); // Set the stage title
        stage.setScene(scene_menu); // Place the scene in the stage
        stage.show(); // Display the stage

        // Event handlers
        // Can't implement an event handler on a Menu object, must be a MenuItem object that is added to a Menu object
        menuItem_orderTotal.setOnAction(e ->{
            grid.getChildren().clear(); // Clear the gridpane so that objects from other MenuItems do not remain on screen
            TextField textField_orderNumber = new TextField();
            Button button_orderTotal = new Button("Get Order Total");
            Label label_results = new Label();
            grid.getChildren().clear();
            grid.add(new Label("Order Number"), 0,0);
            grid.add(textField_orderNumber, 1, 0);
            grid.add(button_orderTotal, 2, 0);
            grid.add(label_results, 0, 1, 3, 1); // Dynamic part that will display the results

            // Issue square brackets are not working for column names with spaces
            // Resolved: Use square brackets for table names with spaces and remove spaces from column names with spaces
            button_orderTotal.setOnAction(e1 -> {
                try {
                    label_results.setText(""); // Clear any results displayed on screen from previous input

                    String orderID = textField_orderNumber.getText();
                    Statement statement = connection.createStatement();
                    String query = """
                            SELECT SUM((unitprice - unitprice * discount) * quantity) 
                            FROM [order details] WHERE orderid = 
                            """ + orderID + ";";
                    ResultSet resultSet = statement.executeQuery(query);
                    resultSet.next();
                    Double unformattedMoney = Double.valueOf(String.valueOf(resultSet.getObject(1)));
                    String results = "The order total is $" + String.format("%.2f", unformattedMoney);
                    label_results.setText(results);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            });
        });

        menuItem_orderDetail.setOnAction(e -> {
            grid.getChildren().clear();
            TextField textField_orderNumber = new TextField();
            Button button_orderDetails = new Button("Get Order Details");
            Label label_date = new Label();
            Label label_freight = new Label();
            Label label_productid = new Label();
            Label label_quantity = new Label();
            Label label_unitprice = new Label();
            Label label_discount = new Label();
            grid.add(new Label("Order Number"), 0,0);
            grid.add(textField_orderNumber, 1, 0);
            grid.add(button_orderDetails, 2, 0);

            // Dynamic part that will display the results
            grid.add(label_date, 0, 1, 3, 1);
            grid.add(label_freight, 0, 2, 3, 1);
            grid.add(label_productid, 0,3);
            grid.add(label_unitprice, 1, 3);
            grid.add(label_quantity, 2, 3);
            grid.add(label_discount, 3, 3);

            button_orderDetails.setOnAction(e1 -> {
                try {
                    label_date.setText("");
                    label_freight.setText("");
                    label_productid.setText("");
                    label_unitprice.setText("");
                    label_quantity.setText("");
                    label_discount.setText("");

                    String orderID = textField_orderNumber.getText();
                    Statement statement = connection.createStatement();
                    String query = """
                            SELECT orderdate, freight 
                            FROM orders 
                            WHERE orderid = 
                            """ + orderID + ";";
                    ResultSet resultSet = statement.executeQuery(query);

                    resultSet.next();
                    // Use substring to remove the time from the date
                    label_date.setText("Order Date: " + String.valueOf(resultSet.getObject(1)).substring(0, 11));
                    double unformattedMoney = Double.parseDouble(String.valueOf(resultSet.getObject(2)));
                    label_freight.setText("Freight Charge $" + String.format("%.2f", unformattedMoney));

                    // Issue: Product doesn't exist
                    // Resolved: GUI shows column name as 'product' but it is actually 'productid'
                    query = """
                            SELECT productid, unitprice, quantity, discount
                            FROM [order details]
                            WHERE orderid =
                            """ + orderID + ";";
                    resultSet = statement.executeQuery(query);

                    // String objects formatted like a column to be displayed on the screen
                    // Use StringBuilder because we will be appending to the String in a loop
                    StringBuilder productID = new StringBuilder("Product ID\n");
                    StringBuilder unitprice = new StringBuilder("Unit Price\n");
                    StringBuilder quantity = new StringBuilder("Quantity\n");
                    StringBuilder discount = new StringBuilder("Discount\n");
                    while (resultSet.next()) {
                        productID.append(resultSet.getObject(1) + "\n");
                        unformattedMoney = Double.parseDouble(String.valueOf(resultSet.getObject(2)));
                        unitprice.append("$" + String.format("%.2f", unformattedMoney) + "\n");
                        quantity.append(resultSet.getObject(3) + "\n");
                        discount.append(resultSet.getObject(4) + "\n");
                    }
                    label_productid.setText(productID.toString());
                    label_unitprice.setText(unitprice.toString());
                    label_quantity.setText(quantity.toString());
                    label_discount.setText(discount.toString());
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            });
        });

        menuItem_customers.setOnAction(e -> {
            grid.getChildren().clear();
            Label label_state = new Label("Name of country");
            TextField textField_country = new TextField();
            Button button_getCustomers = new Button("Get Customer Details");
            Label label_hint = new Label("Enter 'USA' for United States, 'UK' for United Kingdoms");
            Label label_customerName = new Label();
            Label label_customerCity = new Label();
            grid.add(label_state, 0,0);
            grid.add(textField_country, 1, 0);
            grid.add(button_getCustomers, 2, 0);
            grid.add(label_hint, 0, 1, 3, 1);

            // Dynamic part that will display the results
            grid.add(label_customerName, 0, 2);
            grid.add(label_customerCity, 1, 2);

            button_getCustomers.setOnAction(e1 -> {
                try {
                    label_customerName.setText("");
                    label_customerCity.setText("");

                    Statement statement = connection.createStatement();
                    String country = textField_country.getText();
                    String query = """
                    SELECT contactname, city
                    FROM customers
                    WHERE country = """ + "'" + country + "'\n"
                            + "ORDER BY city;";
                    ResultSet resultSet = statement.executeQuery(query);
                    StringBuilder customerName = new StringBuilder("Customer Name\n");
                    StringBuilder customerCity = new StringBuilder("City\n");
                    while (resultSet.next()) {
                        customerName.append(resultSet.getObject(1) + "\n");
                        customerCity.append(resultSet.getObject(2) + "\n");
                    }
                    label_customerName.setText(customerName.toString());
                    label_customerCity.setText(customerCity.toString());
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            });
        });

        menuItem_employees.setOnAction(e -> {
            grid.getChildren().clear();
            Label label_year = new Label("Birth Year");
            TextField textField_year = new TextField();
            Button button_getEmployees = new Button("Get Employee Details");
            Label label_employeeLastName = new Label();
            Label label_employeeFirstName = new Label();
            grid.add(label_year, 0,0);
            grid.add(textField_year, 1, 0);
            grid.add(button_getEmployees, 2, 0);

            // Dynamic part that will display the results
            grid.add(label_employeeLastName, 0, 1);
            grid.add(label_employeeFirstName, 1, 1);

            button_getEmployees.setOnAction(e1 -> {
                try {
                    label_employeeLastName.setText("");
                    label_employeeFirstName.setText("");

                    Statement statement = connection.createStatement();
                    String year = textField_year.getText();
                    String query = """
                        SELECT lastname, firstname 
                        FROM employees 
                        WHERE YEAR(birthdate) = '""" + year + "';";
                    ResultSet resultSet = statement.executeQuery(query);
                    StringBuilder lastName = new StringBuilder("Last Name\n");
                    StringBuilder firstName = new StringBuilder("First Name\n");
                    while (resultSet.next()) {
                        lastName.append(resultSet.getObject(1) + "\n");
                        firstName.append(resultSet.getObject(2) + "\n");
                    }
                    label_employeeLastName.setText(lastName.toString());
                    label_employeeFirstName.setText(firstName.toString());
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            });

        });
    }

    private Connection createConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:ucanaccess://C:/data/Northwind.mdb");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
