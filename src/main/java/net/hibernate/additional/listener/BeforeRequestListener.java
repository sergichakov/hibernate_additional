package net.hibernate.additional.listener;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletRequestEvent;
import jakarta.servlet.ServletRequestListener;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSession;
import net.hibernate.additional.model.SessionObject;

import java.net.http.HttpRequest;

@WebListener
public class BeforeRequestListener  implements ServletRequestListener {

    @Override
    public void requestInitialized(ServletRequestEvent event) {
        System.out.println("Request initialized");
        /*
        ServletRequest request=event.getServletRequest();

        HttpSession currentSession = request.getSession();
        SessionObject sessionObject=(SessionObject) currentSession.getAttribute("session");
        if(sessionObject==null){
            sessionObject= SessionObject.builder()
                    .sessionId(currentSession.getId())
                    .name(userName)
                    .password(password)
                    .build();
            currentSession.setAttribute("session",sessionObject);

         */
    }
}
