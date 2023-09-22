package com.darrenlai.bobabuddies.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.darrenlai.bobabuddies.models.Comment;

@Repository
public interface CommentRepository extends CrudRepository<Comment, Long>{
	
}