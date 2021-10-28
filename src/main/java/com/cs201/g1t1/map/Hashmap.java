// package com.cs201.g1t1.map;
// import com.cs201.g1t1.repository.BusinessRepository;
// import java.util.List;
// import org.springframework.web.bind.annotation.RestController;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.boot.CommandLineRunner;
// import org.springframework.web.bind.annotation.GetMapping;
// import com.cs201.g1t1.model.Business;
// import com.cs201.g1t1.model.Category;
// import java.util.Iterator;
// import java.util.ArrayList;



// @RestController
// public class Hashmap {
//     @Autowired
//     private BusinessRepository businesses;
    
//     @Autowired
//     private Business business;

//     Logger logger = LoggerFactory.getLogger(Hashmap.class);

//     @GetMapping("/city")
//     public List<String> getCities(){
//         List<String> allCity = businesses.getAllCity();
//         return allCity;

//     }

//     @GetMapping("/postalCode")
//     public List<String> getPostalCode(){
//         List<String> postalCode = businesses.getAllPostalCode("Austin");
//         return postalCode;
//     }

//     @GetMapping("/businesses")
//     public List<Business> getBusiness(){
//         List<Business> b = businesses.findByCity("Austin");
//         return b;
//     }

//     @GetMapping("/")
//     public String getUniqueCategory(){
//         List<Business> bs = businesses.findByCity("Austin");// this one will the datas of businesses from Austin
//         ChainHashMap<Integer,Business> map = new ChainHashMap<Integer,Business>(1330,7); // should get the category size from the repositry

//         for(Business b : bs){
//             // Iterator categories = business.getCategories().iterator();
//             for(Category category: business.getCategories()){
//                 Integer cno = Integer.parseInt(category.getCategoryName()); // use as the key
//                 int hash = (int) ((Math.abs(cno.hashCode()*5 + 9) % 7) % 1330); // use as hash
//                 map.bucketPut(hash, cno, b);
//                 //UnsortedTableMap<Integer,Business> businessEntry = new unsortedTableMap<Integer,Business>;

//             }
            
//         }
//         //Iterator<Entry<Integer,Business>> iter = map.entrySet().iterator();
//         UnsortedTableMap<Integer,Business>[] buckets = map.getTable();

//         int max =0;
//         Category result = null;
//         for(Category category: business.getCategories()){
//             Integer cno = Integer.parseInt(category.getCategoryName());
//             int hash = (int) ((Math.abs(cno.hashCode()*5 + 9) % 7) % 1330);
//             UnsortedTableMap<Integer,Business> table = buckets[hash];
//             int size = table.size();
//             if(size > max){
//                 max = size;
//                 result = category;
//             }
//         }
//         if(result == null){
//             String ans = "no reuslt";
//             return ans;
//         }
//         return result.getCategoryName();

//     }    
    
// }
