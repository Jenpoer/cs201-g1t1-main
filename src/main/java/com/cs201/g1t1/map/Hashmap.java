package com.cs201.g1t1.map;
import com.cs201.g1t1.repository.BusinessRepository;
import com.cs201.g1t1.repository.CategoryRepository;
import java.util.List;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.web.bind.annotation.GetMapping;
import com.cs201.g1t1.model.Business;
import com.cs201.g1t1.model.Category;
import java.util.Iterator;
import java.util.ArrayList;



@RestController
public class Hashmap {
    @Autowired
    private BusinessRepository businesses;
    
    @Autowired
    private  CategoryRepository categories;

    Logger logger = LoggerFactory.getLogger(Hashmap.class);

    @GetMapping("/city")
    public List<String> getCities(){
        List<String> allCity = businesses.getAllCity();
        return allCity;

    }

    @GetMapping("/postalCode")
    public List<String> getPostalCode(){
        List<String> postalCode = businesses.getAllPostalCode("Austin");
        return postalCode;
    }

    @GetMapping("/businesses")
    public List<Business> getBusiness(){
        List<Business> b = businesses.findByCity("Concord");
        return b;
    }

    @GetMapping("/unique")
    public String getUniqueCategory(){
        List<Business> bs = businesses.findByCity("Concord");// this one will the datas of businesses from Austin
        ChainHashMap<String,Business> map = new ChainHashMap<String,Business>(1330); // should get the category size from the repositry
        ArrayList<String> c = new ArrayList<>();
        final long startTime = System.currentTimeMillis();

        for(Business b : bs){
            
            // Iterator categories = business.getCategories().iterator();
            for(Category category: b.getCategories()){
                String cno = category.getCategoryName(); // use as the key
                int hash = (int) ((Math.abs(cno.hashCode())) % 1330); // use as hash
                map.bucketPut(hash, b.getBusinessId(), b);
                if(!c.contains(cno)){
                    c.add(cno);
                }
                //logger.info(cno);
                //logger.info(Integer.toString(hash));
                //UnsortedTableMap<Integer,Business> businessEntry = new unsortedTableMap<Integer,Business>;

            }
            
        }
        //Iterator<Entry<Integer,Business>> iter = map.entrySet().iterator();
        UnsortedTableMap<String,Business>[] buckets = map.getTable();
        //logger.info(Integer.toString(buckets.length));
        int max =0;
        String result = null;
        for(String cno: c){
            int hash = (int) ((Math.abs(cno.hashCode())) % 1330);
            if(buckets[hash] != null){
                //logger.info(cno);
                 UnsortedTableMap<String,Business> table = buckets[hash];
                int size = table.size();
                //logger.info(Integer.toString(size));

            if(size > max){
                max = size;
                result = cno;
                //logger.info(cno);
                //logger.info(Integer.toString(max));
            }
            }
           
        }
        final long endTime = System.currentTimeMillis();
        logger.info("Total execution time: " + (endTime - startTime));
        if(result == null){
            String ans = "no reuslt";
            return ans;
        }
        return result;

    }    
    
}
