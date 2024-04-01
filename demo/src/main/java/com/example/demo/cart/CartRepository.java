package com.example.demo.cart;


import com.example.demo.user.LocalUser;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface CartRepository extends ListCrudRepository<Cart,Long> {

    List<Cart> findByUser(LocalUser user);

}
