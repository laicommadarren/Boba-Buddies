package com.darrenlai.bobabuddies.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.darrenlai.bobabuddies.models.Event;
import com.darrenlai.bobabuddies.models.User;
import com.darrenlai.bobabuddies.repositories.EventRepository;
import com.darrenlai.bobabuddies.repositories.UserRepository;

@Service
public class EventService {
	@Autowired
	private EventRepository eventRepository;
	@Autowired
	private UserRepository userRepository;
	
	// pagination
	private static final int PAGE_SIZE = 5;
    public Page<Event> eventsPerPage(int pageNumber) {
        PageRequest pageRequest = PageRequest.of(pageNumber, PAGE_SIZE, Sort.Direction.ASC, "dateTime");
        Page<Event> events = eventRepository.findAll(pageRequest);
        return eventRepository.findAll(pageRequest);
    }
	
    public Boolean checkUserInEvent(Long eventId, Long userId) {
    	if (eventRepository.getUserInEvent(eventId, userId).size() == 0) return false;
    	else return true;
    }
    
	public List<Event> allEvents() {
        return (List<Event>) eventRepository.findAll();
    }
	
	public Event createEvent(Event event, Long userId, BindingResult result) {
		if(result.hasErrors()) return null;
		LocalDateTime currentDateTime = java.time.LocalDateTime.now();
		List<User> users = new ArrayList<User>();
		User user = userRepository.findById(userId).get();
		users.add(user);
		event.setUsers(users);
//		System.out.println("input Date: " + event.getDateTime());
//		System.out.println("current Date: " + currentDateTime);
		if(event.getDateTime().compareTo(currentDateTime) < 1) {
			result.rejectValue("dateTime", "InFuture", "The date must be in the future");
		}
		@SuppressWarnings("deprecation")
		Integer endTime = event.getEndTime().getHours();
//		System.out.println("end Time: " + endTime);
		Integer startTime = event.getDateTime().getHour();
//		System.out.println("start Time: " + startTime);
		if (endTime - startTime <= 0) {
			result.rejectValue("endTime", "EndAfterStart", "End time must be after start time");
		}
		if(result.hasErrors()) return null;
		
		
		return eventRepository.save(event);
	}
	
	public Event findEvent(Long id) {
		Optional<Event> optionalCourse = eventRepository.findById(id);
		if(optionalCourse.isPresent()) {
			return optionalCourse.get();
		}
		else return null;
	}
	public List<Event> findEventsByUser(Long userId) {
		return eventRepository.findEventsByUserId(userId);
	}
	public Event addUserToEvent(Long userId, Long eventId) {
		List<User> users = null;
		User user = userRepository.findById(userId).get();
		Event event = eventRepository.findById(eventId).get();
		users = event.getUsers();
		if (!users.contains(user)) {
			users.add(user);
			event.setUsers(users);
			return eventRepository.save(event);
			}
		else return null;
		
	}
	
	public Event updateEvent(Event event) {
		return eventRepository.save(event);
	}
	
	public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }
}