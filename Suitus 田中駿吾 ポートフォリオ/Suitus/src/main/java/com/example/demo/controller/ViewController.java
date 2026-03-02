package com.example.demo.controller;

import com.example.demo.model.Items;
import com.example.demo.model.Purchase;

import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.PurchaseUserRepository;

import jakarta.persistence.EntityManager;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/")
public class ViewController {

	@Autowired
	ItemRepository itemRepository;

	@Autowired
	PurchaseUserRepository purchaseUserRepository;

	public static final String bindingResult = "org.springframework.validation.BindingResult.validPurchase";
	@Autowired
	private EntityManager entityManager;

	@GetMapping("/index")
	public String index() {
		return "index";
	}

	@GetMapping("/login")
	public String login(Model model) {
		return "login";
	}
	
	@GetMapping("/logoutView")
	public String logoutView(Model model) {
		return "logoutView";
	}
	


	@GetMapping("/purchase/{id}")
	public String purchase(@PathVariable("id") int id, Model model,
			@ModelAttribute("validPurchase") Purchase validPurchase, @ModelAttribute("result") BindingResult result) {

		List<Items> items = itemRepository.findById(id);

		// リストは一つしかないので０番目を取得し、その名前を取得
		validPurchase.setQuantity(1);
		model.addAttribute(bindingResult, result);
		model.addAttribute("validPurchase", validPurchase);

		model.addAttribute("items", items);
		return "purchase";
	}

	@GetMapping("/purchaseView/{id}")
	public String purchaseView(HttpServletRequest request,
			Model model, @PathVariable("id") int id,
			@ModelAttribute("validPurchase") Purchase validPurchase,
			@ModelAttribute("result") BindingResult result) {
		
		model.addAttribute(bindingResult, result);
		model.addAttribute("validPurchase", validPurchase);
		
		
        List<Items> items = itemRepository.findById(id);
		model.addAttribute("items", items);
		validPurchase.setName(items.get(0).getName());
		validPurchase.setPrice(items.get(0).getPrice());
		validPurchase.setQuantity(1);
		validPurchase.setTotal(validPurchase.getPrice()*1);
		/*
		 * System.out.println(validPurchase + "---" + result);
		 * model.addAttribute("validPurchase", validPurchase);
		 * model.addAttribute(bindingResult, result);
		 */
		return "purchaseView";
	}

	
	  @PostMapping("/purcahseDo/{id}") public String purcahseDo(
			  @PathVariable("id") int id,
			  Model model,RedirectAttributes redirectAttributes,	  
	  @Valid @ModelAttribute Purchase validPurchase, BindingResult result){
	  
	  List<Items> items = itemRepository.findById(id);
	  
	  if (result.hasErrors()) {
	    
      redirectAttributes.addFlashAttribute("result",result);
	  redirectAttributes.addFlashAttribute("validPurchase", validPurchase);
	  
	  System.out.println("PurchaseDoHasErrors: " + result);
	  
	  return "redirect:/purchaseView/" + id;
	  }
	//個数を合計金額に設定 単価×個数
	  validPurchase.setTotal(items.get(0).getPrice() *
			  validPurchase.getQuantity());
	  
	  //idを初期化 nullでないと保存時、自動採番が行われないため。
	  validPurchase.setId(null);
	  
	  System.out.println(
	  "モデルアトリビュート---" + validPurchase);
	  
	  model.addAttribute("message", "登録完了"); 
	  
	  System.out.println(validPurchase.getId());
	  System.out.println("PurchaseDoSuccess: " + validPurchase);
	  //受け取った商品idから商品情報を抽出 
	  purchaseUserRepository.save(validPurchase);
	  
	  return "successPurchase"; 
	  }
	 

}