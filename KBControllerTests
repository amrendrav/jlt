package com.tii.kb.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.tii.kb.entity.Interaction;
import com.tii.kb.service.InteractionService;
import org.apache.commons.lang.RandomStringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.tii.kb.entity.Question;
import com.tii.kb.entity.Response;
import com.tii.kb.entity.ResponseDetail;
import com.tii.kb.service.KBService;
import com.tii.kb.util.KBBuilder;
import com.tii.kb.util.KBBuilderService;


public class KBControllerTests extends AbstractControllerTest {

    private String answerTypeJson = "{\"message\":null,\"uniqueId\":null,\"explicitDismissal\":false,\"notificationType\":null,\"userStale\":false,\"payload\":\"PERSON_ORGANIZATION\"}";
    private String tagJson = "{\"message\":null,\"uniqueId\":null,\"explicitDismissal\":false,\"notificationType\":null,\"userStale\":false,\"payload\":[{\"value\":\"president\",\"partOfSpeech\":\"NN\"},{\"value\":\"russia\",\"partOfSpeech\":\"NN\"},{\"value\":\"run\",\"partOfSpeech\":\"JJS\"}]}";

    static {
        System.setProperty("eureka.client.enabled", "false");
    }

    @Autowired
    private KBBuilderService questionBuilderService;

    private KBBuilder questionBuilder;

    @Autowired
    private InteractionService interactionService;

    private Response notFoundResponse;
    private Response response;
    private Question q1;
    private final Long campaignId = 99999L;

    @Autowired
    private RestTemplate mockRestTemplate;

    private MockRestServiceServer mockServer;

    @Autowired
    private KBService kbService;

    @Override
    public void before() throws Exception {
        mockServer = MockRestServiceServer.createServer(mockRestTemplate);
        questionBuilder = questionBuilderService.getBuilder();
        q1 = questionBuilder.withResponse(true).build("who is the president of russia", mockRestTemplate, 1, answerTypeJson, tagJson);

        response = questionBuilder.response;
        
        // create not found response for campaign
        notFoundResponse = new Response("uncategorized_no_response_found", campaignId);
        notFoundResponse.setId(-1L);
        notFoundResponse.addResponseDetailToList(new ResponseDetail("text", 0, "I'm sorry, I didn't get that"));
    }

    @After
    public void verify() {
        mockServer.verify();
    }

    @Test
    public void testGetResponseById() throws Exception {
        String url = "/response/" + response.getId();

        MockHttpServletRequestBuilder getRequest = buildRequest(RequestMethod.GET, url).contentType(MediaType.APPLICATION_JSON);

        String getResult = mvc.perform(getRequest).andExpect(status().isOk()).andReturn().getResponse()
                .getContentAsString();

        JSONObject responseJson = new JSONObject(getResult).getJSONObject("payload");
        Assert.isTrue(response.getId().equals(responseJson.getLong("id")));
        Assert.isTrue(responseJson.getString("name").equals(response.getName()));
    }

    @Test
    public void testGetResponseByIdAndHandleResponse() throws Exception {
        String url = "/response/handle/" + response.getId() + "?interactionId=";

        MockHttpServletRequestBuilder getRequest = buildRequest(RequestMethod.GET, url).contentType(MediaType.APPLICATION_JSON);

        String getResult = mvc.perform(getRequest).andExpect(status().isOk()).andReturn().getResponse()
                .getContentAsString();

        JSONObject responseJson = new JSONObject(getResult).getJSONObject("payload");
        Assert.isTrue(response.getId().equals(responseJson.getLong("id")));
        Assert.isTrue(responseJson.getString("name").equals(response.getName()));
    }

