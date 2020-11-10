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
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.common.xcontent.json.JsonXContent;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Herbert on 2019/1/28.
 */
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
    public void search() throws IOException {
        SearchRequest rq = new SearchRequest();
        rq.indices("movies");
        SearchSourceBuilder sb = new SearchSourceBuilder();
        rq.source(sb);
        sb.from(0);
        sb.size(10);
        SearchResponse search = client.search(rq);
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

    public static void main(String ags[]) {
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("id", "2");
        map1.put("user1", "bbherbert1");
        map1.put("postDate", "2018-08-30");
        map1.put("username", "aa");
        map1.put("message", "message");
        Map<String, Object> map2 = new HashMap<String, Object>();
        map2.put("id", "3");
        map2.put("user2", "bbherbert1");
        map2.put("postDate", "2018-08-30");
        map2.put("username", "aa");
        map2.put("message", "message");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", "1");
        map.put("user", "bbherbert1");
        map.put("postDate", "2018-08-30");
        map.put("username", "aa");
        map.put("message", "message");

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        list.add(map);
        list.add(map1);
        list.add(map2);
        ESUtil esUtil = new ESUtil();
        esUtil.bulkDate("book15", "boo", "id", list);
//        Map<String,Object> map = new HashMap<String, Object>();
//        map.put("user","herbert1");
//        map.put("postDate","2018-08-30");
//        map.put("username","aa");
//        map.put("message","message");
//        String jsonString = JSON.toJSONString(map);
//        esUtil.addData("hh","d","4",jsonString);
//        esUtil.addData("hh","d","4","{" +
//                "\"user\":\"kimchy\"," +
//                "\"postDate\":\"2013-01-30\"," +
//                "\"username\":\"zhangsan\"," +
//                "\"message\":\"trying out Elasticsearch\"" +
//                "}");
    }

}


