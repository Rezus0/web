package com.example.web_2.baseEntity;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class ViewCountService {
    private final RedisTemplate<String, Object> template;

    public ViewCountService(RedisTemplate<String, Object> template) {
        this.template = template;
    }

    private static final String VIEW_COUNTER_KEY_PREFIX = "view:counter:";
    private static final String BRAND_PREFIX = "brand:";
    private static final String MODEL_PREFIX = "model:";
    private static final String OFFER_PREFIX = "offer:";
    private static final String USER_PREFIX = "user:";

    public void incrementBrandViewCounter(String entityId) {
        String key = BRAND_PREFIX + VIEW_COUNTER_KEY_PREFIX + entityId;
        template.opsForValue().increment(key);
    }

    public void incrementModelViewCounter(String entityId) {
        String key = MODEL_PREFIX + VIEW_COUNTER_KEY_PREFIX + entityId;
        template.opsForValue().increment(key);
    }

    public void incrementOfferViewCounter(String entityId) {
        String key = OFFER_PREFIX + VIEW_COUNTER_KEY_PREFIX + entityId;
        template.opsForValue().increment(key);
    }

    public void incrementUserViewCounter(String entityId) {
        String key = USER_PREFIX + VIEW_COUNTER_KEY_PREFIX + entityId;
        template.opsForValue().increment(key);
    }

    public Long geBrandViewCount(String brandId) {
        String key = BRAND_PREFIX + VIEW_COUNTER_KEY_PREFIX + brandId;
        return (Long) template.opsForValue().get(key);
    }

    public Integer getModelViewCount(String modelId) {
        String key = MODEL_PREFIX + VIEW_COUNTER_KEY_PREFIX + modelId;
        return (Integer) template.opsForValue().get(key);
    }

    public Long getOfferViewCount(String offerId) {
        String key = OFFER_PREFIX + VIEW_COUNTER_KEY_PREFIX + offerId;
        return (Long) template.opsForValue().get(key);
    }

    public Long getUserViewCount(String userId) {
        String key = USER_PREFIX + VIEW_COUNTER_KEY_PREFIX + userId;
        return (Long) template.opsForValue().get(key);
    }
}
