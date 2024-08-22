package uz.booker.bookstore.repository.jpa;


import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.booker.bookstore.entity.token.Token;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

    @Query("""
       select t from Token t inner join User u on t.user.id = u.id
       where u.id = :userId and (t.expired = false  or t.revoke = false)
    """)
    List<Token> findAllValidTokenByUser(@Param("userId") Long id);

    Optional<Token> findByToken(String token);

    @Query("DELETE FROM Token t WHERE t.revoke = true AND t.expired = true ")
    @Modifying
    @Transactional
    void deleteByRevokeAndExpired(boolean revoke, boolean expired);



}
