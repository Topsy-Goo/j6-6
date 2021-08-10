package ru.gb.j66.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table (name="orders_data")
public class OrderData
{
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    /* Одна строка табл. orders_data может быть связана только с одним покупателем в таб. customers, но один покупатель может быть связан со многими строками в таб. orders_data.  */
    @ManyToOne
    @JoinColumn(name="customer_id") //< имя соотв. колонки (в табл. orders_data) для внешнего ключа
    private Customer customer;


/*  Одна строка в таб. orders_data может быть связана только с одним товаром в таб. products, но один товар может быть связан с многими строками в таб. orders_data.    */
    @ManyToOne
    @JoinColumn(name="prouct_id")   //< имя соотв. колонки (в табл. orders_data) для внешнего ключа
    private Product prouct;


    @Column(name="buy_price")       //< имя соотв. колонки (в табл. orders_data)
    private double cost;


    private OrderData(){}

    public static OrderData newOrderData (Customer customer, Product product, double cost)
    {
        OrderData od = new OrderData();
        if (!od.setCustomer(customer) || !od.setProduct(product) || !od.setCost(cost))
        {
            od = null;
        }
        return od;
    }
//--------------------- Сеттеры и геттеры ----------------------------------------

    public Long getId ()    {   return id;   }
    private void setId (Long id) {   this.id = id;   }

    public double getCost() {   return cost;   }
    private boolean setCost (double c)
    {
        boolean ok = isCostValid(c);
        if (ok)
        {
            cost = c;
        }
        return ok;
    }

    public Product getProuct()  {   return prouct;   }
    private boolean setProduct (Product p)
    {
        boolean ok = isProductValid(p);
        if (ok)
        {
            prouct = p;
        }
        return ok;
    }

    public Customer getCustomer()   {   return customer;   }
    private boolean setCustomer (Customer c)
    {
        boolean ok = isCustomerValid(c);
        if (ok)
        {
            customer = c;
        }
        return ok;
    }
//--------------------------------------------------------------------------------

    public static boolean isProductValid (Product p)   {   return p != null;   }

    public static boolean isCustomerValid (Customer c)   {   return c != null;   }

    public static boolean isCostValid (double c)   {   return c >= 0.0;   }

    public String toString()
    {
        return String.format("OrderData:(id:%d, customerId:%d, productId:%d, buyCost:%.2f)",
                             id, customer.getId(), prouct.getId(), cost);
    }

    public static List<OrderData> emptyOrderDatatList ()  {   return new ArrayList<>();   }
}
