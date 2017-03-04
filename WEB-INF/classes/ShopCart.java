// Saved as "ebookshop\WEB-INF\classes\QueryServlet.java".
import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
 
public class ShopCart extends HttpServlet {  // JDK 6 and above only
 
   // The doGet() runs once per HTTP GET request to this servlet.
   @Override
   public void doPost(HttpServletRequest request, HttpServletResponse response)
                     throws ServletException, IOException {
      // Set the MIME type for the response message
      response.setContentType("text/html");
      // Get a output writer to write the response message into the network socket
      PrintWriter out = response.getWriter();
 
      Connection conn = null;
      Statement stmt = null;
      try {
         // Step 1: Create a database "Connection" object
         // For MySQL
         conn = DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/food_delivery", "myuser", "xinyue");  // <<== Check
         // For MS Access
         // conn = DriverManager.getConnection("jdbc:odbc:ebookshopODBC");
 
         // Step 2: Create a "Statement" object inside the "Connection"
         stmt = conn.createStatement();
 
         // Step 3: Execute a SQL SELECT query
         out.println("<html><head><title>SHOP CART</title></head><body background='bg_img.jpg'>");
    	 out.println("<form method='post' action='bill'>");
         String[] ids = request.getParameterValues("food_id");
         String sqlStr;
         if (ids == null) {
        	 out.println("<h2>Please go back and select your food!</h2>");
        	 return;
         } else {
        	 sqlStr = "select I.img_name, F.food_name, F.price, F.qty, F.food_id from food F, food_img I where F.food_id in (";
        	 sqlStr += "'" + ids[0] + "'";
        	 for (int i = 1; i< ids.length; i++) {
        		 sqlStr += ", '" + ids[i] + "'";
        	 }
        	 sqlStr += ") and I.food_id = F.food_id;";
     
         }

         ResultSet rset = stmt.executeQuery(sqlStr);
         out.println("<table><tr><th>Item<th><th>Price</th><th>qty</th></tr>");
         while (rset.next()) {
        	 out.println("<p><tr><td>"+rset.getString("I.img_name")+"</td><td>S$"+
         rset.getDouble("F.price")+"</td><td><input type='number' name='food_qty' value='0' min='0' max='"+
        			 rset.getInt("F.qty")+"' ><input type='hidden' name='food_id' value='"+rset.getInt("F.food_id")+"' ><input type='hidden' name='food_price' value='"+ rset.getDouble("F.price")+"' ></td></tr></p>");
         }
         out.println("</table>");
       
         out.println("<p><input type='submit' value='Checkout!'></p>");
         out.println("</form></body></html>");

      } catch (SQLException ex) {
         ex.printStackTrace();
      } finally {
         out.close();
         try {
            // Step 5: Close the Statement and Connection
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
         } catch (SQLException ex) {
            ex.printStackTrace();
         }
      }
   }
}
