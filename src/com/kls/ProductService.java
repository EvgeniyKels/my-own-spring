package com.kls;

import org.springfraemwork.beans.factory.annotation.Autowired;
import org.springfraemwork.beans.factory.annotation.Resource;
import org.springfraemwork.beans.factory.stereotype.Component;

@Component
public class ProductService {
    @Resource
    private PromotionService promotionService;

    public PromotionService getPromotionService() {
        return promotionService;
    }

    public void setPromotionService(PromotionService promotionService) {
        this.promotionService = promotionService;
    }
}
