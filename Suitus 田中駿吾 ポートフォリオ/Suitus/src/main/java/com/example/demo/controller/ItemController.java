package com.example.demo.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.model.Deleteitems;
import com.example.demo.model.Items;
import com.example.demo.model.Users;
import com.example.demo.repository.DeleteItemRepository;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.ItemService;
import com.example.demo.service.UserService;

import jakarta.validation.Valid;


//相対パスの場合、思わぬ不具合になりやすいので、必ず絶対パスを使う
@Controller
@RequestMapping("/admin")
public class ItemController {

	@Autowired
	ItemRepository itemRepository;
	
	@Autowired
	DeleteItemRepository deleteItemRepository;
	
	@Autowired
	UserService userService;
	
	@Autowired
	ItemService itemService;
	
	@Autowired
	UserRepository userRepository;
	//Configクラスでスプリングコンテナに
	 
	@Value("${file.upload-dir}")
	private String uploadDir;
	
	@GetMapping("/application")
	public String application(
			Model model,
			@ModelAttribute("result") BindingResult results,
			RedirectAttributes redirectAttributes) throws IOException
	{
		
			model.addAttribute(BindingResult.MODEL_KEY_PREFIX + "Items", results);
			Items validItem = new Items();
			model.addAttribute("validItem", validItem);
		  //flash属性の設定により
		  //モデルに自動追加されたimagefileNameを取得
		  String imagefileName = (String) model.getAttribute("imagefileName");
		  
		  validItem.setImageurl(imagefileName);
		 
		  	//Itemidのフィールド最大値+1の連番
	      Integer maxItemid = itemService.SerialNumber();
	      System.out.println("maxItemid---"+ maxItemid);
	      if(maxItemid==null) {
	    	  maxItemid = 0;
	      }
	      validItem.setItemid(maxItemid+1);	  
	        
	        //id昇順にitemsテーブルを並び替え
	        model.addAttribute("items",itemRepository.findAllItemsOrderByIdAsc());

	        return "admin/application";
	}
	
	@PostMapping(value="/create",params="action")
	public String create(@RequestParam String action,Model model,RedirectAttributes redirectAttributes,
			@RequestParam("file") MultipartFile file,
			  @Validated @ModelAttribute("validItem") Items validitem,
			 BindingResult result) throws IOException 
	{	  System.out.println(action);
		  if(action.equals("upload")) {
			  System.out.println("画像ファイルを読み込みました。file---" + file.getOriginalFilename());
				  Map<String, String> result2 = handleFileUpload(file);
				    String imageuploadmessage = result2.get("imageuploadmessage");
				    String imagefileName = result2.get("imagefileName");
			  redirectAttributes.addFlashAttribute("imageuploadmessage", imageuploadmessage);
			  redirectAttributes.addFlashAttribute("imagefileName", imagefileName);
			  return "redirect:/admin/application";		  
		  }
		  model.addAttribute("items",itemRepository.findAllItemsOrderByIdAsc());
		  //商品の重複処理
		  if(!itemRepository.findByName(validitem.getName()).isEmpty()||
				  !itemRepository.findByItemid(validitem.getItemid()).isEmpty()) {
			  redirectAttributes.addFlashAttribute
			  ("getResult", 
			  result);
			  model.addAttribute("validItem", validitem);
			  model.addAttribute("duplication", "itemidまたは商品名の重複があります。");
			  return "admin/application";
		  }
		  // エラーがあった場合、データを保持したまま元のフォーム画面に戻る
		  if(result.hasErrors()) { 
			  model.addAttribute("validItem", validitem);
			  model.addAttribute("result",result);
			  return "admin/application";
		  }
	
	itemRepository.save(validitem);
	redirectAttributes.addFlashAttribute("message", "商品を追加しました。");
	return "redirect:/admin/application";			
	}
	
	@DeleteMapping("/{id}")
    public String deleteItem(@PathVariable Integer id,
    		@RequestParam String filename) {

		 itemService.deleteLogic(id,filename);
		 return "redirect:/admin/application";// 一覧画面にリダイレクト
    }
	 
	 public Map<String,String> handleFileUpload(
			 @RequestParam("file") MultipartFile file) throws IOException {
		 //キーと値　連想配列
		 Map<String, String> map = new HashMap<>();
		 
		Path path = Paths.get(uploadDir, file.getOriginalFilename());
		// 2.　正規表現パターンを用意する 
//	    String regex_Alphabet = "^[A-Za-z]+$" ; // アルファベットのみ
	    String regex_AlphaNum = "^[A-Za-z0-9._-[(][)] ]+$"; // 半角英数字+.のみ 
	    //受け取ったファイル名が全て半角英数字._-になっているか確認
	    Boolean checkPattern = checkLogic(regex_AlphaNum,file.getOriginalFilename());
	    if(!checkPattern) {
//	    	redirectAttributes.addFlashAttribute("imageuploadmessage", "エラー：画像名は半角英数字にしてください。");
	    	map.put("imageuploadmessage", "エラー：画像名は半角英数字にしてください。");
	    	return map;
	    }
	    Files.createDirectories(path.getParent());
      //画像ファイルのバイトデータを抽出
        Files.write(path, file.getBytes());
        try {
            Thread.sleep(3000); // ミリ秒数は調整する
        } catch (InterruptedException e) {
            System.out.println("待ち時間中に割り込みが発生しました。");
        }
//        System.out.println(redirectAttributes);
//        redirectAttributes.addFlashAttribute("imageuploadmessage", "画像ファイルのアップロードが完了しました。");
//        redirectAttributes.addFlashAttribute("imagefileName", "/uploads/" + file.getOriginalFilename());
        map.put("imageuploadmessage", "画像ファイルのアップロードが完了しました。");
        map.put("imagefileName", "/uploads/" + file.getOriginalFilename());
        return map;
    }
	 
