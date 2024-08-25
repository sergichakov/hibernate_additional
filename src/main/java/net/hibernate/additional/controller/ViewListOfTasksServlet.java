package net.hibernate.additional.controller;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import net.hibernate.additional.command.TaskCommandDTO;
import net.hibernate.additional.dto.TaskDTO;
import net.hibernate.additional.model.SessionObject;
import net.hibernate.additional.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(name = "viewRequestServlet", value = "/task")
public class ViewListOfTasksServlet extends HttpServlet {
    public void init() {
        Logger logger = LoggerFactory.getLogger(ViewListOfTasksServlet.class);
        ServletContext servletContext = getServletContext();
        servletContext.setAttribute("logger",logger);


        //try{
            TaskService taskService=new TaskService();
            servletContext.setAttribute("service",taskService);
        /*}catch(IOException e) {
            logger.error("cant instantiate QuestionService "+ e.getMessage());
        }

         */
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletContext servletContext = getServletContext();
        HttpSession currentSession = request.getSession();
        String pageNumber = request.getParameter("page");
        String countOnPage = request.getParameter("count");
        System.out.println(pageNumber+" pageNumber=countOnPage="+countOnPage);

        SessionObject sessionObject=(SessionObject) currentSession.getAttribute("session");
        TaskService taskService=(TaskService) servletContext.getAttribute("service");
        Logger logger=(Logger)servletContext.getAttribute("logger");
        String workerName="";
        if(sessionObject==null){
            sessionObject=SessionObject.builder()
                    .sessionId(currentSession.getId())
                    //////.name("Unknown")
                    .build();
            currentSession.setAttribute("session",sessionObject);
        }else{
            workerName=sessionObject.getName();
           //hideForm=" hidden='true' ";
            //request.setAttribute("hideForm",hideForm);
        }

        List<TaskDTO> taskDTOList=taskService.listAllTasks(sessionObject.getName(),Integer.getInteger(pageNumber),Integer.getInteger(countOnPage));
        ObjectMapper objectMapper = new ObjectMapper();
        //objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        String fromDtoToJson="";
        try {
            fromDtoToJson=objectMapper.writeValueAsString(taskDTOList);
        }catch(JsonProcessingException e){
            System.out.println("EXCEPTION");
            logger.error("JSON processing exception");
        }

        response.setStatus(200);

        System.out.println("fromDTOtoJSON"+fromDtoToJson);
        response.setContentType("application/json");//"text/html;charset=UTF-8");
        PrintWriter out =  response.getWriter();
        out.println(fromDtoToJson);
        out.close();
        //request.getRequestDispatcher("/question.jsp").include(request, response);//"/question.jsp"
    }
    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException{
        ServletContext servletContext = getServletContext();
        TaskCommandDTO taskCommandDto=jacksonProcessing(request);
        //TaskCommandDtoEntityMapper taskMapper=new TaskCommandDtoEntityMapperImpl();
        //TaskDTO taskDTO=taskMapper.toDTO(taskEntity);
        TaskService taskService=(TaskService) servletContext.getAttribute("service");
        System.out.println("taskCommandDTO"+taskCommandDto);
        boolean boolSuccess=taskService.editTask(taskCommandDto);
        if (boolSuccess==true){
            response.setStatus(200);
        }else response.setStatus(404);
    }
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException{
        ServletContext servletContext = getServletContext();
        HttpSession currentSession = request.getSession();
        Logger logger=(Logger)servletContext.getAttribute("logger");
        SessionObject sessionObject=(SessionObject) currentSession.getAttribute("session");
        TaskService taskService=(TaskService) servletContext.getAttribute("service");
        TaskCommandDTO taskCommandDto=jacksonProcessing(request);
        TaskDTO taskDTO=taskService.createTask(taskCommandDto);
        ObjectMapper objectMapper = new ObjectMapper();
        //objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        String fromDtoToJson="";
        try {
            fromDtoToJson=objectMapper.writeValueAsString(taskDTO);
        }catch(JsonProcessingException e){

            System.out.println("EXCEPTION");
            logger.error("JSON processing exception");
        }
        response.setContentType("application/json");//"text/html;charset=UTF-8");
        PrintWriter out =  response.getWriter();
        out.println(fromDtoToJson);
        out.close();
    }
    private TaskCommandDTO jacksonProcessing(HttpServletRequest request)throws IOException{
        ServletContext servletContext = getServletContext();
        HttpSession currentSession = request.getSession();
        SessionObject sessionObject=(SessionObject) currentSession.getAttribute("session");
        TaskService taskService=(TaskService) servletContext.getAttribute("service");
        Logger logger=(Logger)servletContext.getAttribute("logger");
        BufferedReader buffer=request.getReader();

        String json=buffer.lines().collect(Collectors.joining());
        ObjectMapper objectMapper=new ObjectMapper();
        TaskCommandDTO taskCommandDto=null;
        System.out.println("JSON_STRING"+json);
        try {
            taskCommandDto = objectMapper.readValue(json, TaskCommandDTO.class);
        }catch(JsonMappingException e){
            throw new IOException("Cant map JSon file",e);
        }catch(JsonProcessingException e){
            throw new IOException("Cant process JSon file",e);
        }
        return taskCommandDto;
    }
    @Override
    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException{
        ServletContext servletContext = getServletContext();
        TaskService taskService=(TaskService) servletContext.getAttribute("service");
        TaskCommandDTO taskCommandDto=jacksonProcessing(request);
        boolean isSuccessFull=taskService.deleteTask(taskCommandDto);
        if (isSuccessFull)response.setStatus(200);
        else response.setStatus(404);
    }
}
