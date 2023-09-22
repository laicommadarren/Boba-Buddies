package com.darrenlai.bobabuddies.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.darrenlai.bobabuddies.models.Comment;
import com.darrenlai.bobabuddies.services.CommentService;
import com.darrenlai.bobabuddies.services.EventService;


import jakarta.validation.Valid;

@Controller
public class CommentController {
	@Autowired
	private CommentService commentService;
	@Autowired
	private EventService eventService;
	
	@PostMapping("/comments/{eventId}/new")
	public String postComment(@Valid @ModelAttribute("comment") Comment comment, BindingResult result, @PathVariable("eventId") Long eventId, Model model) {
		commentService.createComment(comment, result);
		model.addAttribute("event", eventService.findEvent(eventId));
		model.addAttribute("eventService", eventService);
		if (result.hasErrors()) return "oneEvent";
		return String.format("redirect:/events/%d", eventId);
	}
}