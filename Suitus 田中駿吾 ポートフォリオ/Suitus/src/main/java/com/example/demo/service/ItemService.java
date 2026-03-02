package com.example.demo.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.model.Deleteitems;
import com.example.demo.model.Items;
import com.example.demo.repository.DeleteItemRepository;
import com.example.demo.repository.ItemRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

@Service
public class ItemService {
	
	@Autowired
    private EntityManager entityManager;
	
	@Autowired
	ItemRepository itemRepository;
	
	@Autowired
	DeleteItemRepository deleteItemRepository;
	
	@Value("${file.upload-dir}")
	 private String uploadDir;
	
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
	
	public void deleteLogic(@PathVariable Integer id,
    		@RequestParam String filename) {
		//String filepath = filename.substring(filename.lastIndexOf('/') + 1);
		//System.out.println(filepath);
		Items item = itemRepository.findById(id).get();
		Deleteitems deleteitems = new Deleteitems();
		BeanUtils.copyProperties(item, deleteitems);
		
		//連番を機能させるため、idをnullに初期化
		deleteitems.setId(null);
		System.out.println(deleteitems);
		deleteItemRepository.save(deleteitems);
		itemRepository.deleteById(id);
//		Path path = Paths.get(uploadDir,filepath);
//		try {
//			Files.delete(path);
//			Thread.sleep(3000);
//		} catch (IOException | InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
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

public String imageFileUploader(RedirectAttributes redirectAttributes,
		@RequestParam("file") MultipartFile file) throws IOException {
	Path path = Paths.get(uploadDir, file.getOriginalFilename());
	
	// 2.　正規表現パターンを用意する 
	//    String regex_Alphabet = "^[A-Za-z._-[(][)]]+$" ; // アルファベットのみ
    String regex_AlphaNum = "^[A-Za-z0-9._-[(][)] ]+$"; // 半角英数字+.のみ 
    
    //受け取ったファイル名が全て半角英数字._-になっているか確認
    Boolean checkPattern = checkLogic(regex_AlphaNum,file.getOriginalFilename());
    if(!checkPattern) {
    	redirectAttributes.addFlashAttribute("imageuploadmessage", "エラー：画像名は半角英数字にしてください。");
    	return "redirect:/admin/application";
    }
    
    //空の場合は画像ファイル用のディレクトリを生成
    Files.createDirectories(path.getParent());
    //画像ファイルのバイトデータを抽出
    Files.write(path, file.getBytes());
    try {
        Thread.sleep(3000); // ミリ秒数は調整する
    } catch (InterruptedException e) {
        System.out.println("待ち時間中に割り込みが発生しました。");
    }
	return regex_AlphaNum;
}
}