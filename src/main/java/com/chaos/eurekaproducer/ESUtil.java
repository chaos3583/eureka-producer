package com.chaos.eurekaproducer;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentParser;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.*;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.*;

/**
 * @program: eureka-producer
 * * @description:
 * * @author: liaopeng
 * * @create: 2020-11-20 15:42
 **/
public class ESUtil {

    protected RestHighLevelClient client;

    /**
     * Java High Level REST Client  初始化
     */
    public ESUtil() {
        client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("localhost", 9200, "http")));
    }

    /**
     * 新增，修改文档
     * 采用json串的方式
     * @param indexName 索引
     * @param type      mapping type
     * @param id        文档id
     * @param jsonStr   文档数据
     */
    public void addData(String indexName, String type, String id, String jsonStr) {
        try {
            // 1、创建索引请求  //索引  // mapping type  //文档id
            IndexRequest request = new IndexRequest(indexName, type, id);     //文档id
            // 2、准备文档数据
            // 直接给JSON串
            request.source(jsonStr, XContentType.JSON);
            //4、发送请求
            IndexResponse indexResponse = null;
            try {
                // 同步方式
                indexResponse = client.index(request);
            } catch (ElasticsearchException e) {
                // 捕获，并处理异常
                //判断是否版本冲突、create但文档已存在冲突
                if (e.status() == RestStatus.CONFLICT) {
                    System.out.println("冲突了，请在此写冲突处理逻辑！" + e.getDetailedMessage());
                }
            }
            //5、处理响应
            if (indexResponse != null) {
                String index1 = indexResponse.getIndex();
                String type1 = indexResponse.getType();
                String id1 = indexResponse.getId();
                long version1 = indexResponse.getVersion();
                if (indexResponse.getResult() == DocWriteResponse.Result.CREATED) {
                    System.out.println("新增文档成功!" + index1 + type1 + id1 + version1);
                } else if (indexResponse.getResult() == DocWriteResponse.Result.UPDATED) {
                    System.out.println("修改文档成功!");
                }
                // 分片处理信息
                ReplicationResponse.ShardInfo shardInfo = indexResponse.getShardInfo();
                if (shardInfo.getTotal() != shardInfo.getSuccessful()) {
                    System.out.println("分片处理信息.....");
                }
                // 如果有分片副本失败，可以获得失败原因信息
                if (shardInfo.getFailed() > 0) {
                    for (ReplicationResponse.ShardInfo.Failure failure : shardInfo.getFailures()) {
                        String reason = failure.reason();
                        System.out.println("副本失败原因：" + reason);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 批量插入ES
     *
     * @param indexName 索引
     * @param type      类型
     * @param idName    id名称
     * @param list      数据集合
     */
    public void bulkDate(String indexName, String type, String idName, List<Map<String, Object>> list) {
        try {

            if (null == list || list.size() <= 0) {
                return;
            }
            if (StringUtils.isBlank(indexName) || StringUtils.isBlank(idName) || StringUtils.isBlank(type)) {
                return;
            }
            BulkRequest request = new BulkRequest();
            for (Map<String, Object> map : list) {
                if (map.get(idName) != null) {
                    request.add(new IndexRequest(indexName, type, String.valueOf(map.get(idName)))
                            .source(map, XContentType.JSON));
                }
            }
            // 2、可选的设置
               /*
       request.timeout("2m");
       request.setRefreshPolicy("wait_for");
       request.waitForActiveShards(2);
       */
            //3、发送请求
            // 同步请求
            BulkResponse bulkResponse = client.bulk(request);
            //4、处理响应
            if (bulkResponse != null) {
                for (BulkItemResponse bulkItemResponse : bulkResponse) {
                    DocWriteResponse itemResponse = bulkItemResponse.getResponse();

                    if (bulkItemResponse.getOpType() == DocWriteRequest.OpType.INDEX
                            || bulkItemResponse.getOpType() == DocWriteRequest.OpType.CREATE) {
                        IndexResponse indexResponse = (IndexResponse) itemResponse;
                        //TODO 新增成功的处理
                        System.out.println("新增成功,{}" + indexResponse.toString());
                    } else if (bulkItemResponse.getOpType() == DocWriteRequest.OpType.UPDATE) {
                        UpdateResponse updateResponse = (UpdateResponse) itemResponse;
                        //TODO 修改成功的处理
                        System.out.println("修改成功,{}" + updateResponse.toString());
                    } else if (bulkItemResponse.getOpType() == DocWriteRequest.OpType.DELETE) {
                        DeleteResponse deleteResponse = (DeleteResponse) itemResponse;
                        //TODO 删除成功的处理
                        System.out.println("删除成功,{}" + deleteResponse.toString());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * get
     * @param index
     * @param type
     * @param id
     * @return
     * @throws IOException
     */
    public Map<String, Object> getDocumentById(String index, String type, String id) throws IOException {
        GetRequest getRequest = new GetRequest(index, type, id);
        GetResponse response = client.get(getRequest);
        boolean exists = response.isExists();
        Map<String, Object> sourceAsMap = response.getSourceAsMap();
        return sourceAsMap;

    }

    /**
     * search
     * @throws IOException
     */
    public SearchHits searchByCondition(Query query) throws IOException {
        SearchRequest rq = new SearchRequest();
        rq.indices("storetransactionlog");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        //boolQuery有四个方法：
        // must 相当于 与 & = 会进行评分；must not 相当于 非 ~   ！=；
        // should 相当于 或  |   or ；
        // filter  过滤，同must不过不会评分，效率好一点
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

        termQuery(query,boolQuery);//精确匹配
        wildcardQuery(query,boolQuery);//模糊匹配
        multiMatchQuery(query,boolQuery);//多字段精确匹配
        moreLikeThisQuery(query,searchSourceBuilder);//多字段模糊匹配
        rangeQuery(query,boolQuery);//范围查询
        sort(query,searchSourceBuilder);//排序
        page(query,searchSourceBuilder);//分页

        searchSourceBuilder.query(boolQuery);
        rq.source(searchSourceBuilder);
        SearchResponse search = client.search(rq);

        if (search.status() != RestStatus.OK || search.getHits().getTotalHits() <= 0) {
             return null;
        }
        SearchHits hits = search.getHits();
        return hits;

    }

    /**
     * 分页
     * @param query
     * @param searchSourceBuilder
     */
    private void page(Query query, SearchSourceBuilder searchSourceBuilder) {
        if (query.getPageNo()>0){
            searchSourceBuilder.from((query.getPageNo()-1)*query.getSize());
            searchSourceBuilder.size(query.getSize());
        }
    }

    /**
     * 排序
     * @param query
     * @param searchSourceBuilder
     */
    private void sort(Query query, SearchSourceBuilder searchSourceBuilder) {
        if (query.getSortCondition()!=null && query.getSortCondition().size()>0){
            query.getSortCondition().forEach(sort->{
                searchSourceBuilder.sort(sort);
            });
        }
    }

    /**
     * 时间范围查询条件
     * @param query
     * @param boolQuery
     */
    private void rangeQuery(Query query, BoolQueryBuilder boolQuery) {
        if (query.getRangeCondition()!=null && query.getRangeCondition().size()>0){
            query.getRangeCondition().forEach((k,v)->{
                Map<String,String> timeMap = (Map<String,String>) v;
                RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery(k);
                rangeQueryBuilder.gte(timeMap.get("startTime"));
                rangeQueryBuilder.lte(timeMap.get("endTime"));
                boolQuery.filter(rangeQueryBuilder);
            });
        }
    }

    /**
     * 多字段模糊匹配
     * @param query
     * @param searchSourceBuilder
     */
    private void moreLikeThisQuery(Query query, SearchSourceBuilder searchSourceBuilder) {
        if (query.getDimMultiCondition()!=null && query.getDimMultiCondition().size()>0){
            query.getMultiCondition().forEach((k,v)->{
                String[] fields = v.toString().split(",");
                MoreLikeThisQueryBuilder moreLikeThisQueryBuilder = QueryBuilders.moreLikeThisQuery(fields,new String[]{k},null);
                moreLikeThisQueryBuilder.analyzer("ik_max_word");
                System.out.println(moreLikeThisQueryBuilder.analyzer());
                moreLikeThisQueryBuilder.minTermFreq(1);
                searchSourceBuilder.query(moreLikeThisQueryBuilder);
            });
        }
    }

    /**
     * 输入一个条件匹配多个字段
     * @param query
     * @param boolQuery
     */
    private void multiMatchQuery(Query query, BoolQueryBuilder boolQuery) {
        if (query.getMultiCondition()!=null && query.getMultiCondition().size()>0){
            query.getMultiCondition().forEach((k,v)->{
                String[] fields = v.toString().split(",");
                MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(k, fields);
                boolQuery.filter(multiMatchQueryBuilder);
            });
        }
    }

    /**
     * 模糊匹配条件
     */
    private void wildcardQuery(Query query, BoolQueryBuilder boolQuery) {
        if (query.getDimCondition()!=null && query.getDimCondition().size()>0){
            query.getDimCondition().forEach((k,v)->{
                WildcardQueryBuilder wildcardQueryBuilder = QueryBuilders.wildcardQuery(k, "*"+v.toString()+"*");
                boolQuery.filter(wildcardQueryBuilder);
            });
        }
    }

    /**
     * 精确匹配条件
     * @param query
     * @param boolQuery
     */
    private void termQuery(Query query, BoolQueryBuilder boolQuery) {
        if (query.getExactCondition()!=null && query.getExactCondition().size()>0){
            query.getExactCondition().forEach((k,v)->{
                //matchQuery会分词
//            MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery(k, v.toString());
                //termquery不会分词
                TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery(k, v.toString());
                boolQuery.filter(termQueryBuilder);
            });
        }
    }

    /**
     * 创建索引
     * @param index
     * @throws IOException
     */
    public String createIndex(String index,String jsonStr){
        try {
            CreateIndexRequest request = new CreateIndexRequest(index);
            //设置分片，3个分片，2个副本
            request.settings(Settings.builder()
                    .put("index.number_of_shards", 3)
                    .put("index.number_of_replicas", 2));
            //设置mapping
            request.mapping("_doc",jsonStr, XContentType.JSON);
            //设置超时时间
            request.timeout("1m");
            //执行request
            CreateIndexResponse response = client.indices().create(request);
            if (response.isAcknowledged()){
                return "创建索引"+index+"成功";
            }else{
                return "创建索引"+index+"失败";
            }
        }catch (Exception e){
            e.printStackTrace();
            return "创建索引"+index+"失败";
        }
    }

    public String deleteIndex(String index){
        try {
            DeleteIndexRequest request = new DeleteIndexRequest(index);
            AcknowledgedResponse response = client.indices().delete(request);
            if (response.isAcknowledged()){
                return "删除索引"+index+"成功";
            }else{
                return "删除索引"+index+"失败";
            }
        }catch (Exception e){
            e.printStackTrace();
            return "删除索引"+index+"失败";
        }
    }


    /**
     * 添加数据方式1，XContentBuilder的方式
     * @param index
     * @return
     */
    public String addData1(String index,String id){
        try {
            XContentBuilder builder = XContentFactory.jsonBuilder();

            IndexRequest request = new IndexRequest(index, "_doc", id);
            builder.startObject()
                    .field("name", "chaos")
                    .field("age", "200")
                    .endObject();
            request.source(builder);
            IndexResponse response = client.index(request);
            System.out.println("创建索引："+response.toString());
            if (response.status().getStatus()==201){
                return "添加数据成功";
            }else{
                return "添加数据失败";
            }
        }catch (Exception e){
            e.printStackTrace();
            return "添加数据失败";
        }

    }

    /**
     * 添加数据方式2，通过json串添加
     * @param index
     * @return
     */
    public String addData2(String index,String jsonString,String id) throws IOException {
        IndexRequest request = new IndexRequest(index, "_doc",id);
        request.source(jsonString,XContentType.JSON);
        IndexResponse response = client.index(request);
        if (response.status().getStatus()==201){
            return "新增数据成功";
        }else{
            return "新增数据失败";
        }

    }

    /**
     * 添加数据方式3，hashMap的方式
     * @param index
     * @return
     */
    public String addData3(String index,Map<String,Object> map,String id) throws IOException {
        IndexRequest request = new IndexRequest(index, "_doc", id);
        request.source(map);
        IndexResponse response = client.index(request);
        if (response.status().getStatus()==201){
            return "新增数据成功";
        }else{
            return "新增数据失败";
        }
    }

    /**
     * 添加数据方式4，Object键值对的方式
     * @param index
     * @return
     */
    public String addData4(String index) throws IOException {
        IndexRequest request = new IndexRequest(index, "_doc", "1");
        request.source("name","text","age","integer");
        IndexResponse response = client.index(request);
        if (response.status().getStatus()==201){
            return "新增数据成功";
        }else{
            return "新增数据失败";
        }
    }
}


