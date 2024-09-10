package net.hibernate.additional.controller;


import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import net.hibernate.additional.exception.AuthenticationException;
import net.hibernate.additional.exception.NoPermissionException;
import net.hibernate.additional.object.SessionObject;
import net.hibernate.additional.service.TaskService;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "countServlet", value = "/task/count")
public class CountOnPageServlet extends HttpServlet {
    public void init() {
        //Logger logger = LoggerFactory.getLogger(ViewListOfTasksServlet.class);
        ServletContext servletContext = getServletContext();
        //servletContext.setAttribute("logger",logger);


        //try{
        TaskService taskService=new TaskService();
        servletContext.setAttribute("service",taskService);
        /*}catch(IOException e) {
            logger.error("cant instantiate QuestionService "+ e.getMessage());
        }

         */
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletContext servletContext = getServletContext();
        TaskService taskService=(TaskService) servletContext.getAttribute("service");
        HttpSession currentSession = request.getSession();
        SessionObject sessionObject=(SessionObject) currentSession.getAttribute("session");
        Integer count= null;
        try {
            count = taskService.getAllCount(sessionObject);

        } catch (AuthenticationException e) {
            response.sendError(404, "User name "+sessionObject.getName()+" have wrong password or not registered");
        }catch(NoPermissionException e){
            response.sendError(404, "User name "+sessionObject.getName()+" but need ADMIN permission");
        }

        PrintWriter out =  response.getWriter();
        out.println(count);
    }
}
