package com.example.demo;


import com.example.demo.Dao.AddressCount;
import com.example.demo.Dao.AddressCount2;
import com.example.demo.Dao.RealAddress;
import com.example.demo.Service.IAddressCount2Service;
import com.example.demo.Service.IAddressCountService;
import com.example.demo.Service.IRealAddressService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@Slf4j
@SpringBootTest
public class AddressTest {

    @Autowired
    IAddressCount2Service addressCount2Service;

    @Autowired
    IAddressCountService addressCountService;

    @Autowired
    IRealAddressService realAddressService;

    //将有效地址储存到real address
    @Test
    public void saveValidAddress(){
        List<AddressCount2> addresses1 = addressCount2Service.findAddressByCountAndValid(6);
        for (AddressCount2 address : addresses1) {
            RealAddress address1 = realAddressService.findAddress(address.getAddress());
            if(address1!=null){
                continue;
            }
            RealAddress realAddress=new RealAddress();
            realAddress.setAddress(address.getAddress());
            realAddressService.saveAddress(realAddress);
        }
    }


    //用real address去校验新地址
    @Test
    public void validAddress(){
        List<AddressCount> addressByCount = addressCountService.findAddressByCount(0);
        for (AddressCount addressCount : addressByCount) {
            if(addressCount.getValid()==null||addressCount.getValid().equals("")){
                RealAddress address1 = realAddressService.findAddress(addressCount.getAddress());
                if(address1!=null){
                    addressCount.setValid("T");
                    addressCountService.updateAddressCount(addressCount);
                }
            }
        }
    }



}
