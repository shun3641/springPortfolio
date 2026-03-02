package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.model.Purchase;

import jakarta.persistence.LockModeType;

public interface PurchaseUserRepository extends JpaRepository<Purchase, Integer>{
	//たぶん悲観ロック完了
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT p FROM Purchase p WHERE p.id = :id")
	Optional<Purchase> save(@Param("id") Integer id);
}
