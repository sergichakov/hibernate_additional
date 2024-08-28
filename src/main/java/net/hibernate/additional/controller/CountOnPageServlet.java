package net.hibernate.additional.controller;


import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.hibernate.additional.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        Integer count= taskService.getAllCount();
        PrintWriter out =  response.getWriter();
        out.println(count);
    }
}
