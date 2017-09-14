String url = ControllerURLConstants.MEDICATION_ENDPOINT + "/medication-instance/list/" + patient.getId();

        MockHttpServletRequestBuilder getRequest = buildRequest(RequestMethod.GET, url);

        String getResult = mvc.perform(getRequest)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse().getContentAsString();

        JSONArray medInstanceJsonList = new JSONObject(getResult).getJSONArray("payload");
        Assert.isTrue(medInstanceJsonList.length() >= 2);
        
        
        @Test
    //@WithUserDetails(value="phpadmin@php.com", userDetailsServiceBeanName="userService")
    public void testCreateUserMedicationInstance() throws Exception {
        List<String> timesTakenString = new ArrayList<>();
        timesTakenString.add("08:00");
        medicationInstance.setPatient(null);    // pass in patient id as request param
        JSONObject json = new JSONObject(medicationInstance);
        json.getJSONObject("professional").getJSONObject("contactInfo").put("contactPreference", "email");
        json.getJSONArray("frequencies").getJSONObject(0).put("timeTakenList", timesTakenString);
        json.put("startDateTime", startDateTime.format(format));    // testing time zone fix

        String url = ControllerURLConstants.MEDICATION_ENDPOINT + "/medication-instance/" + patient.getId();

        MockHttpServletRequestBuilder postRequest = buildRequest(RequestMethod.POST, url).content(json.toString())
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);

        String postResult = mvc.perform(postRequest).andExpect(status().isOk()).andReturn().getResponse()
                .getContentAsString();

        JSONObject medicationInstanceJson = new JSONObject(postResult).getJSONObject("payload");
        Assert.isTrue(medicationInstanceJson.getLong("id") > 0);

        String confirmationMsg = new JSONObject(postResult).getString("message");
        Assert.isTrue(confirmationMsg.equals("Medication instance successfully created for the patient"));
    }
    
    
    @Test
    //@WithUserDetails(value="phpadmin@php.com", userDetailsServiceBeanName="userService")
    public void testCreateUserMedicationInstance() throws Exception {
        List<String> timesTakenString = new ArrayList<>();
        timesTakenString.add("08:00");
        medicationInstance.setPatient(null);    // pass in patient id as request param
        JSONObject json = new JSONObject(medicationInstance);
        json.getJSONObject("professional").getJSONObject("contactInfo").put("contactPreference", "email");
        json.getJSONArray("frequencies").getJSONObject(0).put("timeTakenList", timesTakenString);
        json.put("startDateTime", startDateTime.format(format));    // testing time zone fix

        String url = ControllerURLConstants.MEDICATION_ENDPOINT + "/medication-instance/" + patient.getId();

        MockHttpServletRequestBuilder postRequest = buildRequest(RequestMethod.POST, url).content(json.toString())
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);

        String postResult = mvc.perform(postRequest).andExpect(status().isOk()).andReturn().getResponse()
                .getContentAsString();

        JSONObject medicationInstanceJson = new JSONObject(postResult).getJSONObject("payload");
        Assert.isTrue(medicationInstanceJson.getLong("id") > 0);

        String confirmationMsg = new JSONObject(postResult).getString("message");
        Assert.isTrue(confirmationMsg.equals("Medication instance successfully created for the patient"));
    }
    
    
     @Test
    public void testGenerateMedicationCalendarViewList() throws Exception {
        medicationService.createOrUpdateMedicationInstance(medicationInstance);

        for (int i = 0; i < 7; i++) {
            medicationService.createMedicationHistory(medicationInstance.getId(), ZonedDateTime.now().plusDays(i));
        }

        String url = ControllerURLConstants.MEDICATION_ENDPOINT
                + "/medication-calendar-view"
                + "?patientId=" + patient.getId()
                + "&startDate=" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                + "&endDate=" + LocalDate.now().plusDays(6).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        MockHttpServletRequestBuilder getRequest = buildRequest(RequestMethod.GET, url)
                .accept(MediaType.APPLICATION_JSON);

        String getResult = mvc.perform(getRequest).andExpect(status().isOk()).andReturn().getResponse()
                .getContentAsString();

        JSONArray medicationCalendarView = new JSONObject(getResult).getJSONArray("payload");

        Assert.notNull(medicationCalendarView);
        Assert.isTrue(medicationCalendarView.length() >= 2);

        Assert.isTrue(medicationCalendarView.getJSONObject(0).getInt("amountTaken") == 1);
        //Assert.isTrue(medicationCalendarView.getJSONObject(0).getString("date").equals(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));
    }
