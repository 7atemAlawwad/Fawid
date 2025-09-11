package com.example.finalprojectjavabootcamp;

import com.example.finalprojectjavabootcamp.Model.*;
import com.example.finalprojectjavabootcamp.Repository.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;




import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RepositoryTests {

    @Autowired
    private BuyerRepository buyerRepository;

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private UserRepository userRepository;


    Buyer buyer;
    Seller seller;
    Subscription subscription;
    User user;



    @BeforeEach
    void setUp() {
        user = new User();
        user.setName("Test User");

        buyer = new Buyer();
        buyer.setUser(user);

        seller = new Seller();
        seller.setUser(user);

        subscription = new Subscription();
        subscription.setStatus("ACTIVE");

    }

    // ===== BuyerRepository =====
    @Test
    public void testFindBuyersByIdAndUser() {
        Buyer saved = buyerRepository.save(buyer);

        Buyer byId = buyerRepository.findBuyersById(saved.getId());
        Buyer byUser = buyerRepository.findBuyersByUser(user);

        Assertions.assertThat(byId).isNotNull();
        Assertions.assertThat(byUser).isNotNull();
        Assertions.assertThat(byUser.getUser().getName()).isEqualTo("Test User");
    }

    // ===== SellerRepository =====
    @Test
    public void testFindSellerByIdAndUser() {
        Seller saved = sellerRepository.save(seller);

        Seller byId =   sellerRepository.findSellerById(saved.getId());
        Seller byUser = sellerRepository.findSellerByUser(user);

        Assertions.assertThat(byId).isNotNull();
        Assertions.assertThat(byUser).isNotNull();
        Assertions.assertThat(byUser.getUser().getName()).isEqualTo("Test User");
    }

    // ===== SubscriptionRepository =====
    @Test
    public void testFindSubscriptionById() {
        Subscription saved = subscriptionRepository.save(subscription);

        Subscription found = subscriptionRepository.findSubscriptionById(saved.getId());

        Assertions.assertThat(found).isNotNull();
        Assertions.assertThat(found.getStatus()).isEqualTo("ACTIVE");
    }


    @Test
    void testFindUserByEmail() {
        // Arrange
        User user = new User();
        user.setName("Fahad");
        user.setEmail("fahad@example.com");
        user = userRepository.save(user);

        // Act
        User foundUser = userRepository.findUserByEmail("fahad@example.com");

        // Assert
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getName()).isEqualTo("Fahad");
    }






}
