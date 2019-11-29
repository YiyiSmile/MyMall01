package indi.tom.mymall01.backmanage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import indi.tom.mymall01.backmanage.mapper.SkuAttrValueMapper;
import indi.tom.mymall01.backmanage.mapper.SkuImageMapper;
import indi.tom.mymall01.backmanage.mapper.SkuInfoMapper;
import indi.tom.mymall01.backmanage.mapper.SkuSaleAttrValueMapper;
import indi.tom.mymall01.bean.SkuAttrValue;
import indi.tom.mymall01.bean.SkuImage;
import indi.tom.mymall01.bean.SkuInfo;
import indi.tom.mymall01.bean.SkuSaleAttrValue;
import indi.tom.mymall01.interfaces.SkuService;
import indi.tom.mymall01.util.RedisUtil;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Author Tom
 * @Date 2019/11/20 16:08
 * @Version 1.0
 * @Description
 */
@Service
public class SkuServiceImpl implements SkuService {
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    SkuInfoMapper skuInfoMapper;
    @Autowired
    SkuAttrValueMapper skuAttrValueMapper;
    @Autowired
    SkuImageMapper skuImageMapper;
    @Autowired
    SkuSaleAttrValueMapper skuSaleAttrValueMapper;

    private static final String SKU_PREFIX = "sku:";
    private static final String SKU_LOCK_SUFFIX = ":lock";
    private static final String SKU_INFO_SUFFIX = ":info";
    private static final int SKU_EXPIRE_SEC = 100;


    @Override
    @Transactional
    public String saveSku(SkuInfo skuInfo) {
        //保存sku基本信息
        skuInfoMapper.insertSelective(skuInfo);
        Integer skuId = skuInfo.getId();
        //保存sku图片表
        List<SkuImage> skuImgList = skuInfo.getSkuImgList();
        for (SkuImage skuImage : skuImgList) {
            skuImage.setSkuId(skuId);
            skuImageMapper.insert(skuImage);
        }
        //保存sku 平台属性值表
        List<SkuAttrValue> skuAttrValueList = skuInfo.getSkuAttrValueList();
        for (SkuAttrValue skuAttrValue : skuAttrValueList) {
            skuAttrValue.setSkuId(skuId);
            skuAttrValueMapper.insert(skuAttrValue);
        }

        //保存sku销售属性值表
        List<SkuSaleAttrValue> skuSaleAttrValueList = skuInfo.getSkuSaleAttrValueList();
        for (SkuSaleAttrValue skuSaleAttrValue : skuSaleAttrValueList) {
            skuSaleAttrValue.setSkuId(skuId);
            skuSaleAttrValueMapper.insert(skuSaleAttrValue);
        }

        return "success";
    }

    @Override
    public String addImage(SkuImage skuImage) {
        skuImageMapper.insertSelective(skuImage);
        return "success";
    }

    @Override
    public SkuInfo getSkuInfoByIdDB(String skuId) {

        System.out.println(Thread.currentThread() + "查询数据库！");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Integer skuId1 = Integer.parseInt(skuId);

        SkuInfo skuInfo = skuInfoMapper.selectByPrimaryKey(skuId1);
        if(skuInfo == null){
            return skuInfo;
        }
        SkuAttrValue skuAttrValue = new SkuAttrValue();
        skuAttrValue.setSkuId(skuId1);
        List<SkuAttrValue> skuAttrValueList = skuAttrValueMapper.select(skuAttrValue);

        SkuImage skuImage = new SkuImage();
        skuImage.setSkuId(skuId1);
        List<SkuImage> skuImageList = skuImageMapper.select(skuImage);

        SkuSaleAttrValue skuSaleAttrValue = new SkuSaleAttrValue();
        skuSaleAttrValue.setSkuId(skuId1);
        List<SkuSaleAttrValue> skuSaleAttrValueList = skuSaleAttrValueMapper.select(skuSaleAttrValue);

        skuInfo.setSkuAttrValueList(skuAttrValueList);
        skuInfo.setSkuImgList(skuImageList);
        skuInfo.setSkuSaleAttrValueList(skuSaleAttrValueList);
        return skuInfo;
    }

    @Override
    public Map getSkuValueIdsMap(String spuId) {
        List<Map> mapList = skuSaleAttrValueMapper.getSaleAttrValuesBySpu(spuId);
        Map skuValueIdsMap = new HashMap();

        for (Map map : mapList) {
            String skuId = (Long) map.get("sku_id") + "";
            String valueIds = (String) map.get("value_ids");
            skuValueIdsMap.put(valueIds, skuId);


        }
        return skuValueIdsMap;
    }



