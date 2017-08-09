package com.mtd.svc.orders.ecommerce.payment.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mtd.svc.orders.ecommerce.payment.TestConfig;
import com.mtd.svc.orders.ecommerce.payment.model.ErpOrderModel;
import com.mtd.svc.orders.ecommerce.payment.model.OrderBatchModel;
import com.mtd.svc.orders.ecommerce.payment.model.PaymentConsumerOrderModel;
import com.mtd.svc.orders.ecommerce.payment.service.PaymentOrderService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Created by ncavallo on 2/3/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfig.class)
@WebAppConfiguration
@ActiveProfiles("h2")
public class PaymentOrderControllerTestIT {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private PaymentOrderService paymentOrderService;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void shouldReturn4xxForMissingContent() throws Exception {
        mockMvc.perform(post("/Payment/Order/OrderInfo")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldReturnOkForEmptyContent() throws Exception {
        mockMvc.perform(post("/Payment/Order/OrderInfo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new OrderBatchModel())))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnOkAndContentFromServiceForNonEmptyContent() throws Exception {
        OrderBatchModel orderBatch = testData();
        paymentOrderService.batchRetrieveOrderInfo(orderBatch.getPaymentConsumerOrders());

        Map<String, Object> expectedModel = new HashMap<>();
        expectedModel.put("content", orderBatch);
        expectedModel.put("status", "ok");

        mockMvc.perform(post("/Payment/Order/OrderInfo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testData())))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedModel)));
    }

    private OrderBatchModel testData() {
        OrderBatchModel orderBatch = new OrderBatchModel();

        List<PaymentConsumerOrderModel> paymentConsumerOrderModels = new ArrayList<>();
        orderBatch.setPaymentConsumerOrders(paymentConsumerOrderModels);

        PaymentConsumerOrderModel orderModel = new PaymentConsumerOrderModel();
        paymentConsumerOrderModels.add(orderModel);

        List<ErpOrderModel> erpOrders = new ArrayList<>();
        ErpOrderModel erpOrderModel = new ErpOrderModel();
        erpOrderModel.setErpOrderNumber("CO-23-33344");
        erpOrders.add(erpOrderModel);
        orderModel.setErpOrders(erpOrders);

        return orderBatch;
    }
}
