package indi.tom.mymall01.search.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import indi.tom.mymall01.bean.SkuSearchInfo;
import indi.tom.mymall01.bean.SkuSearchParam;
import indi.tom.mymall01.bean.SkuSearchResult;
import indi.tom.mymall01.interfaces.SkuSearchService;
import io.searchbox.client.JestClient;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.core.search.aggregation.MetricAggregation;
import io.searchbox.core.search.aggregation.TermsAggregation;
import org.apache.lucene.queryparser.xml.builders.FilteredQueryBuilder;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author Tom
 * @Date 2019/11/30 18:57
 * @Version 1.0
 * @Description
 */
@Service
public class SkuSearchServiceImpl implements SkuSearchService {
    @Autowired
    JestClient jestClient;

    @Override
    public void saveSkuInfoSearch(SkuSearchInfo skuSearchInfo) {
        Index.Builder indexBuilder = new Index.Builder(skuSearchInfo);
        indexBuilder.index("mymall01_sku_info").type("_doc").id(skuSearchInfo.getId());
        Index index = indexBuilder.build();
        try {
            jestClient.execute(index);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public SkuSearchResult getSkuSearchResult(SkuSearchParam skuSearchParam) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        if(skuSearchParam.getKeyword() != null){
            //名字查询
            boolQueryBuilder.must(new MatchQueryBuilder("skuName", skuSearchParam.getKeyword()));
            //设置搜索结果中包含的搜索关键字高亮
            searchSourceBuilder.highlight(new HighlightBuilder().field("skuName").preTags("<span style='color:red'>")
                    .postTags("</span>"));

        }
        if(skuSearchParam.getCatalog3Id() != null){
            //根据三级分类过滤
            boolQueryBuilder.filter(new TermQueryBuilder("catalog3Id", skuSearchParam.getCatalog3Id()));
        }
        //平台属性过滤
        String[] valueIds = skuSearchParam.getValueId();
        if(valueIds != null && valueIds.length > 0){
            for (int i = 0; i < valueIds.length; i++) {
                String valueId = valueIds[i];
                boolQueryBuilder.filter(new TermQueryBuilder("skuAttrValueList.valueId", valueId));
            }
        }
        //创建query部分
        searchSourceBuilder.query(boolQueryBuilder);
        //创建DSL剩余部分： from, size, 聚合， 排序
        searchSourceBuilder.from((skuSearchParam.getPageNo()-1)*skuSearchParam.getPageSize());
        searchSourceBuilder.size(skuSearchParam.getPageSize());
        TermsBuilder aggregation = AggregationBuilders.terms("groupByValueId").field("skuAttrValueList.valueId").size(1000);
        searchSourceBuilder.aggregation(aggregation);

        searchSourceBuilder.sort("hotScore", SortOrder.DESC);

        Search.Builder searchBuilder = new Search.Builder(searchSourceBuilder.toString());
        Search search = searchBuilder.addIndex("mymall01_sku_info").addType("_doc").build();

        SkuSearchResult skuSearchResult = new SkuSearchResult();
        SearchResult searchResult = null;
        try {
            searchResult = jestClient.execute(search);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<SkuSearchInfo> skuSearchInfoList = new ArrayList<>();
        List<SearchResult.Hit<SkuSearchInfo, Void>> hits = searchResult.getHits(SkuSearchInfo.class);
        for (SearchResult.Hit<SkuSearchInfo, Void> hit : hits) {
            SkuSearchInfo skuSearchInfo = hit.source;
            if(hit.highlight != null) {
                String skuName = hit.highlight.get("skuName").get(0);
                skuSearchInfo.setSkuName(skuName);
            }
            skuSearchInfoList.add(skuSearchInfo);
        }
        skuSearchResult.setSkuSearchInfoList(skuSearchInfoList);
        skuSearchResult.setTotal(searchResult.getTotal());
        //计算总页数
        skuSearchResult.setTotalPages((searchResult.getTotal() + skuSearchParam.getPageSize() - 1) / skuSearchParam.getPageSize());
        //聚合部分
        List<TermsAggregation.Entry> buckets = searchResult.getAggregations().getTermsAggregation("groupByValueId").getBuckets();
        List<String> attrValueIdList = new ArrayList<>();
        for (TermsAggregation.Entry bucket : buckets) {
            attrValueIdList.add(bucket.getKey());
        }
        skuSearchResult.setAttrValueIdList(attrValueIdList);

        return skuSearchResult;
    }
}