	 public boolean checkLogic(String regex, String target) {
		    boolean result = true;
		    System.out.println("パターン対象の文字列---" + target);
		    if( target == null || target.isEmpty() ) return false ;
		    // 3. 引数に指定した正規表現regexがtargetにマッチするか確認する
		    Pattern p1 = Pattern.compile(regex); // 正規表現パターンの読み込み
		    Matcher m1 = p1.matcher(target); // パターンと検査対象文字列の照合
		    result = m1.matches(); // 照合結果をtrueかfalseで取得
		    return result;
		  }
	 
			 @GetMapping("/orderBycategory") 
			 public String orderBycategoryAsc(Model model,
			 @RequestParam("category") String category,
			 @ModelAttribute("validitem") Items validItem) { 
				 System.out.println("カテゴリー---" + category);
				 List<Items> Ordereditems = itemRepository.findByCategoryOrderByNameAsc(category);
				 System.out.println(itemRepository.findByCategoryOrderByNameAsc(category));
				 model.addAttribute("items",Ordereditems);
				 model.addAttribute("category", category);
				 return "admin/application";
			 }
			 
			 @GetMapping("/showAll")
			 public String showAll(Model model,
					 @ModelAttribute("validitem") Items validItem) {
				 List<Items> items = itemRepository.findAllItemsOrderByIdAsc();			 
				 model.addAttribute("items", items);
				 return "admin/application";
			 }
			 
			 @GetMapping("/orders")
				public String orders(Model model) {
					return "admin/orders";
				}
			 @GetMapping("/userlist")
				public String userlist(Model model) {
				 List<Users> users = userService.getUserListAsc();
				 model.addAttribute("users", users);
				 return "admin/userlist";
				}
			 
			 @GetMapping("/update")
			 public String update(Model model,
					 
					 @ModelAttribute("getOneItem") Items oneItem,
					 @ModelAttribute("validUitem") Items validUItem,
					 BindingResult result
					 ) {
				 validUItem = oneItem;
				 
				 System.out.println("validUItem---" + validUItem);
				 model.addAttribute("validUitem", validUItem);
				 List<Items> items = itemRepository.findAllItemsOrderByIdAsc();
				 model.addAttribute("items", items);
				 System.out.println(items);
				 
				 model.addAttribute(BindingResult.MODEL_KEY_PREFIX
				            + "Items", result);
				 
				 System.out.println("次行目update---");
				 if(model.getAttribute("oneItem")==null) {
					 model.addAttribute("oneItem", new Items());
				 }
				 
				 return "admin/update";
			 }
			 
			 @PostMapping("/findItemById")
			 public String findItemById(@RequestParam int inputId,
					 Model model,RedirectAttributes redirectAttributes
					 ) {
				 
				List<Items> item = itemRepository.findById(inputId);
				if(item!=null) {
				Items oneItem = item.get(0);
				 System.out.println("getOneItem---" + oneItem);
				 
				redirectAttributes.addFlashAttribute("oneItem", oneItem);
				 }
				Boolean isVisible = true;
				
				redirectAttributes.addFlashAttribute("isVisable", isVisible);
				redirectAttributes.addFlashAttribute("inputId", inputId);
				return "redirect:/admin/update#updateForm";
			 }
			 
				
				
			@PostMapping("/updateDo/{id}") public String updateDo( 
						@PathVariable Integer id,
						RedirectAttributes
				  redirectAttributes,
				  @Valid @ModelAttribute("validUitem") Items validUItem, BindingResult result) {
					  if (result.hasErrors()) {
					        // エラー処理
					        return "admin/update";
					    }
					    validUItem.setId(id);
					    itemRepository.save(validUItem);
					  	Boolean isVisible = false;
					  	
					  	redirectAttributes.addFlashAttribute("isVisible",isVisible);
				  return "redirect:/admin/update"; 
				  }
				 
			@GetMapping("/restorelist") 
			public String restorelist(Model model) {
				List<Deleteitems> deleteitems = deleteItemRepository.findAll();
				model.addAttribute("deleteitems", deleteitems);
				return "admin/restorelist";
			  }
			
			@PostMapping("/restore/{id}") 
			public String restore(@PathVariable Integer id,
					Model model) {
				try {
				Deleteitems deleteitem = deleteItemRepository.findById(id).get();
				Items item = new Items();
				
				//同じフィールドの値をコピーしてる
				BeanUtils.copyProperties(deleteitem,item);
				item.setId(null);
				//Itemテーブルに値を戻し、deleteItemテーブルからは削除、
				//これで復元を実現
				itemRepository.save(item);
				deleteItemRepository.deleteById(id);
				List<Deleteitems> deleteitems = deleteItemRepository.findAll();
				model.addAttribute("deleteitems", deleteitems);
				} catch(NoSuchElementException e) {
					System.out.println("再リクエストが行われました。");
				}
				return "admin/restorelist";
			  }
			
			@PostMapping("/imageupload/{id}")
			public String imageUpload(@PathVariable Integer id,
					@RequestParam("file") MultipartFile file,
					RedirectAttributes redirectAttributes) throws IOException {
				System.out.println("file---"+file);
				try {
				handleFileUpload(file);
				
				Items target = itemRepository.findById(id).get();
				target.setImageurl("/uploads/" + file.getOriginalFilename());
				itemRepository.save(target);
				} catch (Exception e) {
					
					return "redirect:/admin/update";
					// TODO: handle exception
				}
				
				return "redirect:/admin/update";
			}
			
				 
}