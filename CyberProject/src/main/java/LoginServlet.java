import java.io.IOException; //handle input output errors during request/response
import java.security.MessageDigest; //hash passwords with sha-256
import java.security.NoSuchAlgorithmException;
import java.sql.Connection; //to connect mysql db
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet; //maps servlet to url
import javax.servlet.http.HttpServlet;  //base class for creating servlet like request servlet
import javax.servlet.http.HttpServletRequest;  //handles client request
import javax.servlet.http.HttpServletResponse; //sends response to client
import javax.servlet.http.HttpSession;  //user session

@WebServlet("/LoginServlet") //maps servlet url
public class LoginServlet extends HttpServlet  {  //handles http request

    private static final long serialVersionUID = 1L;

    public static void main(String[] args) {
        System.out.println(hashPassword("Sathu@#123"));   //generate hash passwords for storing db 
        System.out.println(hashPassword("SreeVarshini@18"));
    }

    public static String hashPassword(String password) {  //converts password to ShA256
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");  //create object to initialize sha256
            byte[] hash = md.digest(password.getBytes());  //password converted to bytes and hash
            StringBuilder hexString = new StringBuilder(); //hash to hexdecimal string

            for (byte b : hash) { //each byte converted to hexdecimal
                String hex = Integer.toHexString(0xff & b); //ensures proper hexformat
                if (hex.length() == 1)
                    hexString.append('0');

                hexString.append(hex);
            }

            return hexString.toString();  //return hashed password

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public LoginServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.sendRedirect("front.jsp");   //someone directly opens loginservlet redirected to front
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) //runs when loginform is submitted
            throws ServletException, IOException {

        String username = request.getParameter("username").trim();  //get data from login form
        String password = request.getParameter("password").trim();
        String hashedPassword = hashPassword(password);
        //entered password converted to sha 256 before checking with db
        System.out.println("hashedpassword"+ hashedPassword);

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");  //loads sqljdbcdriver

            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/loginappdb",
                    "root",
                    "Password@#123"); //connect to db

            PreparedStatement ps = con.prepareStatement(
                    "SELECT password FROM users WHERE username=?"); //retrieves stored hashpassword for given username

            ps.setString(1, username); //?replaced with entered username

            ResultSet rs = ps.executeQuery(); //executes query and returns result

            HttpSession session = request.getSession(); //session created for login,attempts,locked

            Integer attempts = (Integer) session.getAttribute("attempts"); //number of failed login
            Long lockTime = (Long) session.getAttribute("lockTime"); //when account was locked

            if (attempts == null) {
                attempts = 0; //no attempts,start from 0
            }

            // CHECK IF ACCOUNT IS LOCKED
            if (lockTime != null) {

                long currentTime = System.currentTimeMillis();

                // 1 minute = 60000 ms
                if (currentTime - lockTime < 60000) { //account locked wait 1minute
                    response.sendRedirect("front.jsp?locked=1"); //redirect
                    return;
                } else {
                    // unlock after 1 minute
                    session.setAttribute("attempts", 0); //attempts reset
                    session.removeAttribute("lockTime");
                    attempts = 0;
                }
            }

            if (rs.next()) { //check if username exist in db

                String dbPassword = rs.getString("password").trim(); //get stored hashed password

                if (dbPassword.equals(hashedPassword)) {

                    // LOGIN SUCCESS
                    session.setAttribute("user", username);
                    session.setAttribute("attempts", 0);

                    response.sendRedirect("home.jsp");

                } else {

                    // WRONG PASSWORD
                    attempts++; //increase attempt count
                    session.setAttribute("attempts", attempts);

                    if (attempts >= 3) { //account locked

                        session.setAttribute("lockTime", System.currentTimeMillis());

                        response.sendRedirect("front.jsp?locked=1");

                    } else {

                        response.sendRedirect("front.jsp?error=1&left=" + (3 - attempts)); //how many attempts left
                    }
                }

            } else {

                // USERNAME NOT FOUND
                attempts++; //increase attempts
                session.setAttribute("attempts", attempts);

                if (attempts >= 3) { //lock after 3tries

                    session.setAttribute("lockTime", System.currentTimeMillis());

                    response.sendRedirect("front.jsp?locked=1");

                } else {

                    response.sendRedirect("front.jsp?error=1&left=" + (3 - attempts));
                }
            }

            con.close(); //close db connection

        } catch (Exception e) {
            e.printStackTrace(); //print error in console
        }
    }
}