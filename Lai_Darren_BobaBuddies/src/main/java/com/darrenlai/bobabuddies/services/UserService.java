package com.darrenlai.bobabuddies.services;

import java.util.List;
import java.util.Optional;
    
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.darrenlai.bobabuddies.models.LoginUser;
import com.darrenlai.bobabuddies.models.User;
import com.darrenlai.bobabuddies.repositories.UserRepository;
    
@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepo;
    
    public User register(User newUser, BindingResult result) {
    	Optional<User> potentialUser = userRepo.findByEmail(newUser.getEmail());
    	if(potentialUser.isPresent()) {
    		result.rejectValue("email", "Unique", "The email already exists in database");
    	}
    	
    	if(!newUser.getPassword().equals(newUser.getConfirm())) {
    	    result.rejectValue("confirm", "Matches", "Confirm Password and Password do not match!");
    	}
    	
    	if(result.hasErrors()) return null;
    	
    	String hashed = BCrypt.hashpw(newUser.getPassword(), BCrypt.gensalt());
    	newUser.setPassword(hashed);
        return userRepo.save(newUser);
    }
    public User login(LoginUser newLoginObject, BindingResult result) {

    	Optional<User> user = userRepo.findByEmail(newLoginObject.getEmail());
//    	User currentUser = user.get();
    	if(!user.isPresent()) {
    		result.rejectValue("email", "NoMatch", "Invalid email/password combination");
    	}
        else if(!BCrypt.checkpw(newLoginObject.getPassword(), user.get().getPassword())) {
            result.rejectValue("password", "PasswordMatch", "Invalid email/password combination");
        }

    	if (result.hasErrors()) {
    		return null;
    	}
    	
        return user.get();
    }
    public User findUser(Long id) {
    	return userRepo.findById(id).orElse(null);
    }
    
	public User followUser(Long userId, Long friendId) {
		List<User> friendsOfUser = null;
		User user = userRepo.findById(userId).get();
		User friend = userRepo.findById(friendId).get();
		friendsOfUser = user.getFriends();
		if (!friendsOfUser.contains(friend)) {
			friendsOfUser.add(friend);
			user.setFriends(friendsOfUser);
			return userRepo.save(user);
			}
		else return null;
		
	}
	public Boolean isFollowingUser(Long userId, Long friendId) {
		List<User> friendsOfUser = null;
		User user = userRepo.findById(userId).get();
		User friend = userRepo.findById(friendId).get();
		friendsOfUser = user.getFriends();
		if (friendsOfUser.contains(friend)) return true;
		else return false;
	}
	public void unfollowUser(Long userId, Long friendId) {
		userRepo.deleteFriendshipByIds(userId, friendId);
	}
}

    