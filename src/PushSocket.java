import controller.Controller;
import entity.Customer;

import java.io.IOException;
import javax.websocket.OnOpen;
import javax.websocket.OnClose;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.CloseReason;

@ServerEndpoint("/api/push/{uuid}")
public class PushSocket {

    @OnOpen
    public void open(@PathParam("uuid") String uuid, Session session) {
        if(uuid == null) {
            CloseReason closeReason = new CloseReason(CloseReason.CloseCodes.CANNOT_ACCEPT, "no uuid provided");
            try {
                session.close(closeReason);
            } catch (IOException e) {
                e.printStackTrace();
            }
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
            Controller.getInstance().logOutSeller(customer);
        }
    }
}