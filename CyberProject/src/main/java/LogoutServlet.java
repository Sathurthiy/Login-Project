import java.io.IOException;
import javax.servlet.annotation.WebServlet; 
import javax.servlet.http.*;

@WebServlet("/LogoutServlet")
public class LogoutServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) //servlet class handles http request doget,post
    throws IOException {

        HttpSession session = request.getSession(false); //get existing session

        if(session != null){
            session.invalidate();   // destroy session ,remove all session attributes,logs user out
        }

        response.sendRedirect("front.jsp"); // go back to login page
    }
}
