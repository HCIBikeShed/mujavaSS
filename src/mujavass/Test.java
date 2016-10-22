/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mujavass;

import java.util.Map;

/**
 *
 * @author Luke
 */
public class Test {
    private TestCase testCase;
    private JavaFile javaFile;
    private Result result;
    private String name;
    
    public Test(String name, TestCase testCase, JavaFile javaFile, Result result) {
        this.name = name;
        setTestCase(testCase);
        setJavaFile(javaFile);
        setResult(result);
    }
    
    public Test(Test test) {
        this.testCase = test.getTestCase();
        this.javaFile = test.getJavaFile();
        this.name = test.getName();
        this.result = test.getResult();
    }
    
    public String getName() {
        return this.name;
    }
    
    public Result getResult() {
        return this.result;
    }
    
    public TestCase getTestCase() {
        return this.testCase;
    }
    
    public JavaFile getJavaFile() {
        return this.javaFile;
    }
    
    public Map<String, Mutant> getMutants() {
        return result.getMutants();
    }
    
    public void setTestCase(TestCase testCase) {
        this.testCase = testCase;
    }

    public void setJavaFile(JavaFile javaFile) {
        this.javaFile = javaFile;
    }

    public void setResult(Result result) {
        this.result = result;
    }
}
