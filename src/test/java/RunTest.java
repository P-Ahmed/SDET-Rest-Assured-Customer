import org.apache.commons.configuration.ConfigurationException;
import org.junit.Test;

import java.io.IOException;

public class RunTest {
    Customer customer = new Customer();
    @Test
    public void login() throws ConfigurationException, IOException {
        customer.CallingLoginApi();
    }
    @Test
    public void customerList() throws IOException {
        customer.CustomerList();
    }
    @Test
    public void createCustomer() throws IOException {
        customer.CreateCustomer();
    }
    @Test
    public void searchCustomer() throws IOException {
        customer.SearchCustomer();
    }
    @Test
    public void updateCustomer() throws IOException {
        customer.UpdateCustomer();
    }
    @Test
    public void deleteCustomer() throws IOException {
        customer.DeleteCustomer();
    }
}
