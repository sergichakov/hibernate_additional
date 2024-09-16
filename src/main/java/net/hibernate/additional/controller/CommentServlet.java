package net.hibernate.additional.controller;


import jakarta.servlet.ServletContext;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.hibernate.additional.repository.SessionRepoHelper;
import net.hibernate.additional.service.CommentService;
import net.hibernate.additional.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet(name = "CommentServlet", value = "/comment")
public class CommentServlet extends HttpServlet {
    public void init() {
        Logger logger = LoggerFactory.getLogger(CommentServlet.class);
        ServletContext servletContext = getServletContext();
        servletContext.setAttribute("logger", logger);


        //try{
        CommentService CommentService = new CommentService(new SessionRepoHelper());
        servletContext.setAttribute("service", CommentService);
        /*}catch(IOException e) {
            logger.error("cant instantiate QuestionService "+ e.getMessage());
        }

         */
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response){
        
    }
}
