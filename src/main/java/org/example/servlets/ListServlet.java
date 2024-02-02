package org.example.servlets;


import com.jaunt.*;
import com.jaunt.component.Form;

import org.example.Entity.Product;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

public class ListServlet extends HttpServlet {

    private final static String DBURL = "jdbc:mysql://localhost:3306/product";
    private final static String DBUSER = "root";
    private final static String DBPASS = "password123.";
    private final static String DBDRIVER = "com.mysql.jdbc.Driver";

    private Connection connection;
    private Statement statement;
    private String query;


    public String createSaveQuery(Product product) {
        String query1 = "INSERT INTO product(name, price) ('" + product.getName() + "', '" + product.getPrice() +"');";
        return query1;
    }
    public void save(Product product) {

        String sql = "INSERT INTO product (name, price) VALUES (?, ?)";

        try {
            Class.forName(DBDRIVER).newInstance();
            connection = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
            statement = connection.createStatement();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, product.getName());
            preparedStatement.setString(2, product.getPrice());

            preparedStatement.executeUpdate();

            statement.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException | IllegalAccessException
                 | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("views/main.jsp");
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        int counter = 1;
        try{
            UserAgent userAgent = new UserAgent();
            userAgent.visit("https://sklepkoszykarski.pl/");
            Form form = userAgent.doc.getForm("<form class=\"search-form right \" action=\"https://sklepkoszykarski.pl/pl/s\" method=\"post\">");
            form.setTextField("search", name);
            form.submit();
            Element element = userAgent.doc.findFirst("<div class=\"products products_extended viewphot s-row\">");
            Elements elements = element.findEvery("<div class=\"product product_view-extended s-grid-3 product-main-wrap\">");
            for(Element element1 : elements) {
                Element product = element1.findFirst("<span class=\"productname\">");
                Element inner = element1.findFirst("<div class=\"price f-row\">");
                Element price = inner.findFirst("<em>");
                req.setAttribute("product" + counter, product.getChildText() + " " + price.getChildText());
                save(new Product(product.getChildText(), price.getChildText()));
                counter++;
            }
        }
        catch(JauntException e){
            System.err.println(e);
        }

        doGet(req, resp);
    }
}
