package com.cs201.g1t1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.*;
import org.springframework.data.repository.query.Param;

import com.cs201.g1t1.model.*;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {
    public List<Category> findAll();
}
