package com.mtd.svc.orders.ecommerce.payment;

import com.ibm.as400.access.AS400ConnectionPool;
import com.mtd.svc.orders.ecommerce.common.submit.FileTransmitter;
import com.mtd.svc.orders.ecommerce.common.submit.OrderSubmitter;
import com.mtd.svc.orders.ecommerce.payment.cashbook.CashbookInvoiceWriter;
import com.mtd.svc.orders.ecommerce.payment.cashbook.CashbookService;
import com.mtd.svc.orders.ecommerce.payment.data.entity.PaymentErpOrderKey;
import com.mtd.svc.orders.ecommerce.payment.data.repository.ConsumerCreditCardRepository;
import com.mtd.svc.orders.ecommerce.payment.data.repository.PaymentErpOpenInvoiceRepository;
import com.mtd.svc.orders.ecommerce.payment.data.repository.PaymentErpOrderHoldRepository;
import com.mtd.svc.orders.ecommerce.payment.model.ComOrderHoldType;
import com.mtd.svc.orders.ecommerce.payment.model.InvoiceModel;
import com.mtd.svc.orders.ecommerce.payment.model.PaymentConsumerOrderModel;
import com.mtd.svc.orders.ecommerce.payment.service.*;
import com.mtd.svc.orders.ecommerce.payment.submit.PaymentServiceOrderSubmitter;
import com.mtd.svc.orders.ecommerce.payment.web.PaymentOrderController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

import java.io.File;

/**
 * Created by ncavallo on 2/1/2017.
 */
@Configuration
@EnableAutoConfiguration(exclude = PaymentModuleAutoConfig.class)
@Import({DataConfig.class})
public class TestConfig {

    @Bean
    @Profile("local")
    @Autowired
    public ComInvoiceSubmitter comInvoiceSubmitter(AS400ConnectionPool connectionPool, AS400Properties props) {
        return new ComInvoiceSubmitterProgramCall(connectionPool, props);
    }

    @Bean
    @Profile("h2")
    public ComInvoiceSubmitter noopComInvoiceSubmitter() {
        return new ComInvoiceSubmitter() {
            @Override
            public ComInvoiceSubmitResult submitInvoiceToCom(InvoiceModel invoice) {
                return ComInvoiceSubmitResult.SUCCESSFUL;
            }
        };
    }

    @Bean
    @Profile("local")
    @Autowired
    public ComOrderHoldPlacer comOrderHoldPlacer(AS400ConnectionPool connectionPool, AS400Properties props) {
        return new ComOrderHoldPlacerProgramCall(connectionPool, props);
    }

    @Bean
    @Profile("h2")
    public ComOrderHoldPlacer noopComOrderHoldPlacer() {
        return new ComOrderHoldPlacer() {
            @Override
            public ComOrderHoldPlacementResult placeOrderOnHold(PaymentErpOrderKey orderKey, ComOrderHoldType holdType) {
                return ComOrderHoldPlacementResult.SUCCESSFUL;
            }
        };
    }

    @Bean
    @Profile({"h2", "local"})
    public FileTransmitter cashbookFileTransmitter() {
        return new FileTransmitter() {
            @Override
            public void transmit(File file, String finalName) {

            }
        };
    }

    @Bean
    @Autowired
    public PaymentService paymentService(@Value("${payment-svc.location}") String paymentServiceLocation) {
        return new PaymentService(paymentServiceLocation);
    }

    @Bean
    public OrderSubmitter<PaymentConsumerOrderModel> paymentOrderSubmitter() {
        return new PaymentServiceOrderSubmitter();
    }

    @Bean
    public PaymentOrderController paymentOrderController() {
        return new PaymentOrderController();
    }

    @Bean
    public PaymentOrderService paymentOrderService() {
        return new PaymentOrderService();
    }

    @Bean
    @Autowired
    public InvoiceService invoiceService(ComInvoiceSubmitter comInvoiceSubmitter,
                                         PaymentErpOpenInvoiceRepository paymentErpOpenInvoiceRepository,
                                         ConsumerCreditCardRepository consumerCreditCardRepository,
                                         CashbookService cashbookService) {

        return new InvoiceService(comInvoiceSubmitter,
                paymentErpOpenInvoiceRepository, consumerCreditCardRepository, cashbookService);
    }

    @Bean
    @Autowired
    public CashbookService cashbookService(CashbookInvoiceWriter cashbookInvoiceWriter,
                                           @Qualifier("cashbookFileTransmitter") FileTransmitter cashbookFileTransmitter) {

        return new CashbookService(cashbookInvoiceWriter, cashbookFileTransmitter, "", "");
    }

    @Bean
    public CashbookInvoiceWriter cashbookInvoiceWriter() {
        return new CashbookInvoiceWriter();
    }

    @Bean
    @Autowired
    public PaymentErpOrderHoldService paymentErpOrderHoldService(PaymentErpOrderHoldRepository orderHoldRepo,
                                                                 ComOrderHoldPlacer comOrderHoldPlacer) {

        return new PaymentErpOrderHoldService(orderHoldRepo, comOrderHoldPlacer);
    }
}