    //使用redis原生命令手工实现分布式锁
    @Override
    public SkuInfo getSkuInfoByIdRedis(String skuId) throws Exception {
        SkuInfo skuInfo = null;
        Jedis jedis = redisUtil.getJedis();
        String skuInfoString = jedis.get(SKU_PREFIX+ skuId + SKU_INFO_SUFFIX);
        //redis中没查到
        if (skuInfoString == null) {

            System.err.println(Thread.currentThread() + "没命中缓存");
            String lockKey = SKU_PREFIX + skuId + SKU_LOCK_SUFFIX;
            String token = UUID.randomUUID().toString();
            String result = jedis.set(lockKey, token, "NX", "EX", 100);
            //获取锁成功

            if("OK".equals(result)) {
                System.out.println(Thread.currentThread() + "获取锁成功");
                skuInfo = getSkuInfoByIdDB(skuId);
                if (skuInfo == null) {
                    skuInfoString = "empty";
                } else {
                    skuInfoString = JSON.toJSONString(skuInfo);
                }
                //设置缓存
                System.err.println(Thread.currentThread() + "设置缓存");
                jedis.set(SKU_PREFIX + skuId + SKU_INFO_SUFFIX, skuInfoString);
            }else{//获取锁失败
                System.out.println(Thread.currentThread() + "获取锁失败");
                Thread.sleep(3000);
                skuInfo = getSkuInfoById(skuId);
            }
        } else {//redis中找到
            System.out.println(Thread.currentThread() + "命中缓存");
            if("empty".equals(skuInfoString)){
                throw new Exception("Sku ID" + skuId + "不存在！");
            }
            skuInfo = JSON.parseObject(skuInfoString, SkuInfo.class);
        }
        jedis.close();


        return skuInfo;
    }

    //使用redisson来实现分布式锁
    @Override
    public SkuInfo getSkuInfoById(String skuId) throws Exception {
        SkuInfo skuInfoResult = null;
        Jedis jedis = redisUtil.getJedis();
        String skuInfoKey = SKU_PREFIX + skuId + SKU_INFO_SUFFIX;
        String skuInfoResultString = jedis.get(skuInfoKey);
        String skuLockKey = SKU_PREFIX + skuId + SKU_LOCK_SUFFIX;
        //命中缓存，但是是"empty"，表示sku id不存在
        if("empty".equals(skuInfoResultString)){
            System.out.println(Thread.currentThread() + "命中缓存，值是empty");
            //这里抛出异常不管用，会被dubbo捕捉，给dubbo的consumer返回的还是null值。
            //所以如果是empty，可以什么都不做，返回空的skuInfo就可以。
            throw new Exception("Sku ID" + skuId + "不存在！");
        }else if(null == skuInfoResultString){//没有命中
            System.out.println(Thread.currentThread() + "没有命中缓存");
            //获取锁
            Config config = new Config();
            config.useSingleServer().setAddress("redis://celvpvm16459.us.oracle.com:6379");
            RedissonClient redissonClient = Redisson.create(config);
            RLock lock = redissonClient.getLock(skuLockKey);
//            lock.lock(10, TimeUnit.SECONDS);
            boolean locked = lock.tryLock(10, 5, TimeUnit.SECONDS);
            if(locked) {
                System.out.println(Thread.currentThread() + "获取到锁");
                skuInfoResultString = jedis.get(skuInfoKey);
                if(skuInfoResultString == null) {
                    skuInfoResult = getSkuInfoByIdDB(skuId);
                    //写缓存
                    System.err.println("更新缓存");
                    if (skuInfoResult == null) {
                        jedis.set(skuInfoKey, "empty");
                    } else {
                        jedis.set(skuInfoKey, JSON.toJSONString(skuInfoResult));
                    }
                }else{//命中缓存
                    System.out.println(Thread.currentThread() + "命中缓存");
                    if("empty".equals(skuInfoResultString)){
                        throw new Exception("Sku ID" + skuId + "不存在！");
                    }else{
                        skuInfoResult = JSON.parseObject(skuInfoResultString, SkuInfo.class);
                    }
                }
            }
            lock.unlock();

        }else{//命中缓存，并且不是"empty"(sku id存在)
            System.out.println(Thread.currentThread() + "命中缓存，值不是empty");
            skuInfoResult = JSON.parseObject(skuInfoResultString, SkuInfo.class);
        }

        jedis.close();
        return skuInfoResult;
    }

    }
