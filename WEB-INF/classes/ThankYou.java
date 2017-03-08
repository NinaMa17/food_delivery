// Saved as "ebookshop\WEB-INF\classes\QueryServlet.java".
import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
 
public class ThankYou extends HttpServlet {  // JDK 6 and above only
 
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
            "jdbc:mysql://172.22.210.114:3306/food_delivery", "myuser", "xinyue");  // <<== Check
         // For MS Access
         // conn = DriverManager.getConnection("jdbc:odbc:ebookshopODBC");
 
         // Step 2: Create a "Statement" object inside the "Connection"
         stmt = conn.createStatement();
 
         // Step 3: Execute a SQL SELECT query
         out.println("<html><head><title>Thank You</title></head><body background='bg_img.jpg'>");
         out.println("<p align='center'><font size='50' color='orange'>NTU Food Delivery</font></p>");
    	 out.println("<form method='get' action='main'>");
         String name= request.getParameter("name");
         String phone_num = request.getParameter("phone");
         String address = request.getParameter("address");
         String wait_time = request.getParameter("wait_time");
         int phone_number = Integer.parseInt(phone_num);
          if (name==null || phone_num==null || address==null || wait_time==null) {
              out.println("<h2>Please go back and enter your contact information!</h2>");
              return;
          }
    	 String sqlStr;
         sqlStr = "insert into cus_order values('"+name+"', '"+phone_number+"', '"+address+"', now());";
          int count = 0;
          count=stmt.executeUpdate(sqlStr);
         out.println("<p><center>You will receive your order in "+ wait_time+"</center></p>");
         ////
         //TODO: FORMAT THE TIME INFORMATION!!
         ///
         out.println("<input type='submit' value='Go back to main page' />");
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
