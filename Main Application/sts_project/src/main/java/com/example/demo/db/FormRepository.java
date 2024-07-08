package com.example.demo.db;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FormRepository extends JpaRepository<Form, String>
{
	@Query("SELECT f FROM Form f WHERE f.status = ?2 AND f.owner_ID = ?1")
	List<Form> findByStatusAndOwnerId(String owner_ID, String status);
	@Query("SELECT f FROM Form f WHERE f.status = ?2 AND f.tax_ID = ?1")
	List<Form> findByStatusAndBuyerId(String tax_ID, String status);
	@Query("SELECT f FROM Form f WHERE f.form_ID = ?1")
	List<Form> findByFormId(String form_id);
}
