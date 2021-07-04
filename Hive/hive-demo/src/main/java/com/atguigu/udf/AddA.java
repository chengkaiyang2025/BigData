package com.atguigu.udf;

import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDF;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDFUtils;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.apache.hadoop.io.Text;
public class AddA extends GenericUDF {
    @Override
    public ObjectInspector initialize(ObjectInspector[] objectInspectors) throws UDFArgumentException {
        if(objectInspectors.length!=1){
            throw new UDFArgumentException("参数个数不为1,请重新传参");
        }
        return new GenericUDFUtils.ReturnObjectInspectorResolver(true).get();
    }

    @Override
    public Object evaluate(DeferredObject[] arguments) throws HiveException {
        final Text inputParameter = (Text) arguments[0].get();

        // We should be returning a new object, instead of mutating the input.
        return  new Text( inputParameter.toString() + inputParameter.toString() );
    }

    @Override
    public String getDisplayString(String[] strings) {
        return null;
    }
}