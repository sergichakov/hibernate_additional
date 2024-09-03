package net.hibernate.additional.controller;


import net.hibernate.additional.model.SessionObject;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet(name = "startServlet", value = "/start")
public class StartServlet extends HttpServlet {
    @Override
    public void init() throws ServletException {
        super.init();
        Logger logger = LoggerFactory.getLogger(StartServlet.class);
        ServletContext servletContext = getServletContext();
        servletContext.setAttribute("logger",logger);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        HttpSession currentSession = request.getSession();
        ServletContext servletContext = getServletContext();
        Logger logger=(Logger) servletContext.getAttribute("logger");
        SessionObject sessionObject=(SessionObject) currentSession.getAttribute("session");

        String userName=request.getParameter("userName");
        String password=request.getParameter("password");
        logger.info("The "+userName+" User is registered");
        //String workerName="Unknown";
        //String hideForm="";
/*        if(userName!=null&& !userName.isEmpty()){
            workerName=userName;
            sessionObject.setName(userName);
            sessionObject.setPassword(password);
            //hideForm=" hidden='true' ";
            //request.setAttribute("hideForm",hideForm);
        }*/
        if(sessionObject==null){
            sessionObject=SessionObject.builder()
                    .sessionId(currentSession.getId())
                    .name(userName)
                    .password(password)
                    .build();
            currentSession.setAttribute("session",sessionObject);
        }/*else{
            workerName=sessionObject.getName();
            //hideForm=" hidden='true' ";
            //request.setAttribute("hideForm",hideForm);
            //sessionObject.setCurrentLevel(1);
        }
        String ipAddress = request.getHeader("X-FORWARDED-FOR");//getting ipAddress
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }
        request.setAttribute("ipAddress",""+ipAddress);
        request.setAttribute("playerName",""+workerName);
*/
        request.getRequestDispatcher("/register.jsp").include(request, response);
    }
}