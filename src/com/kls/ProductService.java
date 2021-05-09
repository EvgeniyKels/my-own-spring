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

    public void doSomethingForProfiling() {
        for (int i = 0; i < 10; i++) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
