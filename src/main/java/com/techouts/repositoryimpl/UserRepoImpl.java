package com.techouts.repositoryimpl;

import com.techouts.model.User;
import com.techouts.repository.UserRepo;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepoImpl implements UserRepo {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void save(User user) {
        sessionFactory.getCurrentSession().persist(user);
    }
    @Override
    public User update(User user) {
        return (User) sessionFactory.getCurrentSession().merge(user);
    }


    @Override
    public Optional<User> findByEmail(String email) {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM User u WHERE lower(u.email) = lower(:email)", User.class)
                .setParameter("email", email)
                .uniqueResultOptional();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM User u WHERE u.name = :username", User.class)
                .setParameter("username", username)
                .uniqueResultOptional();
    }

    @Override
    public Optional<User> findByPhone(String phone) {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM User u WHERE u.phone = :phone", User.class)
                .setParameter("phone", phone)
                .uniqueResultOptional();
    }

    @Override
    public boolean existsByUsername(String username) {
        Long count = sessionFactory.getCurrentSession()
                .createQuery("SELECT COUNT(u) FROM User u WHERE u.name = :username", Long.class)
                .setParameter("username", username)
                .uniqueResult();
        return count != null && count > 0;
    }

    @Override
    public boolean existsByEmail(String email) {
        Long count = sessionFactory.getCurrentSession()
                .createQuery("SELECT COUNT(u) FROM User u WHERE u.email = :email", Long.class)
                .setParameter("email", email)
                .uniqueResult();
        return count != null && count > 0;
    }

    @Override
    public Optional<User> findUserWithDetails(String email) {
        return sessionFactory.getCurrentSession()
                .createQuery(
                        "SELECT u FROM User u " +
                                "LEFT JOIN FETCH u.userOrders " +
                                "LEFT JOIN FETCH u.cart c " +
                                "LEFT JOIN FETCH c.cartItems " +
                                "WHERE u.email = :email",
                        User.class)
                .setParameter("email", email)
                .uniqueResultOptional();
    }
}
