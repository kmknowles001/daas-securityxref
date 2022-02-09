package com.daas.securityxref;


import com.daas.securityxref.repository.SecurityXRefRepository;
import com.daas.securityxref.common.SecurityXRef;

import org.json.simple.JSONObject;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class SecurityXrefApplicationTests {

    //
    // test repo with a simple value and key search.
    //
    @Test
    void TestGetTestSecurityXRefByValueAndKey() {

        final String ISIN_CODE = "TEST_ISIN";
        final String ISIN_FIELD_NAME = "isinCode";

        SecurityXRefRepository repo = new SecurityXRefRepository();
        SecurityXRef testXRefRecord = repo.getById(ISIN_FIELD_NAME, ISIN_CODE);

        if (testXRefRecord != null) {
            if (testXRefRecord.getIsinCode().equals(ISIN_CODE)) {
                Assert.assertTrue("Returned correct Security Xref record", true);
            }
        } else {
            Assert.assertTrue("Record not found as expected.", false);
        }
    }

    //
    // test repo with a JSON SecurityXRef JSON i.e. multiple value search
    //
    @Test
    void TestGetTestSecurityXRefByJSON() {

        // create test JSON
        JSONObject jsonSecXRefTest = new JSONObject();
        jsonSecXRefTest.put("exchange","XTEST");
        jsonSecXRefTest.put("type","test-security-type");

        // search and check return
        SecurityXRefRepository repo = new SecurityXRefRepository();
        SecurityXRef rtn = repo.getById(jsonSecXRefTest.toJSONString());

        if (rtn !=null){
            if(rtn.getExchange().equals("XTEST")){
                Assert.assertTrue("Returned correct Security Xref record", true);
            }
        }else{
            Assert.assertTrue("Failed to return correct Security Xref record", false);
        }
    }

    @Test
    void TestRepoGetAll()
    {
        SecurityXRefRepository repo = new SecurityXRefRepository();
        List<SecurityXRef> returnList = repo.getAll();
        if (returnList != null){
            if (returnList.size()>0){
                Assert.assertTrue("At least 1 record has been found. Total number of records found:" + returnList.size(), true);
            }
        } else {
            Assert.assertTrue("No records found", false);
        }
    }
}