package ru.gb.j66.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table (name="customers")
public class Customer
{
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;  //< Если у продукта id == 0, то он считается новым и для него создаётся запись в БД.

    @Column(name="customername")
    private String customerName;

/*  Используем OneToMany, т.к. один покупатель может быть связан с многими строками в таб. orders_data, но каждая строка в таб. orders_data может быть связана только с одним покупателем.   */

    @OneToMany(mappedBy="customer")              //< имя поля в классе OrderData
    //@Cascade({org.hibernate.annotations.CascadeType.DELETE})
    private final List<OrderData> orderDatas;

/*  Покупатель не связан напрямую с таблицей products.  */


    private Customer()
    {
        orderDatas = OrderData.emptyOrderDatatList();
    }

    public static Customer newCustomer (String name)
    {
        Customer customer = new Customer();
        if (!customer.setCustomername(name))
        {
            customer = null;
        }
        return customer;
    }
//--------------------- Сеттеры и геттеры ----------------------------------------

    public Long getId ()    {   return id;   }
    private void setId (Long id)    {   this.id = id;   }

    public String getCustomerName ()   {   return customerName;   }
    public boolean setCustomername (String name)
    {
        boolean ok = isNameValid (name);
        if (ok)
        {
            customerName = name;
        }
        return ok;
    }

    public List<OrderData> getOrderDatas()  {   return Collections.unmodifiableList (orderDatas);   }

//--------------------------------------------------------------------------------

    public static boolean isNameValid (String name)
    {
        return name != null && !name.trim().isEmpty();
    }

    public void addOrderData (OrderData od)
    {
        if (od != null && !orderDatas.contains(od))
            orderDatas.add(od);
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder("Customer:(id:")
            .append(id)
            .append(", имя:")
            .append(customerName)
            .append(")\n");

        for (OrderData od : orderDatas)
        {
            sb.append("\t").append(od).append('\n');
        }
        return sb.toString();
    }

    public static List<Customer> emptyCustomerList ()  {   return new ArrayList<>();   }
}
