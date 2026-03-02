package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Items;

@Repository
@EnableJpaRepositories
public interface ItemRepository extends JpaRepository<Items,Integer>{
	List<Items> findAll();
	List<Items> findByName(String name);
	List<Items> findById(int id);
	Items deleteByImageurl(String imageurl);
	List<Items> findByCategory(String category);
	List<Items> findByItemid(Integer itemid);
	List<Items> findByCategoryOrderByNameAsc(String category);
	
	//昇順にItemsのデータ群を並び替え
	@Query("SELECT u FROM Items u ORDER BY u.id ASC")
	List<Items> findAllItemsOrderByIdAsc();
}