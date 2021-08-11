package ru.gb.j66.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table (name="products")
public class Product
{
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;  //< Если у продукта id == 0, то он считается новым и для него создаётся запись в БД.

    @Column(name="title")
    private String title;

    @Column(name="price")
    private double cost;

/*  Один продукт может быть связан с многими строками таб. orders_data, но строка в таб. orders_data может быть связана только с одним продуктом.    */

    @OneToMany(mappedBy="prouct")    //< имя поля в классе OrderData
    private final List<OrderData> orderDatas;

/*  Продукт не может быть напрямую связан с покупателем.    */

    protected Product()
    {
        orderDatas = OrderData.emptyOrderDatatList();
    }

    public static Product newProduct (String title, double cost)
    {
        Product result = new Product();
        if (!result.setTitle(title) || !result.setCost(cost))
        {
            result = null;
        }
        return result;
    }
//--------------------- Сеттеры и геттеры ----------------------------------------

    public Long getId ()     {   return id;   }
    private void setId (Long id) {   this.id = id;   }

    public String getTitle() {   return title;   }
    public boolean setTitle (String title)
    {
        if (isTitleValid (title))
        {
            this.title = title;
            return true;
        }
        return false;
    }

    public double getCost()  {   return cost;   }
    public boolean setCost (double cost)
    {
        if (isCostValid (cost))
        {
            this.cost = cost;
            return true;
        }
        return false;
    }

    public List<OrderData> getOrderDatas()  {   return Collections.unmodifiableList(orderDatas);   }

//--------------------------------------------------------------------------------

    public static boolean isTitleValid (String title)
    {
        return title != null && !title.trim().isEmpty();
    }

    public static boolean isCostValid (Double cost)
    {
        return cost != null && cost >= 0.0;
    }

    public void addOrderData (OrderData od)
    {
        if (od != null && !orderDatas.contains(od))
            orderDatas.add(od);
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder("Product:(id:")
            .append(id)
            .append(", название:")
            .append(title)
            .append(", цена:")
            .append(String.format("%.2f",cost))
            .append(")\n");

        for (OrderData od : orderDatas)
        {
            sb.append("\t").append(od).append('\n');
        }
        return sb.toString();
    }

    public static List<Product> emptyProductList ()  {   return new ArrayList<>();   }
}
