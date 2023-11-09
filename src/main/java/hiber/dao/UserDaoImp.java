package hiber.dao;

import hiber.model.Car;
import hiber.model.User;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class UserDaoImp implements UserDao {

   @Autowired
   private SessionFactory sessionFactory;

   @Override
   public void add(User user) {
      sessionFactory.getCurrentSession().save(user);
   }

   @Override
   public User getUserById(String model, int series) {
      Criteria criteria = sessionFactory.getCurrentSession()
              .createCriteria(User.class, "user")
              .createAlias("user.car", "car")
              .add(Restrictions.eq("car.model", model))
              .add(Restrictions.eq("car.series", series))
              .setMaxResults(1);  //т.к таблица не самоочищается после запуска, создается много юзеров с одной машиной, и метод выдает эксепшн
      return (User) criteria.uniqueResult();
   }

   @Override
   @SuppressWarnings("unchecked")
   public List<User> listUsers() {
      TypedQuery<User> query=sessionFactory.getCurrentSession().createQuery("from User");
      return query.getResultList();
   }
}
