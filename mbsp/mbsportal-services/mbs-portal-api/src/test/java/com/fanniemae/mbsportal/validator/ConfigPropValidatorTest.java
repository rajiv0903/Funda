package com.fanniemae.mbsportal.validator;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.fanniemae.mbsportal.api.po.ConfigPropPO;
import com.fanniemae.mbsportal.api.validator.ConfigPropValidator;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.utils.exception.MBSBusinessException;

@RunWith(MockitoJUnitRunner.class)
public class ConfigPropValidatorTest {

    private ConfigPropValidator<TransformationObject> configPropValidator;
    private ConfigPropPO configPO;
    
    /**
     * 
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        
        configPropValidator = new ConfigPropValidator<TransformationObject>();
        createConfigPO();
    }
    
    /**
     * 
     * @throws Exception
     */
    @Test
    public void validate_Success() throws Exception {
        
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(configPO);
        configPropValidator.validate(transformationObject);
        ConfigPropPO configPropPORet = (ConfigPropPO) transformationObject.getSourcePojo();
        assertEquals(configPO.getKey(), configPropPORet.getKey());
    }
    /**
     * 
     * @throws Exception
     */
    @Test(expected=MBSBusinessException.class)
    public void validate_Null_Config_Failure() throws Exception {
        
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(null);
        configPropValidator.validate(transformationObject);
        ConfigPropPO configPropPORet = (ConfigPropPO) transformationObject.getSourcePojo();
        assertEquals(configPO.getKey(), configPropPORet.getKey());
    }
    /**
     * 
     * @throws Exception
     */
    @Test(expected=MBSBusinessException.class)
    public void validate_Empty_Key_Failure() throws Exception {
        
        TransformationObject transformationObject = new TransformationObject();
        configPO.setKey(null);
        transformationObject.setSourcePojo(configPO);
        configPropValidator.validate(transformationObject);
        ConfigPropPO configPropPORet = (ConfigPropPO) transformationObject.getSourcePojo();
        assertEquals(configPO.getKey(), configPropPORet.getKey());
    }
    
    /**
     * 
     * @throws Exception
     */
    @Test(expected=MBSBusinessException.class)
    public void validate_Empty_Value_Failure() throws Exception {
        
        TransformationObject transformationObject = new TransformationObject();
        configPO.setValue(null);
        transformationObject.setSourcePojo(configPO);
        configPropValidator.validate(transformationObject);
        ConfigPropPO configPropPORet = (ConfigPropPO) transformationObject.getSourcePojo();
        assertEquals(configPO.getKey(), configPropPORet.getKey());
    }
    
    /**
     * 
     * @throws Exception
     */
    @Test(expected=MBSBusinessException.class)
    public void validate_Empty_Parent_Failure() throws Exception {
        
        TransformationObject transformationObject = new TransformationObject();
        configPO.setParent(null);
        transformationObject.setSourcePojo(configPO);
        configPropValidator.validate(transformationObject);
        ConfigPropPO configPropPORet = (ConfigPropPO) transformationObject.getSourcePojo();
        assertEquals(configPO.getKey(), configPropPORet.getKey());
    }
    
    /**
     * 
     * @throws Exception
     */
    @Test(expected=MBSBusinessException.class)
    public void validate_Empty_DataType_Failure() throws Exception {
        
        TransformationObject transformationObject = new TransformationObject();
        configPO.setDataType(null);
        transformationObject.setSourcePojo(configPO);
        configPropValidator.validate(transformationObject);
        ConfigPropPO configPropPORet = (ConfigPropPO) transformationObject.getSourcePojo();
        assertEquals(configPO.getKey(), configPropPORet.getKey());
    }
    
    /**
     * 
     */
    private void createConfigPO(){
        configPO  = new ConfigPropPO();
        configPO.setKey("key");
        configPO.setValue("value");
        configPO.setParent("C");
        configPO.setDataType("String");
    }
}
