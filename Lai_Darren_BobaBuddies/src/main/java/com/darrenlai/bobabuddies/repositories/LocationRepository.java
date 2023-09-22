package com.darrenlai.bobabuddies.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.darrenlai.bobabuddies.models.Location;

@Repository
public interface LocationRepository extends CrudRepository<Location, String>{
	
}