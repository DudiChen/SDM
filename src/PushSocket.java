import controller.Controller;
import entity.Customer;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.OnClose;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.CloseReason;

@ServerEndpoint("/api/push/")
public class PushSocket {

    @OnOpen
    public void open(@PathParam("uuid") String uuid, Session session) {
        if(uuid == null) {
            // TODO: NOAM: fix following line:
//            session.close(CloseReason closeReason);
        }
        else{
            Customer customer = Controller.getInstance().getCustomerById(Integer.parseInt(uuid));
            if (customer.getRole().equals(Customer.Role.SELLER)) {
                Controller.getInstance().logInSeller(session, customer);
            }
        }
    }

    @OnClose
    public void close(@PathParam("uuid") String uuid, Session session) {
        Customer customer = Controller.getInstance().getCustomerById(Integer.parseInt(uuid));
        if (customer.getRole().equals(Customer.Role.SELLER)) {
            Controller.getInstance().logOutSeller(session, customer);
        }
    }
}