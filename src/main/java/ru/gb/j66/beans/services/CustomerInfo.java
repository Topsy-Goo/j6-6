package ru.gb.j66.beans.services;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.gb.j66.beans.daos.CustomerDao;
import ru.gb.j66.beans.daos.ProductDao;
import ru.gb.j66.entities.Customer;
import ru.gb.j66.entities.Product;

import java.util.List;

@Component
public class CustomerInfo
{
    private final CustomerDao customerDao;
    private final ProductDao productDao;


    @Autowired
    public CustomerInfo (CustomerDao cdao, ProductDao pdao)
    {
        if (cdao == null || pdao == null)
            throw new IllegalArgumentException();

        customerDao = cdao;
        productDao = pdao;
    }
//-------------------------------------------------------------

    public List<Product> getProductsByCustomerId (Long customerId, Session session)
    {
        return customerDao.getProductsByCustomerId (customerId, session);
    }

    public List<Customer> getCustomersByProductId (Long productId, Session session)
    {
        return productDao.getCustomersByProductId (productId, session);
    }
}
