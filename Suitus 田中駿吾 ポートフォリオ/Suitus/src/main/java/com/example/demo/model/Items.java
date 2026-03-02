package com.example.demo.model;

import jakarta.persistence.Entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "items")
public class Items {
	
	  @Id
	  @GeneratedValue(strategy = GenerationType.IDENTITY) 
	  private Integer id;
	  
	  @NotEmpty(message = "名前は必須です。") 
	  private String name;
	  
	  @NotEmpty(message = "カテゴリーは必須です。") 
	  private String category;
	  
	  @NotNull(message = "値段は必須です。")
	  @PositiveOrZero(message = "正の数を入力してください。")
	  @Digits(message = "5桁までの有効な数値を入力してください", fraction = 0, integer = 5)
	  private Integer price;
	  
	  @NotNull(message = "在庫は必須です。")
	  
	  @PositiveOrZero(message = "正の数を入力してください。") 
	  private Integer quantity;
	  
	  @NotNull(message = "idは必須です。")
	  
	  @PositiveOrZero(message = "正の数を入力してください。")
	  //item_id,itemIdの場合だとjpaでsqlを読み込めずエラーになった。 private Integer itemid;
	  private Integer itemid;
	  
	  @NotEmpty(message = "説明は必須です。") 
	  private String description;
	  
	  @NotEmpty(message = "商品情報は必須です。") 
	  private String info;
	  
	  @NotEmpty(message = "最初に画像をアップロードしてください。ファイルを選択のところです。") 
	  private String
	  imageurl;
	  
	  private String period;
	  
	  private String purchaseurl;
	  
	  public String getPeriod() { return period; }
	  
	  public void setPeriod(String period) { this.period = period; }
	  
	  
	  public String getPurchaseurl() { return purchaseurl; }
	  
	  public void setPurchaseurl(String purchaseurl) { this.purchaseurl =
	  purchaseurl; }
	 

}