    @Test
    public void testGetResponseByNameAndHandleResponse() throws Exception {
        Interaction interaction = interactionService.getInteraction("testUniqueId", campaignId);
        Response tempResponse = new Response("temp_name_for_response", campaignId);

        tempResponse.addResponseDetailToList(new ResponseDetail("text", 0, "Hello there"));

        tempResponse = kbService.createResponse(tempResponse);

        String url = "/response/handle?name=" + URLEncoder.encode(tempResponse.getName(), "UTF-8");

        MockHttpServletRequestBuilder getRequest = buildRequest(RequestMethod.GET, url)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Interaction-Id", interaction.getId());

        String getResult = mvc.perform(getRequest).andExpect(status().isOk()).andReturn().getResponse()
                .getContentAsString();

        JSONObject responseJson = new JSONObject(getResult).getJSONObject("payload");
        Assert.isTrue(tempResponse.getId().equals(responseJson.getLong("id")));
        Assert.isTrue(responseJson.getString("name").equals(tempResponse.getName()));
    }


    @Test
    public void testGetResponseByNameAndCampaignId() throws Exception {
        String url = "/response/campaign/" + response.getCampaignId() + "?name=" + response.getName();

        MockHttpServletRequestBuilder getRequest = buildRequest(RequestMethod.GET, url).contentType(MediaType.APPLICATION_JSON);

        String getResult = mvc.perform(getRequest).andExpect(status().isOk()).andReturn().getResponse()
                .getContentAsString();

        JSONObject responseJson = new JSONObject(getResult).getJSONObject("payload");
        Assert.isTrue(response.getId().equals(responseJson.getLong("id")));
        Assert.isTrue(responseJson.getString("name").equals(response.getName()));
    }

    @Ignore
    @Test
    public void testGetResponseByQuestion() throws Exception {
        JSONObject json = new JSONObject(q1);

        String url = "/response";
        MockHttpServletRequestBuilder getRequest = buildRequest(RequestMethod.GET, url).content(json.toString())
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);

        String getResult = mvc.perform(getRequest).andExpect(status().isOk()).andReturn().getResponse()
                .getContentAsString();

