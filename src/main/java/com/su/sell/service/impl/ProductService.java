package com.su.sell.service.impl;

import com.su.sell.dataObject.ProductInfo;
import com.su.sell.dto.CartDto;
import com.su.sell.enums.ResultEnum;
import com.su.sell.exception.SellException;
import com.su.sell.repository.ProductInfoRepository;
import com.su.sell.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ProductService implements IProductService{
    @Autowired
    private ProductInfoRepository repository;
    @Override
    public void increaseStock(List<CartDto> cartDtoList) {

        for (CartDto cartDto : cartDtoList) {

        }
    }

    @Override
    public void decreseStock(List<CartDto> cartDtoList) {
        //查看库存是否够,如果够就剪掉,然后更新
        List <ProductInfo> productInfoList = new ArrayList<>();
        for (CartDto cartDto : cartDtoList) {
            ProductInfo productInfo = repository.findById(cartDto.getProductId()).get();
            if(productInfo==null){
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }
            int ret = productInfo.getProductStock() - cartDto.getProductQuantity();
            if(ret < 0){
                throw new SellException(ResultEnum.PRODUCT_STOCK_ERROR.getCode(),"商品: "+productInfo.getProductName()+"库存不足,请选择其他商品或减少数量QAQ");
            }
            productInfo.setProductStock(ret);
            productInfoList.add(productInfo);
        }
        repository.saveAll(productInfoList);
    }
}
