package com.cs201.g1t1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.*;
import org.springframework.data.repository.query.Param;
import java.util.List;
import com.cs201.g1t1.model.*;

@Repository
public interface BusinessRepository extends JpaRepository<Business, String> {

    @Query(nativeQuery=true, value ="select distinct city from cs201g1t1.business group by city;")
    List<String> getAllCity();
    @Query(nativeQuery=true, value ="select distinct postal_code from cs201g1t1.business where city = ?;")
    List<String> getAllPostalCode(String cityname);
    @Query(nativeQuery=true, value ="select business_id from cs201g1t1.business where city = ?;")
    List<Business> getBusinesses(String cityname);
    List<Business> findByCity(String name);

}
