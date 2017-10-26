package com.getabby.ap.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.getabby.ap.controller.constants.ControllerURLConstants;
import com.getabby.ap.dto.CallQueueDashboard;
import com.getabby.ap.entity.CallQueue;
import com.getabby.ap.entity.Campaign;
import com.getabby.ap.entity.Company;
import com.getabby.ap.entity.User;
import com.getabby.ap.localization.Messages;
import com.getabby.ap.rest.ApiResponse;
import com.getabby.ap.service.CallQueueService;
import com.getabby.ap.service.CampaignService;
import com.getabby.ap.service.CompanyService;
import com.getabby.ap.service.LocalizationService;
import com.getabby.ap.service.UserService;
import com.getabby.ap.util.TransmittalMessageLevel;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(ControllerURLConstants.CALL_QUEUE_ENDPOINT)
@Api(value = ControllerURLConstants.CALL_QUEUE_ENDPOINT + ": Call Queue Module",
        description = "Controller for interacting with the call queue module")
public class CallQueueController {

    private final static Logger log = (Logger) LoggerFactory.getLogger(CallQueueService.class);
    public static final String DEBUG_MSG = "%s:: %s:: companyId=%s";

    private CallQueueService callQueueService;
    private LocalizationService localizationService;
    private CompanyService companyService;
    private CampaignService campaignService;
    private UserService userService;

    @Autowired
    public CallQueueController(CallQueueService callQueueService, LocalizationService localizationService, CompanyService companyService,
            CampaignService campaignService, UserService userService) {
        this.callQueueService = callQueueService;
        this.localizationService = localizationService;
        this.companyService = companyService;
        this.campaignService = campaignService;
        this.userService = userService;
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ApiOperation(value = "searchCallQueue", notes = "Returns calls from the call queue based on search criteria")
    public ResponseEntity<ApiResponse<Page<CallQueue>>> searchCallQueue(
            @AuthenticationPrincipal User authenticatedUser,
            @RequestHeader("x-company-id") Long companyId,
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = true) Long startDate,
            @RequestParam(required = true) Long endDate,
            @RequestParam(required = false) List<Long> campaignIdList,
            Pageable pageable) {

        log.debug(String.format(DEBUG_MSG, "searchCallQueue", authenticatedUser, companyId));

        Company company = companyService.getById(companyId);
        authenticatedUser = userService.getById(authenticatedUser.getId(), company);

        Page<CallQueue> callQueuePage = callQueueService.searchCallQueue(authenticatedUser, company, campaignIdList, phoneNumber, lastName, startDate, endDate,
                pageable);

        ApiResponse<Page<CallQueue>> apiResponse;

        if (callQueuePage.getContent().isEmpty()) {
            apiResponse = new ApiResponse<Page<CallQueue>>(
                    localizationService.resolveMessage(Messages.CALLQUEUE_NO_RESULTS),
                    false,
                    TransmittalMessageLevel.I, callQueuePage);
        } else {
            apiResponse = new ApiResponse<>(callQueuePage);
        }

        return new ResponseEntity<ApiResponse<Page<CallQueue>>>(apiResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
    @ApiOperation(value = "getDashboardStatsForCallQueue", notes = "Returns total count of calls in progress and calls scheduled")
    public ResponseEntity<ApiResponse<List<CallQueueDashboard>>> getDashboardStats(
            @AuthenticationPrincipal User authenticatedUser,
            @RequestHeader("x-company-id") Long companyId) {

        log.debug(String.format(DEBUG_MSG, "getDashboardStats", authenticatedUser, companyId));

        Company company = companyService.getById(companyId);
        authenticatedUser = userService.getById(authenticatedUser.getId(), company);

        List<Campaign> campaignList = campaignService.getByCompany(authenticatedUser, company);
        List<CallQueueDashboard> dashboardStats = callQueueService.getCallQueueDashboardStats(authenticatedUser, company, campaignList);

        ApiResponse<List<CallQueueDashboard>> apiResponse = new ApiResponse<List<CallQueueDashboard>>(dashboardStats);

        return new ResponseEntity<ApiResponse<List<CallQueueDashboard>>>(apiResponse, HttpStatus.OK);
    }
}
