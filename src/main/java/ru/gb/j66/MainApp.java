package ru.gb.j66;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import ru.gb.j66.beans.daos.CustomerDao;
import ru.gb.j66.beans.daos.OrdersDao;
import ru.gb.j66.beans.daos.ProductDao;
import ru.gb.j66.beans.services.CustomerInfo;
import ru.gb.j66.entities.Customer;
import ru.gb.j66.entities.OrderData;
import ru.gb.j66.entities.Product;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@SpringBootApplication (scanBasePackages = "ru.gb.j66.beans")
public class MainApp
{
    public static final boolean H2MEM = true;
    private static ConfigurableApplicationContext context;
    private static SessionFactory sessionFactory;
    public static final String SEPARATOR = "=============================================\n";

	public static void main (String[] args)
	{
		//try
        //{   context = SpringApplication.run (MainApp.class, args);
        //    logline("Инициализация окончена.");
        //}
        //catch (Exception e){e.printStackTrace();}

        context = new AnnotationConfigApplicationContext (MainApp.class);
        runTest();
	}
//------------------- Конфигурация ---------------------------------------------------

    @Bean
    private static SessionFactory sessionFactory ()
    {
        Configuration cfg = new Configuration().configure("hibernate6-6.cfg.xml");
        sessionFactory = cfg.buildSessionFactory();

        if (H2MEM)
        if (sessionFactory != null && !readSqlFile ("lesson6-6.sql", sessionFactory))
        {
            sessionFactory.close();
            sessionFactory = null;
        }
        return sessionFactory;
    }
//------------------------------------------------------------------------------------

    private static boolean readSqlFile (String strPath, SessionFactory sf)
    {
        boolean result = false;
        try (Session session = sf.getCurrentSession();)
        {
            String sql = Files.readString(Paths.get(strPath));

            session.beginTransaction();
            session.createNativeQuery(sql).executeUpdate();
            session.getTransaction().commit();
            result = true;
            logline("SQL-файл считан: "+ strPath);
        }
        catch (IOException e){e.printStackTrace();}
        return result;
    }


    static void runTest()
    {
        Long cid = 3L;
        Long pid = 5L;
        double cost = 150.0;

        CustomerDao cdao = context.getBean(CustomerDao.class);
        ProductDao pdao = context.getBean(ProductDao.class);
        OrdersDao odao = context.getBean(OrdersDao.class);
        CustomerInfo ci = context.getBean(CustomerInfo.class);

        Customer c = cdao.createCastomer ("Толик");
        Product p = pdao.createProduct ("Велик", cost);
        OrderData od = odao.save (c, p, cost);

        System.out.println(SEPARATOR);  //------------------
        try(Session s = sessionFactory.getCurrentSession())
        {
            s.beginTransaction();

            printList (s.createQuery("from Customer", Customer.class).getResultList());
            printList (s.createQuery("from Product", Product.class).getResultList());
            printList (s.createQuery("from OrderData", OrderData.class).getResultList());

            System.out.printf("Список продуктов покупателя с cid = %d:\n\n", cid);
            printList(ci.getProductsByCustomerId(cid, s));

            System.out.printf("Список покупателей продукта с pid = %d:\n\n", pid);
            printList(ci.getCustomersByProductId(pid, s));

            s.getTransaction().commit();
        }
    }

    public static <T> void printList (List<T> list)
    {
        for (T t : list)
            System.out.println(t);
        System.out.println(SEPARATOR);  //------------------
    }

    public static void logline (String msg)
    {
        System.out.print ("\n********************\t" + msg + "\t**************\n\n");
    }
}
