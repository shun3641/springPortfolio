package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.Items;
import com.example.demo.repository.ItemRepository;

@Controller
public class ItemsAi {
	
	@Autowired
	ItemRepository itemRepository;
	@GetMapping("/items")
	public String showItemList(@RequestParam(required = false) String category, Model model) {
	    List<Items> items;
	    
	    if (category != null && !category.isEmpty()) {
	        // カテゴリーが指定されている場合
	        items = itemRepository.findByCategory(category);
	        model.addAttribute("selectedCategory", category);
	    } else {
	        // 指定がない場合は全件表示
	        items = itemRepository.findAll();
	        model.addAttribute("selectedCategory", "すべて");
	    }
	    
	    model.addAttribute("items", items);
	    model.addAttribute("itemCount", items.size());
	    return "items";
	}
}
