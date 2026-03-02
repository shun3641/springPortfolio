package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.model.Items;
import com.example.demo.model.Purchase;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.PurchaseUserRepository;

@Controller
@RequestMapping("/items")
public class PurchaseController {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private PurchaseUserRepository purchaseUserRepository;
    // 商品詳細 兼 購入画面の表示
    @GetMapping("/detail/{id}")
    public String showDetail(@PathVariable Integer id, Model model) {
        Items item = itemRepository.findById(id).orElseThrow();
        
        // Purchaseオブジェクトを初期化して、商品の値をセットしておく
        Purchase purchase = new Purchase();
        purchase.setName(item.getName());   // 商品名を引き継ぐ
        purchase.setPrice(item.getPrice()); // 単価を引き継ぐ
        purchase.setQuantity(1);            // デフォルト個数
        
        model.addAttribute("item", item);
        model.addAttribute("purchase", purchase);
        model.addAttribute("currentQuantity", item.getQuantity());
        return "purchaseView";
    }

    // 購入処理
    @PostMapping("/purchase/{id}")
    public String processPurchase(Model model,@PathVariable Integer id,
    		@Validated @ModelAttribute("purchase") Purchase target,
    		BindingResult result,
    		RedirectAttributes redirectAttributes) {
    	Items item = itemRepository.findById(id).orElseThrow();
    	if(result.hasErrors()) {
    		System.out.println("エラーが発生しました。");
    		model.addAttribute("org.springframework.validation.BindingResult.Purchase", result);
    		model.addAttribute("item", item);
    		return "purchaseView";
    	}
    	
    	//値段と合計金額、名前はdataベースからとることで改ざん不可にする
    	//itemはdata baseからとったレコードのこと
    	target.setId(null);
    	target.setName(item.getName());
    	
    	target.setPrice(item.getPrice());
    	target.setTotal(item.getPrice()*target.getQuantity());
    	System.out.println(item.getQuantity()-target.getQuantity());
    	
    	//もし在庫が-になるならDB保存はせず、購入画面を返す。つまりやり直し
    	if(item.getQuantity()-target.getQuantity()<0) {
    		redirectAttributes.addFlashAttribute("OutofStock", "在庫を超過しています。");
    		return "redirect:/items/detail/"+id;
    	}
    	
    	//購入情報を保存
    	purchaseUserRepository.save(target);
    	
    	//購入分を在庫から減らす
    	item.setQuantity(item.getQuantity()-target.getQuantity());
    	
    	//在庫を減らしたら保存
    	itemRepository.save(item);
    	
    	return "redirect:/items/successPurchase";
    }
    @GetMapping("/successPurchase")
    public String successPurchase() {
        return "successPurchase.html";
    }
}