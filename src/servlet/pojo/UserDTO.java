package servlet.pojo;

import entity.Customer;

public class UserDTO {
    String role;
    String name;

    public UserDTO(Customer customer) {
        this.role = customer.getRole().getName().toLowerCase();
        this.name = customer.getName();
    }
}
