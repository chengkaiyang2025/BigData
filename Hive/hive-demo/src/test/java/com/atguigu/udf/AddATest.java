package com.atguigu.udf;

import junit.framework.Assert;
import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDF;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.JavaStringObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class AddATest {

//    @Test
//    public void evaluate() throws HiveException {
//        AddA addA = new AddA();
//        ObjectInspector stringOI = PrimitiveObjectInspectorFactory.javaStringObjectInspector;
//        ObjectInspector listOI = ObjectInspectorFactory.getStandardListObjectInspector(stringOI);
//        List<String> list = new ArrayList<>();
//        list.add("sdf");
//        JavaStringObjectInspector resultInspector = (JavaStringObjectInspector)addA.initialize(new ObjectInspector[]{listOI});
//        Object result = addA.evaluate(new GenericUDF.DeferredObject[]{new GenericUDF.DeferredJavaObject(list)});
//        System.out.println(resultInspector.getPrimitiveJavaObject(result));
//
//    }
}