package com.darrenlai.bobabuddies.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.darrenlai.bobabuddies.models.Location;
import com.darrenlai.bobabuddies.repositories.LocationRepository;

@Service
public class LocationService {
	@Autowired
	private LocationRepository locationRepository;
	
	public List<Location> allLocations() {
        return (List<Location>) locationRepository.findAll();
    }
}