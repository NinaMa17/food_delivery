// Saved as "ebookshop\WEB-INF\classes\QueryServlet.java".
import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
 
public class FoodDisplay extends HttpServlet {  // JDK 6 and above only
 
   // The doGet() runs once per HTTP GET request to this servlet.
   @Override
   public void doGet(HttpServletRequest request, HttpServletResponse response)
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
            "jdbc:mysql://172.22.210.114:3306/food_delivery", "myuser", "xinyue");  // <<== Check
         // For MS Access
         // conn = DriverManager.getConnection("jdbc:odbc:ebookshopODBC");
 
         // Step 2: Create a "Statement" object inside the "Connection"
         stmt = conn.createStatement();
 
         // Step 3: Execute a SQL SELECT query
         String sqlStr = "SELECT I.img_name, F.food_name, F.food_id, F.canteen_id FROM food F, food_img I where F.food_id=I.food_id ORDER BY canteen_id ASC";
         ResultSet rset = stmt.executeQuery(sqlStr); // Send the query to the server
         
         // Print an HTML page as output of query
         out.println("<html><head><title>NTU FOOD DELIEVERY</title></head><body background='bg_img.jpg'>");
          out.println("<p align='center'><font size='50' color='orange'>NTU Food Delivery</font></p>");
    	 out.println("<form method='post' action='shopcart'>");
         int temp = 0;
         int align_count=0;
         while (rset.next()) {
 
             int canteen = rset.getInt("F.canteen_id");
             if (canteen != temp) {
       
                 temp = canteen;
                 out.println("<hr>");
                 out.println("<p><center><font color='white' size='200'>Canteen" + canteen + "</font></center></p><br>");
                 align_count = 1;
             }
             if (align_count%3==1) {
                 //out.println("<p align='left'>");
                 //out.println("<span style='text-align:left;'><img src='"+rset.getString("I.img_name")+"' alt='"+ rset.getString("F.food_name")+"' style='width:320px;height:240px;' /></span>");
                 out.println("<span style='text-align:left;'><figure style='text-align:left;width:33%;'><img src='"+rset.getString("I.img_name")+"' alt='"+ rset.getString("F.food_name")+"' style='width:320px;height:240px;' />");
                 out.println("<figcaption style='text-align:center;width:320px;'><input type='checkbox' name='food_id' value='"+rset.getInt("F.food_id")+"' />"+rset.getString("F.food_name")+"</figcaption></figure></span>");
             }
             if (align_count%3==2) {
                 //out.println("<p align='center'>");
                 out.println("<span style='text-align:center;'><figure style='text-align:left;width:33%;'><img src='"+rset.getString("I.img_name")+"' alt='"+ rset.getString("F.food_name")+"' style='width:320px;height:240px;' />");
                 out.println("<figcaption style='text-align:center;width:320px;'><input type='checkbox' name='food_id' value='"+rset.getInt("F.food_id")+"' />"+rset.getString("F.food_name")+"</figcaption></figure></span>");
             }
             if (align_count%3==0) {
                 //out.println("<p align='right'>");
                 //out.println("<span style='text-align:right;'><img src='"+rset.getString("I.img_name")+"' alt='"+ rset.getString("F.food_name")+"' style='width:320px;height:240px;' /></span>");
                 out.println("<span style='text-align:right;'><figure style='text-align:left;width:33%;'><img src='"+rset.getString("I.img_name")+"' alt='"+ rset.getString("F.food_name")+"' style='width:320px;height:240px;' />");
                 out.println("<figcaption style='text-align:center;width:320px;'><input type='checkbox' name='food_id' value='"+rset.getInt("F.food_id")+"' />"+rset.getString("F.food_name")+"</figcaption></figure></span>");
             }
        	 //out.println("<font color='white'>" + rset.getString("food_name") + "</font>");
             align_count++;
         }
    	 out.println("<p><input type='submit' value='Go to shop cart!' ></p>");
         //out.println("<p>You query is: " + sqlStr + "</p>"); // Echo for debugging

 
         // Step 4: Process the query result
         /*
         int count = 0;
         while(rset.next()) {
            // Print a paragraph <p>...</p> for each row
            out.println("<p>" + rset.getString("author")
                  + ", " + rset.getString("title")
                  + ", $" + rset.getDouble("price") + "</p>");
            ++count;
         }
         */
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
