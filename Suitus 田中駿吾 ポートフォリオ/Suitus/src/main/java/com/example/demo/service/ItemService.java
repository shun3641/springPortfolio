package com.example.demo.service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Items;
import com.example.demo.model.Users;
import com.example.demo.repository.ItemRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

@Service
public class ItemService {
	
	@Autowired
    private EntityManager entityManager;
	
	@Autowired
	ItemRepository itemRepository;
	
	public List<Items> showItemAll() {
		return itemRepository.findAll();
	}
	
	public Integer SerialNumber() {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Integer> query = cb.createQuery(Integer.class);
        Root<Items> root = query.from(Items.class);
        query.select(cb.max(root.get("itemid")));
        Integer maxItemid = entityManager.createQuery(query).getSingleResult();
        
		return maxItemid;
	}

}
