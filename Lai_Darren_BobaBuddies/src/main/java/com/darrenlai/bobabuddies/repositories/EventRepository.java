package com.darrenlai.bobabuddies.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.darrenlai.bobabuddies.models.Event;

@Repository
public interface EventRepository extends CrudRepository<Event, Long>, PagingAndSortingRepository<Event, Long>{
	
	List<Event> findEventsByUserId(Long userId);
	
	@Query(value="SELECT * FROM events_users WHERE event_id = ?1 AND user_id =?2 ;", nativeQuery=true)
	List<?> getUserInEvent(Long eventId, Long userId);
}