package com.yangqi.recommendedsystem.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yangqi.recommendedsystem.common.BusinessException;
import com.yangqi.recommendedsystem.common.EmBusinessError;
import com.yangqi.recommendedsystem.dal.ShopModelMapper;
import com.yangqi.recommendedsystem.model.CategoryModel;
import com.yangqi.recommendedsystem.model.SellerModel;
import com.yangqi.recommendedsystem.model.ShopModel;
import com.yangqi.recommendedsystem.recommended.RecommendService;
import com.yangqi.recommendedsystem.recommended.RecommendSortService;
import com.yangqi.recommendedsystem.service.CategoryService;
import com.yangqi.recommendedsystem.service.SellerService;
import com.yangqi.recommendedsystem.service.ShopService;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xiaoer
 * @date 2020/2/23 21:34
 */
@Service
public class ShopServiceImpl implements ShopService {
    @Autowired
    private ShopModelMapper shopModelMapper;

    @Autowired
    private RecommendService recommendService;

    @Autowired
    private RecommendSortService recommendSortService;

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SellerService sellerService;

    @Override
    @Transactional
    public ShopModel create(ShopModel shopModel) throws BusinessException {
        shopModel.setCreatedAt(new Date());
        shopModel.setUpdatedAt(new Date());

        // 检验商家是否存在正确
        SellerModel sellerModel = sellerService.get(shopModel.getSellerId());
        if (sellerModel == null) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "商户不存在");
        }
        if (sellerModel.getDisabledFlag().intValue() == 1) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "商户已禁用");
        }

        // 检验类目
        CategoryModel categoryModel = categoryService.get(shopModel.getCategoryId());
        if (categoryModel == null) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "类目不存在");
        }

        shopModelMapper.insertSelective(shopModel);

        return get(shopModel.getId());
    }

    @Override
    public ShopModel get(Integer id) {
        ShopModel shopModel = shopModelMapper.selectByPrimaryKey(id);
        if (shopModel == null) {
            return null;
        }
        shopModel.setSellerModel(sellerService.get(shopModel.getSellerId()));
        shopModel.setCategoryModel(categoryService.get(shopModel.getCategoryId()));
        return shopModel;
    }

    @Override
    public List<ShopModel> selectAll() {
        List<ShopModel> shopModels = shopModelMapper.selectAll();
        shopModels.forEach(shopModel -> {
            shopModel.setSellerModel(sellerService.get(shopModel.getSellerId()));
            shopModel.setCategoryModel(categoryService.get(shopModel.getCategoryId()));
        });
        return shopModels;
    }

    @Override
    public List<ShopModel> recommend(BigDecimal longitude, BigDecimal latitude) {
        // List<ShopModel> shopModels = shopModelMapper.recommend(longitude, latitude);
        // shopModels.forEach(shopModel -> {
        //     shopModel.setSellerModel(sellerService.get(shopModel.getSellerId()));
        //     shopModel.setCategoryModel(categoryService.get(shopModel.getCategoryId()));
        // });
        // return shopModels;

        List<Integer> shopIdList = recommendService.recall(148);
        // 排序
        shopIdList = recommendSortService.sort(shopIdList, 148);
        List<ShopModel>  shopModels = shopIdList.stream().map(id -> {
            ShopModel shopModel = get(id);
            shopModel.setIconUrl("/static/image/shopcover/xchg.jpg");
            shopModel.setDistance(100);
            return shopModel;
        }).collect(Collectors.toList());
        return shopModels;
    }

    @Override
    public List<Map<String, Object>> searchGroupByTags(String keyword, Integer categoryId, String tags) {
        return shopModelMapper.searchGroupByTags(keyword, categoryId, tags);
    }

    @Override
    public Integer countAllShop() {
        return shopModelMapper.countAllShop();
    }

    @Override
    public List<ShopModel> search(BigDecimal longitude, BigDecimal latitude, String keyword, Integer categoryId, Integer orderby,
                                  String tags) {
        List<ShopModel> shopModels = shopModelMapper.search(longitude, latitude, keyword, categoryId, orderby, tags);
        shopModels.forEach(shopModel -> {
            shopModel.setSellerModel(sellerService.get(shopModel.getSellerId()));
            shopModel.setCategoryModel(categoryService.get(shopModel.getCategoryId()));
        });
        return shopModels;
    }

    @Override
    public Map<String, Object> searchEs(BigDecimal longitude, BigDecimal latitude, String keyword, Integer categoryId, Integer orderby,
                                        String tags) throws IOException {
        Map<String, Object> result = new HashMap<>();

        // SearchRequest searchRequest = new SearchRequest("shop");
        // SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // searchSourceBuilder.query(QueryBuilders.matchQuery("name", keyword));
        // searchSourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        // searchRequest.source(searchSourceBuilder);
        //
        // // 存储查询到的 doucumentId
        // List<Integer> shopIdList = new ArrayList<>();
        //
        // SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        // SearchHit[] hits = searchResponse.getHits().getHits();
        // for (SearchHit hit : hits) {
        //     shopIdList.add(new Integer(hit.getSourceAsMap().get("id").toString()));
        // }

        // List<ShopModel> shopModelList = shopIdList.stream().map(this::get).collect(Collectors.toList());

        Request request = new Request("GET", "/shop/_search");

        // String requestJson = "{\n" +
        //         "  \"_source\": \"*\",\n" +
        //         "  \"script_fields\": {\n" +
        //         "    \"distance\": {\n" +
        //         "      \"script\": {\n" +
        //         "        \"source\": \"haversin(lat, lon, doc['location'].lat, doc['location'].lon)\",\n" +
        //         "        \"lang\": \"expression\",\n" +
        //         "        \"params\": {\n" +
        //         "          \"lat\": " + latitude.toString() +",\n" +
        //         "          \"lon\": " + longitude.toString() + "\n" +
        //         "        }\n" +
        //         "      }\n" +
        //         "    }\n" +
        //         "  },\n" +
        //         "  \"query\": {\n" +
        //         "    \"function_score\": {\n" +
        //         "      \"query\": {\n" +
        //         "        \"bool\": {\n" +
        //         "          \"must\": [\n" +
        //         "            {\n" +
        //         "              \"match\": {\n" +
        //         "                \"name\": {\n" +
        //         "                  \"query\": \"" + keyword + "\",\n" +
        //         "                  \"boost\": 0.1\n" +
        //         "                }\n" +
        //         "              }\n" +
        //         "            },\n" +
        //         "            {\n" +
        //         "              \"term\": {\n" +
        //         "                \"seller_disabled_flag\": {\n" +
        //         "                  \"value\": \"0\"\n" +
        //         "                }\n" +
        //         "              }\n" +
        //         "            }\n" +
        //         "          ]\n" +
        //         "        }\n" +
        //         "      },\n" +
        //         "      \"functions\": [\n" +
        //         "        {\n" +
        //         "          \"gauss\": {\n" +
        //         "            \"location\": {\n" +
        //         "              \"origin\": \"" + latitude.toString() + ", " + longitude.toString() + "\",\n" +
        //         "              \"scale\": \"100km\",\n" +
        //         "              \"offset\": \"0km\",\n" +
        //         "              \"decay\": 0.5\n" +
        //         "            }\n" +
        //         "          },\n" +
        //         "          \"weight\": 9\n" +
        //         "        },\n" +
        //         "        {\n" +
        //         "          \"field_value_factor\": {\n" +
        //         "            \"field\": \"remark_score\"\n" +
        //         "          },\n" +
        //         "          \"weight\": 0.2\n" +
        //         "        },\n" +
        //         "        {\n" +
        //         "          \"field_value_factor\": {\n" +
        //         "            \"field\": \"seller_remark_score\"\n" +
        //         "          },\n" +
        //         "          \"weight\": 0.1\n" +
        //         "        }\n" +
        //         "      ],\n" +
        //         "      \"score_mode\": \"sum\",\n" +
        //         "      \"boost_mode\": \"sum\"\n" +
        //         "    }\n" +
        //         "  },\n" +
        //         "  \"sort\": [\n" +
        //         "    {\n" +
        //         "      \"_score\": {\n" +
        //         "        \"order\": \"desc\"\n" +
        //         "      }\n" +
        //         "    }\n" +
        //         "  ]\n" +
        //         "}";

        // 构建请求
        JSONObject jsonRequestObj = new JSONObject();
        // 构建 source 部分
        jsonRequestObj.put("_source", "*");

        //构建自定义距离字段
        jsonRequestObj.put("script_fields", new JSONObject());
        jsonRequestObj.getJSONObject("script_fields").put("distance", new JSONObject());
        jsonRequestObj.getJSONObject("script_fields").getJSONObject("distance").put("script", new JSONObject());
        jsonRequestObj.getJSONObject("script_fields").getJSONObject("distance").getJSONObject("script").put("source", "haversin(lat, lon," +
                " doc['location'].lat, doc['location'].lon)");
        jsonRequestObj.getJSONObject("script_fields").getJSONObject("distance").getJSONObject("script").put("lang", "expression");
        jsonRequestObj.getJSONObject("script_fields").getJSONObject("distance").getJSONObject("script").put("params", new JSONObject());
        jsonRequestObj.getJSONObject("script_fields").getJSONObject("distance").getJSONObject("script").getJSONObject("params").put("lat"
                , latitude);
        jsonRequestObj.getJSONObject("script_fields").getJSONObject("distance").getJSONObject("script").getJSONObject("params").put("lon"
                , longitude);

        // 词性解析
        Map<String, Object> partOfSpeech = analyzeCategoryKeyword(keyword);
        boolean isAffectFilter = false;
        boolean isAffectOrder = true;

        // 构建 query 字段
        jsonRequestObj.put("query", new JSONObject());

        // 构建 function_score
        jsonRequestObj.getJSONObject("query").put("function_score", new JSONObject());

        //构建function score内的query
        jsonRequestObj.getJSONObject("query").getJSONObject("function_score").put("query", new JSONObject());
        jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").put("bool", new JSONObject());
        jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool").put("must",
                new JSONArray());
        jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool").getJSONArray(
                "must").add(new JSONObject());

        // 设置变量表示搜索条件在 jsonArray 中的偏移量
        int queryIndex = 0;

        // 构建 match query
        if (partOfSpeech.keySet().size() > 0 && isAffectFilter) {
            jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool").getJSONArray("must").getJSONObject(queryIndex).put("bool", new JSONObject());
            jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool").getJSONArray("must").getJSONObject(queryIndex).getJSONObject("bool").put("should", new JSONArray());

            int filterQueryIndex = 0;
            jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool").getJSONArray("must").getJSONObject(queryIndex).getJSONObject("bool").getJSONArray("should").add(new JSONObject());
            jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool").getJSONArray("must").getJSONObject(queryIndex).getJSONObject("bool").getJSONArray("should").getJSONObject(filterQueryIndex).put("match", new JSONObject());
            jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool").getJSONArray("must").getJSONObject(queryIndex).getJSONObject("bool").getJSONArray("should").getJSONObject(filterQueryIndex).getJSONObject("match").put("name", new JSONObject());
            jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool").getJSONArray("must").getJSONObject(queryIndex).getJSONObject("bool").getJSONArray("should").getJSONObject(filterQueryIndex).getJSONObject("match").getJSONObject("name").put("query", keyword);
            jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool").getJSONArray("must").getJSONObject(queryIndex).getJSONObject("bool").getJSONArray("should").getJSONObject(filterQueryIndex).getJSONObject("match").getJSONObject("name").put("boost", 0.1);

            for (String key : partOfSpeech.keySet()) {
                filterQueryIndex++;
                Integer cixingCategoryId = (Integer) partOfSpeech.get(key);
                jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool").getJSONArray("must").getJSONObject(queryIndex).getJSONObject("bool").getJSONArray("should").add(new JSONObject());
                jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool").getJSONArray("must").getJSONObject(queryIndex).getJSONObject("bool").getJSONArray("should").getJSONObject(filterQueryIndex).put("term", new JSONObject());
                jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool").getJSONArray("must").getJSONObject(queryIndex).getJSONObject("bool").getJSONArray("should").getJSONObject(filterQueryIndex).getJSONObject("term").put("category_id", new JSONObject());
                jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool").getJSONArray("must").getJSONObject(queryIndex).getJSONObject("bool").getJSONArray("should").getJSONObject(filterQueryIndex).getJSONObject("term").getJSONObject("category_id").put("value", cixingCategoryId);
                jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool").getJSONArray("must").getJSONObject(queryIndex).getJSONObject("bool").getJSONArray("should").getJSONObject(filterQueryIndex).getJSONObject("term").getJSONObject("category_id").put("boost", 0);
            }
        } else {
            jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool").getJSONArray(
                    "must").getJSONObject(queryIndex).put("match", new JSONObject());
            jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool").getJSONArray(
                    "must").getJSONObject(queryIndex).getJSONObject("match").put("name", new JSONObject());
            jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool").getJSONArray(
                    "must").getJSONObject(queryIndex).getJSONObject("match").getJSONObject("name").put("query", keyword);
            jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool").getJSONArray(
                    "must").getJSONObject(queryIndex).getJSONObject("match").getJSONObject("name").put("boost", 0.1);
        }

        queryIndex++;
        //构建第二个query的条件
        jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool").getJSONArray(
                "must").add(new JSONObject());
        jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool").getJSONArray(
                "must").getJSONObject(queryIndex).put("term", new JSONObject());
        jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool").getJSONArray(
                "must").getJSONObject(queryIndex).getJSONObject("term").put("seller_disabled_flag", 0);

        // 标签筛选
        if (tags != null) {
            queryIndex++;
            jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool").getJSONArray("must").add(new JSONObject());
            jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool").getJSONArray("must").getJSONObject(queryIndex).put("term", new JSONObject());
            jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool").getJSONArray("must").getJSONObject(queryIndex).getJSONObject("term").put("tags", tags);
        }

        // 分类筛选
        if (categoryId != null) {
            queryIndex++;
            jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool").getJSONArray("must").add(new JSONObject());
            jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool").getJSONArray("must").getJSONObject(queryIndex).put("term", new JSONObject());
            jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool").getJSONArray("must").getJSONObject(queryIndex).getJSONObject("term").put("category_id", categoryId);
        }

        //构建functions部分
        jsonRequestObj.getJSONObject("query").getJSONObject("function_score").put("functions", new JSONArray());

        // functions 里面的偏移量
        int functionIndex = 0;

        // 构建 functions 里面的数组
        if (orderby == null) {
            // 构建 guess 衰减函数
            jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONArray("functions").add(new JSONObject());
            jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONArray("functions").getJSONObject(functionIndex).put("gauss", new JSONObject());
            jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONArray("functions").getJSONObject(functionIndex).getJSONObject("gauss").put("location", new JSONObject());
            jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONArray("functions").getJSONObject(functionIndex).getJSONObject("gauss").getJSONObject("location").put("origin", latitude.toString() + "," + longitude.toString());
            jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONArray("functions").getJSONObject(functionIndex).getJSONObject("gauss").getJSONObject("location").put("scale", "100km");
            jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONArray("functions").getJSONObject(functionIndex).getJSONObject("gauss").getJSONObject("location").put("offset", "0km");
            jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONArray("functions").getJSONObject(functionIndex).getJSONObject("gauss").getJSONObject("location").put("decay", "0.5");
            jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONArray("functions").getJSONObject(functionIndex).put("weight", 9);

            // 偏移量自增
            functionIndex++;
            // 构建评分权重
            jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONArray("functions").add(new JSONObject());
            jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONArray("functions").getJSONObject(functionIndex).put("field_value_factor", new JSONObject());
            jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONArray("functions").getJSONObject(functionIndex).getJSONObject("field_value_factor").put("field", "remark_score");
            jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONArray("functions").getJSONObject(functionIndex).put("weight", 3);

            // 偏移量自增
            functionIndex++;
            // 构建评分权重
            jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONArray("functions").add(new JSONObject());
            jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONArray("functions").getJSONObject(functionIndex).put("field_value_factor", new JSONObject());
            jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONArray("functions").getJSONObject(functionIndex).getJSONObject("field_value_factor").put("field", "seller_remark_score");
            jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONArray("functions").getJSONObject(functionIndex).put("weight", 0.1);

            if (partOfSpeech.keySet().size() > 0 && isAffectOrder) {
                for (String key : partOfSpeech.keySet()) {
                    functionIndex++;
                    jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONArray("functions").add(new JSONObject());
                    jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONArray("functions").getJSONObject(functionIndex).put("filter", new JSONObject());
                    jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONArray("functions").getJSONObject(functionIndex).getJSONObject("filter").put("term", new JSONObject());
                    jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONArray("functions").getJSONObject(functionIndex).getJSONObject("filter").getJSONObject("term").put("category_id", partOfSpeech.get(key));
                    jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONArray("functions").getJSONObject(functionIndex).put("weight", 3);
                }
            }

            // function_score 的计算方式
            jsonRequestObj.getJSONObject("query").getJSONObject("function_score").put("score_mode", "sum");
            jsonRequestObj.getJSONObject("query").getJSONObject("function_score").put("boost_mode", "sum");
        } else {
            jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONArray("functions").add(new JSONObject());
            jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONArray("functions").getJSONObject(functionIndex).put("field_value_factor", new JSONObject());
            jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONArray("functions").getJSONObject(functionIndex).getJSONObject("field_value_factor").put("field", "price_per_man");


            jsonRequestObj.getJSONObject("query").getJSONObject("function_score").put("score_mode", "sum");
            jsonRequestObj.getJSONObject("query").getJSONObject("function_score").put("boost_mode", "replace");
        }

        // 构建排序字段
        jsonRequestObj.put("sort", new JSONArray());
        jsonRequestObj.getJSONArray("sort").add(new JSONObject());
        jsonRequestObj.getJSONArray("sort").getJSONObject(0).put("_score", new JSONObject());
        if (orderby == null) {
            jsonRequestObj.getJSONArray("sort").getJSONObject(0).getJSONObject("_score").put("order", "desc");
        } else {
            jsonRequestObj.getJSONArray("sort").getJSONObject(0).getJSONObject("_score").put("order", "asc");
        }

        // 构建聚合字段
        jsonRequestObj.put("aggs", new JSONObject());
        jsonRequestObj.getJSONObject("aggs").put("group_by_tags", new JSONObject());
        jsonRequestObj.getJSONObject("aggs").getJSONObject("group_by_tags").put("terms", new JSONObject());
        jsonRequestObj.getJSONObject("aggs").getJSONObject("group_by_tags").getJSONObject("terms").put("field", "tags");

        // 构建 JSON 字符串
        String requestJson = jsonRequestObj.toJSONString();

        System.out.println(requestJson);
        request.setJsonEntity(requestJson);
        Response response = restHighLevelClient.getLowLevelClient().performRequest(request);
        String responseStr = EntityUtils.toString(response.getEntity());
        System.out.println(responseStr);
        JSONObject jsonObject = JSONObject.parseObject(responseStr);
        JSONArray jsonArray = jsonObject.getJSONObject("hits").getJSONArray("hits");
        List<ShopModel> shopModelList = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObj = jsonArray.getJSONObject(i);
            Integer id = new Integer(jsonObj.get("_id").toString());
            BigDecimal distanc = new BigDecimal(jsonObj.getJSONObject("fields").getJSONArray("distance").get(0).toString());
            ShopModel shopModel = get(id);
            // 将距离数转换为米之后向上取整
            // shopModel.setDistance(distanc.setScale(0, BigDecimal.ROUND_CEILING).intValue() * 1000);
            shopModel.setDistance(distanc.multiply(new BigDecimal(1000).setScale(0, BigDecimal.ROUND_CEILING)).intValue());
            shopModelList.add(shopModel);
        }

        // 获取 tags 字段
        List<Map> tagsList = new ArrayList<>();
        JSONArray tagsJsonArray = jsonObject.getJSONObject("aggregations").getJSONObject("group_by_tags").getJSONArray("buckets");
        for (int i = 0; i < tagsJsonArray.size(); i++) {
            JSONObject jsonObj = tagsJsonArray.getJSONObject(i);
            Map<String, Object> tagMap = new HashMap<>();
            tagMap.put("tags", jsonObj.getString("key"));
            tagMap.put("num", jsonObj.getInteger("doc_count"));
            tagsList.add(tagMap);
        }
        result.put("tags", tagsList);

        result.put("shop", shopModelList);

        return result;
    }

    /**
     * 解析词性相关性召回
     *
     * @param keyword 关键词
     * @return 词性相关
     * @throws IOException IO 异常
     */
    private Map<String, Object> analyzeCategoryKeyword(String keyword) throws IOException {
        Map<String, Object> result = new HashMap<>();

        Request request = new Request("GET", "/shop/_analyze");
        request.setJsonEntity("{" + "  \"field\": \"name\"," + "  \"text\":\"" + keyword + "\"\n" + "}");
        Response response = restHighLevelClient.getLowLevelClient().performRequest(request);
        String responseStr = EntityUtils.toString(response.getEntity());
        JSONObject jsonObject = JSONObject.parseObject(responseStr);
        JSONArray jsonArray = jsonObject.getJSONArray("tokens");
        for (int i = 0; i < jsonArray.size(); i++) {
            String token = jsonArray.getJSONObject(i).getString("token");
            Integer categoryId = getCategoryIdByToken(token);
            if (categoryId != null) {
                result.put(token, categoryId);
            }
        }

        return result;
    }

    /**
     * 根据 token 获取分类 ID
     *
     * @param token 搜索字段
     * @return 类目 ID
     */
    private Integer getCategoryIdByToken(String token) {
        for (Integer key : categoryWorkMap.keySet()) {
            List<String> tokenList = categoryWorkMap.get(key);
            if (tokenList.contains(token)) {
                return key;
            }
        }
        return null;
    }

    /**
     * Integer 表示服务类目 ID
     * List<String> 表示该服务类目 ID 下所有的相关性词语
     */
    private Map<Integer, List<String>> categoryWorkMap = new HashMap<>();

    /**
     * 构造分词函数识别器
     */
    @PostConstruct
    public void init() {
        categoryWorkMap.put(1, new ArrayList<>());
        categoryWorkMap.put(2, new ArrayList<>());

        categoryWorkMap.get(1).add("吃饭");
        categoryWorkMap.get(1).add("下午茶");

        categoryWorkMap.get(2).add("休息");
        categoryWorkMap.get(2).add("睡觉");
        categoryWorkMap.get(2).add("住宿");
    }
}
