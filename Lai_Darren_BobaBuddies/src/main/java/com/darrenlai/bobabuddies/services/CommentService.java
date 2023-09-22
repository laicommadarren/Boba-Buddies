package com.darrenlai.bobabuddies.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.darrenlai.bobabuddies.models.Comment;
import com.darrenlai.bobabuddies.repositories.CommentRepository;

@Service
public class CommentService {
	@Autowired
	private CommentRepository commentRepository;
	
	public Comment createComment(Comment comment, BindingResult result) {
		if(result.hasErrors()) return null;
		return commentRepository.save(comment);
	}
}
