package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "deleteitems")
public class Deleteitems {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "category")
	private String category;
	 
	@Column(name = "price")
	private Integer price;
	
	@Column(name = "quantity")
	private Integer quantity;
	
	//item_id,itemIdの場合だとjpaでsqlを読み込めずエラーになった。
	@Column(name = "itemid")
	private Integer itemid;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "info")
	private String info;
	
	@Column(name = "imageurl")
	private String imageurl;
	
	@Column(name = "period")
	private String period;
	
	@Column(name = "purchaseurl")
	private String purchaseurl;

}