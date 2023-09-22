package com.darrenlai.bobabuddies.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.darrenlai.bobabuddies.models.User;

import jakarta.transaction.Transactional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    
    Optional<User> findByEmail(String email);
    
    @Modifying
    @Transactional
    @Query(value="DELETE FROM friendships WHERE user_id = ?1 AND friend_id =?2", nativeQuery=true)
	void deleteFriendshipByIds(Long userId, Long friendId);
    

}
    