        JSONObject responseJson = new JSONObject(getResult).getJSONObject("payload");
        Assert.isTrue(response.getId().equals(responseJson.getLong("id")));
        Assert.isTrue(responseJson.getString("name").equals(response.getName()));
    }

    @Test
    public void testCreateResponse() throws Exception {
        ResponseDetail rd = new ResponseDetail("title2", 0, "some string content2");
        Set<ResponseDetail> responseDetails = new HashSet<>();
        responseDetails.add(rd);
        Response response2 = new Response("response2-" + RandomStringUtils.randomAlphabetic(5));
        response2.setCampaignId(campaignId);
        response2.setResponseDetails(responseDetails);
        JSONObject json = new JSONObject(response2);

        String url = "/response";
        MockHttpServletRequestBuilder postRequest = buildRequest(RequestMethod.POST, url).content(json.toString())
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);

        String postResult = mvc.perform(postRequest).andExpect(status().isOk()).andReturn().getResponse()
                .getContentAsString();

        JSONObject responseJson = new JSONObject(postResult).getJSONObject("payload");
        Assert.isTrue(responseJson.getLong("id") > 0);
        Assert.isTrue(responseJson.getString("name").equals(response2.getName()));

        String confirmationMsg = new JSONObject(postResult).getString("message");
        Assert.isTrue(confirmationMsg.equals("Response successfully created"));
    }

    @Test
    public void testUpdateResponse() throws Exception {
        Response response2 = questionBuilder.buildResponse(true);

        String updatedName = "UpdatedResponseName:" + RandomStringUtils.randomAlphabetic(5);
        JSONObject json = new JSONObject(response2);
        json.put("name", updatedName);

        String url = "/response";
        MockHttpServletRequestBuilder postRequest = buildRequest(RequestMethod.PUT, url).content(json.toString())
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);

        String postResult = mvc.perform(postRequest).andExpect(status().isOk()).andReturn().getResponse()
                .getContentAsString();

        JSONObject responseJson = new JSONObject(postResult).getJSONObject("payload");
        Assert.isTrue(response2.getId().equals(responseJson.getLong("id")));
        Assert.isTrue(responseJson.getString("name").equals(updatedName));

        String confirmationMsg = new JSONObject(postResult).getString("message");
        Assert.isTrue(confirmationMsg.equals("Response successfully updated"));
    }

    @Test
    public void testDeleteResponse() throws Exception {
        answerTypeJson = "{\"message\":null,\"uniqueId\":null,\"explicitDismissal\":false,\"notificationType\":null,\"userStale\":false,\"payload\":\"INFORMATION\"}";
        tagJson = "{\"message\":null,\"uniqueId\":null,\"explicitDismissal\":false,\"notificationType\":null,\"userStale\":false,\"payload\":[{\"value\":\"president\",\"partOfSpeech\":\"NN\"},{\"value\":\"united states\",\"partOfSpeech\":\"NN\"},{\"value\":\"who\",\"partOfSpeech\":\"JJS\"}]}";
        Question q = questionBuilder.withResponse(true).build("who is the president of the united states", mockRestTemplate, 2, answerTypeJson, tagJson);
        Response r = q.getResponse();

        String url = "/response/" + r.getId();
        MockHttpServletRequestBuilder postRequest = buildRequest(RequestMethod.DELETE, url)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);

        String postResult = mvc.perform(postRequest).andExpect(status().isOk()).andReturn().getResponse()
                .getContentAsString();

        boolean responseJson = new JSONObject(postResult).getBoolean("payload");
        Assert.isTrue(responseJson);

        String confirmationMsg = new JSONObject(postResult).getString("message");
        Assert.isTrue(confirmationMsg.equals("Response successfully deleted"));
        mockServer.verify();
    }

    @Test
    public void testAskQuestionResultsUnderThreshold() throws Exception {
        answerTypeJson = "{\"message\":null,\"uniqueId\":null,\"explicitDismissal\":false,\"notificationType\":null,\"userStale\":false,\"payload\":\"INFORMATION\"}";
        tagJson = "{\"message\":null,\"uniqueId\":null,\"explicitDismissal\":false,\"notificationType\":null,\"userStale\":false,\"payload\":[{\"value\":\"president\",\"partOfSpeech\":\"NN\"},{\"value\":\"united states\",\"partOfSpeech\":\"NN\"},{\"value\":\"who\",\"partOfSpeech\":\"JJS\"}]}";
        Question q = questionBuilder.withResponse(true).build("who is the president of the united states", mockRestTemplate, 2, answerTypeJson, tagJson);
        Response r = q.getResponse();

        String url = "/question/ask/" + campaignId + "?question=" + q.getQuestionText();

        MockHttpServletRequestBuilder getRequest = buildRequest(RequestMethod.GET, url).contentType(MediaType.APPLICATION_JSON);

        String getResult = mvc.perform(getRequest).andExpect(status().isOk()).andReturn().getResponse()
                .getContentAsString();

        JSONObject responseJson = new JSONObject(getResult).getJSONObject("payload");
        Assert.isTrue(!r.getId().equals(responseJson.getLong("id")));
        Assert.isTrue(!responseJson.getString("name").equals(r.getName()));
        Assert.isTrue(responseJson.getString("name").equals("uncategorized_no_response_found"));
    }

    @Test
    public void testAskQuestionTypeRepeat() throws UnsupportedEncodingException, Exception {

        // get the interaction id
        String url = "/interaction?user-alias=ThisIsMyUniqueId&campaign-id=9999";
        MockHttpServletRequestBuilder postRequest = buildRequest(RequestMethod.POST, url).contentType(MediaType.APPLICATION_JSON);

        String postResult = mvc.perform(postRequest).andExpect(status().isOk()).andReturn().getResponse()
                .getContentAsString();

        Long interactionId = new JSONObject(postResult).getLong("payload");
        Assert.isTrue(interactionId != null);

        // ask first question
        answerTypeJson = "{\"message\":null,\"uniqueId\":null,\"explicitDismissal\":false,\"notificationType\":null,\"userStale\":false,\"payload\":\"PERSON_ORGANIZATION\"}";
        tagJson = "{\"message\":null,\"uniqueId\":null,\"explicitDismissal\":false,\"notificationType\":null,\"userStale\":false,\"payload\":[{\"value\":\"president\",\"partOfSpeech\":\"NN\"},{\"value\":\"united states\",\"partOfSpeech\":\"NN\"},{\"value\":\"who\",\"partOfSpeech\":\"JJS\"}]}";
        String responseRank = "{\"message\":null,\"uniqueId\":null,\"explicitDismissal\":false,\"notificationType\":null,\"userStale\":false,\"payload\":{\"response\":\"how are you\",\"rank\":\"0.1\"}}";
        String answerTypeJson2 = "{\"message\":null,\"uniqueId\":null,\"explicitDismissal\":false,\"notificationType\":null,\"userStale\":false,\"payload\":\"REPEAT\"}";
        String tagJson2 = "{\"message\":null,\"uniqueId\":null,\"explicitDismissal\":false,\"notificationType\":null,\"userStale\":false,\"payload\":[]}";
        String responseRank2 = "{\"message\":null,\"uniqueId\":null,\"explicitDismissal\":false,\"notificationType\":null,\"userStale\":false,\"payload\":{\"response\":\"how are you\",\"rank\":\"0.1\"}}";
        
        List<String> questions = new ArrayList<String>();
        questions.add("who is the president of the united states");
        questions.add("What did you say");

        List<String> answerTypeJsonList = new ArrayList<String>();
        answerTypeJsonList.add(answerTypeJson);
        answerTypeJsonList.add(answerTypeJson2);
        
        List<String> responserRankJsonList = new ArrayList<String>();
        responserRankJsonList.add(responseRank);
        responserRankJsonList.add(responseRank2);

        List<String> tagJsonList = new ArrayList<String>();
        tagJsonList.add(tagJson);
        tagJsonList.add(tagJson2);

        List<Question> questionList = questionBuilder.withResponse(true).build(questions, 2, answerTypeJsonList, tagJsonList, responserRankJsonList, mockRestTemplate, true, 1);
        List<Response> responseList = new ArrayList<Response>();
        for (Question q : questionList) {
            responseList.add(q.getResponse());
        }

        // had to call this twice because of the way test utils is
        // i have it looping through, since I needed the first call to be made twice and this one to be made only once
        // i had to just do this.  So ignore this, but its necessary
        // also order matters so I had to call one then two then one then two
        kbService.processSentenceFromClient(campaignId, "What did you say", interactionId);

        url = "/question/ask/" + campaignId + "?question=" + questionList.get(0).getQuestionText();

        MockHttpServletRequestBuilder getRequest = buildRequest(RequestMethod.GET, url).contentType(MediaType.APPLICATION_JSON).header("Interaction-Id", interactionId);

        String getResult = mvc.perform(getRequest).andExpect(status().isOk()).andReturn().getResponse()
                .getContentAsString();
        Response r = responseList.get(0);
        JSONObject responseJson = new JSONObject(getResult).getJSONObject("payload");
        Assert.isTrue(!r.getId().equals(responseJson.getLong("id")));
        Assert.isTrue(!responseJson.getString("name").equals(r.getName()));
        Assert.isTrue(responseJson.getString("name").equals("uncategorized_no_response_found"));

        // now ask second question
        String url2 = "/question/ask/" + campaignId + "?question=" + "What did you say";

        getRequest = buildRequest(RequestMethod.GET, url2).contentType(MediaType.APPLICATION_JSON).header("Interaction-Id", interactionId);

        getResult = mvc.perform(getRequest).andExpect(status().isOk()).andReturn().getResponse()
                .getContentAsString();

        JSONObject responseJson2 = new JSONObject(getResult).getJSONObject("payload");
        Assert.isTrue(!r.getId().equals(responseJson2.getLong("id")));
        Assert.isTrue(!responseJson2.getString("name").equals(r.getName()));
        Assert.isTrue(responseJson2.getString("name").equals("uncategorized_no_response_found"));

        Assert.isTrue(responseJson2.getLong("id") == responseJson.getLong("id"));

    }
    
    @Test
    public void testResponseAvailableEndpointForNonSystemResponses() throws UnsupportedEncodingException, Exception {
    	//Common setup
    	answerTypeJson = "{\"message\":null,\"uniqueId\":null,\"explicitDismissal\":false,\"notificationType\":null,\"userStale\":false,\"payload\":\"INFORMATION\"}";
        tagJson = "{\"message\":null,\"uniqueId\":null,\"explicitDismissal\":false,\"notificationType\":null,\"userStale\":false,\"payload\":[{\"value\":\"president\",\"partOfSpeech\":\"NN\"},{\"value\":\"united states\",\"partOfSpeech\":\"NN\"},{\"value\":\"who\",\"partOfSpeech\":\"JJS\"}]}";
       
    	questionBuilder.buildResponse(10503L, Boolean.TRUE);
    	UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString("/response/available/{campaignId}?responseSearchText={searchText}");
    	HashMap<String, String> uriQueryParams = new HashMap<String,String>();
    	uriQueryParams.put("campaignId", "10503");
    	uriQueryParams.put("searchText",  "");
    	String url = (uriBuilder.buildAndExpand(uriQueryParams)).toUriString();
    	
        MockHttpServletRequestBuilder getRequest = buildRequest(RequestMethod.GET, url).contentType(MediaType.APPLICATION_JSON);

        String getResult = mvc.perform(getRequest).andExpect(status().isOk()).andReturn().getResponse()
                .getContentAsString();
        
        JSONObject responsePayload = new JSONObject(getResult).getJSONObject("payload");
        JSONArray payloadContent = responsePayload.getJSONArray("content");
        for(int i=0; i<payloadContent.length(); i++) {
        	//validate that each response has 'valid' data.
        	JSONObject curr = payloadContent.getJSONObject(i);
        	Assert.isTrue(curr.getLong("id") > 0L);
            Assert.isTrue(!curr.getBoolean("system"));
            Assert.isTrue(curr.getJSONArray("responseDetails") != null);
        }
        
    }
    
    @Test
    public void testResponseSearchEndpoint() throws UnsupportedEncodingException, Exception {
    	//Common setup
    	answerTypeJson = "{\"message\":null,\"uniqueId\":null,\"explicitDismissal\":false,\"notificationType\":null,\"userStale\":false,\"payload\":\"INFORMATION\"}";
        tagJson = "{\"message\":null,\"uniqueId\":null,\"explicitDismissal\":false,\"notificationType\":null,\"userStale\":false,\"payload\":[{\"value\":\"president\",\"partOfSpeech\":\"NN\"},{\"value\":\"united states\",\"partOfSpeech\":\"NN\"},{\"value\":\"who\",\"partOfSpeech\":\"JJS\"}]}";
       
        questionBuilder.buildResponse(campaignId, true);
        
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString("/response/search/{campaignId}?");
    	HashMap<String, String> uriQueryParams = new HashMap<String,String>();
    	uriQueryParams.put("campaignId", String.valueOf(campaignId));
    	String url = (uriBuilder.buildAndExpand(uriQueryParams)).toUriString();
    	
        MockHttpServletRequestBuilder getRequest = buildRequest(RequestMethod.GET, url).contentType(MediaType.APPLICATION_JSON);
        String getResult = mvc.perform(getRequest).andExpect(status().isOk()).andReturn().getResponse()
                .getContentAsString();
        
        JSONObject responsePayload = new JSONObject(getResult).getJSONObject("payload");
        JSONArray payloadContent = responsePayload.getJSONArray("content");
        Assert.isTrue(payloadContent.length() >= 1);
        
    }
    
}
