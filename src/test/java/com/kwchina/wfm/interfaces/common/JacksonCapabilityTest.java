package com.kwchina.wfm.interfaces.common;

import java.io.IOException;

import junit.framework.Assert;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonView;
import org.junit.Test;


public class JacksonCapabilityTest {
    
    @JsonIgnoreProperties({"bar3", "bytes"})
    @JsonPropertyOrder(value={"bar", "bar2", "bar3"})
    public static class Foo  {
        private String bar="bar";
        private String bar2="bar2";
        private String bar3="bar3";

        public String getBar() {
            return bar;
        }
        public void setBar(String bar) {
            this.bar = bar;
        }
        public String getBar2() {
            return bar2;
        }
        public void setBar2(String bar2) {
            this.bar2 = bar2;
        }
        public String getBar3() {
            return bar3;
        }
        public void setBar3(String bar3) {
            this.bar3 = bar3;
        }
        public byte[] getBytes() {
            return "I AM A lot of bytes... muhahahahahahaha".getBytes();
        }
    }
    
    @JsonIgnoreProperties({"bar2", "bytes"})
    @JsonPropertyOrder(value={"bar3", "bar2", "bar"})
    public static interface FooFilter  {
    }

    @JsonIgnoreProperties({})
    @JsonPropertyOrder(value={})
    public static interface FooFilter2  {
    }
    
    static class Views {
        static class Public{}
    }

    @JsonIgnoreProperties({})
    @JsonPropertyOrder(value={})
    @JsonAutoDetect(value=JsonMethod.FIELD)
    public static class FooFilter3  {
        @JsonView(Views.Public.class) @JsonProperty String bar;
    }


    @Test
    public void iAmGoingToJackson() throws JsonGenerationException, JsonMappingException, IOException {

        /* Using no filter mixin. */
        ObjectMapper mapper = new ObjectMapper();
        Assert.assertEquals("bar and bar2 should be present, but bar3 should not", 
                "{\"bar\":\"bar\",\"bar2\":\"bar2\"}", mapper.writeValueAsString(new Foo()));
        
        
        /* Using a mixin to override the default mapping. */
        mapper = new ObjectMapper();
        mapper.getSerializationConfig().addMixInAnnotations(Foo.class, FooFilter.class);
        Assert.assertEquals("bar3 and bar should be present, but bar2 should not", 
                "{\"bar3\":\"bar3\",\"bar\":\"bar\"}", mapper.writeValueAsString(new Foo()));
        
        /* Using another filter. */
        mapper = new ObjectMapper();
        mapper.getSerializationConfig().addMixInAnnotations(Foo.class, FooFilter2.class);
        System.out.println("Filter 2::" + mapper.writeValueAsString(new Foo()));
        
        /* Working with binary JSON encoding. */
        byte[] bytes = mapper.readValue(mapper.writeValueAsString(new Foo()), Foo.class).getBytes();
        String str = new String(bytes);
        System.out.println("" + str);
        
        
        /* Using a view and a mixin filter. */
        mapper = new ObjectMapper();
        mapper.getSerializationConfig().addMixInAnnotations(Foo.class, FooFilter3.class);
        mapper.getSerializationConfig().withView(Views.Public.class);
        String str3 = mapper.writeValueAsString(new Foo());
        Assert.assertEquals("{\"bar\":\"bar\"}", str3);
        System.out.println("this is a test " + str3 + "#########");
    }

}
