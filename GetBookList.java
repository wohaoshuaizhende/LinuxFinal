import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

@WebServlet(urlPatterns = "/GetBookList")
public class GetBookList extends HttpServlet {
   final static String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
   final static String URL = "jdbc:mysql://180.76.134.108/linux_final";
   final static String USER = "root";
   final static String PASS = "Lxc5910.";
   final static String SQL_QURERY_ALL_BOOK = "SELECT * FROM t_book;";
   Connection conn = null;

   public void init() {
      try {
         Class.forName(JDBC_DRIVER);
         conn = DriverManager.getConnection(URL, USER, PASS);
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   public void destroy() {
      try {
         conn.close();
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      response.setContentType("application/json");
      response.setCharacterEncoding("UTF-8");
      PrintWriter out = response.getWriter();

      List<Book> bookList = getAllBook();
      Gson gson = new Gson();
      String json = gson.toJson(bookList, new TypeToken<List<Book>>() {
      }.getType());
      out.println(json);
      out.flush();
      out.close();
   }

   private List<Book> getAllBook() {
      List<Book> bookList = new ArrayList<Book>();
      Statement stmt = null;
      try {
         stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(SQL_QURERY_ALL_BOOK);
         while (rs.next()) {
            Book book = new Book();
            book.id = rs.getInt("id");
            book.name = rs.getString("name");
            book.author = rs.getString("author");
            bookList.add(book);
         }
         rs.close();
         stmt.close();
      } catch (SQLException se) {
         se.printStackTrace();
      } catch (Exception e) {
         e.printStackTrace();
      } finally {
         try {
            if (stmt != null)
               stmt.close();
         } catch (SQLException se) {
            se.printStackTrace();
         }
      }

      return bookList;
   }
}
