package com.example.demo.model;

import org.hibernate.validator.constraints.Range;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "purchase")
public class Purchase {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	//java内で処理するため、この３つのフィールドはバリデーション不要
	private String name;
	
	private Integer price;

	private Integer total;
	
	@NotNull(message = "個数は必須です。")
	@Range(min = 0, max = 10,message = "1~9の数字で入力してください。")
	private Integer quantity;
	
	@NotEmpty(message = "住所は必須です。")
	@Pattern(
			  regexp = "^([一-龯ぁ-んァ-ヶ]+市[一-龯ぁ-んァ-ヶ]+町.*)$",
			  message = "住所は「○○市○○町」の形式で入力してください"
			)
	private String address;
	
	public boolean isAddress() {
		if(!getAddress().isBlank()) {
			return true;
		} else {
			return false;
		}
	}
}
