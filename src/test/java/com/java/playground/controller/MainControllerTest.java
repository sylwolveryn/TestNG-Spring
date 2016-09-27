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

import java.util.UUID;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;


public class MainControllerTest {
    public static final String EMAIL = "email";
    public static final String CREDIT_CARD_INFORMATION = "credit-card-information-";
    public static final String EXPECTED_EMAIL = "EXPECTED_EMAIL@nope.com";
    @InjectMocks
    MainController mainController;
    @Mock
    Cache cache;

    private ModelAndView modelAndView;
    private UUID userGuid = UUID.randomUUID();
    private String actualCCIWithGUID = (CREDIT_CARD_INFORMATION + userGuid.toString());

    @BeforeMethod
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetdata() throws Exception {
        givenCache();
        whenUserArrivesToThePage();
        thenModelShouldPopulatedFromCache();
    }

    private void givenCache() {
        when(cache.get(anyString())).thenAnswer(mockedCacheData());
    }

    private Answer<SimpleValueWrapper> mockedCacheData() {
        return new Answer<SimpleValueWrapper>() {
            @Override
            public SimpleValueWrapper answer(InvocationOnMock invocation) throws Throwable {
                String key = (String) invocation.getArguments()[0];
                switch (key) {
                    case EMAIL:
                        return new SimpleValueWrapper(EXPECTED_EMAIL);
                    default:
                        return new SimpleValueWrapper(new Object());
                }
            }
        };
    }

    private void whenUserArrivesToThePage() {
        modelAndView = mainController.home(userGuid);
    }

    private void thenModelShouldPopulatedFromCache() {
        String actualEmail = ((SimpleValueWrapper) modelAndView.getModel().get(EMAIL)).get().toString();
        String actualCreditCardInformation = ((SimpleValueWrapper) modelAndView.getModel().get(actualCCIWithGUID)).get().toString();
        assertTrue("expected: " + EXPECTED_EMAIL + " || but was: " + actualEmail, actualEmail.equals(EXPECTED_EMAIL));

        assertNotNull(actualCreditCardInformation);
    }


}