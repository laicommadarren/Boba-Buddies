package com.darrenlai.bobabuddies.controllers;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.darrenlai.bobabuddies.models.Comment;
import com.darrenlai.bobabuddies.models.Event;
import com.darrenlai.bobabuddies.models.Location;
import com.darrenlai.bobabuddies.models.User;
import com.darrenlai.bobabuddies.services.EventService;
import com.darrenlai.bobabuddies.services.LocationService;
import com.darrenlai.bobabuddies.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class EventController {
	@Autowired
	private UserService userService;
	@Autowired
	private LocationService locationService;
	@Autowired
	private EventService eventService;
	
	Comparator<Event> compareByDateTime = (Event o1, Event o2) ->
	o1.getDateTime().compareTo( o2.getDateTime() );
	
	@ModelAttribute("locations")
	public List<Location> allLocations() {
	    return locationService.allLocations();
	}
	
	@GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
    	Long id = (Long) session.getAttribute("userId");
		if (id == null) return "redirect:/";
		User user = userService.findUser(id);
		Collections.sort(user.getEvents(), compareByDateTime);
		List<Event> eventsByUserId = eventService.findEventsByUser(id);
		model.addAttribute("user", user);
		session.setAttribute("userName", user.getFirstName() + " " + user.getLastName());
		session.setAttribute("userFirstName", user.getFirstName());
		model.addAttribute("events", eventsByUserId);
    	return "dashboard";
    }
	// view logged in user's events
	@GetMapping("/events/user/{id}")
	public String userEvents(@PathVariable("id") Long id, Model model, HttpSession session) {
		Long userId = (Long) session.getAttribute("userId");
		if (userId == null) return "redirect:/";
		User user = userService.findUser(userId);
		Collections.sort(user.getEvents(), compareByDateTime);
	    model.addAttribute("user", user);
	    List<Event> eventsByUserId = eventService.findEventsByUser(id);
	    model.addAttribute("hostedEvents", eventsByUserId);
		return "userEvents";
	}
	// create new event page
	@GetMapping("/events/new")
    public String newEvent(@ModelAttribute("event") Event event, Model model, HttpSession session) {
    	Long userId = (Long) session.getAttribute("userId");
    	if (userId == null) return "redirect:/";
    	List<Location> locations = locationService.allLocations();
    	model.addAttribute("userId", userId);
    	model.addAttribute("locations", locations);
    	return "newEvent";
    }
	@PostMapping("/events/new")
	public String addEvent(@Valid @ModelAttribute("event") Event event, BindingResult result, HttpSession session) {
		eventService.createEvent(event, (Long) session.getAttribute("userId"), result);
		if (result.hasErrors()) return "newEvent";
		else return "redirect:/dashboard";
	}
	// all events (paginated)
	@RequestMapping("/events/all/{pageNumber}")
	public String getEventsPerPage(Model model, @PathVariable("pageNumber") int pageNumber) {
	    Page<Event> events = eventService.eventsPerPage(pageNumber - 1);
	    // total number of pages that we have
	    int totalPages = events.getTotalPages();
	    model.addAttribute("totalPages", totalPages);
	    model.addAttribute("events", events);
	    model.addAttribute("pageIndex", pageNumber);
	    return "allEvents";
	}
	//  view one event 
	@GetMapping("/events/{id}")
	public String oneEvent(@PathVariable("id") Long id, Model model, HttpSession session) {
		Event event = eventService.findEvent(id);
		model.addAttribute("event", event);
		model.addAttribute("eventService", eventService);
		model.addAttribute("comment", new Comment());
//		System.out.println(eventService.checkUserInEvent(Long.valueOf(5), Long.valueOf(1)));
		return "oneEvent";
	}
	@PostMapping("/events/{id}/join")
	public String joinEvent(@PathVariable("id") Long id, Model model, HttpSession session) {
		Long userId = (Long) session.getAttribute("userId");
		eventService.addUserToEvent(userId, id);
		return String.format("redirect:/events/%d", id);
	}
	// update event
	@GetMapping("/events/{id}/edit")
    public String updateEvent(@ModelAttribute("event") Event event, Model model,
    		@PathVariable("id") Long id, HttpSession session) {
    	Long userId = (Long) session.getAttribute("userId");
    	User user = userService.findUser(userId);
    	if (userId == null) return "redirect:/";
    	Event thisEvent = eventService.findEvent(id);
//    	System.out.println(user.getId());
//    	System.out.println(thisEvent.getUser().getId());
//    	System.out.println(userId == (Long) thisEvent.getUser().getId());
    	if (!(user.getId() == thisEvent.getUser().getId())) return "redirect:/events/{id}";
    	List<Location> locations = locationService.allLocations();
    	model.addAttribute("userId", userId);
    	model.addAttribute("locations", locations);
    	model.addAttribute("event", thisEvent);
    	return "updateEvent";
    }
	@PutMapping("/events/{id}/edit")
	public String editEvent(@Valid @ModelAttribute("event") Event event, BindingResult result, HttpSession session) {
		eventService.createEvent(event, (Long) session.getAttribute("userId"), result);
		if (result.hasErrors()) return "updateEvent";
		else return "redirect:/dashboard";
	}
	// delete event
	@DeleteMapping("/events/{id}")
	public String destroyEvent(@PathVariable("id") Long id, HttpSession session) {
		Long userId = (Long) session.getAttribute("userId");
    	User user = userService.findUser(userId);
    	Event thisEvent = eventService.findEvent(id);
    	if (!(user.getId() == thisEvent.getUser().getId())) return "redirect:/events/{id}";
		eventService.deleteEvent(id);
		return "redirect:/dashboard";
	}
	
	
}