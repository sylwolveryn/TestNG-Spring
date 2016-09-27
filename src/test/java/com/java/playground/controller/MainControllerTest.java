package com.java.playground.controller;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.web.servlet.ModelAndView;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Map;
import java.util.UUID;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class MainControllerTest {
    @InjectMocks
    MainController mainController;
    @Mock
    Cache cache;

    private UUID userGuid = UUID.randomUUID();
    private ModelAndView modelAndView;
    private static final String EXPECTED_EMAIL = "Someone@somewhere.com";
    private static final String EMAIL = "email";

    @BeforeMethod
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void modelShouldBePopulatedFromCache() {
        givenCache();
        whenUserArrivesHomePage();
        thenModelShouldPopulatedFromCache();
    }

    private void givenCache() {
        when(cache.get(anyString())).thenAnswer(mockedCacheData());
    }

    private void whenUserArrivesHomePage() {
        modelAndView = mainController.home(userGuid);
    }

    private void thenModelShouldPopulatedFromCache() {
        Map<String, Object> model = modelAndView.getModel();
        String actualEmail = (String) model.get(EMAIL);
        String actualCreditCardInformationKey = "Credit-card-information-" + userGuid.toString();
        Object actualCreditCardInformation = model.get(actualCreditCardInformationKey);

        assertTrue(actualEmail.equals(EXPECTED_EMAIL));
        assertNotNull(actualCreditCardInformation);
    }

    private Answer<SimpleValueWrapper> mockedCacheData() {
        return new Answer<SimpleValueWrapper>() {

            @Override
            public SimpleValueWrapper answer(InvocationOnMock invocationOnMock) throws Throwable {
                String key = (String) invocationOnMock.getArguments()[0];
                switch (key) {
                    case EMAIL: return new SimpleValueWrapper(EXPECTED_EMAIL);
                    default: return new SimpleValueWrapper(new Object());
                }
            }
        };
    }

}