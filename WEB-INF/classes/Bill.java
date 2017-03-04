// Saved as "ebookshop\WEB-INF\classes\QueryServlet.java".
import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
 
public class Bill extends HttpServlet {  // JDK 6 and above only
 
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
         out.println("<html><head><title>Bill</title></head><body background='bg_img.jpg'>");
    	 out.println("<form method='post' action='thankyou'>");
         String[] qties = request.getParameterValues("food_qty");
         String[] prices = request.getParameterValues("food_price");
         String[] ids = request.getParameterValues("food_id");
         String sqlStr1, sqlStr2; 
         Time wait_time = new Time(0,0,0);
         double total_amount=0;
         if (prices == null) {
        	 out.println("<h2>Please go back and select your food!</h2>");
        	 return;
         } else {
        	 int qty = Integer.parseInt(qties[0]);
        	 double price = Double.parseDouble(prices[0]);
        	 total_amount+= qty*price;
        	 sqlStr1 = "update food set qty=qty-"+qty+" where food_id in ("+"'"+ids[0]+"'";
        	 for (int i=1; i< ids.length; i++) {
        		 sqlStr1 += ", '"+ids[i]+"'";
        		 qty = Integer.parseInt(qties[i]);
        		 price = Double.parseDouble(prices[i]);
        		 total_amount+= qty*price;
        	 }
        	 sqlStr1 += ")";

     
         }
         int count;
         count = stmt.executeUpdate(sqlStr1);
         
         sqlStr2="select max(estimated_time) as wait_time from food where food_id in("+"'"+ids[0]+"'";
         for (int i=1; i<ids.length; i++) {
        	 sqlStr2 +=", '"+ids[i]+"'";
         }
         sqlStr2 += ")";
         
         ResultSet rset2=stmt.executeQuery(sqlStr2);
         while (rset2.next()) {
             wait_time=rset2.getTime("wait_time");        	 
         }


        
         out.println("<p><center>The total amount is: S$"+total_amount+"</center></p>");
         out.println("<p>Please enter your personal detail: </p><form>");
         out.println("Enter your name: <input type='text' name='name' /><br/>");
         out.println("Enter your phone number: <input type='text' name='phone' /></br>");
         out.println("Enter your address: <input type='text' name='address' /></br>");
         out.println("<input type='hidden' name='wait_time' value='>"+wait_time+"' /><input type='submit' value='Submit' /></br>");
         //////////////////////
         /*
         TODO: Find the waiting time:
          And enter personal contact
         */
         ////////////////////
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
