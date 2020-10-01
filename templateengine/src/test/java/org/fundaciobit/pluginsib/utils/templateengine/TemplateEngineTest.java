/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fundaciobit.pluginsib.utils.templateengine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author gdeignacio
 */
public class TemplateEngineTest {
    
   
    private Map<String, Object> parameters;
    private Map<String, String> templates;
    
    public Map<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    public Map<String, String> getTemplates() {
        return templates;
    }

    public void setTemplates(Map<String, String> templates) {
        this.templates = templates;
    }
    
    public TemplateEngineTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        
        Foo foo = new Foo();
        foo.setBar(12345);

        this.setParameters(new HashMap<String, Object>());
        this.getParameters().put("foo", foo);

    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of processExpressionLanguage method, of class TemplateEngine.
     */
    @Test
    public void testProcessExpressionLanguage_String_Map() throws Exception {
        System.out.println("processExpressionLanguage");
        
        this.setTemplates(new HashMap<String, String>());
        this.getTemplates().put("${foo.bar}", "12.345");
        this.getTemplates().put("${foo.bar?c}", "12345");
        
        for (String key:this.getTemplates().keySet()){
            String result =  TemplateEngine.processExpressionLanguage(key, this.getParameters());
            String expResult = (String)this.getTemplates().get(key);
            System.out.println("Result[" + key + "]: " + result);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of processExpressionLanguageSquareBrackets method, of class TemplateEngine.
     */
    
    @Test
    public void testProcessExpressionLanguageSquareBrackets_String_Map() throws Exception {
        System.out.println("processExpressionLanguageSquareBrackets");
        
        this.setTemplates(new HashMap<String, String>());
        this.getTemplates().put("[=foo.bar]", "12.345");
        this.getTemplates().put("[=foo.bar?c]", "12345");
        
        for (String key:this.getTemplates().keySet()){
            String result =  TemplateEngine.processExpressionLanguageSquareBrackets(key, this.getParameters());
            String expResult = this.getTemplates().get(key);
            System.out.println("Result[" + key + "]: " + result);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of processExpressionLanguage method, of class TemplateEngine.
     */
    @Test
    public void testProcessExpressionLanguage_3args() throws Exception {
        System.out.println("processExpressionLanguage");

        this.setTemplates(new HashMap<String, String>());
        this.getTemplates().put("${foo.bar}", "12.345");
        this.getTemplates().put("${foo.bar?c}", "12345");

        for (String key : this.getTemplates().keySet()) {
            String result = TemplateEngine.processExpressionLanguage(key, this.getParameters(), null);
            String expResult = (String) this.getTemplates().get(key);
            System.out.println("Result[" + key + "]: " + result);
            assertEquals(expResult, result);
        }
    }
    

    /**
     * Test of processExpressionLanguageSquareBrackets method, of class TemplateEngine.
     */
    @Test
    public void testProcessExpressionLanguageSquareBrackets_3args() throws Exception {
        System.out.println("processExpressionLanguageSquareBrackets");

        this.setTemplates(new HashMap<String, String>());
        this.getTemplates().put("[=foo.bar]", "12.345");
        this.getTemplates().put("[=foo.bar?c]", "12345");

        for (String key : this.getTemplates().keySet()) {
            String result = TemplateEngine.processExpressionLanguageSquareBrackets(key, this.getParameters(), null);
            String expResult = this.getTemplates().get(key);
            System.out.println("Result[" + key + "]: " + result);
            assertEquals(expResult, result);
        }
    }
    

    /**
     * Test of internalProcessExpressionLanguage method, of class TemplateEngine.
     */
    
    @Test
    public void testInternalProcessExpressionLanguage() throws Exception {
        System.out.println("internalProcessExpressionLanguage");
        
        this.setTemplates(new HashMap<String, String>());
        this.getTemplates().put("${foo.bar}", "12.345");
        this.getTemplates().put("${foo.bar?c}", "12345");

        for (String key : this.getTemplates().keySet()) {
            String result = TemplateEngine.internalProcessExpressionLanguage(key, this.getParameters(), null, false);
            String expResult = (String) this.getTemplates().get(key);
            System.out.println("Result[" + key + "]: " + result);
            assertEquals(expResult, result);
        }
        
        this.setTemplates(new HashMap<String, String>());
        this.getTemplates().put("[=foo.bar]", "12.345");
        this.getTemplates().put("[=foo.bar?c]", "12345");

        for (String key : this.getTemplates().keySet()) {
            String result = TemplateEngine.internalProcessExpressionLanguage(key, this.getParameters(), null, true);
            String expResult = this.getTemplates().get(key);
            System.out.println("Result[" + key + "]: " + result);
            assertEquals(expResult, result);
        }
     
    }

    
}
